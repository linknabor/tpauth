package com.eshequ.hexie.tpauth.service.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eshequ.hexie.tpauth.common.Constants;
import com.eshequ.hexie.tpauth.common.WechatConfig;
import com.eshequ.hexie.tpauth.exception.AesException;
import com.eshequ.hexie.tpauth.exception.AppSysException;
import com.eshequ.hexie.tpauth.exception.BusinessException;
import com.eshequ.hexie.tpauth.service.AuthService;
import com.eshequ.hexie.tpauth.util.RestUtil;
import com.eshequ.hexie.tpauth.util.wechat.WXBizMsgCrypt;
import com.eshequ.hexie.tpauth.vo.AuthEvent;
import com.eshequ.hexie.tpauth.vo.AuthRequest;
import com.eshequ.hexie.tpauth.vo.AuthorizationInfo;
import com.eshequ.hexie.tpauth.vo.AuthorizationResp;
import com.eshequ.hexie.tpauth.vo.AuthorizerAccessToken;
import com.eshequ.hexie.tpauth.vo.ComponentAcessToken;
import com.eshequ.hexie.tpauth.vo.ComponentVerifyTicket;
import com.eshequ.hexie.tpauth.vo.PreAuthCode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Service
public class AuthServiceImpl implements AuthService{
	
	private static Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);
	
	@Value("${component.appid}")
	private String componentAppid;
	
	@Value("${component.secret}")
	private String componetSecret;
	
	@Value("${component.token}")
	private String token;
	
	@Value("${component.aeskey}")
	private String aeskey;
	
	@Value("${wechat.cache.folder:refreshToken}")
	private String cacheFolder;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private RestUtil restUtil;
	
	/**
	 * 获取第三方平台accessToken
	 */
	@Override
	public ComponentAcessToken getComponentAccessToken(String verifyTicket) {
		
		String reqUrl = WechatConfig.COMPONENT_ACCESS_TOKEN_URL;
		Map<String, String> postData = new HashMap<>();
		postData.put("component_appid", componentAppid);
		postData.put("component_appsecret", componetSecret);
		postData.put("component_verify_ticket", verifyTicket);
		
		ComponentAcessToken cat = null;
		try {
			cat = restUtil.postByJson(reqUrl, postData, ComponentAcessToken.class);
		} catch (Exception e) {	//可能post返回的是失败的结果，这样转实体会失败
			throw new BusinessException(e.getMessage(), e);
		}
		return cat;
		
	}
	
	/**
	 * 获取被授权公众号的accessToken
	 * @param authAppId
	 * @param authRefreshToken
	 * @return
	 */
	@Override
	public AuthorizerAccessToken getAuthorizerAccessToken(String authAppId, String authRefreshToken) {
		
		ComponentAcessToken componentAcessToken = getComponentAcessTokenFromCache();
		logger.info("componentAcessToken is : " + componentAcessToken);
		if (componentAcessToken == null || StringUtils.isEmpty(componentAcessToken.getComponentAcessToken())) {
			return null;
		}
		String reqUrl = String.format(WechatConfig.AUTHORIZER_ACCESS_TOKEN_URL, componentAcessToken.getComponentAcessToken());
		Map<String, String> postData = new HashMap<>();
		postData.put("component_appid", componentAppid);
		postData.put("authorizer_appid", authAppId);
		postData.put("authorizer_refresh_token", authRefreshToken);
		AuthorizerAccessToken authorizerAccessToken = restUtil.postByJson(reqUrl, postData, AuthorizerAccessToken.class);
		return authorizerAccessToken;
		
	}
	
	/**
	 * 获取预授权码
	 * 先从缓存中取，如果没有，访问微信获取。缓存设置10分钟过期。不做定时轮询的存储操作，因为这个码的使用频率很低，没必要每10分钟取一次。做缓存是为了单个用户反复刷新页面操作不用重复做http开销
	 */
	private PreAuthCode getPreAuthCode() {
		
		PreAuthCode pac = (PreAuthCode) redisTemplate.opsForValue().get(Constants.KEY_PRE_AUTH_CODE);
		
		if (pac == null || StringUtils.isEmpty(pac.getPreAuthCode())) {
			ComponentAcessToken cat = getComponentAcessTokenFromCache();
			String reqUrl = String.format(WechatConfig.PRE_AUTH_CODE_URL, cat.getComponentAcessToken());
			Map<String, String> postData = new HashMap<>();
			postData.put("component_appid", componentAppid);
			PreAuthCode preAuthCode = restUtil.postByJson(reqUrl, postData, PreAuthCode.class);
			redisTemplate.opsForValue().set(Constants.KEY_PRE_AUTH_CODE, preAuthCode);
			redisTemplate.expire(Constants.KEY_PRE_AUTH_CODE, 10, TimeUnit.MINUTES);	//设置10分钟过期
			pac = preAuthCode;
		}
		return pac;
	}

	/**
	 * 处理授权通知
	 */
	@Override
	public void handleAuthEvent(AuthRequest authRequest) throws AesException, IOException {

		String formattedXml = authRequest.getPostData().replaceAll("\r", "").replaceAll("\n", "").
				replaceAll("\r\n", "").replace("\t", "").replaceAll(" ", "");	//去换行
		
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode rootNode = xmlMapper.readTree(formattedXml);	//解析xml
		JsonNode appidNode = rootNode.path("AppId");		//appid节点
		JsonNode encryptNode = rootNode.path("Encrypt");	//取出打了码的内容部分节点
		
		String msgAppId = appidNode.asText();
		if (!componentAppid.equals(msgAppId)) {
			throw new BusinessException("invalid appid : " + msgAppId);
		}
		String encryptStr = encryptNode.asText();
		
		WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(componetSecret, aeskey, componentAppid);
		String decryptedContent = msgCrypt.decryptMsg(authRequest.getMsg_signature(), authRequest.getTimestamp(), 
				authRequest.getNonce(), encryptStr);
		
		logger.info("decrypted content : " + decryptedContent);	//解密后的内容，是个xml
		
		decryptedContent = decryptedContent.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\r\n", "").
				replaceAll("\r\n", "").replace("\t", "").replaceAll(" ", "");	//去换行
		JsonNode decryptRoot = xmlMapper.readTree(decryptedContent);
		JsonNode typeNode = decryptRoot.path("InfoType");
		if (typeNode == null) {
			throw new BusinessException("invalid message! no InfoType node !");
		}
		String infoType = typeNode.asText();
		switch (infoType) {
		case WechatConfig.EVENT_TYPE_VERIFY_TICKET:	//获取第三方授权ticket事件
			ComponentVerifyTicket ticket = xmlMapper.readValue(decryptedContent, ComponentVerifyTicket.class);
			redisTemplate.opsForValue().set(Constants.KEY_VERIFY_TICKET, ticket);
			break;
		case WechatConfig.EVENT_TYPE_AUTHORIZED:	//公众号授权确认事件
			AuthEvent authEvent = xmlMapper.readValue(decryptedContent, AuthEvent.class);
			//下面存储时用的key是oaAuth_authorizationCode_ 拼接被授权公众号appid的形式,因为可能有多个公众号的授权，所以要区分
			redisTemplate.opsForValue().set(Constants.KEY_AUTHORIZATION_CODE+authEvent.getAuthorizerAppid(), authEvent);
			redisTemplate.opsForList().rightPush(Constants.KEY_AUTH_QUEUE, authEvent);	//后续处理丢队列里,因为还要请求微信服务器耗时较长，这里先结束，然后回复微信
			break;
		case WechatConfig.EVENT_TYPE_UPDATEAUTHORIZED:	//公众号授权更新事件
			authEvent = xmlMapper.readValue(decryptedContent, AuthEvent.class);
			redisTemplate.opsForValue().set(Constants.KEY_AUTHORIZATION_CODE+authEvent.getAuthorizerAppid(), authEvent);
			redisTemplate.opsForList().rightPush(Constants.KEY_AUTH_QUEUE, authEvent);	//后续处理丢队列里,因为还要请求微信服务器耗时较长，这里先结束，然后回复微信
			break;
		default:
			logger.error("unknow info type : " + infoType);
			break;
		}
		
	}

	/**
	 * 客户端授权，返回授权链接
	 * @param	requestHead
	 * @return
	 */
	@Override
	public String clientAuth(String requestHead) {
		
		List<String> mobileDevs = Constants.mobileDevices;
		boolean isMobile = false;
		for (String dev : mobileDevs) {
			if (requestHead.indexOf(dev)>0) {
				isMobile = true;
				break;
			}
		}
		String authLink = "";
		if (isMobile) {
			authLink = WechatConfig.MOBILE_AUTH_URL;
		}else {
			authLink = WechatConfig.PC_AUTH_URL;
		}
		PreAuthCode preAuthCode = getPreAuthCode();

		try {
			authLink = authLink.replaceAll("COMPONENT_APPID", componentAppid).replaceAll("PRE_AUTH_CODE", 
					preAuthCode.getPreAuthCode()).replaceAll("REDIRECT_URI", URLEncoder.encode(WechatConfig.AUTH_REDIRECT_URI, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			throw new AppSysException(e);
		}
		return authLink;
	}
	
	/**
	 * 获取授权信息，并将里面的authorizerAccessToken缓存下来
	 */
	@Override
	public void authorizationInfo(String authorizationCode, String authorizerAppid) {

		if (StringUtils.isEmpty(authorizationCode)) {
			AuthEvent authorization = (AuthEvent) redisTemplate.opsForValue().get(Constants.KEY_AUTHORIZATION_CODE + authorizerAppid);
			if (authorization == null || StringUtils.isEmpty(authorization.getAuthorizationCode())) {
				throw new BusinessException("Cannot find authorizationCode, pls re-authorize !");
			}
			authorizationCode = authorization.getAuthorizationCode();
		}
		
		ComponentAcessToken cat = getComponentAcessTokenFromCache();
		String componentAcessToken = cat.getComponentAcessToken();
		
		String reqUrl = String.format(WechatConfig.QUERY_AUTH_URL, componentAcessToken);
//		reqUrl = "http://localhost:81/tpauth/test/queryAuth"; //TODO for test
		Map<String, String> postData = new HashMap<>();
		postData.put("component_appid", componentAppid);
		postData.put("authorization_code", authorizationCode);
		
		AuthorizationResp authorizationResp = restUtil.postByJson(reqUrl, postData, AuthorizationResp.class);
		AuthorizationInfo authorizationInfo = authorizationResp.getAuthorizationInfo();
		
		if (authorizationInfo != null && !StringUtils.isEmpty(authorizationInfo.getAuthorizerAccessToken())) {
			AuthorizerAccessToken authorizerAccessToken = new AuthorizerAccessToken();
			BeanUtils.copyProperties(authorizationInfo, authorizerAccessToken);
			authorizerAccessToken.setCreateTime(System.currentTimeMillis());
			String authTokenKey = Constants.KEY_AUTHORIZER_ACCESS_TOKEN+authorizationInfo.getAuthorizerAppid();
			redisTemplate.opsForValue().set(authTokenKey, authorizerAccessToken);	//将授权公众号的accessToken缓存起来
			String authList = (String) redisTemplate.opsForValue().get(Constants.KEY_AUTHORIZER_LIST);
			if (StringUtils.isEmpty(authList)) {
				authList = authorizationInfo.getAuthorizerAppid();
			}else {
				authList += "," + authorizationInfo.getAuthorizerAppid();
			}
			redisTemplate.opsForValue().set(Constants.KEY_AUTHORIZER_LIST, authList);	//缓存被授权的公众号列表
			writeFile(authorizationInfo.getAuthorizerAppid(), authorizationInfo.getAuthorizerRefreshToken());
		}
		
	}
	
	/**
	 * 从缓存中取componentAccessToken
	 */
	@Override
	public ComponentAcessToken getComponentAcessTokenFromCache() {

		ComponentAcessToken componentAcessToken = (ComponentAcessToken) redisTemplate.opsForValue().get(Constants.KEY_COMPONENT_ACCESS_TOKEN);
		return componentAcessToken;
	}

	/**
	 * 将refreshtoken记录到文件，这个如果丢失了需要客户重新授权，比较麻烦，所以记文件里。这个工程没有数据库
	 * @param fileName
	 * @param content
	 */
	@Override
	public void writeFile(String fileName, String content) {
		
		File tokenFolder = new File(cacheFolder);
		if (!tokenFolder.exists()) {
			tokenFolder.mkdirs();
		}
		FileWriter fw = null;
		try {
			String wFileName = tokenFolder.getAbsolutePath() + File.separator + fileName;
			File file = new File(wFileName);
			fw = new FileWriter(file);
			fw.write(content);
			
		} catch (Throwable e) {
			logger.error("save refresh token failed !!!", e);
		} finally {
			try {
				fw.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
	
}
