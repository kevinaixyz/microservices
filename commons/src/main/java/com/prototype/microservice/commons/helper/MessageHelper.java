package com.prototype.microservice.commons.helper;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

@Component
public class MessageHelper {

    private final static String MSG = ".msg";
    private final static String CODE = ".code";

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private HttpServletRequest httpRequest;

    public MessageSource getMessageSource() {
        return messageSource;
    }

    /**
     * @return error code per defined in message_en_us.properties
     */
    public String getCode(final String key) {
        try {
            return StringUtils.trimToEmpty(
                    getMessageSource().getMessage((key + CODE), null, Locale.US));
        } catch (NoSuchMessageException e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * @return error message per defined in message_en_US.properties
     */
    public String getMessage(final String key, final Object[] args, final Locale locale) {
        try {
            return StringUtils.trimToEmpty(
                    getMessageSource().getMessage((key + MSG), args, resolveCurrentLocale(locale)));
        } catch (NoSuchMessageException e) {
            return StringUtils.EMPTY;
        }
    }

    private Locale resolveCurrentLocale(final Locale locale) {
        if (locale != null) {
            return locale;
        } else {
            try {
                return RequestContextUtils.getLocale(httpRequest);
            } catch (Exception e) {
                // safeguard
                return Locale.US;
            }
        }
    }

}
