package com.maven.web.util;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * 
* @ClassName: JsonWare
* @Description:
* json 处理类  
* @author BJB 
* @date Dec 29, 2011 11:47:43 AM
 */
public class JsonWare {
	static Logger logger = LoggerFactory.getLogger(JsonWare.class);

	private final static String RET_STATUS = "flag";
	private final static String RET_MSG = "msg";

	private UudevStatus rspCod = UudevStatus.OK;
	private String rspMsg = "OK";

	private static ObjectMapper objectMapper = new ObjectMapper();
	private Map m = new HashMap();

	public JsonWare(UudevStatus retStatus, String rspMsg) throws Exception {
		this.rspCod = retStatus;
		this.rspMsg = rspMsg;
	}

	public static String toJsonString(Object obj) throws IOException {
		JsonGenerator gen = null;
		StringWriter sw = null;
		String swstr = null;
		try {
			sw = new StringWriter();
			gen = new JsonFactory().createJsonGenerator(sw);
			objectMapper.writeValue(gen, obj);
			swstr = sw.toString();
		} finally {
			if (gen != null)
				gen.close();
			if (sw != null)
				sw.close();

		}
		return swstr;
	}

	public static String getRetJsonString(UudevStatus retStatus, String rspMsg, Object... obj) throws JsonGenerationException, JsonMappingException, IOException {
		int len = obj.length;
		
		if (len % 2 != 0) {
			throw new java.lang.IllegalArgumentException("输入参数错误！");
		}

		Map map = new HashMap();
		map.put(RET_STATUS, retStatus.value());
		map.put(RET_MSG, rspMsg);

		
		for (int i = 0; i < len; i++) {
			map.put(obj[i], obj[++i]);
		}

		return objectMapper.writeValueAsString(map);
	}
	
	/**
	 * @param obj
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 * 将MAP转JSON串
	 */
	public static String getJsonString( Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		return objectMapper.writeValueAsString(obj);
	}


	public static Map getJsonObjectAsMap(HttpServletRequest request) throws Exception {
		String reqJson = URLDecoder.decode(IOUtils.toString(request.getInputStream()), "UTF-8");

		return getJsonObjectAsMap(reqJson);
	}

	/**
	 * @param request
	 * @return
	 * @throws Exception
	 * 将JSON串转MAP
	 */
	public static Map getJsonObjectAsMap(String request) throws Exception {
		if (StringUtils.isBlank(request)) {
			request = "{}";
		}

		Map m = objectMapper.readValue(request, Map.class);
		return m;
	}

	public static <T>T getJsonObject(String request, Class<T> t) throws Exception {
		if (StringUtils.isBlank(request)) {
			request = "{}";
		}

		return objectMapper.readValue(request, t);
	}
	


	public static void main(String[] args) throws Exception {
 
	}

	public Object json2Bean(String json, Class cls) throws Exception {
		if (StringUtils.isBlank(json)) {
			json = "{}";
		}
		return objectMapper.readValue(json, cls);
	}

	public JsonWare put(String key, Object obj) {
		m.put(key, obj);
		return this;
	}

	public void toJsonAndSend(HttpServletResponse response) throws IOException {
		ServletUtils.response(response, toJson());
	}

	public String toJson() throws IOException {
		StringBuilder status = new StringBuilder(100);
		status.append("\"" + RET_STATUS + "\":");
		status.append("\"").append(rspCod.value()).append("\"").append(",");
		status.append("\"" + RET_MSG + "\":").append("\"").append(rspMsg).append("\"");

		JsonGenerator gen = null;
		StringWriter sw = null;
		try {
			sw = new StringWriter();
			gen = new JsonFactory().createJsonGenerator(sw);
			objectMapper.writeValue(gen, m);
			String swstr = sw.toString();

			StringBuilder back = new StringBuilder(status.length() + swstr + 2);

			if (m.size() == 0)
				return back.append("{").append(status).append("}").toString();
			else
				return back.append("{").append(status).append("").append(swstr.substring(1, swstr.length() - 1)).append("}").toString();
		} finally {
			if (gen != null)
				gen.close();
			if (sw != null)
				sw.close();
			status = null;
			clear();
		}

	}

	private void clear() {
		m.clear();
	}
	/**
     * 转码
     * @param s
     * @return String
     */
    public static String decode(String s ){
        try{
            return java.net.URLDecoder.decode(s, "UTF-8");
        }catch(Exception ex){
            ex.printStackTrace();
            return s;
        }
    }
    /**
     * 转码
     * @param s
     * @return String
     */
    public static String encode(String s ){
        try{
            return java.net.URLEncoder.encode(s, "UTF-8");
        }catch(Exception ex){
            ex.printStackTrace();
            return s;
        }
    }

    
    
}