package com.example.audioalbum;

public class StatusObj {
	
	private String name;
	private String time;
	private int profile;
	private int image;
	
	public StatusObj(String n, String t, int p,int im){
		this.name = n;
		this.time = t;
		this.profile = p;
		this.image = im;
	}

	public String getName() {
		return name;
	}

	public String getTime() {
		return time;
	}

	public int getProfile() {
		return profile;
	}

	public int getImage() {
		return image;
	}
	

}
