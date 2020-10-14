package com.example.directoryshow1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;

public class SMBaudioPlay1<audioPlay> extends Activity implements SensorEventListener{
	
	int width=0,height=0;
    final float MAX_VOLUME=100.0f;
	static ImageView iv;
	TextView tvStat,timerValue,tvvolStat,tvab;
	EditText ed1;
	private Button startButton;
    private Button pauseButton;
    private Button clrButton;
    private Button button_back;
    ImageView imageView,imageViewP;
    SMBDrawView imageViewVOL;
    ProgressBar progressBar;
    SeekBar seekBar;
    View view1,view2,view3,view4;
    ActionBar ab;
    double screenHeight;
    
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private boolean isPaused=false;
    MediaPlayer mediaPlayer;
    int length;
    boolean isAudioKilled=true;
    int file_exists_flag=0;
    int songDuration=0;
	int TabletIndex;
	float startX,startY;
	float leftVolume=0.4f,rightVolume=0.4f;
	int fileIndex=0;
	ArrayList<String> playableFiles = new ArrayList<String>();
	byte[] art;
	boolean playPauseOnTouch=false;
    int layout1Height;
    int layout1Width;
	Context context=this;
	private MP3FileLoadTask mp3Load;
	
	Animation anim;
	//tablet params table:  24 columns per row
//	tv1 		height,			margin height
//	tv2			height,			margin height
//	progress	height,			margin height
//	imageView 	height, 		margin height		width,		margin width
//		play 		height, 		margin height		width,		margin width
//	pasue 		height, 		margin height		width,		margin width
//	CLR			height, 		margin height		width,		margin width
//	back		height, 		margin height
// total 24 params 0 to 23 indexes
	
//	int TabletParmas[][]={
//			{60,30,  40,30,  40,30,  750,40,750,180, 250,60,250,80,	250,60,250,60,	250,60,250,60,	200,60,  200,60,200,40  },  // idx=0
//			{60,30,  40,30,  40,30,  350,20,350,120, 100,40,100,60,		100,40,100,60,		100,40,100,60,		130,40,  100,140,100,20   },  // idx=1
//			{60,30,  40,30,  40,30,  450,20,450,160,   150,50,150,80,	150,50,150,80,		150,50,150,80,		150,40,  150,50,150,20  },  // idx=2
//			{60,30,  40,30,  40,30,  100,20,100,20, 	40,20,40,20,	40,20,40,20,		40,20,40,20,		40,20,  40,20,40,10    },  // idx=3
//			{60,30,  40,30,  40,30,  100,20,100,20, 	40,20,40,20,	40,20,40,20,		40,20,40,20,		40,20,  40,20,40,10  	 }  //  idx=4
//			};
	
	//24 per row
	
	
	//KeyListener mKeyListener;
	
	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "MyPrefsGridSMBAUDIO" ;  // my pref internal folder name
    
    // keys
    public static final String Initialized = "Initialized";
    public static final String size_x = "size_x";
    public static final String size_y = "size_y";
    public static final String directory = "Directory";
    public static final String file_name = "File_name";
    public static final String sort_type = "sort";  // 0=no sort, 1 by name asc, 2 by name desc, 3 by date asc,  4 by name dsc
    public static final String volume = "VOLUME";
    public static final String shuffleaudioplay = "SHUFFLEAUDIOPLAY";
    public static final String LOCALPATHANDROIDFIXED = ".SAMBALOCALDIRSHOW1";
    public static final String PLAYFILESONG = "PLAYFILESONG";
      
    String size_x_="4",size_y_="4"; 	  // matrix of images size to be stored in preferenecs 
	static String Dir="";
	static String Fname="";
	String read_string;	
	String shuffleYesNo="";
	int mWidthScreen,mHeightScreen;
	RelativeLayout layout1;

	private SensorManager sensorManager;
    private long lastUpdate;	
    private Drawable drd1,drd2,drd3;
    private int states=0; //0=horizontal,1=reached X right rotate, 2=reached left rotate
    private int blibkCounter1=0;
	
    private String fromWeb,k999;
	String albom="",artist="",song_name="";

	String url,username,password;
    
	String localFile2Play="myLocalMP3File";
	
	public void onCreate(Bundle bundle) {
	    super.onCreate(bundle);
	    
	    Intent intent = getIntent();
	    url=intent.getStringExtra("url");
	    username=intent.getStringExtra("username");    
	    password=intent.getStringExtra("password");
	    
        Drawable img = context.getResources().getDrawable( R.drawable.redcircle32x32);
        Bitmap b = ((BitmapDrawable)img).getBitmap();
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 40, 40, false);
        drd1=new BitmapDrawable(this.getResources(), bitmapResized);

        img = context.getResources().getDrawable( R.drawable.bluecircle32x32);
        b = ((BitmapDrawable)img).getBitmap();
        bitmapResized = Bitmap.createScaledBitmap(b, 40, 40, false);
        drd2=new BitmapDrawable(this.getResources(), bitmapResized);
        
        img = context.getResources().getDrawable( R.drawable.greencircle32x32);
        b = ((BitmapDrawable)img).getBitmap();
        bitmapResized = Bitmap.createScaledBitmap(b, 40, 40, false);
        drd3=new BitmapDrawable(this.getResources(), bitmapResized);
        		
	    
	    /*
	    Display disp= getWindowManager().getDefaultDisplay();
		Point size = new Point();
		disp.getSize(size);
		mWidthScreen = size.x;
		mHeightScreen = size.y;
		*/
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();
        
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		read_prefs();
		Dir="/storage/emulated/0/effects";
		Fname="Pew9.mp3";
		double dbl=get_screen_params();
		 TabletIndex=0;
		if (dbl>5 && dbl <6){
			TabletIndex=0;
			}
			else if (dbl>7 && dbl <7.49){
				TabletIndex=1;
			}
			else if (dbl>7.5 && dbl <8){
				TabletIndex=2;
			}
			else if (dbl>8 && dbl <9.9){
				TabletIndex=3;	
			}
			else {
				TabletIndex=4;	
			}
		
		
		layout1 = new RelativeLayout(this);
        layout1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setContentView(layout1);
		  ab = getActionBar();
		  setAnim();
		  initActionBar();


	}// end on create

	
	private void initMediaRetriever() {
		String songDuration="-1";
		String songLength="-1";
		String hasAudio = "false";
		int error=0;
		MediaMetadataRetriever	metaRetriver = new MediaMetadataRetriever();
		FileInputStream inputS=null;
		try {
			inputS = openFileInput(PLAYFILESONG);
			try {
				metaRetriver.setDataSource(inputS.getFD());
			} catch (IllegalArgumentException | IOException e) {
				error++;
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			error++;
			e1.printStackTrace();
		}	

		if (error!=0) {
		    Toast.makeText(getApplicationContext(), "Bad media", Toast.LENGTH_SHORT).show();
//			finish();
		}
		else {
		songDuration=metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
		songLength=metaRetriver.extractMetadata(length);
		hasAudio=metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO);
		if (songDuration==null || hasAudio==null){
			   Toast.makeText(getApplicationContext(), "Bad media null", Toast.LENGTH_SHORT).show();
//				finish();
		}

		if (Integer.valueOf(songDuration)<=0 || !hasAudio.equalsIgnoreCase("yes")){
		    Toast.makeText(getApplicationContext(), "Bad format ", Toast.LENGTH_SHORT).show();
//			finish();
			}
		}
		metaRetriver.release();
	}


	@Override
	protected void onResume() {
        sensorManager.registerListener(SMBaudioPlay1.this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
		super.onResume();
	}

	   @Override
	    protected void onPause() {
	        // unregister listener
	        super.onPause();
	        sensorManager.unregisterListener(SMBaudioPlay1.this);
	    }
	    
	    @Override
	    protected void onDestroy() {
	        super.onDestroy();
	        if (sensorManager != null) {
	        	sensorManager.unregisterListener(this);
	        }
	    }

	
	@Override
	public void onWindowFocusChanged (boolean hasFocus){
 if (width==0 || height==0){
	 	height=layout1.getHeight();
        width=layout1.getWidth();

		set_view_play(height,width);
        setContentView(layout1);
        tvvolStat.setText(String.valueOf((int)(leftVolume*100)   )+"%");
		
		isAudioKilled=true;
		startButton.setEnabled(false);
		pauseButton.setEnabled(false);
		clrButton.setEnabled(false);
				
		//audioFilePath =Environment.getExternalStorageDirectory().getAbsolutePath()+ "/kalibma.mp3";
		startButton.setEnabled(true);
		pauseButton.setEnabled(false);
		clrButton.setEnabled(false);
//		initMediaretrievr1();
//		updateTVSTAT();

    			
	    startButton.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View view) {
		            updateVolColour();
		            	playIt();
		            						}
	    		});
	
	    
	    
	    clrButton.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View view) {
		            set_clear_actions();	
		            pauseButton.clearAnimation();
		            timerValue.clearAnimation();
		            }

					
		        });
	    
		        pauseButton.setOnClickListener(new View.OnClickListener() {
		            public void onClick(View view) {
	            	pauseIt();
		            }
		        });
		
		
		
		        button_back.setOnClickListener(new View.OnClickListener() {
		               public void onClick(View view) {
		            	if (!isAudioKilled) {
		            		//mediaPlayer.stop();
			            	mediaPlayer.release();
			            	mediaPlayer = null;
		            				}
		        		sensorManager.unregisterListener(SMBaudioPlay1.this);
		        		SMBaudioPlay1.this.finish();	
		               }
		        });
		        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		        {
		        @Override
		        public void onStopTrackingTouch(SeekBar seekBar) 
		        {
           if (mediaPlayer!=null && isAudioKilled){
		        Toast.makeText(getApplicationContext(), "on stop "+seekBar.getProgress()
			    		, Toast.LENGTH_SHORT).show();
		        timeSwapBuff=(seekBar.getProgress()*mediaPlayer.getDuration())/1000;
		        length=(int) timeSwapBuff;
		        isPaused=true;
           						}
           else {
        	   pauseIt(); 
		        timeSwapBuff=(seekBar.getProgress()*mediaPlayer.getDuration())/1000;
		        length=(int) timeSwapBuff;
        	   playIt();
        	   
        	   
        	   
           }
		        }
		        @Override
		        public void onStartTrackingTouch(SeekBar seekBar) 
		        {
		            if (mediaPlayer!=null && isPaused){
		             
		        Toast.makeText(getApplicationContext(), "on start "+seekBar.getProgress()
			    		, Toast.LENGTH_SHORT).show(); 
		        }
		        }
		        @Override
		        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) 
		        {
		        if (mediaPlayer!=null && isPaused){

		        }
		        	
	
		        }
		        });
		    
//	new MP3FileLoadTask().execute();
	mp3Load =new MP3FileLoadTask();
	mp3Load.execute();
	
	
	}
	
        super.onWindowFocusChanged(hasFocus);
	}

private void updateTVSTAT() {
	StringBuilder sb=new StringBuilder();
	String[] s0=url.split("/{2}");  // get fr0m net		
	for (int i=0;i<s0.length-1;i++){
		sb.append(s0[i]+"//");
	}
//	getAudioPlayableFiles(Dir, Fname);
//	fileIndex=getAudioIndex();
	tvStat.setText("Directory= "+sb.toString()+
			"\nFile= "+Fname+
	"\nAlbom= "+albom+"\nArtist= "+artist+"\nSong: "+song_name
//	+ 	"\nI= "+fileIndex
			);	
	tvab.setText("   "+Fname+"  ");	
}


private void initMediaretrievr1() {
	MediaMetadataRetriever	metaRetriver = new MediaMetadataRetriever();	
	FileInputStream inputS=null;
	try {
		inputS = openFileInput(PLAYFILESONG);
		try {
			metaRetriver.setDataSource(inputS.getFD());
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
	} catch (FileNotFoundException e1) {
		e1.printStackTrace();
	}
	
try {	
	art = metaRetriver.getEmbeddedPicture();
	albom = metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
	artist=metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
	song_name=metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
	Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
	imageView.setImageBitmap(songImage);
	
} catch (Exception e) {
	 imageView.setImageResource(R.drawable.song100);
	 albom="Unknown Album";
	 artist="Unknown Artist";
	 song_name=Fname;		
}	
metaRetriver.release();
	}


/////////////////////////////////////////////
	@Override
	protected void onStart() {
	super.onStart();
	}

	private void read_prefs() {
		Dir	= sharedpreferences.getString(directory, "");
		Fname	= sharedpreferences.getString(file_name, "");
		SharedPreferences.Editor editor = sharedpreferences.edit(); 
		if (sharedpreferences.getString(volume,"").equals("")){
			editor.putString(volume, "50").apply();
		}
		leftVolume=Float.parseFloat(sharedpreferences.getString(volume, "50"));
		rightVolume=Float.parseFloat(sharedpreferences.getString(volume, "50"));
		
		if (leftVolume>90.0f || rightVolume>90.0f){
			editor.putString(volume, "90").apply();
			leftVolume=Float.parseFloat(sharedpreferences.getString(volume, ""));
			rightVolume=Float.parseFloat(sharedpreferences.getString(volume, ""));				
		}
		
		
		if (sharedpreferences.getString(shuffleaudioplay, "").equals("")){
			add_prefs_key(shuffleaudioplay, "NO");
			shuffleYesNo="NO";
		}
		else {
			shuffleYesNo=sharedpreferences.getString(shuffleaudioplay, "");
		}
Toast.makeText(getApplicationContext(), "shuffle "+shuffleYesNo,	Toast.LENGTH_SHORT).show();	
		 		
					}

	
	
	private void set_clear_actions() {
    	startButton.setText("");
    	isPaused=false;
    	startButton.setEnabled(true);
		pauseButton.setEnabled(false);
		clrButton.setEnabled(false);
		isAudioKilled=true;
		mediaPlayer.pause();
		mediaPlayer.seekTo(0);
//		mediaPlayer.release();
//    	mediaPlayer = null;
       	timeSwapBuff = 0L;
      customHandler.removeCallbacks(updateTimerThread);
      timerValue.setText("");
      imageViewP.setImageResource(R.drawable.audioclear1);
      seekBar.setProgress(0);
	}
	
	//tablet params table:  24 columns per row
//	tv1 		height,			margin height
//	tv2			height,			margin height
//	progress	height,			margin height
//	imageView 	height, 		margin height		width,		margin width   6,7,8,9
//		play 		height, 		margin height		width,		margin width 10,11,12,13,
//	pasue 		height, 		margin height		width,		margin width 14,15,16,17
//	CLR			height, 		margin height		width,		margin width 18,19,20,21
//	back		height, 		margin height 22,23
// total 24 params 0 to 23 indexes 
	
	private void set_view_play(int height, int width) {

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    	view2=new View (getBaseContext());  // top green line
        RelativeLayout.LayoutParams params_view2 = 
        		new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT);
        view2.setLayoutParams(params_view2);
        view2.setId(901);
        params_view2.height=5;
        params_view2.width=width;
        params_view2.setMargins(0,height/120, 0, 0);
        params_view2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params_view2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        view2.setBackgroundColor(Color.GREEN);
        layout1.addView(view2,params_view2);
		
		
		tvStat = new TextView(getBaseContext());  // song   data
        RelativeLayout.LayoutParams params_tvStat = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT);
        tvStat.setLayoutParams(params_tvStat);
        tvStat.setId(1000);
        //params.addRule(RelativeLayout.LEFT_OF, 1001); 
        params_tvStat.addRule(RelativeLayout.BELOW, 901);
        params_tvStat.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params_tvStat.height= height/8  ;
        params_tvStat.width= width  ;
        params_tvStat.setMargins(0,	0 , 0, 0);  
        tvStat.setGravity(Gravity.BOTTOM | Gravity.CENTER);
//        tvStat.setHint("STATUS   Enter some text....");
        tvStat.setTextSize(height/110);
        tvStat.setTextColor(Color.BLACK);
        layout1.addView(tvStat);
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    	view3=new View (getBaseContext());// 2nd green line
	        RelativeLayout.LayoutParams params_view3 = 
	        new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
	                LayoutParams.WRAP_CONTENT);
	        view3.setLayoutParams(params_view3);
	        view3.setId(902);
	        params_view3.height=5;
	        params_view3.width=width;

	        params_view3.addRule(RelativeLayout.BELOW, 1000);
	        params_view3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        view3.setBackgroundColor(Color.GREEN);
	        params_view3.setMargins(0,height/140 , 0, 0);
	        layout1.addView(view3,params_view3);

	        
	        ///////////////////
	        timerValue = new TextView(getBaseContext());  // timer position duration
	        RelativeLayout.LayoutParams params_timerValue = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
	                LayoutParams.WRAP_CONTENT);
	        timerValue.setLayoutParams(params_timerValue);
	        timerValue.setId(1001);
	        params_timerValue.addRule(RelativeLayout.BELOW, 902);
	       // params_timerValue.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	        params_timerValue.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        //params_timerValue.height= (int)((mHeightScreen)/35)     ;
	        params_timerValue.height=height/15 ;
	        params_timerValue.width=width ; 
	        //timerValue.setTextSize(TypedValue.COMPLEX_UNIT_DIP,35);
	        params_timerValue.setMargins(0,	0  , 0, 0);
	        timerValue.setGravity(Gravity.BOTTOM | Gravity.CENTER);
	        timerValue.setText("00:00:00");
	        timerValue.setTextColor(Color.BLACK);
	        timerValue.setTextSize(height/110);
	        layout1.addView(timerValue);

	        seekBar = new SeekBar(getBaseContext());
	        seekBar.setMax(1000);
	        seekBar.setId(1011);
	        ShapeDrawable thumb = new ShapeDrawable(new OvalShape());
	        thumb.setIntrinsicHeight(80);
	        thumb.setIntrinsicWidth(30);
	        seekBar.setThumb(thumb);
	        seekBar.setProgress(0);
	        seekBar.setVisibility(View.VISIBLE);
	        seekBar.setBackgroundColor(Color.BLUE);
	        RelativeLayout.LayoutParams params_seekBar = new RelativeLayout.LayoutParams(
	         		RelativeLayout.LayoutParams.MATCH_PARENT,
	         		RelativeLayout.LayoutParams.WRAP_CONTENT);
	        seekBar.setLayoutParams(params_seekBar);
	 //       LayoutParams sb = new LayoutParams(200, 50);
	 //       seekBar.setLayoutParams(sb);
	        params_seekBar.setMargins(0,	height/30  , 0, 0);
	        // params_progressBar.height=100;
	         //RelativeLayout.LayoutParams params_progressBar  = new RelativeLayout.LayoutParams(100,100);
	         //params_progressBar.addRule(RelativeLayout.CENTER_IN_PARENT);
	         params_seekBar.addRule(RelativeLayout.BELOW, 1001);
	         params_seekBar.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	         
	         getResources().getDrawable(R.drawable.drag_thumb); // once
	         seekBar.setThumb(getResources().getDrawable(R.drawable.drag_thumb))    ;
	         getResources().getDrawable(R.layout.smbcustom_seekbar_layout); // once
	         seekBar.setProgressDrawable(getResources().getDrawable(R.layout.smbcustom_seekbar_layout))    ;;
     	         
	        layout1.addView(seekBar);
	        
	        
			///////////////////////////////////////////////////////////////////////////////  
	        imageView = new ImageView(getBaseContext());  // album image picture
	        RelativeLayout.LayoutParams params_imageView = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
	                LayoutParams.WRAP_CONTENT);
	         
	        imageView.setLayoutParams(params_imageView);
	        imageView.setId(1015);
	        //params.addRule(RelativeLayout.LEFT_OF, 1001); 
	        params_imageView.addRule(RelativeLayout.BELOW,1011);
	        params_imageView.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        params_imageView.setMargins(width/250 ,	height/160	  , 0, 0);  
	        //params_tvStat.height= (int)(mHeightScreen/35)     ;
	        //params_tv1.height= 30   ;
	        params_imageView.height=(3*width)/5 ;
	        params_imageView.width=(3*width)/5  ;
	        //imageView.setImageResource(R.drawable.audioplay);
	        imageView.setImageResource(R.drawable.song100);
	        layout1.addView(imageView);
	        ///////////////////////////////
	       
	        
	///////////////////////////////////////////////////////////////////////////////  imageViewP
	imageViewP = new ImageView(getBaseContext());//right of main image
	RelativeLayout.LayoutParams params_imageViewP = 
			new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
			LayoutParams.WRAP_CONTENT);
	imageViewP.setLayoutParams(params_imageViewP);
	imageViewP.setId(1095);
	params_imageViewP.addRule(RelativeLayout.ABOVE,903);// view 4
	params_imageViewP.addRule(RelativeLayout.RIGHT_OF,1015);
	params_imageViewP.addRule(RelativeLayout.ALIGN_BASELINE,1015);
	params_imageViewP.setMargins(width/8,0,0, 5);  
	params_imageViewP.height=(height)/10 ;
	params_imageViewP.width=(width)/10 ;
	imageViewP.setImageResource(R.drawable.audioclear1);
	layout1.addView(imageViewP,params_imageViewP);
//	imageViewP.setVisibility(View.GONE);//////////////
	////////////////////////////////////////
	imageViewVOL = new SMBDrawView(getBaseContext());
	
	RelativeLayout.LayoutParams params_imageViewVOL = 
			new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
			LayoutParams.WRAP_CONTENT);
	imageViewVOL.setLayoutParams(params_imageViewVOL);
	imageViewVOL.setId(1096);
	params_imageViewVOL.height=(height)/10 ;
	params_imageViewVOL.width=(width)/30 ;
	params_imageViewVOL.addRule(RelativeLayout.BELOW,1011);
	params_imageViewVOL.addRule(RelativeLayout.RIGHT_OF,1015);
//	params_imageViewVOL.setMargins(60,90,0, 0);  
	
//	imageViewVOL.setImageResource(R.drawable.yellowvolume);
	layout1.addView(imageViewVOL,params_imageViewVOL);
	
	SMBDrawView.height=height/10;
	SMBDrawView.width=width/30;
    ViewGroup.MarginLayoutParams pi = 
    		(ViewGroup.MarginLayoutParams) imageViewVOL.getLayoutParams();
    		pi.setMargins(width/10,3*height/100	,0,0);
    		imageViewVOL.requestLayout();
    		SMBDrawView.VOLpercent=leftVolume;//////////////////////////////////
	imageViewVOL.invalidate();//////////////////////////////////////////

	
	
	
	
	
	///////////////////////////////
	tvvolStat = new TextView(getBaseContext());
    RelativeLayout.LayoutParams params_tvvolStat = 
    		new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
            LayoutParams.WRAP_CONTENT);
    tvvolStat.setLayoutParams(params_tvvolStat);
    tvvolStat.setId(1097);
    //params.addRule(RelativeLayout.LEFT_OF, 1001); 
//    params_tvvolStat.addRule(RelativeLayout.BELOW, 1096);
//    params_tvvolStat.addRule(RelativeLayout.RIGHT_OF,1015);
    params_tvvolStat.height= height/10  ;
    params_tvvolStat.width= width /8 ;
    params_tvvolStat.addRule(RelativeLayout.BELOW,1011);  // under seekbar
    params_tvvolStat.addRule(RelativeLayout.RIGHT_OF,1015);
    params_tvvolStat.setMargins(width/5,3*height/100,0, 0);  
    tvvolStat.setGravity(Gravity.CENTER | Gravity.CENTER);
    tvvolStat.setTextSize(height/70);
    tvvolStat.setTextColor(Color.BLACK);
    tvvolStat.setTypeface(null, Typeface.BOLD_ITALIC);
    layout1.addView(tvvolStat);
	tvvolStat.setTextSize(TypedValue.COMPLEX_UNIT_PX,
			getResources().getDimension(R.dimen.font_size2));
	tvvolStat.setBackgroundColor(Color.GRAY);
	updateVolColour();
	//////////////////////////////////////
	view4=new View (getBaseContext());
    RelativeLayout.LayoutParams params_view4 = 
    		new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
            LayoutParams.WRAP_CONTENT);
    view4.setLayoutParams(params_view4);
    view4.setId(903);
    params_view4.height=5;
    params_view4.width=width;
    params_view4.addRule(RelativeLayout.BELOW, 1015);
    params_view4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    view4.setBackgroundColor(Color.GREEN);
    params_view4.setMargins(0,height/60	 , 0, height/60);
    layout1.addView(view4,params_view4);

////////////////	           
	        startButton = new Button(getBaseContext());
	        RelativeLayout.LayoutParams params_startButton = 
	        		new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
	        LayoutParams.WRAP_CONTENT);
	        startButton.setLayoutParams(params_startButton);
	        params_startButton.width=(width)/7  ;
	        params_startButton.height=(height)/10  ;
	        params_startButton.setMargins(width/20,height/60	 , 0, height/60); 
	        startButton.setId(1020);
	        startButton.setText("");
	        startButton.setTextColor(Color.GREEN);
	        startButton.setTextSize((width)/50);
	        startButton.setTypeface(null, Typeface.BOLD);
	        params_startButton.addRule(RelativeLayout.BELOW, 903);
	        params_startButton.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        startButton.setBackgroundResource(R.drawable.audioplay1);	
	        layout1.addView(startButton,params_startButton);
	        
	       ///////////////////////////////////////////////////////////////////////////////////////////////////////////// ;
  
	        pauseButton = new Button(getBaseContext());
	        RelativeLayout.LayoutParams params_pauseButton = 
	        		new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
	        LayoutParams.WRAP_CONTENT);
	        pauseButton.setLayoutParams(params_pauseButton);
	        params_pauseButton.width=(width)/7  ;
	        params_pauseButton.height=(height)/10  ;
	        params_pauseButton.setMargins(width/20,height/60	 , 0, height/60); 
	        pauseButton.setId(1021);
	        pauseButton.setText("");
	        pauseButton.setTextColor(Color.GREEN);
	        pauseButton.setTextSize((width)/50);
	        pauseButton.setTypeface(null, Typeface.BOLD);
	        params_pauseButton.addRule(RelativeLayout.BELOW, 903);
	        params_pauseButton.addRule(RelativeLayout.RIGHT_OF,1020);
	        pauseButton.setBackgroundResource(R.drawable.audiopause2);	
	        layout1.addView(pauseButton,params_pauseButton);	        
	        
	        
	        
	        //////////////////////////////////////////////////////////////
	        clrButton = new Button(getBaseContext());
	        RelativeLayout.LayoutParams params_clrButton = 
	        		new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
	                LayoutParams.WRAP_CONTENT);
	        clrButton.setLayoutParams(params_clrButton);
	        params_clrButton.width=(width)/7  ;
	        params_clrButton.height=(height)/10  ;
	        clrButton.setId(1022);
	        clrButton.setText("");  
	        clrButton.setTextColor(Color.GREEN);
	        clrButton.setTextSize((width)/50);
	        clrButton.setTypeface(null, Typeface.BOLD);
	        params_clrButton.addRule(RelativeLayout.BELOW, 903);
//	        params_clrButton.addRule(RelativeLayout.RIGHT_OF,1021);
	        params_clrButton.setMargins(46*width/100,height/60, 0, height/60);
	        params_clrButton.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

	        clrButton.setBackgroundResource(R.drawable.audiostop1);
	        layout1.addView(clrButton,params_clrButton);
	        /////////////////////////////////////////////////////////////////
        
	        button_back = new Button(getBaseContext());
	        RelativeLayout.LayoutParams params_button_back = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
	                LayoutParams.WRAP_CONTENT);
	        button_back.setLayoutParams(params_button_back);
	        params_button_back.height=(height)/10;
	        params_button_back.width=(width)/7;
	        params_button_back.setMargins(76*width/100,height/60	 , 0, height/60);
	        
	        button_back.setId(1030);
	        button_back.setText("");
	        button_back.setTextColor(Color.RED);
	        button_back.setTextSize((width)/50);
	        button_back.setTypeface(null, Typeface.BOLD);
	        params_button_back.addRule(RelativeLayout.BELOW, 903);
	        params_clrButton.addRule(RelativeLayout.RIGHT_OF,1022);
	        params_button_back.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        button_back.setBackgroundResource(R.drawable.back10);
	        layout1.addView(button_back,params_button_back);
	        
	    	view1=new View (getBaseContext());
	        RelativeLayout.LayoutParams params_view1 = 
	        		new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
	                LayoutParams.WRAP_CONTENT);
	        view1.setLayoutParams(params_view1);
	        view1.setId(999);
	        params_view1.height=5;
	        params_view1.width=width;
	        params_view1.setMargins(0,height/60	 , 0, 0);
	        params_view1.addRule(RelativeLayout.BELOW, 1030);
	        params_view1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        view1.setBackgroundColor(Color.GREEN);
	        layout1.addView(view1,params_view1);
	        

			
			
//			android.view.ViewGroup.LayoutParams params3i = tvvolStat.getLayoutParams();
//			params3i.height = getResources().getDimensionPixelSize(R.dimen.height1a);
//			params3i.width = getResources().getDimensionPixelSize(R.dimen.width2);
//			tvvolStat.setTextSize(TypedValue.COMPLEX_UNIT_PX,
//					getResources().getDimension(R.dimen.font_size2));
//			tvvolStat.setBackgroundColor(Color.GRAY);
//			tvvolStat.setLayoutParams(params3i);
	
//			ViewGroup.MarginLayoutParams pxc = 
//					(ViewGroup.MarginLayoutParams) tvvolStat.getLayoutParams();
//					pxc.setMargins(40,60,0,0);
//					tvvolStat.requestLayout();
			
	}


	private Runnable updateTimerThread = new Runnable() {
        public void run() {
/////timerValue.setCompoundDrawablesWithIntrinsicBounds(null, null, drd1, null );	
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            int milliseconds = (int) (updatedTime % 1000);
            
            // this is song duration parsing
            int song_time=songDuration;
            int secsp = (int) (song_time / 1000);
            int minsp = secsp / 60;
            secsp = secsp % 60;
            int millisecondsp = (int) (song_time % 1000);
  
            timerValue.setText("Song position:  " + String.format("%02d", mins) + ":"
            + String.format("%02d", secs) + ":"
            + String.format("%03d", milliseconds)
            + "\nSong Duration:   "+ 
			//"" + minsp + ":"
			"" + String.format("%02d", minsp) + ":"
			+ String.format("%02d", secsp) + ":"
			+ String.format("%03d", millisecondsp)
                        		);

            
            blibkCounter1++;
            
            
            
if (blibkCounter1<5 ) timerValue.setCompoundDrawablesWithIntrinsicBounds(null, null, drd3, null );	
else if (blibkCounter1>5 ){
	timerValue.setCompoundDrawablesWithIntrinsicBounds(null, null, drd2, null );	
         
}      
       if ( blibkCounter1>=10) blibkCounter1=0;
            
           // progressBar.setProgress((int)  (    (updatedTime*100) /songDuration)            );
            seekBar.setProgress((int)  (    (updatedTime*1000) /songDuration)            );
            customHandler.postDelayed(this, 300);
        }
		
    };

    private double get_screen_params() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		double wp=dm.widthPixels;
		double xdpi=dm.xdpi;
		double x = Math.pow(wp/xdpi,2);
		
		screenHeight=dm.heightPixels;
		double ydpi=dm.ydpi;
		double y = Math.pow(screenHeight/ydpi,2);
		double screenInches = Math.sqrt(x+y);
     
		return screenInches;
	}
    
    
    
    
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.smbshowaudio1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		
		case R.id.showlyrics:
			
			if (!SMBLyricsGetNetworkConnecionStatus.isNetworkAvailable(context)		){
				aldiag("network connection is not available!!!",false);
			}	
			
			else	if (		artist==null || artist.length()==0
						|| 	song_name==null || song_name.length()==0
					){
				aldiag("can not reslove song  to lyrics!!!",false);
			}				
			else{	      
				getHtmlFromWeb();
			}

			
			
			
			
			
			
			
			break;	
		
		
		
		
		
		
		case R.id.shuffleYes:
			
			if (!isAudioKilled || isPaused) 
				Toast.makeText(getApplicationContext(), "Can't change while playing...", Toast.LENGTH_SHORT).show(); 
			else {
			add_prefs_key(shuffleaudioplay,"YES");
initActionBar();
}
			break;		
			
		case R.id.shuffleNo:
			if (!isAudioKilled || isPaused) 
				Toast.makeText(getApplicationContext(), "Can't change while playing...", Toast.LENGTH_SHORT).show(); 
			else {
			add_prefs_key(shuffleaudioplay,"NO");
			initActionBar();
			}
			break;			
					
					
		case R.id.exit:
	//Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 
			add_prefs_key(volume,Float.toString(leftVolume));
			if (mediaPlayer!=null) {
        		//mediaPlayer.stop();
            	mediaPlayer.release();
            	mediaPlayer = null;
        	}
			sensorManager.unregisterListener(SMBaudioPlay1.this);
			File PathAndr = new File(Environment.getExternalStorageDirectory(), 
					LOCALPATHANDROIDFIXED);
			if(PathAndr.exists()) {
File[] ff=PathAndr.listFiles();
for (int i=0;i<ff.length;i++){
	ff[i].delete();
				}		
		}		
    		SMBaudioPlay1.this.finish();	
			break;
			
			
		case R.id.test:
//	new MP3FileLoadTask().execute();
	mp3Load =new MP3FileLoadTask();
	mp3Load.execute();

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
	
	@Override
	public boolean onTouchEvent (MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = event.getY();
			startX=event.getX();
			break;
		case MotionEvent.ACTION_UP: {
			float endY = event.getY();
			float endX = event.getX();
			
			float diffY=endY - startY ;
			float diffX=endX - startX;
	
			if (Math.abs(diffY)<20.0f && Math.abs(diffX)<20.0f) {  // touch 
//Toast.makeText(getApplicationContext(), "TOUCH", Toast.LENGTH_SHORT).show();	
playPauseOnTouch=!playPauseOnTouch;
if (playPauseOnTouch) playIt(); //true
else pauseIt();

////////////////////////////////////
}
			
			else if (Math.abs(diffY)> Math.abs(diffX) && endY < startY) {
 
//				float log1=(float)(Math.log(maxVolume-currVolume)/Math.log(maxVolume));
//				yourMediaPlayer.setVolume(1-log1);
//
final float volume=(float) (1 - (Math.log(MAX_VOLUME - Math.abs(diffY/(10))) / Math.log(MAX_VOLUME)));
			
		leftVolume += volume;
		rightVolume += volume;
					if (leftVolume>0.95f){
						leftVolume=0.95f;
						rightVolume=0.95f;
					}
 //Toast.makeText(getApplicationContext(), "Volume UP "+String.valueOf(Math.abs(leftVolume*100)), Toast.LENGTH_SHORT).show();	

			if (mediaPlayer!=null)
 mediaPlayer.setVolume(leftVolume, rightVolume);
//			imageViewVOL.width=imageViewVOL.getWidth();
//			imageViewVOL.height=imageViewVOL.getHeight();
//			imageViewVOL.invalidate();
			SMBDrawView.VOLpercent=leftVolume;
			imageViewVOL.invalidate();
//			actionBar.setTitle("Music player"
//			+String.valueOf((int)(leftVolume*100))
			
//					);
updateVolColour();
			}
		

			else if (Math.abs(diffY)> Math.abs(diffX) && endY > startY) {
final float volume=(float) (1 - (Math.log(MAX_VOLUME - Math.abs(diffY/10)) / Math.log(MAX_VOLUME)));
				
	leftVolume -= volume;
	rightVolume -= volume;

		if (leftVolume<0){
			leftVolume=0.0f;
			rightVolume=0.0f;
		}
//Toast.makeText(getApplicationContext(), "Volume DOWN "+String.valueOf(Math.abs(leftVolume*100)), Toast.LENGTH_SHORT).show();
//		actionBar.setTitle("Music player"
//	+String.valueOf((int)(leftVolume*100)   )
//					);
		
		
		

updateVolColour();


		if (mediaPlayer!=null)
	 mediaPlayer.setVolume(leftVolume, rightVolume);
		SMBDrawView.VOLpercent=leftVolume;
		imageViewVOL.invalidate();
}	
	
			else if (Math.abs(diffY)< Math.abs(diffX) && endX > startX) {  //  right
				//Toast.makeText(getApplicationContext(), "RIGHT", Toast.LENGTH_SHORT).show();	
	fileIndex++;  
		if (  (fileIndex>playableFiles.size()-1) //&& playableFiles.size()>0
				)
			{
			fileIndex=0;
			}
//Toast.makeText(getApplicationContext(), "RIGHT "+playableFiles.get(fileIndex),
//		Toast.LENGTH_SHORT).show();	
add_prefs_key(file_name, playableFiles.get(fileIndex));

restart();
			}
			else {
				fileIndex--;  
				if (  (fileIndex<0 && playableFiles.size()>0)) {
					fileIndex=playableFiles.size()-1;
				}
//				Toast.makeText(getApplicationContext(), "LEFT "+playableFiles.get(fileIndex),
//						Toast.LENGTH_SHORT).show();	
				add_prefs_key(file_name, playableFiles.get(fileIndex));
				restart();
			}
	  
		
		}
		
		
	
	}
		
		
		
		return false;
	}
	
	private void add_prefs_key(String string, String string2) {
		SharedPreferences.Editor editor = sharedpreferences.edit();  
		editor.putString(string, string2).apply();
	}
	
	private void restart(){
		if (mediaPlayer!=null){
			mediaPlayer.release();
			mediaPlayer=null;
		}
		
		Intent intent = getIntent();
		finish();
		startActivity(intent);	
		
	}
	
	private int getAudioIndex() {
		for (int i=0;i<playableFiles.size();i++){
			if (playableFiles.get(i).equalsIgnoreCase(Fname)){
				fileIndex=i;
//				Toast.makeText(getApplicationContext(), "INDEX= "+String.valueOf(fileIndex)+
//						"  "+playableFiles.get(fileIndex)
//						, Toast.LENGTH_SHORT).show();
				break;
			}
		}
		return fileIndex;
	}
	
	private void getAudioPlayableFiles (String Dir,String Fname){
	
		playableFiles.clear();
//		File Path = new File(Environment.getExternalStorageDirectory(), Dir);  //ok
//		File Path = new File(Dir);  //ok
		File[] files = new File(Dir).listFiles();
		
		for (int i=0;i<files.length;i++){
		
			String[] separated = files[i].toString().split("/");
			String correct_separated=separated[separated.length-1];	
			
			if (	correct_separated.toUpperCase().endsWith(".MP3") 
					|| correct_separated.toUpperCase().endsWith(".3GP")
					|| correct_separated.toUpperCase().endsWith(".WAV")			) 
			{
 	           	  playableFiles.add(correct_separated);  // file type is MP3 
 	              }
			}

	}
	
	@Override
	public void onBackPressed() {
		mp3Load.myCancel();
		add_prefs_key(volume,Float.toString(leftVolume));
		if (mediaPlayer!=null){
			mediaPlayer.release();
			mediaPlayer=null;
		}
		sensorManager.unregisterListener(SMBaudioPlay1.this);
		File PathAndr = new File(Environment.getExternalStorageDirectory(), 
				LOCALPATHANDROIDFIXED);
		if(PathAndr.exists()) {
			File[] ff=PathAndr.listFiles();
				for (int i=0;i<ff.length;i++){
					ff[i].delete();
							}		
			}	
		
//////		MP3FileLoadTask.cancel(true);
		
		finish();
		super.onBackPressed();
	}	
	
	public void initializeMediaPlayer1 (){
		mediaPlayer = new MediaPlayer();
    	mediaPlayer.setVolume(leftVolume, rightVolume);
    	 //String baseDir2 = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Kalimba.mp3" ;
//String baseDir2 = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"memo_files"+"/"+"Kalimba.mp3" ;
//Toast.makeText(getBaseContext(),baseDir2 , Toast.LENGTH_LONG).show();     	 		

//String filePath = Dir+File.separator+Fname ;
//File file = new File(Dir+File.separator+Fname);
//FileInputStream inputStream = null;
FileInputStream inputS=null;
try {		
//    OutputStream fos = openFileOutput("pp", Context.MODE_PRIVATE);   
inputS = openFileInput(PLAYFILESONG);
 //   inputStream = new FileInputStream(new File("pp"));
} catch (FileNotFoundException e2) {
	e2.printStackTrace();
}
//mediaPlayer.setDataSource(inputStream.getFD());

	 			        	 
    		 
    			try {
    				mediaPlayer.setDataSource(inputS.getFD());
					//mediaPlayer.setDataSource(baseDir2) ;
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			
    			try {
    				inputS.close();
    			} catch (IOException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}	
    			
    			try {
					mediaPlayer.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	

	

	
	

}
	
	private void setAnim(){
		anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(550); //You can manage the blinking time with this parameter
		anim.setStartOffset(20);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
			}
	
	private void initActionBar() {
		int separator=20;
		shuffleYesNo=sharedpreferences.getString(shuffleaudioplay, "");
		  tvab = new TextView(getApplicationContext());
		  LayoutParams lp = new RelativeLayout.LayoutParams(
		          LayoutParams.WRAP_CONTENT, // Width of TextView
		          LayoutParams.WRAP_CONTENT);
		  tvab.setLayoutParams(lp);
		  tvab.setTextColor(Color.RED);
		  tvab.setTextSize(20.0f);
		  tvab.setLines(1);
		  tvab.setHorizontallyScrolling(true);
		  tvab.setMarqueeRepeatLimit(-1);  //  At this point the view is not scrolling!
		  tvab.setSelected(true);
//		  Drawable drrback=(Drawable)getResources().getDrawable(R.drawable.ic_launcher);
//		  drrback.setBounds(0, 0, 80, 80);
//		  tvab.setCompoundDrawables(drrback, null, null, null);
		  Bitmap audioplay = BitmapFactory.decodeResource(getResources(), R.drawable.audioplayer2);
		  Bitmap shuffleon = BitmapFactory.decodeResource(getResources(), R.drawable.shuffleon);
		  Bitmap shuffleoff = BitmapFactory.decodeResource(getResources(), R.drawable.shuffleoff);

		  Bitmap b9=combineImages(audioplay, shuffleon,shuffleoff,separator);
		  Drawable drawable = new BitmapDrawable(SMBaudioPlay1.this.getResources(), b9);
		  drawable.setBounds(0, 0, 100+separator, 50);
		  tvab.setCompoundDrawables(drawable,null,null,null);	  
        // Set the text color of TextView to red
        // This line change the ActionBar title text color
        // Set the ActionBar display option
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		  ab.setCustomView(tvab);
	}

	public Bitmap combineImages(Bitmap audioplay, Bitmap shuffleon , Bitmap shuffleoff, int sep) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom 
	    Bitmap cs = null; 
	    Bitmap b21=null;
		  Bitmap b11 = Bitmap.createScaledBitmap(audioplay, 50, 50, false);
		  if (shuffleYesNo.equals("YES")) b21 = Bitmap.createScaledBitmap(shuffleon, 50, 50, false);
		  else b21 = Bitmap.createScaledBitmap(shuffleoff, 50, 50, false);
	    int width = 0, height = 0; 

width=sep+2*Math.max(b11.getWidth(),b21.getWidth());
height=Math.max(b11.getHeight(), b21.getHeight());
	    cs = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 

	    Canvas comboImage = new Canvas(cs); 

	    comboImage.drawBitmap(b11, 0f, 0f, null); 
	    comboImage.drawBitmap(b21, sep+b21.getWidth(), 0f, null); 

	    // this is an extra bit I added, just incase you want to save the new image somewhere and then return the location 
	    /*String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png"; 

	    OutputStream os = null; 
	    try { 
	      os = new FileOutputStream(loc + tmpImg); 
	      cs.compress(CompressFormat.PNG, 100, os); 
	    } catch(IOException e) { 
	      Log.e("combineImages", "problem combining images", e); 
	    }*/ 

	    return cs; 
	  } 
	
	
    public static void resizeText(TextView textView, int originalTextSize, int minTextSize) {
        final Paint paint = textView.getPaint();
        final int width = textView.getWidth();
        if (width == 0) return;
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, originalTextSize);
        float ratio = width / paint.measureText(textView.getText().toString());
        if (ratio <= 1.0f) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    Math.max(minTextSize, originalTextSize * ratio));
        }
    }
private void playIt(){
        		try {
					pauseButton.clearAnimation();
					timerValue.clearAnimation();
					startButton.setEnabled(false);
					pauseButton.setEnabled(true);
					clrButton.setEnabled(true);
							if(!isPaused) {
								isPaused=false;
								startButton.setText("");
								songDuration=mediaPlayer.getDuration();
								mediaPlayer.start();
								imageViewP.setImageResource(R.drawable.audioplay);
										}
							else {
								isPaused=false;
								startButton.setText("");
								mediaPlayer.seekTo(length);
								songDuration=mediaPlayer.getDuration();
								mediaPlayer.start();
								imageViewP.setImageResource(R.drawable.audioplay);
								}
							mediaPlayer.setOnCompletionListener(new OnCompletionListener(){

								// @Override
					            public void onCompletion(MediaPlayer arg0) {
					                   // File has ended !!! 
					           	Toast.makeText(getBaseContext(), "Media Completed with Success",
					           			Toast.LENGTH_SHORT).show();
					           	set_clear_actions();
					            }
					    });
							
							
						isAudioKilled=false;		
						startTime = SystemClock.uptimeMillis();
						customHandler.postDelayed(updateTimerThread, 0);
				} catch (IllegalStateException e) {
		           	Toast.makeText(getBaseContext(), "Media ERROR  "+e.getMessage(),
		           			Toast.LENGTH_SHORT).show();
					e.printStackTrace();
					finish();
				}
					 catch (Exception e) {
				           	Toast.makeText(getBaseContext(), "Media ERROR  "+e.getMessage(),
				           			Toast.LENGTH_SHORT).show();
				           	e.printStackTrace();
						finish();
					}
}	
private void pauseIt(){
	pauseButton.startAnimation(anim);
	timerValue.startAnimation(anim);
	startButton.setEnabled(true);
	pauseButton.setEnabled(false);
	clrButton.setEnabled(true);
	isPaused=true;
	isAudioKilled=true;
	startButton.setText("");
	mediaPlayer.pause();
	imageViewP.setImageResource(R.drawable.audiopause1);
	length=mediaPlayer.getCurrentPosition();
	timeSwapBuff += timeInMilliseconds;
	customHandler.removeCallbacks(updateTimerThread);}
		

public void updateVolColour(){
	
	tvvolStat.setText(String.valueOf((int)(leftVolume*100)   )+"%");
	tvvolStat.setBackgroundColor(Color.RED);
if (leftVolume<0.35)
	tvvolStat.setBackgroundColor(Color.GREEN);
else if (leftVolume<0.65)
	tvvolStat.setBackgroundColor(Color.YELLOW);

}


@Override
public void onSensorChanged(SensorEvent event) {
    if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
        getAccelerometer(event);
	
    }
	
}

private void getAccelerometer(SensorEvent event) {
    float[] values = event.values;
    // Movement
    float x = values[0];
//    float y = values[1];
//    float z = values[2];

//    float accelationSquareRoot = (x * x + y * y + z * z)
//            / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
    long actualTime = event.timestamp;
    
//    tv.setText("x= "+x+"  y= "+y+"   z=  "+z+" ACC "+accelationSquareRoot+  "\n\r  STATE=   "+states);
///////////////////	if (!isAudioKilled || isPaused)     
//    if (accelationSquareRoot >= 2) //
    if (shuffleYesNo.equals("YES") && !(!isAudioKilled || isPaused) )        
    {
        if (actualTime - lastUpdate < 200) {
            return;
        }

        if (states==0 && Math.abs(x)<1.2)  {
        	states=1;
 if (timerValue!=null) timerValue.setCompoundDrawablesWithIntrinsicBounds(null, null, drd1, null );
          	lastUpdate = actualTime;
        }
        
        if (states==1 && x>1.50 && x<6 )  {
        	states=2;
if (timerValue!=null) timerValue.setCompoundDrawablesWithIntrinsicBounds(null, null, drd2, null );
          	lastUpdate = actualTime;
        }
//        else {lastUpdate = actualTime;states=0;}
        if (states==2 && x<-1.50 && x>-6 )    {
        	states=3;
if (timerValue!=null) timerValue.setCompoundDrawablesWithIntrinsicBounds(null, null, drd3, null );
          	lastUpdate = actualTime;
        }
//        else {lastUpdate = actualTime;states=0;}
//        if (states==2 && x<-1.50 && x>-6 && Math.abs(y)<4.5)  {
//        	states=3;
if (timerValue!=null) timerValue.setCompoundDrawablesWithIntrinsicBounds(null, null, drd2, null );
//          	lastUpdate = actualTime;
//        }
        if (states==3 )  {
        	states=0;
if (timerValue!=null) timerValue.setCompoundDrawablesWithIntrinsicBounds(null, null, drd1, null );

          	lastUpdate = actualTime;
            Toast.makeText(this, "Device was shuffLLLed", Toast.LENGTH_SHORT).show();
        	fileIndex++;  
    		if (  (fileIndex>playableFiles.size()-1) //&& playableFiles.size()>0
    				)
    			{
    			fileIndex=0;
    			}
//    Toast.makeText(getApplicationContext(), "RIGHT "+playableFiles.get(fileIndex),
//    		Toast.LENGTH_SHORT).show();	
    add_prefs_key(file_name, playableFiles.get(fileIndex));

    restart();
        }
//        if (states==2 && x<-1.50 && x>-4)
//        {
//        	states=0;
//          	 tv.setCompoundDrawablesWithIntrinsicBounds(null, null,drd3 , null );
//	            lastUpdate = actualTime;
//        Toast.makeText(this, "Device was shuffLLLed", Toast.LENGTH_SHORT)
//                .show();  
//        }
        
        

//        if (color) {
//            tv.setBackgroundColor(Color.GREEN);
//        } else {
//            tv.setBackgroundColor(Color.RED);
//        }
//        color = !color;
    }
}

@Override
public void onAccuracyChanged(Sensor sensor, int accuracy) {
	
}

private void aldiag(String s, final boolean b17){
	
	AlertDialog.Builder builder1 = new AlertDialog.Builder(
		    SMBaudioPlay1.this);	
	builder1.setMessage(s);		
	builder1.setCancelable(true);
	builder1.setPositiveButton(
	    "OK",
	    new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) {
	        	if (b17){
	        		dialog.cancel();
	        		finish();		        	
	        	}
	        	else {
	        		dialog.cancel();
	            }
	            
	        }
	    });

	AlertDialog alert11 = builder1.create();
	alert11.show();
}

private void showLyricsCustomDialog (String Artist, String songName,String song){
	// custom dialog
	final Dialog dialog = new Dialog(context);
	dialog.setContentView(R.layout.smblyricscustom);
	dialog.setTitle(Artist+" -  "+songName);
	TextView textCustom = (TextView) dialog.findViewById(R.id.text);
	textCustom.setMovementMethod(new ScrollingMovementMethod());
	textCustom.setGravity(Gravity.CENTER_HORIZONTAL);	      

	textCustom.setText(song);
	ImageView image = (ImageView) dialog.findViewById(R.id.image);
	image.setImageResource(R.drawable.songlyrics70x75);

	Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
	dialogButton.setBackgroundColor(Color.YELLOW);
	dialogButton.setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			dialog.dismiss();
		}
	});

	dialog.show();
  }



private void getHtmlFromWeb() {
	final int FIXEDOFFSET=17;
    new Thread(new Runnable() {
       @Override
       public void run() {
try {
fromWeb="https://www.azlyrics.com/lyrics/had ouken/gets   mashedga      tecrash.html";
//fromWeb="https://www.azlyrics.com/lyrics/janisian/roses.html";
//fromWeb="https://www.azlyrics.com/lyrics/janisIan/betweenthelines.html";// all must be lower case
fromWeb="https://www.azlyrics.com/lyrics/Janis Ian/In the winter.html";// all must be lower case

String artist2=artist.replaceAll("\\s+","").toLowerCase(Locale.ENGLISH);
String artist1=cleanString(artist2);

String song_name2=song_name.replaceAll("\\s+","").toLowerCase(Locale.ENGLISH);
String song_name1=cleanString(song_name2);
fromWeb="https://www.azlyrics.com/lyrics/"+artist1+"/"+song_name1+".html";// all must be lower case

//String zz02=fromWeb.replaceAll("\\s+","").toLowerCase(Locale.ENGLISH);
//String x11111=cleanString();
//String zz42=st20.replaceAll("\\s+","").toLowerCase(Locale.ENGLISH);



//String zz01=zz02.toLowerCase(Locale.ENGLISH);
String i6767=Html.fromHtml(SMBGetFromInternetSiteLyrics1.getLyrics(fromWeb)).toString();

//int ind3=i6767.indexOf("A B C D E F G H I J K L M N O P Q R S T UV W X Y Z #");
int ind3=i6767.indexOf("Search")+"Search".length()+FIXEDOFFSET;
int ind1=i6767.indexOf("if ( /Android|webOS|iPhone|iPod|iPad|BlackBerry|IEMobile|Opera Mini/i.test");

k999=i6767.substring(ind3, ind1);
} catch (Exception e) {
k999="song not located";
e.printStackTrace();
}

//             https://www.azlyrics.com/lyrics/hadouken/getsmashedgatecrash.html
             
//             String title = doc.title();
//             Elements song=doc.select("div");
//             Elements links = doc.select("a[href]");
//             stringBuilder.append(title).append("\n");
             
//             for (int i=0;i<song.size();i++) {
//stringBuilderSong.append("\n").append("Line : ").append(song.get(i).attr("href")).append("\n").append("Text : ").append(song.get(i).text());
//	               }
//             
//             for (Element link : links) {
//                stringBuilder.append("\n").append("Link : ").append(link.attr("href")).append("\n").append("Text : ").append(link.text());
//             }
//          } 
//          catch (IOException e) {
//             stringBuilder.append("Error : ").append(e.getMessage()).append("\n");
//          }
          runOnUiThread(new Runnable() {
             @Override
             public void run() {
     			showLyricsCustomDialog(artist,song_name,k999.toString());

 ///////////////               textView.setText(k999.toString());
             }
          });
          }
    }).start();
 }

private String cleanString(String str){

int ccc;
char phraseArray[] = str.toCharArray(); 
for(int i=0; i< phraseArray.length; i++) {
        String value = Character.toString(phraseArray[i]); 
        ccc=phraseArray[i];
        if (ccc<97 || ccc>122){
        value = value.replace(value," "); 
        phraseArray[i] = value.charAt(0);
        }
}
//String st20= new String(phraseArray);
return new String(phraseArray).replaceAll("\\s+","");
}





/////// async task

class MP3FileLoadTask extends AsyncTask<Void, String, Void>    {
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
  InputStream  in=null;OutputStream out999 = null;
try {
//	    String pathSmb = "smb://" + ip + "//" + sharedFolder + sharedFile1;  // smb file
	    NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",username,password);
	    SmbFile smbFile = new SmbFile(url,auth2);
	    long lenghtOfFile=smbFile.length(); 
		File PathAndr = new File(Environment.getExternalStorageDirectory(), 
				LOCALPATHANDROIDFIXED);
		localPath=PathAndr.toString();  
		if(!PathAndr.exists()) {
			PathAndr.mkdirs();
			}
		
		String[] s0=url.split("/{2}");  // get fr0m net		
		File file = new File(PathAndr, s0[s0.length-1]);		
		localFname=s0[s0.length-1];
//		File source = new File(Path, AndroidFileName);//file is android file		
//		InputStream in = new FileInputStream(source);
//	    SmbFile source = new SmbFile("smb://username:password@a.b.c.d/sandbox/sambatosdcard.txt");
//	    File destination = new File(Environment.DIRECTORY_DOWNLOADS, "SambaCopy.txt");	
				
		in = smbFile.getInputStream();
	    out999 = new FileOutputStream(file);
	    
//	    OutputStream fos = openFileOutput(PLAYFILESONG, Context.MODE_PRIVATE);
	    
	    
	    
	    
	    byte[] buf = new byte[6*1024];
	    int len;
	    long total = 0;
	    while ((len = in.read(buf)) > 0) {
	    	total+=len;
			if (total%100==0) {
				publishProgress(""+(int)((total*100)/lenghtOfFile)+"%   ("+String.valueOf(total)+"/"+String.valueOf(lenghtOfFile)
					+")"	);
			}
			out999.write(buf, 0, len); //fos.write(buf,0,len);
				if (isCanceled){
				    in.close();
				    out999.flush();
				    out999.close();
				return null;
				}
	    }
	    in.close();
	    out999.flush();
	    out999.close();
//	    fos.flush();
//	    fos.close();

	} catch (MalformedURLException e) {
		lastErrormessage=e.toString();errors++;
	    e.printStackTrace();
	    try {
			in.close();
			out999.flush();
			out999.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
	} catch (IOException e) {
		lastErrormessage=e.toString();errors++;
	    e.printStackTrace();
	    try {
			in.close();
			out999.flush();
			out999.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		}
	catch (Exception e) {
		lastErrormessage=e.toString();errors++;
	    e.printStackTrace();
	    try {
			in.close();
			out999.flush();
			out999.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		} 

  return null;            
  
}

@Override
protected void onPostExecute(Void vd) {
  super.onPostExecute(vd);
   if (dialog.isShowing()) {
			dialog.dismiss();
     } 
  Dir=localPath;
 Fname=localFname;
 
 if (errors==0){
	 Toast.makeText(getBaseContext(), "DONE  OK  !!! ", Toast.LENGTH_SHORT).show();

	initMediaRetriever();
	initMediaretrievr1();
	updateTVSTAT();
	initializeMediaPlayer1 ();				
 	}
 else {
	 Toast.makeText(getBaseContext(), "Error:  "+lastErrormessage, Toast.LENGTH_SHORT).show();
finish();
	 
 }

      } 

protected void onProgressUpdate(String... values) {
//	dialog.setTitle("Downloading file");
	dialog.setMessage("progress "+values[0]);
//dialog.show();
}

}  // end of class












}


//tablet params table:  24 columns per row
//		tv1 		height,			margin height
//		tv2			height,			margin height
//		progress	height,			margin height
//		imageView 	height, 		margin height		width,		margin width
// 		play 		height, 		margin height		width,		margin width
//		pasue 		height, 		margin height		width,		margin width
//		CLR			height, 		margin height		width,		margin width
//		back		height, 		margin height





