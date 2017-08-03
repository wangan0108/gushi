package com.maven.web.controller;

import com.maven.web.util.HttpRequestSender;

public class JushiTaskController implements Runnable {
	
	public JushiTaskController(String url, String json, String uuid) {
		
		String result = "";
		
		for (int i = 0; i < 5; i++) {

			sends(url, json);

			if (result.equals("success")) {

				return;
				
			} else {
				
				try {
					
					System.out.println("url=" + url + "&json=" + json
							+ "&uuid=" + uuid);
					
					Thread.sleep(subtime(i));

				} catch (InterruptedException e) {

					e.printStackTrace();
					
				}

			}

		}
	}

	public String sends(String url, String json) {

		String codeMap = HttpRequestSender.doPost(url, json);

		return codeMap;

	}

	/**
	 * 
	 * @param sum
	 * @return 计算休眠时间
	 */
	@Override
	public void run() {
		
		System.out.println("run运行");
		
	}

	public int subtime(int sum) {
		
		int temp = 0;

		if (sum == 0) {
			
			temp = 60*1000;
			
		} else if (sum == 1) {
			
			temp = 300*1000;
			
		} else if (sum == 2) {
			
			temp = 600*1000;
			
		} else if (sum == 3) {
			
			temp = 3600*1000;
			
		} else if (sum == 4) {
			
			temp = 7200*1000;
			
		}
		return temp;
	}

}
