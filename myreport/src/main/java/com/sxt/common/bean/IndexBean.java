package com.sxt.common.bean;


public class IndexBean{
	private String indexName;//索引名称
	private Integer shards;//分片
	private Integer replicas;//副本
	public String getIndexName() {
		return indexName;
	}
	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}
	public Integer getShards() {
		return shards;
	}
	public void setShards(Integer shards) {
		this.shards = shards;
	}
	public Integer getReplicas() {
		return replicas;
	}
	public void setReplicas(Integer replicas) {
		this.replicas = replicas;
	}
	
	
}
