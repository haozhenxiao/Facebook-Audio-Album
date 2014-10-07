package com.audioalbum.fragments;

import java.util.Arrays;
import java.util.List;

import com.audioalbum.fbstuff.FacebookPhotoPickActivity;
import com.audioalbum.photo.ChoosePhotoActivity;
import com.audioalbum.photo.TakePhotoActivity;
import com.example.audioalbum.R;
import com.example.audioalbum.R.drawable;
import com.example.audioalbum.R.id;
import com.example.audioalbum.R.layout;
import com.example.audioalbum.R.menu;
import com.facebook.Session;
import com.facebook.SessionDefaultAudience;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

@SuppressLint("NewApi")
public class AuthenticatedMainActivity extends FragmentActivity implements ActionBar.TabListener{
	private ActionBar actionBar;
	private ViewPager mViewPager;
	private AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());
		actionBar=getActionBar();
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.news_button).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.popular_button).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.friends_button).setTabListener(this));
        actionBar.addTab(actionBar.newTab().setIcon(R.drawable.me_button).setTabListener(this));     
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.action_menu, menu);
		return true;
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.action_camera:
			Session session = Session.getActiveSession();
			List<String> permissions = session.getPermissions();
			if (!permissions.contains("publish_actions")) {
			     Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(this, Arrays.asList("publish_actions"))
			            .setDefaultAudience(SessionDefaultAudience.FRIENDS);
			     session.requestNewPublishPermissions(newPermissionsRequest);
			}
			
			showPhotoOptionDialog();
			return true;
		case R.id.action_notice:
			System.out.println("NOTICE IS ABLE TO BE USED??????????????????????????????????");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {	
	}
	
	private void showPhotoOptionDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		final String [] items = new String [] {"Take from camera", "Select from gallery","Facebook photos"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		builder.setTitle("Select Image");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if(item==0){
					startActivity(new Intent(AuthenticatedMainActivity.this, TakePhotoActivity.class));
				}else if(item==1){
					startActivity(new Intent(AuthenticatedMainActivity.this, ChoosePhotoActivity.class));
				}else{
					startActivity(new Intent(AuthenticatedMainActivity.this, FacebookPhotoPickActivity.class));
				}
				
			}
		});
		final AlertDialog dialog = builder.create();
		dialog.show();
	}

}
