package com.prototype.microservice.edge;

import java.text.MessageFormat;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.prototype.microservice.edge.helper.AppHelper;

@Component
public class EdgeServerAppRunner implements ApplicationRunner {

    private final static Logger LOG = LoggerFactory.getLogger(EdgeServerAppRunner.class);

    @Autowired
    private AppHelper appHelper;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (LOG.isInfoEnabled()) {
            LOG.info(MessageFormat.format(
                    "=========> {0} started with instanceId [{1}]",
                    new Object[]{this.getClass().getSimpleName(), appHelper.getInstanceId()}));
        }
        MDC.put("eureka.instance.metadataMap.instanceId", appHelper.getInstanceId());

    }

}
