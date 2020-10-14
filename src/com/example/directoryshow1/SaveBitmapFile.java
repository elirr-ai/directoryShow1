package com.example.directoryshow1;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.os.Environment;

public class SaveBitmapFile {

	public SaveBitmapFile(){}
	
	public void saveBitmap(Bitmap b,String d, String f){
		String path = Environment.getExternalStorageDirectory()+
		"/"+d+"/"+f; 
		File imgFile = new File(path);

		   if (imgFile.exists ()) imgFile.delete (); 
		   try {
		       FileOutputStream out = new FileOutputStream(imgFile);
		       b.compress(Bitmap.CompressFormat.PNG, 100, out);
		       out.flush();
		       out.close();

		   } catch (Exception e) {
		       e.printStackTrace();
		   }
		
		
	}
	
}
