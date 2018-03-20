package com.sxt.test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sxt.common.util.ElasticSearchJestClient;
import com.sxt.common.util.ElasticSearchJestHelper;
import com.sxt.common.util.JsonUtil;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchScroll;
import io.searchbox.core.search.aggregation.TermsAggregation;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.indices.mapping.PutMapping;
import io.searchbox.indices.settings.GetSettings;
import io.searchbox.indices.settings.UpdateSettings;
import io.searchbox.indices.type.TypeExist;
import io.searchbox.params.Parameters;
import io.searchbox.params.SearchType;

public class estest {
	private static final JestClient client = ElasticSearchJestClient.getJestClient();
	private static final String index="mall";
	private static final String type="order";
	
	public static void getSearchAll() throws IOException{
		JestClient client = ElasticSearchJestClient.getJestClient();
		Search search = new Search.Builder("{\"_source\":[\"name\",\"brand_id\"],\"query\":{\"bool\":{\"must\":[{\"match\":{\"name\":\"小米\"}}]}}}")
				.addIndex("shop")
				.addType("product")
				.setParameter(Parameters.SIZE, 20)
				.setParameter(Parameters.SCROLL, "5m")
				.setParameter(Parameters.TIMEOUT, "5m")
				.build();
		JestResult rs = client.execute(search);
		int j=0;
		int total = rs.getJsonObject().getAsJsonObject("hits").get("total").getAsInt();
	
		int page = (int) Math.ceil(total/20.0);
		for(int i=0;i<page;i++){
			List<Map> sourceAsObjectList = rs.getSourceAsObjectList(Map.class);
			System.out.println("===========第"+(i+1)+"页begin============");
			for(Map map :sourceAsObjectList){
				j++;
				System.out.println("第" + j + "条数据:"+JsonUtil.object2Json(map));
			}
			System.out.println("===========第"+(i+1)+"页end============");
			String scrollId = rs.getJsonObject().get("_scroll_id").getAsString();
			SearchScroll scroll = new SearchScroll.Builder(scrollId, "5m").build();
			rs = client.execute(scroll);
		}
		System.out.println("====一共"+total+"条，共"+page+"页");
	}
	
	
	public static void main(String[] args) throws IOException {
		
		getSearchAll();
//		{"size":180,"query":{"match_phrase":{"name":"小米5c"}}}
//		List<product> list = ElasticSearchJestHelper.getSearchAll("shop", "product", 
//				"{\"_source\":[\"name\",\"brand_id\"],\"query\":{\"bool\":{\"must\":[{\"match\":{\"brand_id\":\"小米\"}}]}}}", 
//				20, 
//				product.class);
////		System.out.println("====="+list.size());
//		JestClient client = ElasticSearchJestClient.getJestClient();
//		Search search = new Search.Builder("{\"aggss\": {\"name\": {\"terms\": {\"field\": \"brand_id\"}}}}").addIndex("mall").addType("product").build();
//		JestResult rs = client.execute(search);
//		System.out.println("--"+rs.getErrorMessage());
//		System.out.println("==="+rs.getSourceAsString());
//		JestClient client = ElasticSearchJestClient.getJestClient();
//		
//		Search search = new Search.Builder("{\"aggs\": {\"name\": {\"terms\": {\"field\": \"brand_id\"}}}}")
//				.addIndex("shop")
//				.addType("product")
//				.setParameter(Parameters.SIZE, 20)
//				.setParameter(Parameters.SCROLL, "5m")
//				.build();
//		JestResult rs = client.execute(search);
//		int j=0;
//		int total = rs.getJsonObject().getAsJsonObject("hits").get("total").getAsInt();
//	
//		int page = (int) Math.ceil(total/20.0);
//		for(int i=0;i<page;i++){
//			List<Map> sourceAsObjectList = rs.getSourceAsObjectList(Map.class);
//			System.out.println("===========第"+(i+1)+"页begin============");
//			for(Map map :sourceAsObjectList){
//				j++;
//				System.out.println("第" + j + "条数据:"+JsonUtil.object2Json(map));
//			}
//			System.out.println("===========第"+(i+1)+"页end============");
//			String scrollId = rs.getJsonObject().get("_scroll_id").getAsString();
//			SearchScroll scroll = new SearchScroll.Builder(scrollId, "5m").build();
//			rs = client.execute(scroll);
//		}
//		System.out.println("====一共"+total+"条，共"+page+"页");
		
		
//		Object s = "{\"order2\":{ \"dynamic\":\"strict\",\"_all\":{\"enabled\":false}, \"properties\":{ \"brandid\":{ \"type\":\"string\",\"index\":\"not_analyzed\"},\"citycode\":{\"type\":\"string\",\"index\":\"not_analyzed\"}}}}";
//		putMapping("mall", "order2", s);
//		System.out.println("mapping=="+getMapping("ceshi","order"));
		
//		Object s="{\"number_of_replicas\":2}";
	}
	//获得mapping
	private static String getMapping(String index,String type){
		GetMapping m = new GetMapping.Builder().addIndex(index).addType(type).build();
		try {
			JestResult result = client.execute(m);
			return result.getJsonString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	} 
	//判断索引是否存在
	private static Boolean indicesExists(String index){
		IndicesExists indicesExists = new IndicesExists.Builder(index).build();
		try {
			JestResult result = client.execute(indicesExists);
			return result.isSucceeded();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	//判断类型是否存在
	private static Boolean typeExists(String index,String type){
		TypeExist typeExist = new TypeExist.Builder(index).addType(type).build();
		try {
			JestResult result = client.execute(typeExist);
			return result.isSucceeded();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	//创建索引
	private static boolean createIndex(String index){
		CreateIndex createIndex = new CreateIndex.Builder(index).build();
		try {
			JestResult result = client.execute(createIndex);
			return result.isSucceeded();	
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	//添加mapping
	private static boolean putMapping(String index,String type,Object source){
		PutMapping putMapping = new PutMapping.Builder(index, type, source).build();
		try {
			JestResult result = client.execute(putMapping);
			return result.isSucceeded();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	//
	private static boolean updateSettings(Object source){
		UpdateSettings updateSettings = new UpdateSettings.Builder(source).addIndex("mall").build();
		try {
			JestResult result = client.execute(updateSettings);
			return result.isSucceeded();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	private static boolean deleteIndex(String index){
		DeleteIndex deleteIndex = new DeleteIndex.Builder(index).build();
		try {
			JestResult result = client.execute(deleteIndex);
			return result.isSucceeded();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
//	private static String getSetting(){
//		GetSettings
//	}
}
