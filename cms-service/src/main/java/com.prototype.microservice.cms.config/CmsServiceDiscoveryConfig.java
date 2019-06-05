package com.prototype.microservice.cms.config;

import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Service discovery config.
 */
@Profile("!test") // disable service discovery in junit
@Configuration
@EnableEurekaClient
public class CmsServiceDiscoveryConfig {

}
