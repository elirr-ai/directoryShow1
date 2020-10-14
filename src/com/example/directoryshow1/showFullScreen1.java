package com.example.directoryshow1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public   class showFullScreen1 extends Activity implements OnTouchListener   {

	private ClipboardManager myClipboard;
	private ClipData myClip;
	Context context1=this;
	private static final String TAG = "Touch";
	private boolean f=true;
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();

	static final int maxXmovement=1000,maxYdiff=300;
	static final int NOACTION=0,MOVRRIGHT=1,MOVELEFT=2;
	// We can be in one of these 3 states
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	static final int DRAG2FINGERS=3;
	int mode = NONE;

	// Remember some things for zooming
	PointF start = new PointF();
	PointF mid = new PointF();
	float oldDist = 1f;
	PointF finger1Start= new PointF(),finger2Start= new PointF();
	PointF finger1End= new PointF(),finger2End= new PointF();	
	
	
	
	private ActionBar actionBar;
	static ImageView iv;
	TextView tv1;
	static int rotate_angel=0;
	
	SharedPreferences sharedpreferences;
	public static final String MyPREFERENCES = "MyPrefsGrid" ;  // my pref internal folder name
    
    // keys
    public static final String Initialized = "Initialized";
    public static final String size_x = "size_x";
    public static final String size_y = "size_y";
    public static final String directory = "Directory";
    public static final String file_name = "File_name";
    public static final String fitFullScreen = "FITFULL";
    public static final String showFullScreenMode = "showFullScreenMode";

    
    String showFull="0";
    static String Dir="";
	static String Fname="";
	String fitScreen="";	
	GetScreenHieghtWidth g1;
	imageLoadTask task;
	private static Bitmap bitmap1;
	
	static int mWidthScreen;
	static int mHeightScreen;
	private ArrayList<String>  graphicsFilesInDir = new ArrayList<String>();
	private int graphicsFilesInDirIndex=0;
	
	
	
	 @Override
	  public void onCreate(Bundle bundle) {
	    super.onCreate(bundle);
	    mid.x=0f; mid.y=0f;
	    //Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
	    //int mWidthScreen = display.getWidth();
	    //int mHeightScreen = display.getHeight();
	      
	    Display disp= getWindowManager().getDefaultDisplay();
	    GetScreenHieghtWidth g1 = new GetScreenHieghtWidth(disp);
		mWidthScreen=g1.getmWidthScreen();
	    mHeightScreen=g1.getmHeightScreen();
	    
	  //  Point size = new Point();
	//	disp.getSize(size);
	//	mWidthScreen = size.x;
	//	mHeightScreen = size.y;
		//Toast.makeText(getApplicationContext(), "TABLET =  "+Integer.toString(mWidthScreen)+"  "+Integer.toString(mHeightScreen), Toast.LENGTH_SHORT).show();
		
	    
	    /*
	    
Display disp= getWindowManager().getDefaultDisplay();
Point dimensions = new Point();
disp.getSize(size);
int width = size.x;
int height = size.y;

	     */
	    
	    sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		read_prefs();  
		String[] pattern1 ={".bmp", ".jpg",".png",".BMP", ".JPG",".PNG"};
//	    getListofFileTypes gl=new getListofFileTypes();
	    
	    graphicsFilesInDir= getListofFileTypes.getListofFiles(Dir,pattern1);
	    
	    RelativeLayout layout1 = new RelativeLayout(this);
        layout1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
        tv1 = new TextView(this);
        RelativeLayout.LayoutParams params_tv1 = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
                LayoutParams.WRAP_CONTENT);
        //params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        // use same id as defined when adding the button
        
        tv1.setLayoutParams(params_tv1);
        tv1.setId(1004);
        //params.addRule(RelativeLayout.LEFT_OF, 1001); 
        params_tv1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        params_tv1.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //params_tv1.setMargins(0,	50, 0, 0);  
        params_tv1.height= (int)(mHeightScreen/35)     ;
        //params_tv1.height= 30   ;
        tv1.setGravity(Gravity.BOTTOM | Gravity.START);
        layout1.addView(tv1);
        
        
        iv = new ImageView(this);
        RelativeLayout.LayoutParams iv_params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
                LayoutParams.MATCH_PARENT);
        //params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        // use same id as defined when adding the button
       // iv_params.height = 850;  /////////////////////////////////////////////////////////////////
        iv_params.height =  (int) (mHeightScreen*31)/35;
        iv_params.width = (int) mWidthScreen;
        iv.setLayoutParams(iv_params);
        iv.setId(1000);
        iv_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        iv_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        iv.setScaleType(ScaleType.FIT_XY);
        layout1.addView(iv);
        
        setContentView(layout1);
        
        actionBar = getActionBar();
		actionBar.setIcon(R.drawable.viewimage);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle("View "+Fname+String.valueOf(mWidthScreen)+" x "+String.valueOf(mHeightScreen));
	    
	    iv.setOnTouchListener(this);  
    
	    task = new imageLoadTask(this);
        task.execute();
	    }
	
		/////// async task

		class imageLoadTask extends AsyncTask<Void, String, Bitmap>    {
			private ProgressDialog dialog;
			boolean isCanceled = false;
			
			public imageLoadTask(showFullScreen1 activity) {
				dialog = new ProgressDialog(activity);
			}
			
	        public void myCancel()
	        {
	            isCanceled = true;
	        }
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
	        	dialog.setMessage("Loading....  , please wait....");
	    		dialog.show();
			}

	        @Override
	        protected Bitmap doInBackground(Void... params) {
	        f=true;
	        Bitmap bitmap=null;
	       	matrix.reset();
	  	    matrix.postRotate(rotate_angel); 

	  	    if (isCanceled)
          {
              return null;
          }
	  	    
	  	    try {
  	    
		  	    bitmap = LoadBitmapFile.decodeSampledBitmapFromFile
		        		(new File(Dir +File.separator+Fname ).toString(),
		    	  	    		(int) mWidthScreen  ,(int) (mHeightScreen*31)/35);
		  	    if (bitmap!=null){
		  	    	f=true;
		  	    bitmap1=Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),
	  	    		bitmap.getHeight(),matrix,false);
		  	    		}
		  	    else {
		  	    	f=false;
		  	    	bitmap1= (BitmapFactory.decodeResource(context1.getResources(),
			  				R.drawable.badimage100));
		  	    }
	  	    		}
	  	  catch (Exception e) {
	  		  f=false;
	  		  bitmap1= (BitmapFactory.decodeResource(context1.getResources(),
	  				R.drawable.badimage100));   	  
	  	  } 
	  	    publishProgress("Loading.....");	        	        	
	return bitmap1;            
	            
	        }

	        @Override
	        protected void onPostExecute(Bitmap bm) {
	            super.onPostExecute(bm);
	    	    graphicsFilesInDirIndex=getListofFileTypes.getFileIndexLocation(Fname, graphicsFilesInDir);
	            if (dialog.isShowing()) {
	       			dialog.dismiss();
	               }
	    	    tv1.setText(Dir+"    "+Fname+" "+Integer.toString(mWidthScreen)+" "
	               +Integer.toString(mHeightScreen)+
	               "  "+String.valueOf(1+graphicsFilesInDirIndex)+
	               " of "+
	               String.valueOf(graphicsFilesInDir.size())
	               +"  pictures"
	    	    );

	            float bmsx=(float)( (float)bm.getWidth()/(float)mWidthScreen);
	            float bmsy=(float)( (float)bm.getHeight()/(float)mHeightScreen);
//	            Matrix matrix = new Matrix();
	            matrix.reset();
				if (sharedpreferences.getString(fitFullScreen, "").equals("YES")){
					// use to fit full screen
		            matrix.preScale(  (float)(1/bmsx), (float)((float)(1/   (bmsy*35/31)  )));
				} 
				
				else {
		            if (bmsx>bmsy){
	            	matrix.preScale(  (float)(1/bmsx), (float)((float)(1/bmsx)));
	            }
	            else {
	            	   matrix.preScale(  (float)(1/bmsy), (float)((float)(1/bmsy)));
	            	}	
				}

///////////////////////	  use to keep accurate scaling image          

///////////////////////////
	            

	            iv.setImageMatrix(matrix);
	            iv.setScaleType(ScaleType.MATRIX);
	            iv.setImageBitmap(bm);
	    	  	String s1=" fit ";
	            if (sharedpreferences.getString(fitFullScreen, "").equals("YES")) s1=" full "; 
	    	  
	    		actionBar.setTitle("View "+Fname+s1+
	    				String.valueOf(mWidthScreen)+" x "+String.valueOf(mHeightScreen));
	            
	 	        } 
	        
	        protected void onProgressUpdate(String... values) {
	            }
	
}  // end of class
		
		
		///////////////////
	 
	 
	    
		private void read_prefs() {
				
			Dir	= sharedpreferences.getString(directory, "");
			Fname	= sharedpreferences.getString(file_name, "");
			fitScreen=sharedpreferences.getString(fitFullScreen, ""); 
			showFull=sharedpreferences.getString(showFullScreenMode, ""); 
				}
	    
	   
	 public static void show_image_rotate(int ang) {
		 
		 Matrix matrix = new Matrix();
	      matrix.postRotate(ang); 
	      iv.setImageBitmap(Bitmap.createBitmap(bitmap1,0,0,
	    		  bitmap1.getWidth(),bitmap1.getHeight(),matrix,true));

	 }
	 

@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.showimage1, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		Matrix matrix = new Matrix();
		int id = item.getItemId();
		switch (id) {
				
		case R.id.rotateright:
			//Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 
			
		if (f){	
			
			if (rotate_angel==270) rotate_angel=0;
			else rotate_angel+=90;
			show_image_rotate(rotate_angel);  }
					break;
			
		
					
		case R.id.reload:
			Toast.makeText(getApplicationContext(), "reload ", Toast.LENGTH_SHORT).show(); 
			task.cancel(false);
			task = new imageLoadTask(this);
			task.execute();
			break;
	

		case R.id.enhance:
			Toast.makeText(getApplicationContext(), "enh ", Toast.LENGTH_SHORT).show(); 
			
Intent ie = new Intent(showFullScreen1.this, BitmapEnhancment1.class);
ie.putExtra("dir", Dir);
ie.putExtra("name", Fname);	
startActivity(ie);
			break;
			
		case R.id.fullscreen:
			rotate_angel=0;
			if (sharedpreferences.getString(fitFullScreen, "").equals("YES")){
				add_prefs_key(fitFullScreen, "NO"); 
				Toast.makeText(getApplicationContext(), "Fit screen     ", Toast.LENGTH_SHORT).show(); 
			}
			else{ 
				add_prefs_key(fitFullScreen, "YES");
				Toast.makeText(getApplicationContext(), "Full screen     ", Toast.LENGTH_SHORT).show(); 
			}
//			Toast.makeText(getApplicationContext(), "full screen     ", Toast.LENGTH_SHORT).show(); 
			task = new imageLoadTask(this);
			task.execute();
					break;			
			
					
		case R.id.savepdf:
			
HandlePDF h=new HandlePDF(context1);
h.saveImagePdf(Dir, Fname, bitmap1);
			Toast.makeText(getApplicationContext(), "PDF "+Dir+"  "+Fname, Toast.LENGTH_SHORT).show(); 
					break;							
					
		case R.id.copyclip:
		      myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            myClip = ClipData.newPlainText("text", Dir+","+Fname);
	            myClipboard.setPrimaryClip(myClip);
			break;
					
					
					
		case R.id.nextpage:
	//Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 
incrementPicture();
			break;
					
		case R.id.previouspage:
	//Toast.makeText(getApplicationContext(), "Alarm pressed", Toast.LENGTH_SHORT).show(); 
decremantPicture();
			break;				
					
					
		case R.id.showexif:

String f= new File(Environment.getExternalStorageDirectory()+
		File.separator + Dir +File.separator+Fname ).getAbsolutePath();
ArrayList< ExifHolder> al =GetExifDataFromBitMap.getExifData(context1, f);
ArrayList<String> abl=GetBMPProperties.getProperties(bitmap1);

StringBuilder sb =new StringBuilder();
sb.append("EXIF DATA...\n");
sb.append("-------------------------\n");
for (int i=0;i<al.size();i++){
	sb.append(al.get(i).getTag() +" :  "+al.get(i).name+"\n"  );
}
sb.append("\n\nBITMAP DATA...\n");
sb.append("-------------------------\n");
sb.append("DIR: "+Dir+"\n");
sb.append("FILE: "+Fname+"\n");
for (int i=0;i<abl.size();i++){
	sb.append(abl.get(i));
}

//Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show(); 

final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context1);
alertDialogBuilder.setMessage(sb.toString());
alertDialogBuilder.setPositiveButton("OK", 
   new DialogInterface.OnClickListener() {
   @Override
   public void onClick(DialogInterface arg0, int arg1) {
   }
});
AlertDialog alertDialog = alertDialogBuilder.create();
alertDialog.show();

			break;				

		case R.id.exit:
			task.myCancel();
			Intent intentMessage_2=new Intent();
		    // put the message to return as result in Intent
	        intentMessage_2.putExtra("MESSAGE","back back !!!!!!");
			
			// Set The Result in Intent
	        setResult(3,intentMessage_2);
						
			showFullScreen1.this.finish();
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
	public boolean onTouch(View v, MotionEvent event) {
		   ImageView view = (ImageView) v;
		   // make the image scalable as a matrix
		   view.setScaleType(ImageView.ScaleType.MATRIX);
		   float scale;

		   // Handle touch events here...
		   switch (event.getAction() & MotionEvent.ACTION_MASK) {

		   case MotionEvent.ACTION_DOWN: //first finger down only
		      savedMatrix.set(matrix);
		      start.set(event.getX(), event.getY());
		      Log.d(TAG, "mode=DRAG" );
		      mode = DRAG;
		      break;
		   case MotionEvent.ACTION_UP: //first finger lifted
		if (mode == DRAG)   mode = NONE;
			      Log.d(TAG, "mode=NONE" );
			      break;
		   case MotionEvent.ACTION_POINTER_UP: //second finger lifted
			   finger1End.x=event.getX(0);
			   finger1End.y=event.getY(0);
			   finger2End.x=event.getX(1);
			   finger2End.y=event.getY(1);			   
		   
if (checkParallel()==1){
	Toast.makeText(this, "Right...", Toast.LENGTH_SHORT).show();
incrementPicture();
	}
if (checkParallel()==2){
	Toast.makeText(this, "Left...", Toast.LENGTH_SHORT).show();
decremantPicture();

}
		   
		      mode = NONE;
		      Log.d(TAG, "mode=NONE" );
		      break;
		   case MotionEvent.ACTION_POINTER_DOWN: //second finger down
			   finger1Start.x=event.getX(0);
			   finger1Start.y=event.getY(0);
			   finger2Start.x=event.getX(1);
			   finger2Start.y=event.getY(1);
		      oldDist = spacing(event); // calculates the distance between two points where user touched.
		      Log.d(TAG, "oldDist=" + oldDist);
		      // minimal distance between both the fingers
		      if (oldDist > 5f) {
		         savedMatrix.set(matrix);
		         midPoint(mid, event); // sets the mid-point of the straight line between two points where user touched. 
		         mode = ZOOM;
		         Log.d(TAG, "mode=ZOOM" );
		      }
		      break;

		   case MotionEvent.ACTION_MOVE: 
		      if (mode == DRAG) 
		      { //movement of first finger
		         matrix.set(savedMatrix);
//		         if (view.getLeft() >= -192)    {
//  Toast.makeText(this, "view.getLeft() "+view.getLeft(), Toast.LENGTH_SHORT).show();
	            matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
//		         }
		      }
		      else if (mode == ZOOM) { //pinch zooming
		         float newDist = spacing(event);
		         Log.d(TAG, "newDist=" + newDist);
		         if (newDist > 5f) {
		            matrix.set(savedMatrix);
		            scale = newDist/oldDist; //thinking I need to play around with this value to limit it**
		            matrix.postScale(scale, scale, mid.x, mid.y);
		            //matrix.postScale(scale, scale, 0f, 0f);
		         }
		      }
		      break;
		   }

		   // Perform the transformation
		   view.setImageMatrix(matrix);

		   return true; // indicate event was handled
		}
	 
	private void decremantPicture() {
		// TODO Auto-generated method stub
		if (graphicsFilesInDirIndex>0){
			graphicsFilesInDirIndex--;
			Fname=graphicsFilesInDir.get(graphicsFilesInDirIndex);
		    task = new imageLoadTask(this);
		    task.execute();
		}		
	}


	private void incrementPicture() {
		if (graphicsFilesInDir.size()-1>graphicsFilesInDirIndex){
			graphicsFilesInDirIndex++;
			Fname=graphicsFilesInDir.get(graphicsFilesInDirIndex);
		    task = new imageLoadTask(this);
		    task.execute();
		}
	}


	private float spacing(MotionEvent event) {
		   float x = event.getX(0) - event.getX(1);
		   float y = event.getY(0) - event.getY(1);
		   return (float) Math.sqrt(x * x + y * y);
		}

		private void midPoint(PointF point, MotionEvent event) {
		   float x = event.getX(0) + event.getX(1);
		   float y = event.getY(0) + event.getY(1);
		   point.set(x / 2, y / 2);
		}
	
		private void add_prefs_key(String string, String string2) {
			SharedPreferences.Editor editor = sharedpreferences.edit();  
			editor.putString(string, string2);
			editor.apply();
		}
	
		private int checkParallel() {

			if	( Math.abs(finger1Start.x-finger1End.x)>maxXmovement &&
					(finger1Start.x<finger1End.x) &&
					( Math.abs(finger2Start.x-finger2End.x)>maxXmovement) &&
//							(finger2Start.x<finger2End.x)	&&
							
							( Math.abs(finger1Start.y-finger1End.y))<maxYdiff &&
									(finger1Start.y>finger1End.y) &&
									( Math.abs(finger2Start.y-finger2End.y))<maxYdiff 
//									&&
//											(finger2Start.y>finger2End.y)
											)		
					
					{
				return MOVRRIGHT;
			}
			

			if	( Math.abs(finger1Start.x-finger1End.x)>maxXmovement &&
					(finger1Start.x>finger1End.x) &&
					( Math.abs(finger2Start.x-finger2End.x)>maxXmovement) &&
							(finger2Start.x>finger2End.x)	&&
							
							( Math.abs(finger1Start.y-finger1End.y))<maxYdiff &&
//									(finger1Start.y>finger1End.y) &&
									( Math.abs(finger2Start.y-finger2End.y))<maxYdiff
//									&&
//											(finger2Start.y>finger2End.y)
									)		
					
					{
				return MOVELEFT;
			}
			
			else  return NOACTION;
			}
		
		
		@Override
		public void onBackPressed() 
		{
			task.myCancel();
			Intent intentMessage_1=new Intent();
		    // put the message to return as result in Intent
	        intentMessage_1.putExtra("MESSAGE","back back !!!!!!");
			// Set The Result in Intent
	        setResult(3,intentMessage_1);
			showFullScreen1.this.finish();
			// Your Code Here. Leave empty if you want nothing to happen on back press.
		}
/////////////////////////////////////////////  NOT USED /////////////////////////
		private void createPdf(){
	        // create a new document 	 
			PdfDocument  document = new PdfDocument();

	        // crate a page description
	        PdfDocument.PageInfo pageInfo = 
	                new PdfDocument.PageInfo.Builder(100, 100, 1).create();

	        // start a page
	        PdfDocument.Page page = document.startPage(pageInfo);

	        Canvas canvas = page.getCanvas();

	        Paint paint = new Paint();
	        paint.setColor(Color.RED);

	        canvas.drawCircle(50, 50, 30, paint);

	        // finish the page
	        document.finishPage(page);

	        // Create Page 2
	        pageInfo = new PdfDocument.PageInfo.Builder(500, 500, 2).create();
	        page = document.startPage(pageInfo);
	        canvas = page.getCanvas();
	        paint = new Paint();
	        paint.setColor(Color.BLUE);
	        canvas.drawCircle(200, 200, 100, paint);
	        document.finishPage(page);

	        // write the document content
	        String targetPdf = "/sdcard/test.pdf";
	        File filePath = new File(targetPdf);
	        try {
	            document.writeTo(new FileOutputStream(filePath));
	            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
	        } catch (IOException e) {
	            e.printStackTrace();
	            Toast.makeText(this, "Something wrong: " + e.toString(),
	                    Toast.LENGTH_LONG).show();
	        }

	        // close the document
	        document.close();
	    }
		
		private void saveImagePdf(){
			PdfDocument  document = new PdfDocument();
	        PdfDocument.PageInfo pageInfo = 
               new PdfDocument.PageInfo.Builder(bitmap1.getWidth(), bitmap1.getHeight(), 1).create();
	        PdfDocument.Page page = document.startPage(pageInfo);
	        Canvas canvas = page.getCanvas();

	        Paint paint = new Paint();
	        paint.setColor(Color.YELLOW);
	        paint.setStrokeWidth(5f);
	        paint.setTextSize(35f);

	        Rect rect =new Rect(0,0,bitmap1.getWidth(), bitmap1.getHeight());
	        canvas.drawBitmap(bitmap1, null,rect,  null);
	        canvas.drawText(Fname+".pdf", 30f, 30f, paint);
	        document.finishPage(page);

	        String pdffile=Fname+".pdf";
			String path = Environment.getExternalStorageDirectory()+
					Dir+"/"+pdffile; 
					File pdfFile = new File(path);

					   if (pdfFile.exists ()) pdfFile.delete (); 

	        try {
	            document.writeTo(new FileOutputStream(pdfFile));
	            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
	        } catch (IOException e) {
	            e.printStackTrace();
	            Toast.makeText(this, "Something wrong: " + e.toString(),
	                    Toast.LENGTH_LONG).show();
	        }
		
	        document.close();
	    }
		
		private Bitmap loadImageFromStorage()
		{
			Bitmap b=null;
		    ContextWrapper cw = new ContextWrapper(getApplicationContext());
		    // path to /data/data/yourapp/app_data/imageDir
		   File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
		   // Create imageDir
		   File mypath=new File(directory,"profile.jpg");
		    try {
//		        File f=new File(path, "profile.jpg");
		        b = BitmapFactory.decodeStream(new FileInputStream(mypath));
		    } 
		    catch (FileNotFoundException e) 
		    {
		        e.printStackTrace();
		    }
		return b;
		}		
		
		
		
}

