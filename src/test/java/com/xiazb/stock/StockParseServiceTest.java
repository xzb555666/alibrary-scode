/**
 * 
 */
package com.xiazb.stock;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.junit.Test;

/**
 * @Author xiazb
 * @CreateTime 2017年6月21日-下午6:16:39
 * @Copyright  Glodon
 */
public class StockParseServiceTest {

	
	private StockParseService stockService = new StockParseService();
	
	
	/**
	 * Test method for {@link com.xiazb.stock.StockParseService#parseHtmlContent(java.lang.String)}.
	 */
	private String names = "广汇汽车(600297) 安琪酵母(600298) 安迪苏(600299) 维维股份(600300) ST南化(600301) 标准股份(600302) 曙光股份(600303) 恒顺醋业(600305) 商业城(600306) 酒钢宏兴(600307) 华泰股份(600308) 万华化学(600309) 桂东电力(600310) 荣华实业(600311) 平高电气(600312) 农发种业(600313) 上海家化(600315) 洪都航空(600316) 营口港(600317) 新力金融(600318) 亚星化学(600319) 振华重工(600320) 国栋建设(600321) 天房发展(600322) 瀚蓝环境(600323) 华发股份(600325) 西藏天路(600326) 大东方(600327) 兰太实业(600328) 中新药业(600329) 天通股份(600330) 宏达股份(600331) 白云山(600332) 长春燃气(600333) XD国机汽(600335) 澳柯玛(600336) 美克家居(600337) 西藏珠峰(600338) 中油工程(600339) 华夏幸福(600340) 航天动力(600343) 长江通信(600345) 恒力股份(600346) 阳泉煤业(600348) 富通昭和(600349) 山东高速(600350) 亚宝药业(600351) 浙江龙盛(600352) 旭光股份(600353) 敦煌种业(600354) 精伦电子(600355) 恒丰纸业(600356) 承德钒钛(600357) 国旅联合(600358) 新农开发(600359) 华微电子(600360) 华联综超(600361) 江西铜业(600362) 联创光电(600363) 通葡股份(600365) 宁波韵升(600366) 红星发展(600367) 五洲交通(600368) 西南证券(600369) 三房巷(600370) 万向德农(600371) 中航电子(600372) 中文传媒(600373) 华菱星马(600375) 首开股份(600376) 宁沪高速(600377) 天科股份(600378) 宝光股份(600379) 健康元(600380) 青海春天(600381) 广东明珠(600382) 金地集团(600383) 山东金泰(600385) 北巴传媒(600386) 海越股份(600387) 龙净环保(600388) 江山股份(600389) 五矿资本(600390) 航发科技(600391) 盛和资源(600392) 粤泰股份(600393) 盘江股份(600395) 金山股份(600396) 安源煤业(600397) 海澜之家(600398";
	
	@Test
	public void testParseHtmlContent() {
		String [] nameArr = names.split(" ");
		for (String name : nameArr){
			String stockCode = StringUtils.substringBetween(name, "(", ")");
			if (StringUtils.isEmpty(stockCode)) continue;
			
			// String page = "http://quotes.money.163.com/f10/fhpg_%s.html";
			 
			 stockService.parseHtmlContent(stockCode);
			
			 /*try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
		}
	}
	
	@Test
	public void testShowBonusDate() {
		String [] nameArr = names.split(" ");
		for (String name : nameArr){
			String stockCode = StringUtils.substringBetween(name, "(", ")");
			if (StringUtils.isEmpty(stockCode)) continue;
			
			stockService.showBonusDate(stockCode,false);
			 
		}
	}
	
	private String shFilepath = "F:/a股/stocks/shanghai/sh-stock.txt";
	private String szFilepath = "F:/a股/stocks/shenzhen/sz-stock.txt";
	private String nihuigouFilepath = "F:/a股/stocks/shenzhen/逆回购.txt";
	
	@Test
	public void testShowAllStockBonusDate() {
		boolean today = false;
		String date =  "2017-06-26";
		long time = System.currentTimeMillis();
		stockService.showAllStockBonusDate(shFilepath, date, today);
		System.out.println((System.currentTimeMillis() - time) + "ms");
		
		time = System.currentTimeMillis();
		stockService.showAllStockBonusDate(szFilepath, date , today);
		System.out.println((System.currentTimeMillis() - time) + "ms");
	}
	
	@Test
	public void testDouble() {
		double price = 8.50D;
		if (price > 3.00D ) {
			System.out.println("more than 3.");
		}
	}
	
	@Test
	public void testPersistAllInFile() {
		stockService.persistAllInFile("F:/a股/stocks/", "2017-06-23");
	}
	
	@Test
	public void testGetPrice() {
		String stockCode = "600606";
		String p = stockService.getPrice(stockCode);
		System.out.println("price:" + p);
	}
	
	@Test
	public void testGetPriceBatch() {
		String stockCode = "600606";
		stockService.getPrice(stockCode);
	}
	
	
	
	@Test
	public void testToday() {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date yestoday = new Date(System.currentTimeMillis() - (24*60*60*1000));
		System.out.println(format.format(yestoday));
		
	}
	
	
	

}
