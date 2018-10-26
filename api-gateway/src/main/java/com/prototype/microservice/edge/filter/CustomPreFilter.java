package com.prototype.microservice.edge.filter;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import com.prototype.microservice.edge.constant.EdgeServerAppConstant;
import com.prototype.microservice.edge.exception.IPWhitelistNotDefinedException;
import com.prototype.microservice.edge.exception.WhitelistNotDefinedException;
import com.prototype.microservice.edge.helper.AppHelper;
import com.prototype.microservice.edge.service.SecurityService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.google.gson.Gson;
import com.prototype.microservice.commons.utils.JsonUtils;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class CustomPreFilter extends ZuulFilter {

	private final static Logger LOG = LoggerFactory.getLogger(CustomPreFilter.class);

	@Autowired
	private AppHelper appHelper;

	@Autowired
	private SecurityService secSvc;

	private Gson gson = new Gson();

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		String sessionID = "N/A";
		RequestContext ctx = RequestContext.getCurrentContext();
		try {
			HttpServletRequest req = ctx.getRequest();
			try {
				sessionID = req.getSession().getId();
			} catch (Exception e) {
				if (LOG.isWarnEnabled()) {
					LOG.warn("Unable to get Session ID!", e);
				}
			}
			if (LOG.isInfoEnabled()) {
				LOG.info(MessageFormat.format(
						"Zuul (Session ID # {0}) > CustomPreFilter > Method=[{1}] URI=[{2}] Route=[{3}] Principal=[{4}] FromHost=[{5}] ClientIP=[{6}]",
						new Object[] {
								sessionID,
								req.getMethod(),
								req.getRequestURI(),
								getRouteByRequestURI(req.getRequestURI()),
								req.getUserPrincipal(),
								req.getRemoteHost(),
								appHelper.getClientIP(req)
						}));
			}
			// IP whitelist filtering
			if (secSvc.isWhiteListedIP(getRouteByRequestURI(req.getRequestURI()), appHelper.getClientIP(req))) {
				if (LOG.isInfoEnabled()) {
					LOG.info(MessageFormat.format(
							"Zuul (Session ID # {0}) > CustomPreFilter > Whitelist allowed > URI=[{1}] ClientIP=[{2}]",
							new Object[] {
									sessionID,
									req.getRequestURI(),
									appHelper.getClientIP(req)
							}));
				}
			} else {
				if (LOG.isInfoEnabled()) {
					LOG.warn(MessageFormat.format(
							"Zuul (Session ID # {0}) > CustomPreFilter > Whitelist rejected > URI=[{1}] ClientIP=[{2}]",
							new Object[] {
									sessionID,
									req.getRequestURI(),
									appHelper.getClientIP(req)
							}));
				}
				throw new IPWhitelistNotDefinedException(MessageFormat.format(
						"The IP is blocked from accessing the requested URI [{0}] (ClientIP[{1}] SessionID[{2}])",
						new Object[] {
								req.getRequestURI(),
								appHelper.getClientIP(req),
								sessionID
						}));
			}
			
			whitelistFiltering(sessionID, req, appHelper.getServerPort(req), "ServerPort", SecurityService.ZUUL_ROUTE_SERVER_PORT_CFG_KEY);
			whitelistFiltering(sessionID, req, appHelper.getLocalPort(req), "LocalPort", SecurityService.ZUUL_ROUTE_LOCAL_PORT_CFG_KEY);
			
		} catch (IPWhitelistNotDefinedException e) {
			// IP whtielisting failed - blocked access!
			if (LOG.isWarnEnabled()) {
				LOG.warn(e.getLocalizedMessage());
			}
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
			ctx.setResponseBody(gson.toJson(JsonUtils.getErrorResponseJson(e, appHelper.getInstanceId())));
		} catch (WhitelistNotDefinedException e) {
			// whtielisting failed - blocked access!
			if (LOG.isWarnEnabled()) {
				LOG.warn(e.getLocalizedMessage());
			}
			ctx.setSendZuulResponse(false);
			ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
			ctx.setResponseBody(gson.toJson(JsonUtils.getErrorResponseJson(e, appHelper.getInstanceId())));
		}  catch (Exception e) {
			// Just log the error gracefully without failing the call
			LOG.warn(MessageFormat.format(
					"Zuul (Session ID # {0}) > Problem occurred during pre-process {1} due to: {2}",
					new Object[] { sessionID, this.getClass().getName(), e.getLocalizedMessage() }),
					e);
		}
		return null;
	}

	private String getRouteByRequestURI(String requestURI) {
		String[] parts = StringUtils.split(requestURI, "/");
		/*
		 * The Route ID should be the 3rd element of the URI separated by slash.
		 * i.e.: /edge/api/<Route ID>/...
		 */
		if (parts != null && parts.length >= 3) {
			return parts[2];
		} else {
			return "";
		}
	}

	@Override
	public String filterType() {
		return EdgeServerAppConstant.ZUUL_FILTER_TYPE_PRE;
	}

	@Override
	public int filterOrder() {
		return 1;
	}
	
	private void whitelistFiltering(final String sessionID, final HttpServletRequest req, final String value, final String valueName, final String cfgKey) throws WhitelistNotDefinedException {
		//whitelist filtering
		if (secSvc.isWhiteListedValue(getRouteByRequestURI(req.getRequestURI()), value, valueName, cfgKey)) {
			if (LOG.isInfoEnabled()) {
				LOG.info(MessageFormat.format(
						"Zuul (Session ID # {0}) > CustomPreFilter > Whitelist allowed > URI=[{1}] {3}=[{2}]",
						new Object[] {
								sessionID,
								req.getRequestURI(),
								value,
								valueName
						}));
			}
		} else {
			if (LOG.isInfoEnabled()) {
				LOG.warn(MessageFormat.format(
						"Zuul (Session ID # {0}) > CustomPreFilter > Whitelist rejected > URI=[{1}] {3}=[{2}]",
						new Object[] {
								sessionID,
								req.getRequestURI(),
								value,
								valueName
						}));
			}
			throw new WhitelistNotDefinedException(MessageFormat.format(
					"The {3} is blocked from accessing the [{0}] ({3}[{1}] SessionID[{2}])",
					new Object[] {
							req.getRequestURI(),
							value,
							sessionID,
							valueName
					}));
		}
	}

}
