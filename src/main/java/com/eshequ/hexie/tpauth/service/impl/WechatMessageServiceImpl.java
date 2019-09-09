package com.eshequ.hexie.tpauth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.eshequ.hexie.tpauth.exception.BusinessException;
import com.eshequ.hexie.tpauth.util.wechat.WXBizMsgCrypt;
import com.eshequ.hexie.tpauth.vo.EventRequest;
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
	
	/**
	 * 处理微信消息
	 */
	@Override
	public String handleMsgEvent(EventRequest eventRequest) {

		try {
			String formattedXml = eventRequest.getPostData().replaceAll("\r", "").replaceAll("\n", "").
					replaceAll("\r\n", "").replace("\t", "").replaceAll(" ", "");	//去换行
			
			XmlMapper xmlMapper = new XmlMapper();
			JsonNode rootNode = xmlMapper.readTree(formattedXml);	//解析xml
			JsonNode userNode = rootNode.path("ToUserName");		//appid节点
			JsonNode encryptNode = rootNode.path("Encrypt");	//取出打了码的内容部分节点
			
			String toUserName = userNode.asText();
			//TODO validate toUserName
			
			String encryptStr = encryptNode.asText();
			
			WXBizMsgCrypt msgCrypt = new WXBizMsgCrypt(componetSecret, aeskey, componentAppid);
			String decryptedContent = msgCrypt.decryptMsg(eventRequest.getMsg_signature(), eventRequest.getTimestamp(), 
					eventRequest.getNonce(), encryptStr);
			
			logger.info("decrypted content : " + decryptedContent);	//解密后的内容，是个xml
			
			decryptedContent = decryptedContent.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\r\n", "").
					replaceAll("\r\n", "").replace("\t", "").replaceAll(" ", "");	//去换行
			JsonNode decryptRoot = xmlMapper.readTree(decryptedContent);
			JsonNode typeNode = decryptRoot.path("MsgType");
			if (typeNode == null) {
				throw new BusinessException("invalid message! no MsgType node !");
			}
			String msgType = typeNode.asText();
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}

}
