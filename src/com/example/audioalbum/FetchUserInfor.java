package com.example.audioalbum;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;

public class FetchUserInfor {

	private String userId;
	private String userName;
	
	
	public void setUserInfor(){
		final Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
	        Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
				@Override
				public void onCompleted(GraphUser user, Response response) {
	                    if (user != null) {
	                    	System.out.println("RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR "+response.getError());
	                    	userId=user.getId();
	                    	userName=user.getName();
	                    }    					
				}
	              
	        }); 
	        Request.executeAndWait(request);	        
	    } 		
	}
	
	
	public Bitmap getUserProfileImage(){
		URL image_value=null;
		Bitmap profileBitmap=null;
		setUserInfor();
		try {
			image_value = new URL("http://graph.facebook.com/"+getUserId()+"/picture?style=small" );
			profileBitmap=BitmapFactory.decodeStream(image_value.openConnection().getInputStream());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return profileBitmap;
	}
	
	public String getUserId(){
		return this.userId;
	}
	
	public String getUserName(){
		return this.userName;
	}
	
	
	
}
