package com.eshequ.hexie.tpauth.vo;

import java.io.Serializable;

public class EventRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7804094185777281779L;
	
	private String signature;
	private String timestamp;
	private String nonce;
	private String encrypt_type;
	private String msg_signature;
	private String postData;
	private String appId;
	
	public EventRequest(String appId, String signature, String timestamp, String nonce, String encrypt_type, String msg_signature,
			String postData) {
		super();
		this.signature = signature;
		this.timestamp = timestamp;
		this.nonce = nonce;
		this.encrypt_type = encrypt_type;
		this.msg_signature = msg_signature;
		this.postData = postData;
		this.appId = appId;
	}
	
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
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
	public String getEncrypt_type() {
		return encrypt_type;
	}
	public void setEncrypt_type(String encrypt_type) {
		this.encrypt_type = encrypt_type;
	}
	public String getMsg_signature() {
		return msg_signature;
	}
	public void setMsg_signature(String msg_signature) {
		this.msg_signature = msg_signature;
	}
	public String getPostData() {
		return postData;
	}
	public void setPostData(String postData) {
		this.postData = postData;
	}
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Override
	public String toString() {
		return "EventRequest [signature=" + signature + ", timestamp=" + timestamp + ", nonce=" + nonce
				+ ", encrypt_type=" + encrypt_type + ", msg_signature=" + msg_signature + ", postData=" + postData
				+ ", appId=" + appId + "]";
	}

	
	
}
