package com.eshequ.hexie.tpauth.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员卡相关
 */
@RestController
@RequestMapping(value = "/card")
public class CardController {

	private static final Logger logger = LoggerFactory.getLogger(CardController.class);
	
	@RequestMapping(method = RequestMethod.PUT)
	private String create(@RequestParam(required = true) String code) {
		
		logger.info("start to create card !");
		
		
		return "success";
	}
}
