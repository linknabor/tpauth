package com.eshequ.hexie.tpauth.vo.auth;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 授权相关的回调事件
 * @author david
 *
 */
public class AuthEvent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1775085379107959418L;

	@JsonProperty("AppId")
	private String appId;
	
	@JsonProperty("CreateTime")
	private String createTime;
	
	@JsonProperty("InfoType")
	private String infoType;
	
	@JsonProperty("AuthorizerAppid")
	private String authorizerAppid;	//被授权的公众号appid
	
	@JsonProperty("AuthorizationCode")
	private String authorizationCode;	//授权码
	
	@JsonProperty("AuthorizationCodeExpiredTime")
	private String authorizationCodeExpiredTime;	//授权码过期事件

	@JsonProperty("PreAuthCode")
	private String preAuthCode;	//预授权码

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public String getAuthorizerAppid() {
		return authorizerAppid;
	}

	public void setAuthorizerAppid(String authorizerAppid) {
		this.authorizerAppid = authorizerAppid;
	}

	public String getAuthorizationCode() {
		return authorizationCode;
	}

	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	public String getAuthorizationCodeExpiredTime() {
		return authorizationCodeExpiredTime;
	}

	public void setAuthorizationCodeExpiredTime(String authorizationCodeExpiredTime) {
		this.authorizationCodeExpiredTime = authorizationCodeExpiredTime;
	}

	public String getPreAuthCode() {
		return preAuthCode;
	}

	public void setPreAuthCode(String preAuthCode) {
		this.preAuthCode = preAuthCode;
	}

	@Override
	public String toString() {
		return "Authorization [appId=" + appId + ", createTime=" + createTime + ", infoType=" + infoType
				+ ", authorizerAppid=" + authorizerAppid + ", authorizationCode=" + authorizationCode
				+ ", authorizationCodeExpiredTime=" + authorizationCodeExpiredTime + ", preAuthCode=" + preAuthCode
				+ "]";
	}

	

}
