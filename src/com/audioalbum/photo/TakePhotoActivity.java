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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

import com.example.audioalbum.R;
import com.example.audioalbum.R.id;
import com.example.audioalbum.R.layout;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.Session.OpenRequest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class TakePhotoActivity extends Activity {
    private Uri mImageCaptureUri;
    private ImageView mImageView;
	
	private static final int PICK_FROM_CAMERA = 1;
	private static final int CROP_FROM_CAMERA = 2;
	
	private Button upload;
	private ImageButton voicePreview;
	private ImageButton postButton;
	private File theImage=null;
	private static ProgressDialog dialog = null;
	private int serverResponseCode = 0;

	private String voiceFileName=null;
	private File voiceFile;
	private MediaPlayer mPlayer=null;
	private MediaRecorder mRecorder = null;
	private static final String LOG_TAG = "AudioRecordTest";
	
	private final String IP = "192.168.1.105";
	
	private static String uploadUrl="http://192.168.1.105/upup.php"; 
	private static String audioUp="http://192.168.1.105/UploadToServer.php";

	@SuppressLint({ "ShowToast", "NewApi" })
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_photo);
        
        upload=(Button)findViewById(R.id.button1);
        voicePreview=(ImageButton)findViewById(R.id.voice_preview);
        postButton=(ImageButton)findViewById(R.id.post_button);
        postButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialog = ProgressDialog.show(TakePhotoActivity.this, "", "Uploading file...", true);

				//new PostToFB().execute(mImageCaptureUri);                        	 				
				
				//uploadToFB(mImageCaptureUri);
				
				/*new Thread(new Runnable() {
                    public void run() {                                          
                         try{
                        	 new PostToFB().execute(theImage.getPath());                        	 
							 postFile(voiceFile);						
						} catch (Exception e) {
							e.printStackTrace();
						}
                                                 
                    }
                  }).start(); */
				
			/*	new Thread(new Runnable() {
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
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);	
        if(intent.resolveActivity(getPackageManager())!=null){
        	try {
				theImage=createImageFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        	if(theImage!=null){
        		mImageCaptureUri=Uri.fromFile(theImage);
        		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        		mediaScanIntent.setData(mImageCaptureUri);
        		this.sendBroadcast(mediaScanIntent);
        		intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(theImage));
        		startActivityForResult(intent, PICK_FROM_CAMERA);
        	}
        }       
        
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;
		switch(requestCode){
		case PICK_FROM_CAMERA:
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			final Bitmap bitmap = BitmapFactory.decodeFile(mImageCaptureUri.getPath(),
					options);
			mImageView.setImageBitmap(bitmap);
	    	doCrop();	    	
	    	break;
		case CROP_FROM_CAMERA:	    	
	        Bundle extras = data.getExtras();
	        if (extras != null) {	        	
	            Bitmap photo = extras.getParcelable("data");	            
	            mImageView.setImageBitmap(photo);
	            ByteArrayOutputStream bos = new ByteArrayOutputStream();
	            photo.compress(CompressFormat.PNG, 0 , bos);
	            byte[] bitmapdata = bos.toByteArray();
	            FileOutputStream fos;
				try {
					fos = new FileOutputStream(theImage);
					fos.write(bitmapdata);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
   
	        }
	        theImage = new File(mImageCaptureUri.getPath());	        
	        break;
	    	
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
	private File createImageFile() throws IOException {
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String imageFileName = "JPEG_" + timeStamp + "_";
	    File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);
	    File image = File.createTempFile(
	        imageFileName, 
	        ".jpg",   
	        storageDir     
	    );
	    return image;
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
		        alert.setCanceledOnTouchOutside(false);
		        
		        alert.show();
        	}
        }
	}
	
	public String postFile(File audio) throws Exception {
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
		
	public int uploadFile(File sourceFile) { 	  
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
	                            Toast.makeText(TakePhotoActivity.this, "File Upload Complete.", 
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
	                      Toast.makeText(TakePhotoActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
	                  }
	              });
	              
	              Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
	          } catch (Exception e) {	        	  
	              dialog.dismiss();  
	              e.printStackTrace();              
	              runOnUiThread(new Runnable() {
	                  public void run() {
	                      Toast.makeText(TakePhotoActivity.this, "Got Exception : see logcat ", 
	                    		  Toast.LENGTH_SHORT).show();
	                  }
	              });
	              Log.e("Upload file to server Exception", "Exception : " 
	            		                           + e.getMessage(), e);  
	          }
	          //dialog.dismiss();  
	          //uploadToFB("http://"+IP+"/uploads/images/"+theImage.getName());
	          return serverResponseCode;  
       } 
	
	public void uploadToFB(){		
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
