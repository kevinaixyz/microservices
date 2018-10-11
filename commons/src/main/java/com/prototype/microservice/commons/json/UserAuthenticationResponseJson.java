package com.prototype.microservice.commons.json;

import java.util.Set;
import java.util.TreeSet;

public class UserAuthenticationResponseJson extends SimpleResponseJson {

	private static final long serialVersionUID = -9003142047247425585L;
	private String userName;
	private String userDisplayname;
	private Set<String> authorities;

	public UserAuthenticationResponseJson() {
		super();
	}

	public UserAuthenticationResponseJson(String instanceId) {
		super(instanceId);
	}

	public Set<String> getAuthorities() {
		if (authorities == null) {
			authorities = new TreeSet<>();
		}
		return authorities;
	}

	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserDisplayname() {
		return userDisplayname;
	}

	public void setUserDisplayname(String userDisplayname) {
		this.userDisplayname = userDisplayname;
	}

}
