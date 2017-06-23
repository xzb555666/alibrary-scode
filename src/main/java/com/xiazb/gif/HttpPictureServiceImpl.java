package com.xiazb.gif;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.xiazb.gif.entity.GifEntity;

/**
 *
 */
public class HttpPictureServiceImpl {

	private GifPageParseService gifService;

	public static boolean isMocker = true;
	
    static final Logger LOGGER = LoggerFactory.getLogger(HttpPictureServiceImpl.class);

    
    private static final String sitePage = "http://www.gaoxiaogif.com/index_%s.html";
    
    private static final String storageDirectory = "E:/gif/gif-auto/";
    
    
    private static final ClientConfig jerseyClientConfig;
    private static final Client jerseyClient;

    public HttpPictureServiceImpl (){}
    
    public HttpPictureServiceImpl (GifPageParseService gifService){
    	this.gifService = gifService;
    }
    
    
    static {
        jerseyClientConfig = new DefaultClientConfig();
        jerseyClientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        jerseyClient = Client.create(jerseyClientConfig);
    }
	
    public byte[] getFile(String path) {
        //WebResource webResource = jerseyClient.resource(URL + "/" + path);
    	WebResource webResource = jerseyClient.resource(path);
        ClientResponse response = webResource.get(ClientResponse.class);
        final InputStream inputStream = response.getEntityInputStream();
        try {
        	//IOUtils.toCharArray(is)
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            return null;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
    
    public char[] getFileWithChar(String path) {
    	WebResource webResource = jerseyClient.resource(path);
        ClientResponse response = webResource.get(ClientResponse.class);
        final InputStream inputStream = response.getEntityInputStream();
        try {
        	return IOUtils.toCharArray(inputStream);
        } catch (IOException e) {
            return null;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }
    
   	public byte[] get(String path) {
   		return getFile(path);
   	}

    
   	public String write(String downloadPath, String storePath) {
    	OutputStream output = null;
    	/*try {
			String name = RandomStringUtils.randomAlphanumeric(32);
			String path = "E:/gif/gif-auto/" + name + ".gif";
			output = new FileOutputStream(path) ;
			
			IOUtils.write(getFileWithChar(downloadPath), output, Charset.forName("UTF-8"));
			System.out.println(path);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(output);
		}*/
    	String path = null;
    	try {
    		if (StringUtils.isEmpty(storePath)) {
    			String name = RandomStringUtils.randomAlphanumeric(16);
    			path = storageDirectory + name + ".gif";	
    		}else 
    			path = storePath;
    		
    		System.out.println(path);
			output = new FileOutputStream(path) ;
			
			IOUtils.write(getFile(downloadPath), output);
			
			return path;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			IOUtils.closeQuietly(output);
		}
    	
   	}
    
 	public String write(byte[] data, String storePath) {
		try {
			String name = RandomStringUtils.randomAlphanumeric(32);
			String path = storageDirectory + name + ".gif";
			System.out.println(path);
			IOUtils.write(data,  new FileOutputStream(path));
			return path;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
   	}
 	
 	
 	public String write(char[] data, String storePath) {
		try {
			String name = RandomStringUtils.randomAlphanumeric(32);
			String path = storageDirectory + name + ".gif";
			System.out.println(path);
			IOUtils.write(data, new FileOutputStream(path), Charset.forName("UTF-8"));
			return path;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
   	}

   	

	public void downloadGifOnePage (String downloadPath, String storageDir, boolean isRawFileName){
		List<GifEntity> list = gifService.parseHtmlContent(downloadPath);
		if (list == null || list.isEmpty()) {
			System.out.println("gifEntity is empty.");
			return;
		}


		if (StringUtils.isEmpty(storageDir)) {
			// storageDir = storageDirectory;
			String filename = FilenameUtils.getName(downloadPath);
			//index_300.html
			try {
				String number = StringUtils.substringBetween(filename, "_", ".");
				storageDir = storageDirectory + number + "/";
			}catch (Exception e){
				storageDir = storageDirectory;
				e.printStackTrace();
			}
		}
		
		File file = new File(storageDir);
		if (file.isDirectory() && file.exists()) {
			
		}else {
			try {
				FileUtils.forceMkdir(file);
			} catch (IOException e) {
				System.out.println("mkdir failed. path:" + storageDir);
				e.printStackTrace();
			}
		}
		
		String storePath = null;
		String srcName = null;

		for (GifEntity gif:list) {
			if (gif == null) continue;
			if (StringUtils.isEmpty(gif.getGifSrc()) && StringUtils.isEmpty(gif.getSrc())) continue;
			String gifsrc = null;
			try {
				gifsrc = gif.getGifSrc();
				if (StringUtils.isEmpty(gifsrc)) gifsrc = gif.getSrc();
				
				String filename = FilenameUtils.getName(gifsrc);
				if (isRawFileName) {
					srcName = gif.getAlt() + "." + FilenameUtils.getExtension(filename);
				}else {
					srcName = filename;
				}
				if (StringUtils.isEmpty(srcName)) srcName = filename;
				
				storePath = storageDir + srcName ;
				
				this.write(gifsrc, storePath);
			}catch (Exception e){
				System.out.println(gifsrc + ", storePath: " + storePath);
				e.printStackTrace();
			}
		}
	}
	
	

	public void downloadGifAllPage (int start, int end, boolean byPage, boolean isRawFileName){
		if (start > end || end <= 1) {
			throw new IllegalArgumentException("illegal argument exception.");
		}
		
		String storageDir = null;
		String page = null;
		int count = 0;
		for (int i = start; i<=end; i++){
			if (isMocker  && count > 9) break;
			count++;
			
			if (i == 1) page = FilenameUtils.getPath(sitePage);
			else page = String.format(sitePage, i); 
			
			// storePath = storageDir storagePath + srcName ;
			if (byPage)  storageDir =  storageDirectory + i + "/";
			downloadGifOnePage(page, storageDir, isRawFileName);
		}
	}
	
	
    
    
}


