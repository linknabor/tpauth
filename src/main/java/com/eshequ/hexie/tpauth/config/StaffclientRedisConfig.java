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
public class StaffclientRedisConfig {

	
	@Value("${staffclient.redis.host}")
	private String redisHost;
	@Value("${staffclient.redis.port}")
	private Integer redisPort;
	@Value("${staffclient.redis.password}")
	private String redisPassword;
	@Value("${staffclient.redis.database}")
	private Integer redisDatabase;
	
	@Autowired
	private StringRedisSerializer stringRedisSerializer;
	
	@Bean(name = "staffclientRedisStandaloneConfiguration")
	public RedisStandaloneConfiguration redisStandaloneConfiguration() {

		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setDatabase(redisDatabase);
		redisStandaloneConfiguration.setHostName(redisHost);
		redisStandaloneConfiguration.setPort(redisPort);
		redisStandaloneConfiguration.setPassword(RedisPassword.of(redisPassword));
		return redisStandaloneConfiguration;
	}

	@Bean(name = "staffclientLettuceConnectionFactory")
	public LettuceConnectionFactory lettuceConnectionFactory(@Qualifier(value="staffclientRedisStandaloneConfiguration") RedisStandaloneConfiguration redisStandaloneConfiguration) {
		LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration);
		return factory;
	}

	/**
	 * GenericJackson2JsonRedisSerializer 从redis里取Long可能会有坑?
	 * @param lettuceConnectionFactory
	 * @return
	 */
	@Bean(name = "staffclientRedisTemplate")
	public RedisTemplate<String, Object> redisTemplate(@Qualifier(value="staffclientLettuceConnectionFactory") LettuceConnectionFactory lettuceConnectionFactory) {

		RedisTemplate<String, Object> template = new RedisTemplate<>();
		
		Jackson2JsonRedisSerializer<String> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(String.class);
		template.setKeySerializer(stringRedisSerializer);
		template.setValueSerializer(jackson2JsonRedisSerializer);	//有泛型的对象先转换成json字符串再往redis里存，不然反序列化时会报错。
		template.setHashKeySerializer(stringRedisSerializer);
		template.setHashValueSerializer(jackson2JsonRedisSerializer);	//同上
		template.setConnectionFactory(lettuceConnectionFactory);
		return template;
	}

	
	@Bean(name = "staffclientStringRedisTemplate")
	public StringRedisTemplate stringRedisTemplate(@Qualifier(value="staffclientLettuceConnectionFactory") LettuceConnectionFactory lettuceConnectionFactory) {
		StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
		stringRedisTemplate.setConnectionFactory(lettuceConnectionFactory);
		return stringRedisTemplate;
	}
	
	
}
