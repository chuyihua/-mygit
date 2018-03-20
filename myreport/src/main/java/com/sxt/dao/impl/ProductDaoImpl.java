package com.sxt.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sxt.common.base.BaseDao;
import com.sxt.dao.IProductDao;

@Repository("productDao")
public class ProductDaoImpl extends BaseDao implements IProductDao{

	public List<Map<String, Object>> getProductInfo() {
//		String sql="select * from (select rownum rn,a.* from("
//			+"  select t1.product_id as productID , t1.brand_id as brandID ,t1.name as name ,decode(t1.updown_flag,'D','上架','U','下架') as updownFlag,"
//			+"  to_char(t1.create_time ,'yyyy-mm-dd hh24:mi:ss') as createTime , nvl(t1.list_image,t1.min_image) as listImage , t1.boss_no as bossNo , decode(t1.protype_id,'1316502658758','终端','配件') as protypeID ,"
//			+"  decode(t1.spare2,'',t1.name,t1.spare2) as spare ,decode(t1.product_system,'1','体系内','2','体系外') as productSystem ,"
//			+"  t2.id as supplierdID , t2.full_name as fullName , t2.simple_name as simpleName , t2.group_id as groupID ,decode(t2.parentid,'1','主店铺','2','子店铺') as supplierGrade ,"
//			+"  decode(t2.parentid,'1','安徽移动',(select t2.full_name from t_supplier_info t3 where t3.id =t2.parentid)) as parentName , "
//			+"  getcityname(t2.city_code) as cityCode,r.region_name as districtName , t2.address as address , t2.phone as phone , t2.enter_time as entertime , decode(t2.state,'0','营业','待营业') as state"
//			+"  from t_product t1 left join t_supplier_info t2 on t1.supplierid = t2.id left join T_REGION_INFO r on t2.district_code = r.region_boss_no order by t1.product_id asc )a)";
		String sql="select * from t_product";
		List<Map<String, Object>> list = super.getJdbcTemplate().queryForList(sql);
		return list;
	}

}
