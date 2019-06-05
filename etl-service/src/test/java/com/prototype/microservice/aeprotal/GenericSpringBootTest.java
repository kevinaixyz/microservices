package com.prototype.microservice.aeprotal;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class GenericSpringBootTest {

    @Before
    public void setup() {
        // Initialise Mockito
        MockitoAnnotations.initMocks(this);
    }

}
