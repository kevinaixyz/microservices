package com.prototype.microservice.commons.integration;

import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.prototype.microservice.commons.helper.APIGatewayHelper;
import com.prototype.microservice.commons.helper.CommonHelper;
import com.prototype.microservice.commons.helper.JsonHelper;
import com.prototype.microservice.commons.helper.SecurityHelper;
import com.prototype.microservice.commons.json.AuditLogPersistRequestJson;
import com.prototype.microservice.commons.json.SimpleResponseJson;

/**
 * auditlog-service integrator
 *
 * 
 *
 */
@Service
public class AuditLogServiceIntegrator {

    private final static Logger LOG = LoggerFactory.getLogger(AuditLogServiceIntegrator.class);

    @Autowired
    private APIGatewayHelper edgeHelper;

    @Autowired
    private CommonHelper comHelper;

    @Autowired
    private SecurityHelper secHelper;

    @Autowired
    private JsonHelper jsonHelper;

    @Value("${api-gateway.routes.auditlog-service.credentials.apiUsername:UNDEFINED}")
    private String apiUsername;

    @Value("${api-gateway.routes.auditlog-service.credentials.apiPassword:UNDEFINED}")
    private String apiPassword;

    @Value("${api-gateway.routes.auditlog-service.endpoints.writeLog:/auditlog-service/api/audit/rest/persist}")
    private String writeLogEndpoint;

    private RestTemplate restTemplate;

    @PostConstruct
    public void postConstruct() throws Exception {
        /*
         * Always use the APIGatewayService.buildRestTemplate method to build a
         * RestTemplate object. Do NOT use the native autowired RestTemplate.
         */
        restTemplate = edgeHelper.buildRestTemplate(apiUsername, apiPassword);
        if (LOG.isInfoEnabled()) {
            LOG.info(MessageFormat.format(
                    "RestTemplate constructed [{0}]",
                    new Object[]{ restTemplate }));
        }
    }

    @Async
    public CompletableFuture<Boolean> writeLog(final AuditLogPersistRequestJson request) throws Exception {
        try {
            // Preset mandatory fields if needed
            if (StringUtils.isBlank(request.getAppId())) {
                request.setAppId(comHelper.getAppInstanceName());
            }
            if (StringUtils.isBlank(request.getUserId())) {
                request.setUserId(secHelper.getCurrentAuditor());
            }
            if (LOG.isInfoEnabled()) {
                try {
                    LOG.info("Posting audit trail -> {}", jsonHelper.toJson(request));
                } catch (Exception e) {
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("Unable to write log message due to {}", e.getLocalizedMessage());
                    }
                }
            }
            restTemplate.postForObject(edgeHelper.buildEdgeUrl(writeLogEndpoint), request, SimpleResponseJson.class);
            return CompletableFuture.completedFuture(Boolean.TRUE);
        } catch (Exception e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn("Unable to write audit log due to: {}", e.getLocalizedMessage());
            }
            return CompletableFuture.completedFuture(Boolean.FALSE);
        }
    }

}
