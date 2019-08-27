package com.eshequ.hexie.tpauth.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.eshequ.hexie.tpauth.exception.BusinessException;

@Component
public class RestUtil {

	private static Logger logger = LoggerFactory.getLogger(RestUtil.class);
	
	@Autowired
	@Qualifier("restTemplate")
	private RestTemplate restTemplate;
	

	public <T> T doPost(String reqUrl, Map<String, String> postData, Class<T> respClazz){
		
		logger.info("request url : " + reqUrl);
		logger.info("post data : " + postData);
		
		ResponseEntity resp = restTemplate.postForEntity(reqUrl, postData, Map.class);
		logger.info("response : " + resp);
		HttpStatus httpStatus = resp.getStatusCode();
		if (HttpStatus.OK.equals(httpStatus) ) {
			//do nothing
		}else {
			throw new BusinessException("request failed, code : " + httpStatus);
		}
		return null;
		
	}
	
}
