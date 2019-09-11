package com.eshequ.hexie.tpauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class HexieRedisConfig {

	
	@Value("${hexie.redis.host}")
	private String hexieHost;
	@Value("${hexie.redis.port}")
	private Integer hexiePort;
	@Value("${hexie.redis.password}")
	private String hexiePassword;
	@Value("${hexie.redis.database}")
	private Integer hexieDatabase;
	
	@Autowired
	private StringRedisSerializer stringRedisSerializer;
	
	@Bean(name = "hexieRedisStandaloneConfiguration")
	public RedisStandaloneConfiguration redisStandaloneConfiguration() {

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setDatabase(hexieDatabase);
		redisStandaloneConfiguration.setHostName(hexieHost);
		redisStandaloneConfiguration.setPort(hexiePort);
		redisStandaloneConfiguration.setPassword(RedisPassword.of(hexiePassword));
		return redisStandaloneConfiguration;
	}

	@Bean(name = "hexieLettuceConnectionFactory")
	public LettuceConnectionFactory lettuceConnectionFactory(@Qualifier(value="hexieRedisStandaloneConfiguration") RedisStandaloneConfiguration redisStandaloneConfiguration) {
		LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration);
		return factory;
	}

	/**
	 * GenericJackson2JsonRedisSerializer 从redis里取Long可能会有坑?
	 * @param lettuceConnectionFactory
	 * @return
	 */
	@Bean(name = "hexieRedisTemplate")
	public RedisTemplate<String, Object> redisTemplate(@Qualifier(value="hexieLettuceConnectionFactory") LettuceConnectionFactory lettuceConnectionFactory) {

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		
		Jackson2JsonRedisSerializer<String> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(String.class);
		template.setKeySerializer(stringRedisSerializer);
		template.setValueSerializer(jackson2JsonRedisSerializer);	//有泛型的对象先转换成json字符串再往redis里存，不然反序列化时会报错。
		template.setHashKeySerializer(stringRedisSerializer);
		template.setHashValueSerializer(jackson2JsonRedisSerializer);	//同上
		template.setConnectionFactory(lettuceConnectionFactory);
		return template;
	}

	
	@Bean(name = "hexieStringRedisTemplate")
	public StringRedisTemplate stringRedisTemplate(@Qualifier(value="hexieLettuceConnectionFactory") LettuceConnectionFactory lettuceConnectionFactory) {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(lettuceConnectionFactory);
		return stringRedisTemplate;
	}
	
	
}
