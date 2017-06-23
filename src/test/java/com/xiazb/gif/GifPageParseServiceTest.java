/**
 * 
 */
package com.xiazb.gif;

import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import com.xiazb.gif.entity.GifEntity;

/**
 * @Author xiazb
 * @CreateTime 2017年6月21日-下午2:46:18
 * @Copyright  Glodon
 */
public class GifPageParseServiceTest {

	
	private GifPageParseService gifService;
	
	private HttpPictureServiceImpl fileService;
	
	@Before
	public void init (){
		gifService = new GifPageParseService();
		fileService = new HttpPictureServiceImpl(gifService);
	}
	
	/**
	 * Test method for {@link com.xiazb.gif.GifPageParseService#parseHtmlContent()}.
	 */
	@Test
	public void testParseHtmlContent() {
		String page = "http://www.gaoxiaogif.com/index_88.html";
		page = "http://www.gaoxiaogif.com/index_988.html";
		
		List<GifEntity> list = gifService.parseHtmlContent(page);
		//System.out.println(list.toString());
		for (GifEntity s:list) {
			System.out.println(s);
		}
	}
	
	
	@Test
	public void testDownloadGif() {
		String downloadPath = "http://www.gaoxiaogif.com/index_88.html";
		downloadGifOnePage(downloadPath);
	}
	
	
	private void downloadGifOnePage (String downloadPath){
		List<GifEntity> list = gifService.parseHtmlContent(downloadPath);
		if (list == null || list.isEmpty()) {
			System.out.println("gifEntity is empty.");
			return;
		}
		
		String storePath = null;
		for (GifEntity gif:list) {
			if (gif == null) continue;
			if (StringUtils.isEmpty(gif.getGifSrc()) && StringUtils.isEmpty(gif.getSrc())) continue;
			String gifsrc = null;
			try {
				gifsrc = gif.getGifSrc();
				if (StringUtils.isEmpty(gifsrc)) gifsrc = gif.getSrc();
				
				String srcName = FilenameUtils.getName(gifsrc);
				storePath = "E:/gif/gif-auto/" + srcName ;
				//E:/gif/gif-auto/DTnArbJTxCbLaXxX.gif
				
				fileService.write(gifsrc, storePath);
			}catch (Exception e){
				System.out.println(gifsrc + ", storePath: " + storePath);
				e.printStackTrace();
			}
		}
	}
	
	
	
	@Test
	public void testDownloadGifOnePage() {
		boolean isRawFileName = true;
		fileService.downloadGifOnePage("http://www.gaoxiaogif.com/index_300.html", null, isRawFileName);
	}
	
	
	@Test
	public void testDownloadGifAllPage() {
		boolean isRawFileName = true;
		fileService.isMocker = false;
		fileService.downloadGifAllPage(100,120 , true, isRawFileName);
	}
	
	 
	
	@Test
	public void testExtractFilename() {
		String page = "http://www.gaoxiaogif.com/index_88.html";

		//FileUtils.
		//StringUtils.getCommonPrefix(new String[]{});
		
		System.out.println(FilenameUtils.getExtension(page));
		System.out.println("name:" + FilenameUtils.getName(page));
		System.out.println("path:" + FilenameUtils.getPath(page));
	}

}
