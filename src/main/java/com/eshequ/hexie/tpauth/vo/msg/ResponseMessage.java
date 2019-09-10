package com.eshequ.hexie.tpauth.vo.msg;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("xml")
public class ResponseMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1382872544724162262L;
	
	private static final String CDATA_BEGIN = "![CDATA[";
	private static final String CDATA_END = "]]";

	@JsonProperty("ToUserName")
	private String toUserName;
	@JsonProperty("FromUserName")
	private String fromUserName;
	@JsonProperty("CreateTime")
	private String createTime;
	@JsonProperty("MsgType")
	private String msgType;
	@JsonProperty("Content")
	private String content;
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = CDATA_BEGIN + toUserName + CDATA_END;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = CDATA_BEGIN + fromUserName + CDATA_END;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = CDATA_BEGIN + msgType + CDATA_END;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = CDATA_BEGIN + content + CDATA_END;
	}
	@Override
	public String toString() {
		return "ResponseMessage [toUserName=" + toUserName + ", fromUserName=" + fromUserName + ", createTime="
				+ createTime + ", msgType=" + msgType + ", content=" + content + "]";
	}
	
	
	
}
