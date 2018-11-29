package com.prototype.microservice.aeprotal;

import org.springframework.test.context.ContextConfiguration;

import com.prototype.microservice.etl.EtlServiceApp;

@ContextConfiguration(classes = EtlServiceApp.class)
public abstract class CrmPwmBaseSpringBootTest extends GenericSpringBootTest {

}
