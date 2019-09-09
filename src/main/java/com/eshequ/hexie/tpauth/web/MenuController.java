package com.eshequ.hexie.tpauth.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.eshequ.hexie.tpauth.vo.menu.MenuRequest;

@RestController
public class MenuController {

	private Logger logger = LoggerFactory.getLogger(MenuController.class);
	
	@RequestMapping(value = "menu", method = RequestMethod.PUT)
	public String menu(@RequestBody List<MenuRequest> menuList) {
		
		logger.info("menuList is : " + menuList);
		return "success";
	}
	
}
