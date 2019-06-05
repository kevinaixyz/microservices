package com.prototype.microservice.edge.helper;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.prototype.microservice.edge.AbstractEdgeServerServiceTest;

public class AppHelperTest extends AbstractEdgeServerServiceTest {

    @Autowired
    private AppHelper helper;

    @Test
    public void testGetEnvKey() {

        String value;
        String defaultValue = RandomStringUtils.random(10);

        value = helper.getEnvironmentPropertyByKey("zuul.routes.mock-service.ip-whitelist");
        Assert.assertEquals("0:0:0:0:0:0:0:1,10.168.51.64", value);

        value = helper.getEnvironmentPropertyByKey("zuul.routes.mock-service.ip-whitelist", defaultValue);
        Assert.assertEquals("0:0:0:0:0:0:0:1,10.168.51.64", value);

        value = helper.getEnvironmentPropertyByKey("zuul.routes.security-service.ip-whitelist");
        Assert.assertEquals("", value);

        value = helper.getEnvironmentPropertyByKey("zuul.routes.security-service.ip-whitelist", defaultValue);
        Assert.assertEquals(defaultValue, value);

    }

}
