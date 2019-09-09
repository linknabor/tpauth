package com.eshequ.hexie.tpauth.vo.msg;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestMessage implements Serializable {

/**
	 * 
	 */
	private static final long serialVersionUID = 5352122339745453069L;
//	<xml>
//	  <ToUserName><![CDATA[toUser]]></ToUserName>
//	  <FromUserName><![CDATA[fromUser]]></FromUserName>
//	  <CreateTime>1348831860</CreateTime>
//	  <MsgType><![CDATA[text]]></MsgType>
//	  <Content><![CDATA[this is a test]]></Content>
//	  <MsgId>1234567890123456</MsgId>
//	</xml>
	
//	<PicUrl><![CDATA[this is a url]]></PicUrl>
//	  <MediaId><![CDATA[media_id]]></MediaId>
	
//	<Format><![CDATA[Format]]></Format>
//	<Recognition>< ![CDATA[腾讯微信团队] ]></Recognition>
//	<ThumbMediaId><![CDATA[thumb_media_id]]></ThumbMediaId>
	
//	 <Location_X>23.134521</Location_X>
//	  <Location_Y>113.358803</Location_Y>
//	  <Scale>20</Scale>
//	  <Label><![CDATA[位置信息]]></Label>
	
//	<Title><![CDATA[公众平台官网链接]]></Title>
//	  <Description><![CDATA[公众平台官网链接]]></Description>
//	  <Url><![CDATA[url]]></Url>
	
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
	@JsonProperty("MsgId")
	private String msgId;
	@JsonProperty("PicUrl")
	private String picUrl;
	@JsonProperty("MediaId")
	private String mediaId;
	@JsonProperty("Format")
	private String format;
	@JsonProperty("Recongnition")
	private String recongnition;
	@JsonProperty("ThumbMediaId")
	private String thumbMediaId;
	@JsonProperty("Location_X")
	private String locationX;
	@JsonProperty("Location_Y")
	private String locationY;
	@JsonProperty("Scale")
	private String scale;
	@JsonProperty("Label")
	private String label;
	@JsonProperty("Title")
	private String title;
	@JsonProperty("Description")
	private String description;
	@JsonProperty("Url")
	private String url;
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
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
		this.msgType = msgType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getRecongnition() {
		return recongnition;
	}
	public void setRecongnition(String recongnition) {
		this.recongnition = recongnition;
	}
	public String getThumbMediaId() {
		return thumbMediaId;
	}
	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}
	public String getLocationX() {
		return locationX;
	}
	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}
	public String getLocationY() {
		return locationY;
	}
	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}
	public String getScale() {
		return scale;
	}
	public void setScale(String scale) {
		this.scale = scale;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "Message [toUserName=" + toUserName + ", fromUserName=" + fromUserName + ", createTime=" + createTime
				+ ", msgType=" + msgType + ", content=" + content + ", msgId=" + msgId + ", picUrl=" + picUrl
				+ ", mediaId=" + mediaId + ", format=" + format + ", recongnition=" + recongnition + ", thumbMediaId="
				+ thumbMediaId + ", locationX=" + locationX + ", locationY=" + locationY + ", scale=" + scale
				+ ", label=" + label + ", title=" + title + ", description=" + description + ", url=" + url + "]";
	}
	
	
	
}
