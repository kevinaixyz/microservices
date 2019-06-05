package com.prototype.microservice.commons.helper;

import java.io.File;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.prototype.microservice.commons.constants.CommonConstants;

@Component
public class CommonHelper {

    private final static Logger LOG = LoggerFactory.getLogger(CommonHelper.class);

    @Value("${spring.application.name:UNKNOWN_APPLICATION}")
    private String appName;

    @Value("${eureka.instance.metadataMap.instanceId:UNKNOWN_INSTANCE}")
    private String appInstanceName;

    @Autowired
    private HttpServletRequest httpRequest;

    /**
     * @return the app name per defined in "spring.application.name"
     */
    public String getAppName() {
        return appName;
    }

    /**
     * @return the app instance name per defined in "eureka.instance.metadataMap.instanceId"
     */
    public String getAppInstanceName() {
        if ("UNKNOWN_INSTANCE".equals(appInstanceName) && !"UNKNOWN_APPLICATION".equals(appName)) {
            return appName;
        } else {
            return appInstanceName;
        }
    }

    /**
     * Persist the JSON Correlation ID per request.
     *
     * @param correlationID
     */
    public void rememberJsonCorrelationIDPerRequest(final String correlationID) {

        if (httpRequest != null && StringUtils.isNotBlank(correlationID)) {
            httpRequest.setAttribute(CommonConstants.HTTP_ATTRIBUTE_CORRELATION_ID, StringUtils.trimToEmpty(correlationID));
        } else {
            if (LOG.isWarnEnabled()) {
                LOG.warn(MessageFormat.format(
                        "Cannot remember JSON Correlation ID -> HTTP Request [{0}] Correlation ID [{1}]",
                        new Object[]{httpRequest, correlationID}));
            }
        }

    }

    /**
     * Get the persisted JSON Correlation ID per request. N/A will be returned if it is not set.
     *
     * @return
     */
    public String getJsonCorrelationIDPerRequest() {

        if (httpRequest != null && httpRequest.getAttribute(CommonConstants.HTTP_ATTRIBUTE_CORRELATION_ID) instanceof String) {
            return (String) httpRequest.getAttribute(CommonConstants.HTTP_ATTRIBUTE_CORRELATION_ID);
        } else {
            return CommonConstants.NA;
        }

    }

    /**
     * Returns a Java 8 current local timestamp.
     *
     * @return
     */
    public ZonedDateTime getLocalisedCurrentTimestamp() {
        return Instant.now().atZone(ZoneOffset.systemDefault());
    }

    /**
     * Returns the current local timestamp in the ISO-8601 format of yyyy-MM-dd'T'HH:mm:ss.SSSZ
     *
     * @return
     */
    public String getLocalisedCurrentTimestampAsString() {
        return DateTimeFormatter.ofPattern(CommonConstants.ISO_8601_TIMESTAMP_FORMAT).format(getLocalisedCurrentTimestamp());
    }

    /**
     * Convert a long timestamp into the ISO-8601 format of yyyy-MM-dd'T'HH:mm:ss.SSSZ
     *
     * @param timestamp
     * @return
     */
    public String formatTimestampToString(long timestamp) {
        return DateTimeFormatter.ofPattern(CommonConstants.ISO_8601_TIMESTAMP_FORMAT).format(
                (new Timestamp(timestamp)).toInstant().atZone(ZoneOffset.systemDefault()));
    }

    /**
     * Convert a timestamp string in the ISO-8601 format of yyyy-MM-dd'T'HH:mm:ss.SSSZ into a long value
     *
     * @param timestampString
     * @return
     */
    public Long formatTimestampStringToLong(String timestampString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(CommonConstants.ISO_8601_TIMESTAMP_FORMAT);
        return sdf.parse(timestampString).getTime();
    }

    /**
     * Returns a random UUID with enhanced uniqueness.
     *
     * @return
     */
    public String secureRandomUUID() {
        return MessageFormat.format("{0}-{1}",
                UUID.randomUUID().toString(),
                String.valueOf(Instant.now().getNano()));
    }

    /**
     * Go back in time given a number of day.
     *
     * @param timestamp
     * @param backday
     * @return
     */
    public Timestamp backday(final Timestamp timestamp, final int backday) {

        Date backdate = DateUtils.addDays(new Date(timestamp.getTime()), (backday * -1));
        return Timestamp.from(backdate.toInstant());

    }

    /**
     * Returns a new file object under the /temp/ directory with name
     * yyyyMMdd-HHmmss.SSS.ext
     *
     * @param ext
     * @return
     */
    public File getTempFile(final String ext) {

        return new File(MessageFormat.format(
                "/temp/{0}.{1}",
                DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd-HHmmss.SSS"),
                ext));

    }

    /**
     * Convert an array of string into a single comma separated string.
     *
     * @param strings
     * @return
     */
    public String arrayToString(String... strings) {
        if (strings == null || strings.length == 0) {
            return "";
        } else {
            return String.join(",", strings);
        }
    }

}
