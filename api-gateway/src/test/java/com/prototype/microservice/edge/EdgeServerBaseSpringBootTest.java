package com.prototype.microservice.edge;

import org.springframework.test.context.ContextConfiguration;

/**
 * Spring Boot unit test base class.
 */
@ContextConfiguration(classes = EdgeServerApp.class)
public abstract class EdgeServerBaseSpringBootTest extends GenericSpringBootTest {

}
