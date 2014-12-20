package com.xinhua.xinhuashe.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 网络请求
 *
 * @author azuryleaves
 * @since 2013-12-13 下午4:38:17
 * @version 1.0
 *
 */
public class NetUtils {

	private static DefaultHttpClient defaultHttpClient = null;

	private static CookieStore cookieStore;
	
	public static String StatusCode = "StatusCode", Result = "Result";
	
	public static final int TimeOut = 500;

	/**
	 * Get请求网络(String)
	 * 
	 * @param urlpath
	 *            请求地址
	 * @param encoding
	 *            编码格式
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> doHttpGetSetCookie(String urlpath, String encoding) {
		HttpGet request = new HttpGet(urlpath);
		try {
			HttpParams httpParams = new BasicHttpParams();
			int ConnectionTimeout = 15000;
			int SocketTimeout = 15000;
			// -- 请求时间
			HttpConnectionParams.setConnectionTimeout(httpParams,
					ConnectionTimeout);
			// -- 读取时间
			HttpConnectionParams.setSoTimeout(httpParams, SocketTimeout);
			if (defaultHttpClient == null) {
				defaultHttpClient = new DefaultHttpClient(httpParams);
			}
			if (cookieStore != null) {
				defaultHttpClient.setCookieStore(cookieStore);
			}
			
			defaultHttpClient.setRedirectHandler(new ClientRedirectHandler());
			request.addHeader("android", "android");
			Map<String, Object> result = new HashMap<String, Object>();
			synchronized(defaultHttpClient){
				HttpResponse response = defaultHttpClient.execute(request);
				cookieStore = defaultHttpClient.getCookieStore();
				int statusCode = response.getStatusLine().getStatusCode();
				result.put(StatusCode, statusCode);
				System.out.println("---请求状态码---" + statusCode + "---");
				if (statusCode == HttpStatus.SC_OK) {
					String string = EntityUtils.toString(response.getEntity(),
							encoding);
					result.put(Result, string);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Map<String, Object> doHttpPostSetCookie(String uri, List<NameValuePair> params) {
		HttpPost hp = new HttpPost(uri);
		try {
			if (params != null) {
				hp.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			}
			if (defaultHttpClient == null) {
				defaultHttpClient = new DefaultHttpClient();
			}
			// 请求超时
			defaultHttpClient.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
			// 读取超时
			defaultHttpClient.getParams().setParameter(
					CoreConnectionPNames.SO_TIMEOUT, 15000);
			if (cookieStore != null) {
				defaultHttpClient.setCookieStore(cookieStore);
			}
			defaultHttpClient.setRedirectHandler(new ClientRedirectHandler());
			hp.addHeader("android", "android");
			Map<String, Object> result = new HashMap<String, Object>();
			synchronized(defaultHttpClient){
				HttpResponse response = defaultHttpClient.execute(hp);
				cookieStore = defaultHttpClient.getCookieStore();
				int statusCode = response.getStatusLine().getStatusCode();
				System.out.println("---请求状态码---" + statusCode + "---");
				result.put(StatusCode, statusCode);
				if (statusCode == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					String string = EntityUtils.toString(entity);
					result.put(Result, string);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}