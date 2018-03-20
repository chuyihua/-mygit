package com.sxt.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sxt.common.util.ConstantsUtil;
import com.sxt.common.util.JsonUtil;
import com.sxt.service.IProductService;

public class UuidTest {
	private static IProductService productService;
	/*static{
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring*.xml");
		productService = (IProductService) context.getBean("productService");
	}*/
	public static void main(String[] args) {
		/*String[] strArray1 = FileUtil.getStrArrayFromFile(ConstantsUtil.RESOURCES_PATH+"es/uuidJSON.json", null);
		String[] strArray2 = FileUtil.getStrArrayFromFile(ConstantsUtil.RESOURCES_PATH+"es/uuidJSON2.json", null);
		
		String uuid1 = strArray1[0];
		String uuid2 = strArray2[0];*/
		
		try {
			JSONObject obj1 = JsonUtil.getJsonStrByFileName(ConstantsUtil.RESOURCES_PATH+"es/uuidJSON.json");
			JSONObject obj2 = JsonUtil.getJsonStrByFileName(ConstantsUtil.RESOURCES_PATH+"es/uuidJSON2.json");
			
			JSONArray arr1 = obj1.getJSONArray("uuids");
			JSONArray arr2 = obj2.getJSONArray("uuids");
			
			List<String> list = new ArrayList<String>();
			
			for(Object e:arr1){
				JSONObject o = (JSONObject) e;
				list.add(o.getString("key"));
			}
			List<String> list2 = new ArrayList<String>();
			for(Object e:arr2){
				JSONObject o = (JSONObject) e;
				list2.add(o.getString("key"));
//				if(!list.contains(o.getString("key"))){
//					System.out.println(o.getString("key")+"==="+o.getString("doc_count"));
//				}
			}
			list.removeAll(list2);
			int i=1;
			for(String s : list){  
				System.out.println(i+++"=="+s);
			}
			/*int i=1;
			for(String s : list2){
				Boolean flag = true;
				for(String ss : list){
					if(s.equals(ss)){
						flag = false;
						break;
					}
				}
				if(flag){
					System.out.println(i+"==="+s);
					i++;
				}
			}*/
			System.out.println("end");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
