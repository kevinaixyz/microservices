package com.prototype.microservice.commons.utils;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IPUtils {

    private final static Logger LOG = LoggerFactory.getLogger(IPUtils.class);

    private IPUtils() {
    }

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    private static final String UNKNOWN = "unknown";

    /**
     * Return the list of client IP address based on the IP header candidates
     *
     * @param request
     * @return
     */
    public static Set<String> getClientIpAddressCandidates(final HttpServletRequest request) {
        Set<String> result = new HashSet<>();
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !UNKNOWN.equalsIgnoreCase(ip)) {
                if (LOG.isInfoEnabled()) {
                    LOG.info(MessageFormat.format(
                            "getClientIpAddressCandidates -> Request[{0}] Header[{1}] -> Found Client IP Candidate[{2}]",
                            new Object[]{request, header, ip}));
                }
                result.add(ip);
            }
        }
        return result;
    }

    /**
     * Matches an IP (ipToMatch) with the Client IP address based on the IP header candidates.
     * Return true for match, false for mismatch.
     * Set matchLocalhost to true to match localhost IP address (i.e.: localhost will returns true by this method)
     *
     * @param request
     * @param ipToMatch
     * @param matchLocalhost
     * @return
     */
    public static boolean matchClientIpAddressCandidates(final HttpServletRequest request, final String ipToMatch, final boolean matchLocalhost) {
        // TODO match localhost
        Boolean result = getClientIpAddressCandidates(request).contains(ipToMatch);
        if (LOG.isInfoEnabled()) {
            LOG.info(MessageFormat.format(
                    "getClientIpAddressCandidates -> Request[{0}] -> matchClientIpAddressCandidates: ipToMatch[{1}] matchLocalhost[{2}] -> Result=[{3}]",
                    new Object[]{request, ipToMatch, matchLocalhost, result}));
        }
        return result;
    }

}
