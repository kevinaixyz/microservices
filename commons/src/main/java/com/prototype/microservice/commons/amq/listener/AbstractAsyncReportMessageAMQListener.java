package com.prototype.microservice.commons.amq.listener;

import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;

import com.prototype.microservice.commons.helper.JsonHelper;
import com.prototype.microservice.commons.message.AsyncReportMessage;

/**
 * Abstract JMS message listener for async report AMQ messages.
 */
public abstract class AbstractAsyncReportMessageAMQListener {

    private final static Logger LOG = LoggerFactory.getLogger(AbstractAsyncReportMessageAMQListener.class);

    @Autowired
    private JsonHelper jsonHelper;

    /**
     * Process the message received from the destination.
     *
     * @param message
     */
    @JmsListener(destination = "${report-server.jms.topic.asyncReportEvent:topic/unknown}")
    public void processMessage(String message) {

        try {
            if (LOG.isInfoEnabled()) {
                LOG.info(MessageFormat.format(
                        "AsyncReportMessageAMQListener -> Received message [{0}]",
                        new Object[]{StringUtils.trimToEmpty(message)}));
            }
            AsyncReportMessage asyncReportMsg = jsonHelper.fromJson(message, AsyncReportMessage.class);
            doProcessMessage(asyncReportMsg);
            if (LOG.isInfoEnabled()) {
                LOG.info(MessageFormat.format(
                        "AsyncReportMessageAMQListener -> Message translated into [{0}]",
                        new Object[]{asyncReportMsg.toString()}));
            }
        } catch (Exception e) {
            if (LOG.isWarnEnabled()) {
                LOG.warn(MessageFormat.format(
                        "AsyncReportMessageAMQListener -> Unable to convert received message into AsyncReportMessage due to: {0}",
                        new Object[]{e.getLocalizedMessage()}), e);
            }
        }

    }

    public abstract void doProcessMessage(AsyncReportMessage asyncReportMsg);

}
