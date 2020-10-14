package com.example.directoryshow1;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class BitmapEnhancment1 extends Activity 
	implements OnItemSelectedListener , OnTouchListener{

ActionBar actionBar;
Context context=this;	
GraphicsOps1 GO1;
ImageView iv,iv2;
private ProgressBar pb1;
TextView tv;
Spinner spinner;
View vv;
Bitmap myBitmap,backupBitmap;
List<String> categories = new ArrayList<String>();
ArrayList<OpsDataBase> db =new ArrayList<OpsDataBase>(); 
ArrayList<Bitmap> bmparr =new ArrayList<Bitmap>();
UndoBitmapOp UBO=new UndoBitmapOp();
String[] params=new String[4];
int last=-1;
private Handler customHandler1 = new Handler();
boolean blink=false;
float downx,downy,upx,upy;
int sPosition;
int ScreenWidth,ScreenHeight;
String bmpW,bmpH;
String path1;
String file;
SharedPreferences sharedpreferences;
public static final String MyPREFERENCES = "SVPrefs" ;
public static final String Name = "SAVE";	
public static final String Text = "TEXT";	
SharedPreferences spFullScreen1;
public static final String directory = "Directory";
public static final String file_name = "File_name";

private int height=0;
private int width=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_mainbitmapenhancment);
		vv=(View)findViewById(R.id.vv);
		tv = (TextView) findViewById (R.id.textView1);
		iv = (ImageView) findViewById (R.id.iv);
		pb1 = (ProgressBar)findViewById(R.id.pb1);
	    spinner = (Spinner) findViewById(R.id.spinner);
	    iv2 = (ImageView) findViewById(R.id.iv2);
	    
	    spinner.setOnItemSelectedListener(this);
		pb1.setVisibility(View.GONE);
		
		iv.setOnTouchListener(this);
		
        GetDisplaySize GDS=new GetDisplaySize(context);
        ScreenWidth=GDS.getwidthPixels();
        ScreenHeight=GDS.getheightPixels();
		

		
//		LoadBitmapFile LBF=new LoadBitmapFile(ScreenWidth,ScreenHeight);
//		myBitmap=LBF.loadBitMap( path1,file);
//		iv.setImageBitmap(myBitmap);
//		GO1.putBitmap(myBitmap);
		
		
//		String path = Environment.getExternalStorageDirectory()+
//				"/DILE/view1.png"; 
//        File imgFile = new File(path);
//        if(imgFile.exists())
//    {
//            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());                  
//            iv.setImageBitmap(myBitmap);
//            GO1.putBitmap(myBitmap);
//    }
//        else                    
//            Toast.makeText(this,"no IMAGE IS PRESENT'",Toast.LENGTH_SHORT).show();
//   
 
		initDB();
		sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
       
        // Creating adapter for spinner
ArrayAdapter<String> dataAdapter = 
new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
		GO1 = new GraphicsOps1(ScreenWidth,ScreenHeight);

		Bundle b= getIntent().getExtras();
		path1=b.getString("dir");
		file=b.getString("name");

			actionBar = getActionBar();
			actionBar.setIcon(R.drawable.imageenhancer1);
			actionBar.setDisplayShowTitleEnabled(true);

		sPosition=1;  // OP=load bmp
		runIt();
		


		
Toast.makeText(this,String.valueOf(ScreenWidth)+" / "+String.valueOf(ScreenHeight)
		,Toast.LENGTH_SHORT).show();
	
	}	

	private void initDB() {
		
		
		categories.add("idle");  //0
        categories.add("refresh");//1
        categories.add("gray");//2
        categories.add("gamma0.5-2.5");//3   //0.6-1.8
        categories.add("tint 50-80");//4   //tint 50-80
        categories.add("snow");//5
        categories.add("sharpen");//6
        categories.add("invert");//7
        categories.add("flip H");//8
        categories.add("flip V");//9
        categories.add("remove mean");//10
        categories.add("emboss");//11
        categories.add("brightness");//12
        categories.add("contrast");//13
        categories.add("AddText");//14
        categories.add("UNDO");//15
        categories.add("Clibrate");//16
        categories.add("Engrave");//17
        
        db.clear();
 //     db.add(new OpsDataBase("name", needSlide, opScale, compueConvolution));
        db.add(new OpsDataBase("idle", false, 0, false));  //0
        db.add(new OpsDataBase("refresh", false, 0, false)); //1
        db.add(new OpsDataBase("gray", false, 0, false)); //2
        db.add(new OpsDataBase("gamma", true, 2.5f, false));//3
        db.add(new OpsDataBase("tint", true, 23.0f, false)); //4
        db.add(new OpsDataBase("snow", false, 0, false));//5
        db.add(new OpsDataBase("sharpen", true, 60.0f, true));//6
        db.add(new OpsDataBase("invert", false, 0, false));//7 
        db.add(new OpsDataBase("flip H", false, 0, false));//8
        db.add(new OpsDataBase("flip V", false, 0, false)); //9
        db.add(new OpsDataBase("remove mean", false, 0, true));//10
        db.add(new OpsDataBase("emboss", false, 0, true));//11
        db.add(new OpsDataBase("brightness", true, 60.0f, false));//12 
        db.add(new OpsDataBase("contrast", true, 60.0f, false));//13
        db.add(new OpsDataBase("AddText", false, 0, false)); //14
        db.add(new OpsDataBase("UNDO", false, 0, false)); //15
        db.add(new OpsDataBase("calibrate", true, 0, false)); //16
        db.add(new OpsDataBase("engrave", false, 0, true)); //17
        
	}





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mainbitmapenhancment, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.setfilename) {
		showAlertDialogCust1 sDC1 =new showAlertDialogCust1(context,7.16f);
		myBitmap=GO1.getBitmap();
		sDC1.showDial("Enter File Name:",path1,file,myBitmap);
		}
		
		return super.onOptionsItemSelected(item);
	}
	
    private class AsyncTaskRunner extends AsyncTask <String[], Void, Bitmap> {


		@Override
		protected void onPreExecute() {
			params[0]=String.valueOf(sPosition); 
			UBO.push(GO1.getBitmap());
//			bmparr.add(myBitmap);
			pb1.setVisibility(View.VISIBLE);
			customHandler1.postDelayed(updateTimerThread1, 0);
			super.onPreExecute();
		}
		@Override
		protected Bitmap doInBackground(String[]... passing) {
			
			Bitmap bmp1 = null;
			String[] passed = passing[0]; //get passed array
			
			if (Integer.parseInt(passed[0])==1){  // load bitmap
				LoadBitmapFile LBF=new LoadBitmapFile(ScreenWidth,ScreenHeight);
				myBitmap=LBF.loadBitMap( path1,file,LBF.getBitmapScale( path1,file));
				GO1.putBitmap(myBitmap);
				bmpH=LBF.getImageHeight();
				bmpW=LBF.getImageWidth();
				return myBitmap;
				
		}	

		else if (Integer.parseInt(passed[0])==2){
		return GO1.ConvGrey();
	}
	else if (Integer.parseInt(passed[0])==3){
		return GO1.GammaCorrection(
				Float.valueOf(passed[1]),
				Float.valueOf(passed[2]),
				Float.valueOf(passed[3]));
	}
	
	else if (Integer.parseInt(passed[0])==4){
		return GO1.TintColor(
				Integer.valueOf(passed[1]));
	}
	else if (Integer.parseInt(passed[0])==5){
		return GO1.applySnowEffect();
	}
	else if (Integer.parseInt(passed[0])==6){
		return GO1.sharpen(Double.parseDouble(passed[1]));
	}
	else if (Integer.parseInt(passed[0])==7){
		return GO1.doInvert(myBitmap);
	}
	else if (Integer.parseInt(passed[0])==8){
		return GO1.flipHorizontal();
	}
	else if (Integer.parseInt(passed[0])==9){
		return GO1.flipVertical();
	}
	else if (Integer.parseInt(passed[0])==10){
		return GO1.applyMeanRemoval();
	}
	else if (Integer.parseInt(passed[0])==11){
		return GO1.emboss();
	}
	else if (Integer.parseInt(passed[0])==12){
		return GO1.doBrightness(Integer.valueOf(passed[1]));
	}
	else if (Integer.parseInt(passed[0])==13){
		return GO1.createContrast(Integer.valueOf(passed[1]));
	}
	else if (Integer.parseInt(passed[0])==17){
		return GO1.engrave();
	}
			return bmp1;
		}
		
//		@Override
//		protected void onProgressUpdate(Integer... values) {
//			tv.setText(String.valueOf(values[0]));
//			super.onProgressUpdate(values);
//		}
		
		protected void onPostExecute(Bitmap result) {
			actionBar.setTitle("View "+file+"  "+
					String.valueOf(myBitmap.getWidth())+" x "+
					String.valueOf(myBitmap.getHeight()));

			spinner.setSelection(0);
			pb1.setVisibility(View.GONE);
			tv.setText("100 / 100");
			customHandler1.removeCallbacks(updateTimerThread1);
			iv2.setImageResource(R.drawable.greencircle);
	        Matrix matrix=new Matrix();
			if (null!=result) {
	            float bmsx=(float)( (float)result.getWidth()/(float)ScreenWidth);
	            float bmsy=(float)( (float)result.getHeight()/(float)ScreenHeight);
	            if (bmsx>bmsy){
            	matrix.preScale(  (float)(1/bmsx), (float)((float)(1/bmsx)));}
	            else {
	            	   matrix.preScale(  (float)(1/bmsy), (float)((float)(1/bmsy)));}
	            iv.setImageMatrix(matrix);
	            iv.setScaleType(ScaleType.MATRIX);
	            iv.setImageBitmap(result);

	        	
	        	
	        	
	        	
//	        	iv.setImageBitmap(result);
	        	myBitmap=result;
	        	GO1.putBitmap(myBitmap);
	        	        }
	         Toast.makeText(getApplicationContext(), "done"+bmpW+" "+bmpH,
	        		 Toast.LENGTH_SHORT).show();
	     }

	
    }

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
	      // On selecting a spinner item
sPosition=position;
String item = parent.getItemAtPosition(position).toString();	      
	      // Showing selected spinner item
//Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
 
//////////////

if (sPosition==1){  // OP load file
	UBO.clear();
//	bmparr.clear();
	runIt();
//	String path1="DILE";
//	String file="view1.png";
//	LoadBitmapFile LBF=new LoadBitmapFile(ScreenWidth,ScreenHeight);
//	myBitmap=LBF.loadBitMap( path1,file);
//	GO1.putBitmap(myBitmap);
//	iv.setImageBitmap(myBitmap);
}


if (sPosition==15){
	Bitmap b=UBO.pop();

	  if (null!=b){
		  myBitmap=b;  
		  GO1.putBitmap(myBitmap);
		  iv.setImageBitmap(myBitmap);
		  spinner.setSelection(0);
		  tv.setText("Stack= "+String.valueOf(UBO.getSize()-1));
		  Toast.makeText(this,"Un-done ("+String.valueOf(UBO.getSize()-1)+")",Toast.LENGTH_SHORT).show();

	  }	
	  else {
		  Toast.makeText(this,"Nothing to undo ("+String.valueOf(UBO.getSize()-1)+")",Toast.LENGTH_SHORT).show();
		  tv.setText("Stack empty");

	  }
}

if (sPosition == 2) {// OP=makegrey
	runIt();
}

if (sPosition == 5) {// OP=snow
	runIt();	
	
}		

//if (sPosition == 6) {// OP=sharpen
//	params[1]=String.valueOf(3);
//	runIt();	
//	}	

if (sPosition == 7) {// OP=invert
	runIt();
	}		

if (sPosition == 8) {// OP=flip hor
	runIt();
	}	
if (sPosition == 9) {// OP=flip ver
	runIt();
	}	

if (sPosition == 10) {// OP=mean
	runIt();
	}	

if (sPosition == 11) {// OP=emboss
	runIt();
	}	
if (sPosition == 17) {// OP=engrave
	runIt();
	}
if (sPosition==14){  // add text
		UBO.push(myBitmap);
		sPosition=0;
		spinner.setSelection(0);
		showAlertDialogCust1 sDC1 =new showAlertDialogCust1(context,7.16f);
		sDC1.getText(iv, GO1,upx,upy);
	}   
/////////////////





	}





	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void runIt(){//////////////////////////////////////////////
	AsyncTaskRunner	runner = new AsyncTaskRunner();
	runner.execute(params);	
	}
	
    private Runnable updateTimerThread1 = new Runnable() {
        public void run() {
        	int z=0;
	if (blink)
        	iv2.setImageResource(R.drawable.greencircle);
	else
		iv2.setImageResource(R.drawable.redcircle);
	blink=!blink;
//	tv.setText(String.valueOf(GO1.XX*GO1.YY+GO1.YY)+" / "+
//			String.valueOf(GO1.HEIGHT*GO1.WIDTH));
	
	
	if (!db.get(sPosition).isCompueConvolution()){  //no convolution
	double x=0,y=0;
	if (GO1.invertXY==0){
	x=GO1.XX*GO1.HEIGHT+GO1.YY;
	y=GO1.HEIGHT*GO1.WIDTH;
	z=(int)((x/y)*100);}
	else{
			x=GO1.YY*GO1.WIDTH+GO1.XX;
			y=GO1.HEIGHT*GO1.WIDTH;
			z=(int)((x/y)*100);
	}
        }
	else {  //with convolution
	double x=0,y=0;
	
	x=ConvolutionMatrix.XX;
	y=ConvolutionMatrix.YY;
	double h11=(double)ConvolutionMatrix.HEIGHT;
	//y=ConvolutionMatrix.HEIGHT*GO1.WIDTH;
//	z=(int) ((int)  y*100/h11);
	z=(int)((ConvolutionMatrix.YY*100)/(double)ConvolutionMatrix.HEIGHT);
	        }
	
	
	
	
	
	tv.setText(String.valueOf(z)+" / 100");
           customHandler1.postDelayed(this, 400);
        }
    };

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int action = event.getAction();
		
		switch (action) {
	    case MotionEvent.ACTION_DOWN:
	      downx = event.getX();
	      downy = event.getY();
	      break;
	    case MotionEvent.ACTION_MOVE:
	    	if (sPosition == 3) {
	    		float mvval= Math.abs(event.getX()-downx)/(ScreenWidth/2.5f);
	    		String formattedString = String.format("%.2f", mvval);
	    		tv.setText("GAMMA= "+formattedString);
	    	}    
	    	if (sPosition == 4) {
	    		float mvval= Math.abs(event.getX()-downx)/(ScreenWidth/23.0f);
	    		String formattedString = String.format("%.2f", mvval);
	    		tv.setText("TINT= "+formattedString);
	    	} 
	    	if (sPosition == 12) {
	    		float mvval= (event.getX()-downx)/(ScreenWidth/60.0f);
	    		String formattedString = String.format("%.2f", mvval);
	    		tv.setText("BRIGHTNESS= "+formattedString);
	    	} 
	    	
	    	if (sPosition == 13) {
	    		float mvval= (event.getX()-downx)/(ScreenWidth/60.0f);
	    		String formattedString = String.format("%.2f", mvval);
	    		tv.setText("CONTRAST= "+formattedString);
	    	} 

	    	if (sPosition == 6) {
	    		float mvval= (event.getX()-downx)/(ScreenWidth/60.0f);
	    		String formattedString = String.format("%.2f", mvval);
	    		tv.setText("Sharpen= "+formattedString);
	    	}
	      break;
	    case MotionEvent.ACTION_UP:
	    	showAllDispParams();
	      upx = event.getX();
	      upy = event.getY();
	      float diffx=upx-downx;
	      float diffy=upy-downy;
	      String sx=String.valueOf(diffx);
	      String sy=String.valueOf(diffy);
    Toast.makeText(context,"x "+String.valueOf(upx)+
		"y "+String.valueOf(upy),Toast.LENGTH_SHORT).show();

 
   if (sPosition == 3) {
	    		params[0]="3";  // OP=gamma18
	    		params[1]=String.valueOf(Math.abs(diffx/(ScreenWidth/2.5f)));  
	    		params[2]=String.valueOf(Math.abs(diffx/(ScreenWidth/2.5f)));  
	    		params[3]=String.valueOf(Math.abs(diffx/(ScreenWidth/2.5f)));    
	    		runIt();	
  		}
   if (sPosition == 4) {
		params[0]="4";  // OP=tint50
		params[1]=String.valueOf((int)(diffx/((float)(ScreenWidth)/23.0f)));
		runIt();
		}
   if (sPosition == 12) {
		params[0]="12";  // OP=brightness
		//params[1]=String.valueOf(30);
		params[1]=String.valueOf((int)(diffx/((float)(ScreenWidth)/60.0f)));
		runIt();	
		}	
   if (sPosition == 13) {
		params[0]="13";  // OP=contrast
		params[1]=String.valueOf((int)(diffx/((float)(ScreenWidth)/60.0f)));
		runIt();	
		}
if (sPosition == 6) {// OP=sharpen
	params[1]=String.valueOf((double)(diffx/((float)(ScreenWidth)/60.0f)));
	runIt();	
	}	  
   
   
//Toast.makeText(this,"x= "+sx+ "y= "+sy,Toast.LENGTH_SHORT).show();

	      
	      
	      
	      break;
	    case MotionEvent.ACTION_CANCEL:
	      break;
	    default:
	      break;
	    }
		
		
		return true;
	}
	
	public void showAllDispParams(){
		GetDisplaySize gt=new GetDisplaySize(context);
	}

	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

if (height==0 || width==0){
			 height=vv.getHeight();
	         width=vv.getWidth();
  
android.view.ViewGroup.LayoutParams params3 = tv.getLayoutParams();
		params3.height = getResources().getDimensionPixelSize(R.dimen.height3);
		params3.width = getResources().getDimensionPixelSize(R.dimen.width2);
tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				getResources().getDimension(R.dimen.font_size2));
tv.setBackgroundColor(Color.YELLOW);
tv.setLayoutParams(params3);
        
ViewGroup.MarginLayoutParams p = 
(ViewGroup.MarginLayoutParams) iv2.getLayoutParams();
p.setMargins(20,0,20,0);
iv2.requestLayout();

android.view.ViewGroup.LayoutParams params3b = iv2.getLayoutParams();
params3b.height = getResources().getDimensionPixelSize(R.dimen.height2);
params3b.width = getResources().getDimensionPixelSize(R.dimen.width3);
iv2.setLayoutParams(params3b);

ViewGroup.MarginLayoutParams p1 = 
(ViewGroup.MarginLayoutParams) pb1.getLayoutParams();
p1.setMargins(width/100,0,width/100,0);
p1.height=height/20;
p1.width=width/10;
pb1.requestLayout();
     
android.view.ViewGroup.LayoutParams params3a = spinner.getLayoutParams();
params3a.height = getResources().getDimensionPixelSize(R.dimen.height1);
params3a.width = getResources().getDimensionPixelSize(R.dimen.width2);
spinner.setLayoutParams(params3a);
}
		super.onWindowFocusChanged(hasFocus);
	}
		
	
	
	
}
//  https://xjaphx.wordpress.com/learning/tutorials/