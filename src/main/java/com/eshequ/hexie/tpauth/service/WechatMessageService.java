package com.eshequ.hexie.tpauth.service;

import com.eshequ.hexie.tpauth.vo.EventRequest;

public interface WechatMessageService {

	String handleMsgEvent(EventRequest eventRequest);
}
