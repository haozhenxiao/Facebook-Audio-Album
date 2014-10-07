package com.audioalbum.photo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.example.audioalbum.R;
import com.example.audioalbum.R.id;
import com.example.audioalbum.R.layout;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint({ "InlinedApi", "NewApi" }) public class ChoosePhotoActivity extends Activity {
	private Uri mImageCaptureUri;
	private ImageView mImageView;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int PICK_FROM_FILE = 3;
	
	private Button upload;
	private File theImage=null;
	private ProgressDialog dialog = null;
	private ImageButton postButton;
	private ImageButton voicePreview;
	
	private String voiceFileName=null;
	private File voiceFile;
	private MediaPlayer mPlayer=null;
	private MediaRecorder mRecorder = null;
	private static final String LOG_TAG = "AudioRecordTest";
	
	private String uploadUrl="http://129.16.239.235/upup.php"; 
	private static String audioUp="http://129.16.239.235/UploadToServer.php";
    
    private int serverResponseCode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feed_photo);
		
        upload=(Button)findViewById(R.id.button1);        
        upload.setOnTouchListener(new View.OnTouchListener() {			
			@Override
			public boolean onTouch(View view, MotionEvent motion) {
				if(motion.getAction()==MotionEvent.ACTION_DOWN){
					startRecording();
					return true;
				}else if(motion.getAction()==MotionEvent.ACTION_UP){
					stopRecording();
					return true;
				}
				return false;
			}
		});
        
        postButton=(ImageButton)findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialog = ProgressDialog.show(ChoosePhotoActivity.this, "", "Uploading file...", true);
				
				/*new Thread(new Runnable() {
                    public void run() {                                          
                         try{
                        	 uploadFile(theImage);                         	 
							 postFile(voiceFile);						
						} catch (Exception e) {
							e.printStackTrace();
						}
                                                 
                    }
                  }).start(); */
				uploadToFB();
			
			}
		});
        
        voicePreview=(ImageButton)findViewById(R.id.voice_preview);
        voicePreview.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				if(mPlayer==null){
					startPlaying();
				}else{
					stopPlaying();
				}
				
			}
		});
        
        voiceFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        voiceFileName += "/"+String.valueOf(System.currentTimeMillis())+" audiorecordtest.3gp";  
        
		mImageView = (ImageView) findViewById(R.id.iv_photo);
		
		if(Build.VERSION.SDK_INT<19){
			Intent intent = new Intent();					
	        intent.setType("image/*");
	        intent.setAction(Intent.ACTION_GET_CONTENT);	        
	        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
		}else{
			Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
		    intent.addCategory(Intent.CATEGORY_OPENABLE);
		    intent.setType("image/*");
		    startActivityForResult(intent, PICK_FROM_FILE);
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;
		switch(requestCode){
		case PICK_FROM_FILE: 
	    	mImageCaptureUri = data.getData();
	    	
	    	
	    	
	    	//////////
	        //theImage = new File(mImageCaptureUri.getPath());
	    	String imagePath =getPath(this,mImageCaptureUri);
	    	if(imagePath==null){
	    		System.out.println("------------------------------------------------------------------");
	    	}else{
	    		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ "+imagePath);
	    	}
	    	theImage = new File(imagePath);
	        
	    	try {
				Bitmap bmp=BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageCaptureUri));
				mImageView.setImageBitmap(bmp);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
	    	
	    	//doCrop();   
	    	break;	
		case CROP_FROM_CAMERA:	    	
//	        Bundle extras = data.getExtras();
//	        if (extras != null) {	        	
//	            Bitmap photo = extras.getParcelable("data");	            
//	            mImageView.setImageBitmap(photo);
//	            String imagePath=getPath(mImageCaptureUri);
//		        theImage = new File(imagePath);
//	            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//	            photo.compress(CompressFormat.PNG, 0 , bos);
//	            byte[] bitmapdata = bos.toByteArray();
//	            FileOutputStream fos;
//				try {
//					fos = new FileOutputStream(theImage);
//					fos.write(bitmapdata);
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//	        }	
	        break;
	    	
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void uploadToFB(){	
		System.out.println("================================================== "+theImage.getAbsolutePath());
        Session session = Session.getActiveSession();
		Bundle bundle = new Bundle(); 
		bundle.putParcelable("source", getImageFormData(theImage));
		
		new Request(
		    session,
		    "/me/photos",
		    bundle,
		    HttpMethod.POST,
		    new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					dialog.dismiss();
					
				}
		    }
		).executeAsync();
		
	}
	
	public Bitmap getImageFormData(File image) {
	    return BitmapFactory.decodeFile(image.getPath());
	}
	
	private void doCrop() {
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>(); 	
    	Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        
        List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );      
        int size = list.size();       
        if (size == 0) {	        
        	Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();
        	
            return;
        } else {
        	intent.setData(mImageCaptureUri);
            
            intent.putExtra("outputX", 400);
            intent.putExtra("outputY", 400);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            
        	if (size == 1) {
        		Intent i = new Intent(intent);
	        	ResolveInfo res	= list.get(0);
	        	
	        	i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
	        	
	        	startActivityForResult(i, CROP_FROM_CAMERA);
        	} else {
		        for (ResolveInfo res : list) {
		        	final CropOption co = new CropOption();
		        	
		        	co.title = getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
		        	co.icon	= getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
		        	co.appIntent= new Intent(intent);
		        	
		        	co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
		        	
		            cropOptions.add(co);
		        }
	        
		        CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);
		        
		        AlertDialog.Builder builder = new AlertDialog.Builder(this);
		        builder.setTitle("Choose Crop App");
		        builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
		            public void onClick( DialogInterface dialog, int item ) {
		                startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
		            }
		        });
	        
		        builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
		            @Override
		            public void onCancel( DialogInterface dialog ) {
		               
		                if (mImageCaptureUri != null ) {
		                    getContentResolver().delete(mImageCaptureUri, null, null );
		                    mImageCaptureUri = null;
		                }
		            }
		        } );
		        
		        AlertDialog alert = builder.create();
		        
		        alert.show();
        	}
        }
	}
	
	private String getPath(Uri uri) {
		String res = null;
	    String[] proj = { MediaStore.Images.Media.DATA };
	    Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
	    if(cursor.moveToFirst()){
	       int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	       if(cursor.getString(column_index)==null){
	    	   System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
	       }
	       res = cursor.getString(column_index);
	    }
	    cursor.close();
	    return res;
     }
	
	
	
	
	private String getPath(final Context context, final Uri uri) {

	    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

	    // DocumentProvider
	    if (isKitKat) {
	        
	            final String docId = DocumentsContract.getDocumentId(uri);
	            final String[] split = docId.split(":");
	            final String type = split[0];

	            Uri contentUri = null;
	            if ("image".equals(type)) {
	                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	            } else if ("video".equals(type)) {
	                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
	            } else if ("audio".equals(type)) {
	                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	            }

	            final String selection = "_id=?";
	            final String[] selectionArgs = new String[] {
	                    split[1]
	            };

	            return getDataColumn(context, contentUri, selection, selectionArgs);

	    }
	    // MediaStore (and general)
	    else if ("content".equalsIgnoreCase(uri.getScheme())) {
	        return getDataColumn(context, uri, null, null);
	    }
	    // File
	    else if ("file".equalsIgnoreCase(uri.getScheme())) {
	        return uri.getPath();
	    }

	    return null;
	}
	
	private String getDataColumn(Context context, Uri uri, String selection,
	        String[] selectionArgs) {

	    Cursor cursor = null;
	    final String column = "_data";
	    final String[] projection = {
	            column
	    };

	    try {
	        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
	                null);
	        if (cursor != null && cursor.moveToFirst()) {
	            final int column_index = cursor.getColumnIndexOrThrow(column);
	            return cursor.getString(column_index);
	        }
	    } finally {
	        if (cursor != null)
	            cursor.close();
	    }
	    return null;
	}
	
	
	
	
	
	
	
	
		
	private int uploadFile(File sourceFile) { 	  
	  	  String fileName = sourceFile.getName();
	        HttpURLConnection conn = null;
	        DataOutputStream dos = null;  
	        String lineEnd = "\r\n";
	        String twoHyphens = "--";
	        String boundary = "*****";
	        int bytesRead, bytesAvailable, bufferSize;
	        byte[] buffer;
	        int maxBufferSize = 1 * 1024 * 1024 * 1024; 
		           try { 
		               FileInputStream fileInputStream = new FileInputStream(sourceFile);
		               URL url = new URL(uploadUrl);

		               conn = (HttpURLConnection) url.openConnection(); 
		               conn.setDoInput(true); 
		               conn.setDoOutput(true); 
		               conn.setUseCaches(false); 
		               conn.setRequestMethod("POST");
		               conn.setRequestProperty("Connection", "Keep-Alive");
		               conn.setRequestProperty("ENCTYPE", "multipart/form-data");
		               conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
		               conn.setRequestProperty("uploaded_file", fileName);                
		               dos = new DataOutputStream(conn.getOutputStream());     
		               dos.writeBytes(twoHyphens + boundary + lineEnd); 
		               dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
		            		                     + fileName + "\"" + lineEnd);	               
		               dos.writeBytes(lineEnd);
		               bytesAvailable = fileInputStream.available(); 	     
		               bufferSize = Math.min(bytesAvailable, maxBufferSize);
		               buffer = new byte[bufferSize];
		               bytesRead = fileInputStream.read(buffer, 0, bufferSize);  	                 
		               while (bytesRead > 0) {	            	   
		                 dos.write(buffer, 0, bufferSize);
		                 bytesAvailable = fileInputStream.available();
		                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
		                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);   	                 
		                }
		               dos.writeBytes(lineEnd);
		               dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
		               serverResponseCode = conn.getResponseCode();
		               String serverResponseMessage = conn.getResponseMessage();	                
		               Log.i("uploadFile", "HTTP Response is : " 
		            		   + serverResponseMessage + ": " + serverResponseCode);	               
		               if(serverResponseCode == 200){	            	   
		                   runOnUiThread(new Runnable() {
		                        public void run() {
		                            Toast.makeText(ChoosePhotoActivity.this, "File Upload Complete.", 
		                            		     Toast.LENGTH_SHORT).show();
		                        }
		                    });                
		               }    
		               fileInputStream.close();
		               dos.flush();
		               dos.close();
		                
		          } catch (MalformedURLException ex) {	        	  
		              dialog.dismiss();  
		              ex.printStackTrace();              
		              runOnUiThread(new Runnable() {
		                  public void run() {
		                      Toast.makeText(ChoosePhotoActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
		                  }
		              });
		              
		              Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
		          } catch (Exception e) {	        	  
		              dialog.dismiss();  
		              e.printStackTrace();              
		              runOnUiThread(new Runnable() {
		                  public void run() {
		                      Toast.makeText(ChoosePhotoActivity.this, "Got Exception : see logcat ", 
		                    		  Toast.LENGTH_SHORT).show();
		                  }
		              });
		              Log.e("Upload file to server Exception", "Exception : " 
		            		                           + e.getMessage(), e);  
		          }
		          dialog.dismiss();       
		          return serverResponseCode;  
	       } 
	 
	 private String postFile(File audio) throws Exception {
		    HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost(audioUp);
		    MultipartEntityBuilder builder = MultipartEntityBuilder.create();        
		    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		    FileBody fbImage = new FileBody(audio);

		    builder.addPart("audio", fbImage); 
		    final HttpEntity yourEntity = builder.build();

		    class ProgressiveEntity implements HttpEntity {
		        @Override
		        public void consumeContent() throws IOException {
		            yourEntity.consumeContent();                
		        }
		        @Override
		        public InputStream getContent() throws IOException,
		                IllegalStateException {
		            return yourEntity.getContent();
		        }
		        @Override
		        public Header getContentEncoding() {             
		            return yourEntity.getContentEncoding();
		        }
		        @Override
		        public long getContentLength() {
		            return yourEntity.getContentLength();
		        }
		        @Override
		        public Header getContentType() {
		            return yourEntity.getContentType();
		        }
		        @Override
		        public boolean isChunked() {             
		            return yourEntity.isChunked();
		        }
		        @Override
		        public boolean isRepeatable() {
		            return yourEntity.isRepeatable();
		        }
		        @Override
		        public boolean isStreaming() {             
		            return yourEntity.isStreaming();
		        } 

		        @Override
		        public void writeTo(OutputStream outstream) throws IOException {

		            class ProxyOutputStream extends FilterOutputStream {

		                public ProxyOutputStream(OutputStream proxy) {
		                    super(proxy);    
		                }
		                public void write(int idx) throws IOException {
		                    out.write(idx);
		                }
		                public void write(byte[] bts) throws IOException {
		                    out.write(bts);
		                }
		                public void write(byte[] bts, int st, int end) throws IOException {
		                    out.write(bts, st, end);
		                }
		                public void flush() throws IOException {
		                    out.flush();
		                }
		                public void close() throws IOException {
		                    out.close();
		                }
		            } 

		            class ProgressiveOutputStream extends ProxyOutputStream {
		                public ProgressiveOutputStream(OutputStream proxy) {
		                    super(proxy);
		                }
		                public void write(byte[] bts, int st, int end) throws IOException {
		                    out.write(bts, st, end);
		                }
		            }

		            yourEntity.writeTo(new ProgressiveOutputStream(outstream));
		        }

		    };
		    ProgressiveEntity myEntity = new ProgressiveEntity();
		    post.setEntity(myEntity);
		    HttpResponse response = client.execute(post); 
		    return getContent(response);
		} 

		public static String getContent(HttpResponse response) throws IOException {
		    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		    String body = "";
		    String content = "";
		    while ((body = rd.readLine()) != null) 
		    {
		        content += body + "\n";
		    }
		    return content.trim();
		}
			 
	 private void startRecording() {
	        mRecorder = new MediaRecorder();
	        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        mRecorder.setOutputFile(voiceFileName);
	        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	        voiceFile=new File(voiceFileName);
	        try {
	            mRecorder.prepare();
	        } catch (IOException e) {
	            Log.e(LOG_TAG, "prepare() failed");
	        }
	        mRecorder.start();
	    }
	    
	    private void stopRecording() {
	        mRecorder.stop();
	        mRecorder.release();
	        mRecorder = null;
	    }
	    
	    private void startPlaying() {
	        mPlayer = new MediaPlayer();
	        try {
	            mPlayer.setDataSource(voiceFileName);
	            mPlayer.prepare();
	            mPlayer.start();
	        } catch (IOException e) {
	            Log.e(LOG_TAG, "prepare() failed");
	        }
	    }
	    
	    private void stopPlaying() {
	        mPlayer.release();
	        mPlayer = null;
	    }
	    
}
