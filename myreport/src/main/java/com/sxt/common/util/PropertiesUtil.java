package com.sxt.common.util;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
	private static Properties prop = new Properties();

	public static Properties getProperties(String filename) throws Exception {
		if (filename != null && !filename.equals("")) {
			InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
			prop.load(fis);
			fis.close();
			return prop;
		}
		return null;
	}

	/**
	 * @param propName
	 * @return properties
	 * @throws Exception
	 */
	public static Properties readProperties(String propName) {
		Properties properties = null;
		try {
			properties = new Properties();
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream inStream = classLoader.getResourceAsStream(propName);
			if (inStream != null) {
				properties.load(inStream);
				inStream.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

	public static String getProperty(String fileName, String propName) {
		Properties properties = readProperties(fileName + ".properties");
		if (properties != null) {
			return (String) properties.get(propName);
		}
		return "";
	}
	
	public static long getLongProperty(String fileName, String propName, long defaultValue) {
		String value = getProperty(fileName, propName);
		if(value == null || "".equals(value.trim())) {
			return defaultValue;
		}
		return Long.valueOf(value);
	}
	
	public static int getIntProperty(String fileName, String propName, int defaultValue) {
		String value = getProperty(fileName, propName);
		if(value == null || "".equals(value.trim())) {
			return defaultValue;
		}
		return Integer.valueOf(value);
	}
	
	public static boolean getBooleanProperty(String fileName, String propName, boolean defaultValue) {
		String value = getProperty(fileName, propName);
		if(value == null || "".equals(value.trim())) {
			return defaultValue;
		}
		return Boolean.valueOf(value);
	}
	
}
