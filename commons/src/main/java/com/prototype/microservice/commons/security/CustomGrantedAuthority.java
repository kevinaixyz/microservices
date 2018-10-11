package com.prototype.microservice.commons.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

public class CustomGrantedAuthority implements GrantedAuthority {

	private static final long serialVersionUID = -5063425418957126165L;

	private static final String PREFIX = "ROLE_";

	private final String role;

	public CustomGrantedAuthority(String role) {
		Assert.hasText(role, "A granted authority textual representation is required");
		if (!role.startsWith(PREFIX)) {
			this.role = PREFIX + role;
		} else {
			this.role = role;
		}
	}

	@Override
	public String getAuthority() {
		return role;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj instanceof CustomGrantedAuthority) {
			return role.equals(((CustomGrantedAuthority) obj).role);
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.role.hashCode();
	}

	@Override
	public String toString() {
		return this.role;
	}

}
