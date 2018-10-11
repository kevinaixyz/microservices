package com.prototype.microservice.edge.event;

import org.springframework.context.ApplicationEvent;


/**
 * Admin console action event
 *
 *
 *
 */
public class AdminConsoleUserActionEvent extends ApplicationEvent {

	/**
	 * SVID
	 */
	private static final long serialVersionUID = -1109235451497493406L;

	public AdminConsoleUserActionEvent(Object source) {
		super(source);
	}

}
