/**
 * 
 */
package com.sxt.test;

import java.lang.reflect.InvocationTargetException;
import org.apache.log4j.Logger;

/**
 * hibernate解密加密工具类
 * @author wujiajun
 * mail:wujiajun@sinovatech.com
 * QQ:352181949
 * 2017年6月21日下午2:48:45
 */
public class EncryptUtil {
	private static final Logger logger = Logger.getLogger(EncryptUtil.class);
    
    /**
     * 加密（提供流程中字段加密处理），加密后的密文前后有会中括号（[]）包括
    * @param field
     * @param entity
     * @return 属性不支持加密返回null
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static String encryptForStr(String value){
        if(value != null){
            
            String encryptStr="";
			try {
				encryptStr = EncryptionUtil.byte2hex(EncryptionUtil.encode(value.toString().getBytes(EncryptionUtil.CHAR_CODE), EncryptionUtil.KEY.getBytes()));
				encryptStr = ("[" + encryptStr + "]");
			} catch (Exception e) {
				logger.info("value"+value.toString()+" encrypt is error"+e.getMessage());
			}
            
            return encryptStr;
        }
        
        return null;
    }

    
    /**
     * 解密(单个字符串用于set方法)
    * @param field
     * @param entity
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static String decryptForSet(String value){
        if(value != null){
            //对于有加密标记的值才会进行解密
           if(EncryptUtil.hasEncryptMark(value.toString())){
        	   String decryStr ="";
				try {
					String tag = value.toString().replaceAll("\\[", "");
					tag = tag.replaceAll("\\]", "");
					decryStr = new String(EncryptionUtil.decode(EncryptionUtil
							.hex2byte(tag), EncryptionUtil.KEY.getBytes()));
				} catch (Exception e) {
					logger.info("value"+value.toString()+" decryptForSet is error"+e.getMessage());
				}
               return decryStr;
            }else{
                return value;
            }
        }
        return null;
    }

    
   
    /**
     * 判断密文中是否有加密标记（使用中括号[]将加密后的字符串包括起来）
    * @param encryptStr
     * @return
     */
    public static boolean hasEncryptMark(String encryptStr) {
        if(encryptStr != null && encryptStr.length() > 2) {
            return (encryptStr.startsWith("[") && encryptStr.endsWith("]"));
        }
        
        return false;
    }

    public static void main(String[] args) throws Exception {
		String id = "4RpS/IxlDzE4f2Xbnc0xFA==";
		//id = encryptForStr(id);logger.info(id);
		id = decryptForSet(id);
		logger.info(id);
    }
}

