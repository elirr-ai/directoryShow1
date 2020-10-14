package com.example.directoryshow1;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class audiorec1   extends Activity {
	private TextView tvTimeCounter,textHeader;   
	private EditText edFile; 
	private Button stop,record,back,enter;
	private ImageView imageView,circle;
	private View vv;
	private int height=0, width=0;
	
	   private MediaRecorder myAudioRecorder;
	   private String outputFile = null;
	   private String dname1,fnameNNN="AUDIO_";
	   private String recFname;
	   
	   boolean isRecording=false,updated_text_in_editText=false;
	   
	   private long startTime = 0L;
	    private Handler customHandler = new Handler();
	    long timeInMilliseconds = 0L;
	    long timeSwapBuff = 0L;
	    long updatedTime = 0L;
	   
//	    public getListofFileTypes getListFiles;
	    public dateFrom1970 df1970;
//	    ArrayList<String> RecFileList = new ArrayList<String>();
	    SharedPreferences sharedpreferences;
	    public static final String MyPREFERENCES = "MyPrefsGrid" ;  // my pref internal folder name
	    // keys
	       public static final String Initialized = "Initialized";
	       public static final String size_x = "size_x";
	       public static final String size_y = "size_y";
	       public static final String directory = "Directory";
	       public static final String file_name = "File_name";
	       public static final String sort_type = "sort";  // 0=no sort, 1 by name asc, 2 by name desc, 3 by date asc,  4 by name dsc
	       public static final String position_location = "POSIION";
	       public static final String sort_type_Main_Activity = "sort_Main_Act"; 	// 0=no sort, 1 by name asc, 2 by name desc, 3 by date asc,  4 by name dsc
	       public static final String myVeryFavouriteDir = "MYVeryFavouriteDir";
	       public static final String fitFullScreen = "FITFULL";
	       public static final String volume = "VOLUME";
	       public static final String ENCODING = "ENCODING";
	       public static final String GPP3GPP = "11",MPEG4MPEG4="22",THREEGPPAMRNB="13";
	       
	       
	       
	       
	   @Override
	   protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

	      setContentView(R.layout.audiorec2);
	      
//	      getListFiles=new getListofFileTypes();
	      df1970=new dateFrom1970();
	      
	      vv=(View)findViewById(R.id.vv);
	      textHeader=(TextView)findViewById(R.id.textView);
	      edFile=(EditText)findViewById(R.id.edFile);
	      tvTimeCounter=(TextView)findViewById(R.id.tv1);
	      imageView=(ImageView)findViewById(R.id.imageView);
	      circle=(ImageView)findViewById(R.id.circle);
	      record=(Button)findViewById(R.id.buttonStart);
	      enter=(Button)findViewById(R.id.enter);

	      
	      stop=(Button)findViewById(R.id.buttonStop);
	      back=(Button)findViewById(R.id.buttonExit);
	      
	      
	      dname1=sharedpreferences.getString(directory, "");
	      if (dname1.length()<2) dname1+="/AUDIO";
	      if (sharedpreferences.getString(ENCODING, "").equals("")){
	            SharedPreferences.Editor editor = sharedpreferences.edit();
	            editor.putString(ENCODING, "13").commit();
	      }
	      
	      
//     	 recFname =dname1+"_"+df1970.getmillisString()+".3gp";

	      recFname =dname1.substring(1, dname1.length())+"_"+df1970.getDateString()+".3gp";
     	
   
     	 edFile.setText(recFname);
	      
	      stop.setEnabled(false);
	      tvTimeCounter.setText("" + "00:00:00"  + "" );
	      
	      //outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
	      circle.setImageResource(R.drawable.green50x50);	
	      //Toast.makeText(getBaseContext(),"outputFile.... " +outputFile , Toast.LENGTH_SHORT).show();
	      
	      textHeader.setText("Audio recording:"
	    		  + " \n\rFolder path= "+dname1
	      		+ " \n\r"+"Format= "+getRECtype());
	      
	    //  RecFileList=getListFiles.getListofFiles(dname, fnameNN, ".JPG");
	      hide_kbd();
	      record.setOnClickListener(new View.OnClickListener() {
	         @Override
	         public void onClick(View v) {
	        	 prep_audio_rec();
//	        	 recFname =fnameNNN+"_"+df1970.getmillisString()+".3gp"; 
	        	  		     
if (!updated_text_in_editText)	        	 
	        	 edFile.setText(recFname);
updated_text_in_editText=false;
	        	 
	        	 outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() +
		 	    		  "/"+dname1+"/"+edFile.getText().toString();
		 	      myAudioRecorder.setOutputFile(outputFile);
	        	 isRecording=true;	  

	            try {
	            	circle.setImageResource(R.drawable.red50x50);
	               myAudioRecorder.prepare();
	               myAudioRecorder.start();
	            }
	            
	            catch (IllegalStateException e) {
	               e.printStackTrace();
	            }
	            
	            catch (IOException e) {
	               e.printStackTrace();
	            }
	            
	            record.setEnabled(false);
	            stop.setEnabled(true);
	            updated_text_in_editText=false;
	            Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
	            startTime = SystemClock.uptimeMillis();
	              customHandler.postDelayed(updateTimerThread, 0);
	         }
	      });
	      
	      stop.setOnClickListener(new View.OnClickListener() {
	         @Override
	         public void onClick(View v) {
///////////////////////////////
	        	 hide_kbd();
	        	 isRecording=false;
	            myAudioRecorder.stop();
	            myAudioRecorder.release();
	            myAudioRecorder  = null;
	            
	            stop.setEnabled(false);
	            record.setEnabled(true);
	            Toast.makeText(getApplicationContext(), "Audio recorded successfully",Toast.LENGTH_LONG).show();
	            timeSwapBuff = 0L;
	              customHandler.removeCallbacks(updateTimerThread);
	           	            circle.setImageResource(R.drawable.green50x50);
	    	            	circle.setVisibility(View.VISIBLE);
//////////
	         }
	      });
	      
	      
	      
	      enter.setOnClickListener(new View.OnClickListener() {
		         @Override
		         public void onClick(View v) {
		        	 hide_kbd();
		        	 updated_text_in_editText=true;
		        	 circle.setImageResource(R.drawable.green50x50);
		            	circle.setVisibility(View.VISIBLE);

		         }
		      });      
	      
	      back.setOnClickListener(new View.OnClickListener() {
		         @Override
		         public void onClick(View v) {
		     		if (isRecording) {
		    			if (myAudioRecorder!=null){
		    			
		           	 myAudioRecorder.stop();
		                myAudioRecorder.release();
		                myAudioRecorder  = null;
		                stop.setEnabled(false);
		      	            }
		    			}
		               Toast.makeText(getApplicationContext(), "Exiting... ",Toast.LENGTH_LONG).show();
		       		Intent intentMessage_a1=new Intent();
//		    	    // put the message to return as result in Intent
		            intentMessage_a1.putExtra("MESSAGE","back back !!!!!!");
//		    		
//		    		// Set The Result in Intent
		            setResult(9,intentMessage_a1);
		    		
		               
		               
		               audiorec1.this.finish();
		         }
		      });
	      
	      
	   }
	   
	   private void prep_audio_rec() {
		
		   myAudioRecorder=new MediaRecorder();
		      myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//		      myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4); //0x2
//		      myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.MPEG_4); //0x2
// 		      recFname =fnameNNN+"_"+df1970.getDateString()+".mp4";	

		      if (sharedpreferences.getString(ENCODING, "").equals("11")){
			      myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);  //0x1
			      myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.THREE_GPP); //0x1
			      recFname =fnameNNN+"_"+df1970.getDateString()+".3gp";		    	  
		      }
		      else if (sharedpreferences.getString(ENCODING, "").equals("22")){
			      myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  //0x2
			      myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.MPEG_4); //0x23
			      recFname =fnameNNN+"_"+df1970.getDateString()+".mp4";		    	  
		      }	      
		      
		      else { 
		      myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);  //0x1
		      myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB); //0x3
		      recFname =fnameNNN+"_"+df1970.getDateString()+".3gp";		      
		      }
		      
		      hide_kbd();
// valid: 3GPP/3GPP , MPEG4/MPEG4 , THREEGPP/AMR_NB

	   	   			}

	private Runnable updateTimerThread = new Runnable() {
	        public void run() {
		
	            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
	            updatedTime = timeSwapBuff + timeInMilliseconds;
	            int secs = (int) (updatedTime / 1000);
	            int mins = secs / 60;
	            secs = secs % 60;
	            int milliseconds = (int) (updatedTime % 1000);
	            tvTimeCounter.setText("" + mins + ":"
	            + String.format("%02d", secs) + ":"
	            + String.format("%03d", milliseconds));
	            if ((int) (updatedTime / 500) % 2==0 ) 
	            	circle.setVisibility(View.VISIBLE);
	            else circle.setVisibility(View.GONE);
	            
	           customHandler.postDelayed(this, 200);
	        }
			
	    };
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.audiorec100, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.set_3GPP) {
		            SharedPreferences.Editor editor = sharedpreferences.edit();
		            editor.putString(ENCODING, "11").commit();
		  	      textHeader.setText("Audio recording:"
			    		  + " \n\rFolder path= "+dname1
			      		+ " \n\r"+"Format= "+getRECtype());
			return true;
		}
		
		if (id == R.id.set_MPEG4) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(ENCODING, "22").commit();
  	      textHeader.setText("Audio recording:"
	    		  + " \n\rFolder path= "+dname1
	      		+ " \n\r"+"Format= "+getRECtype());
			return true;
		}
		
		if (id == R.id.set_3GPPAMR) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(ENCODING, "13").commit();
  	      textHeader.setText("Audio recording:"
	    		  + " \n\rFolder path= "+dname1
	      		+ " \n\r"+"Format= "+getRECtype());
			return true;
		}
		
		if (id == R.id.show) {
			showRECparams();
			return true;
		}	
		
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() 
	{
		if (isRecording) {
			if (myAudioRecorder!=null){
				myAudioRecorder.stop();
				myAudioRecorder.release();
				myAudioRecorder  = null;
				stop.setEnabled(false);
  	            }
			}
           Toast.makeText(getApplicationContext(), "Exiting... ",Toast.LENGTH_LONG).show();
   		Intent intentMessage_a1=new Intent();
//	    // put the message to return as result in Intent
        intentMessage_a1.putExtra("MESSAGE","back back !!!!!!");
//		// Set The Result in Intent
        setResult(9,intentMessage_a1);
		
           
           
           audiorec1.this.finish();
			}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		ViewGroup.MarginLayoutParams p;		
		if (width==0 || height==0){
		   height=vv.getHeight();
	         width=vv.getWidth();

//Toast.makeText(getBaseContext(),"HW.... " +width+" "+height , Toast.LENGTH_SHORT).show();

	        
p = 	(ViewGroup.MarginLayoutParams) textHeader.getLayoutParams();
		    		   p.setMargins(width/80,0,width/80,0);
		    		   p.height=height/10;
		    		   p.width=(width*60)/64;
		    		   textHeader.setTextSize(width/70.0f);
		    		   textHeader.requestLayout();	      
		       
		       
 p = (ViewGroup.MarginLayoutParams) edFile.getLayoutParams();
p.setMargins(width/80,height/60,width/80,0);
p.height=height/12;
p.width=(45*width)/64;
edFile.setTextSize(width/70.0f);
edFile.requestLayout();
       
 p = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
p.setMargins((width*20)/64,height/240,0,height/4);
p.height=height/3;
p.width=width/3;
imageView.requestLayout();        		              
  
p =(ViewGroup.MarginLayoutParams) circle.getLayoutParams();
p.setMargins((width*3)/64,height/240,0,height/2);
p.height=height/30;
p.width=width/30;
imageView.requestLayout();

p =(ViewGroup.MarginLayoutParams) tvTimeCounter.getLayoutParams();
p.setMargins(width/2-width/8,
		height/240,width/80,0);
p.height=height/20;
p.width=width/2;
tvTimeCounter.setTextSize(width/50.0f);
tvTimeCounter.requestLayout();
// 

p =(ViewGroup.MarginLayoutParams) record.getLayoutParams();
p.setMargins(width/80,height/240,width/80,height/10);
p.height=((height/4)*width)/height;
p.width=width/4;
record.requestLayout();////////  

 p = (ViewGroup.MarginLayoutParams) stop.getLayoutParams();
p.setMargins(width/80,height/240,width/80,height/10);
p.height=((height/4)*width)/height;
p.width=width/4;
stop.requestLayout();  


//        		           		
 p = (ViewGroup.MarginLayoutParams) back.getLayoutParams();
p.setMargins(2*width/64,height/40,2*width/64,height/30);
p.height=height/12;
p.width=(60*width)/64;
back.requestLayout(); 
/////////////////////////////////////////////////////////////////////
 p = 
(ViewGroup.MarginLayoutParams) enter.getLayoutParams();
//p.setMargins(width/80,height/240,width/80,0);
p.setMargins(width/30,height/60,width/80,0);
p.height=height/12;
p.width=(10*width)/64;




enter.requestLayout(); 
//////////////////////////////////////////////////////////////
//		       }
       
		       
		       
		       //exit.setWidth(hw/2);
//		       Toast.makeText(getApplicationContext(), "heightZZZZZ "+height+"/+"
//	        +width+"/"+bot+"/"+top+"/"+left+"/"+right
//			   		, Toast.LENGTH_LONG).show(); 
		}
		super.onWindowFocusChanged(hasFocus);
	}
	
	private void hide_kbd() {
		// TODO Auto-generated method stub

this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
imm.hideSoftInputFromWindow(edFile.getWindowToken(), 0);


	}	
	
	
	private void showRECparams(){

			      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			      alertDialogBuilder.setTitle("Audio Rec params");
			      alertDialogBuilder.setMessage("REC= "+getRECtype());
			      alertDialogBuilder.setPositiveButton("yes", 
			         new DialogInterface.OnClickListener() {
			         @Override
			         public void onClick(DialogInterface arg0, int arg1) {
			            Toast.makeText(audiorec1.this,"You clicked yes button",Toast.LENGTH_LONG).show();
			         }
			      });



			      AlertDialog alertDialog = alertDialogBuilder.create();
			      alertDialog.show();
			   }
		
	private String getRECtype(){
		String s =null;
		if (sharedpreferences.getString(ENCODING, "").equals("11")){
			s="3GPP_3GPP";
		}
		else if (sharedpreferences.getString(ENCODING, "").equals("22")){
			s="MPEG4_MPEG4";
		}  
		else {
			s="3GPP_AMR_NB";
			
		}
		return s;
	}
	
	
}
