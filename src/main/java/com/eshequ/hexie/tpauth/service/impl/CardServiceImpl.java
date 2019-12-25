package com.eshequ.hexie.tpauth.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eshequ.hexie.tpauth.mapper.normal.UserMapper;
import com.eshequ.hexie.tpauth.model.User;
import com.eshequ.hexie.tpauth.service.CardService;

@Service
public class CardServiceImpl implements CardService {
	
	@Autowired
	private UserMapper userMapper;
	
	@PostConstruct
	public void test() {
		
		User user = new User();
		user.setName("yiming");;
		List<User> list = userMapper.select(user);
		for (User user2 : list) {
			System.out.println(user2.getName() + user2.getOpenid());
		}
	}

	@Override
	public void create() {
		// TODO Auto-generated method stub

	}

}
