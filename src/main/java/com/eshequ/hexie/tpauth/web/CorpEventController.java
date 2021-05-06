package com.eshequ.hexie.tpauth.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eshequ.hexie.tpauth.common.WechatConfig;
import com.eshequ.hexie.tpauth.exception.AesException;

/**
 * 企业微信消息处理
 * @author david
 *
 */
@RestController
@RequestMapping(value = "/corp")
public class CorpEventController {
	
	private Logger logger = LoggerFactory.getLogger(CorpEventController.class);
	
	/**
	 * 授权事件接收
	 * @param requestXml
	 * @return
	 * @throws AesException 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/event/auth", method = {RequestMethod.POST})
	public String authEvent(@RequestBody String requestXml, 
			@RequestParam(value = "signature", required = false) String signature,
			@RequestParam(value = "timestamp", required = false) String timeStamp,
			@RequestParam(value = "nonce", required = false) String nonce, 
			@RequestParam(value = "encrypt_type", required = false) String encryptType,
			@RequestParam(value = "msg_signature", required = false) String msgSignature) throws AesException, IOException {
		
		return "";
	}
	
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
		
		
		return "";
		
	}

}
