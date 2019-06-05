package com.prototype.microservice.edge.config;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.prototype.microservice.edge.security.CustomAccessDeniedHandler;
import com.prototype.microservice.edge.security.CustomAuthenticationSuccessHandler;
import com.prototype.microservice.edge.security.CustomUserDetailsAuthenticationProvider;
import com.prototype.microservice.edge.security.CustomUserDetailsService;

/**
 * Web security configurations
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final static Logger LOG = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Autowired
    private CustomAccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CustomUserDetailsService userDetailsSvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Disable HTTP Basic Auth
        http.httpBasic().disable();

        // Disable CSRF
        http.csrf().disable();

        // Enable X-frame from same origin
        http.headers().frameOptions().sameOrigin();

        // Enable authorisation
        http.authorizeRequests()

                // Unprotected resources
                .antMatchers("/", "/api/**", "/public/**", "/console/", "console/error/**")
                .permitAll()

                // Protected resources
                .antMatchers("/console/private/admin/**").hasAnyRole("ADMIN")
                .antMatchers("/console/private/user/**").hasAnyRole("USER")
                .anyRequest().authenticated()
                .antMatchers("/**").authenticated()

                // Web console login / logout page
                .and()
                .formLogin().loginPage("/console/login").defaultSuccessUrl("/console/private/admin")
                .permitAll()
                .and()
                .logout().permitAll()

                // Access denied handler
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        // Login / logout success handler
        http.formLogin().successHandler(customAuthenticationSuccessHandler);
        http.logout().logoutSuccessUrl("/console/login?logout");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        /*
         * Using a bespoke user details service to demonstrate how
         * it can be customised
         */
        //		auth.userDetailsService(userDetailsSvc);
        auth.authenticationProvider(getAuthProvider());

        /*
         * Using in-memory hardcoded set of users for demonstration purpose.
         * It can be replaced by JDBC user profile datasource.
         */
        //		auth.inMemoryAuthentication()
        //		.withUser("admin").password("password").authorities("ROLE_ADMIN", "ROLE_ACTUATOR")
        //		.and()
        //		.withUser("user").password("password").authorities("ROLE_USER")
        //		.and()
        //		.withUser("superuser").password("password").authorities("ROLE_ADMIN", "ROLE_ACTUATOR", "ROLE_USER")
        //		;

    }

    public AuthenticationProvider getAuthProvider() {
        //		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        CustomUserDetailsAuthenticationProvider authProvider = new CustomUserDetailsAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsSvc);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @PostConstruct
    public void postConstruct() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                    return myTrustedAnchors;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Problem occurred during TrustManager customization.", e);
            }
        }
    }

}
