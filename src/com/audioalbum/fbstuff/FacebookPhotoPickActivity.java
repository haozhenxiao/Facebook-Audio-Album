package com.audioalbum.fbstuff;

import java.util.ArrayList;

import com.example.audioalbum.JSONResolver;
import com.example.audioalbum.R;
import com.example.audioalbum.R.id;
import com.example.audioalbum.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

public class FacebookPhotoPickActivity extends Activity{

	private ArrayList<FacebookData> theDatas;
	private GridView gridView;
	private FacebookImageFetchAndCache fetchAndCache;
	private FacebookPhotoAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_pick_photo);
		gridView = (GridView) findViewById(R.id.grid_view);
		fetchAndCache=new FacebookImageFetchAndCache(this);

		JSONResolver resolver=new JSONResolver(FacebookPhotoPickActivity.this);
		resolver.execute("SELECT owner, pid, src, src_big FROM photo WHERE owner = me()");
		
		gridView.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				FacebookData data=theDatas.get(position);
				String srcString=data.getSrc_big();
				Intent intent=new Intent(getApplicationContext(),FacebookPhotoPostActivity.class);
				intent.putExtra("src", srcString);
				startActivity(intent);				
			}
		});
		
	}
	
	public void alert (String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
	
	public void setData(ArrayList<FacebookData> facebookDatas){
		this.theDatas = facebookDatas;
		this.adapter=new FacebookPhotoAdapter(this, fetchAndCache, theDatas, gridView);
		this.gridView.setAdapter(adapter);
	}
	
}
