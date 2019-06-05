package com.prototype.microservice.eureka_server.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Generic helper service for non-business logic.
 */
@Component
public class AppHelper {

    @Value("${eureka.instance.metadataMap.instanceId:UNKNOWN_INSTANCE}")
    private String instanceId;

    /**
     * Returns the Instnace ID per defined in "eureka.instance.metadataMap.instanceId"
     *
     * @return
     */
    public String getInstanceId() {
        return instanceId;
    }

}
