package com.eshequ.hexie.tpauth.schedule.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eshequ.hexie.tpauth.common.Constants;
import com.eshequ.hexie.tpauth.schedule.ScheduleService;
import com.eshequ.hexie.tpauth.service.AuthService;
import com.eshequ.hexie.tpauth.vo.AuthEvent;
import com.eshequ.hexie.tpauth.vo.AuthorizerAccessToken;
import com.eshequ.hexie.tpauth.vo.ComponentAcessToken;
import com.eshequ.hexie.tpauth.vo.ComponentVerifyTicket;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	private static Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private AuthService authService;
	
	
	/**
	 * 获取componentAccessToken(平台token)，每隔5分钟一次，如果token未超时，则不更新。
	 * token每2小时超时，大于1小时50分的时候更新
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
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
			logger.info("save component access token into redis .");
		}
		logger.info("end checking component access token.");
		
	}
	
	/**
	 * 获取authorizerAccessToken(被授权方的 token)，每隔5分钟一次，如果token未超时，则不更新。
	 * token每2小时超时，大于1小时50分的时候更新
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
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
				boolean updateFlag = false;
				AuthorizerAccessToken aat = (AuthorizerAccessToken) redisTemplate.opsForValue().get(Constants.KEY_AUTHORIZER_ACCESS_TOKEN + authAppid);
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
					redisTemplate.opsForValue().set(Constants.KEY_AUTHORIZER_ACCESS_TOKEN + authAppid, aat);
					logger.info("save authorizer access token into redis, auth appId : " + authAppid);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
		
		logger.info("end checking authorizer access token.");
		
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

}
