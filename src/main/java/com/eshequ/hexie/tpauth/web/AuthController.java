package com.eshequ.hexie.tpauth.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eshequ.hexie.tpauth.common.WechatConfig;
import com.eshequ.hexie.tpauth.exception.AesException;
import com.eshequ.hexie.tpauth.exception.BusinessException;
import com.eshequ.hexie.tpauth.service.AuthService;
import com.eshequ.hexie.tpauth.vo.EventRequest;

@RestController
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
	@RequestMapping(value = "/event/auth", method = {RequestMethod.POST})
	public String authEvent(@RequestBody String requestXml, 
			@RequestParam(value = "signature", required = false) String signature,
			@RequestParam(value = "timestamp", required = false) String timeStamp,
			@RequestParam(value = "nonce", required = false) String nonce, 
			@RequestParam(value = "encrypt_type", required = false) String encryptType,
			@RequestParam(value = "msg_signature", required = false) String msgSignature) throws AesException, IOException {
		
		EventRequest authRequest = new EventRequest(signature, timeStamp, nonce, encryptType, msgSignature, requestXml);
		logger.info("auth event request is : " + authRequest);
		authService.handleAuthEvent(authRequest);
		return WechatConfig.SUCCESS;
	}
	
	/**
	 * 客户端授权
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/client/auth", method = RequestMethod.GET)
	public String clientAuth(HttpServletRequest request) {
		
		String vericode = request.getParameter("vericode");
		Assert.hasText(vericode, "invalid request !");
		String requestHead = request.getHeader("user-agent");
		if (StringUtils.isEmpty(vericode)) {
			throw new BusinessException("invalid request!");
		}
		String authLink = authService.clientAuth(requestHead);
		return authLink;
	}
	
	/**
	 * 手动获取授权信息（被授权公众号的AccessToken等相关信息）,只有在authorization_code（1小时过期）没有过期的情况下。
	 * 通常后台程序会自动出发，不需要手工取调用，留个口子
	 * @param vericode
	 * @return
	 */
	@RequestMapping(value = "/client/authorizationInfo", method = RequestMethod.GET)
	public String authorizationInfo(@RequestParam(value = "vericode", required = true) String vericode,
			@RequestParam(value = "authorizer_appid", required = true) String authorizerAppid) {
		
		Assert.hasText(vericode, "invalid request !");
		Assert.hasText(authorizerAppid, "invalid request !");
		authService.authorizationInfo("", authorizerAppid);
		return WechatConfig.SUCCESS;
		
	}
	
	/**
	 * 挡板程序
	 * @return
	 */
	@RequestMapping(value = "/test/queryAuth", method = RequestMethod.POST)
	public String testQueryAuth() {
		
		String json = "{\r\n" + 
				"\"authorization_info\": {\r\n" + 
				"\"authorizer_appid\": \"wxf8b4f85f3a794e77\",\r\n" + 
				"\"authorizer_access_token\": \"QXjUqNqfYVH0yBE1iI_7vuN_9gQbpjfK7hYwJ3P7xOa88a89-Aga5x1NMYJyB8G2yKt1KCl0nPC3W9GJzw0Zzq_dBxc8pxIGUNi_bFes0qM\",\r\n" + 
				"\"expires_in\": 7200,\r\n" + 
				"\"authorizer_refresh_token\": \"dTo-YCXPL4llX-u1W1pPpnp8Hgm4wpJtlR6iV0doKdY\",\r\n" + 
				"\"func_info\": [\r\n" + 
				"{\r\n" + 
				"\"funcscope_category\": {\r\n" + 
				"\"id\": 1\r\n" + 
				"}\r\n" + 
				"},\r\n" + 
				"{\r\n" + 
				"\"funcscope_category\": {\r\n" + 
				"\"id\": 2\r\n" + 
				"}\r\n" + 
				"},\r\n" + 
				"{\r\n" + 
				"\"funcscope_category\": {\r\n" + 
				"\"id\": 3\r\n" + 
				"}\r\n" + 
				"}\r\n" + 
				"]\r\n" + 
				"}}";
		return json;
	}
	

}
