package com.sxt.test;

import java.util.Map;

import com.sxt.common.util.Esutil;
import com.sxt.common.util.JsonUtil;

public class ProductSearchTest {
	
	public static void main(String[] args) {
		Map<String, Object> map = Esutil.search("t9105（租机）", "shop", "product", 1, 10);
		System.out.println(JsonUtil.object2Json(map));
	}
}
