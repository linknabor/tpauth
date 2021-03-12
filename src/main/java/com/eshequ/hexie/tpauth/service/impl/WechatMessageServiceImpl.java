package com.eshequ.hexie.tpauth.service.impl;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.eshequ.hexie.tpauth.common.Constants;
import com.eshequ.hexie.tpauth.common.WechatConfig;
import com.eshequ.hexie.tpauth.exception.AesException;
import com.eshequ.hexie.tpauth.exception.BusinessException;
import com.eshequ.hexie.tpauth.service.AuthService;
import com.eshequ.hexie.tpauth.service.WechatMessageService;
import com.eshequ.hexie.tpauth.util.RandomUtil;
import com.eshequ.hexie.tpauth.util.RestUtil;
import com.eshequ.hexie.tpauth.util.wechat.WXBizMsgCrypt;
import com.eshequ.hexie.tpauth.vo.EventRequest;
import com.eshequ.hexie.tpauth.vo.WechatResponse;
import com.eshequ.hexie.tpauth.vo.auth.AuthorizerAccessToken;
import com.eshequ.hexie.tpauth.vo.msg.CsMessage;
import com.eshequ.hexie.tpauth.vo.msg.CsMessage.CsText;
import com.eshequ.hexie.tpauth.vo.msg.ResponseImageMessage;
import com.eshequ.hexie.tpauth.vo.msg.ResponseImageMessage.Image;
import com.eshequ.hexie.tpauth.vo.subscribemsg.EventChange;
import com.eshequ.hexie.tpauth.vo.subscribemsg.EventPopup;
import com.eshequ.hexie.tpauth.vo.msg.ResponseMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Service
public class WechatMessageServiceImpl implements WechatMessageService {

	private Logger logger = LoggerFactory.getLogger(WechatMessageServiceImpl.class);
	
	@Value("${component.appid}")
	private String componentAppid;
	
	@Value("${component.secret}")
	private String componetSecret;
	
	@Value("${component.token}")
	private String token;
	
	@Value("${component.aeskey}")
	private String aeskey;
	
	@Value("${wechatCardEnabledApps}")
	private String wechatCardEnabledApps;
	
	@Value("${customservice.image.mediaid}")
	private String mediaId;
	
	@Value("customServiceEnabledApps")
	private String customServiceEnabledApps;
	
	@Autowired
	private RestUtil restutil;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	@Qualifier(value = "redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	@Qualifier(value = "hexieStringRedisTemplate")
	private StringRedisTemplate hexieStringRedisTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	/**
	 * 消费队列中的关注事件消息，给新关注用户发会员卡
	 */
	@PostConstruct
	public void consumeSubscribeMsg() {
		
	}
	
	/**
	 * 处理微信消息
	 */
	@Override
	public String handleMsgEvent(EventRequest eventRequest) {

		String response = "";
		try {
			String formattedXml = eventRequest.getPostData().replaceAll("\r", "").replaceAll("\n", "").
					replaceAll("\r\n", "").replace("\t", "").replaceAll(" ", "");	//去换行
			
			XmlMapper xmlMapper = new XmlMapper();
			JsonNode rootNode = xmlMapper.readTree(formattedXml);	//解析xml
			JsonNode userNode = rootNode.path("ToUserName");		//toUserName节点
			JsonNode encryptNode = rootNode.path("Encrypt");	//取出打了码的内容部分节点
			String encryptStr = encryptNode.asText();
			
			WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(token, aeskey, componentAppid);
			String decryptedContent = msgCrypt.decryptMsg(eventRequest.getMsg_signature(), eventRequest.getTimestamp(), 
					eventRequest.getNonce(), encryptStr);
			
			logger.info("decrypted content : " + decryptedContent);	//解密后的内容，是个xml
			
			decryptedContent = decryptedContent.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\r\n", "").
					replaceAll("\r\n", "").replace("\t", "").replaceAll(" ", "");	//去换行
			
			String toUserName = userNode.asText();
			
			//微信第三方平台授权 验证用，微信会发请求验证
			if (WechatConfig.TEST_USERNAME.equals(toUserName)) {
				response = reply4TestPub(decryptedContent);
			}else {
				
				JsonNode decryptRoot = xmlMapper.readTree(decryptedContent);

				JsonNode typeNode = decryptRoot.path("MsgType");
				if (typeNode == null) {
					throw new BusinessException("invalid message! no MsgType node !");
				}
				String msgType = typeNode.asText();
				logger.info("msgType : " + msgType);
				
				switch (msgType) {
				case WechatConfig.MSG_TYPE_TEXT:
					response = replyTextMsg(decryptedContent);
					break;
				case WechatConfig.MSG_TYPE_EVENT:
					response = replyEventMsg(eventRequest.getAppId(), decryptedContent);
					break;
				default:
					break;
				}
				
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return response;
	}
	
	/**
	 * 全网发布测试校验，腾讯回发消息请求过来验证
	 * @return
	 * @throws IOException 
	 * @throws AesException 
	 */
	private String reply4TestPub(String decryptedContent) throws IOException, AesException {
		
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode decryptRoot = xmlMapper.readTree(decryptedContent);

		JsonNode typeNode = decryptRoot.path("MsgType");
		if (typeNode == null) {
			throw new BusinessException("invalid message! no MsgType node !");
		}
		
		JsonNode contentNode = decryptRoot.path("Content");
		String content = contentNode.asText();
		JsonNode fromUserNode = decryptRoot.path("FromUserName");
		JsonNode toUserNode = decryptRoot.path("ToUserName");
		String fromUserName = fromUserNode.asText();
		String toUserName = toUserNode.asText();
		
		String reply = "";
		if (WechatConfig.TEST_MSG_TEXT.equals(content)) {
			String respContent = content + "_callback";
			ResponseMessage responseMessage = new ResponseMessage();
			responseMessage.setFromUserName(toUserName);
			responseMessage.setToUserName(fromUserName);
			responseMessage.setMsgType(WechatConfig.MSG_TYPE_TEXT);
			responseMessage.setCreateTime(String.valueOf(System.currentTimeMillis()));
			responseMessage.setContent(respContent);
			String replyMsg = xmlMapper.writeValueAsString(responseMessage);
			
			WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(componetSecret, aeskey, componentAppid);
			reply = msgCrypt.encryptMsg(replyMsg, String.valueOf(System.currentTimeMillis()), RandomUtil.buildRandom());
			logger.info("reply4TestPub, request conent :" + content + ", response content :" + replyMsg);
			
		}else if (content.indexOf(WechatConfig.TEST_MSG_TEXT2)>-1) {
			String queryAuthCode = content.substring(content.indexOf(":")+1);
			
			Runnable run = ()->{
				CsMessage csMessage = new CsMessage();
				csMessage.setMsgtype(WechatConfig.MSG_TYPE_TEXT);
				csMessage.setTouser(fromUserName);
				CsText csText = new CsText();
				csText.setContent(queryAuthCode+"_from_api");
				csMessage.setText(csText);
				String reqUrl = WechatConfig.CUSTOM_MSG_URL;
				AuthorizerAccessToken authorizerAccessToken = authService.getAuthorizerAccessTokenFromCache(WechatConfig.TEST_APP_ID);
				reqUrl = reqUrl.replaceAll("ACCESS_TOKEN", authorizerAccessToken.getAuthorizerAccessToken());
				WechatResponse wechatResponse = restutil.postByJson(reqUrl, csMessage, WechatResponse.class);
				logger.info("wechatResponse : " + wechatResponse);
			};
			Thread t = new Thread(run);
			t.start();
			
			reply = "";
			
		}
		
		return reply;
		
	}
	
	/**
	 * 普通文本消息回复
	 * 微信发布测试用，勿删！！！
	 * @param decryptedContent
	 * @return
	 * @throws IOException
	 * @throws AesException
	 */
	public String replyTextMsg(String decryptedContent) throws IOException, AesException {
		
		logger.info(decryptedContent);
		return "";
	}
	
	
	
	/**
	 * 事件消息回复
	 * @param appId
	 * @param decryptedContent
	 * @return
	 * @throws Exception
	 */
	private String replyEventMsg(String appId, String decryptedContent) throws Exception {
		
		if (StringUtils.isEmpty(appId)) {
			logger.warn("appId is empty, will skip ! decryptRoot : " + decryptedContent);
			return "";
		}
		
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode decryptRoot = xmlMapper.readTree(decryptedContent);
		JsonNode eventNode = decryptRoot.path("Event");
		String event = eventNode.asText();
		switch (event) {
		//关注事件处理
		case WechatConfig.EVENT_TYPE_SUBSCRIBE:
			eventSubscribe(appId, decryptRoot);
			break;
		case WechatConfig.EVENT_TYPE_UNSUBSCRIBE:
			//TODO 异步删除用户信息（微信要求，基于欧洲用户隐私保护法）
			break;
		case WechatConfig.EVENT_TYPE_USERGETCARD:
			eventGetCard(appId, decryptRoot);
			break;
		case WechatConfig.EVENT_TYPE_UPDATECARD:
			eventUpdateCard(appId, decryptRoot);
			break;
		case WechatConfig.EVENT_TYPE_DELCARD:
			//TODO
			break;
		case WechatConfig.EVENT_TYPE_SUBSCRIBE_MSG_POPUP:
			eventSubscribeMsgPopup(appId, decryptedContent);
			break;
		case WechatConfig.EVENT_TYPE_SUBSCRIBE_MSG_CHANGE:
			eventSubscribeMsgChange(appId, decryptedContent);
			break;
		default:
			break;
		}
		
		
		return "";
	}
	
	/**
	 * 关注事件
	 * 事件消息直接放到队列，推送到 hexie的redis队列，在hexie处理，这里直接返回空串。
	 * @param appId
	 * @param decryptRoot
	 * @throws JsonProcessingException
	 */
	private void eventSubscribe(String appId, JsonNode decryptRoot) throws JsonProcessingException {
		
		if (wechatCardEnabledApps.indexOf(appId)==-1) {
			logger.info("当前公众号["+appId+"]，未开通卡券服务。");
			return;
		}
		
		//异步推送模板消息：推送到队列，队列慢慢处理，这样每个线程可以省下时间，应对并发。
		JsonNode fromUserNode = decryptRoot.path("FromUserName");
		String fromUserOpenId = fromUserNode.asText();
		JsonNode createTimeNode = decryptRoot.path("CreateTime");
		String createTime = createTimeNode.asText();
		String keyPrev = "event_Subsribe_";
		String userTimeKey = keyPrev + fromUserOpenId + "_" + createTime;
		
		Long times = redisTemplate.opsForValue().increment(userTimeKey, 1);	//直接往上加
		if (times == 1) {
			Map<String, String> map = new HashMap<>();
			map.put("openid", fromUserOpenId);
			map.put("appId", appId);
			String json = objectMapper.writeValueAsString(map);
			hexieStringRedisTemplate.opsForList().rightPush(Constants.KEY_EVENT_SUBSCRIBE_QUEUE, json);
			redisTemplate.expire(userTimeKey, 10, TimeUnit.MINUTES);	//10分钟过期。一般并发出现在服务器没有响应腾讯的情况下，腾讯会陆续发3次请求，间隔不会超过10分钟的。
		}else {
			logger.warn("duplicated request, user :" + fromUserOpenId + ", createTime : " + createTime);
		}
		
		
	}
	
	/**
	 * 领取卡事件
	 *  事件消息直接放到队列，推送到 hexie的redis队列，在hexie处理，这里直接返回空串。
	 * @param appId
	 * @param decryptRoot
	 * @throws JsonProcessingException 
	 */
	private void eventGetCard(String appId, JsonNode decryptRoot) throws JsonProcessingException {
		
		JsonNode fromUserNode = decryptRoot.path("FromUserName");
		String fromUserOpenId = fromUserNode.asText();
		JsonNode createTimeNode = decryptRoot.path("CreateTime");
		String createTime = createTimeNode.asText();
		JsonNode cardIdNode = decryptRoot.path("CardId");
		String cardId = cardIdNode.asText();
		JsonNode userCardCodeNode = decryptRoot.path("UserCardCode");
		String userCardCode = userCardCodeNode.asText();
		JsonNode outerIdNode = decryptRoot.path("OuterId");
		String outerId = outerIdNode.asText();
		JsonNode outStrNode = decryptRoot.path("OuterStr");
		String outStr = outStrNode.asText();
		JsonNode isRestoreMemberCardNode = decryptRoot.path("IsRestoreMemberCard");
		String isRestoreMemberCard = isRestoreMemberCardNode.asText();
		JsonNode oldUserCardCodeNode = decryptRoot.path("OldUserCardCode");
		String oldUserCardCode = oldUserCardCodeNode.asText(); 
		JsonNode sourceSceneNode = decryptRoot.path("SourceScene");
		String sourceScene = sourceSceneNode.asText();
		
		String keyPrev = "event_userGetCard_";
		String userTimeKey = keyPrev + fromUserOpenId + "_" + createTime;
		
		Long times = redisTemplate.opsForValue().increment(userTimeKey, 1);	//直接往上加
		if (times == 1) {
			Map<String, String> map = new HashMap<>();
			map.put("openid", fromUserOpenId);
			map.put("appId", appId);
			map.put("cardId", cardId);
			map.put("cardCode", userCardCode);
			map.put("outerId", outerId);
			map.put("outStr", outStr);
			map.put("isRestoreMemberCard", isRestoreMemberCard);
			map.put("oldUserCardCode", oldUserCardCode);
			map.put("sourceScene", sourceScene);
			String json = objectMapper.writeValueAsString(map);
			hexieStringRedisTemplate.opsForList().rightPush(Constants.KEY_EVENT_GETCARD_QUEUE, json);
			redisTemplate.expire(userTimeKey, 10, TimeUnit.MINUTES);	//10分钟过期。一般并发出现在服务器没有响应腾讯的情况下，腾讯会陆续发3次请求，间隔不会超过10分钟的。
		}else {
			logger.warn("duplicated request, user :" + fromUserOpenId + ", createTime : " + createTime);
		}
	}
	
	/**
	 * 更新卡事件
	 * @param appId
	 * @param decryptRoot
	 * @throws JsonProcessingException 
	 */
	public void eventUpdateCard(String appId, JsonNode decryptRoot) throws JsonProcessingException {
		
		if (wechatCardEnabledApps.indexOf(appId)==-1) {
			logger.info("当前公众号["+appId+"]，未开通卡券服务。");
			return;
		}
		
		JsonNode fromUserNode = decryptRoot.path("FromUserName");
		String fromUserOpenId = fromUserNode.asText();
		JsonNode createTimeNode = decryptRoot.path("CreateTime");
		String createTime = createTimeNode.asText();
		JsonNode cardIdNode = decryptRoot.path("CardId");
		String cardId = cardIdNode.asText();
		JsonNode userCardCodeNode = decryptRoot.path("UserCardCode");
		String userCardCode = userCardCodeNode.asText();
		JsonNode modifyBonusNode = decryptRoot.path("ModifyBonus");
		String modifyBonus = modifyBonusNode.asText();	//变动的积分值
		JsonNode modifyBalanceNode = decryptRoot.path("ModifyBalance");
		String modifyBalance = modifyBalanceNode.asText();	//变动的余额值
		
		String keyPrev = "event_updateCard_";
		String userTimeKey = keyPrev + fromUserOpenId + "_" + createTime;
		
		Long times = redisTemplate.opsForValue().increment(userTimeKey, 1);	//直接往上加
		if (times == 1) {
			Map<String, String> map = new HashMap<>();
			map.put("createTime", createTime);
			map.put("openid", fromUserOpenId);
			map.put("appId", appId);
			map.put("cardId", cardId);
			map.put("cardCode", userCardCode);
			map.put("modifyBonus", modifyBonus);
			map.put("modifyBalance", modifyBalance);
			String json = objectMapper.writeValueAsString(map);
			hexieStringRedisTemplate.opsForList().rightPush(Constants.KEY_EVENT_UPDATECARD_QUEUE, json);
			redisTemplate.expire(userTimeKey, 10, TimeUnit.MINUTES);	//10分钟过期。一般并发出现在服务器没有响应腾讯的情况下，腾讯会陆续发3次请求，间隔不会超过10分钟的。
		}else {
			logger.warn("duplicated request, user :" + fromUserOpenId + ", createTime : " + createTime);
		}
		
		
	}
	
	/**
	 * 对于普通文本消息，回复客服消息，并发送客服的企业微信二维码
	 * 微信发布测试用，勿删！！！
	 * @param decryptedContent
	 * @return
	 * @throws IOException
	 * @throws AesException
	 */
	protected String replyTextMsgByImg(String decryptedContent) throws IOException, AesException {
		
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode decryptRoot = xmlMapper.readTree(decryptedContent);

		JsonNode contentNode = decryptRoot.path("Content");
		String content = contentNode.asText();
		JsonNode fromUserNode = decryptRoot.path("FromUserName");
		JsonNode toUserNode = decryptRoot.path("ToUserName");
		String fromUserName = fromUserNode.asText();
		String toUserName = toUserNode.asText();
		if (customServiceEnabledApps.indexOf(toUserName)==-1) {
			logger.info("当前公众号["+toUserName+"]，未开通图片客服消息。");
			return "";
		}
		ResponseImageMessage responseMessage = new ResponseImageMessage();
		responseMessage.setFromUserName(toUserName);
		responseMessage.setToUserName(fromUserName);
		responseMessage.setMsgType(WechatConfig.MSG_TYPE_IMAGE);
		responseMessage.setCreateTime(String.valueOf(System.currentTimeMillis()));
		Image image = new Image();
		image.setMediaId(mediaId);
		responseMessage.setImage(image);
		
		String replyMsg = xmlMapper.writeValueAsString(responseMessage);
		replyMsg = replyMsg.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\r\n", "").replace("\t", "").replaceAll(" ", "");	//去换行
		WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(token, aeskey, componentAppid);
		String reply = msgCrypt.encryptMsg(replyMsg, String.valueOf(System.currentTimeMillis()), RandomUtil.buildRandom());
		logger.info("replyTextMsgByImage, request conent :" + content + ", response content :" + replyMsg);
		return reply;
	}

	/**
	 * 用户在图文等场景内订阅通知的操作
	 * @param appId
	 * @param decryptRoot
	 * @throws Exception 
	 */
	public void eventSubscribeMsgPopup(String appId, String decryptedContent) throws Exception {
		
		XmlMapper xmlMapper = new XmlMapper();
		EventPopup eventPopup = xmlMapper.readValue(decryptedContent, EventPopup.class);
		eventPopup.setAppId(appId);
		String keyPrev = "event_subscribeMsgPopup_";
		String userTimeKey = keyPrev + eventPopup.getFromUserName() + "_" + eventPopup.getCreateTime();
		
		Boolean success = redisTemplate.opsForValue().setIfAbsent(userTimeKey, "", Duration.ofMinutes(10l));	//10分钟过期
		if (success) {
			String json = objectMapper.writeValueAsString(eventPopup);
			hexieStringRedisTemplate.opsForList().rightPush(Constants.KEY_EVENT_SUBSCRIBE_MSG_QUEUE, json);
		}
		
	}
	
	/**
	 * 用户在服务通知管理页面做通知管理时的操作
	 * @param appId
	 * @param decryptRoot
	 * @throws Exception 
	 */
	public void eventSubscribeMsgChange(String appId, String decryptedContent) throws Exception {
		
		XmlMapper xmlMapper = new XmlMapper();
		EventChange eventChange = xmlMapper.readValue(decryptedContent, EventChange.class);
		eventChange.setAppId(appId);
		String keyPrev = "event_subscribeMsgChange_";
		String userTimeKey = keyPrev + eventChange.getFromUserName() + "_" + eventChange.getCreateTime();
		
		Boolean success = redisTemplate.opsForValue().setIfAbsent(userTimeKey, "", Duration.ofMinutes(10l));	//10分钟过期
		if (success) {
			String json = objectMapper.writeValueAsString(eventChange);
			//和图文的用一个queue，因为xml反序列化出来的实体只差一个字段，设置可空即可。
			hexieStringRedisTemplate.opsForList().rightPush(Constants.KEY_EVENT_SUBSCRIBE_MSG_QUEUE, json);
		}
		
	}
	
}
