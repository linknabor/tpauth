package com.eshequ.hexie.tpauth.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WechatResponse implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7712305882439130175L;
	@JsonProperty("errcode")
	private String errCode;
	
	@JsonProperty("errmsg")
	private String errMsg;

	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	@Override
	public String toString() {
		return "WechatResponse [errCode=" + errCode + ", errMsg=" + errMsg + "]";
	}
	
	
}
