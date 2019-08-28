package com.eshequ.hexie.tpauth.vo;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 授权信息
 * @author david
 *
 */
public class AuthorizationInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6468079922096840887L;
	
	@JsonProperty("authorizer_appid")
	private String authorizerAppid;
	
	@JsonProperty("authorizer_access_token")
	private String authorizerAccessToken;
	
	@JsonProperty("expires_in")
	private String expiresIn;
	
	@JsonProperty("authorizer_refresh_token")
	private String authorizerRefreshToken;
	
	@JsonProperty("func_info")
	private List<FuncscopeCategory> funcInfo;
	
	class FuncscopeCategory {
		
		private String id;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
		
	}

	public String getAuthorizerAppid() {
		return authorizerAppid;
	}

	public void setAuthorizerAppid(String authorizerAppid) {
		this.authorizerAppid = authorizerAppid;
	}

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

	public List<FuncscopeCategory> getFuncInfo() {
		return funcInfo;
	}

	public void setFuncInfo(List<FuncscopeCategory> funcInfo) {
		this.funcInfo = funcInfo;
	}

	@Override
	public String toString() {
		return "AuthorizationInfo [authorizerAppid=" + authorizerAppid + ", authorizerAccessToken="
				+ authorizerAccessToken + ", expiresIn=" + expiresIn + ", authorizerRefreshToken="
				+ authorizerRefreshToken + ", funcInfo=" + funcInfo + "]";
	}
	
	

}
