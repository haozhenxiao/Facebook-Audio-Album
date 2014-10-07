package com.example.audioalbum;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;

import android.os.Bundle;
import android.text.StaticLayout;
import android.util.Log;

public class JSONStringFetchHelper {
	
	private static final int HTTP_STATUS_OK = 200;
	private static byte[] buff = new byte[1024];
	private static final String logTag = "LastFMHelper";
	private static String theUrl = "http://graph.facebook.com/";
	private static String userId="100001819193148";
	private static JSONObject result=null;
	private static String fqlQuery;
	
	
	public static class ApiException extends Exception {
		private static final long serialVersionUID = 1L;

		public ApiException (String msg)
		{
			super (msg);
		}

		public ApiException (String msg, Throwable thr)
		{
			super (msg, thr);
		}
	}

	public static synchronized JSONObject downloadFromServer(String... params) throws ApiException{
		/*Thread getUserInforThread=new Thread(new Runnable() {			
			@Override
			public void run() {
				FetchUserInfor fInfor=new FetchUserInfor();
				fInfor.setUserInfor();
				userId=fInfor.getUserId();
			}
		});
		try {
			getUserInforThread.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		getUserInforThread.start();*/
		
		/*String url=theUrl+userId+"?"+params[0];
		Log.d(logTag,"Fetching " + url);
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		
		try {
			HttpResponse response = client.execute(request);
			StatusLine status = response.getStatusLine();
			if (status.getStatusCode() != HTTP_STATUS_OK) {
				throw new ApiException("Invalid response from last.fm" + 
						status.toString());
			}

			HttpEntity entity = response.getEntity();
			InputStream ist = entity.getContent();
			ByteArrayOutputStream content = new ByteArrayOutputStream();
			int readCount = 0;
			while ((readCount = ist.read(buff)) != -1) {
				content.write(buff, 0, readCount);
			}
			result = new String (content.toByteArray());			

		} catch (Exception e) {
			throw new ApiException("Problem connecting to the server " + 
					e.getMessage(), e);
		}*/
		fqlQuery=params[0];
		Bundle param = new Bundle();
        param.putString("q", fqlQuery);
        Session session = Session.getActiveSession();
        Request request = new Request(session,
            "/fql",                         
            param,                         
            HttpMethod.GET,                 
            new Request.Callback(){         
                public void onCompleted(Response response) {                   
                    if(response.getError()==null){
                    	GraphObject gObject=response.getGraphObject();                   	
                    	result=gObject.getInnerJSONObject();
                    }
                }                  
        }); 
        Request.executeAndWait(request);

		return result;
		
	}

}
