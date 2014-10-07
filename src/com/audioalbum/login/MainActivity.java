package com.audioalbum.login;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.Session.OpenRequest;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity{
	
	private boolean isResumed = false;
	private UiLifecycleHelper uiHelper;
	private LoginFragment mainFragment;
	/*private Session.StatusCallback callback = 
	    new Session.StatusCallback() {
	    @Override
	    public void call(Session session, 
	            SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};*/
	

	/*@SuppressLint("NewApi")
	@Override
    protected void onCreate(final Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.audioalbum", 
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        
        
	}*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    if (savedInstanceState == null) {
	        // Add the fragment on initial activity setup
	        mainFragment = new LoginFragment();
	        getSupportFragmentManager()
	        .beginTransaction()
	        .add(android.R.id.content, mainFragment)
	        .commit();
	    } else {
	        // Or set the fragment from restored state info
	        mainFragment = (LoginFragment) getSupportFragmentManager()
	        .findFragmentById(android.R.id.content);
	    }
	    
	    //Get key hash
	    try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.audioalbum", 
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
        } catch (NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
	    
	}
	
	/*@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	    isResumed = true;
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	    isResumed = false;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}*/
	
	/*private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	   //if (isResumed) {
	    	FragmentManager manager = getSupportFragmentManager();
	        int backStackSize = manager.getBackStackEntryCount();
	        for (int i = 0; i < backStackSize; i++) {
	            manager.popBackStack();
	        }
	        if (state.isOpened()) {
	        	System.out.println("+++++++++++++++++++++++++++++++++++++++");
	        	startActivity(new Intent(this, LoggedMainActivity.class));
	        } else if (state.isClosed()) {
	        	System.out.println("----------------------------------------");
	        	showLoginFragment();
	        }
	        
	        if (session == null) {
	            System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
	        } else if (!session.isOpened()) {
	        	System.out.println("?!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    		final OpenRequest open = new OpenRequest(this);
	            open.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
	            open.setPermissions(Arrays.asList(new String[]{"publish_stream"}));
	            System.out.println("000000000000000000000000");
	            session.openForPublish(open);
	            System.out.println("111111111111111111111111");
	        }
	        
	   // }
	}*/
	
	/*@Override
	protected void onResumeFragments() {
	    super.onResumeFragments();
	    Session session = Session.getActiveSession();

	    if (session != null && session.isOpened()) {
	    	System.out.println("(((((((((((((((((((((((((((((((((((((((((((((");
	    	startActivity(new Intent(this, LoggedMainActivity.class));
	    } else {
	    	System.out.println("))))))))))))))))))))))))))))))))))))))))))))))");
	    	showLoginFragment();	    	
	    }
	}
	
	private void showLoginFragment(){
        LoginFragment login=new LoginFragment();
        	getSupportFragmentManager()
            .beginTransaction()
            .add(android.R.id.content, login)
            .commit();
	}*/
	
	
    
}
