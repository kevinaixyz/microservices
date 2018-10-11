package com.prototype.microservice.eureka_server;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApp {

	private final static Logger LOG = LoggerFactory.getLogger(EurekaServerApp.class);

	public static void main(String[] args) {
		if (LOG.isInfoEnabled()) {
			LOG.info(MessageFormat.format(
					"Staring up {0}...",
					new Object[] { EurekaServerApp.class.getName() }));
		}
		SpringApplication.run(EurekaServerApp.class, args);
	}

}
