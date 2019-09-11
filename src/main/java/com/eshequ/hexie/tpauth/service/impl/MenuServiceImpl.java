package com.eshequ.hexie.tpauth.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.eshequ.hexie.tpauth.common.WechatConfig;
import com.eshequ.hexie.tpauth.service.AuthService;
import com.eshequ.hexie.tpauth.service.MenuService;
import com.eshequ.hexie.tpauth.util.RestUtil;
import com.eshequ.hexie.tpauth.vo.WechatResponse;
import com.eshequ.hexie.tpauth.vo.auth.AuthorizerAccessToken;
import com.eshequ.hexie.tpauth.vo.menu.MenuRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class MenuServiceImpl implements MenuService {
	
	private Logger logger = LoggerFactory.getLogger(MenuServiceImpl.class);
	
	@Autowired
	private RestUtil restUti;
	
	@Autowired
	private AuthService authService;
	
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void addMenu(List<MenuRequest> menuList) throws JsonProcessingException {

		Assert.notEmpty(menuList, "新增菜单不能为空。");
	
		List<Map<String, String>> list = new ArrayList<>();
		for (MenuRequest menuRequest : menuList) {
			
			String menuText = menuRequest.getTitle();
			String[]tmpArr = menuText.split(",");
			String menuName = tmpArr[0];
			String menuUrl = tmpArr[1];
			Map<String, String> btn = new HashMap<String, String>();
			btn.put("name", menuName);
			btn.put("type", "view");
			btn.put("url", menuUrl);
			
			list.add(btn);
		}
		
		Map<String, List<Map<String, String>>> map = new HashMap<>();
		map.put("button", list);
		String json = objectMapper.writeValueAsString(map);
		logger.info(json);
		
		String appId = "wxa48ca61b68163483";	//TODO 先写死
		AuthorizerAccessToken authorizerAccessToken = authService.getAuthorizerAccessTokenFromCache(appId);
		String accessToken = authorizerAccessToken.getAuthorizerAccessToken();
		String reqUrl = WechatConfig.UPDATE_MENU_URL.replaceAll("ACCESS_TOKEN", accessToken);
		WechatResponse wechatResponse = restUti.postByJson(reqUrl, map, WechatResponse.class);
		logger.info("wechatResponse is : " + wechatResponse);
	}

}
