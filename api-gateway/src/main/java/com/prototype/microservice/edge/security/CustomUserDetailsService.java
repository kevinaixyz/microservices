/**
 *
 */
package com.prototype.microservice.edge.security;

import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Demonstrate how we could customise the user data sources.
 *
 * e.g.: You can externalise the user data into an external source,
 * such as another microservice.
 *
 *
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	private final static Logger LOG = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private UsersStore usersStore;

	public CustomUserDetailsService() {	}


	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		/*
		 * Note:
		 * You can obtain the users from an external data source
		 * This example uses a hard-coded set of users just for quick
		 * demonstration reason
		 */
		UserDetails user = usersStore.getUsers().get(username);
		if (user != null) {
			if (LOG.isInfoEnabled()) {
				LOG.info(MessageFormat.format(
						"Found user [{0}] with hash [{1}]",
						new Object[] { username, user.getPassword() }));
			}
			return user;
		} else {
			if (LOG.isWarnEnabled()) {
				LOG.warn(MessageFormat.format(
						"User [{0}] cannot be found",
						new Object[] { username }));
			}
			throw new UsernameNotFoundException(MessageFormat.format(
					"User [{0}] cannot be found",
					new Object[] { username }));
		}
	}

}
