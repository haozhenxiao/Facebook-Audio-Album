package com.audioalbum.fbstuff;

import java.util.ArrayList;

public class FacebookData {
	
	private String src;
	private String src_big;
	
	public FacebookData(String src,String big){
		this.src=src;
		this.src_big=big;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src_webp) {
		this.src = src_webp;
	}

	public String getSrc_big() {
		return src_big;
	}

	public void setSrc_big(String src_big) {
		this.src_big = src_big;
	}

		

}
