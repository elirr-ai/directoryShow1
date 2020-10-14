package com.example.directoryshow1;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class SMBshowText1 extends Activity implements View.OnTouchListener  {
	
	String url,username,password;
	private ActionBar actionBar;
	static ImageView iv;
	TextView tv1,tv_note,tv10;
	EditText ed1;
	boolean enableEditText=false;
	
	int cursorIndex=0,ci=0;
	boolean show_hide_kbd=false;
	
    int height;
    int width;
    int bot;
    int top;
    int left;
    int right;
	View view1,view2,view3,view4;
	//KeyListener mKeyListener;
	float eventX,eventY;
	
	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "MyPrefsGridSMBTEXT" ;  // my pref internal folder name
    
    // keys
    public static final String Initialized = "Initialized";
    public static final String size_x = "size_x";
    public static final String size_y = "size_y";
    public static final String directory = "Directory";
    public static final String file_name = "File_name";
    public static final String sort_type = "sort";  // 0=no sort, 1 by name asc, 2 by name desc, 3 by date asc,  4 by name dsc
    public static final String screenHeight = "scx";
    public static final String screenWidth = "scy";
    public static final String editRead = "editRead";//false is read only
    public static final String initfocused = "initfocused";
    public static final String FONT = "FONT";
    
    String size_x_="4",size_y_="4"; 	  // matrix of images size to be stored in preferenecs 
	static String Dir="";
	static String Fname="";
	String read_string="";	
	int countEdit=0;
	int mWidthScreen,mHeightScreen;
	int fontSize=1;
	RelativeLayout layout1;
	SMBDrawViewTXT imageViewVOL;
	ScrollView scroller;
	private float[] fonts_size = new float[]{10,12,16,18,20,22,25,27,30,33,35,37,40,45};
	SMBGetScreenHieghtWidth g1;
	Menu menu1;
//	FileReadWriteTextSMB FRWT;
	Context context=this;
	
	private boolean showHideKeyboardFlag=false;
	private AsyncTaskReadText Rdtext;
	
	@Override
	  public void onCreate(Bundle bundle) {
	    super.onCreate(bundle);
	    
	    Display disp= getWindowManager().getDefaultDisplay();
	    SMBGetScreenHieghtWidth g1 = new SMBGetScreenHieghtWidth(disp);
		mWidthScreen=g1.getmWidthScreen();
	    mHeightScreen=g1.getmHeightScreen();
		
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		read_all_prefs(); 
		SharedPreferences.Editor editor = sharedpreferences.edit();
		editor.putBoolean(editRead, false).commit();//false is read only
		editor.putBoolean(initfocused, false).commit();
		
	    Intent intent = getIntent();
	    url=intent.getStringExtra("url");
	    username=intent.getStringExtra("username");    
	    password=intent.getStringExtra("password");
		
		layout1 = new RelativeLayout(this);
        layout1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        setContentView(layout1);
        Toast.makeText(getBaseContext(), "Read only !!!.", Toast.LENGTH_SHORT).show();

	}
	
	private void set_view_read_only() {
	
		height	= Integer.parseInt(sharedpreferences.getString(screenHeight, ""));
		width	= Integer.parseInt(sharedpreferences.getString(screenWidth, ""));
		tv1 = new TextView(this);
        RelativeLayout.LayoutParams params_tv1 = 
        		new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
                LayoutParams.WRAP_CONTENT);
         
        tv1.setLayoutParams(params_tv1);
        tv1.setId(1004);
        //params.addRule(RelativeLayout.LEFT_OF, 1001); 
        params_tv1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params_tv1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params_tv1.setMargins(0, 0, 0, 0);  
        params_tv1.height= (height*6)/64;
        tv1.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        tv1.setHint("txt.");
        tv1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,width/72);
        tv1.setTextColor(Color.BLUE);
        tv1.setBackgroundColor(Color.GREEN);
        layout1.addView(tv1);
        tv1.setOnTouchListener(this);
//        tv1.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ap72x72, 0, R.drawable.audioplay, 0);
 //       Bitmap b = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
  //      		profileImage.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));

        Drawable m1 = getResources().getDrawable( R.drawable.backfwd );
        Bitmap bmp = ((BitmapDrawable)m1).getBitmap();
        Drawable d = new BitmapDrawable(getResources(), bmp);
        d.setBounds(0,0,width/4,(height*4)/64);
               
        
        Drawable m2 = getResources().getDrawable( R.drawable.updown );
        Bitmap bmp1 = ((BitmapDrawable)m2).getBitmap();
        Drawable d1 = new BitmapDrawable(getResources(), bmp1);
        d1.setBounds(0,0,width/4,(height*4)/64);
 
        Drawable m3 = getResources().getDrawable( R.drawable.keyboardimage );
        Bitmap bmp2 = ((BitmapDrawable)m3).getBitmap();
        Drawable d2 = new BitmapDrawable(getResources(), bmp2);
        d2.setBounds(0,0,width/4,(height*2)/64);
        tv1.setCompoundDrawables(d, null, d1, d2);

        ///////////////////////////////////
        tv_note = new TextView(this);
        RelativeLayout.LayoutParams params_tv_note = 
        		new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
                LayoutParams.WRAP_CONTENT);
        tv_note.setLayoutParams(params_tv_note);
        tv_note.setId(1011);
        params_tv_note.addRule(RelativeLayout.BELOW,1004);
        params_tv_note.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params_tv_note.height= (height*238)/256;
        params_tv_note.width= (width-16);
        params_tv_note.setMargins( 	8, 10 ,	8, 8);  
        tv_note.setTextSize(TypedValue.COMPLEX_UNIT_DIP,width/52);
        tv_note.setGravity(Gravity.TOP | Gravity.LEFT);
        tv_note.setBackgroundColor(Color.YELLOW);
        tv_note.setHint("STATUS ..");
        tv_note.setMovementMethod(new ScrollingMovementMethod());
        layout1.addView(tv_note);
    	view1=new View (getBaseContext());
        RelativeLayout.LayoutParams params_view1 = 
        		new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT);
        view1.setLayoutParams(params_view1);
        view1.setId(999);
        params_view1.height=height/480;
        params_view1.width=width;
        params_view1.setMargins(0,10	 , 0, 0);
        params_view1.addRule(RelativeLayout.BELOW, 1004);
        params_view1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        view1.setBackgroundColor(Color.RED);
        layout1.addView(view1,params_view1);
        
    	view2=new View (getBaseContext());
        RelativeLayout.LayoutParams params_view2 = 
        		new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT);
        view2.setLayoutParams(params_view2);
        view2.setId(998);
        params_view2.height=((height*60)/64)+(height/480) ;
        params_view2.width=5;
        params_view2.setMargins(0,0	 , 0, 0);
        params_view2.addRule(RelativeLayout.BELOW, 999);
        params_view2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        view2.setBackgroundColor(Color.RED);
        layout1.addView(view2,params_view2);
        
    	view3=new View (getBaseContext());
        RelativeLayout.LayoutParams params_view3 = 
        		new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT);
        view3.setLayoutParams(params_view3);
        view3.setId(997);
        params_view3.height=((height*60)/64)+(height/480) ;
        params_view3.width=5;
        params_view3.setMargins(width-6,0	 , 0, 0);
        params_view3.addRule(RelativeLayout.BELOW, 999);
        params_view3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        view3.setBackgroundColor(Color.RED);
        layout1.addView(view3,params_view3);
 
        view4=new View (getBaseContext());
        RelativeLayout.LayoutParams params_view4 = 
        		new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
                LayoutParams.WRAP_CONTENT);
        view4.setLayoutParams(params_view4);
        view4.setId(996);
        params_view4.height=height=height/480; ;
        params_view4.width=width;
        params_view4.setMargins(0,0	 , 0, 0);
        params_view4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params_view4.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        view4.setBackgroundColor(Color.RED);
        layout1.addView(view4,params_view4);
	}

	private void set_view_edit() {
		height	= Integer.parseInt(sharedpreferences.getString(screenHeight, ""));
		width	= Integer.parseInt(sharedpreferences.getString(screenWidth, ""));
        ed1 = new EditText(this);
        RelativeLayout.LayoutParams params_ed1 = 
        		new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
                LayoutParams.WRAP_CONTENT);
        ed1.setLayoutParams(params_ed1);
        ed1.setId(805);
        params_ed1.addRule(RelativeLayout.BELOW,1004);
        params_ed1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params_ed1.height= (height*238)/256;
        params_ed1.width= (width-16);
        params_ed1.setMargins( 	8, 10 ,	8, 8); 
        ed1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,width/52);
        ed1.setGravity(Gravity.TOP | Gravity.LEFT);
        ed1.setHint("STATUS   Enter some text....");
        ed1.setMovementMethod(new ScrollingMovementMethod());
        String PACKAGE_NAME = getApplicationContext().getPackageName();
	    
	    	//int resid_ = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+"note4" , null, null);
	   
        ed1.setBackgroundResource(getResources().getIdentifier(PACKAGE_NAME+":drawable/"+"note4" , null, null));
        ed1.setEnabled(true);
        layout1.addView(ed1,params_ed1);


        hide_kbd();
	}
	
	private void hide_kbd() {

this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
imm.hideSoftInputFromWindow(ed1.getWindowToken(), 0);

	}
	
	private void read_all_prefs() {
		Dir	= sharedpreferences.getString(directory, "");
		Fname	= sharedpreferences.getString(file_name, "");
		if (sharedpreferences.getString(FONT, "").equals("") || sharedpreferences.getString(FONT, "").length()==0){
			SharedPreferences.Editor editor = sharedpreferences.edit();
			editor.putString(FONT, "4").commit();
		}
		fontSize=Integer.valueOf(sharedpreferences.getString(FONT, ""));
					}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.smbshowtext1, menu);
		menu1=menu;
		menu1.getItem(1).setVisible(false);		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		
		case R.id.save:
			if (countEdit>0) {			
				if (!enableEditText) {
				Toast.makeText(getApplicationContext(), "READ ONLY MODE - nothing saved ...", Toast.LENGTH_SHORT).show();
			}
			else{ 
				new AsyncTaskWriteText().execute(url);
							}
				}
			if (countEdit==0) {
				Toast.makeText(getApplicationContext(), "READ ONLY MODE - nothing saved ...", Toast.LENGTH_SHORT).show();
			}			
			
					break;						
					
					
		case R.id.edit:
			enableEditText=!enableEditText;
			SharedPreferences.Editor editor = sharedpreferences.edit();
			editor.putBoolean(editRead, enableEditText).commit();			
			countEdit++;
			enableEditText=sharedpreferences.getBoolean(editRead, false);
				if (enableEditText)	{
					
					if (tv_note!=null) layout1.removeView(tv_note); 
					set_view_edit();
					show_tv1_text();
					if (!enableEditText) {
						tv_note.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fonts_size[fontSize]);
							}
					else {
						ed1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fonts_size[fontSize]);
					}
					Toast.makeText(getApplicationContext(), "Switching to EDIT MODE ...."
							+height
							, Toast.LENGTH_SHORT).show(); 
					actionBar.setTitle("Text View (EDIT) ");
					ed1.setText(read_string);	  
					try {
				        java.lang.reflect.Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
				        f.setAccessible(true);
				        f.set(ed1, R.drawable.cursoret);
				    } catch (Exception ignored) {
				    	String s=ignored.getMessage();
				    }
					ed1.requestFocus();
				    ed1.setSelection(cursorIndex);
					//File Path = new File(Environment.getExternalStorageDirectory(), Dir);
					show_tv1_text();
					menu1.getItem(1).setVisible(true);
						}// end of enable edit yes
				
				else {
					if (ed1!=null) {
						hide_kbd();
						read_string=ed1.getText().toString();
						layout1.removeView(ed1); 
							}
					set_view_read_only();
					Toast.makeText(getApplicationContext(),
							"Switching to READ ONLY MODE ...."+height,
							Toast.LENGTH_SHORT).show(); 
					actionBar.setTitle("Text View (READ ONLY) ");
//					file_read (Dir, Fname);
					tv_note.setText(read_string);
					show_tv1_text();
					//Toast.makeText(getApplicationContext(), "Alarm size= "+Integer.toString(fontSize), Toast.LENGTH_SHORT).show(); 
					
					if (!enableEditText) {
						tv_note.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fonts_size[fontSize]);
							}
					else {
						ed1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fonts_size[fontSize]);
					}
					menu1.getItem(1).setVisible(false);
				}

					break;		
					
				
		case R.id.exit:
			Intent intentMessage_1=new Intent();
	        intentMessage_1.putExtra("MESSAGE","back back !!!!!!");
	        setResult(6,intentMessage_1);  
			SMBshowText1.this.finish();		
			break;					
					
		case R.id.font:
			changeFont();
			break;
			
		case R.id.pretty:
			break;

		case R.id.pdf:
//		PDFWriteText pw=new PDFWriteText(context,Dir, Fname, tv_note.getText().toString());	
//		pw.MakePdfFile();
			break;
					
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void changeFont() {
		fontSize++;
		if (fontSize>fonts_size.length-1){
			fontSize=0;
		}
		show_tv1_text();
		//Toast.makeText(getApplicationContext(), "Alarm size= "+Integer.toString(fontSize), Toast.LENGTH_SHORT).show(); 
		
		if (!enableEditText) {
			tv_note.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fonts_size[fontSize]);
				}
		else {
			ed1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fonts_size[fontSize]);
		}
		SharedPreferences.Editor editor = sharedpreferences.edit();
		editor.putString(FONT, String.valueOf(fontSize)).commit();
	}

	@Override
	public void onBackPressed() 	{
		Rdtext.makeCanceled(true);
		Rdtext.cancel(true);
		Intent intentMessage_1=new Intent();
        intentMessage_1.putExtra("MESSAGE","back back !!!!!!");
        setResult(6,intentMessage_1);  
		SMBshowText1.this.finish();		
		}
	
	
	
	private void show_tv1_text() {
//		File Path = new File(Environment.getExternalStorageDirectory(), Dir);
//		tv1.setText("Directory= "+Path.toString()+"\n"+"File= "+Fname+" "+"   Font= "+
//				Integer.toString(fontSize+1)+" of "+String.valueOf(fonts_size.length));
		tv1.setText("File= "+Fname);
	
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
	public void onWindowFocusChanged(boolean hasFocus) {
if (sharedpreferences.getBoolean(initfocused, false)){ //already init do nothing
	
}
else {
        height=layout1.getHeight();
        width=layout1.getWidth();
        bot=layout1.getBottom();
        top=layout1.getTop();
        left=layout1.getLeft();
        right=layout1.getRight();

SharedPreferences.Editor editor = sharedpreferences.edit();
editor.putString(screenHeight,String.valueOf(height)).commit();
editor.putString(screenWidth, String.valueOf(width)).commit();
editor.putBoolean(editRead, false).commit();
editor.putBoolean(initfocused, true).commit();
set_view_read_only();

//AsyncTaskReadText asr =new AsyncTaskReadText(url);
//asr.execute("");

//				new AsyncTaskReadText().execute(url);
				Rdtext =new AsyncTaskReadText();
				Rdtext.execute(url);
				
				
tv_note.setText(read_string);
show_tv1_text();	       
	        actionBar = getActionBar();
			actionBar.setIcon(R.drawable.viewimage);
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setTitle("Text View (READ ONLY) ");

			show_tv1_text();
			//Toast.makeText(getApplicationContext(), "Alarm size= "+Integer.toString(fontSize), Toast.LENGTH_SHORT).show(); 
			
			if (!enableEditText) {
				tv_note.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fonts_size[fontSize]);
					}
			else {
				ed1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fonts_size[fontSize]);
			}
		////////////////
	}
			
		super.onWindowFocusChanged(hasFocus);
	}

@Override
public boolean onTouch(View v, MotionEvent event) {

	  int action = event.getAction() & MotionEvent.ACTION_MASK;
	  eventY=event.getY(); eventX=event.getX();
	  if (v.getId()==1004){
		  	  switch(action) {
	   case MotionEvent.ACTION_DOWN : {
	       
	     break;
	   }
	   case MotionEvent.ACTION_MOVE : {
	     break;
  }
	  case MotionEvent.ACTION_UP : {
//		  changeFont();
		  if (enableEditText && (int)eventX>0 && eventX<width/8){// LEFT			  
			  Toast.makeText(getBaseContext(), "up   A !!!. "+v.getId(), Toast.LENGTH_SHORT).show();
			  int q1=SMBSetCursorForEditText.setCursor(ed1.getText().toString(),cursorIndex, -1);
			  if (q1>=0){ 
				  ed1.setSelection(q1);
			  cursorIndex=q1;
			  	}
			  }
        
		  else  if (enableEditText &&   (int)eventX>width/8 && eventX<2*width/8){// RIGHT 

		        Toast.makeText(getBaseContext(), "up   B !!!. "+v.getId(), Toast.LENGTH_SHORT).show();
		        int q1=SMBSetCursorForEditText.setCursor(ed1.getText().toString(),cursorIndex, 1);
		        if (q1>=0) {ed1.setSelection(q1);
		        cursorIndex=q1;}
		  
		  }
		  
		  else  if (enableEditText && (int)eventX>6*width/8 && eventX<7*width/8){//DOWN
			  int q1=SMBSetCursorForEditText.setCursor(ed1.getText().toString(),cursorIndex, 11);
			  if (q1>=0) {
				  ed1.setSelection(q1);
				  cursorIndex=q1;
			  }
			  
			  Toast.makeText(getBaseContext(), "up   C !!!. "+v.getId(), Toast.LENGTH_SHORT).show();
		  }
		  
		  
		  else  if (enableEditText && (int)eventX>7*width/8 && eventX<8*width/8){//UP

			  int q1=SMBSetCursorForEditText.setCursor(ed1.getText().toString(),cursorIndex, -11);
			  if (q1>=0){ ed1.setSelection(q1);
			  cursorIndex=q1;}
			  Toast.makeText(getBaseContext(), "up   D !!!. "+v.getId(), Toast.LENGTH_SHORT).show();
		  }
		  
		  else  {// show hide keyboard
			  Toast.makeText(getBaseContext(), "up   NONE !!!. "+v.getId(), Toast.LENGTH_SHORT).show();
	
			  if (enableEditText){
				  show_hide_kbd=!show_hide_kbd;
				  if (show_hide_kbd){ 
					  showSoftKeyboard(ed1);
					  }
				  else {
						if (showHideKeyboardFlag){
							SMBSetCursorForEditText.setCursor(ed1.getText().toString(),cursorIndex, 0);// NOP, just create mid cursor
							cursorIndex=SMBSetCursorForEditText.midCursorPosition;
							ed1.setSelection(cursorIndex);
							}
					  hideSoftKeyboard(ed1);
				  	}
			  } 
			  
		  }
		  
		  break;
	  }
	 }
}
	  return true;
	  }  // ontouch
	
	

public void hideSoftKeyboard(View view) {
	showHideKeyboardFlag=false;
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
}

public void showSoftKeyboard(View view) {
    if (view.requestFocus()) {
		showHideKeyboardFlag=true;
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }
}



private class AsyncTaskReadText extends AsyncTask<String, String, String> {
ProgressDialog dialog;
boolean b=false;

public void makeCanceled(boolean b1){
	this.b=b1;
}

		@Override
	    protected void onPreExecute() {
			dialog = new ProgressDialog(context);
			dialog.setMessage("Loading....   please wait....");
			dialog.show();
	       super.onPreExecute();
	    }
	    @Override
	    protected String doInBackground(String... strings) {
	    	String text100 = null;
	    	long lenghtOfFile = 0;
	    	long total = 0;
	   	    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, username, password);
			SmbFileInputStream fis = null;
			try {
				SmbFile	file = new SmbFile(strings[0], auth);
			    lenghtOfFile=file.length(); 
				fis = new SmbFileInputStream (file);
			} catch (SmbException e1) {
				e1.printStackTrace();
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
			InputStreamReader isr = new InputStreamReader(fis);
			char[] data=new char[256];  // data is char array with size=100
			String final_data="";
			int size;					
				try {
					while(  ( size=isr.read(data))>0 )	{
					String read_data=String.copyValueOf(data, 0, size); 
					final_data+=read_data;	
					total += size;
					if (total%100==0) {
						publishProgress(""+(int)((total*100)/lenghtOfFile)+"%   ("+String.valueOf(total)+"/"+String.valueOf(lenghtOfFile)
							+")"	);
					}
					
					data = new char[256];
					text100=final_data;
					if (b) return "";
					}										
				} catch (IOException e) {
					e.printStackTrace();
					}
	       return text100;
	    }
	    
	    protected void onProgressUpdate(String... values) {
//	    	dialog.setTitle("Downloading file");
	    	dialog.setMessage("progress "+values[0]);
	    //dialog.show();
	    }
	    
	    
	    @Override
	    protected void onPostExecute(String string2) {
	       super.onPostExecute(string2);
	       if (dialog.isShowing()) {
				dialog.dismiss();
	     } 
			read_string=string2;
			if (!enableEditText) {
				tv_note.setText(string2);
				tv_note.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fonts_size[fontSize]);
					}
			else {
				ed1.setText(string2);
				ed1.setTextSize(TypedValue.COMPLEX_UNIT_DIP,fonts_size[fontSize]);
			}
	       }
	    }
	 



private class AsyncTaskWriteText extends AsyncTask<String, Void, Void> {
//	 String url;
//		public AsyncTaskWriteText(String url1) {
//		url=url1;
//		}
		@Override
	    protected void onPreExecute() {
	       super.onPreExecute();
	    }
	    @Override
	    protected Void doInBackground(String... strings) {
	   	    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, username, password);
			try {
				SmbFile	file = new SmbFile(strings[0], auth);
			    SmbFileOutputStream out= new SmbFileOutputStream(file);
			    out.write(ed1.getText().toString().getBytes());
			    out.flush();
			    out.close();
			} catch(Exception e11) {
				e11.printStackTrace();
			}
	       return null;
	    }
	    @Override
	    protected void onPostExecute(Void v) {
	       super.onPostExecute(v);
			Toast.makeText(getApplicationContext(), "Saved...", Toast.LENGTH_SHORT).show();
	       }
	    }



}


	

	
	

