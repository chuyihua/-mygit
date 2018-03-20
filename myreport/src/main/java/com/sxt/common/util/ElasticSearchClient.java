package com.sxt.common.util;

import java.net.InetAddress;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

/**
 * 
 * TransportClient 客户端
 *
 */
public class ElasticSearchClient {//
	
	private static Settings settings = Settings.settingsBuilder()
			.put("cluster.name", "es-cluster") //设置ES实例的名称
			.put("client.transport.sniff", true) //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中
			.build();

	private static TransportClient client;

	static {
		try {
			client = TransportClient.builder().settings(settings).build();
			String[] clusterAddresses = PropertiesUtil.getProperty("es-cluster", "elastcisearch.cluster.address").split(";");
			for (String address : clusterAddresses) {
				String[] splits = PropertiesUtil.getProperty("es-cluster", address).split(":");
				InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(InetAddress.getByName(splits[0]), Integer.valueOf(splits[1]));
				client.addTransportAddress(transportAddress);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized TransportClient getTransportClient() {
		return client;
	}

	public static synchronized void colse() {
		client.close();
	}
	
	public static void main(String[] args) {
		System.out.println(client);
	}
}