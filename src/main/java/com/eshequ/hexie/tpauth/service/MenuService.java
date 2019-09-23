package com.eshequ.hexie.tpauth.service;

import java.util.List;

import com.eshequ.hexie.tpauth.vo.menu.MenuRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface MenuService {

	void addMenu(String appId, List<MenuRequest> menuList) throws JsonProcessingException;
}
