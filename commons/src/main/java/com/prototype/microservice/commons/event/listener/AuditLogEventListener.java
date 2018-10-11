package com.prototype.microservice.commons.event.listener;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.prototype.microservice.commons.event.AuditLogEvent;
import com.prototype.microservice.commons.helper.CommonHelper;
import com.prototype.microservice.commons.integration.AuditLogServiceIntegrator;
import com.prototype.microservice.commons.json.AuditLogPersistRequestJson;

/**
 * Event listener for audit logs.
 *
 *
 *
 */
@Component
public class AuditLogEventListener {

	private final static Logger LOG = LoggerFactory.getLogger(AuditLogEventListener.class);

	public AuditLogEventListener() { }

	@Autowired
	private CommonHelper comHelper;

	@Autowired
	private AuditLogServiceIntegrator auditLogSvc;

	@EventListener
	public void auditLogEventListener(final AuditLogEvent event) {

		if (LOG.isInfoEnabled()) {
			LOG.info(MessageFormat.format(
					"AuditLogEventListener -> Receceived event Class[{0}] Source[{1}] Details[{2}]",
					event.getClass().getSimpleName(),
					event.getSource().toString(),
					event.toString()));
		}

		try {

			// Persist the log using auditlog-service
			AuditLogPersistRequestJson req = new AuditLogPersistRequestJson();
			req.setAppId(event.getAppId());
			req.setUserId(event.getUserId());
			req.setCorrelationID(comHelper.secureRandomUUID());
			req.setLogMessage(event.getLogMessage());
			req.setLogTimestamp(comHelper.formatTimestampToString(event.getLogTimestamp().getTime()));
			req.setPreCondition(event.getPreCondition());
			req.setPostCondition(event.getPostCondition());
			auditLogSvc.writeLog(req);

		} catch (Exception e) {

			LOG.error(MessageFormat.format(
					"AuditLogEventListener -> Unable to write audit log due to: {0}",
					e.getLocalizedMessage()), e);

		}

	}


}
