package com.prototype.microservice.commons.integration;

import java.text.MessageFormat;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.prototype.microservice.commons.helper.APIGatewayHelper;
import com.prototype.microservice.commons.helper.ErrorHelper;
import com.prototype.microservice.commons.helper.JsonHelper;
import com.prototype.microservice.commons.json.ResponseJson;
import com.prototype.microservice.commons.json.UserAuthenticationRequestJson;
import com.prototype.microservice.commons.json.UserAuthenticationResponseJson;

/**
 * entitlement-service integrator
 */
@Service
public class EntitlementServiceIntegrator {

    private final static Logger LOG = LoggerFactory.getLogger(EntitlementServiceIntegrator.class);

    @Autowired
    private APIGatewayHelper edgeHelper;

    @Autowired
    private ErrorHelper errorHelper;

    @Value("${api-gateway.routes.entitlement-service.credentials.apiUsername:UNDEFINED}")
    private String apiUsername;

    @Value("${api-gateway.routes.entitlement-service.credentials.apiPassword:UNDEFINED}")
    private String apiPassword;

    @Value("${api-gateway.routes.entitlement-service.endpoints.authenticate:/entitlement-service/api/user/rest/authenticate}")
    private String authenticateEndpoint;

    @Autowired
    private JsonHelper jsonHelper;

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
                    new Object[]{restTemplate}));
        }
    }

    public UserAuthenticationResponseJson authenticate(final UserAuthenticationRequestJson request) throws Exception {
        try {
            Assert.notNull(request, "Authentication infomration missing");
            if (LOG.isInfoEnabled()) {
                // obfuscate passwords in log files
                String requestJson = jsonHelper.toJson(request);
                UserAuthenticationRequestJson obfuscatedRequest = jsonHelper.fromJson(requestJson, UserAuthenticationRequestJson.class);
                obfuscatedRequest.setPassword("***obfuscated***");
                LOG.info(MessageFormat.format("********** Authentication request {0} with payload {1} **********", new Object[]{edgeHelper.buildEdgeUrl(authenticateEndpoint), jsonHelper.toJson(obfuscatedRequest)}));
            }
            UserAuthenticationResponseJson resp = restTemplate.postForObject(edgeHelper.buildEdgeUrl(authenticateEndpoint), request, UserAuthenticationResponseJson.class);
            if (LOG.isInfoEnabled()) {
                LOG.info(MessageFormat.format("********** Authentication response {0} **********", new Object[]{jsonHelper.toJson(resp)}));
            }
            if (ResponseJson.JSON_RESP_CODE_OK.equals(resp.getRespCode())) {
                return resp;
            } else {
                throw new RuntimeException(MessageFormat.format(
                        "Something has gone wrong {0}",
                        new Object[]{resp.getRespMsg()}));
            }
        } catch (Exception e) {
            if (LOG.isInfoEnabled()) {
                LOG.info(MessageFormat.format("********** Authentication error {0} **********", new Object[]{e.getLocalizedMessage()}));
            }
            errorHelper.handleErrorResponse(e); // convert into CheckedException
            return null;
        }
    }

}
