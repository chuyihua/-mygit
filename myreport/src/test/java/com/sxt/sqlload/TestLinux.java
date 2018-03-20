package com.sxt.sqlload;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * sqlload linux 下测试
 * @author CYH
 *
 */
public class TestLinux {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // ./szrpp_files/clPath
        //写控制文件.ctl
        
    	 String fileRoute = "/home/chu/SQLLOAD";//文件地址路径
         String fileName = "student.txt";//数据文件名
         String tableName = "sql_load";//表名
         String fieldName = "(name,age)";//要写入表的字段
         String ctlfileName = "student.ctl";//控制文件名
         
         stlFileWriter(fileRoute,fileName,tableName,fieldName,ctlfileName);
         //要执行的DOS命令
         String user = "scott";
         String psw = "scott";
         String Database = "//192.168.85.128:1521/orcl";  
//        IP要指向数据库服务器的地址
         
         String logfileName = "test_table.log";
         Executive(user,psw,Database,fileRoute,ctlfileName,logfileName);
    }
    
    /**
     * * 写控制文件.ctl
     * @param fileRoute 数据文件地址路径
     * @param fileName 数据文件名
     * @param tableName 表名
     * @param fieldName 要写入表的字段
     * @param ctlfileName 控制文件名
     */
    public static void stlFileWriter(String fileRoute,String fileName,String tableName,String fieldName,String ctlfileName)
    {
        FileWriter fw = null;
        String strctl =
        		"LOAD DATA"
        		+" INFILE  '"+fileRoute+"/"+fileName+"'"
        		+" append INTO TABLE scott.SQL_LOAD"
        		+" FIELDS TERMINATED BY \",\""
        		+"(NAME,AGE)";
         try {
            fw = new FileWriter(fileRoute+""+ctlfileName);
            fw.write(strctl);
         } 
         catch (IOException e) 
        {
               e.printStackTrace();
        }
        finally {
            try 
            {
                fw.flush();
                fw.close();
            } 
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
        }
    }
    
    /**
     * 调用系统DOS命令
     * @param user 
     * @param psw
     * @param Database
     * @param fileRoute 文件路径
     * @param ctlfileName 控制文件名
     * @param logfileName 日志文件名
     */
    public static void Executive(String user,String psw,String Database,String fileRoute,String ctlfileName,String logfileName)
    {
        InputStream ins = null;
        //要执行的DOS命令
        String dos="sqlldr "+user+"/"+psw+"@"+Database+" control="+fileRoute+""+ctlfileName+" log="+fileRoute+""+logfileName;
        //Linux环境下注释掉不需要CMD 直接执行DOS就可以
        //String[] cmd = new String[]
        //{ "cmd.exe", "/C", dos }; // Windows环境 命令
        try
        {
            Process process = Runtime.getRuntime().exec(dos);
            ins = process.getInputStream(); // 获取执行cmd命令后的信息
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
            String line = null;
            while ((line = reader.readLine()) != null)
            {
                String msg = new String(line.getBytes("ISO-8859-1"), "UTF-8");
                System.out.println(msg); // 输出
            }
            int exitValue = process.waitFor();
            if(exitValue==0)
            {
                System.out.println("返回值：" + exitValue+"\n数据导入成功");
                
            }else
            {
                System.out.println("返回值：" + exitValue+"\n数据导入失败");
                
            }
            process.getOutputStream().close(); // 关闭
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}