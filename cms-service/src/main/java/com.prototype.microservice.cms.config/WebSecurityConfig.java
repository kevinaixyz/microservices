package com.prototype.microservice.cms.config;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.prototype.microservice.cms.security.UsersStore;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UsersStore usersStore;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder("", 1, 256);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Disable CSRF
        http.csrf().disable();

        // Use Basic Auth
        http.httpBasic();

        // Authentication policies
        // Permit anonymous access to /public/**
        // Protect everything else
        http.authorizeRequests()
                .antMatchers("/", "/public/**").permitAll()
                .antMatchers("/**").authenticated();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getAuthProvider());
    }

    public DaoAuthenticationProvider getAuthProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(new InMemoryUserDetailsManager(new HashSet<>(usersStore.getUsers().values())));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;

    }

}
