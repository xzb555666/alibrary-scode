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

/**
 * @Author xiazb
 * @CreateTime 2017年6月23日-下午3:54:21
 * @Copyright  Glodon
 */
public class HttpConnection {

	// 向服务器发送get请求
	public String doGet(String path) {
		try {
			URL url = new URL(path);
			HttpURLConnection uRLConnection = (HttpURLConnection) url.openConnection();
			InputStream is = uRLConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "GBK"));
			String response = "";
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				// response = br.readLine();
				response = response + readLine;
			}
			is.close();
			br.close();
			uRLConnection.disconnect();
			return response;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 向服务器发送post请求
	public String doPost(String path, String username, String password) {
		try {
			URL url = new URL(path);
			HttpURLConnection uRLConnection = (HttpURLConnection) url.openConnection();
			uRLConnection.setDoInput(true);
			uRLConnection.setDoOutput(true);
			uRLConnection.setRequestMethod("POST");
			uRLConnection.setUseCaches(false);
			uRLConnection.setInstanceFollowRedirects(false);
			uRLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			uRLConnection.connect();

			DataOutputStream out = new DataOutputStream(
					uRLConnection.getOutputStream());
			String content = "username=" + username + "&password="
					+ password;
			out.writeBytes(content);
			out.flush();
			out.close();

			InputStream is = uRLConnection.getInputStream();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(is));
			String response = "";
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				// response = br.readLine();
				response = response + readLine;
			}
			is.close();
			br.close();
			uRLConnection.disconnect();
			return response;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
