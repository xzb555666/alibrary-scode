package com.xiazb.gif;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest  {
	
	@Test
	public void testRandom(){
		System.out.println(RandomStringUtils.random(3) );
		System.out.println(RandomStringUtils.randomAlphanumeric(32));
	}

}