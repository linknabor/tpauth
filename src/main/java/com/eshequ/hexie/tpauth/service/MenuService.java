package com.eshequ.hexie.tpauth.service;

import java.util.List;

import com.eshequ.hexie.tpauth.vo.menu.MenuRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface MenuService {

	void addMenu(List<MenuRequest> menuList) throws JsonProcessingException;
}
