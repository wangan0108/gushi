package com.maven.web.servic.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maven.web.servic.UserService;
import com.maven.web.util.HttpRequestSender;
import com.maven.web.util.RedisService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	

	
	@Resource
	private RedisService redisService;
	
	public static final String USER_INFO = "uid";



	public void getUserInfo(Long uid) {
		
		if (redisService.exists(USER_INFO+uid)){
			System.out.println(redisService.get(USER_INFO+uid));
		} else {
			redisService.put(USER_INFO+uid, uid);
			System.out.println(redisService.get(USER_INFO+uid));
		}
		

	}
public String sends(String url, String json) {
		
		String codeMap = HttpRequestSender.doPost(url,json);
		
		return codeMap;
		
	}

}
