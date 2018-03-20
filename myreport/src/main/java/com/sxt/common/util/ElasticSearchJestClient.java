package com.sxt.common.util;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * JestClient
 *
 */
public class ElasticSearchJestClient {

	private static JestClientFactory factory = new JestClientFactory();
	private static Set<String> servers = new LinkedHashSet<String>();
	private static JestClient client;
	
	static {
		try {
			String[] clusterAddresses = PropertiesUtil.getProperty(
					ElasticSearchJestConstants.configFlie,
					"elastcisearch.cluster.address").split(";");
			for (String address : clusterAddresses) {
				String uri = PropertiesUtil.getProperty(
						ElasticSearchJestConstants.configFlie, address);
				servers.add(uri);
			}
			factory.setHttpClientConfig(new HttpClientConfig.Builder(servers)
					.discoveryEnabled(true)
					.discoveryFrequency(1l, TimeUnit.MINUTES).connTimeout(60000).readTimeout(120000)
					.multiThreaded(true).build());
			client = factory.getObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized JestClient getJestClient() {
		return client;
	}

}