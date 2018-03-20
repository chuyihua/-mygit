package com.sxt.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sxt.dao.IProductDao;
import com.sxt.service.IProductService;

@Service("productService")
public class ProductServiceImpl implements IProductService {
	@Autowired
	private IProductDao productDao;
	public List<Map<String, Object>> getProductInfo() {
		return productDao.getProductInfo();
	}

}
