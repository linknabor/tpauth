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
	 * 获取componentAccessToken，每隔10分钟一次，如果token未超时，则不更新。
	 * token每2小时超时，大于1小时50分的时候更新
	 */
	@Scheduled(cron = "0 0/5 * * * ?")
	@Override
	public void updateComponentAccessToken() {
		
		logger.info("start to check component access token.");
		ComponentAcessToken cat = (ComponentAcessToken) redisTemplate.opsForValue().get(Constants.COMPONENT_ACCESS_TOKEN);
		boolean updateFlag = false;
		if (cat == null) {
			updateFlag = true;
		}else {
			Long createTime = cat.getCreateTime();
			Long currentTime = System.currentTimeMillis();
			if (currentTime - createTime > ((60+50)*60*1000)) {
				updateFlag = true;
			}
		}
		if (updateFlag) {
			ComponentVerifyTicket verifyTicket = (ComponentVerifyTicket) redisTemplate.opsForValue().get(Constants.VERIFY_TICKET);
			if (verifyTicket == null || StringUtils.isEmpty(verifyTicket.getVerifyTicket())) {
				logger.warn("no verify ticket .");
				return;
			}
			cat = authService.getComponentAccessToken(verifyTicket.getVerifyTicket());
			cat.setCreateTime(System.currentTimeMillis());
			redisTemplate.opsForValue().set(Constants.COMPONENT_ACCESS_TOKEN, cat);
			logger.info("save component access token into redis .");
		}
		logger.info("end checking component access token.");
		
	}

}
