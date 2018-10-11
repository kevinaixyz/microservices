package com.prototype.microservice.edge.security;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

/**
 * In-memory storage of predefined, static set of users.<p/>
 * Users are defined in <b>application.yml</b>:<p/>
 * api-gateway.users.admin.username<br/>
 * api-gateway.users.admin.password
 *
 *
 *
 */
@Service
public class UsersStore {

	@Value("${api-gateway.users.admin.username:admin}")
	private String adminUsername;

	@Value("${api-gateway.users.admin.password}")
	private String adminPasswordHash; // BCrypt-Base64-URL safe password hash

	private final GrantedAuthority roleAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");
	private final GrantedAuthority roleActuator = new SimpleGrantedAuthority("ROLE_ACTUATOR");
	private final Set<GrantedAuthority> authAdmin;

	public UsersStore() {
		authAdmin = new HashSet<>();
		authAdmin.add(roleAdmin);
		authAdmin.add(roleActuator);
	}

	private User buildUser(final String username, final String passwordHash, final Set<GrantedAuthority> roles) {
		User user = new User(username, passwordHash, roles);
		return user;
	}

	public Map<String, User> getUsers() {
		Map<String, User> users = new HashMap<>();
		users.put(adminUsername, buildUser(adminUsername, adminPasswordHash, authAdmin));
		return users;
	}

}
