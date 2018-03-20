package com.sxt.common.util;

public class StringUtils {
	// 空字符串
	public static final String EMPTY = "";

	// 字符串搜索时使用
	public static final int INDEX_NOT_FOUND = -1;

	public static boolean isBlank(String str) {
		int strLen;
		if (str == null || (strLen = str.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(str.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotBlank(String str) {
		return !StringUtils.isEmpty(str);
	}

	public static boolean equals(String str1, String str2) {
		return str1 == null ? str2 == null : str1.equals(str2);
	}

	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}

	public static boolean isNotEmpty(String str) {
		return !StringUtils.isEmpty(str);
	}

	/**
	 * 字符串空格处理
	 */
	public static final String trim(String value) {
		return value == null ? "" : value.trim();
	}
	
	/**
	 * 字符串替换处理
	 */
	public static final String replace_(String str1,String str2,String str3,String value){
		StringBuffer sBuffer = new StringBuffer(value);
		return sBuffer.replace(value.indexOf(str1)+1, value.indexOf(str2), str3).toString();
	}

}
