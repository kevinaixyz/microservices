package com.prototype.microservice.edge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.trace.InMemoryTraceRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.thymeleaf.cache.StandardCacheManager;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.prototype.microservice.edge.filter.CustomPreFilter;

/**
 * Generic application configuration
 *
 *
 *
 */
@Configuration
public class EdgeServerAppConfig {

	@Value("${api-gateway.trace.history:100}")
	private int traceHistory;

	/**
	 * Zuul routes pre-filter
	 *
	 * @return
	 */
	@Bean
	public CustomPreFilter customPreFilter() {
		return new CustomPreFilter();
	}

	/**
	 * Global definition of the password encoder
	 *
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		//		return new Base64BCryptPasswordEncoder();
		return new Pbkdf2PasswordEncoder();
	}

	/**
	 * Thymeleaf template engine
	 *
	 * @param templateResolver
	 * @return
	 */
	@Bean
	public SpringTemplateEngine templateEngine(TemplateResolver templateResolver) {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver);
		templateEngine.addDialect(new SpringSecurityDialect());
		StandardCacheManager cacheManager = new StandardCacheManager();
		cacheManager.setTemplateCacheMaxSize(100);
		templateEngine.setCacheManager(cacheManager);
		return templateEngine;
	}

	/**
	 * SpringBoot Actuator trace repo.
	 *
	 * @return
	 */
	@Bean
	public InMemoryTraceRepository inMemoryTraceRepository() {
		InMemoryTraceRepository traceRepo = new InMemoryTraceRepository();
		traceRepo.setCapacity(traceHistory);
		return traceRepo;
	}

}
