package com.sxt.es;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;


public class myhbasetest {
	public static void main(String[] args) throws Exception {
		 Configuration conf = HBaseConfiguration.create();
	     conf.set("hbase.zookeeper.quorum", "192.168.85.134,192.168.85.135");
	     System.out.println("获得连接");
	     
	     // 1.创建表
        String tableName = "menber";
        String[] family = { "cf1", "cf2" };
//        creatTable(tableName, family,conf);
        
        // 2.为表添加数据
//        String[] column1 = { "cf1test1", "cf1test2", "cf1test3" };
//        String[] value1 = { "one", "two", "three" };
//        String[] column2 = { "cf2test1", "cf2test2" };
//        String[] value2 = { "lijie", "chongqing" };
//         addData("rowkey1", "t1", "cf1", column1, value1, "cf2", column2,
//         value2, conf);
//         addData("rowkey2", "t1", "cf1", column1, value1, "cf2", column2,
//         value2, conf);
//         addData("rowkey3", "t1", "cf1", column1, value1, "cf2", column2,
//         value2, conf);
        
        getResult("member", "scutshuxue", conf);
	}
	 /**
     * 创建表
     * 
     * @param tableName 表名字
     * @param family 列族
     * @param conf
     * @throws Exception
     */
    public static void creatTable(String tableName, String[] family,
            Configuration conf) throws Exception {
        HBaseAdmin admin = new HBaseAdmin(conf);
        HTableDescriptor desc = new HTableDescriptor(tableName);
        for (int i = 0; i < family.length; i++) {
            desc.addFamily(new HColumnDescriptor(family[i]));
        }
        admin.createTable(desc);
        System.out.println("创建成功!");
        /*if (admin.tableExists(tableName)) {
            System.out.println("表存在!");
        } else {
            admin.createTable(desc);
            System.out.println("创建成功!");
        }*/
    }
    /**
     * 插入数据
     * 
     * @param rowKey
     * @param tableName  表名字
     * @param cf1 列族1
     * @param column1 列族1的列限定符
     * @param value1 列族1的值
     * @param cf2 列族2
     * @param column2 列族2的列限定符
     * @param value2 列族2的值
     * @param conf
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void addData(String rowKey, String tableName, String cf1,
            String[] column1, String[] value1, String cf2, String[] column2,
            String[] value2, Configuration conf) throws IOException {
        Put put = new Put(Bytes.toBytes(rowKey));
        for (int i = 0; i < column1.length; i++) {
            put.add(Bytes.toBytes(cf1), Bytes.toBytes(column1[i]),
                    Bytes.toBytes(value1[i]));
        }

        for (int i = 0; i < column2.length; i++) {
            put.add(Bytes.toBytes(cf2), Bytes.toBytes(column2[i]),
                    Bytes.toBytes(value2[i]));
        }
        HTable table = new HTable(conf, tableName);
        table.put(put);
        System.out.println("数据插入成功！");
    }
    /**
     * 根据rowkey查询
     * 
     * @param tableName  表名字
     * @param rowKey
     * @param conf
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public static void getResult(String tableName, String rowKey,
            Configuration conf) throws IOException {
        Get get = new Get(Bytes.toBytes(rowKey));
        HTable table = new HTable(conf, tableName);
        Result result = table.get(get);
        List<KeyValue> list = result.list();
        for (KeyValue kv : list) {
            System.out.println("列族:" + Bytes.toString(kv.getFamily()));
            System.out.println("列族限定名:" + Bytes.toString(kv.getQualifier()));
            System.out.println("值:" + Bytes.toString(kv.getValue()));
            System.out.println("时间戳:" + kv.getTimestamp());
        }
    }
}
