package com.prototype.microservice.etl;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EntityScan(
        basePackageClasses = {EtlServiceApp.class, Jsr310JpaConverters.class}
)

public class EtlServiceApp {

	private static final Logger LOG = LoggerFactory.getLogger(EtlServiceApp.class);

	public static void main(final String[] args) {
		SpringApplication.run(EtlServiceApp.class, args);
		if (LOG.isInfoEnabled()) {
			LOG.info(MessageFormat.format("{0} is running.", EtlServiceApp.class.getName()));
		}
	}
}
