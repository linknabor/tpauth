package com.eshequ.hexie.tpauth.vo.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class JsTicket {

	private int errcode;//0成功
	private String errmsg;//":"ok",
	private String ticket;//bxLdikRXVbTPdHSM05e5u5sUoXNKd8-41ZO3MhKoyN5OfkWITDGgnr2fwJ0m9E8NYzWKVZvdVtaUgWvsdshFKA",
	@JsonProperty("expires_in")
	private int expiresIn;//7200
	private Long createTime; 
	
	public int getErrcode() {
		return errcode;
	}
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public int getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "JsTicket [errcode=" + errcode + ", errmsg=" + errmsg + ", ticket=" + ticket + ", expiresIn=" + expiresIn
				+ ", createTime=" + createTime + "]";
	}
	
	
}
