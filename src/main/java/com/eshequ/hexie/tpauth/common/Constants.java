package com.eshequ.hexie.tpauth.common;

import java.util.ArrayList;
import java.util.List;

public class Constants {

	public static final String VERIFY_TICKET = "oaAuth_verifyTicket";
	
	public static final String COMPONENT_ACCESS_TOKEN = "oaAuth_componentAccessToken";
	
	public static final String PRE_AUTH_CODE = "oaAuth_preAuthCode";
	
	public static List<String> mobileDevices;
	
	static {
		
		mobileDevices = new ArrayList<String>();
		mobileDevices.add("iPhone");
		mobileDevices.add("Android");
		mobileDevices.add("iPad ");
		mobileDevices.add("windows phone");
		
	}
}
