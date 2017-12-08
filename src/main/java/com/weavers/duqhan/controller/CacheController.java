package com.weavers.duqhan.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weavers.duqhan.business.ProductService;
import com.weavers.duqhan.domain.Users;
import com.weavers.duqhan.dto.ProductBean;
import com.weavers.duqhan.dto.ProductBeans;
import com.weavers.duqhan.dto.ProductRequistBean;

@CrossOrigin
@RestController
@Configuration
@EnableScheduling
@RequestMapping("/buildController/**")
public class CacheController {
	public static String cacheProductList = null;
	public static Map<Long,List<ProductBean>> userProductCacheMap = new HashMap<Long,List<ProductBean>>();
	
	@Autowired
    ProductService productService;
	
	@Scheduled(fixedRate=900000)
	public void sheduledCache(){
		try {
			cacheProductList = new ObjectMapper().writeValueAsString(productService.getAllProducts(0, 300, new ProductRequistBean()));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "/build-product-cache", method = RequestMethod.POST)  
	public  void getCacheProductList(HttpServletResponse response, HttpServletRequest request) throws JsonProcessingException {
		//cacheProductList = productService.getAllProducts(0, 300, new ProductRequistBean()).getProducts();
		cacheProductList = new ObjectMapper().writeValueAsString(productService.getAllProducts(0, 300, new ProductRequistBean()));
		
	}
	
	public static boolean isProductBeanListAvailableForUser(Users u) {
		if (userProductCacheMap.get(u.getId()) == null ) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void buildProductBeanList(Users u) throws JsonParseException, JsonMappingException, IOException {
		ProductBeans productBeans = new ObjectMapper().readValue(cacheProductList, ProductBeans.class);
		List<ProductBean> listOfProduct = productBeans.getProducts();
		Collections.shuffle(listOfProduct);
		userProductCacheMap.put(u.getId(), listOfProduct);
	}
	
	
	public static List<ProductBean> getProductBeanList(Users u, int start , int limit) throws JsonParseException, JsonMappingException, IOException {
		List<ProductBean> listOfProduct = new ArrayList<ProductBean>();
		listOfProduct = userProductCacheMap.get(u.getId());
		if(listOfProduct.size()>(start + limit))
		return listOfProduct.subList(start, start + limit);
		else
			return listOfProduct.subList(start, listOfProduct.size());
			
	}
	
	public static void emptyUserCacheList(Users u){
		if(userProductCacheMap.containsKey(u.getId()))
		userProductCacheMap.remove(u.getId());
	}
	
}