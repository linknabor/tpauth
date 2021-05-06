package com.eshequ.hexie.tpauth.vo.corp;

import java.io.Serializable;

public class CorpEventRequest implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -667726145826116867L;
	
	private String msg_signature;
	private String timestamp;
	private String nonce;
	private String echostr;
	
	public String getMsg_signature() {
		return msg_signature;
	}
	public void setMsg_signature(String msg_signature) {
		this.msg_signature = msg_signature;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getEchostr() {
		return echostr;
	}
	public void setEchostr(String echostr) {
		this.echostr = echostr;
	}
	@Override
	public String toString() {
		return "CorpEventRequest [msg_signature=" + msg_signature + ", timestamp=" + timestamp + ", nonce=" + nonce
				+ ", echostr=" + echostr + "]";
	}
	
	
}
