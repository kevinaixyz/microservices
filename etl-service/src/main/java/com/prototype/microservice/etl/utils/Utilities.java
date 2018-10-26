package com.prototype.microservice.etl.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities class
 */
public final class Utilities {

	private static final Logger LOG = LoggerFactory.getLogger(Utilities.class);

	/**
	 * Convert BigDecimal to String
	 *
	 * @param getterSrc
	 * @param setterDest
	 * @param src
	 * @param dest
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static <T> void convertBigDecimalToString(final Method getterSrc, final Method setterDest, final Object src,
			final T dest) throws IllegalAccessException, InvocationTargetException {
		if (getterSrc.invoke(src) != null) {
			final String val = getterSrc.invoke(src).toString();
			setterDest.invoke(dest, val);
		}
	}

	/**
	 * Convert Date to String
	 *
	 * @param getterSrc
	 * @param setterDest
	 * @param src
	 * @param dest
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static <T> void convertDateToString(final Method getterSrc, final Method setterDest, final Object src,
			final T dest) throws IllegalAccessException, InvocationTargetException {
		if (getterSrc.invoke(src) != null) {
			final Date date = (Date) getterSrc.invoke(src);
			final DateTime dt = new DateTime(date);

			final String stringDate = ISODateTimeFormat.date().print(dt);
			setterDest.invoke(dest, stringDate);
		}
	}

	/**
	 * Convert Object to String
	 *
	 * @param getterSrc
	 * @param setterDest
	 * @param src
	 * @param dest
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static <T> void convertObjToString(final Method getterSrc, final Method setterDest, final Object src,
			final T dest) throws IllegalAccessException, InvocationTargetException {
		String val = ToStringBuilder.reflectionToString(getterSrc.invoke(src));
		final int start = val.indexOf('=');

		if (start != -1) {
			final int end = val.length();
			val = val.substring(start + 1, end - 1);
			setterDest.invoke(dest, val);
		} else {
			setterDest.invoke(dest, "");
		}
	}

	/**
	 * Converts all properties in src class to string and return object of clazz
	 * type
	 *
	 * @param src
	 *            Object with properties to be converted
	 * @param clazz
	 *            Class of object that contains all String properties
	 * @return Object of clazz type containing all String properties
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws NoSuchMethodException
	 */
	public static <T> T propsToString(final Class<T> clazz, final Object... objects)
			throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		final Constructor<T> constr = clazz.getConstructor();
		final T dest = constr.newInstance();
		final Field[] fields = clazz.getDeclaredFields();

		for (final Field field : fields) {
			if ("serialVersionUID".equalsIgnoreCase(field.getName())) {
				continue;
			}

			final String fieldName = field.getName();
			final Class<?> fieldType = field.getType();
			final String getterName = "get" + fieldName.substring(0, 1).toUpperCase(Locale.ENGLISH)
					+ fieldName.substring(1);
			final String setterName = "set" + fieldName.substring(0, 1).toUpperCase(Locale.ENGLISH)
					+ fieldName.substring(1);

			for (final Object src : objects) {
				if (src == null) {
					continue;
				}
				try {
					final Method getterSrc = src.getClass().getDeclaredMethod(getterName);
					final Method setterDest = clazz.getDeclaredMethod(setterName, fieldType);

					final String type = getterSrc.getReturnType().getSimpleName();

					switch (type) {
					case "String":
					case "String[]":
						setterDest.invoke(dest, getterSrc.invoke(src));
						break;
					case "Date":
						convertDateToString(getterSrc, setterDest, src, dest);
						break;
					case "BigDecimal":
						convertBigDecimalToString(getterSrc, setterDest, src, dest);
						break;
					default:
						convertObjToString(getterSrc, setterDest, src, dest);
					}

				} catch (final NoSuchMethodException e) {
					if (LOG.isDebugEnabled()) {
						LOG.debug(MessageFormat.format("Utilities -> Line {0}, {1}",
								e.getStackTrace()[0].getLineNumber(), e));
					}
				}
			}
		}

		return clazz.cast(dest);
	}

	// public static <T> List<T> propsToString(final Class<T> clazz, final
	// Map<String, Object>... maps) {
	// final List<T> newList = new ArrayList<>();
	//
	// final Set<String> keys = new HashSet<>();
	// for (final Map<String, Object> map : maps) {
	// keys.addAll(map.keySet());
	// }
	//
	// keys.forEach(key -> {
	// newList.add(propsToString(clazz, ))
	// });
	// }

	/**
	 * Converts list of objects to new objects list with all String fields
	 *
	 * @param clazz
	 *            Class of object that contains all String properties
	 * @param list
	 *            List of objects to be converted
	 * @return
	 */
	public static <T, S> List<T> propsToStringForList(final Class<T> clazz, final List<S> list) {
		final List<T> newList = new ArrayList<>();
		list.forEach(o -> {
			try {
				newList.add(propsToString(clazz, o));
			} catch (final Exception e) {
				if (LOG.isDebugEnabled()) {
					LOG.debug(MessageFormat.format("Utilities -> Line {0}, {1}", e.getStackTrace()[0].getLineNumber(),
							e));
				}
			}
		});

		return newList;
	}

	private Utilities() {
	}
}
