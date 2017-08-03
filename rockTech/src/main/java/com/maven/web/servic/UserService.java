package com.maven.web.servic;


public interface UserService {

//	Integer insert(UserInfo userInfo);
//
//	Integer delete(Long uid);
//
//	UserInfo select(Long uid);
//
	public String sends(String url, String json);
//	/** 从缓存中获取 */
	void getUserInfo(Long uid);

}
