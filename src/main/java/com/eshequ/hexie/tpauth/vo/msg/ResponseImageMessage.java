package com.eshequ.hexie.tpauth.vo.msg;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("xml")
public class ResponseImageMessage implements Serializable {

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
	@JsonProperty("Image")
	private Image image;
	
	public static class Image{
		@JsonProperty("MediaId")
		private String mediaId;

		public String getMediaId() {
			return mediaId;
		}

		public void setMediaId(String mediaId) {
			this.mediaId = CDATA_BEGIN + mediaId  + CDATA_END;
		}
	}
	
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
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	
	
}
