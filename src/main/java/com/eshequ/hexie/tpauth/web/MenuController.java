package com.eshequ.hexie.tpauth.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eshequ.hexie.tpauth.service.MenuService;
import com.eshequ.hexie.tpauth.vo.menu.MenuRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
public class MenuController {

	private Logger logger = LoggerFactory.getLogger(MenuController.class);
	
	@Autowired
	private MenuService menuService;
	
	@RequestMapping(value = "menu", method = RequestMethod.PUT)
	public String menu(@RequestParam(required = true) String appId, 
			@RequestBody(required=true) List<MenuRequest> menuList) throws JsonProcessingException {
		
		logger.info("appId is : " + appId + ", menuList is : " + menuList);
		menuService.addMenu(appId, menuList);
		return "success";
	}
	
}
