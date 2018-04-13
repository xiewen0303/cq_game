package com.junyou.utils.common;

import java.util.Properties;

import com.junyou.utils.exception.JunYouCustomException;

public class PropertyUtil {
	public static int getInt(Properties pro, String key) {
		if (pro == null) {
			throw new JunYouCustomException("Properties == null");
		}
		String value = pro.getProperty(key);
		if (value == null) {
			throw new JunYouCustomException(String.format("缺少属性%s", key));
		} else {
			return Integer.valueOf(value);
		}
	}

	public static boolean getBoolean(Properties pro, String key) {
		if (pro == null) {
			throw new JunYouCustomException("Properties == null");
		}
		String value = pro.getProperty(key);
		if (value == null) {
			throw new JunYouCustomException(String.format("缺少属性%s", key));
		} else {
			return Boolean.valueOf(value);
		}
	}

	public static String getString(Properties pro, String key) {
		if (pro == null) {
			throw new JunYouCustomException("Properties == null");
		}
		String value = pro.getProperty(key);
		if (value == null) {
			throw new JunYouCustomException(String.format("缺少属性%s", key));
		} else {
			return value;
		}
	}
}
