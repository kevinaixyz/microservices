package com.prototype.microservice.commons.helper;

import java.text.MessageFormat;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.prototype.microservice.commons.utils.SecurityUtils;

/**
 * API Gateway Helper.
 *
 *
 *
 */
@Component
public class APIGatewayHelper {

	private final static Logger LOG = LoggerFactory.getLogger(APIGatewayHelper.class);

	@Autowired
	private RestTemplateBuilder builder;

	@Value("${api-gateway.root-url:localhost}")
	private String rootUrl;

	@Value("${api-gateway.rest.timeout.connect:2000}")
	private int connectTimeout;

	@Value("${api-gateway.rest.timeout.read:5000}")
	private int readTimeout;

	/**
	 * Build a RestTemplate with HTTP Basic Auth.
	 *
	 * @param servicePath
	 * @param apiUsername
	 * @param apiPassword
	 * @return
	 */
	public RestTemplate buildRestTemplate(final String apiUsername, final String apiPassword) {
		HttpClient httpClient = null;
		try {
			// Workaround to accept self-signed certs for RestTemplate
			httpClient = SecurityUtils.unquestioningHttpClient();
		} catch (Exception e) {
			if (LOG.isWarnEnabled()) {
				LOG.warn(MessageFormat.format(
						"Cannot construct the unquestioningHttpClient, default HttpClient will be used: {0}",
						new Object[] { e.getLocalizedMessage() }) , e);
			}
		}
		if (LOG.isInfoEnabled()) {
			LOG.info("HTTP Client for the RestTempalte: {}", httpClient);
		}
		builder.setConnectTimeout(connectTimeout);
		builder.setReadTimeout(readTimeout);
		RestTemplate restTemplate = builder.build();
		if (httpClient != null) {
			((HttpComponentsClientHttpRequestFactory) restTemplate.getRequestFactory()).setHttpClient(httpClient);
		}
		restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(apiUsername, apiPassword));
		return restTemplate;
	}

	/**
	 * Construct the full API Gateway URL for a specific service path.
	 * e.g.: https://api-gateway/edge/api/service-provider
	 *
	 * @param servicePath
	 * @return
	 */
	public String buildEdgeUrl(final String servicePath) {
		return rootUrl + servicePath;
	}

}
