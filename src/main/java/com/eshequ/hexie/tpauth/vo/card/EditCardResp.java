package com.eshequ.hexie.tpauth.vo.card;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EditCardResp implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5833967728621667830L;
	
	private String errcode;
	private String errmsg;
	@JsonProperty("send_check")
	private String sendCheck;	//此次更新是否需要提审，true为需要，false为不需要
	
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getSendCheck() {
		return sendCheck;
	}
	public void setSendCheck(String sendCheck) {
		this.sendCheck = sendCheck;
	}
	@Override
	public String toString() {
		return "EditCardResp [errcode=" + errcode + ", errmsg=" + errmsg + ", sendCheck=" + sendCheck + "]";
	}
	
	

}
