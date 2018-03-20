package com.sxt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sxt.service.IProductService;

@Controller
public class mytest {
	
	@Autowired
	private static IProductService productService;
	public static void main(String[] args) {
		productService.getProductInfo();
	}
}
