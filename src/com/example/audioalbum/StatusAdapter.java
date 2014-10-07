package com.example.audioalbum;

import java.util.ArrayList;

import com.audioalbum.fragments.NewsFragment.MyViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StatusAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater layoutInflater;
	private ArrayList<StatusObj> statusObjs;

	
	public StatusAdapter(Context c, LayoutInflater l, ArrayList<StatusObj> objs){
		context = c;
		layoutInflater = l;
		statusObjs = objs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return statusObjs.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int pos) {
		// TODO Auto-generated method stub
		return pos;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyViewHolder holder;
		
		convertView = layoutInflater.inflate (R.layout.status_item, parent, false);
		holder = new MyViewHolder();
		holder.name = (TextView)convertView.findViewById(R.id.profile_name);
		holder.time = (TextView)convertView.findViewById(R.id.time);
		holder.image = (ImageView)convertView.findViewById(R.id.post_image);
		holder.profile = (ImageView)convertView.findViewById(R.id.poster_profile);
		
		StatusObj status = statusObjs.get(position);
		holder.name.setText(status.getName());
		holder.time.setText(status.getTime());
		holder.image.setImageResource(status.getImage());
		holder.profile.setImageResource(status.getProfile());
		
		return convertView;
	}

}
