package com.maven.web.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.maven.web.entity.UserInfo;
import com.maven.web.servic.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private UserService userService;
	
	@ResponseBody
	@RequestMapping(value="/insert", method=RequestMethod.POST)
	public String insert(@RequestBody UserInfo userInfo){
		if(userInfo.getStatus()==null){
			userInfo.setStatus("0");
		}
//		Integer count = userService.insert(userInfo);
//		if(count>0){
//			return "保存用户信息成功";
//		}
		return "保存用户信息失败";
	}
	
	@ResponseBody
	@RequestMapping(value="/select", method=RequestMethod.GET)
	public String select(@RequestParam Long uid){
//		UserInfo userInfo = userService.select(uid);
//		if(userInfo!=null){
//			return "您要查找的用户名是:"+userInfo.getUserName();
//		}
		return "查找用户失败";
	}
	
	@ResponseBody
	@RequestMapping(value="/delete", method=RequestMethod.DELETE)
	public String delete(@RequestParam Long uid){
//		Integer count = userService.delete(uid);
//		if(count>0){
//			return "删除用户信息成功";
//		}
		return "删除用户信息失败";
	}

}
