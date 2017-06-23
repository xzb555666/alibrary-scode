/**
 * 
 */
package com.xiazb.stock;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @Author xiazb
 * @CreateTime 2017年6月23日-下午4:01:00
 * @Copyright  Glodon
 */
public class JsoupTest {
	
 	private static final String userAgent = "Mozilla/5.0 (jsoup)";
    private static final int timeout = 5 * 1000;

    // args :
    /*
    https://www.baidu.com
	div[class="\"head_wrapper\""]
	
	https://www.baidu.com
	a[href]
     * */
    public static void main(String... args) throws IOException {
        Validate.isTrue(args.length == 1 || args.length == 2, "usage: java -cp jsoup.jar org.jsoup.examples.HtmlToPlainText url [selector]");
        final String url = args[0];
        final String selector = args.length == 2 ? args[1] : null;

        // fetch the specified URL and parse to a HTML DOM
        Document doc = Jsoup.connect(url).userAgent(userAgent).timeout(timeout).get();

        HtmlToPlainText formatter = new HtmlToPlainText();
     
        
        if (selector != null) {
            Elements elements = doc.select(selector); // get each element that matches the CSS selector
            for (Element element : elements) {
                String plainText = formatter.getPlainText(element); // format that element to plain text
                System.out.println(plainText);
            }
        } else { // format the whole doc
            String plainText = formatter.getPlainText(doc);
            System.out.println(plainText);
        }
    }

}
