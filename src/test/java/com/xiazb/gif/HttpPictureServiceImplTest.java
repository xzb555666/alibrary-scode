/**
 * 
 */
package com.xiazb.gif;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * @Author xiazb
 * @CreateTime 2017年6月21日-上午10:53:14
 * @Copyright  Glodon
 */
public class HttpPictureServiceImplTest {
	
	private HttpPictureServiceImpl service;
	
	@Before
	public void init(){
		service = new HttpPictureServiceImpl();
	}

	/**
	 * Test method for {@link com.xiazb.gif.HttpPictureServiceImpl#store(byte[], java.lang.String)}.
	 */
	@Test
	public void testStore() {
	}

	/**
	 * Test method for {@link com.xiazb.gif.HttpPictureServiceImpl#get(java.lang.String)}.
	 */
	@Test
	public void testGet() {
		String url = "http://ww3.sinaimg.cn/mw690/b8b73ba1gw1fa5dhjiddvg2085057npd.gif";
		service.get(url);
		/*
		InputStream in = c.getEntityInputStream();
		logger.info(in.available() + "");
		FileOutputStream fos = new FileOutputStream(new File ("E:/captcha.jpg"));
		byte [] b = new byte[1024];
		int len = -1;
//		in.read(b, 0, len);
		while ((len = in.read(b)) != -1) {
			fos.write(b);	
		}
		fos.flush();
		in.close();
		fos.close();*/
	}
	
	@Test
	public void testWrite() { 
		String url = "http://ww1.sinaimg.cn/mw690/5eef6257gw1f9x6zt6br9g208w04ykjn.gif";
		//url = "http://ww3.sinaimg.cn/mw690/5eef6257gw1f9uwofoumqg209g076u0y.gif";
		String path = service.write(url, null);
		System.out.println(path);
	}
	
	
	@Test
	public void testWriteWithChar() { 
		String url = "http://ww1.sinaimg.cn/mw690/5eef6257gw1f9x6zt6br9g208w04ykjn.gif";
		//url = "http://image.beekka.com/blog/2014/bg2014092006.png";
		String path = service.write(service.get(url), null);
		System.out.println(path);
	}
	
	
	@Test
	public void testRanodm() {
		byte [] data = RandomUtils.nextBytes(10);
		for (byte b : data) {
			System.out.print(b + ",");
		}
		System.out.println("");
		String str = RandomStringUtils.random(4, 5, 10, true, true);
		//String str = RandomStringUtils.random(10);
		System.out.println(str);
	}
	
	

}
