package com.eshequ.hexie.tpauth.vo.msg;

import java.io.Serializable;

/**
 * 客服消息
 * @author david
 *
 */
public class CsMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -27537200803964244L;
	private String touser;	//openid
	private String msgtype;
	private CsText text;
	
	public static class CsText{
		
		private String content;

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
		
	}

	public String getTouser() {
		return touser;
	}

	public void setTouser(String touser) {
		this.touser = touser;
	}

	public String getMsgtype() {
		return msgtype;
	}

	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}

	public CsText getText() {
		return text;
	}

	public void setText(CsText text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "CsMessage [touser=" + touser + ", msgtype=" + msgtype + ", text=" + text + "]";
	}
	
	
	
}
