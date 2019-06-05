package com.prototype.microservice.etl.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class EtlMessageHelper {
    @Autowired
    private MessageSource messageSource;

    public String getMessage(String code) {
        return messageSource.getMessage(code, null, Locale.US);
    }

    public String getMessage(String code, Object[] values) {
        return messageSource.getMessage(code, values, Locale.US);
    }

    public String getMessage(String code, Locale locale) {
        return messageSource.getMessage(code, null, locale);
    }

    public String getMessage(String code, Object[] values, Locale locale) {
        return messageSource.getMessage(code, values, locale);
    }
}
