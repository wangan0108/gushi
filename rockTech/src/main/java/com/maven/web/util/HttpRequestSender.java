package com.maven.web.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.impl.conn.SchemeRegistryFactory;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;





/**
 * 发送http请求工具类
 * 
 * @Description:
 * @author SHI Shaofei
 * @date 2014-5-11 上午11:34:22
 * 
 */
public class HttpRequestSender {
	private static Logger logger = LoggerFactory
			.getLogger(HttpRequestSender.class);

	public static final String UTF8 = "UTF-8";
	public static final String GBK = "GBK";

	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	public static final String METHOD_PUT = "PUT";

	private static PoolingClientConnectionManager clientConnManager;
	private static HttpClient httpClient;

	static {
		clientConnManager = new PoolingClientConnectionManager(
				SchemeRegistryFactory.createDefault(), 60, TimeUnit.SECONDS);
		clientConnManager.setDefaultMaxPerRoute(20);
		clientConnManager.setMaxTotal(100);
		httpClient = new DefaultHttpClient(clientConnManager);
		HttpParams params = httpClient.getParams();
		HttpConnectionParams.setSoTimeout(params, 15 * 1000);// 设定Sokcet超时时间
		HttpConnectionParams.setConnectionTimeout(params, 15 * 1000);// 设定连接超时时间
	}

	/**
	 * 请求JSON 获取JSON
	 * 
	 * @param url
	 * @param headerMap
	 * @param jsonMap
	 * @return
	 */
	public static Map<String, Object> postJson(String url,
			Map<String, String> headerMap, Map<String, Object> jsonMap) {
		HttpEntity requestEntity = createJsonEntity(jsonMap);
		return postEntity(url, headerMap, requestEntity);
	}

	public static String get(String uri, String queryString) {
		HttpGet httpget = null;
		try {
			httpget = new HttpGet(uri); 
			httpget.setURI(new URI(httpget.getURI().toString() + "?" + queryString));
			logger.info("RequestHttpEntity request： {} ", uri + queryString);
			HttpResponse httpResponse = httpClient.execute(httpget);
			HttpEntity entity = httpResponse.getEntity();
			String jsonResult = EntityUtils.toString(entity, UTF8);
			logger.info("RequestHttpEntity Response Result: {} ", jsonResult);
			return jsonResult;
		} catch (Exception e) {
			logger.error("HTTP 请求异常", e);
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (httpget != null) {
				httpget.releaseConnection();
			}
		}
	}
	
	public static String get(String uri, String queryString, String encode) {
		HttpGet httpget = null;
		try {
			httpget = new HttpGet(uri); 
			httpget.setURI(new URI(httpget.getURI().toString() + "?" + queryString));
			logger.info("RequestHttpEntity request： {} ", uri + queryString);
			HttpResponse httpResponse = httpClient.execute(httpget);
			HttpEntity entity = httpResponse.getEntity();
			String jsonResult = EntityUtils.toString(entity, encode);
			logger.info("RequestHttpEntity Response Result: {} ", jsonResult);
			return jsonResult;
		} catch (Exception e) {
			logger.error("HTTP 请求异常", e);
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (httpget != null) {
				httpget.releaseConnection();
			}
		}
	}
	
	/**
	 * Post 表单 返回 Json
	 * 
	 * @param url
	 * @param headerMap
	 * @param formMap
	 * @return
	 */
	public static Map<String, Object> postForm(String url,
			Map<String, String> headerMap, Map<String, String> formMap) {
		HttpEntity requestEntity = createFormEntity(formMap);
		return postEntity(url, headerMap, requestEntity);
	}

	public static String postFormRetString(String url, Map<String, String> headerMap, Map<String, String> param) {
		HttpEntity requestEntity = createFormEntity(param);
		return postFromEntityRetString(url, headerMap, requestEntity);
	}
	
	/**
	 * Post xml 返回 xml
	 * 
	 * @param url
	 * @param headerMap
	 * @param xml
	 * @return
	 */
	public static String postXml(String url, Map<String, String> headerMap,
			String xml) {
		HttpEntity requestEntity = createXmlEntity(xml);
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(url);
			headerMap.put("Connection", "Keep-Alive");
			headerMap.put("Keep-Alive", "timeout=65,max=50");
			setHeader(httpPost, headerMap);
			httpPost.setEntity(requestEntity);
			logger.info("Url:{}, Request Header: {} ", url, headerMap);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String resultXml = EntityUtils.toString(entity, UTF8);
			logger.info("postXml Response Result: {} ", resultXml);
			return resultXml;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
	}

	/**
	 * Post HttpEntity 返回 Json
	 * 
	 * @param url
	 * @param headerMap
	 * @param formMap
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Map<String, Object> postEntity(String url,
			Map<String, String> headerMap, HttpEntity httpEntity) {
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(url);
			headerMap.put("Connection", "Keep-Alive");
			headerMap.put("Keep-Alive", "timeout=65,max=50");
			setHeader(httpPost, headerMap);
			httpPost.setEntity(httpEntity);
			logger.info("Url:{}, Request Header: {} ", url, headerMap);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String jsonResult = EntityUtils.toString(entity, UTF8);
			logger.info("RequestHttpEntity Response Result: {} ", jsonResult);
			return JsonWare.getJsonObjectAsMap(jsonResult);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
	}
	/**
     * post请求
     * @param url
     * @param json
     * @return
     */
    public static String doPost(String url,String json){
        
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        String result ="";
        try {
            StringEntity s = new StringEntity(json.toString());
            
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");//发送json数据需要设置contentType
            post.setEntity(s);
            logger.info("Url:{}, Request Header: {} ", url,s);
            HttpResponse res = httpclient.execute(post);
            if(res.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                 result = EntityUtils.toString(res.getEntity());// 返回json格式：
                 logger.info("RequestHttpEntity Response Result: {} ", result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }
    
	private static String postFromEntityRetString(String url,
			Map<String, String> headerMap, HttpEntity httpEntity) {
		HttpPost httpPost = null;
		try {
			httpPost = new HttpPost(url);
			headerMap.put("Connection", "Keep-Alive");
			headerMap.put("Keep-Alive", "timeout=65,max=50");
			setHeader(httpPost, headerMap);
			httpPost.setEntity(httpEntity);
			logger.info("Url:{}, Request Header: {} ", url, headerMap);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity entity = httpResponse.getEntity();
			String jsonResult = EntityUtils.toString(entity, UTF8);
			logger.info("RequestHttpEntity Response Result: {} ", jsonResult);
			return jsonResult;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
		}
	}
	
	private static HttpEntity createFormEntity(Map<String, String> formMap) {
		try {
			List<NameValuePair> entity = new ArrayList<NameValuePair>();
			for (Entry<String, String> entry : formMap.entrySet()) {
				BasicNameValuePair nameValuePair = new BasicNameValuePair(entry
						.getKey(), entry.getValue());
				logger.info("request param:" + entry.getKey() + "," + entry.getValue());
				entity.add(nameValuePair);
			}
			return new UrlEncodedFormEntity(entity, UTF8);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static HttpEntity createJsonEntity(Map<String, Object> jsonMap) {
		try {
			logger.info("request jsonMap:{}", jsonMap);
			String json = JsonWare.getJsonString(jsonMap);
			StringEntity entity = new StringEntity(json, UTF8);
			entity.setContentType("application/json;charset=utf-8");
			entity.setContentEncoding(UTF8);
			return entity;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static HttpEntity createXmlEntity(String xml) {
		try {
			logger.info("request content:{}", xml);
			StringEntity entity = new StringEntity(xml, UTF8);
			entity.setContentType("text/xml;charset=utf-8");
			entity.setContentEncoding(UTF8);
			return entity;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 设置Header
	 * 
	 * @param httpRequest
	 * @param headParas
	 */
	private static void setHeader(HttpRequestBase httpRequest,
			Map<String, String> headParas) {
		if (headParas != null) {
			Set<String> nameSet = headParas.keySet();
			for (String name : nameSet) {
				String value = headParas.get(name);
				httpRequest.setHeader(name, value);
			}
		}
	}
	
//	/**
//	 * 功能：将字符串的的xml解析成document
//	 * 
//	 * @param xml
//	 * @return
//	 */
//	private static Element parseXml(String xml) {
//		StringReader reader = new StringReader(xml);
//		InputSource source = new InputSource(reader);
//		SAXReader sax = new SAXReader();
//		try {
//			Document doc = sax.read(source);
//			Element root = doc.getRootElement();
//			return root;
//		} catch (DocumentException e) {
//			throw new RuntimeException(e.getMessage());
//		}
//	}

	public static void main(String[] args) throws Exception {
		String url = "http://115.28.17.87:8082/mmgame.aspx";
		String queryString = "imei=865701010019682&imsi=460025681215387&ctid=30000828154910&cpparam=1300000000000000&ip=202.11.11.11";
		System.out.println(get(url, queryString));
	}
}