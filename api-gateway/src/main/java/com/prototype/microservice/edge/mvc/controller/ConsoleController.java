package com.prototype.microservice.edge.mvc.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.prototype.microservice.edge.event.AdminConsoleUserActionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.prototype.microservice.edge.constant.RolesConstant;
import com.prototype.microservice.edge.mvc.helper.ModelAndViewHelper;
import com.prototype.microservice.edge.mvc.model.RouteVO;
import com.prototype.microservice.edge.mvc.view.ViewConstants;
import com.prototype.microservice.edge.service.AdminService;
import com.prototype.microservice.edge.service.SecurityService;

/**
 * Welcome Controller with Spring MVC.
 *
 *
 *
 */
@Controller
@RequestMapping("/console")
public class ConsoleController {

	private final static Logger LOG = LoggerFactory.getLogger(ConsoleController.class);

	@Autowired
	private AdminService adminService;

	@Autowired
	private SecurityService secService;

	@Autowired
	private ModelAndViewHelper mvHelper;

	@Autowired
	private ApplicationEventPublisher appEventPublisher;

	private final static String ADMIN_PAGE_URL = "/edge/console/private/admin";

	@RequestMapping("/login")
	public ModelAndView login(Map<String, Object> model, SecurityContextHolderAwareRequestWrapper request, HttpServletResponse response) throws Exception {

		if (request != null && request.isUserInRole(RolesConstant.ROLE_ADMIN)) {

			return redirectToAdminPage(response);

		} else {

			return mvHelper.buildDefaultMnV(ViewConstants.V_LOGIN, model);

		}

	}

	private ModelAndView redirectToAdminPage(HttpServletResponse response) throws IOException {
		// User is logged in as an admin -> go to admin page directly
		if (LOG.isInfoEnabled()) {
			LOG.info("User is logged in as an admin -> go to admin page directly!");
		}
		try {
			response.sendRedirect(ADMIN_PAGE_URL);
		} catch (IOException e) {
			LOG.error(MessageFormat.format("Cannot redirect to [{0}] due to error [{1}]", new Object[] { ADMIN_PAGE_URL, e.getLocalizedMessage() }));
			throw e;
		}
		return null;
	}

	@RequestMapping("/private/admin")
	public ModelAndView admin(Map<String, Object> model, SecurityContextHolderAwareRequestWrapper request, HttpServletResponse response) throws Exception {

		// Zuul routes
		List<RouteVO> routeVOs = new ArrayList<>();
		List<Route> zuulRoutes = adminService.getAllRoutes();
		if (zuulRoutes != null && !zuulRoutes.isEmpty()) {
			for (Route route : zuulRoutes) {
				if (LOG.isDebugEnabled()) {
					LOG.debug(MessageFormat.format(
							"Found Zuul route -> {0}",
							new Object[] { route.toString() }));
				}
				RouteVO rVO = new RouteVO();
				rVO.setId(route.getId());
				rVO.setFullPath(route.getFullPath());
				rVO.setPath(route.getPath());
				rVO.setLocation(route.getLocation());
				rVO.setPrefix(route.getPrefix());
				rVO.setRetryable(String.valueOf(route.getRetryable()));
				rVO.setIpWhitelist(StringUtils.replace(secService.getIPWhitelistStringByRouteID(route.getId()), SecurityService.WHITELIST_DELIMITER, "<br/>"));
				routeVOs.add(rVO);
			}
			model.put(ViewConstants.F_ZUUL_ROUTES, routeVOs);
		}

		appEventPublisher.publishEvent(new AdminConsoleUserActionEvent(
				MessageFormat.format(
						"User ({0}) visited /private/admin at ({1})",
						new Object[] { request.getUserPrincipal().getName(), new Date(System.currentTimeMillis()) })));

		return mvHelper.buildDefaultMnV(ViewConstants.V_ADMIN, model);


	}

	@RequestMapping("/error/403")
	public ModelAndView error403(Map<String, Object> model) throws Exception {
		return mvHelper.buildDefaultMnV(ViewConstants.V_ERROR_403, model);
	}

	@RequestMapping("/**")
	public ModelAndView welcome(Map<String, Object> model, SecurityContextHolderAwareRequestWrapper request, HttpServletResponse response) throws Exception {

		if (request != null && request.isUserInRole(RolesConstant.ROLE_ADMIN)) {

			return redirectToAdminPage(response);

		} else {

			return mvHelper.buildDefaultMnV(ViewConstants.V_WELCOME, model);

		}

	}

}
