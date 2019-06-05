package com.prototype.microservice.edge;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * Base unit test case for Spring Boot services.
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class AbstractEdgeServerServiceTest extends EdgeServerBaseSpringBootTest {

}
