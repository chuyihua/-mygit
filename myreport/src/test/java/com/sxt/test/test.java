package com.sxt.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sxt.common.util.StringUtils;


public class test {
	public static void main(String[] args) {
		String yyyy_mm_dd="2017-08-30";
		String essql = "select count(distinct uuid) as item from activity/nginx_v1 " +
				"where urlbody='/activity/deploy/ah/2016/flowzc/images/1.png' and time between '2017-08-30 00:00:00' and '2017-08-30 23:59:59'   limit 0";
		String essql2="select count(*) as item from activity/nginx_v1  where (urlbody='/activity/ff/order.do') and time between  '2017-08-30 00:00:00' and '2017-08-30 23:59:59'    limit 0";
		
		String esSql="select count(*) as item from %s/%s " +
				"where (urlbody='/activity/deploy/ah/2017/sfkdkj/index_tg.html' or urlbody = '/activity/deploy/ah/2017/sfkdkj/index_wx.html')" +
				" and time between #starttime and #endtime group by time limit 0";
		
		//将sql语句转换成ES结构化语句
		String[] ss = esSql.replaceAll(" =", "=").replaceAll("= ", "=").split(" ");//去除=两边的空格符，并将sql语句按" "分割
		List<String> list= new ArrayList<String>();
		Map<String, List<String>> terms = new HashMap<String, List<String>>();//最终条件
		List<String> termValues = null;
		for(String s:ss){
			if(s.contains("=")){//获得查询条件
				list.add(s.replace("(", "").replace(")", ""));
			}
		}
		for(String term:list){
			String[] aa=term.replaceAll("'", "").split("="); //条件按=分割
			if(terms.containsKey(aa[0])){
				terms.get(aa[0]).add(aa[1]);
			}else{
				termValues = new ArrayList<String>();
				termValues.add(aa[1]);
				terms.put(aa[0], termValues);
			}
		}
		
		//ES查询语句转换
		String query3 = "{\"_source\":[\"uuid\"#source],"
				+"\"query\" : {\"filtered\": {\"query\":{\"bool\":{"
					+"#must"
					+"#should"						
					+"}},"
					+"\"filter\":{\"range\": {\"time\": {\"from\": \""+yyyy_mm_dd+" 00:00:00\",\"to\": \""+yyyy_mm_dd+" 23:59:59\"}}}	"		
					+"}}}";
		//count(*)、count(distinct uuid)、group by
		String must="";
		String should="";
		Set<String> keySet = terms.keySet();
		Iterator<String> it = keySet.iterator();
		while(it.hasNext()){
			String key=it.next();
			termValues = terms.get(key);
			if(termValues.size()==1){//must  {\"must\":[{\"regexp\": {\"url\": \"#url\" }}#refer]
				if(StringUtils.isNotBlank(must)){
					must +=",{\"term\": {\""+key+"\": \""+termValues.get(0)+"\" }}";
				}else{
					must="\"must\":[{\"term\": {\""+key+"\": \""+termValues.get(0)+"\" }}";
				}
			}else{//should \"should\":[{\"bool\":{\"must_not\":{\"term\": {\"supplierid\":\"\"}}}},{\"match\": {\"requestBody\": \"supplierID\"}}],\"minimum_should_match\" : 1"
				for(String s:termValues){
					if(StringUtils.isNotBlank(should)){
						should +=",{\"term\": {\""+key+"\":\""+s+"\"}}";
					}else{
						should = "\"should\":[{\"term\": {\""+key+"\":\""+s+"\"}}";
					}
				}
			}
		}
		
		if(StringUtils.isNotBlank(must)&&StringUtils.isNotBlank(should)){
			must +="],";
			should +="],\"minimum_should_match\" : 1";
		}else if(StringUtils.isNotBlank(must)){
			must +="]";
		}else if(StringUtils.isNotBlank(should)){
			should +="],\"minimum_should_match\" : 1";
		}
		String source = getTheKeyOfGroupBy(esSql);
		if(StringUtils.isNotBlank(source)){
			source = ",\""+source+"\"";
		}else{
			source = "";
		}
		
		String query= query3.replace("#source", source).replace("#must", must).replace("#should", should);
		System.out.println(query);
//		if(es3.contains("urlbody")){
//			
//		}
//	
//		
//		
//		String sql = "{\"_source\":[\"uuid\"],\"query\" : {\"filtered\": " +
//				"{\"query\":{\"bool\":{\"must\":[{\"regexp\": {\"url\": \"/mpad/pad/num/act/numDetail.html.*\" }},{\"regexp\": {\"refer\":\"http://ah.10086.cn/mpad/pad/numCheck/index2.html.*supplierId=.*\"}}]," +
//				"\"should\":[{\"bool\":{\"must_not\":{\"term\": {\"supplierid\":\"\"}}}},{\"match\": {\"requestBody\": \"supplierID\"}}],\"minimum_should_match\" : 1}}," +
//				"\"filter\":{\"range\": {\"time\": {\"from\": \"2017-08-30 00:00:00\",\"to\": \"2017-08-30 23:59:59\"}}}	}}}";
//		
//		sql = sql.replaceAll(",\"should.*\"minimum_should_match\" \\: 1", "");
//		System.out.println(sql);
//		String refer="http://ah.10086.cn/mpad/pad/num/xh/xuanhao_index.html.*aa?123";
//		refer=refer.replaceAll("\\.\\*", "|").replaceAll("\\.", "\\\\\\\\.").replaceAll("\\|", "\\.\\*").replace("?", "\\\\?");
//		System.out.println(refer);
	}
	 private static String getTheKeyOfGroupBy(String esSql){
		   String key = "";
			String[] str1 = esSql.split(" ");
			for(int i=0;i<str1.length;i++){
				if(str1[i].equals("by")){
					key = str1[i+1];
				}
			}
			return key;
	   }
}
