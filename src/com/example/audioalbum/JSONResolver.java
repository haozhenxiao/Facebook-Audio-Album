package com.example.audioalbum;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.audioalbum.fbstuff.FacebookData;
import com.audioalbum.fbstuff.FacebookPhotoPickActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class JSONResolver extends AsyncTask<String, Void, JSONObject>{

	private ProgressDialog progDialog;
	private Context context;
	private FacebookPhotoPickActivity activity;
	private static final String debugTag = "JSONRESOLVER";
	
	public JSONResolver(FacebookPhotoPickActivity activity){
		super();
		this.activity=activity;
		this.context = this.activity.getApplicationContext();
	}
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progDialog = ProgressDialog.show(this.activity, "Loading", "Loading photos...", true, false);
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		try {
        	Log.d(debugTag,"Background:" + Thread.currentThread().getName());
        	JSONObject result = JSONStringFetchHelper.downloadFromServer(params);
            return result;
        } catch (Exception e) {
            return null;
        }
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		
		ArrayList<FacebookData> facebookDatas=new ArrayList<FacebookData>();
		FacebookData data;
		
		progDialog.dismiss();
        if (result == null) {
            this.activity.alert ("Unable to find track data. Try again later.");
            return;
        }
        
        try {
			JSONArray array=result.getJSONArray("data");
			for(int i=0;i<array.length();i++){
				JSONObject object=array.getJSONObject(i);
				String src=object.getString("src");
				String src_big=object.getString("src_big");
				data=new FacebookData(src, src_big);
				facebookDatas.add(data);
			}
			this.activity.setData(facebookDatas);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
