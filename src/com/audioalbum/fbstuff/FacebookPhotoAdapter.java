package com.audioalbum.fbstuff;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FacebookPhotoAdapter extends BaseAdapter{
	
	private static final String debugTag = "TrackDataAdapter";
	private FacebookPhotoPickActivity mContext;
	private FacebookImageFetchAndCache imgFetcher;
	private ArrayList<FacebookData> datas;
	private GridView gv;
	
    public FacebookPhotoAdapter (FacebookPhotoPickActivity a, FacebookImageFetchAndCache i, ArrayList<FacebookData> data, GridView gView)
    {
    	this.mContext = a;
    	this.imgFetcher = i;
    	this.datas = data;
    	this.gv=gView;
    }
    
    @Override
    public int getCount() {
        return this.datas.size();
    }

    @Override
    public boolean areAllItemsEnabled () 
    {
    	return true;
    }
    
    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
    	ImageView imageView = new ImageView(mContext);
    	FacebookData theData=datas.get(pos);
    	String src=theData.getSrc();
    	imageView.setTag(src);
    	Drawable dr = imgFetcher.loadImage(this, imageView);
    	imageView.setImageDrawable(dr);
    	imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, (gv.getWidth()-16)/3));
        return imageView;
    }
    
    
}