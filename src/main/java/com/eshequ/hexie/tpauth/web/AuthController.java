package com.eshequ.hexie.tpauth.web;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eshequ.hexie.tpauth.common.WechatConfig;
import com.eshequ.hexie.tpauth.exception.AesException;
import com.eshequ.hexie.tpauth.service.AuthService;
import com.eshequ.hexie.tpauth.vo.AuthRequest;

@RestController
@RequestMapping(value = "/event")
public class AuthController {
	
	private Logger logger = LoggerFactory.getLogger(AuthController.class);
	
	@Autowired
	private AuthService authService;
	
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	public String hello() {
		return "hello world!";
	}
	
	/**
	 * 授权事件接收
	 * @param requestXml
	 * @return
	 * @throws AesException 
	 * @throws IOException 
	 */
	@RequestMapping(value = "/auth", method = {RequestMethod.POST})
	public String authEvent(@RequestBody String requestXml, 
			@RequestParam(value = "signature", required = false) String signature,
			@RequestParam(value = "timestamp", required = false) String timeStamp,
			@RequestParam(value = "nonce", required = false) String nonce, 
			@RequestParam(value = "encrypt_type", required = false) String encryptType,
			@RequestParam(value = "msg_signature", required = false) String msgSignature) throws AesException, IOException {
		
		AuthRequest authRequest = new AuthRequest(signature, timeStamp, nonce, encryptType, msgSignature, requestXml);
		logger.info("auth event request is : " + authRequest);
		authService.handleAuthEvent(authRequest);
		return WechatConfig.SUCCESS;
	}
	
	/**
	 * 消息事件接收
	 * @param requestXml
	 * @return
	 */
	@RequestMapping(value = "/msg/*/", method = RequestMethod.POST)
	public String msgEvent(@RequestBody String requestXml) {
		
		logger.info("msg event request : " + requestXml);
		logger.info(requestXml);
		return "a";
	}
	

}
