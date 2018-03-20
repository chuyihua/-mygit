package com.sxt.common.util;



public class ElasticSearchJestConstants {
	public static String configFlie = "es-cluster-jest";
	public static String clusterName = PropertiesUtil.getProperty(configFlie, "elastcisearch.cluster.name");
	
	public static String index_activity_nginx_access = PropertiesUtil.getProperty(configFlie, "elastcisearch.cluster.activity.nginx.access.index");
	public static String type_activity_nginx_access = PropertiesUtil.getProperty(configFlie, "elastcisearch.cluster.activity.nginx.access.type");
	public static String type_activity_nginx_access_analysis = PropertiesUtil.getProperty(configFlie, "elastcisearch.cluster.activity.nginx.accessAnalysis.type");
	
	
	public static String index_mall_nginx_access = PropertiesUtil.getProperty(configFlie, "elastcisearch.cluster.mall.nginx.access.index");
	public static String type_mall_nginx_access = PropertiesUtil.getProperty(configFlie, "elastcisearch.cluster.mall.nginx.access.type");
	

	public static String index_mall_shopreport_access = PropertiesUtil.getProperty(configFlie, "elastcisearch.cluster.mall.shopreport.access.index");
	public static String type_mall_shopreport_access = PropertiesUtil.getProperty(configFlie, "elastcisearch.cluster.mall.shopreport.access.type");
	

	public static String index_mall_product_access = PropertiesUtil.getProperty(configFlie, "elastcisearch.cluster.mall.product.access.index");
	public static String type_mall_product_access = PropertiesUtil.getProperty(configFlie, "elastcisearch.cluster.mall.product.access.type");
	
	public static String index_mall_order_access = PropertiesUtil.getProperty(configFlie, "elastcisearch.cluster.mall.order.access.index");
	public static String type_mall_order_access = PropertiesUtil.getProperty(configFlie, "elastcisearch.cluster.mall.order.access.type");
	
	public static int MAX_RECORD = 100; // 10条数据
	public static int TIME_LIMIT_SECOND = 10 * 1000; // 10秒
}
