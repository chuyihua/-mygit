package com.sxt.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileUtil {
	
	/**
	 * 
	 * @param filePath 文件路劲
	 * @param splitChar 分割符
	 * @return
	 */
	public static String[] getStrArrayFromFile(String filePath,String splitChar){
		String[] arr = null;
		String document="";
		File file = new File(filePath);
		try {
			FileReader freader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(freader);
			String str=null;
			while((str=bufferedReader.readLine())!=null){
				document +=str;
			}
			bufferedReader.close();
			freader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(document.length()!=0){
			if(splitChar==null){
				arr = new String[]{document};
			}else{
				arr = document.split(splitChar);
			}
		}
		return arr;
	}
	
	
}
