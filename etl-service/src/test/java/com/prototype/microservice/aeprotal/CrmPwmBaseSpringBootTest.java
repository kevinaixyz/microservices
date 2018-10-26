package com.prototype.microservice.aeprotal;

import org.springframework.test.context.ContextConfiguration;

import com.prototype.microservice.ficc.EtlServiceApp;

/**
 * CRMPWM Spring Boot unit test base class.
 *
 * @author Danny Tse
 *
 */
@ContextConfiguration(classes = EtlServiceApp.class)
public abstract class CrmPwmBaseSpringBootTest extends GenericSpringBootTest {

}
