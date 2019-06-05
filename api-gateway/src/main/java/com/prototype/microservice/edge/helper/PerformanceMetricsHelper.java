package com.prototype.microservice.edge.helper;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Component;

/**
 * Helper for Spring Boot performance metrics recording.
 */
@Component
public class PerformanceMetricsHelper {

    private final static Logger LOG = LoggerFactory.getLogger(PerformanceMetricsHelper.class);

    @Autowired
    private CounterService counterSvc;

    public PerformanceMetricsHelper() {
    }

    /**
     * Increase the performance metrics counter value by 1.
     *
     * @param counterName
     */
    public void counterIncrement(final String metricName) {

        try {
            if (StringUtils.isNotBlank(metricName)) {

                counterSvc.increment(metricName);

            } else {

                if (LOG.isWarnEnabled()) {
                    LOG.warn("Cannot do counterIncrement due to missing metricName!");
                }

            }
        } catch (Exception e) {

            // Fail gracefully :)
            if (LOG.isWarnEnabled()) {
                LOG.warn(MessageFormat.format(
                        "Can't do counterIncrement for [{0}] due to [{1}]",
                        new Object[]{StringUtils.trimToEmpty(metricName), e.getLocalizedMessage()}));
            }

        }

    }

}
