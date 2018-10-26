package com.prototype.microservice.etl.cdms.config;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		entityManagerFactoryRef = "cdmsEntityManagerFactory",
		transactionManagerRef = "cdmsEntityManager",
		basePackages = {"com.prototype.microservice.etl.cdms.repository" })
@EnableScheduling
@ComponentScan({ "com.prototype.etl.tasks" })

public class CdmsDBConfig {

	@Bean(name = "dataSource-cdms")
	@ConfigurationProperties(prefix = "spring.datasource-cdms")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@PersistenceContext(unitName = "cdms")
	@Bean(name = "cdmsEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("dataSource-cdms") DataSource dataSource) {
		return builder.dataSource(dataSource).packages("com.prototype.microservice.etl.cdms.domain")
				.persistenceUnit("cdms").build();
	}

	@Bean(name = "cdmsEntityManager")
	public PlatformTransactionManager entityTransactionManager(
			@Qualifier("cdmsEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
}
