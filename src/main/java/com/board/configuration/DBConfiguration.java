package com.board.configuration;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@PropertySource("classpath:/application.properties")
@EnableTransactionManagement
public class DBConfiguration {

	@Autowired
	private ApplicationContext applicationContext;
	// Spring Container 중 하나

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.hikari")
	// @PropertySource에 지정된 파일(application.properties)에서
	// prefix에 해당하는 설정을 모두 읽어 해당 메서드에 매핑
	public HikariConfig hikariConfig() {
		// HiKari Connection Pool 객체 생성

		return new HikariConfig();
	}

	@Bean
	public DataSource dataSource() {
		// DataSource 객체 생성 (Connection Pool을 지원하는 인터페이스)

		return new HikariDataSource(hikariConfig());
	}
	
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		// SqlSessionFactory 객체 생성

		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource());
		factoryBean.setMapperLocations(applicationContext.getResources("classpath:/mappers/**/*Mapper.xml"));
		factoryBean.setTypeAliasesPackage("com.board.domain");
		factoryBean.setConfiguration(mybatisConfg());
		// 마이바티스와 스프링의 연동 모듈

		return factoryBean.getObject();
		// Bean 자체가 아닌, getObject 메서드가 리턴하는 sqlSessionFactory를 생성
	}

	@Bean
	@ConfigurationProperties(prefix = "mybatis.configuration")
	public org.apache.ibatis.session.Configuration mybatisConfg() {
		return new org.apache.ibatis.session.Configuration();
	}

	@Bean
	public SqlSessionTemplate sqlSession() throws Exception {
		// sqlSession 객체 생성

		return new SqlSessionTemplate(sqlSessionFactory());
	}

}
