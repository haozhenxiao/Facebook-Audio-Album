package com.audioalbum.fragments;

import java.util.ArrayList;

import com.example.audioalbum.R;
import com.example.audioalbum.StatusAdapter;
import com.example.audioalbum.StatusObj;
import com.example.audioalbum.R.drawable;
import com.example.audioalbum.R.layout;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class NewsFragment extends ListFragment {
	
	private ListView statusListView;
	private LayoutInflater layoutInflator;
	
// Delete from here	**********************************************
	public Integer[] dogs={
			R.drawable.dog1,
			R.drawable.dog2,
			R.drawable.dog3,
			R.drawable.dog4,
			R.drawable.dog5
		};
		
		public String[] names={
			"Hao Zhenxiao",
			"Ma Shengnan",
			"Zhenxiao Hao",
			"Shengnan Ma",
			"Fred"
		};
		
		public Integer[] profiles={
				R.drawable.profile,
				R.drawable.profile,
				R.drawable.profile,
				R.drawable.profile,
				R.drawable.profile
		};
		
		public String[] times={
			"10 hours",
			"1 week",
			"3 days",
			"1 month",
			"2 weeks"
		};
		
		private ArrayList<StatusObj> objs = new ArrayList<StatusObj>();
//until here  *************************************************	
		
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	 
	        View rootView = inflater.inflate(R.layout.news_fragment, container, false);
	        statusListView = (ListView)rootView.findViewById(android.R.id.list);
	        this.layoutInflator = LayoutInflater.from(getActivity());
	         
	        return rootView;
	    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		for(int i = 0; i <5; i++){
			objs.add(new StatusObj(names[i], times[i], profiles[i], dogs[i]));
		}
		statusListView.setAdapter(new StatusAdapter(getActivity(), layoutInflator, objs));
	}
	
	public static class MyViewHolder {
        public TextView name, time;
        public Button trackButton;
        public ImageView image;
        public ImageView profile;
    }

}
