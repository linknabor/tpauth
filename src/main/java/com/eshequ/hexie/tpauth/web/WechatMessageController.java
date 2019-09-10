package com.eshequ.hexie.tpauth.web;

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
	@RequestMapping(value = "/event/msg/*", method = RequestMethod.POST, produces = {"application/xml; charset=UTF-8"})
	public String msgEvent(@RequestBody String postData, 
			@RequestParam(value = "signature", required = false) String signature,
			@RequestParam(value = "timestamp", required = false) String timeStamp,
			@RequestParam(value = "nonce", required = false) String nonce, 
			@RequestParam(value = "encrypt_type", required = false) String encryptType,
			@RequestParam(value = "msg_signature", required = false) String msgSignature) {
		
		EventRequest eventRequest = new EventRequest(msgSignature, timeStamp, nonce, 
				encryptType, msgSignature, postData);
		logger.info("event msg : " + eventRequest);
		String responeMsg = messageService.handleMsgEvent(eventRequest);
		return responeMsg;
	}
}
