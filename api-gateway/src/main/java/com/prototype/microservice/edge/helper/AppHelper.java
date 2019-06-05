package com.prototype.microservice.edge.helper;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Generic helper service for non-business logic.
 */
@Component
public class AppHelper {

    @Value("${eureka.instance.metadataMap.instanceId:UNKNOWN_INSTANCE}")
    private String instanceId;

    @Autowired
    private Environment env;

    private final static String X_FORWARDED_FOR = "X-Forwarded-For";

    /**
     * Returns the Instnace ID per defined in "eureka.instance.metadataMap.instanceId"
     *
     * @return
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * Obtain the IP address of the client.
     *
     * @param httpRequest
     * @return
     */
    public String getClientIP(final HttpServletRequest httpRequest) {
        if (httpRequest.getHeader(X_FORWARDED_FOR) == null) {
            return httpRequest.getRemoteAddr();
        } else {
            return httpRequest.getHeader(X_FORWARDED_FOR).split(",")[0];
        }
    }

    /**
     * Obatin an environment property value by a key.
     * An empty string will be returned if no value is found.
     *
     * @param key
     * @return
     */
    public String getEnvironmentPropertyByKey(final String key) {
        return StringUtils.trimToEmpty(env.getProperty(key));
    }

    /**
     * Obatin an environment property value by a key.
     * The default value (given as param) will be returned if no value is found.
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getEnvironmentPropertyByKey(final String key, final String defaultValue) {
        return StringUtils.trimToEmpty(env.getProperty(key, defaultValue));
    }

    /**
     * Obtain the Server Port of the client request.
     *
     * @param httpRequest
     * @return
     */
    public String getServerPort(final HttpServletRequest httpRequest) {
        if (httpRequest.getHeader(X_FORWARDED_FOR) == null) {
            return String.valueOf(httpRequest.getServerPort());
        } else {
            return String.valueOf(httpRequest.getServerPort());
        }
    }

    /**
     * Obtain the Port of the client.
     *
     * @param httpRequest
     * @return
     */
    public String getLocalPort(final HttpServletRequest httpRequest) {
        if (httpRequest.getHeader(X_FORWARDED_FOR) == null) {
            return String.valueOf(httpRequest.getLocalPort());
        } else {
            return String.valueOf(httpRequest.getLocalPort());
        }
    }
}
