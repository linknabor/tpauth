package com.eshequ.hexie.tpauth.common;

import java.util.ArrayList;
import java.util.List;

public class Constants {

	public static final String KEY_VERIFY_TICKET = "oaAuth_verifyTicket";
	
	public static final String KEY_COMPONENT_ACCESS_TOKEN = "oaAuth_componentAccessToken";
	
	public static final String KEY_PRE_AUTH_CODE = "oaAuth_preAuthCode";
	
	public static final String KEY_AUTHORIZATION_CODE = "oaAuth_authorizationCode_";	//后面要拼接appId
	
	public static final String KEY_AUTHORIZER_ACCESS_TOKEN = "oaAuth_authorizerAccessToken_";	//后面要拼接appId
	
	public static final String KEY_AUTHORIZER_JS_TICKET = "oaAuth_authorizerJsTicket_";	//后面要拼接appId
	
	public static final String KEY_AUTHORIZER_LIST = "oaAuth_authorizerList";	//所有被授权的公众号链表
	
	public static final String KEY_AUTH_QUEUE = "oaAuth_authorizationQueue";
	
	public static List<String> mobileDevices;
	
	static {
		
		mobileDevices = new ArrayList<String>();
		mobileDevices.add("iPhone");
		mobileDevices.add("Android");
		mobileDevices.add("iPad ");
		mobileDevices.add("windows phone");
		
	}
	
}
