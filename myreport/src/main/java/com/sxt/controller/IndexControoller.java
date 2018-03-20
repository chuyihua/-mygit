package com.sxt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sxt.service.IProductService;

@Controller
@RequestMapping("/")
public class IndexControoller {
	@Autowired
	private IProductService productService;
	
	@RequestMapping("/test.do")
	public String toIndexJsp(){
		System.out.println("--------------------");
		productService.getProductInfo();
		return "test";
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring*.xml");
		IProductService productService = (IProductService) context.getBean("productService");
		productService.getProductInfo();
	}
}
