package com.prototype.microservice.cms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.MessageFormat;

@SpringBootApplication
public class CmsService {
    private final static Logger LOG = LoggerFactory.getLogger(CmsService.class);

    public static void main(String[] args) {
        SpringApplication.run(CmsService.class, args);
        if (LOG.isInfoEnabled()) {
            LOG.info(MessageFormat.format("{0} is running.", new Object[] { CmsService.class.getName() }));
        }
    }
}
