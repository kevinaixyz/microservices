package com.prototype.microservice.cms.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.prototype.microservice.cms.constant.CmsServiceConstant;
import com.prototype.microservice.cms.converter.BigDecimalGsonAdapter;
import com.prototype.microservice.cms.converter.BooleanGsonAdapter;
import com.prototype.microservice.cms.converter.LocalDateGsonAdapter;
import com.prototype.microservice.cms.converter.LocalDateTimeGsonAdapter;
import com.prototype.microservice.cms.converter.LongGsonAdapter;
import com.prototype.microservice.cms.entity.BaseEntity;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class CmsHelper {
    @Value("${cms-service.baseDate}")
    private String baseDate;
    @Value("${cms-service.batch.file.path}")
    private String filePath;
    @Value("${cms-service.batch.file.datePattern}")
    private String fileDatePattern;
    @Value("${cms-service.batch.file.pattern}")
    private String fileNamePattern;
    @Value("${cms-service.batch.file.tradeDateFormat}")
    private String tradeDateFormat;
    @Value("${cms-service.batch.file.headerRowIndex}")
    private int headerRowIndex;
    @Value("${cms-service.batch.test.isTest}")
    private boolean isTest;
    @Value("${cms-service.batch.test.systemDate}")
    private String systemDate;

    private static final String HK_TIME_ZONE = "UTC+8";
    private static final String REGEX_NUMBER = "^[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$";
    private static final String XLS = ".xls";
    private static final String XLSX = ".xlsx";
    private static final String[] INVALID_STRING = {"-"};

    public File getFile(LocalDate date) {
        File cmsFile = null;
        if (filePath != null && fileNamePattern != null) {
            String dateStr = date.format(DateTimeFormatter.ofPattern(fileDatePattern));
            String cmsFileName = fileNamePattern.replace(fileDatePattern, dateStr);
            try {
                File dir = new File(filePath);
                if (dir.exists() && dir.isDirectory()) {
                    Path startPath = dir.toPath();
                    List<File> files = Files.find(startPath, Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile() && (filePath.getFileName().toString().equals(cmsFileName + XLS) || filePath.getFileName().toString().equals(cmsFileName + XLSX))).map(Path::toFile).collect(Collectors.toList());
                    if (files.size() > 0) {
                        cmsFile = files.get(0);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cmsFile;
    }

    public static LocalDateTime getFileLastModified(File file) {
        if (file != null && file.exists()) {
            long time = file.lastModified();
            Instant instant = Instant.ofEpochMilli(time);
            return instant.atZone(ZoneId.of(HK_TIME_ZONE)).toLocalDateTime();
        }
        return null;
    }

    public boolean iscmsFileExist(LocalDate date) {
        return getFile(date) != null;
    }

    public static Date getCurrentDate() {
        LocalDate localDate = LocalDate.now();
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.of(HK_TIME_ZONE)).toInstant());
    }

    public static Date getCurrentDateTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Instant instant = localDateTime.atZone(ZoneId.of(HK_TIME_ZONE)).toInstant();
        return Date.from(instant);
    }

    public static LocalDateTime getCurrentLocalDateTime() {
        return LocalDateTime.now(ZoneId.of(HK_TIME_ZONE));
    }

    public static String getCurrentDateTimeStr() {
        return formatLocalDateTime(getCurrentLocalDateTime());
    }

    public static LocalDate getPrevWeekday(LocalDate date) {
        LocalDate prevTradeDate = LocalDate.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

        if (date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            prevTradeDate = prevTradeDate.minusDays(2);
        } else if (date.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            prevTradeDate = prevTradeDate.minusDays(3);
        } else {
            prevTradeDate = prevTradeDate.minusDays(1);
        }
        return prevTradeDate;
    }

    public static LocalDate parseIsoDate(String dateStr) {
        LocalDate localDate = null;
        if (StringUtils.isNotBlank(dateStr)) {
            try {
                LocalDateTime localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(CmsServiceConstant.ISO_8601_TIMESTAMP_FORMAT_NEW));
                localDate = localDateTime.toLocalDate();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return localDate;
    }

    public static LocalDateTime parseIsoDateTime(String dateStr) {
        LocalDateTime localDateTime = null;
        if (!"".equals(dateStr)) {
            try {
                localDateTime = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(CmsServiceConstant.ISO_8601_TIMESTAMP_FORMAT_NEW));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return localDateTime;
    }

    public static LocalDate toLocalDate(Date date) {
        LocalDate localDate = null;
        if (date != null) {
            localDate = date.toInstant().atZone(ZoneId.of(HK_TIME_ZONE)).toLocalDate();
        }
        return localDate;
    }

    public static Date localDateToUtilDate(LocalDate localDate) {
        Date date = null;
        if (localDate != null) {
            date = Date.from(localDate.atStartOfDay().atZone(ZoneId.of(HK_TIME_ZONE)).toInstant());
        }
        return date;
    }

    public static String formatLocalDate(LocalDate localDate) {
        String dateStr = null;
        ZonedDateTime datetime = ZonedDateTime.of(localDate.atStartOfDay(), ZoneId.of(HK_TIME_ZONE));
        dateStr = datetime.format(DateTimeFormatter.ofPattern(CmsServiceConstant.ISO_8601_TIMESTAMP_FORMAT_NEW));
        //(DateTimeFormatter.ofPattern(CmsServiceConstant.ISO_8601_TIMESTAMP_FORMAT));
        return dateStr;
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        String dateStr = null;
        ZonedDateTime datetime = ZonedDateTime.of(localDateTime, ZoneId.of(HK_TIME_ZONE));
        dateStr = datetime.format(DateTimeFormatter.ofPattern(CmsServiceConstant.ISO_8601_TIMESTAMP_FORMAT_NEW));
        //(DateTimeFormatter.ofPattern(CmsServiceConstant.ISO_8601_TIMESTAMP_FORMAT));
        return dateStr;
    }

    public String toJson(Object o) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateGsonAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeGsonAdapter())
                .registerTypeAdapter(Boolean.class, new BooleanGsonAdapter())
                .registerTypeAdapter(BigDecimal.class, new BigDecimalGsonAdapter())
                .registerTypeAdapter(Long.class, new LongGsonAdapter())
                .create();
        return gson.toJson(o);
    }

    public <T extends Object> T fromJson(String json, Class<T> type) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateGsonAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeGsonAdapter())
                .registerTypeAdapter(Boolean.class, new BooleanGsonAdapter())
                .registerTypeAdapter(BigDecimal.class, new BigDecimalGsonAdapter())
                .registerTypeAdapter(Long.class, new LongGsonAdapter())
                .create();
        return gson.fromJson(json, type);
    }

    public LocalDate getSystemDate() {
        LocalDate systemLocalDate = LocalDate.now();
        if (StringUtils.isNotBlank(systemDate)) {
            systemLocalDate = LocalDate.parse(systemDate);
        }
        return systemLocalDate;
    }

    public LocalDate parseExcelTradeDate(String dateStr) {
        LocalDate localDate = null;
        if (!"".equals(dateStr)) {
            try {
                localDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(fileDatePattern));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return localDate;
    }

    public String formatRawTradeDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat(fileDatePattern);
        return df.format(date);
    }

    public LocalDate getNextTradeDate(LocalDate date) {
        LocalDate nextTradeDate = null;
        if (date.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
            nextTradeDate = date.plusDays(3);
        } else if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            nextTradeDate = date.plusDays(2);
        } else {
            nextTradeDate = date.plusDays(1);
        }
        return nextTradeDate;
    }

    public static BigDecimal stringToBigDecimal(String str) {
        if (StringUtils.isNotBlank(str)) {
            return new BigDecimal(str);
        }
        return null;
    }

    public static String bigDecimalToString(BigDecimal b) {
        return b == null ? null : b.toString();
    }

    public static boolean isNumber(String str) {
        if (str.matches(REGEX_NUMBER)) {
            return true;
        }
        return false;
    }

    public static <T extends BaseEntity> void copyCommonFieldValue(final T source, final T target) {
        target.setActive(source.isActive());
        if (StringUtils.isNotBlank(source.getCreatedUser())) {
            target.setCreatedUser(source.getCreatedUser());
        }
        if (source.getCreatedDateTime() != null) {
            target.setCreatedDateTime(source.getCreatedDateTime());
        }
        if (StringUtils.isNotBlank(source.getLastUpdUser())) {
            target.setLastUpdUser(source.getLastUpdUser());
        }
        if (source.getLastUpdDateTime() != null) {
            target.setLastUpdDateTime(source.getLastUpdDateTime());
        }
    }

    public static String[] getNullPropertyNames(Object source) {
        List<String> emptyNames = new ArrayList<String>();
        Class clazz = source.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            String m = "get" + StringUtils.capitalize(fieldName);
            Object value;
            try {
                value = clazz.getMethod(m).invoke(source);
                if (value == null) {
                    emptyNames.add(fieldName);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    // then use Spring BeanUtils to copy and ignore null
    public static void myCopyProperties(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    public static boolean isInvalidStr(String str) {
        boolean res = false;
        if (StringUtils.isBlank(str)) {
            return true;
        }
        for (String item : INVALID_STRING) {
            if (item.toLowerCase().equals(str.toLowerCase())) {
                res = true;
                break;
            }
        }
        return res;
    }

    public LocalDate getBaseDate() {
        LocalDate baseLocalDate = null;
        LocalDate defaultDate = LocalDate.of(2018, 4, 1);
        //Format: yyyy-MM-dd
        String baseDateStr = baseDate;
        try {
            if (StringUtils.isNotBlank(baseDateStr)) {
                baseLocalDate = LocalDate.parse(baseDateStr);
            } else {
                baseLocalDate = defaultDate;
            }
        } catch (Exception e) {
            e.printStackTrace();
            baseLocalDate = defaultDate;
        }

        return baseLocalDate;
    }

    public static BigDecimal addPosition(BigDecimal a, BigDecimal b) {
        BigDecimal newSharesNum = BigDecimal.ZERO;
        if (a != null && b != null) {
            newSharesNum = a.add(b);
        }
        if (a == null || b == null) {
            newSharesNum = (a == null ? (b == null ? BigDecimal.ZERO : b) : a);
        }
        return newSharesNum;
    }
}
