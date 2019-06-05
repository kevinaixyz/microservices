package com.prototype.microservice.commons.helper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityHelper {

    public String getCurrentAuditor() {
        if (SecurityContextHolder.getContext() != null &&
                SecurityContextHolder.getContext().getAuthentication() != null &&
                StringUtils.isNotBlank(SecurityContextHolder.getContext().getAuthentication().getName())) {
            return StringUtils.trimToEmpty(SecurityContextHolder.getContext().getAuthentication().getName());
        } else {
            return "N/A";
        }
    }

}
