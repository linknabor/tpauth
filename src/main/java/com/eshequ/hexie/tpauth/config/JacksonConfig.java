package com.eshequ.hexie.tpauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Configuration
public class JacksonConfig {

	/**
	 * 单例，直接注入用
	 * @return
	 */
	@Bean
	public ObjectMapper objectMapper() {
		
		ObjectMapper objectMaper =  new ObjectMapper();
		objectMaper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		objectMaper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMaper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
		return objectMaper;
	}
	
}
