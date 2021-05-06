package com.eshequ.hexie.tpauth.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.eshequ.hexie.tpauth.util.wechat.WXBizMsgCrypt;

@Configuration
public class CorpMsgConfig {
	
	private static Logger logger = LoggerFactory.getLogger(CorpMsgConfig.class);

	@Value("${corp.corpid}")
	private String corpID;
	
	@Value("${corp.providerSecret}")
	private String providerSecret;
	
	@Value("${corp.token}")
	private String token;
	
	@Value("${corp.aeskey}")
	private String aeskey;
	
	@Bean(name = "CorpWxBizMsgCrypt")
	public WXBizMsgCrypt corpWXBizMsgCrypt() {
		
		WXBizMsgCrypt msgCrypt = null;
		try {
			msgCrypt = new WXBizMsgCrypt(providerSecret, aeskey, corpID);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return msgCrypt;
	}
	
}
