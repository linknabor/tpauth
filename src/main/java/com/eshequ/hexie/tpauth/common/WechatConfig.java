package com.eshequ.hexie.tpauth.common;

public class WechatConfig {

	public final static String SUCCESS = "success";
	
	public final static String FAIL = "fail";
	
	public final static String COMPONENT_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
	
	public final static String AUTHORIZER_ACCESS_TOKEN_URL = "https://api.weixin.qq.com /cgi-bin/component/api_authorizer_token?component_access_token=%s" ;
	
	public final static String PRE_AUTH_CODE_URL = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=%s";
	
	public final static String QUERY_AUTH_URL = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=%s";

	public final static String EVENT_TYPE_VERIFY_TICKET = "component_verify_ticket";	//获取第三方验证ticket事件
	
	public final static String EVENT_TYPE_AUTHORIZED = "authorized";	//确认授权事件
	
	public final static String EVENT_TYPE_UPDATEAUTHORIZED = "updateauthorized";	//授权更新事件
	
	public final static String PC_AUTH_URL = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?auth_type=1&component_appid=COMPONENT_APPID&pre_auth_code=PRE_AUTH_CODE&redirect_uri=REDIRECT_URI";
	
	public final static String MOBILE_AUTH_URL = "https://mp.weixin.qq.com/safe/bindcomponent?action=bindcomponent&auth_type=1&no_scan=1&component_appid=COMPONENT_APPID&pre_auth_code=PRE_AUTH_CODE&redirect_uri=REDIRECT_URI#wechat_redirect";
	
	public final static String AUTH_REDIRECT_URI = "https://test.e-shequ.com/official/authSuccess.html";
	
}
