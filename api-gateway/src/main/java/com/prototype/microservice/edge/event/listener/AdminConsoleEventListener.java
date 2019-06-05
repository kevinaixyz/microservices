package com.prototype.microservice.edge.event.listener;

import java.text.MessageFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.prototype.microservice.edge.event.AdminConsoleLoginEvent;
import com.prototype.microservice.edge.event.AdminConsoleUserActionEvent;

/**
 * Event listener for admin console activities.
 */
@Component
public class AdminConsoleEventListener {

    private final static Logger LOG = LoggerFactory.getLogger(AdminConsoleEventListener.class);

    public AdminConsoleEventListener() {
    }

    @EventListener
    public void adminConsoleLoginEventListener(final AdminConsoleLoginEvent event) {
        if (LOG.isInfoEnabled()) {
            LOG.info(MessageFormat.format(
                    "adminConsoleLoginEventListener -> Event[{0}] Source[{1}] Timestamp[{2}]",
                    new Object[]{
                            event.getClass().getSimpleName(),
                            event.getSource().toString(),
                            new Date(event.getTimestamp())
                    }));
        }
    }

    @EventListener
    public void adminConsoleUserActionEventListener(final AdminConsoleUserActionEvent event) {
        if (LOG.isInfoEnabled()) {
            LOG.info(MessageFormat.format(
                    "adminConsoleUserActionEventListener -> Event[{0}] Source[{1}] Timestamp[{2}]",
                    new Object[]{
                            event.getClass().getSimpleName(),
                            event.getSource().toString(),
                            new Date(event.getTimestamp())
                    }));
        }
    }

}
