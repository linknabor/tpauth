package com.eshequ.hexie.tpauth.common;

public class WechatConfig {

	public final static String SUCCESS = "success";
	
	public final static String FAIL = "fail";
	
	//微信消息类型
	public final static String MSG_TYPE_TEXT = "text";
	public final static String MSG_TYPE_IMAGE = "image";
	public final static String MSG_TYPE_EVENT = "event";
	public final static String EVENT_TYPE_SUBSCRIBE = "subscribe";
	public final static String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
	public final static String EVENT_TYPE_USERGETCARD = "user_get_card";
	public final static String EVENT_TYPE_UPDATECARD = "update_member_card";	//会员卡内容更新事件,当用户的会员卡积分余额发生变动时，微信会推送事件告知开发者。
	public final static String EVENT_TYPE_DELCARD = "user_del_card";	//删除卡事件
	
	public final static String EVENT_TYPE_SUBSCRIBE_MSG_POPUP = "subscribe_msg_popup_event";	//用户在图文等场景内订阅通知的操作
	public final static String EVENT_TYPE_SUBSCRIBE_MSG_CHANGE = "subscribe_msg_change_event";	//用户在服务通知管理页面做通知管理时的操作
	
	/* authorization start */
	public final static String COMPONENT_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/component/api_component_token";
	
	public final static String AUTHORIZER_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token=%s" ;
	
	public final static String PRE_AUTH_CODE_URL = "https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=%s";
	
	public final static String QUERY_AUTH_URL = "https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=%s";

	public final static String EVENT_TYPE_VERIFY_TICKET = "component_verify_ticket";	//获取第三方验证ticket事件
	
	public final static String EVENT_TYPE_AUTHORIZED = "authorized";	//确认授权事件
	
	public final static String EVENT_TYPE_UPDATEAUTHORIZED = "updateauthorized";	//授权更新事件
	
	public final static String EVENT_TYPE_UNAUTHORIZED = "unauthorized";	// 取消授权事件
	
	public final static String PC_AUTH_URL = "https://mp.weixin.qq.com/cgi-bin/componentloginpage?auth_type=1&component_appid=COMPONENT_APPID&pre_auth_code=PRE_AUTH_CODE&redirect_uri=REDIRECT_URI";
	
	public final static String MOBILE_AUTH_URL = "https://mp.weixin.qq.com/safe/bindcomponent?action=bindcomponent&auth_type=1&no_scan=1&component_appid=COMPONENT_APPID&pre_auth_code=PRE_AUTH_CODE&redirect_uri=REDIRECT_URI#wechat_redirect";
	
	public final static String AUTH_REDIRECT_URI = "https://test.e-shequ.com/official/authSuccess.html";
	
	public final static String TEST_APP_ID = "wx570bc396a51b8ff8";
	
	public final static String TEST_USERNAME = "gh_3c884a361561";
	
	public final static String TEST_MSG_TEXT = "TESTCOMPONENT_MSG_TYPE_TEXT";
	
	public final static String TEST_MSG_TEXT2 = "QUERY_AUTH_CODE";
	
	public final static String CUSTOM_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
	
	public final static String UPDATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	public final static String JS_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	/* authorization end */
	
	/* member card start */
	public final static String MEMBER_CARD_CREATE_URL = "https://api.weixin.qq.com/card/create?access_token=ACCESS_TOKEN";	//会员卡创建
	/* member card end */

}
