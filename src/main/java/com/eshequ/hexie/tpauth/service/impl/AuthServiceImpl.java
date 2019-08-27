package com.eshequ.hexie.tpauth.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eshequ.hexie.tpauth.common.Constants;
import com.eshequ.hexie.tpauth.common.WechatConfig;
import com.eshequ.hexie.tpauth.exception.AesException;
import com.eshequ.hexie.tpauth.exception.BusinessException;
import com.eshequ.hexie.tpauth.service.AuthService;
import com.eshequ.hexie.tpauth.util.RestUtil;
import com.eshequ.hexie.tpauth.util.wechat.WXBizMsgCrypt;
import com.eshequ.hexie.tpauth.vo.AuthRequest;
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
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private RestUtil restUtil;
	
	@Override
	public void saveVerifyTicket(ComponentVerifyTicket verifyTicket) {

		redisTemplate.opsForValue().set(Constants.VERIFY_TICKET, verifyTicket);
	}

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
	 * 获取预授权码
	 * 先从缓存中取，如果没有，访问微信获取。缓存设置10分钟过期。不做定时轮询的存储操作，因为这个码的使用频率很低，没必要每10分钟取一次。做缓存是为了单个用户反复刷新页面操作不用重复做http开销
	 */
	@Override
	public PreAuthCode getPreAuthCode() {
		
		PreAuthCode pac = (PreAuthCode) redisTemplate.opsForValue().get(Constants.PRE_AUTH_CODE);
		
		if (pac == null || StringUtils.isEmpty(pac.getPreAuthCode())) {
			ComponentAcessToken cat = (ComponentAcessToken) redisTemplate.opsForValue().get(Constants.COMPONENT_ACCESS_TOKEN);
			String reqUrl = WechatConfig.PRE_AUTH_CODE_URL;
			reqUrl = String.format(reqUrl, cat.getComponentAcessToken());
			Map<String, String> postData = new HashMap<>();
			postData.put("component_appid", componentAppid);
			PreAuthCode preAuthCode = restUtil.postByJson(reqUrl, postData, PreAuthCode.class);
			redisTemplate.opsForValue().set(Constants.PRE_AUTH_CODE, preAuthCode);
			redisTemplate.expire(Constants.PRE_AUTH_CODE, 10, TimeUnit.MINUTES);	//设置10分钟过期
			pac = preAuthCode;
		}
		return pac;
	}

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
		case WechatConfig.EVENT_TYPE_VERIFY_TICKET:
			ComponentVerifyTicket ticket = xmlMapper.readValue(decryptedContent, ComponentVerifyTicket.class);
			saveVerifyTicket(ticket);
			break;
		default:
			logger.error("unknow info type : " + infoType);
			break;
		}
		
		
	}

	/**
	 * 页面或者手机端获取授权链接
	 */
	@Override
	public String getAuthLink(String requestHeader) {
		
		boolean isMobile = false;
		for (String device : Constants.mobileDevices) {
			if (requestHeader.indexOf(device) > 0 ) {
				isMobile = true;
				break;
			}
		}
		String authLink = "";
		String redirectUri = "";
		if (isMobile) {
			authLink = WechatConfig.MOBILE_AUTH_LINK;
			redirectUri = "";
		}else {
			authLink = WechatConfig.PC_AUTH_LINK;
			redirectUri = "";
		}
		PreAuthCode preAuthCode = getPreAuthCode();
		authLink = authLink.replaceAll("COMPONENT_APPID", componentAppid).replaceAll("PRE_AUTH_CODE", preAuthCode.getPreAuthCode()).replaceAll("REDIRECT_URI", redirectUri);
		return null;
	}

	
}
