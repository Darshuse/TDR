package com.eastnets.domain.reporting;

import java.util.HashMap;
import java.util.Map;

public class ReportSetParameterTypeFactory {

	private static Map<Long, Class<? extends ReportSetParameter>> factory = new HashMap<Long, Class<? extends ReportSetParameter>>();
	private static Class<? extends ReportSetParameter> defaultClazz;

	public static void register(Long typeID, Class<? extends ReportSetParameter> clazz) {
		factory.put(typeID, clazz);
	}

	public static ReportSetParameter getReportSetParameter(Long typeID) {
		if (factory.isEmpty()) {
			ReportSetParameterTypes.register();
		}
		try {
			if (typeID == null) {
				return defaultClazz.newInstance();// default
			}
			if (!factory.containsKey(typeID)) {
				// System.err.println("Report parameter Type (" + typeID + ") not registerd, using default.");
				return defaultClazz.newInstance();
			}

			return factory.get(typeID).newInstance();
		} catch (Exception e) {
			System.err.println("Report parameter Type (" + typeID + ") cannot be created.");
			try {
				return defaultClazz.newInstance();
			} catch (Exception e2) {
			}
		}
		return null;
	}

	public static ReportSetParameter getDefaultReportSetParameter() {
		return getReportSetParameter(null);
	}

	public static void register(Class<? extends ReportSetParameter> defaultClazz) {
		ReportSetParameterTypeFactory.defaultClazz = defaultClazz;

	}
}
