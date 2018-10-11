package com.prototype.microservice.edge;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.retry.annotation.EnableRetry;

import com.prototype.microservice.commons.utils.SecurityUtils;

@SpringBootApplication
@EnableZuulProxy
@EnableRetry
public class EdgeServerApp {

	private final static Logger LOG = LoggerFactory.getLogger(EdgeServerApp.class);

	public static void main(String[] args) {
		SpringApplication.run(EdgeServerApp.class, args);
		try {
			if (LOG.isInfoEnabled()) {
				LOG.info("Trusting self-signed certs...");
			}
			SecurityUtils.trustSelfSignedCerts();
			if (LOG.isInfoEnabled()) {
				LOG.info("Trusting self-signed certs done!");
			}
		} catch (Exception e) {
			LOG.error("Cannot trust self-signed certs due to error(s)...", e);
		}
		if (LOG.isInfoEnabled()) {
			LOG.info(MessageFormat.format("{0} is running.", new Object[] { EdgeServerApp.class.getName() }));
		}
	}

}
