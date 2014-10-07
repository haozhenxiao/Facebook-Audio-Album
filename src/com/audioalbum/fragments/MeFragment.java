package com.audioalbum.fragments;

import com.audioalbum.login.MainActivity;
import com.example.audioalbum.R;
import com.example.audioalbum.R.id;
import com.example.audioalbum.R.layout;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MeFragment extends Fragment {
	
	private UiLifecycleHelper uiHelper;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	 @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {	 
	        View rootView = inflater.inflate(R.layout.me_fragment, container, false);
	        LoginButton authButton = (LoginButton) rootView.findViewById(R.id.logout_button);
			authButton.setFragment(this);
	        return rootView;
	    }
	 
	 private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		    if (state.isClosed()) {
		        Intent i=new Intent(getActivity(),MainActivity.class);
		        startActivity(i);
		    }
		}
	 
	 @Override
	 public void onResume() {
	     super.onResume();
	     uiHelper.onResume();
	 }

	 @Override
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);
	     uiHelper.onActivityResult(requestCode, resultCode, data);
	 }

	 @Override
	 public void onPause() {
	     super.onPause();
	     uiHelper.onPause();
	 }

	 @Override
	 public void onDestroy() {
	     super.onDestroy();
	     uiHelper.onDestroy();
	 }

	 @Override
	 public void onSaveInstanceState(Bundle outState) {
	     super.onSaveInstanceState(outState);
	     uiHelper.onSaveInstanceState(outState);
	 }
	 

}
