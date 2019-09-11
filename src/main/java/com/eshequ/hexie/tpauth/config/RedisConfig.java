package com.eshequ.hexie.tpauth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Value("${redis.host}")
	private String host;
	@Value("${redis.port}")
	private Integer port;
	@Value("${redis.password}")
	private String password;
	@Value("${redis.database}")
	private Integer database;
	
	@Bean
	public RedisStandaloneConfiguration redisStandaloneConfiguration() {

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setDatabase(database);
		redisStandaloneConfiguration.setHostName(host);
		redisStandaloneConfiguration.setPort(port);
		redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
		return redisStandaloneConfiguration;
	}

	@Bean
	@Primary
	public LettuceConnectionFactory lettuceConnectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration) {
		LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration);
		return factory;
	}

	/**
	 * GenericJackson2JsonRedisSerializer 从redis里取Long可能会有坑?
	 * @param lettuceConnectionFactory
	 * @return
	 */
	@Bean(name = "redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
		template.setKeySerializer(stringRedisSerializer);
		template.setValueSerializer(genericJackson2JsonRedisSerializer);	//有泛型的对象先转换成json字符串再往redis里存，不然反序列化时会报错。
		template.setHashKeySerializer(stringRedisSerializer);
		template.setHashValueSerializer(genericJackson2JsonRedisSerializer);	//同上
		template.setConnectionFactory(lettuceConnectionFactory);
		return template;
	}

	@Bean(name = "stringRedisTemplate")
	public StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(lettuceConnectionFactory);
		return stringRedisTemplate;
	}
	
	@Bean
	public StringRedisSerializer stringRedisSerializer() {
		
		StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
		return stringRedisSerializer;
		
	}
	
	

}
