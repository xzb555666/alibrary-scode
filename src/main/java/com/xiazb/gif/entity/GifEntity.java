/**
 * 
 */
package com.xiazb.gif.entity;

import java.io.Serializable;

/**
 * @Author xiazb
 * @CreateTime 2017年6月21日-下午3:15:23
 * @Copyright Glodon
 */
public class GifEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2727536984273560516L;
	
	private String srcName;
	private String gifSrcName;
	private String src;
	private String gifSrc;
	private String alt;

	public GifEntity() {
	}

	/**
	 * @param src
	 * @param gifSrc
	 * @param alt
	 * @param title
	 */
	public GifEntity(String srcName, String src, String gifSrcName, String gifSrc, String alt) {
		this.srcName = srcName;
		this.gifSrcName = gifSrcName;
		this.src = src;
		this.gifSrc = gifSrc;
		this.alt = alt;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getGifSrc() {
		return gifSrc;
	}

	public void setGifSrc(String gifSrc) {
		this.gifSrc = gifSrc;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getSrcName() {
		return srcName;
	}

	public void setSrcName(String srcName) {
		this.srcName = srcName;
	}

	public String getGifSrcName() {
		return gifSrcName;
	}

	public void setGifSrcName(String gifSrcName) {
		this.gifSrcName = gifSrcName;
	}

	@Override
	public String toString() {
		return "GifEntity [src=" + src + ", gifSrc=" + gifSrc + ", alt=" + alt + "]";
	}
}
