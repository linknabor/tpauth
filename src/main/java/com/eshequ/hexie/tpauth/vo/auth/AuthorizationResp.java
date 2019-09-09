package com.eshequ.hexie.tpauth.vo.auth;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1775085379107959418L;
	
	@JsonProperty("authorization_info")
	private AuthorizationInfo authorizationInfo;

	public AuthorizationInfo getAuthorizationInfo() {
		return authorizationInfo;
	}

	public void setAuthorizationInfo(AuthorizationInfo authorizationInfo) {
		this.authorizationInfo = authorizationInfo;
	}

	@Override
	public String toString() {
		return "Authorization [authorizationInfo=" + authorizationInfo + "]";
	}
	
	

}
