package com.example.directoryshow1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

public class SMBshowFullMovie extends Activity implements View.OnTouchListener{
	
//	RelativeLayout layout1;
	ArrayList<String> al=new ArrayList<String>();
	Context context =this;
	private ActionBar actionBar;
	private int position = 0;
//	private ProgressDialog progressDialog;
	private MediaController mediaControls;
	SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor ;
	public static final String MyPREFERENCES = "MyPrefsGridSMBMOOVIE" ;  // my pref internal folder name
    public static final String LOCALPATHANDROIDFIXED = "SAMBALOCALDIRSHOW1";
    public static final String PLAYFILESONG = "PLAYFILESONG";

    // keys
   
    public static final String directory = "Directory";
    public static final String file_name = "File_name";
    public static final String initialized = "INITIALIZED";
    public static final String volume = "VOLUME";
    public static final String screen = "SCREEN";
    
//	static String Dir="";
	static String Fname="";
	int mWidthScreen,mHeightScreen;
	int mp4width,mp4height;	
	VideoView myVideoView;
	AudioManager audioManager;
    final float MAX_VOLUME=100.0f;
    int volumeMax,volumeNow;
	float startX=0,startY=0;
	float leftVolume=0.4f,rightVolume=0.4f;
	RelativeLayout rl1;
	int width,height,orientation;
	private boolean initDone=false;
	private String url,username,password;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		orientation = SMBshowFullMovie.this.getResources().getConfiguration().orientation;		
		Intent intent = getIntent();
	    url=intent.getStringExtra("url");
	    username=intent.getStringExtra("username");    
	    password=intent.getStringExtra("password");
	
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();  
///////////		Dir	= sharedpreferences.getString(directory, "");
		Fname	= sharedpreferences.getString(file_name, "");

		Display disp= getWindowManager().getDefaultDisplay();
		Point size = new Point();
		disp.getSize(size);
		mWidthScreen = size.x;
		mHeightScreen = size.y;
		        
        actionBar = getActionBar();
		actionBar.setIcon(R.drawable.viewimage);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(Fname);
		
		if (sharedpreferences.getString(initialized, "").equals("")){
			editor.putString(initialized, "00000").commit();
			editor.putString(volume, String.valueOf(10)).commit();
			editor.putString(screen, "full").commit();
			}
		
////////////////		
init();
		new MP4FileLoadTask().execute();
	}// end on create

	private void init() {
		
//		rl1 = (RelativeLayout) findViewById(R.id.rl1);
//		myVideoView = (VideoView) findViewById(R.id.videoViewRelative);
		if (sharedpreferences.getString(screen,"").equals("full")) {
			setContentView(R.layout.smbfullvideoplayview1);
			rl1 = (RelativeLayout) findViewById(R.id.rl1);
			myVideoView = (VideoView) findViewById(R.id.videoViewRelative);
			LayoutParams params=myVideoView.getLayoutParams();
            params.height=mHeightScreen;
            params.width=mWidthScreen;          
            myVideoView.setLayoutParams(params);
				}
		
		else  {
			setContentView(R.layout.smbfullvideoplayview);
			rl1 = (RelativeLayout) findViewById(R.id.rl1);
			myVideoView = (VideoView) findViewById(R.id.videoViewRelative);
		}		
		rl1.setOnTouchListener(this);		
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		volumeMax=(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		volumeNow = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);	
		mediaControls = new MediaController(SMBshowFullMovie.this);
		mediaControls.show(8000);
		mediaControls.setAnchorView(myVideoView);
		myVideoView.setMediaController(mediaControls);

		
	}// end of init

	private void initMedia(){
//		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
//		volumeMax=(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
//		volumeNow = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);	
//		mediaControls = new MediaController(showFullMovie.this);
//		mediaControls.show(8000);
//		mediaControls.setAnchorView(myVideoView);
//		myVideoView.setMediaController(mediaControls);
		
		try {
			File PathAndr = new File(Environment.getExternalStorageDirectory(), 
					LOCALPATHANDROIDFIXED);
			if(!PathAndr.exists()) {
				PathAndr.mkdirs();
				}			
			String[] s0=url.split("/{2}");  // get fr0m net		
			File file = new File(PathAndr, s0[s0.length-1]);		
//			localFname=s0[s0.length-1];
//			File source = new File(Path, AndroidFileName);//file is android file		
//			InputStream in = new FileInputStream(source);
//		    SmbFile source = new SmbFile("smb://username:password@a.b.c.d/sandbox/sambatosdcard.txt");
//		    File destination = new File(Environment.DIRECTORY_DOWNLOADS, "SambaCopy.txt");												
			myVideoView.setVideoPath(file.toString());
			myVideoView.setMinimumHeight(mHeightScreen);
			myVideoView.setMinimumWidth(mWidthScreen);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
			Toast.makeText(getApplicationContext(),
			"Error  "+e.getMessage()	, Toast.LENGTH_SHORT).show();
			finish();
		}

		myVideoView.requestFocus();
		myVideoView.setOnErrorListener(new OnErrorListener() {
			
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Toast.makeText(getApplicationContext(),
						"Can not play media!!! ", Toast.LENGTH_LONG).show();
				finish();
				return true;
			}
		});
		
		myVideoView.setOnPreparedListener(new OnPreparedListener() {
			// Close the progress bar and play the video
			public void onPrepared(MediaPlayer mp) {
			
				myVideoView.seekTo(position);
				if (position == 0) {			
					myVideoView.start();
				} else {
					myVideoView.pause();
				}
			}
		});

	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
		myVideoView.pause();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		position = savedInstanceState.getInt("Position");
		myVideoView.seekTo(position);
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.smbshowmovie1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		
				
		
		case R.id.full:
			myVideoView.stopPlayback();
			sharedpreferences.getString(screen,"");
			if (sharedpreferences.getString(screen,"").equals("full")){
				editor.putString(screen, "notfull").commit();
			}
			else {
				editor.putString(screen, "full").commit();

			}
				
			init();
			break;	
			
		case R.id.portrait:
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		//  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			break;
			
		case R.id.landscape:
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			break;		
			
		case R.id.exit:
				
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);			
			SMBshowFullMovie.this.finish();
			break;
			
		case R.id.show:
	//Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 
showDialogmsg();
			break;	
					
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	
	
	
	@Override
	public boolean onMenuOpened(int featureId, Menu menu)
	{
		if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
	        if(menu.getClass().getSimpleName().equals("MenuBuilder")){
	            try{
	                Method m = menu.getClass().getDeclaredMethod(
	                    "setOptionalIconsVisible", Boolean.TYPE);
	                m.setAccessible(true);
	                m.invoke(menu, true);
	            }
	            catch(NoSuchMethodException e){
	                Log.e("TAG", "onMenuOpened", e);
	            }
	            catch(Exception e){
	                throw new RuntimeException(e);
	            }
	        }
	    }
	    return super.onMenuOpened(featureId, menu);
	}
	 
	public void showDialogmsg(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);  
        //Uncomment the below code to Set the message and title from the strings.xml file  
        //builder.setMessage(R.string.dialog_message) .setTitle(R.string.dialog_title);  
          
        //Setting message manually and performing action on button click  
        
        builder.setMessage(getMetadataOfMP4())  
            .setCancelable(false)  
            
            .setNegativeButton("Done", new DialogInterface.OnClickListener() {  
                public void onClick(DialogInterface dialog, int id) {  
                //  Action for 'NO' Button  
                dialog.cancel();  
             }  
            });  
  
        //Creating dialog box  
        AlertDialog alert = builder.create();  
        //Setting the title manually  
        alert.setTitle("File description");  
        alert.show();  
		
		
		
		
	}	
	
	
	public String getMetadataOfMP4(){
//		File file777 = new File(Environment.getExternalStorageDirectory(), "/"+Dir+"/"+Fname);
//		File filestr=new File(Dir+File.separator+Fname);
		        
		    	int z;	 
		    	
		    	
		    	
		    	
		    	
	            MediaMetadataRetriever retriever1 = new MediaMetadataRetriever();
//	    		FileInputStream inputS=null;
//	    		try {
//	    			inputS = openFileInput(PLAYFILESONG);
//	    			try {
//	    				retriever1.setDataSource(inputS.getFD());
//	    			} catch (IllegalArgumentException | IOException e) {
//	    				
//	    				e.printStackTrace();
//	    			}
//	    		} catch (FileNotFoundException e1) {
//	    			
//	    			e1.printStackTrace();
//	    		}
	            
	            
	            //	    		File file1 = new File(Environment.getExternalStorageDirectory(), "/"+Dir+"/"+Fname);

//	            retriever1.setDataSource(filestr.toString());

//	            retriever1.setDataSource(filestr.toString());
	            
				File PathAndr = new File(Environment.getExternalStorageDirectory(), 
						LOCALPATHANDROIDFIXED);
				if(!PathAndr.exists()) {
					PathAndr.mkdirs();
					}			
				String[] s0=url.split("/{2}");  // get fr0m net		
				File file = new File(PathAndr, s0[s0.length-1]);	  
	            retriever1.setDataSource(file.toString());
		    	ArrayList<SMBMP4MetaData> almp4 =new ArrayList<SMBMP4MetaData>();
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_ALBUM;
		    		almp4.add(new SMBMP4MetaData("ALBUM", retriever1.extractMetadata(z)));
		    	}
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST;
		    		almp4.add(new SMBMP4MetaData("ALBUMARTIST", retriever1.extractMetadata(z)));
		    	}
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_ARTIST;
		    		almp4.add(new SMBMP4MetaData("ARTIST", retriever1.extractMetadata(z)));
		    	}
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_AUTHOR;
		    		almp4.add(new SMBMP4MetaData("AUTHOR", retriever1.extractMetadata(z)));
		    	}
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_BITRATE;
		    		almp4.add(new SMBMP4MetaData("BITRATE", retriever1.extractMetadata(z)));
		    	}
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER;
		    		almp4.add(new SMBMP4MetaData("CD_TRACK_NUMBER", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPILATION)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_COMPILATION;
		    		almp4.add(new SMBMP4MetaData("COMPILATION", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_COMPOSER;
		    		almp4.add(new SMBMP4MetaData("COMPOSER", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_DATE;
		    		almp4.add(new SMBMP4MetaData("DATE", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_DURATION;
		    		almp4.add(new SMBMP4MetaData("DURATION", retriever1.extractMetadata(z)));
		    	}
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_GENRE;
		    		almp4.add(new SMBMP4MetaData("GENRE", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO;
		    		almp4.add(new SMBMP4MetaData("HAS_AUDIO", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO;
		    		almp4.add(new SMBMP4MetaData("HAS_VIDEO", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_LOCATION;
		    		almp4.add(new SMBMP4MetaData("LOCATION", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_TITLE;
		    		almp4.add(new SMBMP4MetaData("TITLE", retriever1.extractMetadata(z)));
		    	}	
		    	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT;
		    		almp4.add(new SMBMP4MetaData("VIDEO_HEIGHT", retriever1.extractMetadata(z)));
		    	}	
		    	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH;
		    		almp4.add(new SMBMP4MetaData("VIDEO_WIDTH", retriever1.extractMetadata(z)));
		    	}	
		    	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_WRITER;
		    		almp4.add(new SMBMP4MetaData("WRITER", retriever1.extractMetadata(z)));
		    	}	
		    	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_YEAR;
		    		almp4.add(new SMBMP4MetaData("YEAR", retriever1.extractMetadata(z)));
		    	}	
		    	
		    	
		    	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION;
		    		almp4.add(new SMBMP4MetaData("VIDEO_ROTATION", retriever1.extractMetadata(z)));
		    	}	
		    	
		    	StringBuilder sb=new StringBuilder();
		    	for (int i=0;i<almp4.size();i++){
		    		sb.append(almp4.get(i).getKey()+":   "+almp4.get(i).getValue()+"\n");
		    		
		    	}
		        
		return sb.toString();
	}
	
	
	
	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
float yu=0;
	       switch(v.getId()){
           case R.id.rl1:  // layout1 id

               switch (event.getAction()) {
                   case MotionEvent.ACTION_DOWN:
           			startY = event.getY();
        			startX=event.getX();

                       break;
                   case MotionEvent.ACTION_MOVE:
 
                       break;
                   case MotionEvent.ACTION_UP:
				event.getX();
                        yu =  event.getY();
                        
if (Math.abs(startY-yu)>rl1.getHeight()/12){
		if (startY-yu >0 ){
		volumeNow++;
		if (volumeNow>volumeMax) volumeNow=volumeMax; 
						}
		if (startY-yu <0 ){
			volumeNow--;
			if (volumeNow<0) volumeNow=0; 
		}                       
//			Toast.makeText(getApplicationContext(), "Volume= "+(volumeNow)+" / "+volumeMax
//    		   , Toast.LENGTH_SHORT).show(); 
			audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volumeNow, 0);
			editor.putString(volume, String.valueOf(volumeNow)).apply();    
			}                   
                       break;  //switch events
               }
               break;  //case 1
           case 2:
               //do stuff for button 2
               break;
           case 3:
               //do stuff for button 3
               break;
           case 4:
               //do stuff for button 4
               break;
       }
		
		
		
		///////
		
		
		


        return true;
	}
	
	///////////////
	public void onWindowFocusChanged(boolean hasFocus) {

//		if (!initDone) {
//			initDone=true;
//			init();
//	width=rl1.getMeasuredWidth();
//	height=rl1.getMeasuredHeight();

if (!sharedpreferences.getString(screen, "").equals("full")) {
Toast.makeText(getBaseContext(),"onWindowsMP4 = "+mp4width+"  "+mp4height,  Toast.LENGTH_SHORT).show();  
//ViewGroup.MarginLayoutParams ipet1 = 
//			(ViewGroup.MarginLayoutParams) myVideoView.getLayoutParams();
//			ipet1.width=700;
//			ipet1.height=900;		
//			ipet1.setMargins(0,0,0,0);
//			myVideoView.requestLayout();
		}

		

		super.onWindowFocusChanged(hasFocus);
	}
	/////////////////////

//    DisplayMetrics displaymetrics = new DisplayMetrics();
//    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//    int height = displaymetrics.heightPixels;
//    int width = displaymetrics.widthPixels;
//    android.widget.FrameLayout.LayoutParams params = (android.widget.FrameLayout.LayoutParams) videoSurface.getLayoutParams();
//    params.width = width;
//    params.height=height;
//    params.setMargins(0, 0, 0, 0);
	
	///////////////
	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);			
		SMBshowFullMovie.this.finish();
		super.onBackPressed();
	}
	
	
	class MP4FileLoadTask extends AsyncTask<Void, String, Void>    {
		private ProgressDialog dialog;
		boolean isCanceled = false;
		private String localFname,localPath;
		private int errors=0;
		private String lastErrormessage="";


		public void myCancel()
		{
		  isCanceled = true;
		}
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Toast.makeText(getBaseContext(), "START !!!!!!!!!!!!... ", Toast.LENGTH_SHORT).show();
			dialog = new ProgressDialog(context);
			dialog.setMessage("Loading....   please wait....");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

		  if (isCanceled)
		{
		return null;
		}
		try {
//			    String pathSmb = "smb://" + ip + "//" + sharedFolder + sharedFile1;  // smb file
			    NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",username,password);
			    SmbFile smbFile = new SmbFile(url,auth2);
			    long lenghtOfFile=smbFile.length(); 
				File PathAndr = new File(Environment.getExternalStorageDirectory(), 
						LOCALPATHANDROIDFIXED);
				localPath=PathAndr.toString();  
				if(!PathAndr.exists()) {
					PathAndr.mkdirs();
					}
				errors=0;
				String[] s0=url.split("/{2}");  // get fr0m net		
				File file = new File(PathAndr, s0[s0.length-1]);		
				localFname=s0[s0.length-1];
//				File source = new File(Path, AndroidFileName);//file is android file		
//				InputStream in = new FileInputStream(source);
//			    SmbFile source = new SmbFile("smb://username:password@a.b.c.d/sandbox/sambatosdcard.txt");
//			    File destination = new File(Environment.DIRECTORY_DOWNLOADS, "SambaCopy.txt");	
						
				InputStream  in = smbFile.getInputStream();
//			    OutputStream out999 = new FileOutputStream(file);
			    
//			    OutputStream fos = openFileOutput(PLAYFILESONG, Context.MODE_PRIVATE);
				OutputStream out999 = new FileOutputStream(file);
			    
			    
			    
			    
			    byte[] buf = new byte[6*1024];
			    int len;
			    long total = 0;
			    while ((len = in.read(buf)) > 0) {
			    	total+=len;
		if (total%1000==0) {

				publishProgress(""+(int)((total*100)/lenghtOfFile)+"%   ("+String.valueOf(total)+"/"+String.valueOf(lenghtOfFile)
					+")"	);			
		}
		out999.write(buf, 0, len); //////fos.write(buf,0,len);
			    }
			    in.close();
			    out999.flush();
			    out999.close();
//			    fos.flush();fos.close();

			} catch (MalformedURLException e) {
				lastErrormessage=e.toString();errors++;
			    e.printStackTrace();
			} catch (IOException e) {
				lastErrormessage=e.toString();errors++;
			    e.printStackTrace();
				}
			catch (Exception e) {
				lastErrormessage=e.toString();errors++;
			    e.printStackTrace();
				}  
		  return null;            
		  
		}

		@Override
		protected void onPostExecute(Void vd) {
		  super.onPostExecute(vd);
		   if (dialog.isShowing()) {
				dialog.dismiss();
	     } 
//		  Dir=localPath;
		 Fname=localFname;
		 if (errors==0){
			 Toast.makeText(getBaseContext(), "DONE  OK  !!! ", Toast.LENGTH_SHORT).show();
			 initMedia();		
		 	}
		 else {
			 Toast.makeText(getBaseContext(), "Error:  "+lastErrormessage, Toast.LENGTH_SHORT).show();
		finish();
			 
		 }

		      } 

		protected void onProgressUpdate(String... values) {
			dialog.setMessage("progress "+values[0]);
		}

		}  // end of class
	
}


//Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 

/////////////////////////////////////
//MP4 metadata content:
// 5 -creation date
// 9 duration mSec
// 12 type Video/mp4
//18 width
//19 height
//20 Total numbers of frames
//43- audio sample rate per Sec
//44 WAV sampling bits
////////////////////////////////////////


//orintation:
//	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


