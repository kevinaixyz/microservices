package com.prototype.microservice.commons.helper;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.prototype.microservice.commons.event.AuditLogEvent;

/**
 * AuditLogEventPublisher
 */
@Component
public class AuditLogHelper {

    @Value("${spring.application.name:UNKNOWN_APPLICATION}")
    private String appName;

    @Autowired
    private ApplicationEventPublisher eventPub;

    /**
     * Log a audit message using auditlog-service asynchronously
     *
     * @param callerObjReference
     * @param userId
     * @param preCondition
     * @param postCondition
     * @param logMessage
     */
    @Async
    public void log(
            Object callerObjReference,
            String userId,
            String preCondition,
            String postCondition,
            String logMessage) {
        AuditLogEvent event = new AuditLogEvent(
                callerObjReference,
                appName,
                userId,
                preCondition,
                postCondition,
                logMessage,
                new Timestamp(System.currentTimeMillis())
        );
        eventPub.publishEvent(event);
    }

}
