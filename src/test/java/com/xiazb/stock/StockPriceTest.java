/**
 * 
 */
package com.xiazb.stock;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * @Author xiazb
 * @CreateTime 2017年6月21日-下午6:16:39
 * @Copyright Glodon
 */
public class StockPriceTest {

	class CommonValues {
		public String User_Agent = "";
	}

	// Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
	/*
	 * Accept-Encoding:gzip, deflate, sdch Accept-Language:zh-CN,zh;q=0.8
	 * Cache-Control:max-age=0 Connection:keep-alive Host:hq.sinajs.cn
	 * Upgrade-Insecure-Requests:1 User-Agent:Mozilla/5.0 (Windows NT 6.1;
	 * Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110
	 * Safari/537.36
	 */

	@Test
	public void testGetBaiduPage() throws Exception {
		String page = "https://www.baidu.com/";
		String content = getPage(page);
		System.out.println(content);
	}
	
	@Test
	public void testGetStockPage() throws Exception {
		String page = "http://hq.sinajs.cn/list=sh601006";
		String content = getPage(page);
		System.out.println(content);
	}
	
	private String getPage(String page) throws Exception {
		HttpConnection c = new HttpConnection();
		String content = c.doGet(page);
		return content;
	}
	
	
	
	@Test
	public void testGetStockPrice() throws Exception {
		String page = "http://hq.sinajs.cn/list=sh601006";
		String content = getPage(page);
		System.out.println(content);
		
		if (StringUtils.isEmpty(content)) {
			System.out.println("page content is null.");
			return;
		}
		//content.indexOf("=");
		String data = StringUtils.substringBetween(content, "\"", "\"");
		System.out.println("data:" + data);
		String [] array = StringUtils.split(data, ",");
		System.out.println(array.length);
		
		if (array == null || array.length == 0){
			 System.out.println(" array is null or empty");
			 return ;
		}
		if (array.length > 3)System.out.println(array[3]);
	}
	
	

	@Test
	public void testGetStockPrice2() throws Exception {
		/*
		 * httpurlconnection.setRequestProperty("User-Agent",CommonValues.User_Agent
		 * );
		 * httpurlconnection.setRequestProperty("Accept",CommonValues.Accept);
		 * httpurlconnection
		 * .setRequestProperty("Accept-Charset",CommonValues.Accept_Charset);
		 * httpurlconnection
		 * .setRequestProperty("Accept-Language",CommonValues.Accept_Language);
		 * httpurlconnection
		 * .setRequestProperty("Connection",CommonValues.Connection);
		 * httpurlconnection
		 * .setRequestProperty("Keep-Alive",CommonValues.Keep_Alive);
		 * httpurlconnection.setConnectTimeout(CommonValues.ConnectionTimeOut);
		 * httpurlconnection.setReadTimeout(CommonValues.ReadTimeOut);
		 */

	}

}
