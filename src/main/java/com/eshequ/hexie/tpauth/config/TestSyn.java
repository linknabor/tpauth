package com.eshequ.hexie.tpauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.eshequ.hexie.tpauth.vo.TestEntity;

@Component
public class TestSyn {

	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	
	@Async
	public TestEntity test(TestEntity testEntity) {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		testEntity.setWuyeId("12345671235");
		redisTemplate.opsForValue().set("myasync", testEntity);
		return testEntity;
	}
	
}
