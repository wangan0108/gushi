package com.maven.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.maven.web.servic.UserService;
import com.maven.web.util.RedisService;

@Controller
@RequestMapping("")
public class RedisController {
	
	@Resource
	private UserService userService;
	
	@Resource
	private RedisService redisService;
	
	
	@RequestMapping("/sendjson")
	@ResponseBody
	public String toIndex(String url, String json, String uuid)
			throws TimeoutException, Exception {

		String result = userService.sends(url, json);
		
		Map<String,Object> map=new HashMap<String, Object>();
	     
	        
	       
		if (!result.equals("success")) {
			
			redisService.put(uuid, json+"!"+url);

			ExecutorService executorService = Executors.newCachedThreadPool();

			executorService.submit(new JushiTaskController(url,json,uuid));

			map.put("stase", "fail");
			
		}else{
			
			map.put("stase", "0000");
			
		}
		
		String jsonString = JSON.toJSONString(map);
		
        return jsonString;
            
	}
	

}
