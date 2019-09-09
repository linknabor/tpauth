package com.eshequ.hexie.tpauth.vo.auth;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 授权公众号的 accessToken
 * @author david
 *
 */
public class AuthorizerAccessToken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4621675941539349184L;
	
	@JsonProperty("authorizer_access_token")
	private String authorizerAccessToken;
	
	@JsonProperty("expires_in")
	private String expiresIn;
	
	@JsonProperty("authorizer_refresh_token")
	private String authorizerRefreshToken;
	
	private Long createTime;

	public String getAuthorizerAccessToken() {
		return authorizerAccessToken;
	}

	public void setAuthorizerAccessToken(String authorizerAccessToken) {
		this.authorizerAccessToken = authorizerAccessToken;
	}

	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getAuthorizerRefreshToken() {
		return authorizerRefreshToken;
	}

	public void setAuthorizerRefreshToken(String authorizerRefreshToken) {
		this.authorizerRefreshToken = authorizerRefreshToken;
	}
	
	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "AuthorizerAccessToken [authorizerAccessToken=" + authorizerAccessToken + ", expiresIn=" + expiresIn
				+ ", authorizerRefreshToken=" + authorizerRefreshToken + ", createTime=" + createTime + "]";
	}

	

}
