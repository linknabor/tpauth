package com.eshequ.hexie.tpauth.schedule.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eshequ.hexie.tpauth.common.Constants;
import com.eshequ.hexie.tpauth.common.WechatConfig;
import com.eshequ.hexie.tpauth.schedule.ScheduleService;
import com.eshequ.hexie.tpauth.service.AuthService;
import com.eshequ.hexie.tpauth.util.RestUtil;
import com.eshequ.hexie.tpauth.vo.auth.AuthEvent;
import com.eshequ.hexie.tpauth.vo.auth.AuthorizerAccessToken;
import com.eshequ.hexie.tpauth.vo.auth.ComponentAcessToken;
import com.eshequ.hexie.tpauth.vo.auth.ComponentVerifyTicket;
import com.eshequ.hexie.tpauth.vo.auth.JsTicket;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	private static Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);
	
	@Autowired
	@Qualifier(value = "redisTemplate")
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	@Qualifier(value = "hexieRedisTemplate")
	private RedisTemplate<String, Object> hexieRedisTemplate;
	
	@Autowired
	private AuthService authService;
	
	@Autowired
	private RestUtil restUtil;
	
	
	/**
	 * 获取componentAccessToken(平台token)，每隔5分钟一次，如果token未超时，则不更新。
	 * token每2小时超时，大于1小时50分的时候更新
	 */
	@Scheduled(cron = "0 0/30 * * * ?")
	@Override
	public void updateComponentAccessToken() {
		
		logger.info("start to check component access token.");
		ComponentAcessToken cat = authService.getComponentAcessTokenFromCache();
		boolean updateFlag = false;
		if (cat == null || StringUtils.isEmpty(cat.getComponentAcessToken())) {
			updateFlag = true;
		}else {
			Long createTime = cat.getCreateTime();
			Long currentTime = System.currentTimeMillis();
			//超过1小时50分钟更新
			if (currentTime - createTime > ((60+50)*60*1000)) {
				updateFlag = true;
			}
		}
		if (updateFlag) {
			ComponentVerifyTicket verifyTicket = (ComponentVerifyTicket) redisTemplate.opsForValue().get(Constants.KEY_VERIFY_TICKET);
			if (verifyTicket == null || StringUtils.isEmpty(verifyTicket.getVerifyTicket())) {
				logger.warn("no verify ticket .");
				return;
			}
			cat = authService.getComponentAccessToken(verifyTicket.getVerifyTicket());
			cat.setCreateTime(System.currentTimeMillis());
			redisTemplate.opsForValue().set(Constants.KEY_COMPONENT_ACCESS_TOKEN, cat);
			hexieRedisTemplate.opsForValue().set(Constants.KEY_COMPONENT_ACCESS_TOKEN, cat.getComponentAcessToken());
			logger.info("save component access token into redis .");
		}
		logger.info("end checking component access token.");
		
	}
	
	/**
	 * 获取authorizerAccessToken(被授权方的 token)，每隔5分钟一次，如果token未超时，则不更新。
	 * token每2小时超时，大于1小时50分的时候更新
	 */
	@Scheduled(cron = "0 0/30 * * * ?")
	@Override
	public void updateAuthorizerAccessToken() {
		
		logger.info("start to check authorizer access token.");
		String authList = (String) redisTemplate.opsForValue().get(Constants.KEY_AUTHORIZER_LIST);
		if (StringUtils.isEmpty(authList)) {
			logger.warn("no auth list !");
			return;
		}
		String[]authAppids = authList.split(",");
		logger.info("auth list is :" + authList);
		for (String authAppid : authAppids) {
			try {
				String authTokenKey = Constants.KEY_AUTHORIZER_ACCESS_TOKEN + authAppid;
				boolean updateFlag = false;
				AuthorizerAccessToken aat = (AuthorizerAccessToken) redisTemplate.opsForValue().get(authTokenKey);
				if (aat == null || StringUtils.isEmpty(aat.getAuthorizerAccessToken())) {
					updateFlag = true;
				}else {
					Long createTime = aat.getCreateTime();
					Long currentTime = System.currentTimeMillis();
					//超过1小时50分钟更新
					if (currentTime - createTime > ((60+50)*60*1000)) {
						updateFlag = true;
					}
				}
				if (updateFlag) {
					if (aat == null || StringUtils.isEmpty(aat.getAuthorizerRefreshToken())) {
						logger.warn("no authorizer refresh token. ");	//需要人工找到缓存里或者文件里的refresh token,重新set到redis里
						continue;
					}
					aat = authService.getAuthorizerAccessToken(authAppid, aat.getAuthorizerRefreshToken());
					aat.setCreateTime(System.currentTimeMillis());
					redisTemplate.opsForValue().set(authTokenKey, aat);
					try {
						hexieRedisTemplate.opsForValue().set(authTokenKey, aat.getAuthorizerAccessToken());	//给合协公众号设置授权了的AccessToken
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					logger.info("save authorizer access token into redis, auth appId : " + authAppid);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		logger.info("end checking authorizer access token.");
		
	}
	
	/**
	 * 获取js ticket(被授权方的 token)，每隔5分钟一次，如果token未超时，则不更新。
	 * 每2小时超时，大于1小时50分的时候更新
	 */
	@Scheduled(cron = "0 1/30 * * * ?")
	@Override
	public void updateAuthorizerJsTicket() {
		
		logger.info("start to check authorizer js ticket .");
		String authList = (String) redisTemplate.opsForValue().get(Constants.KEY_AUTHORIZER_LIST);
		if (StringUtils.isEmpty(authList)) {
			logger.warn("no auth list !");
			return;
		}
		String[]authAppids = authList.split(",");
		logger.info("auth list is :" + authList);
		for (String authAppid : authAppids) {
			try {
				String jsKey = Constants.KEY_AUTHORIZER_JS_TICKET + authAppid;
				boolean updateFlag = false;
				JsTicket jsTicket = (JsTicket) redisTemplate.opsForValue().get(jsKey);
				if (jsTicket == null || StringUtils.isEmpty(jsTicket.getTicket())) {
					updateFlag = true;
				}else {
					Long createTime = jsTicket.getCreateTime();
					Long currentTime = System.currentTimeMillis();	
					//超过1小时50分钟更新
					if (currentTime - createTime > ((60+50)*60*1000)) {
						updateFlag = true;
					}
				}
				if (updateFlag) {
					AuthorizerAccessToken aat = authService.getAuthorizerAccessTokenFromCache(authAppid);
					if (aat == null || StringUtils.isEmpty(aat.getAuthorizerAccessToken())) {
						logger.warn("no authorizer acess token. ");	//需要人工找到缓存里或者文件里的refresh token,重新set到redis里
						continue;
					}
					jsTicket = queryJsTicket(aat.getAuthorizerAccessToken());
					redisTemplate.opsForValue().set(jsKey, jsTicket);
					try {
						hexieRedisTemplate.opsForValue().set(jsKey, jsTicket.getTicket());	//给合协公众号设置授权了的AccessToken
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					logger.info("save authorizer js ticket into redis, auth appId : " + authAppid);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		logger.info("end checking authorizer js ticket.");
		
	}
	
	
	
	@Scheduled(cron = "0 0/1 * * * ?")
	@Override
	public void handleAuthQueue() {
		
		AuthEvent authEvent = (AuthEvent) redisTemplate.opsForList().leftPop(Constants.KEY_AUTH_QUEUE);
		if (authEvent == null || StringUtils.isEmpty(authEvent.getAuthorizationCode())) {
			return;
		}
		logger.info("start to get authorization info ... ");
		authService.authorizationInfo(authEvent.getAuthorizationCode(), authEvent.getAuthorizerAppid());
		logger.info("end getting authorization info ... ");
	}
	
	/**
	 * 从微信获取js ticket
	 * @param authorizerAccessToken
	 */
	private JsTicket queryJsTicket(String authorizerAccessToken) {
		
		String reqUrl = WechatConfig.JS_TICKET_URL;
		reqUrl = reqUrl.replaceAll("ACCESS_TOKEN", authorizerAccessToken);
		JsTicket jsTicket = restUtil.getByJson(reqUrl, JsTicket.class);
		jsTicket.setCreateTime(System.currentTimeMillis());
		return jsTicket;
		
	}

}
