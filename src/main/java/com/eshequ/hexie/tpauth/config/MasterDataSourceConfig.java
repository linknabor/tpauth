/**
 * 
 */
package com.eshequ.hexie.tpauth.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

import tk.mybatis.spring.annotation.MapperScan;

/**
 * 主数据源
 * 
 * @author davidhardson
 *
 */
@Configuration
@MapperScan(basePackages = { "com.eshequ.hexie.tpauth.mapper.normal",
		"com.eshequ.hexie.tpauth.mapper.custom", }, sqlSessionFactoryRef = "masterSqlSessionFactory")
public class MasterDataSourceConfig {

	@Value("${mybatis.mapper.resource}")
	private String mapperResource;

	@Autowired
	private Environment env;

	@Bean(name = "masterDataSource")
	@Primary
	public DataSource masterDataSource() {
		DataSource dataSource = DruidDataSourceBuilder.create().build(env, "spring.datasource.druid.one.");
		return dataSource;
	}

	@Bean(name = "masterTransactionManager")
	@Primary
	public DataSourceTransactionManager masterTransactionManager() {
		return new DataSourceTransactionManager(masterDataSource());
	}

	@Bean(name = "masterSqlSessionFactory")
	@Primary
	public SqlSessionFactory masterSqlSessionFactory(@Qualifier("masterDataSource") DataSource masterDataSource)
			throws Exception {

		SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
		sessionFactory.setDataSource(masterDataSource);
		PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		String[] resources = mapperResource.split(",");
		for (String resource : resources) {
			resource = resource.trim();
			sessionFactory.setMapperLocations(resolver.getResources(resource));
		}
		return sessionFactory.getObject();
	}

}
