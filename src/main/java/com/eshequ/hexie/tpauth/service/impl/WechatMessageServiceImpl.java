package com.eshequ.hexie.tpauth.service.impl;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eshequ.hexie.tpauth.common.WechatConfig;
import com.eshequ.hexie.tpauth.exception.AesException;
import com.eshequ.hexie.tpauth.exception.BusinessException;
import com.eshequ.hexie.tpauth.service.AuthService;
import com.eshequ.hexie.tpauth.util.RandomUtil;
import com.eshequ.hexie.tpauth.util.RestUtil;
import com.eshequ.hexie.tpauth.util.wechat.WXBizMsgCrypt;
import com.eshequ.hexie.tpauth.vo.EventRequest;
import com.eshequ.hexie.tpauth.vo.WechatResponse;
import com.eshequ.hexie.tpauth.vo.auth.AuthorizerAccessToken;
import com.eshequ.hexie.tpauth.vo.msg.CsMessage;
import com.eshequ.hexie.tpauth.vo.msg.CsMessage.CsText;
import com.eshequ.hexie.tpauth.vo.msg.ResponseMessage;
import com.fasterxml.jackson.databind.JsonNode;
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
	
	@Autowired
	private RestUtil restutil;
	
	@Autowired
	private AuthService authService;
	
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
			
			WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(componetSecret, aeskey, componentAppid);
			String decryptedContent = msgCrypt.decryptMsg(eventRequest.getMsg_signature(), eventRequest.getTimestamp(), 
					eventRequest.getNonce(), encryptStr);
			
			logger.info("decrypted content : " + decryptedContent);	//解密后的内容，是个xml
			
			decryptedContent = decryptedContent.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\r\n", "").
					replaceAll("\r\n", "").replace("\t", "").replaceAll(" ", "");	//去换行
			
			String toUserName = userNode.asText();
			
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
			reply = replyMsg;
			
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
	 * 测试用
	 * @param decryptedContent
	 * @return
	 * @throws IOException
	 * @throws AesException
	 */
	private String replyTextMsg(String decryptedContent) throws IOException, AesException {
		
		XmlMapper xmlMapper = new XmlMapper();
		JsonNode decryptRoot = xmlMapper.readTree(decryptedContent);

		JsonNode contentNode = decryptRoot.path("Content");
		String content = contentNode.asText();
		JsonNode fromUserNode = decryptRoot.path("FromUserName");
		JsonNode toUserNode = decryptRoot.path("ToUserName");
		String fromUserName = fromUserNode.asText();
		String toUserName = toUserNode.asText();
		
		String respContent = content + "_callback";
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setFromUserName(toUserName);
		responseMessage.setToUserName(fromUserName);
		responseMessage.setMsgType(WechatConfig.MSG_TYPE_TEXT);
		responseMessage.setCreateTime(String.valueOf(System.currentTimeMillis()));
		responseMessage.setContent(respContent);
		String replyMsg = xmlMapper.writeValueAsString(responseMessage);
		
		WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(token, aeskey, componentAppid);
		String reply = msgCrypt.encryptMsg(replyMsg, String.valueOf(System.currentTimeMillis()), RandomUtil.buildRandom());
		logger.info("replyTextMsg, request conent :" + content + ", response content :" + replyMsg);
		return reply;
	}
	

}
