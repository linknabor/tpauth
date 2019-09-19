package com.eshequ.hexie.tpauth.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.eshequ.hexie.tpauth.exception.AppSysException;
import com.eshequ.hexie.tpauth.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RestUtil {

	private static Logger logger = LoggerFactory.getLogger(RestUtil.class);
	
	@Autowired
	@Qualifier("restTemplate")
	private RestTemplate restTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public <T> T getByJson(String reqUrl, Class<T> respClazz) {
		
		logger.info("request url : " + reqUrl);
		ResponseEntity<String> resp = restTemplate.getForEntity(reqUrl, String.class, "");
		logger.info("response : " + resp);
		HttpStatus httpStatus = resp.getStatusCode();
		T t = null;
		if (HttpStatus.OK.equals(httpStatus) ) {
			try {
				t = objectMapper.readValue(resp.getBody(), respClazz);
			} catch (Exception e) {
				throw new AppSysException(e);
			}
		}else {
			throw new BusinessException("request failed, code : " + httpStatus);
		}
		return t;
	}
	

	/**
	 * 参数在body中的post
	 * @param reqUrl
	 * @param postData
	 * @param respClazz
	 * @return
	 */
	public <T> T postByJson(String reqUrl, Object object, Class<T> respClazz){
		
		String json = "";
		try {
			json = objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			throw new AppSysException(e);
		}
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> httpEntity = new HttpEntity<>(json, headers);
		
		logger.info("request url : " + reqUrl);
		logger.info("post data : " + httpEntity);
		
		ResponseEntity<String> resp = restTemplate.postForEntity(reqUrl, httpEntity, String.class);	//先用String，接收进来看下response是什么。直接指定泛型可能会转成空
		
		logger.info("response : " + resp);
		HttpStatus httpStatus = resp.getStatusCode();
		T t = null;
		if (HttpStatus.OK.equals(httpStatus) ) {
			try {
				t = objectMapper.readValue(resp.getBody(), respClazz);
			} catch (Exception e) {
				throw new AppSysException(e);
			}
		}else {
			throw new BusinessException("request failed, code : " + httpStatus);
		}
		return t;
		
	}
	
	/**
	 * 参数带在URL上的post
	 * @param reqUrl
	 * @param postData
	 * @param respClazz
	 * @return
	 */
	public <T> T postByForm(String reqUrl, LinkedMultiValueMap<String, String> postData, Class<T> respClazz){
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Object> httpEntity = new HttpEntity<>(postData, headers);
        
        logger.info("request url : " + reqUrl);
        logger.info("post data : " + httpEntity);
        
		ResponseEntity<String> resp = restTemplate.postForEntity(reqUrl, httpEntity, String.class);
		
		logger.info("response : " + resp);
		HttpStatus httpStatus = resp.getStatusCode();
		T t = null;
		if (HttpStatus.OK.equals(httpStatus) ) {
			try {
				t = objectMapper.readValue(resp.getBody(), respClazz);
			} catch (Exception e) {
				throw new AppSysException(e);
			}
		}else {
			throw new BusinessException("request failed, code : " + httpStatus);
		}
		return t;
		
	}
	
	
	
}
