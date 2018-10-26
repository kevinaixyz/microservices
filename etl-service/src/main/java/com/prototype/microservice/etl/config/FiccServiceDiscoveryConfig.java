package com.prototype.microservice.etl.config;


import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Service discovery config.
 *
 * @author Danny Tse
 *
 */
@Profile("!test") // disable service discovery in junit
@Configuration
@EnableEurekaClient
public class etlServiceDiscoveryConfig {

}
