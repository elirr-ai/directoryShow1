package com.example.directoryshow1;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

public class ReadPdf_MainActivity extends Activity implements OnTouchListener{
	
	
	private static final String TAG = "Touch";
//	private boolean fl10=true;
	// These matrices will be used to move and zoom image
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	// We can be in one of these 3 states
	
	static final int maxXmovement=1000,maxYdiff=300;
	static final int NOACTION=0,MOVRRIGHT=1,MOVELEFT=2;
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
	
//	String d="DILE",f="qq.pdf";
	String targetPdf = "/sdcard/test.pdf";
    ImageView pdfView;
	TextView tv;
	Context context=this;
	int currentPage=0,maxPage;
	String d,f;
	HandleFromPDF handleFromPdf;
	Bitmap bm;
	
	static int mWidthScreen;
	static int mHeightScreen;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pdfview_activity_main);
		tv = (TextView)findViewById(R.id.tv);

		pdfView = (ImageView)findViewById(R.id.pdfview);
		pdfView.setOnTouchListener(this);  
		 d = getIntent().getExtras().getString("dir");
		 f = getIntent().getExtras().getString("file");
		handleFromPdf=new HandleFromPDF(context,d,f);
			 maxPage=handleFromPdf.getPDFCountPages();	
		        new ReadPdf(this).execute();
			    Display disp= getWindowManager().getDefaultDisplay();
			    GetScreenHieghtWidth g1 = new GetScreenHieghtWidth(disp);
				mWidthScreen=g1.getmWidthScreen();
			    mHeightScreen=g1.getmHeightScreen();		 
	Toast.makeText(this, "Max pages...  "+maxPage, Toast.LENGTH_LONG).show();
		 


		
		
	       //Converting PDF to Bitmap Image...
//        StandardFontTF.mAssetMgr = getAssets();
        //Load document to get the first page
//        try {
//            PDFDocument pdf = new PDFDocument("/sdcard/test.pdf", null);
            
//            int y=pdf.getPageCount();
//            PDFPage page = pdf.getPage(0);
//            int width = (int)Math.ceil(page.getDisplayWidth());
//            int height = (int)Math.ceil(page.getDisplayHeight());
//            Bitmap bm = Bitmap.createBitmap(width, height, Config.ARGB_8888);
//            Canvas c = new Canvas(bm);
//            page.paintPage(c);
////            OutputStream os = new FileOutputStream("/sdcard/test.jpg");  //Saving the Bitmap
////            bm.compress(CompressFormat.PNG, 100, os);
////            os.close();
 
//        } catch (PDFException e) {
//            e.printStackTrace();
//        } 
//        catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } 
//        catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
	}

	class ReadPdf extends AsyncTask<Void, Void, Void> {
		private ProgressDialog dialog;
		
		public ReadPdf(ReadPdf_MainActivity activity) {
			dialog = new ProgressDialog(activity);
		}
		
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        	dialog.setMessage("Loading....  , please wait....");
    		dialog.show();
        }

        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(Void   unused) {
 	       tv.setText(""+(currentPage+1)+" / "+handleFromPdf.getPDFCountPages()+" ");
//           pdfView.setImageBitmap(bm);		
           if (dialog.isShowing()) {
   			dialog.dismiss();
           }
   			
   			
            float bmsx=(float)( (float)bm.getWidth()/(float)mWidthScreen);
            float bmsy=(float)( (float)bm.getHeight()/(float)mHeightScreen);
//            Matrix matrix = new Matrix();
            matrix.reset();
			if (false){
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
			
			
			pdfView.setImageMatrix(matrix);
			pdfView.setScaleType(ScaleType.MATRIX);
            pdfView.setImageBitmap(bm);
        }

		@Override
		protected Void doInBackground(Void... params) {
			 bm=handleFromPdf.getBitmapFromPDFFile(currentPage);
			return null;
		}
    } 

		


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.pdfviewmain, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		if (id == R.id.next) {
			incrementPage();
			return true;
		}
		
		if (id == R.id.previous) {
			decremantPage();
			return true;
		}

		if (id == R.id.status) {
	try {		
			
			
			ArrayList<String> al1=handleFromPdf.getPDFMetadata();	
String s=null;
for (int i=0;i<al1.size();i++){
	s+=al1.get(i);
}
int resID = getResources().getIdentifier("pdf1", 
		"drawable", getPackageName());
		    AlertDialog.Builder builder;
		    builder = new AlertDialog.Builder(context);
		    builder.setTitle("PDF data")
		    .setMessage(s)
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        }
		     })
		    
		    .setIcon(resID)
		    .show();
	}
	catch (Exception e) {
      e.printStackTrace();
  	Toast.makeText(this, "Missing or Wrong META data...", Toast.LENGTH_LONG).show();

  }	
			
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		   ImageView view = (ImageView) v;

		   // make the image scalable as a matrix
		   view.setScaleType(ImageView.ScaleType.MATRIX);
		   float scale;
		
		   switch (event.getAction() & MotionEvent.ACTION_MASK) {

		   case MotionEvent.ACTION_DOWN: //first finger down only
		      savedMatrix.set(matrix);
		      start.set(event.getX(), event.getY());
		      Log.d(TAG, "mode=DRAG" );
		      mode = DRAG;
		      break;
		   case MotionEvent.ACTION_UP: //first finger lifted
		if (mode == DRAG) {
			mode = NONE;
			      Log.d(TAG, "mode=NONE" );
			      }
			      break;
		   case MotionEvent.ACTION_POINTER_UP: //second finger lifted
			   finger1End.x=event.getX(0);
			   finger1End.y=event.getY(0);
			   finger2End.x=event.getX(1);
			   finger2End.y=event.getY(1);			   
		   
if (checkParallel()==1){
	Toast.makeText(this, "Right...", Toast.LENGTH_SHORT).show();
incrementPage();
	}
if (checkParallel()==2){
	Toast.makeText(this, "Left...", Toast.LENGTH_SHORT).show();
decremantPage();

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
		      if (oldDist > 5f ) {
		         savedMatrix.set(matrix);
		         midPoint(mid, event); // sets the mid-point of the straight line between two points where user touched. 
		         mode = ZOOM;
		         Log.d(TAG, "mode=ZOOM" );
		      }
//		      else {
//		    	  mode=DRAG2FINGERS;
//Toast.makeText(this, "drag 2 fingers", Toast.LENGTH_SHORT).show();
//  	  
//		      }
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
	
	
	private void decremantPage() {
		if (currentPage>0){
			currentPage--;
			new ReadPdf(this).execute();
				}
	}

	private void incrementPage() {
		if (currentPage<maxPage-1){
			currentPage++;
			new ReadPdf(this).execute();
				}
	}

	private float spacing(MotionEvent event) {
		   float x = event.getX(0) - event.getX(1);
		   float y = event.getY(0) - event.getY(1);
		   return (float) Math.sqrt(x * x + y * y);
		}

	private int checkParallel() {

		if	( Math.abs(finger1Start.x-finger1End.x)>maxXmovement &&
				(finger1Start.x<finger1End.x) &&
				( Math.abs(finger2Start.x-finger2End.x)>maxXmovement) &&
//						(finger2Start.x<finger2End.x)	&&
						
						( Math.abs(finger1Start.y-finger1End.y))<maxYdiff &&
								(finger1Start.y>finger1End.y) &&
								( Math.abs(finger2Start.y-finger2End.y))<maxYdiff 
//								&&
//										(finger2Start.y>finger2End.y)
										)		
				
				{
			return MOVRRIGHT;
		}
		

		if	( Math.abs(finger1Start.x-finger1End.x)>maxXmovement &&
				(finger1Start.x>finger1End.x) &&
				( Math.abs(finger2Start.x-finger2End.x)>maxXmovement) &&
						(finger2Start.x>finger2End.x)	&&
						
						( Math.abs(finger1Start.y-finger1End.y))<maxYdiff &&
//								(finger1Start.y>finger1End.y) &&
								( Math.abs(finger2Start.y-finger2End.y))<maxYdiff
//								&&
//										(finger2Start.y>finger2End.y)
								)		
				
				{
			return MOVELEFT;
		}
		
		
//		   float x = event.getX(0) - event.getX(1);
//		   float y = event.getY(0) - event.getY(1);
		else  return NOACTION;
		}
	
		private void midPoint(PointF point, MotionEvent event) {
		   float x = event.getX(0) + event.getX(1);
		   float y = event.getY(0) + event.getY(1);
		   point.set(x / 2, y / 2);
		}
	
	
	
	
	
	
}
//https://stackoverflow.com/questions/3642928/adding-a-library-jar-to-an-eclipse-android-project
//https://stackoverflow.com/questions/8814758/convert-a-pdf-page-into-bitmap-in-android-java/16294833#16294833
//
//
//


//Now for the missing class problem.
//
//I'm an Eclipse Java EE developer and have been in the habit for many years of adding third-party libraries via the "User Library" mechanism in Build Path. Of course, there are at least 3 ways to add a third-party library, the one I use is the most elegant, in my humble opinion.
//
//This will not work, however, for Android, whose Dalvik "JVM" cannot handle an ordinary Java-compiled class, but must have it converted to a special format. This does not happen when you add a library in the way I'm wont to do it.
//
//Instead, follow the (widely available) instructions for importing the third-party library, then adding it using Build Path (which makes it known to Eclipse for compilation purposes). Here is the step-by-step:
//
//    Download the library to your host development system.
//    Create a new folder, libs, in your Eclipse/Android project.
//    Right-click libs and choose Import -> General -> File System, then Next, Browse in the filesystem to find the library's parent directory (i.e.: where you downloaded it to).
//    Click OK, then click the directory name (not the checkbox) in the left pane, then check the relevant JAR in the right pane. This puts the library into your project (physically).
//    Right-click on your project, choose Build Path -> Configure Build Path, then click the Libraries tab, then Add JARs..., navigate to your new JAR in the libs directory and add it. (This, incidentally, is the moment at which your new JAR is converted for use on Android.)
//
//NOTE
//
//Step 5 may not be needed, if the lib is already included in your build path. Just ensure that its existence first before adding it.
//
//What you've done here accomplishes two things:
//
//    Includes a Dalvik-converted JAR in your Android project.
//    Makes Java definitions available to Eclipse in order to find the third-party classes when developing (that is, compiling) your project's source code.

////
    


