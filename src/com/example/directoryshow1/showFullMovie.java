package com.example.directoryshow1;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
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

public class showFullMovie extends Activity implements View.OnTouchListener{
	
//	RelativeLayout layout1;
	ArrayList<String> al=new ArrayList<String>();
	Context context =this;
	private ActionBar actionBar;
	private int position = 0;
//	private ProgressDialog progressDialog;
	private MediaController mediaControls;
	SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor ;
	public static final String MyPREFERENCES = "MyPrefsGrid" ;  // my pref internal folder name
  
    // keys
   
    public static final String directory = "Directory";
    public static final String file_name = "File_name";
    public static final String initialized = "INITIALIZED";
    public static final String volume = "VOLUME";
    public static final String screen = "SCREEN";
    
	static String Dir="";
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
	
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		orientation = showFullMovie.this.getResources().getConfiguration().orientation;		
    
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		editor = sharedpreferences.edit();  
		Dir	= sharedpreferences.getString(directory, "");
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

	
	}// end on create

	private void init() {
		
		rl1 = (RelativeLayout) findViewById(R.id.rl1);
		myVideoView = (VideoView) findViewById(R.id.videoViewRelative);
		if (sharedpreferences.getString(screen,"").equals("full")) {
			setContentView(R.layout.fullvideoplayview1);
			rl1 = (RelativeLayout) findViewById(R.id.rl1);
			myVideoView = (VideoView) findViewById(R.id.videoViewRelative);
			LayoutParams params=myVideoView.getLayoutParams();
            params.height=mHeightScreen;
            params.width=mWidthScreen;          
            myVideoView.setLayoutParams(params);
//			ViewGroup.MarginLayoutParams ipet1 = 
//					(ViewGroup.MarginLayoutParams) myVideoView.getLayoutParams();
//					ipet1.width=mWidthScreen;
//					ipet1.height=mHeightScreen;		
//					ipet1.setMargins(0,0,0,0);
//					myVideoView.requestLayout();
				}
		
		else  {
			setContentView(R.layout.fullvideoplayview);
			rl1 = (RelativeLayout) findViewById(R.id.rl1);
			myVideoView = (VideoView) findViewById(R.id.videoViewRelative);
//			ViewGroup.MarginLayoutParams ipet1 = 
//					(ViewGroup.MarginLayoutParams) myVideoView.getLayoutParams();
//					ipet1.width=700;
//					ipet1.height=700;		
//					ipet1.setMargins(0,0,0,0);
//					myVideoView.requestLayout();
//			LayoutParams params=myVideoView.getLayoutParams();
//            params.height=mHeightScreen/2;
//           params.width=mWidthScreen/2;          
//            myVideoView.setLayoutParams(params);
		}		

		rl1.setOnTouchListener(this);		
		audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		volumeMax=(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
		volumeNow = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		
		mediaControls = new MediaController(context);
		mediaControls.show(8000);
		mediaControls.setAnchorView(myVideoView);
		myVideoView.setMediaController(mediaControls);

//		progressDialog = new ProgressDialog(showFullMovie.this);
//		progressDialog.setTitle(" Video View Loading");
//		progressDialog.setMessage("Loading...");
//		progressDialog.setCancelable(true);
//		progressDialog.show();
//		progressDialog.setCanceledOnTouchOutside(true);
		try {			
			myVideoView.setVideoPath(new File(Dir+File.separator+Fname).toString());
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

//				progressDialog.dismiss();				
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
		getMenuInflater().inflate(R.menu.showmovie1, menu);
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
			showFullMovie.this.finish();
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
		File filestr=new File(Dir+File.separator+Fname);
/*		 al.clear();
		String[] s11=new String[50];
		for (int i=0;i<s11.length;i++) {
			s11[i]="";
			}
		s11[5]="date/Time";
		s11[9]="Duration (mSec)";
		s11[12]="Type";
		s11[18]="width";
		s11[19]="height";
		s11[20]="frames";
		s11[43]="Audio sampling rate";
		s11[44]="audio bits/sample";
	
		String s22="";
		        if (filestr.exists()) {
		            Log.i(TAG, ".mp4 file Exist");

		            s22+="Direcory: "+Dir+"\n\rFile:  "+Fname+"\n\r"; 
		            
		            
		            //Added in API level 10
		            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		            try {
		                retriever.setDataSource(filestr.toString());
		                for (int i = 0; i < 100; i++){
		                     //only Metadata != null is printed!
		                    if(retriever.extractMetadata(i)!=null) {

		                    	al.add(retriever.extractMetadata(i));
		                    	s22+="\n\r"+String.valueOf(i)+" : \t"+
		                    			s11[i]+"\t"+
		                    			retriever.extractMetadata(i);
if (s11[i].equals("width"))	mp4width=Integer.parseInt(retriever.extractMetadata(i));
if (s11[i].equals("height")) mp4height=Integer.parseInt(retriever.extractMetadata(i));
		                    	
		                    	
		                    	
		                        Log.i(TAG, "Metadata :: " + retriever.extractMetadata(i));
		                    }

		                }
	               
		            }catch (Exception e){
		                Log.e(TAG, "Exception : " + e.getMessage());
		            }

		        }else {
		            Log.e(TAG, ".mp4 file doesn´t exist.");
		        }	
*/		        
		    	int z;	 
	            MediaMetadataRetriever retriever1 = new MediaMetadataRetriever();
//	    		File file1 = new File(Environment.getExternalStorageDirectory(), "/"+Dir+"/"+Fname);

	            retriever1.setDataSource(filestr.toString());

		    	ArrayList<MP4MetaData> almp4 =new ArrayList<MP4MetaData>();
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_ALBUM;
		    		almp4.add(new MP4MetaData("ALBUM", retriever1.extractMetadata(z)));
		    	}
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST;
		    		almp4.add(new MP4MetaData("ALBUMARTIST", retriever1.extractMetadata(z)));
		    	}
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_ARTIST;
		    		almp4.add(new MP4MetaData("ARTIST", retriever1.extractMetadata(z)));
		    	}
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_AUTHOR;
		    		almp4.add(new MP4MetaData("AUTHOR", retriever1.extractMetadata(z)));
		    	}
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_BITRATE;
		    		almp4.add(new MP4MetaData("BITRATE", retriever1.extractMetadata(z)));
		    	}
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER;
		    		almp4.add(new MP4MetaData("CD_TRACK_NUMBER", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPILATION)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_COMPILATION;
		    		almp4.add(new MP4MetaData("COMPILATION", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_COMPOSER;
		    		almp4.add(new MP4MetaData("COMPOSER", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_DATE;
		    		almp4.add(new MP4MetaData("DATE", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_DURATION;
		    		almp4.add(new MP4MetaData("DURATION", retriever1.extractMetadata(z)));
		    	}
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_GENRE;
		    		almp4.add(new MP4MetaData("GENRE", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO;
		    		almp4.add(new MP4MetaData("HAS_AUDIO", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO;
		    		almp4.add(new MP4MetaData("HAS_VIDEO", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_LOCATION;
		    		almp4.add(new MP4MetaData("LOCATION", retriever1.extractMetadata(z)));
		    	}	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_TITLE;
		    		almp4.add(new MP4MetaData("TITLE", retriever1.extractMetadata(z)));
		    	}	
		    	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT;
		    		almp4.add(new MP4MetaData("VIDEO_HEIGHT", retriever1.extractMetadata(z)));
		    	}	
		    	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH;
		    		almp4.add(new MP4MetaData("VIDEO_WIDTH", retriever1.extractMetadata(z)));
		    	}	
		    	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_WRITER;
		    		almp4.add(new MP4MetaData("WRITER", retriever1.extractMetadata(z)));
		    	}	
		    	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_YEAR;
		    		almp4.add(new MP4MetaData("YEAR", retriever1.extractMetadata(z)));
		    	}	
		    	
		    	
		    	
		    	if (retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)!=null){
		    		z=MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION;
		    		almp4.add(new MP4MetaData("VIDEO_ROTATION", retriever1.extractMetadata(z)));
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

		if (!initDone) {
			initDone=true;
			init();
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
else {}
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
		showFullMovie.this.finish();
		super.onBackPressed();
	}
	
	
	
	
	
	
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


