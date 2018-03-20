package com.sxt.common.util;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Search;
import io.searchbox.core.SearchScroll;
import io.searchbox.indices.ClearCache;
import io.searchbox.params.Parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ElasticSearchJestHelper {
	private static Log logger = LogFactory.getLog(ElasticSearchJestHelper.class);

	/**
	 * 清理内存
	 */
	public static void clearCache(String index){
		long start = System.currentTimeMillis();
		logger.info("ESClearCache start ==== " + start);
		JestResult searchResult ;
		JestClient client = ElasticSearchJestClient.getJestClient();
		try {
			if(index == null || index.length()==0){
				searchResult = client.execute(new ClearCache.Builder().addIndex(index).filter(true).fieldData(true).build());
			}else{
				searchResult = client.execute(new ClearCache.Builder().filter(true).fieldData(true).build());
			}
			logger.info("ESClearCache send:" + searchResult.toString());
		} catch (IOException e) {
			logger.error("ElasticSearchJestHelper clearCache has error!", e);
			e.printStackTrace();
		}
		logger.info("ESClearCache end ==== " + (System.currentTimeMillis()-start));
	}
	/**
	 * 查询并返回es所有结果
	 * @param index
	 * @param type
	 * @param query 查询的DSL语句
	 * @param size 每次取的条数
	 * @param T 
	 * @return
	 * @throws IOException
	 */
	public static <T> List<T> getSearchAll(String index,String type,String query, Integer size,Class<T> T) throws IOException{
		JestClient client = ElasticSearchJestClient.getJestClient();
		Search search = new Search.Builder(query)
				.addIndex(index)
				.addType(type)
				.setParameter(Parameters.SIZE, size)
				.setParameter(Parameters.SCROLL, "5m")
				.build();
		JestResult rs = client.execute(search);
		logger.info("query: "+query);
		int total = rs.getJsonObject().getAsJsonObject("hits").get("total").getAsInt();
		logger.info("总的记录数："+total+" size:"+size);
		List<T> list =new ArrayList<T>(); 
		int page = (int) Math.ceil(total*1.0/size);
		for(int i=0;i<page;i++){
			list.addAll(rs.getSourceAsObjectList(T));
			String scrollId = rs.getJsonObject().get("_scroll_id").getAsString();
			SearchScroll scroll = new SearchScroll.Builder(scrollId, "5m").build();
			rs = client.execute(scroll);
		}
		return list;
	}
	
}
