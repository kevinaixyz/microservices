package com.prototype.microservice.edge.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.stereotype.Service;

/**
 * Administrative service.
 *
 *
 *
 */
@Service
public class AdminService {

	private final static Logger LOG = LoggerFactory.getLogger(AdminService.class);

	@Autowired
	private SimpleRouteLocator routeLocator;

	public AdminService() {	}

	public List<Route> getAllRoutes() {
		return routeLocator.getRoutes();
	}

}
