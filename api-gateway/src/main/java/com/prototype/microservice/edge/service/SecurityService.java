package com.prototype.microservice.edge.service;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.prototype.microservice.edge.helper.AppHelper;

/**
 * Security Service
 *
 *
 *
 */
@Service
public class SecurityService {

	private final static Logger LOG = LoggerFactory.getLogger(SecurityService.class);

	@Autowired
	private AppHelper appHelper;

	private final static String ZUUL_ROUTE_CFG_KEY = "zuul.routes.{0}.ipWhitelist";
	public final static String ZUUL_ROUTE_SERVER_PORT_CFG_KEY = "zuul.routes.{0}.serverPortWhitelist";
	public final static String ZUUL_ROUTE_LOCAL_PORT_CFG_KEY = "zuul.routes.{0}.localPortWhitelist";

	public final static String WHITELIST_DELIMITER = ",";

	/**
	 * Check if the IP address is defined as white-list address (i.e.: allowed for execution)
	 * per a specific Zuul route.
	 *
	 * The IP address will be treated as white-listed if the white-list itself is not defined
	 * for the route.
	 *
	 * Route ID equals to zuul.routes.<Route ID> within the configuration.
	 *
	 * The whtielist must be defined in zuul.routes.<Route ID>.ipWhitelist
	 *
	 * The method will return true if no whitelist (or an empty whitelist) is defined.
	 *
	 * @param routeID
	 * @param ipAddress
	 * @return
	 */
	public boolean isWhiteListedIP(final String routeID, final String ipAddress) {
		String whitelistString = getIPWhitelistStringByRouteID(routeID);
		if (StringUtils.isEmpty(whitelistString)) {
			// Whitelist is not defined, allow execution.
			return whitelistNotDefined(routeID, ipAddress);
		} else {
			String[] whitelistArray = StringUtils.split(whitelistString, WHITELIST_DELIMITER);
			if (whitelistArray == null || whitelistArray.length == 0) {
				// Should not happen but returns true just in case...
				return whitelistNotDefined(routeID, ipAddress);
			} else {
				for (String trustedIP : whitelistArray) {
					if (StringUtils.equalsIgnoreCase(trustedIP, ipAddress)) {
						// White-listed
						if (LOG.isInfoEnabled()) {
							LOG.info(MessageFormat.format(
									"isWhiteListedIP -> Requested IP [{0}] was found in whitelist for route [{1}] -> ALLOWED ACCESS",
									new Object[] { ipAddress, routeID }));
						}
						return true;
					}
				}
				// Not white-listed
				LOG.info(MessageFormat.format(
						"isWhiteListedIP -> Requested IP [{0}] was not found in whitelist for route [{1}] -> REJECTED ACCESS",
						new Object[] { ipAddress, routeID }));
				return false;
			}
		}
	}

	/**
	 * Get the IP whitelist string given a Route ID.
	 * An empty string will be returned if the whitelist is not defined.
	 *
	 * @param routeID
	 * @return
	 */
	public String getIPWhitelistStringByRouteID(final String routeID) {
		return appHelper.getEnvironmentPropertyByKey(
				MessageFormat.format(ZUUL_ROUTE_CFG_KEY, new Object[] { routeID }),
				StringUtils.EMPTY);
	}

	private boolean whitelistNotDefined(final String routeID, final String ipAddress) {
		if (LOG.isInfoEnabled()) {
			LOG.info(MessageFormat.format(
					"isWhiteListedIP -> IP whitelist is not defined for route [{0}] @ requested IP [{1}] -> ALLOWED ACCESS",
					new Object[] { routeID, ipAddress }));
		}
		return true;
	}
	
	
	/**
	 * Get the whitelist string given a Route ID.
	 * An empty string will be returned if the whitelist is not defined.
	 *
	 * @param routeID
	 * @return
	 */
	private boolean whitelistNotDefined(final String routeID, final String value, final String valueName) {
		if (LOG.isInfoEnabled()) {
			LOG.info(MessageFormat.format(
					"isWhiteListed{2} -> {2} whitelist is not defined for route [{0}] @ requested {2} [{1}] -> ALLOWED ACCESS",
					new Object[] { routeID, value, valueName }));
		}
		return true;
	}
	
	/**
	 * Get the whitelist string given a Route ID.
	 * An empty string will be returned if the whitelist is not defined.
	 *
	 * @param routeID
	 * @return
	 */
	public String getWhitelistStringByRouteID(final String routeID, final String cfgKey) {
		return appHelper.getEnvironmentPropertyByKey(
				MessageFormat.format(cfgKey, new Object[] { routeID }),
				StringUtils.EMPTY);
	}
	
	/**
	 * Check if the Ports is defined as white-list address (i.e.: allowed for execution)
	 * per a specific Zuul route.
	 *
	 * The Ports will be treated as white-listed if the white-list itself is not defined
	 * for the route.
	 *
	 * Route ID equals to zuul.routes.<Route ID> within the configuration.
	 *
	 * The whtielist must be defined in zuul.routes.<Route ID>.ipWhitelist
	 *
	 * The method will return true if no whitelist (or an empty whitelist) is defined.
	 *
	 * @param routeID
	 * @param value
	 * @return
	 */
	public boolean isWhiteListedValue(final String routeID, final String value, final String valueName, String cfgKey) {
		String whitelistString = getWhitelistStringByRouteID(routeID, cfgKey);
		if (StringUtils.isEmpty(whitelistString)) {
			// Whitelist is not defined, allow execution.
			return whitelistNotDefined(routeID, value, valueName);
		} else {
			String[] whitelistArray = StringUtils.split(whitelistString, WHITELIST_DELIMITER);
			if (whitelistArray == null || whitelistArray.length == 0) {
				// Should not happen but returns true just in case...
				return whitelistNotDefined(routeID, value, valueName);
			} else {
				for (String trustedPort : whitelistArray) {
					if (StringUtils.equalsIgnoreCase(trustedPort, value)) {
						// White-listed
						if (LOG.isInfoEnabled()) {
							LOG.info(MessageFormat.format(
									"isWhiteListed{2} -> Requested {2} [{0}] was found in whitelist for route [{1}] -> ALLOWED ACCESS",
									new Object[] { value, routeID, valueName }));
						}
						return true;
					}
				}
				// Not white-listed
				LOG.info(MessageFormat.format(
						"isWhiteListed{2} -> Requested {2} [{0}] was not found in whitelist for route [{1}] -> REJECTED ACCESS",
						new Object[] { value, routeID, valueName }));
				return false;
			}
		}
	}

}
