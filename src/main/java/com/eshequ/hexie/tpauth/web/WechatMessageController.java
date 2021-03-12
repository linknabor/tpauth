package com.eshequ.hexie.tpauth.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eshequ.hexie.tpauth.service.WechatMessageService;
import com.eshequ.hexie.tpauth.vo.EventRequest;

@RestController
public class WechatMessageController {
	
	private Logger logger = LoggerFactory.getLogger(WechatMessageController.class);

	@Autowired
	private WechatMessageService messageService;
	
	/**
	 * 消息事件接收
	 * @param requestXml
	 * @return
	 */
	@RequestMapping(value = "/event/msg/*", method = RequestMethod.POST)
	public String msgEvent(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String postData, 
			@RequestParam(value = "signature", required = false) String signature,
			@RequestParam(value = "timestamp", required = false) String timeStamp,
			@RequestParam(value = "nonce", required = false) String nonce, 
			@RequestParam(value = "encrypt_type", required = false) String encryptType,
			@RequestParam(value = "msg_signature", required = false) String msgSignature) {
		
		String requestUri = request.getRequestURI();
		String appId = getAppIdByRequestUri(requestUri);
		
		EventRequest eventRequest = new EventRequest(appId, msgSignature, timeStamp, nonce, 
				encryptType, msgSignature, postData);
		logger.info("event msg : " + eventRequest);
		
		String responseMsg = messageService.handleMsgEvent(eventRequest);
		return responseMsg;
		
	}
	
	/**
	 * 从请求链接中截取appId
	 * @param requestUri
	 * @return
	 */
	private String getAppIdByRequestUri(String requestUri) {
		String appId = "";
		try {
			int endIndex = requestUri.indexOf("?");
			if (endIndex == -1) {
				endIndex = requestUri.length();
			}else {
				endIndex = endIndex - 1;
			}
			appId = requestUri.substring(requestUri.lastIndexOf("/")+1, endIndex);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return appId;
	}
}
