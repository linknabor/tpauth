package com.eshequ.hexie.tpauth.vo.subscribemsg;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "xml")
public class EventPopup implements Serializable {
	
//	<xml>
//    <ToUserName><![CDATA[gh_123456789abc]]></ToUserName>
//    <FromUserName><![CDATA[otFpruAK8D-E6EfStSYonYSBZ8_4]]></FromUserName>
//    <CreateTime>1610969440</CreateTime>
//    <MsgType><![CDATA[event]]></MsgType>
//    <Event><![CDATA[subscribe_msg_popup_event]]></Event>
//    <SubscribeMsgPopupEvent>
//        <List>
//            <TemplateId><![CDATA[VRR0UEO9VJOLs0MHlU0OilqX6MVFDwH3_3gz3Oc0NIc]]></TemplateId>
//            <SubscribeStatusString><![CDATA[accept]]></SubscribeStatusString>
//            <PopupScene>2</PopupScene>
//        </List>
//        <List>
//            <TemplateId><![CDATA[9nLIlbOQZC5Y89AZteFEux3WCXRRRG5Wfzkpssu4bLI]]></TemplateId>
//            <SubscribeStatusString><![CDATA[reject]]></SubscribeStatusString>
//            <PopupScene>2</PopupScene>
//        </List>
//    </SubscribeMsgPopupEvent>
//</xml>

	/**
	 * 
	 */
	private static final long serialVersionUID = 7133555912877861267L;
	
	@JacksonXmlProperty(localName = "ToUserName")
	private String toUserName;
	@JacksonXmlProperty(localName = "FromUserName")
	private String fromUserName;
	@JacksonXmlProperty(localName = "CreateTime")
	private String createTime;
	@JacksonXmlProperty(localName = "MsgType")
	private String msgType;
	@JacksonXmlProperty(localName = "Event")
	private String event;
	@JacksonXmlElementWrapper(localName = "SubscribeMsgPopupEvent")
	@JacksonXmlProperty(localName = "List")
	private List<TemplateDetail> list;
	
	public static class TemplateDetail{
		
		@JacksonXmlProperty(localName = "TemplateId")
		private String templateId;
		@JacksonXmlProperty(localName = "SubscribeStatusString")
		private String subscribeStatusString;
		@JacksonXmlProperty(localName = "PopupScene")
		private String popupScene;
		
		public TemplateDetail() {
			super();
		}
		public TemplateDetail(String templateId, String subscribeStatusString, String popupScene) {
			super();
			this.templateId = templateId;
			this.subscribeStatusString = subscribeStatusString;
			this.popupScene = popupScene;
		}
		public String getTemplateId() {
			return templateId;
		}
		public void setTemplateId(String templateId) {
			this.templateId = templateId;
		}
		public String getSubscribeStatusString() {
			return subscribeStatusString;
		}
		public void setSubscribeStatusString(String subscribeStatusString) {
			this.subscribeStatusString = subscribeStatusString;
		}
		public String getPopupScene() {
			return popupScene;
		}
		public void setPopupScene(String popupScene) {
			this.popupScene = popupScene;
		}
		@Override
		public String toString() {
			return "TemplateDetail [templateId=" + templateId + ", subscribeStatusString=" + subscribeStatusString
					+ ", popupScene=" + popupScene + "]";
		}
		
	}

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

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return "EventPopup [toUserName=" + toUserName + ", fromUserName=" + fromUserName + ", createTime=" + createTime
				+ ", msgType=" + msgType + ", event=" + event + ", list=" + list + "]";
	}


}
