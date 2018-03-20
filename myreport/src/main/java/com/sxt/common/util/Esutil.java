package com.sxt.common.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.highlight.HighlightField;

import com.sxt.common.bean.Doc;
import com.sxt.common.bean.IndexBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Esutil {
	private static Logger logger = Logger.getLogger(Esutil.class);
	
	
	//获得节点
	public List<DiscoveryNode> getConnectedNodes(){
		return ElasticSearchClient.getTransportClient().connectedNodes();
	}
	//获得mapping
	public static String getMapping(String index,String type){
		ImmutableOpenMap<String, MappingMetaData> mappings = ElasticSearchClient.getTransportClient()
				.admin()
				.cluster()
				.prepareState()
				.execute()
                .actionGet()
                .getState()
                .getMetaData()
                .getIndices()
                .get(index)
                .getMappings();
        String mapping = mappings.get(type).source().toString();
        return mapping;
	}
	/** 
	 * 增加mapping
     */  
	public static Boolean buildIndexMapping(String index,String type,String mapping){  
    	logger.info("buildIndexMapping start :the index is "+index+" the type is "+type);
    	PutMappingResponse response = ElasticSearchClient.getTransportClient()
	        .admin()
	        .indices()
	        .preparePutMapping(new String[]{index})
	        .setType(type)
	        .setSource(mapping)
	        .get();  
       logger.info("buildIndexMapping end :the index is "+index+" ,the type is "+type+"==success");
       return response.isAcknowledged();
    }  
	/**
	 *创建索引并设置分片、副本 
	 */
	public static Boolean createIndex(IndexBean bean) {
		TransportClient client = ElasticSearchClient.getTransportClient();
		Settings.Builder settings = Settings.builder();
		settings.put("number_of_shards", bean.getShards());//分片
		settings.put("number_of_replicas", bean.getReplicas());//副本
		CreateIndexResponse indexResponse = client
		    .admin().indices().prepareCreate(bean.getIndexName())
		    .setSettings(settings)
		    .get();
		return indexResponse.isAcknowledged();

	}
	/**
     * 判断指定的索引名是否存在
     * @param indexName 索引名
     * @return  存在：true; 不存在：false;
     */
    public static boolean isExistsIndex(String index){
        IndicesExistsResponse  response = 
        		ElasticSearchClient.getTransportClient().admin().indices().exists( 
                        new IndicesExistsRequest().indices(new String[]{index})).actionGet();
        return response.isExists();
    }
    
    /**
     * 判断指定的索引的类型是否存在
     * @param indexName 索引名
     * @param indexType 索引类型
     * @return  存在：true; 不存在：false;
     */
    public static boolean isExistsType(String indexName,String indexType){
        TypesExistsResponse  response = 
        		ElasticSearchClient.getTransportClient().admin().indices()
                .typesExists(new TypesExistsRequest(new String[]{indexName}, indexType)
                ).actionGet();
        return response.isExists();
    }
    /**
     *删除指定index 
     */
    public static Boolean deleteIndex(String index){
		DeleteIndexResponse response =  ElasticSearchClient.getTransportClient().admin().indices().prepareDelete(index)
                .execute().actionGet();
		return response.isAcknowledged();
	}
    /**
     * 添加文档内容到es
     * @param index
     * @param type
     * @param jsonDocument
     * @return
     */
    public static Boolean addIndexDocument(String index,String type,String jsonDocument){
    	IndexResponse response = ElasticSearchClient.getTransportClient().prepareIndex(index, type)
    	        .setSource(jsonDocument)
    	        .get();
    	return response.isCreated();
    }
    
    public static Boolean deleteType(String index,String type){
    	DeleteRequest deleteRequest = new DeleteRequest(index);
    	ElasticSearchClient.getTransportClient();
    	
    	DeleteResponse deleteResponse = ElasticSearchClient.getTransportClient().prepareDelete().setIndex(index).setType(type).execute().actionGet();
//		DeleteIndexResponse response =  ElasticSearchClient.getTransportClient().admin().indices().prepareDelete(index)
//                .execute().actionGet();
//		return response.isAcknowledged();
    	return deleteResponse.isFound();
	}
    
	public static String addIndex(String index,String type,Doc Doc){
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("id", Doc.getId());
		hashMap.put("title", Doc.getTitle());
		hashMap.put("describe", Doc.getDescribe());
		hashMap.put("author", Doc.getAuthor());
		
		IndexResponse response =  ElasticSearchClient.getTransportClient().prepareIndex(index, type).setSource(hashMap).execute().actionGet();
		return response.getId();
	}
	
	
	
	public static Map<String, Object> search(String key,String index,String type,int start,int row){
		SearchRequestBuilder builder =  ElasticSearchClient.getTransportClient().prepareSearch(index);
		builder.setTypes(type);
		builder.setFrom(start);
		builder.setSize(row);
		//设置高亮字段名称
		builder.addHighlightedField("name");
		builder.addHighlightedField("name");
		//设置高亮前缀
		builder.setHighlighterPreTags("<font color='red' >");
		//设置高亮后缀
		builder.setHighlighterPostTags("</font>");
		builder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
		if(StringUtils.isNotBlank(key)){
//			builder.setQuery(QueryBuilders.termQuery("title",key));
			builder.setQuery(QueryBuilders.multiMatchQuery(key, "name","name"));
		}
		builder.setExplain(true);
		SearchResponse searchResponse = builder.get();
		
		SearchHits hits = searchResponse.getHits();
		long total = hits.getTotalHits();
		Map<String, Object> map = new HashMap<String,Object>();
		SearchHit[] hits2 = hits.getHits();
		map.put("count", total);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (SearchHit searchHit : hits2) {
			Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
			HighlightField highlightField = highlightFields.get("name");
			Map<String, Object> source = searchHit.getSource();
			if(highlightField!=null){
				Text[] fragments = highlightField.fragments();
				String name = "";
				for (Text text : fragments) {
					name+=text;
				}
				source.put("name", name);
			}
			HighlightField highlightField2 = highlightFields.get("name");
			if(highlightField2!=null){
				Text[] fragments = highlightField2.fragments();
				String describe = "";
				for (Text text : fragments) {
					describe+=text;
				}
				source.put("name", describe);
			}
			list.add(source);
		}
		map.put("dataList", list);
		return map;
	}
	
	public static Map<String, String> getSearchAll(String query,Integer size,String index,String type){
		TransportClient client = ElasticSearchClient.getTransportClient();
		Map<String, String> map =new HashMap<String, String>();
		Set<String> set = new HashSet();
		SearchResponse response = client.prepareSearch(index)
				.setTypes(type)
				.setSearchType(SearchType.SCAN)
				.setQuery(query)
                .setSize(size)
                .setScroll(new TimeValue(100000))
                .execute()  
                .actionGet();
		//获取总数量  
        long totalCount = response.getHits().getTotalHits(); 
        map.put("pv", ""+totalCount);
        int page=(int)totalCount/(size);//计算总页数,每次搜索数量为分片数*设置的size大小  
        System.out.println("总量"+totalCount+"总业数："+page); 
        for (int i = 0; i <= page; i++) {  
            //再次发送请求,并使用上次搜索结果的ScrollId  
            response = client  
                    .prepareSearchScroll(response.getScrollId())  
                    .setScroll(new TimeValue(20000)).execute()  
                    .actionGet();  
            SearchHits hits = response.getHits();
            for (SearchHit searchHit : hits.getHits()) {  
                try {  
                	set.add((String)searchHit.getSource().get("name"));
                } catch (Exception e) {  
                    e.printStackTrace();  
                } 
            }  
            
        }  
        map.put("uv", ""+set.size());
        return map;
	}

	public static void main(String[] args) {
		getSearchAll( "{\"query\":{\"bool\":{\"must\":[{\"match\":{\"brand_id\":\"小米\"}}]}}}", 20, "shop", "product");
	}
	/*public static void main(String[] args) throws Exception {
		TransportClient client = ElasticSearchClient.getTransportClient();
		int j=0;
		SearchResponse response = client.prepareSearch("shop")
				.setTypes("product")
				.setSearchType(SearchType.SCAN)
				.setQuery("{\"query\":{\"bool\":{\"must\":[{\"match\":{\"brand_id\":\"小米\"}}]}}}")
                .setSize(10)
                .setScroll(new TimeValue(20000))
                .execute()  
                .actionGet();
		
		//获取总数量  
        long totalCount = response.getHits().getTotalHits();  
        int page=(int)totalCount/(10);//计算总页数,每次搜索数量为分片数*设置的size大小  
        System.out.println("总量"+totalCount+"总业数："+page);  
        for (int i = 0; i <= page; i++) {  
            //再次发送请求,并使用上次搜索结果的ScrollId  
            response = client  
                    .prepareSearchScroll(response.getScrollId())  
                    .setScroll(new TimeValue(20000)).execute()  
                    .actionGet();  
            SearchHits hits = response.getHits();  
            System.out.println("-----------begin------------"+i);  
            for (SearchHit searchHit : hits.getHits()) {  
                try {  
                	j++;
//                    i++;  
                    String id = searchHit.getId();
                    System.out.println("第" + j + "条数据:" + searchHit.getSourceAsString());  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
            System.out.println("-----------end------------");   
        }  
        System.out.println("j======"+j);
	}*/
}
