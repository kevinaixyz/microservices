package com.prototype.microservice.aeprotal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Base unit test class for RESTful endpoints.
 *
 * @author Danny Tse
 *
 */
public abstract class AbstractCrmPwmRestfulTest extends CrmPwmBaseSpringBootTest {

	@Autowired
	protected MockMvc mockMvc;

}
