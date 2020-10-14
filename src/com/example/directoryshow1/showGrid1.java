package com.example.directoryshow1;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;



public class showGrid1 extends Activity implements OnTouchListener{

	
		Context context1=this;
		Activity activity=this;
		final int TOP_ID = 3;
	    final int BOTTOM_ID = 4;
	    private static final int ROWS_ = 20;
		private static final int COLS_ = 20;
		private float startY,startX;
		
		SharedPreferences sharedpreferences;
		public static final String MyPREFERENCES = "MyPrefsGrid" ;  // my pref internal folder name
	    
	    // keysdown
		
		
	    public static final String Initialized = "Initialized";
	    public static final String size_x = "size_x";
	    public static final String size_y = "size_y";
	    public static final String directory = "Directory";
	    public static final String file_name = "File_name";
	    public static final String sort_type = "sort";  // 0=no sort, 1 by name asc, 2 by name desc, 3 by date asc,  4 by name dsc
	   
		//  set of params for  4X4 ///////////////////////////////////////////
		String size_x_="4",size_y_="4"; 	  // matrix of images size to be stored in preferenecs 
		String Dir="",Fname="",sort_="";
		  int Height_=105,Width_=105;				// thumbnail XxY size
		  int tablet_width,tablet_height;
		  double [ ]  coef = { 3, 4.15 , 5.5 , 6.9 , 8.4 , 10 };
	    int margin_right=10,margin_left=10;
		  
		ArrayList<String> filex = new ArrayList<String>();
		ArrayList<String> filex_date = new ArrayList<String>();
		File[] files;
		File Path;
		int page=0;
		private ActionBar actionBar;
		ProgressBar progressBar;
		TextView tv1,tv2,tv3;
		Button[][] buttons_array = new Button[ROWS_][COLS_];
		ArrayList<Bitmap> bitmapArray = new ArrayList<Bitmap>();
		String[] sort_types = {"No sort ", "Title Ascending ","Title Descending " ,"Date Ascending ","Date Descending "}; 
		RelativeLayout layout1;
		Button but1;
		OurTask task;
		GetScreenHieghtWidth g1;
		Context context=this;
		boolean inTaskProgerss=false;
		Bitmap bitmap,bitmap1;
		int heightTvPb;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			//setContentView(R.layout.activity_main);
			
			//path = Environment.getExternalStorageDirectory()+ "/FileInputOutput/img1.jpg"; 
			Display disp= getWindowManager().getDefaultDisplay();
			GetScreenHieghtWidth g1 = new GetScreenHieghtWidth(disp);
			tablet_width=g1.getmWidthScreen();
			tablet_height=g1.getmHeightScreen();
			
			
			//Point size = new Point();
			//disp.getSize(size);
			//tablet_width = size.x;
			//tablet_height = size.y;
			
//			Toast.makeText(getApplicationContext(), "TABLET =  "+Integer.toString(tablet_width)+"  "+Integer.toString(tablet_height), Toast.LENGTH_SHORT).show();
			
			
			
			sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
			//String data = sharedpreferences.getString("Initialized", "");
			//if (data.equalsIgnoreCase("")) {
			//	Toast.makeText(getApplicationContext(), "String not found!!!!", Toast.LENGTH_SHORT).show();
			//init_all_prefs();		
			//}
			read_all_prefs();    
			
			
			actionBar = getActionBar();
			actionBar.setIcon(R.drawable.viewimage);
			actionBar.setDisplayShowTitleEnabled(true);
			//actionBar.setTitle("View "+Integer.toString(files.length)+" "+Integer.toString(filex.size()) );
			actionBar.setTitle("View ");
						
	//		Toast.makeText(getApplicationContext(), "ACT2: "+Dir, Toast.LENGTH_SHORT).show();
			
			layout1 = new RelativeLayout(this);
	        layout1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
//	        layout1.setOnTouchListener(this);
	        layout1.setId(8000);
	        set_all_views();
	        
		    setContentView(layout1);
	        hide_kbd();
	        
	        task = new OurTask(activity);
            task.execute();
            
	        //populate_bitmap_array();
	        //page=0;
			//sort("1");
			//populate_all_thmbnails_with_images(page);
	        
	        //actionBar.setTitle("View "+Integer.toString(files.length)+" "+Integer.toString(filex.size()) );
	        //hide_kbd();
	        //update_all_text_views();
	        //////////////
	        
			//populate_all_thmbnails_with_images(page);
//			Toast.makeText(getApplicationContext(), "Previous Page "+Integer.toString(page), Toast.LENGTH_SHORT).show();
			actionBar.setTitle("View "+Integer.toString(page)+" "+Integer.toString(filex.size()) );
			//update_all_text_views();
	        ///////////////////////
			
			/*
			
			//setContentView(R.layout.activity_main);
			// Creating a new RelativeLayout
	        RelativeLayout relativeLayout = new RelativeLayout(this);

	        // Defining the RelativeLayout layout parameters.
	        // In this case I want to fill its parent
	        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
	                RelativeLayout.LayoutParams.FILL_PARENT,
	                RelativeLayout.LayoutParams.FILL_PARENT);

	        // Creating a new TextView
	        TextView tv = new TextView(this);
	        tv.setText("Test");

	        // Defining the layout parameters of the TextView
	        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
	                RelativeLayout.LayoutParams.WRAP_CONTENT,
	                RelativeLayout.LayoutParams.WRAP_CONTENT);
	        lp.addRule(RelativeLayout.CENTER_IN_PARENT);

	        // Setting the parameters on the TextView
	        tv.setLayoutParams(lp);

	        // Adding the TextView to the RelativeLayout as a child
	        relativeLayout.addView(tv);

	        // Setting the RelativeLayout as our content view
	        setContentView(relativeLayout, rlp);
			
			
			*/
			
			
		}

	//////////////////////////////
		
		
		
		
	//////////////////////////////
		
		private void set_all_views() {
			// TODO Auto-generated method stub
			//////////////////////////////////////////////////
			//ed = new EditText(this);
	        //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
	        //        LayoutParams.WRAP_CONTENT);
	        //params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        // use same id as defined when adding the button
	        
	        //ed.setLayoutParams(params);
	        //ed.setId(1000);
	        //params.addRule(RelativeLayout.LEFT_OF, 1001); 
	        //params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	        //params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	       // ed.setGravity(Gravity.BOTTOM | Gravity.CENTER);
	        //ed.setHint("Enter some text....");
	        //layout1.addView(ed);
	        heightTvPb=tablet_height/45;
	        int factorFontSize=80;
	        
	        
	        tv1 = new TextView(this);
	        RelativeLayout.LayoutParams params_tv1 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
	        		heightTvPb);
	        //params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        // use same id as defined when adding the button
	        
	        tv1.setLayoutParams(params_tv1);
	        tv1.setId(1004);
	        //params.addRule(RelativeLayout.LEFT_OF, 1001); 
	        //params_tv1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	        params_tv1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	        params_tv1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        params_tv1.setMargins( 	0, heightTvPb ,	0, 0);  
	        
	       tv1.setGravity(Gravity.BOTTOM | Gravity.CENTER);
	        tv1.setHint("STATUS   Enter some text....");
	        tv1.setTextSize(22.0f);
	        tv1.setTextColor(Color.BLACK);
	        tv1.setBackgroundResource(R.drawable.textview_border);
	        tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX,(heightTvPb*factorFontSize/100));
	        layout1.addView(tv1);
	               
	        tv2 = new TextView(this);
	        RelativeLayout.LayoutParams params_tv2 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
	        		heightTvPb);
	        //params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        // use same id as defined when adding the button
	        
	        tv2.setLayoutParams(params_tv2);
	        tv2.setId(1005);
	        //params.addRule(RelativeLayout.LEFT_OF, 1001); 
	        params_tv2.addRule(RelativeLayout.BELOW, tv1.getId());
	        params_tv2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        params_tv2.setMargins(
	        		0,
	        		heightTvPb,
	        		0,
	        		0);  
	        
	       tv2.setGravity(Gravity.BOTTOM | Gravity.CENTER);
	        tv2.setHint("STATUS__00009   Enter some text....");
	        tv2.setTextSize(22.0f);
	        tv2.setTextColor(Color.BLACK);
	        tv2.setBackgroundResource(R.drawable.textview_border);
	        tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX,(heightTvPb*factorFontSize/100));

	        layout1.addView(tv2);
	        
	        
	        
	        // lp.addRule(RelativeLayout.RIGHT_OF, tv1.getId());
	        // lp.addRule(RelativeLayout.BELOW, someOtherView.getId())
	       // lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
	        
	        
	        
	        
	        
	        
	        
	        set_margin_params(Integer.valueOf(size_x_),Integer.valueOf(size_y_));
	        for (int rows=0 ; rows<Integer.valueOf(size_x_) ; rows++){
	        	for (int cols=0 ; cols<Integer.valueOf(size_y_) ; cols++){
	        but1 = new Button(this);
	        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
	                LayoutParams.WRAP_CONTENT);
	        //params2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	        but1.setLayoutParams(params2);
	       
	        params2.width=Width_;
	        params2.height=Height_;
	        but1.setId(100+cols+100*rows);
	        
	        if (but1.getId()==100){
	        	params2.addRule(RelativeLayout.BELOW, tv2.getId());
	             params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	        }
	        else  if (but1.getId()>100 && but1.getId()<200){
	        	params2.addRule(RelativeLayout.BELOW, tv2.getId());
	       	params2.addRule(RelativeLayout.RIGHT_OF, but1.getId()-1);
	    } 
	        
	        
	        
	        
	        
	        else if (but1.getId()%100==0){
	       	params2.addRule(RelativeLayout.BELOW, but1.getId()-100);
	        params2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	    }
	        else  if (but1.getId()%100!=0){
	        	params2.addRule(RelativeLayout.BELOW, but1.getId()-100);
	          	params2.addRule(RelativeLayout.RIGHT_OF, but1.getId()-1);
	       } 
        
	                                    if (but1.getId()>=100 && but1.getId()<200){
	                                    	params2.setMargins(margin_right,50,margin_left,10);  	
	                                   } 
	                                    else {
	                                    	params2.setMargins(margin_right,10,margin_left,10);      	
	                                    }

	        buttons_array[rows][cols]=but1;
	        but1.setOnClickListener ( new OnClickListener(){
				@Override
				public void onClick(View v) {
					gridButtonClicked((v.getId()));
				}
			});
	        
	        /////////////////////////////
	        but1.setOnLongClickListener(new View.OnLongClickListener() {
	            @Override
	            public boolean onLongClick(View v) {

	              if (!inTaskProgerss) gridButtonLongClicked(v.getId());
	              else Toast.makeText(context, "Please wait ",Toast.LENGTH_SHORT ).show();
	              return true; }
	            }
	        );

	        //////////////////////////

	        layout1.addView(but1,params2);
        
	        	}
	           			
	        }
	        
	   // put here progressbar
	        progressBar = new ProgressBar(showGrid1.this,null,android.R.attr.progressBarStyleHorizontal);
	        RelativeLayout.LayoutParams params_progressBar = new RelativeLayout.LayoutParams(
	           		RelativeLayout.LayoutParams.MATCH_PARENT,
	           		heightTvPb);
	           progressBar.setLayoutParams(params_progressBar);
	           //layout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
	           progressBar.setVisibility(View.VISIBLE);
	           progressBar.setId(1076);
	           params_progressBar.setMargins(0,	0, 0, 0);
	          
	           params_progressBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	           params_progressBar.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	           layout1.addView(progressBar,params_progressBar);
	        
	        
	   //////////////////////////////////////////////////////     
		        tv3 = new TextView(this);
		        tv3.setOnTouchListener(this);
		        RelativeLayout.LayoutParams params_tv3 = 
		        		new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
		        				4*heightTvPb);
		        tv3.setLayoutParams(params_tv3);
		        tv3.setId(1006);
		        //params.addRule(RelativeLayout.LEFT_OF, 1001); 
		        params_tv3.addRule(RelativeLayout.ABOVE, progressBar.getId());
		        params_tv3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		        params_tv3.setMargins(
		        		0,
		        		0,
		        		0,
		        		0);  
		        tv3.setText("  Swipe left/right to move pages\n  Touch to change grid dimenstions");
		        tv3.setTextSize(36.0f);
		        tv3.setBackgroundColor(Color.YELLOW);
		        tv3.setTextColor(Color.CYAN);
		        tv3.setBackgroundResource(R.drawable.textview_border);
		        tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX,(2*heightTvPb*factorFontSize/100));
		        layout1.addView(tv3); 
	           /////////////////////////////////////////////////////////////
	        
	        
		}
	    ///////////////////////////////////////////////////////   
		
		
		/* (non-Javadoc)
		 * @see android.app.Activity#onWindowFocusChanged(boolean)
		 */
		@Override
		public void onWindowFocusChanged(boolean hasFocus) {
			super.onWindowFocusChanged(hasFocus);
		}

		private void update_all_text_views() {

			File path1 = new File(Environment.getExternalStorageDirectory(), Dir);
			//String ttt1=path1.toString();
			//String ttt2=Integer.toString(filex.size());
			tv1.setText("Path: "+path1.toString()+"  Files= "+Integer.toString(filex.size()));

			int mtrx=Integer.valueOf(size_x_)*Integer.valueOf(size_y_);
			int files_div_page_size= (int) (filex.size()/(mtrx));
			if (filex.size()%mtrx!=0) {
				files_div_page_size++;
			}
			tv2.setText("Matrix= "+size_x_+" X "+size_y_+" ,   page= "+Integer.toString(page+1)+
			" of "+   Integer.toString(files_div_page_size)+	
			" ,  Sort by: "+sort_types[Integer.valueOf(sharedpreferences.getString(sort_type, ""))]);
			
			Float a1= (float) (page+1)*100;
			Float a2= (float) (files_div_page_size);
			int precnt= (int) (a1/a2);
			progressBar.setProgress(precnt);
			}
				
		private void populate_bitmap_array() {
			String temp_tv1;
			temp_tv1= tv1.getText().toString();
			bitmapArray.clear();
			int mHeight_= (int) ((tablet_width-3*margin_left-3*margin_right)/3);  // overrides the cooeff table
			int mWidth_= (int) ((tablet_width-3*margin_left-3*margin_right)/3);
			
			tv1.setText("Reading file:   "+Integer.toString(8)+ "out_of    "+ Integer.toString(filex.size())       );
			   actionBar.setTitle("Reading file: "+Integer.toString(9)+ "out_of "+ Integer.toString(filex.size())       );
			
			for (int r=0; r<filex.size() ; r++){
	        
	               		//Matrix matrix = new Matrix();
	        		   //matrix.postRotate(0); 
	        		   tv1.setText("Reading file:   "+Integer.toString(r)+ "out_of    "+ Integer.toString(filex.size())       );
	        		   actionBar.setTitle("Reading file: "+Integer.toString(r)+ "out_of "+ Integer.toString(filex.size())       );
	        		   File file = new File(Path, filex.get(r));
	      
	        			//Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 1000,700);
	        			Bitmap bitmap = LoadBitmapFile.decodeSampledBitmapFromFile(file.getAbsolutePath(), mWidth_,mHeight_);
	        	        Bitmap rotated =  Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),null,false);
	        	        bitmapArray.add(rotated);
	            	     	//Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());  
	       
	                	}
			tv1.setText(temp_tv1);
		}

		private void read_all_prefs() {
		
			size_x_ = sharedpreferences.getString(size_x, "");
			size_y_ = sharedpreferences.getString(size_y, "");
			Dir	= sharedpreferences.getString(directory, "");
			Fname	= sharedpreferences.getString(file_name, "");
			sort_ 	= sharedpreferences.getString(sort_type, "");
				}
	    
	   
		private void add_prefs_key(String string, String string2) {

			SharedPreferences.Editor editor = sharedpreferences.edit();  
			editor.putString(string, string2);
			editor.apply();
		}
		
		
		
		
		private void set_margin_params(Integer x1, Integer y1) {
	
			Height_= (int) ((tablet_width-3*margin_left-3*margin_right)/coef[x1-3]);
			Width_= (int) ((tablet_width-3*margin_left-3*margin_right)/coef[y1-3]);
			
			int coeef=tablet_width;
			if (tablet_width>tablet_height-9*heightTvPb) coeef=tablet_height-9*heightTvPb;
			
			
			Height_= (int) ((coeef-margin_left*x1-margin_right*x1)/x1);
			Width_= (int) ((coeef-margin_left*x1-margin_right*x1)/x1);

			
			Toast.makeText(getBaseContext(), "set margin "+tablet_width+" x "+tablet_height+
					" "+Width_+" * "+Height_
					, Toast.LENGTH_SHORT).show();
			
		}
		
		private void get_all_files_in_folder() {			
//			Path = new File(Environment.getExternalStorageDirectory(), Dir);
			Path = new File(Dir);
			if(!Path.exists()) {
			    Path.mkdirs();
			}
			if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				
				Toast.makeText(getBaseContext(), "Cannot use storage.", Toast.LENGTH_SHORT).show();
			    finish();
			    return;
			    		}
			else {
				files = Path.listFiles();
				filex.clear();  // total files in folder
				filex_date.clear();
				for (int i=0;i<files.length;i++){
				
					String a1=files[i].toString();
					String[] separated = a1.split("/");
					String correct_separated=separated[separated.length-1];	
					if (correct_separated.toUpperCase().endsWith(".JPG") ||
							correct_separated.toUpperCase().endsWith(".PNG") ||
							correct_separated.toUpperCase().endsWith(".BMP") ) {	//  exclude all non note file types  
			   					filex.add(correct_separated); // only files matching graphics format
								File file100 = new File(Path, correct_separated);
								Date lastModified = new Date(file100.lastModified());
								filex_date.add(lastModified.toString());
		    		}
				}   //for loop	
			}
		}

		private void populate_all_thmbnails_with_images(int page9) {
			int xm=Integer.valueOf(size_x_);
			int ym=Integer.valueOf(size_y_);
			int base=page9*xm*ym;
			for (int rows=0; rows<xm ; rows++){
	        	for (int cols=0 ; cols<ym ; cols++){
	        	
	        		if (base+rows* xm  +cols<filex.size()  )   {
	        			//String aaa=filex.get( base+rows* xm  +cols     );
	        			buttons_array[rows][cols].setText(filex.get(base+rows*xm+cols).substring(0,filex.get( base+rows*xm+cols).length()-4) ); 
	        			buttons_array[rows][cols].setGravity(Gravity.BOTTOM | Gravity.START);
	        			buttons_array[rows][cols].setTextColor(Color.BLACK);  	 
	        			buttons_array[rows][cols].setTextSize(15.0f); 
	            	     	
BitmapDrawable bitmapDrawable = 
new BitmapDrawable(context.getResources(),bitmapArray.get(base+rows*xm+cols));
	        			
	        			
	        			buttons_array[rows][cols].setBackground(bitmapDrawable); // new
	            	                   	               				}
	        		
	        		else 			{
	        			buttons_array[rows][cols].setText("No image"); 
	        			buttons_array[rows][cols].setGravity(Gravity.BOTTOM | Gravity.START);
	        			buttons_array[rows][cols].setTextColor(Color.YELLOW);
	        			buttons_array[rows][cols].setTextSize(15.0f); 
	        			buttons_array[rows][cols].setBackgroundResource(R.drawable.filenot);		
	        					}	
	        		  	       	               
	        		}
	        	}
			
		}
		
		
		private void hide_kbd() {
	//this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	//InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	//imm.hideSoftInputFromWindow(ed.getWindowToken(), 0);
		}
/*		
		public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) 
		   { // BEST QUALITY MATCH		       
		       //First decode with inJustDecodeBounds=true to check dimensions
		       final BitmapFactory.Options options = new BitmapFactory.Options();
		       options.inJustDecodeBounds = true;
		       BitmapFactory.decodeFile(path, options);

		       // Calculate inSampleSize, Raw height and width of image
		       final int height = options.outHeight;
		       final int width = options.outWidth;
		       options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		       int inSampleSize = 1;

		       if (height > reqHeight) 
		       {
		           inSampleSize = Math.round((float)height / (float)reqHeight);
		       }
		       int expectedWidth = width / inSampleSize;

		       if (expectedWidth > reqWidth) 
		       {
		           //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
		           inSampleSize = Math.round((float)width / (float)reqWidth);
		       }

		       options.inSampleSize = inSampleSize;

		       // Decode bitmap with inSampleSize set
		       options.inJustDecodeBounds = false;

		       return BitmapFactory.decodeFile(path, options);
		   }
*/		
		
		
		private void gridButtonClicked(int y) {
			if (!inTaskProgerss)  {
			int y0=y-100;
			int yy1=(int) (y0/100);
			int xx1=(int) (y0%100);
			
			int y6=(  (page*Integer.valueOf(size_x_)*Integer.valueOf(size_y_)) + yy1*Integer.valueOf(size_y_) + xx1);
					
			if (y6<filex.size()) {
				String y3= filex.get(y6);
				Toast.makeText(this," :Y:  "+Integer.toString(y)+"    "+Integer.toString(y6)+"  "+y3,Toast.LENGTH_SHORT).show();
				add_prefs_key(file_name, y3);
				
				Intent i2 = new Intent(getApplicationContext(), showFullScreen1.class);
				//Dir	= sharedpreferences.getString(directory, "");
				//i1.putExtra("Value1", Dir);  //directory name
				//Fname	= sharedpreferences.getString(file_name, "");
				//i1.putExtra("Value2", y3);  //file name
		   	    startActivity(i2);
		   	  
			}
			else {
				Toast.makeText(this,"Empty Image !!!",Toast.LENGTH_SHORT).show();
				add_prefs_key(file_name, "NULL");
			}
			}
			else Toast.makeText(this,"Pls wait",Toast.LENGTH_SHORT).show();

		}
		
		private void gridButtonLongClicked(int y) {
	
			int y6=((page*Integer.valueOf(size_x_)*Integer.valueOf(size_y_))+
					(int) ((y-100)/100)*Integer.valueOf(size_y_) + (int) ((y-100)%100));
					
			if (y6<filex.size()) {
				Path = new File(Environment.getExternalStorageDirectory(), Dir);
				final File file = new File(Path, filex.get(y6));
           		BitmapFactory.Options options = new BitmapFactory.Options();
           		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
           		Bitmap bitmap = BitmapFactory.decodeFile(file.toString(), options);
  	//		Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath());

//	Toast.makeText(this,"height "+Integer.toString(h)+" width "+Integer.toString(w)+"  ",Toast.LENGTH_SHORT).show();
	AlertDialog alertDialog = new AlertDialog.Builder(context).create();
	alertDialog.setTitle("Image file properties...");
	
	if (bitmap==null) { alertDialog.setMessage("No image loaded");}
			
			
			else { alertDialog.setMessage(
			"Bitmap Name= "+filex.get(y6)+"\n"+
		    "Bitmap Height= "+String.valueOf(bitmap.getHeight())+"\n"+
		    "Bitmap Width=  "+String.valueOf(bitmap.getWidth())+"\n"+
		    "Bitmap Bytes= "+String.valueOf(bitmap.getRowBytes()*bitmap.getHeight())+"\n"+
		    "File size= "+String.valueOf((long)file.length()   )+"\n"+
		    "Bitmap density=  "+String.valueOf(bitmap.getDensity())+"\n"+
		    "Bitmap Byte count=  "+String.valueOf(bitmap.getByteCount())+"\n"+
		    "Bitmap Config=  "+String.valueOf(bitmap.getConfig().toString())+"\n"
			);
			}
	
	BitmapDrawable bitmapDrawable1 = 
			new BitmapDrawable(context.getResources(),bitmapArray.get(y6));

	alertDialog.setIcon(bitmapDrawable1);
	
	alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "delete image?", new DialogInterface.OnClickListener() {

	      public void onClick(DialogInterface dialog, int id) {
	    	  
	    	  boolean dele=file.delete();
	    	  if (dele) {
	    		  Toast.makeText(context,"deleted file: "+file.toString(),Toast.LENGTH_LONG).show();
	    		  task = new OurTask(activity);
	              task.execute();
	              
	    	  }
	    	  
	    	  
	    	  
	    	  else Toast.makeText(context,"Not deleted file: "+file.toString(),Toast.LENGTH_LONG).show();
	    }}); 
		
	
	alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Back", new DialogInterface.OnClickListener() {

	      public void onClick(DialogInterface dialog, int id) {
		    	Toast.makeText(context,"lllll menu pressed ",Toast.LENGTH_SHORT).show();
	    }}); 

	alertDialog.show();			
	alertDialog.getButton(Dialog.BUTTON_NEGATIVE).setBackgroundColor(Color.RED);  	  
	alertDialog.getButton(Dialog.BUTTON_POSITIVE).setBackgroundColor(Color.GREEN);  	  
			
	            }
			else {
				Toast.makeText(this,"Empty Image long clicked !!!",Toast.LENGTH_SHORT).show();
				add_prefs_key(file_name, "NULL");
			}
		
		}
		
		
		
		
		private void sort(String string) {
			
			page=0;
			add_prefs_key(sort_type, string); 
						
			int sort1=Integer.valueOf(string);
			boolean flag_sort=true;
			Bitmap bm;
			String temp_sort;
			if (sort1==0) return;
			else if (sort1==1) {
			
			     while ( flag_sort ) 	     {
			            flag_sort= false;    //set flag to false awaiting a possible swap
			            for(int j_sort=0;  j_sort < filex.size() -1;  j_sort++ )  {
			                   
			            	
			                 	if ( filex.get(j_sort).compareTo(filex.get(j_sort+1)) < 0 )       {
			                           temp_sort = filex.get(j_sort);                //swap elements
			                           filex.set(j_sort, filex.get(j_sort+1));
			                           filex.set(j_sort+1, temp_sort);
			                           
			                           temp_sort = filex_date.get(j_sort);                //swap elements
			                           filex_date.set(j_sort, filex_date.get(j_sort+1));
			                           filex_date.set(j_sort+1, temp_sort);
			                           	
			                           bm = bitmapArray.get(j_sort);                //swap elements
			                           bitmapArray.set(j_sort, bitmapArray.get(j_sort+1));
			                           bitmapArray.set(j_sort+1, bm);
			                           
			                          flag_sort = true;              //shows a swap occurred 
			                  }
			            }
			      } 
							
			}
			else if (sort1==2) {
				while ( flag_sort ) 	     {
		            flag_sort= false;    //set flag to false awaiting a possible swap
		            for(int j_sort=0;  j_sort < filex.size() -1;  j_sort++ )  {
		                   
		            	
		                 	if ( filex.get(j_sort).compareTo(filex.get(j_sort+1)) > 0 )       {
		                           temp_sort = filex.get(j_sort);                //swap elements
		                           filex.set(j_sort, filex.get(j_sort+1));
		                           filex.set(j_sort+1, temp_sort);
		                           
		                           temp_sort = filex_date.get(j_sort);                //swap elements
		                           filex_date.set(j_sort, filex_date.get(j_sort+1));
		                           filex_date.set(j_sort+1, temp_sort);
		                           		
		                           bm = bitmapArray.get(j_sort);                //swap elements
		                           bitmapArray.set(j_sort, bitmapArray.get(j_sort+1));
		                           bitmapArray.set(j_sort+1, bm);
		                           
		                          flag_sort = true;              //shows a swap occurred 
		                  }
		            }
		      } 
				
				
				
			}
			else if (sort1==3) {
				while ( flag_sort ) 	     {
		            flag_sort= false;    //set flag to false awaiting a possible swap
		            for(int j_sort=0;  j_sort < filex.size() -1;  j_sort++ )  {
		                   	if ( filex_date.get(j_sort).compareTo(filex_date.get(j_sort+1)) < 0 )       {
		                           temp_sort = filex.get(j_sort);                //swap elements
		                           filex.set(j_sort, filex.get(j_sort+1));
		                           filex.set(j_sort+1, temp_sort);
		                           
		                           temp_sort = filex_date.get(j_sort);                //swap elements
		                           filex_date.set(j_sort, filex_date.get(j_sort+1));
		                           filex_date.set(j_sort+1, temp_sort);
		                           	
		                           bm = bitmapArray.get(j_sort);                //swap elements
		                           bitmapArray.set(j_sort, bitmapArray.get(j_sort+1));
		                           bitmapArray.set(j_sort+1, bm);
		                           
		                          flag_sort = true;              //shows a swap occurred 
		                  }
		            }
		      } 
			}
			else if (sort1==4) {
				while ( flag_sort ) 	     {
		            flag_sort= false;    //set flag to false awaiting a possible swap
		            for(int j_sort=0;  j_sort < filex.size() -1;  j_sort++ )  {
		                   	if ( filex_date.get(j_sort).compareTo(filex_date.get(j_sort+1)) > 0 )       {
		                           temp_sort = filex.get(j_sort);                //swap elements
		                           filex.set(j_sort, filex.get(j_sort+1));
		                           filex.set(j_sort+1, temp_sort);
		                           
		                           temp_sort = filex_date.get(j_sort);                //swap elements
		                           filex_date.set(j_sort, filex_date.get(j_sort+1));
		                           filex_date.set(j_sort+1, temp_sort);
		                           	
		                           bm = bitmapArray.get(j_sort);                //swap elements
		                           bitmapArray.set(j_sort, bitmapArray.get(j_sort+1));
		                           bitmapArray.set(j_sort+1, bm);
		                           
		                          flag_sort = true;              //shows a swap occurred 
		                  }
		            }
		      } 
			}
			populate_all_thmbnails_with_images(page);
//			Toast.makeText(getApplicationContext(), "Previous Page "+Integer.toString(page), Toast.LENGTH_SHORT).show();
			actionBar.setTitle("View "+Integer.toString(page)+" "+Integer.toString(filex.size()) );
			update_all_text_views();
		}
		
		
		/////// async task

		class OurTask extends AsyncTask<Void, String, Void>    {
			private ProgressDialog dialog;

			boolean isCanceled = false;
			
	        public OurTask(Activity activity) {
	        	dialog =new ProgressDialog(activity);
}
			public void myCancel()
	        {
	            isCanceled = true;
	        }
	        /* (non-Javadoc)
			 * @see android.os.AsyncTask#onPreExecute()
			 */
			@Override
			protected void onPreExecute() {

				super.onPreExecute();
	        	dialog.setMessage("Loading....  , please wait....");
	    		dialog.show();
				inTaskProgerss=true;
				page=0;
				sort("1");
				bitmapArray.clear();
	            get_all_files_in_folder();
			}


	        
	        @Override
	        protected Void doInBackground(Void... params) {   	
	        	bitmapArray.clear();
//	        	int mHeight_= (int) ((tablet_width-3*margin_left-3*margin_right)/3);  // overrides the cooeff table
//				int mWidth_= (int) ((tablet_width-3*margin_left-3*margin_right)/3);
				Path = new File(Dir);
				if (isCanceled)
		          {
		              return null;
		          }
				for (int r=0; r<filex.size() ; r++){       	
					try {	            	
						bitmap = LoadBitmapFile.decodeSampledBitmapFromFile(new File(Path,
								filex.get(r)).getAbsolutePath(), Height_,Width_);
						bitmap1 =  Bitmap.createBitmap(bitmap,0,0,
								bitmap.getWidth(),bitmap.getHeight(),null,false);
						}
catch (Exception e) {
	bitmap1 = BitmapFactory.decodeResource(context1.getResources(),
            R.drawable.badimage100);
	  } 



					bitmapArray.add(bitmap1);
	        	        int  percentage = (int) (  (r*100) /filex.size());
	                    String passToPublish = null;
	                    String p = null;
	                    String p1 = null;
	                    passToPublish=Integer.toString(percentage);
	                    p=filex.get(r);
	                    p1=Integer.toString(r);	        	 
	        	        publishProgress(passToPublish,p,p1);
	                	}
				return null;
	        }

	        @Override
	        protected void onPostExecute(Void result) {
	            super.onPostExecute(result);
	            if (dialog.isShowing()) {
	       			dialog.dismiss();
	               }
	            inTaskProgerss=false;
	            page=0;
	            populate_all_thmbnails_with_images(page);
	            update_all_text_views();
	            
	        } 

	        
	        protected void onProgressUpdate(String... values) {
	            tv1.setText("Loading file: "+ values[2]+"  "+values[1]     );   
	        	progressBar.setProgress(Integer.parseInt(values[0]));
	        	 
  	        if (Integer.valueOf(values[2])<Integer.valueOf(size_x_)*Integer.valueOf(size_y_)){
    	        	int rows=(int)Integer.valueOf(values[2])/Integer.valueOf(size_x_);
    	        	int cols=(int)Integer.valueOf(values[2])%Integer.valueOf(size_y_);
//		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmapArray.get(r));  //  new
    	        	BitmapDrawable bitmapDrawable = 
    	        			new BitmapDrawable(context.getResources(),
    	        					bitmapArray.get(Integer.valueOf(values[2])));

		buttons_array[rows][cols].setBackground(bitmapDrawable); // new	
  	        		}	        	
	            }	
}  // end of class
		
		///////////////////

		@Override
		public boolean onTouch(View v,MotionEvent event) {
		if (v.getId()==1006){
//			if (true){
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
		
				if (Math.abs(diffY)<20.0f && Math.abs(diffX)<20.0f) {
	Toast.makeText(getApplicationContext(), "TOUCH "+v.getId(), Toast.LENGTH_SHORT).show();	
set_grid();
}
				
				else if (Math.abs(diffY)> Math.abs(diffX) && endY < startY) {
	Toast.makeText(getApplicationContext(), "UP "+v.getId(), Toast.LENGTH_SHORT).show();	}

				else if (Math.abs(diffY)> Math.abs(diffX) && endY > startY) {
	Toast.makeText(getApplicationContext(), "DOWN "+v.getId(), Toast.LENGTH_SHORT).show();
	}	
		
				else if (Math.abs(diffY)< Math.abs(diffX) && endX > startX) {
	Toast.makeText(getApplicationContext(), "RIGHT "+v.getId(), Toast.LENGTH_SHORT).show();	
		next_page();
				}
				else {
					Toast.makeText(getApplicationContext(), "LEFT", Toast.LENGTH_SHORT).show();	
previous_page();
				}
		    }
			
			
		
		}
			
		}/////////////////////
			return true;
		}
		
		
				
			



	@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.showgrid1, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			switch (id) {
			
			case R.id.nextpage:
				next_page();
				break;
				
			case R.id.prevpage:
				//	Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 
				previous_page();
				break;
				
			case R.id.grid:
			set_grid();	
			
				

				break;
							
			case R.id.fullimage1:
				//Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 
					
				Intent i = new Intent(getApplicationContext(), showFullScreen1.class);
				Dir	= sharedpreferences.getString(directory, "");
				i.putExtra("Value1", Dir);  //directory name
				Fname	= sharedpreferences.getString(file_name, "");
				i.putExtra("Value2", Fname);  //file name
	       	    startActivity(i);
				
						break;
				
						
			case R.id.SortTitleAsc:
				sort("1");
				break;
				
			case R.id.SortTitleDsc:
				sort("2");
				break;
			
				
			case R.id.SortDateAsc:
				sort("3");
				break;
				
			case R.id.SortDateDsc:
				sort("4");
				break;
		
			case R.id.exit:
		//Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 
				task.myCancel();
				Intent inte=new Intent();
				// put the message to return as result in Intent
					inte.putExtra("MESSAGE","back back !!!!!!");
				// Set The Result in Intent
				setResult(4,inte);
				showGrid1.this.finish();
				break;
							
			}
			return super.onOptionsItemSelected(item);
		}
		
		
		
		
		
		
		private void set_grid() {
			if (!inTaskProgerss){	
				page=0;
				int x2=Integer.valueOf(size_x_);
				int y2=Integer.valueOf(size_y_);
				if (x2==8 && y2==8){
					x2=3;y2=3;
				}
				else {
					x2++;y2++;
				}
				size_x_=Integer.toString(x2);
				size_y_=Integer.toString(y2);
				Toast.makeText(getApplicationContext(), "Image matrix size changed  "+size_x_+" X "+size_y_, Toast.LENGTH_SHORT).show(); 
				add_prefs_key(size_x, size_x_);
				add_prefs_key(size_y, size_y_); 
				update_all_text_views();
				layout1.removeAllViews(); 
			    set_all_views();
			    populate_all_thmbnails_with_images(page);
		        actionBar.setTitle("View "+Integer.toString(files.length)+" "+Integer.toString(filex.size()) );
		        hide_kbd();
		        update_all_text_views();
			}
			else{
				Toast.makeText(getApplicationContext(), "Please wait... "+Integer.toString(page), Toast.LENGTH_SHORT).show();
				
			}
			
		}

		private void next_page() {
			int base1=(page)*Integer.valueOf(size_x_)*Integer.valueOf(size_y_)+( Integer.valueOf(size_x_)*Integer.valueOf(size_y_)  );
			if (!inTaskProgerss){	
				if (filex.size()-base1>0){
					page++;
//					Toast.makeText(getApplicationContext(), "Page next   "+Integer.toString(page), Toast.LENGTH_SHORT).show();
					actionBar.setTitle("View "+Integer.toString(page)+" "+Integer.toString(filex.size()) );
					populate_all_thmbnails_with_images(page);
					update_all_text_views();
				}
				else {
					Toast.makeText(getApplicationContext(), "No more images "+Integer.toString(page), Toast.LENGTH_SHORT).show();
					update_all_text_views();
				}
			}else{
				Toast.makeText(getApplicationContext(), "Please wait... "+Integer.toString(page), Toast.LENGTH_SHORT).show();
		
			}
			
		}

		private void previous_page() {
			if (!inTaskProgerss){	
			if (page<1){
					page=0;
//					Toast.makeText(getApplicationContext(), "Page 0 found   "+Integer.toString(page), Toast.LENGTH_SHORT).show();
					update_all_text_views();
				}
				else {
					page--;
				}
				populate_all_thmbnails_with_images(page);
//				Toast.makeText(getApplicationContext(), "Previous Page "+Integer.toString(page), Toast.LENGTH_SHORT).show();
				actionBar.setTitle("View "+Integer.toString(page)+" "+Integer.toString(filex.size()) );
				update_all_text_views();
			}else{
				Toast.makeText(getApplicationContext(), "Please wait... "+Integer.toString(page), Toast.LENGTH_SHORT).show();
				
			}	
			
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
		
		public void onBackPressed() 
		{
			task.myCancel();
			Intent inte=new Intent();
		// put the message to return as result in Intent
			inte.putExtra("MESSAGE","back back !!!!!!");
		// Set The Result in Intent
		setResult(4,inte);
			showGrid1.this.finish();
			}

//		@Override
//		public boolean onTouch(View v, MotionEvent event) {
//			// TODO Auto-generated method stub
//			return false;
//		}
		
		
	}

//Intent intentMessage_1=new Intent();
//// put the message to return as result in Intent
//intentMessage_1.putExtra("MESSAGE","back back !!!!!!");
//// Set The Result in Intent
//setResult(2,intentMessage_1);


	
