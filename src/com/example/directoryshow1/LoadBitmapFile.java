package com.example.directoryshow1;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class LoadBitmapFile {
int w,h;
int imageHeight;
int imageWidth;
	public LoadBitmapFile(int w, int h){
		this.w=w;
		this.h=h;
	}
	
	public Bitmap loadBitMap(String p, String f, int scale){
		Bitmap myBitmap=null;
		String path = Environment.getExternalStorageDirectory()+
		"/"+p+"/"+f; 
		File imgFile = new File(path);
		BitmapFactory.Options options = new BitmapFactory.Options();	
		 
	//	if (scale>1) scale=scale/2;
			options.inSampleSize = scale; 
		 
        if(imgFile.exists())
{

myBitmap=BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);                  
          }
return myBitmap;
   		
	}

public int getBitmapScale(String p, String f){
	String path = Environment.getExternalStorageDirectory()+
			"/"+p+"/"+f; 
			File imgFile = new File(path);
    BitmapFactory.Options o = new BitmapFactory.Options(); 
   o.inJustDecodeBounds = true; 
   BitmapFactory.decodeFile(imgFile.getAbsolutePath(),o); 
   //int REQUIRED_SIZE = 640; 
 int REQUIRED_SIZE = Math.min(w, h);
   int width_tmp = o.outWidth, height_tmp = o.outHeight; 
   int scale = 1; 
   while(true) { 
       if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) break; 
       width_tmp /= 2; 
       height_tmp /= 2; 
       scale *= 2; 
   } 
	return scale;
}
	
public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) 
{ // BEST QUALITY MATCH
    
    //First decode with inJustDecodeBounds=true to check dimensions
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(path, options);

    // Calculate inSampleSize, Raw height and width of image
    final int height = options.outHeight;
    final int width = options.outWidth;
    
    if (height==-1 || width==-1) {
    	return null;
    }
    else {
    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    int inSampleSize = 1;
    if (height > reqHeight) {
        inSampleSize = Math.round((float)height / (float)reqHeight);
    	}
    int expectedWidth = width / inSampleSize;
    if (expectedWidth > reqWidth) {
        //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
        inSampleSize = Math.round((float)width / (float)reqWidth);
    	}
    options.inSampleSize = inSampleSize;
    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(path, options);
    } 
}// end of method






	
	public String getImageHeight() {
		return String.valueOf(imageHeight); 
	}

	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}

	public String getImageWidth() {
		return String.valueOf(imageWidth);
	}

	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}
	
}
