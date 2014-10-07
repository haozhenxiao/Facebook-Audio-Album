package com.audioalbum.fbstuff;

import java.io.InputStream;

import com.example.audioalbum.R;
import com.example.audioalbum.R.id;
import com.example.audioalbum.R.layout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

public class FacebookPhotoPostActivity extends Activity {
	
	private ProgressDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_photo);
		
		final Intent intent=getIntent();		
		dialog = ProgressDialog.show(FacebookPhotoPostActivity.this, "", "Loading...", true);
		ImageView imageView=(ImageView)findViewById(R.id.iv_photo);
		String src=intent.getExtras().getString("src");
		new DownloadImageTask(imageView).execute(src);
	}
	
	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public DownloadImageTask(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	        dialog.dismiss();
	    }
	}
	

}
