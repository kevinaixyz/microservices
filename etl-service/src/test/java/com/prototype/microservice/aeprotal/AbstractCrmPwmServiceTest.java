package com.prototype.microservice.aeprotal;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

/**
 * Base unit test case for Spring Boot services.
 *
 * @author Danny Tse
 *
 */
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public abstract class AbstractCrmPwmServiceTest extends CrmPwmBaseSpringBootTest {

}
