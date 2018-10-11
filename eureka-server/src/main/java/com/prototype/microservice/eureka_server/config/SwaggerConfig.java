package com.prototype.microservice.eureka_server.config;

import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger API tools configuration.
 *
 *
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Value("${info.component:Swagger API}")
	private String componentName;

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build()
				.apiInfo(apiInfo()).useDefaultResponseMessages(false);
	}

	private ApiInfo apiInfo() {
		ApiInfoBuilder apiInfoBuilder = new ApiInfoBuilder();
		apiInfoBuilder.title(MessageFormat.format("{0} APIs", new Object[] { componentName }));
		apiInfoBuilder.description(MessageFormat.format("{0} API documentation by Swagger2", new Object[] { componentName }));
		apiInfoBuilder.version("N/A");
		apiInfoBuilder.contact(new Contact("N/A", "http://http://www.htisec.com/", "no-reply@non-existing-domain.com"));
		apiInfoBuilder.license("www.htisec.com");
		apiInfoBuilder.licenseUrl("http://http://www.htisec.com/");
		return apiInfoBuilder.build();
	}
}
