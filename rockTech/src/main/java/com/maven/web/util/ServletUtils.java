package com.maven.web.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServletUtils {
	static Logger logger = LoggerFactory.getLogger(ServletUtils.class);
	public static final String HTTP_ENCODING = "UTF-8";
	
	public static void response(HttpServletResponse response, String rspStr) throws IOException {
		PrintWriter out = response.getWriter();
		out.println(URLEncoder.encode(rspStr,HTTP_ENCODING));
		out.flush();
		logger.info("\n>>返回客户端业务处理信息>>>>>[" + rspStr + "]\n\n");
	}
	public static void responseNoE(HttpServletResponse response, String rspStr) throws IOException {
        PrintWriter out = response.getWriter();
        out.println(rspStr);
        out.flush();
        logger.info("\n>>返回客户端业务处理信息>>>>>[" + rspStr + "]\n\n");
    }
	
	public static String getContextRealPath(HttpServletRequest request){
		return request.getSession().getServletContext().getRealPath("/");
	}

//	public static void responseAsZip(HttpServletRequest request, HttpServletResponse response, String rspStr) throws IOException {
//		if (isGzipSupport(request)) {
//			responseAsGzip(request, response, rspStr);
//			logger.info("\n>>返回客户端业务处理信息>>>>>[" + rspStr + "]\n\n");
//			return;
//		} else if (isLzmaSupport(request)) {
//			responseAsLzma(request, response, rspStr);
//			logger.info("\n>>返回客户端业务处理信息>>>>>[" + rspStr + "]\n\n");
//			return;
//		} else {
//			response(response, rspStr);
//			logger.info("\n>>返回客户端业务处理信息>>>>>[" + rspStr + "]\n\n");
//		}
//	}

	public static void responseAsGzip(HttpServletRequest request, HttpServletResponse response, String rspStr) throws IOException {
		// rspStr=URLEncoder.encode(rspStr,HTTP_ENCODING);
		byte[] byteUncompress = rspStr.getBytes(HTTP_ENCODING);
		
		long start = System.currentTimeMillis();
		byte[] byteCompressed = GZipUtils.compress(byteUncompress);
 
		if (byteUncompress != null && byteCompressed.length < byteUncompress.length) {
			send2Client(response,byteCompressed,"gzip");
		} else {
			response(response, rspStr);
		}
	}

//	public static void responseAsLzma(HttpServletRequest request, HttpServletResponse response, String rspStr) throws IOException {
//		// rspStr=URLEncoder.encode(rspStr,HTTP_ENCODING);
//
//		byte[] byteUncompress = rspStr.getBytes(HTTP_ENCODING);
//
//		long start = System.currentTimeMillis();
//		byte[] byteCompressed = Lzma7zipUtils.compress(byteUncompress);
//	 
//		if (byteUncompress != null && byteCompressed.length < byteUncompress.length) {
//			send2Client(response,byteCompressed,"lzma");
//		} else {
//			response(response, rspStr);
//		}
//	}
//	
	public static void send2Client( HttpServletResponse response, byte[] byteCompressed,String contentEncoding) throws IOException{
		response.reset();
		response.setHeader("Content-Encoding",contentEncoding );
		response.setHeader("Content-Length", byteCompressed.length + "");

		OutputStream oStream = response.getOutputStream();
		oStream.write(byteCompressed);
		oStream.flush();
	}

	public static boolean isGzipSupport(HttpServletRequest req) {
		String headEncoding = req.getHeader("Accept-Encoding");
		if (headEncoding != null && (headEncoding.toLowerCase().indexOf("gzip") != -1)) {
			return true;
		}
		return false;
	}

	public static boolean isLzmaSupport(HttpServletRequest req) {
		String headEncoding = req.getHeader("Accept-Encoding");
		if (headEncoding != null && (headEncoding.toLowerCase().indexOf("lzma") != -1)) {
			return true;
		}
		return false;
	}
	

	public static Integer getIntValue(HttpServletRequest req,String key){
		try{
			return Integer.parseInt(req.getParameter(key).trim());
		}catch(Exception ex){
			return null;
		}
	}
	
	public static Map getRequestMap(HttpServletRequest request) {
		Map m=new HashMap();
		int i=0;
		Enumeration paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			if(i==0)m=new HashMap();
		    ++i;
			String name = (String) paramNames.nextElement();
			String[] values = request.getParameterValues(name);
			if(values==null){
				m.put(name, null);
			}else if(values.length==1){
				m.put(name, StringUtils.trim(values[0]));
			
			}else{
				m.put(name, values);
			}
		}

		return m;
	}
}
