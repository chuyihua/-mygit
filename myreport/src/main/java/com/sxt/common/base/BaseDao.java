package com.sxt.common.base;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
@Repository("baseDao")
public class BaseDao extends JdbcDaoSupport{
	/**
	 * 注入JdbcTemplate
	 * 
	 * @Autowired/@Resource </br> 否则 JdbcTemplate及报错'dataSource' or
	 *                      'jdbcTemplate' is required
	 */
	@Resource(name = "myjdbcTemplate")
	public void setJb(JdbcTemplate jb) {
		super.setJdbcTemplate(jb);
	}

}
