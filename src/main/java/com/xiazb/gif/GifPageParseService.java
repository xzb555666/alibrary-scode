/**
 * 
 */
package com.xiazb.gif;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.xiazb.gif.entity.GifEntity;

/**
 * @Author xiazb
 * @CreateTime 2017年6月21日-上午10:40:19
 * @Copyright Glodon
 */
public class GifPageParseService {

	//private String page = "http://www.gaoxiaogif.com/index_88.html";
	
	public List<GifEntity> parseHtmlContent(String gifPath) {
		Document doc = null;
		List<GifEntity> list = null;
		try {
			doc = Jsoup.connect(gifPath).get();
			if (doc == null) return null;
			
			if (doc.hasText());
			
			//System.out.println(doc.html());
			//String title = doc.title();
			//System.out.println("title: " + title);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// class: content: listgif-box: {listgif-giftitle:listgif-giftu}
		Elements contents = doc.getElementsByClass("center");
		if (contents == null || contents.size() == 0) {
			System.out.println("content is null");
			return null;
		}

		list = new ArrayList<GifEntity>();
		for (Element e : contents) {
			// Elements boxs = e.getElementsByClass("listgif-box");
			// Element e = contents.get(0);
			Elements gifs = e.select("img[src$=.gif]");
			if (gifs == null || gifs.isEmpty()) {
				System.out.println("gifs is null or is empty.");
				break;
			}

			for (Element gif : gifs) {
				String src = gif.attr("src");
				String gifsrc = gif.attr("gifsrc");
				String alt = gif.attr("alt");
				
				GifEntity ge = new GifEntity(StringUtils.isNotEmpty(src) ? FilenameUtils.getName(src) : null
						,src
						,StringUtils.isNotEmpty(gifsrc) ? FilenameUtils.getName(gifsrc) : null
						,gifsrc, alt);
				
				list.add(ge);
			}
		}
		return list;

		/*
		 * Elements links = content.getElementsByTag("a"); for (Element link :
		 * links) { String linkHref = link.attr("href"); String linkText =
		 * link.text(); }
		 */
	}
}
