package com.eshequ.hexie.tpauth.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.eshequ.hexie.tpauth.exception.AesException;
import com.eshequ.hexie.tpauth.service.CorpEventService;
import com.eshequ.hexie.tpauth.util.wechat.WXBizMsgCrypt;
import com.eshequ.hexie.tpauth.vo.corp.CorpEventRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Service
public class CorpEventServiceImpl implements CorpEventService {
	
	private static Logger logger = LoggerFactory.getLogger(CorpEventServiceImpl.class);
	
	@Autowired
	@Qualifier(value = "CorpWxBizMsgCrypt")
	private WXBizMsgCrypt wxBizMsgCrypt;
	
	/**
	 * 验证企业微信第三方应用
	 * 1.对收到的请求做Urldecode处理
	 * 2.通过参数msg_signature对请求进行校验，确认调用者的合法性。
	 * 3.解密echostr参数得到消息内容(即msg字段)
	 * 4.在1秒内响应GET请求，响应内容为上一步得到的明文消息内容(不能加引号，不能带bom头，不能带换行符)
	 */
	public void validate(CorpEventRequest corpEventRequest) {
		
		String decryptedContent = "";
		try {
			decryptedContent = wxBizMsgCrypt.decryptMsg(corpEventRequest.getMsg_signature(), corpEventRequest.getTimestamp(), 
					corpEventRequest.getNonce(), corpEventRequest.getEchostr());
		
			decryptedContent = decryptedContent.replaceAll("\r", "").replaceAll("\n", "").replaceAll("\r\n", "").
					replaceAll("\r\n", "").replace("\t", "").replaceAll(" ", "");	//去换行
			
			logger.info("decryptedContent : " + decryptedContent);
			
//			XmlMapper xmlMapper = new XmlMapper();
//			JsonNode rootNode = xmlMapper.readTree(decryptedContent);	//解析xml
//			JsonNode appidNode = rootNode.path("AppId");		//appid节点
//			JsonNode encryptNode = rootNode.path("Encrypt");	//取出打了码的内容部分节点
		
		
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	} 

}
