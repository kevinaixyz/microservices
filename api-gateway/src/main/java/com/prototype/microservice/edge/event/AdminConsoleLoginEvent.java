package com.prototype.microservice.edge.event;

import org.springframework.context.ApplicationEvent;

/**
 *
 *
 */
public class AdminConsoleLoginEvent extends ApplicationEvent {

    /**
     * SVID
     */
    private static final long serialVersionUID = 5538478491832800377L;

    public AdminConsoleLoginEvent(Object source) {
        super(source);
    }

}
