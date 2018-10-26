package com.prototype.microservice.aeprotal;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Base unit test class for testing a Spring Boot application.
 *
 * @author Danny Tse
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
public abstract class GenericSpringBootTest {

	@Before
	public void setup() {
		// Initialise Mockito
		MockitoAnnotations.initMocks(this);
	}

}
