package com.sxt.es;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.sxt.common.bean.IndexBean;
import com.sxt.common.util.ConstantsUtil;
import com.sxt.common.util.Esutil;
import com.sxt.common.util.JsonUtil;

public class EsProductHelp {
	private Logger logger = Logger.getLogger(EsProductHelp.class);
	
	public Boolean createProductIndex(String index,String type,String pathMapping) throws IOException{
		Boolean isSuccess = false;
		String mapping = "";
		//创建索引
		IndexBean indexBean = new IndexBean();
		indexBean.setIndexName(index);
		indexBean.setReplicas(1);
		indexBean.setShards(1);
		
		if(!Esutil.isExistsIndex(index)){
			Esutil.createIndex(indexBean);
		}
		mapping = JsonUtil
				.getJsonStrByFileName(ConstantsUtil.RESOURCES_PATH+pathMapping)
				.getJSONObject(index)
				.getJSONObject("mappings")
				.getString(type);
		isSuccess = Esutil.buildIndexMapping(index, type, mapping );
		if(isSuccess){
			logger.info("创建索引，类型成功mapping:"+mapping);
		}
		return isSuccess;
	}
	
	
}
