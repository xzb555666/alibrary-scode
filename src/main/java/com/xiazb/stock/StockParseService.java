package com.xiazb.stock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.show.api.ShowApiRequest;

/**
 * @Author xiazb
 * @CreateTime 2017年6月21日-下午5:57:42
 * @Copyright  Glodon
 */
public class StockParseService {
	
	// private String page = "http://quotes.money.163.com/f10/fhpg_600036.html";
	private String page = "http://quotes.money.163.com/f10/fhpg_%s.html";
	private String stockClosed = "sotck_closed";
	

	public /*StockBonusEntity*/String parseHtmlContent(String stockCode) {
		Document doc = null;
		 
		try {
			doc = Jsoup.connect(String.format(page, stockCode)).get();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		if (doc == null) return null;
		
		
		// 获取当前的价格
		Elements details = doc.getElementsByClass("stock_detail");
		String price = null;
		if (details != null && !details.isEmpty()) {
			Element detail = details.get(0);
			Elements tbodys = detail.getElementsByTag("tbody");
			if (tbodys != null && !tbodys.isEmpty()) {
				Element tbody = tbodys.get(0);
				Element tr = tbody.child(0);
				Element td = tr.child(0);
				Element span = td.child(0);
				//boolean isClosed = stockClosed.equals(span.attr("class"));
				
				String stockFlag = span.text(); 
				if ("停牌".equals(stockFlag)) {// 停牌数据从 163上面获取
					map.put(stockCode, "[停牌!] " + map.get(stockCode));
				}else if ("已退市".equals(stockFlag)) {
					map.put(stockCode, "[已退市!] " + map.get(stockCode));
					System.out.println(map.get(stockCode) + "已退市");
					return null;
				}else if ("未上市".equals(stockFlag)) {
					map.put(stockCode, "[未上市!] " + map.get(stockCode));
					System.out.println(map.get(stockCode) + "未上市");
					return null;
				}
				
				// 价格数据从 新浪获取
				price = this.getPrice(stockCode);
				// FIXME 
				map.put(stockCode, map.get(stockCode) + price + "元");
			}
		}
	
		// 获取分红数据
		// class: table_bg001 border_box limit_sale
		Elements tables = doc.getElementsByClass("table_bg001 border_box limit_sale");
		if (tables == null || tables.size() == 0) {
			System.out.println("tables is null");
			return null;
		}

		//List<StockBonusEntity> list = new ArrayList<StockBonusEntity>();
		
		Element table = tables.get(0);
		
		Elements tbodys = table.getElementsByTag("tbody");
		if (tbodys == null || tbodys.size() == 0) {
			System.out.println("tbodys is null");
			return null;
		}
		
		Element tbody = tbodys.get(0);
		Element tr = tbody.child(0);
		if (tr == null) {
			System.out.println("tbodys is null");
			return null;
		}
		Elements tds = tr.getElementsByTag("td");
		
		if (tds == null || tds.size() == 0) {
			System.out.println("tds is null");
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(stockCode);
		sb.append("	");

		int i = 0;
		for (Element td : tds) {
			
			if (i > 1 && i< 7) { sb.append("(");}
			sb.append(td.text());
			if (i > 1 && i< 7) { sb.append(")");}
			
			sb.append("	");
			i++;
		}
		sb.append("\r\n");
		sb.append(" 价格 " + price);
		String s = sb.toString();
		//System.out.println(s);
		//System.out.println();
		return s;
	}
	
	
	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	Date yestoday = new Date(System.currentTimeMillis() - (24*60*60*1000));
	Date today = new Date();
	String yestodayStr = format.format(yestoday);
	String todayStr = format.format(today);

	/**
	 * 
	 * @param stockCode
	 * @param dateStr		dateStr和isToday都为空的时候，查询的就是 昨天(yestodayStr)之后的分红日
	 * @param isToday		为true的时候会覆盖第二个参数 
	 * @return
	 */
	public String showBonusDate (String stockCode, boolean isToday) {
		return showBonusDate(stockCode, isToday ? todayStr : null);
	}
	
	
	public String showBonusDate (String stockCode, String dateStr) {
		 
		String s = this.parseHtmlContent(stockCode);
		String[] data = StringUtils.substringsBetween(s, "(", ")");
		if (data == null || data.length == 0) {
			// System.out.println(map.get(stockCode) + " " + stockCode + " 未公布 " + s);
			return null;
		}
		String song=null, zhuan = null,pai = null;
		
		String bonusSignDate=null, bonusDate = null;
 
		if (data.length == 5) {
			song = data[0];
			zhuan = data[1];
			pai = data[2];
			bonusSignDate = data[3];
			bonusDate = data[4];
		}else {
			System.out.print(map.get(stockCode) + " " + stockCode + " 数据不全 " + s);
			for (String d : data){
				System.out.print("---->" + d + " ");
			}
			return null;
		}
		
		Date _bonusSignDate = null, _bonusDate = null;
		StringBuilder sb= null;
		try {
			if (StringUtils.isNotEmpty(bonusSignDate) && !bonusSignDate.equals("--")) _bonusSignDate = format.parse(bonusSignDate);
			if (StringUtils.isNotEmpty(bonusDate) && !bonusDate.equals("--")) _bonusDate = format.parse(bonusDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		sb = new StringBuilder();
		String preText = null;
		String content;
		if (_bonusSignDate == null && _bonusDate == null){
			preText = "[未公布!!]";
		}

		sb = new StringBuilder();
		
		if (dateStr != null) {
			//if(dateStr.equals(bonusSignDate)||dateStr.equals(bonusDate)){
			if(dateStr.equals(bonusSignDate)){
				if (preText != null) sb.append(preText);
				
				if (StringUtils.isNotEmpty(pai) && Double.valueOf(pai) >= 3.00D) 
					sb.append("[分红多!!]");
				
				sb.append(map.get(stockCode))
				.append(" ")
				.append(stockCode)
				.append(" [送]")
				.append(song)
				.append(" [转]")
				.append(zhuan)
				.append("[派](")
				.append(pai )
				.append(") [登记日]")
				.append(bonusSignDate)
				.append(" [分红日]")
				.append(bonusDate);
				content = sb.toString();
				System.out.println(content);
				return content;
			}
			return null;
		}
		
		Date _dateStr = null;
		try {
			_dateStr = format.parse(dateStr == null ? this.yestodayStr : dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		
		boolean afterSignDate = false, afterBonusDate = false;
		if (_bonusSignDate != null && _bonusSignDate.after(_dateStr)) afterSignDate = true; 
		if (_bonusDate != null && _bonusDate.after(_dateStr)) afterBonusDate = true;
		
		// 只有分红日 没有登记日
		if (afterBonusDate && _bonusSignDate == null) { 
			preText = "[只有分红日!]";
		}
		 
		if (afterSignDate || afterBonusDate){
			//System.out.println(map.get(stockCode) + " " + stockCode + " [送]" + song + " [转]" + zhuan + "[派](" + pai + ") [登记日]" + bonusSignDate + " [分红日]" + bonusDate);

			if (preText != null) sb.append(preText);
			
			sb.append(map.get(stockCode))
			.append(" ")
			.append(stockCode)
			.append(" [送]")
			.append(song)
			.append(" [转]")
			.append(zhuan)
			.append("[派](")
			.append(pai )
			.append(") [登记日]")
			.append(bonusSignDate)
			.append(" [分红日]")
			.append(bonusDate);

			content = sb.toString();
			System.out.println(content);
			return content;
		}else {
			return null;
		}
	}
	
	
	
	public  void showAllStockBonusDate(String filepath, String dateStr, Boolean isToday) {
		
		List<String> stockCodes = readFromFile(filepath);
		
		if (stockCodes == null || stockCodes.isEmpty()) return ;
		
		int size = stockCodes.size();
		System.out.println("dateStr: " + dateStr + ", todayStr:" + todayStr + ", size:" + size);
		
		String stockFullname = null;
		String stockCode = null;
		
		for (int i=0;i<size;i++){
			stockFullname = stockCodes.get(i);
			if (StringUtils.isEmpty(stockFullname)) continue;
			
			stockCode = StringUtils.substringBetween(stockFullname, "(", ")");
			if (StringUtils.isEmpty(stockCode)) continue;
			
			map.put(stockCode, stockFullname);
			
			if (isToday) this.showBonusDate(stockCode, isToday);
			else this.showBonusDate(stockCode, dateStr);
			 
			stockFullname = null;
			stockCode = null;
		}
	}


	public void persistAllInFile(String directoryPath, String dateStr){
			List<String> stockCodes = readFromFile(null);
			int size = stockCodes.size();
			String stockFullname = null;
			String stockCode = null;
			List<String> list = new ArrayList<String>();
			
			for (int i=0;i<size;i++){
				stockFullname = stockCodes.get(i);
				if (StringUtils.isEmpty(stockFullname)) continue;
				
				stockCode = StringUtils.substringBetween(stockFullname, "(", ")");
				if (StringUtils.isEmpty(stockCode)) continue;
				
				map.put(stockCode, stockFullname);
				
				String content = this.showBonusDate(stockCode, dateStr);
				
				if (StringUtils.isNotEmpty(content)) {
					list.add(content);
				}
				
				stockFullname = null;
				stockCode = null;
				
				
			}
			
			if (list.isEmpty()) return ;

			String path = null;
			if (directoryPath.endsWith("/") || directoryPath.endsWith("\\")) {
				path = directoryPath + dateStr + ".txt";
			}else {
				path = directoryPath + "/" + dateStr + ".txt";
			}
			
			OutputStream output = null;
			try {
				File file = new File(directoryPath);
				if (!file.isDirectory()){
					FileUtils.forceMkdir(file);
				}
				output = new FileOutputStream(new File(path));
				IOUtils.writeLines(list, "UTF-8", output);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(output);
			}
					
			
		}
		
		
	
	public List<String> readFromFile(String filepath){
		if (StringUtils.isEmpty(filepath)) {
			throw new NullPointerException("filepath is null");
		}
		FileInputStream input = null;
		try {
			input = new FileInputStream(new File(filepath));
			return IOUtils.readLines(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(input);
		} 
		return null;
	}
	
	
	private Map<String,String> map = new HashMap<String,String>();

 	private static final String userAgent = "Mozilla/5.0 (jsoup)";
    private static final int timeout = 5 * 1000;
    
	private String getPage(String page) throws Exception {
		HttpConnection c = new HttpConnection();
		String content = c.doGet(page);
		return content;
	}
	
	String pricePage =  "http://hq.sinajs.cn/list=%s"; //  "http://hq.sinajs.cn/list=sh601006";
	
	public String getStockData(String stockCode) throws Exception {
		String page = String.format(pricePage, stockCode.startsWith("6") ? "sh" + stockCode : "sz" + stockCode);
		String content = getPage(page);
		//System.out.println(content);
		
		if (StringUtils.isEmpty(content)) {
			System.out.println("page content is null.");
			return null;
		}
		
		//content.indexOf("=");
		String data = StringUtils.substringBetween(content, "\"", "\"");
		return data;
	}
	
	//doc = Jsoup.connect(forPricePage).header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8").get();
	//doc = Jsoup.connect(forPricePage).userAgent(userAgent).timeout(timeout).get();
	// Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8
	
	public String getPrice(String stockCode)  {
		String data = null;
		try {
			data =  this.getStockData(stockCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (isEmpty(data)) {
			//System.out.println(stockCode + " page data is null");
			return null;
		}
		
		String [] array = StringUtils.split(data, ",");

		// FIXME remove
		//int index = StringUtils.lastIndexOf(data, ",");
		//StringUtils.replace
		
		if (array == null || array.length == 0){
			 System.out.println("array is null or empty");
			 return null;
		}
		if (array.length > 3){
			String currentPrice = array[3];
			if ("0.000".equals(currentPrice)) {// 停牌
				return array[2];			// 取昨日数据
			}
			return currentPrice;
		}
		System.out.println("data:" + data);
		return null;
	}
	
	private boolean isEmpty (String s){
		return StringUtils.isEmpty(s);
	}
	
	/**
	 * @param stockCodes 用逗号分隔
	 * @return
	 * @throws Exception
	 * http://hq.sinajs.cn/list=sh601006,sh600606
	 */
	public Map<String,String> getPriceBatch(String stockCodes) throws Exception {
		if (isEmpty(stockCodes)) return null;
		
		Map<String,String> priceMap = new HashMap<String,String>();
		// singal
		if (!stockCodes.contains(",")) {
			String p = this.getPrice(stockCodes);
			priceMap.put(stockCodes, p);
			return priceMap;
		}
		String [] stockArray = StringUtils.split(stockCodes, ",");
		int size = stockArray.length;
		String stockCode = null;
		for (int i=0;i<size;i++){
			stockCode = stockArray[i];
			//if (stockCode.startsWith("6"))
		}
		return priceMap;
	}
	
	public Map<String,String> getPriceBatch(Collection<String> stockCodes) throws Exception {
		return null;
	}
	
	
	@Deprecated
	public void getPrice() throws Exception {
        // URL u=new URL("http://route.showapi.com/131-43?showapi_appid=myappid&name=&code=&pinyin=&showapi_sign=mysecret");
		String res=new ShowApiRequest("http://route.showapi.com/131-43","40882","dd8fe8df5c8b49f4b71f430f94a408ca")
        .addTextPara("name","万达")
        .addTextPara("code","")
        .addTextPara("pinyin","wd")
        .post();
		System.out.println(res);
/*
        InputStream in=u.openStream();
        ByteArrayOutputStream out=new ByteArrayOutputStream();
        try {
            byte buf[]=new byte[1024];
            int read = 0;
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
            }
        }  finally {
            if (in != null) {
                in.close();
            }
        }
        byte b[]=out.toByteArray( );
        System.out.println(new String(b,"utf-8"));*/
	}
	
}
