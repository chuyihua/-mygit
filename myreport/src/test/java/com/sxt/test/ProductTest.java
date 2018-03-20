package com.sxt.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sxt.common.util.ConstantsUtil;
import com.sxt.common.util.Esutil;
import com.sxt.common.util.FileUtil;
import com.sxt.common.util.JsonUtil;
import com.sxt.es.EsProductHelp;
import com.sxt.service.IProductService;


public class ProductTest {
	private static Logger logger = Logger.getLogger(ProductTest.class);	
	private static IProductService productService;
	static{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring*.xml");
		productService = (IProductService) context.getBean("productService");
	}
	public static void main(String[] args) throws IOException {
//		Esutil.deleteIndex("shop");
//		System.out.println("删除 index：shop");
		//创建索引 mapping
		EsProductHelp esProductHelp = new EsProductHelp();
		if(esProductHelp.createProductIndex("shop","product","es/product2mapping.json")){
			//获得sources
			String[] strArray = FileUtil.getStrArrayFromFile(ConstantsUtil.RESOURCES_PATH+"es/produc2info.json", "\\|");
			for(int i=0;i<strArray.length;i++){
				String jsonDocument = strArray[i];
				System.out.println(jsonDocument);
				Boolean isSuccess = Esutil.addIndexDocument("shop", "product", jsonDocument );//增加文档
				if(isSuccess){
					System.out.println("增加成功！");
				}else{
					System.out.println("添加失败！the jsonDocument is :"+jsonDocument);
					continue;
				}
			}
		}		
	}
	/**
	 *load产品信息，并保存到 producinfo.json
	 */
	private static void  loadProductInfo() throws IOException{
		List<Map<String, Object>> list = productService.getProductInfo();
		System.out.println("list.length:"+list.size());
		File file= new File(ConstantsUtil.RESOURCES_PATH+"es/producinfo.json");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for(Map<String, Object> map:list){
			map.remove("RN");
			 String content = JsonUtil.object2Json(map).toLowerCase();
			 bw.write(content+"|");
		}
		System.out.println("生产文件 END ！");
		bw.flush();
		bw.close();
	}
	/**
	 *load产品信息，并保存到 producinfo.json
	 */
	private static void  loadProduct2Info() throws IOException{
		List<Map<String, Object>> list = productService.getProductInfo();
		System.out.println("list.length:"+list.size());
		File file= new File(ConstantsUtil.RESOURCES_PATH+"es/produc2info.json");
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(Map<String, Object> map:list){
			String content ="{";
			Set<String> keySet = map.keySet();
			for(String key : keySet){
				content +="\""+key.toLowerCase()+"\":\""+map.get(key)+"\"";
			}
			content +="}";
//			 String content = JsonUtil.object2Json(map).toLowerCase();
			 bw.write(content+"|");
		}
		System.out.println("生产文件 END ！");
		bw.flush();
		bw.close();
	}
}
