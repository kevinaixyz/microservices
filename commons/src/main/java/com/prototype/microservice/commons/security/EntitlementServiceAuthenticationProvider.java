package com.prototype.microservice.commons.security;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;

import com.prototype.microservice.commons.constants.CommonConstants;
import com.prototype.microservice.commons.error.CheckedException;
import com.prototype.microservice.commons.helper.CommonHelper;
import com.prototype.microservice.commons.helper.JsonHelper;
import com.prototype.microservice.commons.helper.MessageHelper;
import com.prototype.microservice.commons.integration.EntitlementServiceIntegrator;
import com.prototype.microservice.commons.json.ErrorResponseJson;
import com.prototype.microservice.commons.json.UserAuthenticationRequestJson;
import com.prototype.microservice.commons.json.UserAuthenticationResponseJson;

/**
 * Authenticate a user by using the entitlement-service
 *
 *
 *
 */
@Component
public class EntitlementServiceAuthenticationProvider implements AuthenticationProvider {

	private final static Logger LOG = LoggerFactory.getLogger(EntitlementServiceAuthenticationProvider.class);

	@Autowired
	private EntitlementServiceIntegrator entitlementSvc;

	@Autowired
	private CommonHelper comHelper;

	@Autowired
	private JsonHelper jsonHelper;

	@Autowired
	private MessageHelper msgHelper;

	@Value("${entitlements.requiredRoles:}")
	private String[] requiredRoles;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (authentication == null) {
			throw new AuthenticationServiceException("Authentication information is not provided");
		}
		UserAuthenticationRequestJson authReq = new UserAuthenticationRequestJson();
		authReq.setCorrelationID(comHelper.secureRandomUUID());
		authReq.setUserName(String.valueOf(authentication.getPrincipal()));
		authReq.setPassword(String.valueOf(authentication.getCredentials()));
		try {
			UserAuthenticationResponseJson authResp = entitlementSvc.authenticate(authReq);
			verifyRequiredRoles(authResp);
			Set<GrantedAuthority> roles = new HashSet<>();
			for (String authority : authResp.getAuthorities()) {
				roles.add(new CustomGrantedAuthority(authority));
			}
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					authentication.getPrincipal(), authentication.getCredentials(), roles);
			if (StringUtils.isBlank(authResp.getUserDisplayname())) {
				authToken.setDetails(authentication.getPrincipal());
			} else {
				authToken.setDetails(authResp.getUserDisplayname());
			}
			return authToken;
		} catch (Exception e) {
			StringBuffer errMsg = new StringBuffer(50);
			if (e instanceof HttpServerErrorException) {
				((HttpServerErrorException) e).getResponseBodyAsString();
				String errorJsonString = ((HttpServerErrorException) e).getResponseBodyAsString();
				try {
					ErrorResponseJson errorJson = jsonHelper.fromJson(errorJsonString, ErrorResponseJson.class);
					errMsg.append(MessageFormat.format("Cannot authenticate user due to: {0}",
							new Object[] { errorJson.getRespMsg() }));
				} catch (Exception e1) {
					if (LOG.isWarnEnabled()) {
						LOG.warn(MessageFormat.format("Cannot extract error message from response JSON due to: {0}",
								new Object[] { e.getLocalizedMessage() }), e);
					}
					errMsg.append(MessageFormat.format("Cannot authenticate user due to: {0}",
							new Object[] { e.getLocalizedMessage() }));
				}
				throw new AuthenticationServiceException(errMsg.toString(), e);
			} else if (e instanceof CheckedException) {
				errMsg.append(msgHelper.getMessage(e.getMessage(), ((CheckedException) e).getArgs(), null));
				throw new AuthenticationServiceException(errMsg.toString(), e);
			} else {
				errMsg.append(MessageFormat.format("Cannot authenticate user due to: {0}",
						new Object[] { e.getLocalizedMessage() }));
				throw new AuthenticationServiceException(errMsg.toString(), e);
			}
		}
	}

	private String authoritiesToString(UserAuthenticationResponseJson authResp) {
		try {
			return comHelper.arrayToString(authResp.getAuthorities().toArray(new String[authResp.getAuthorities().size()]));
		} catch (Exception e) {
			if (LOG.isWarnEnabled()) {
				LOG.warn("Unable to do authoritiesToString due to -> {}", e.getLocalizedMessage());
			}
			return "";
		}
	}

	/**
	 * Check if the user is entitled to at least one of the roles per defined in
	 * entitlement.web-roles
	 *
	 * @param authResp
	 */
	private void verifyRequiredRoles(UserAuthenticationResponseJson authResp) throws AuthenticationServiceException {
		if (requiredRoles == null || requiredRoles.length == 0) {
			if (LOG.isInfoEnabled()) {
				LOG.info("verifyRequiredRoles -> requiredRoles is not defined");
			}
			return;
		}
		if (LOG.isInfoEnabled()) {
			LOG.info(MessageFormat.format(
					"verifyRequiredRoles -> requiredRoles: {0}",
					new Object[] { comHelper.arrayToString(requiredRoles) }));
		}
		boolean sufficientRoles = false;
		if (authResp != null &&
				authResp.getAuthorities() != null &&
				!authResp.getAuthorities().isEmpty() &&
				requiredRoles != null &&
				requiredRoles.length > 0) {
			if (LOG.isInfoEnabled()) {
				LOG.info(MessageFormat.format(
						"verifyRequiredRoles -> granted authorities: {0}",
						new Object[] { authoritiesToString(authResp) }));
			}
			for (String requiredRole : requiredRoles) {
				requiredRole = normaliseRoleWithPrefix(requiredRole);
				for (String auth : authResp.getAuthorities()) {
					if (normaliseRoleWithPrefix(auth).equalsIgnoreCase(requiredRole)) {
						sufficientRoles = true;
						break;
					}
				}
				if (sufficientRoles) break;
			}
		}
		if (!sufficientRoles) {
			throw new AuthenticationServiceException("Insufficient entitlement to the system");
		}
	}

	private String normaliseRoleWithPrefix(String role) {
		if (!role.startsWith(CommonConstants.SPRING_SECURITY_ROLE_PREFIX)) {
			return CommonConstants.SPRING_SECURITY_ROLE_PREFIX + role;
		} else {
			return role;
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}



}
