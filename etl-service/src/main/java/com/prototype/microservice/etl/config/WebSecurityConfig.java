package com.prototype.microservice.etl.config;

import java.text.MessageFormat;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.util.Base64Utils;

import com.prototype.microservice.commons.utils.SecurityUtils;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final static Logger LOG = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Value("${etl-service.trust-self-signed-certs:false}")
    private boolean trustSelfSignedCerts;

    @Value("${etl-service.users.admin.username:admin}")
    private String adminUsername;

    @Value("${etl-service.users.admin.password}")
    private String adminPassword;

    @Value("${etl-service.users.apiClient.username:apiClient}")
    private String apiClientUsername;

    @Value("${etl-service.users.apiClient.password}")
    private String apiClientPassword;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable().authorizeRequests()

                // Permit anonymous access to /public/**
                .antMatchers("/public/**")
                .permitAll()

                // Protect everything else
                .antMatchers("/**")
                .authenticated().and().httpBasic();
        //		.antMatchers("/**").permitAll();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        if (LOG.isInfoEnabled()) {
            LOG.info(MessageFormat.format("*** USER MANAGEMENT *** admin usernmae=[{0}]", new Object[]{adminUsername}));
        }
        if (StringUtils.isBlank(adminPassword)) {
            adminPassword = generateDefaultPasswordFor(adminUsername);
        }

        if (LOG.isInfoEnabled()) {
            LOG.info(MessageFormat.format("*** USER MANAGEMENT *** apiClient usernmae=[{0}]", new Object[]{apiClientUsername}));
        }
        if (StringUtils.isBlank(apiClientPassword)) {
            apiClientPassword = generateDefaultPasswordFor(apiClientUsername);
        }

        auth.inMemoryAuthentication()
                .withUser(adminUsername).password(adminPassword).authorities("ROLE_ADMIN", "ROLE_ACTUATOR")
                .and()
                .withUser(apiClientUsername).password(apiClientPassword).authorities("ROLE_USER")
        ;

    }

    private String generateDefaultPasswordFor(final String username) {
        String secret = Base64Utils.encodeToUrlSafeString(UUID.randomUUID().toString().getBytes());
        if (LOG.isWarnEnabled()) {
            LOG.warn(MessageFormat.format(
                    "*** USER MANAGEMENT *** [generateDefaultPassword] -> Password is not set for user [{0}]! Generated default password=[{1}]",
                    new Object[]{username, secret}));
        }
        return secret;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }

    @PostConstruct
    public void postConstruct() {
        try {
            if (trustSelfSignedCerts) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Overriding JVM default TrustManager...");
                }
                SecurityUtils.trustSelfSignedCerts();
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Overriding JVM default TrustManager completed. "
                            + "All SSL certs, including self-signed ones, will be trusted. "
                            + "DO NOT do this in a production environment!");
                }
            }

        } catch (Exception e) {
            LOG.error("Error occurred in WebSecurityConfig.postConstruct()", e);
        }
    }

}
