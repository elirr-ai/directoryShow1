package com.example.directoryshow1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.media.ExifInterface;
//import android.support.media.ExifInterface;
//import android.media.ExifInterface;
import android.os.Environment;
import android.widget.Toast;

 public class GetExifDataFromBitMap {

	// GetExifDataFromBitMap(){}
	
//	static public ArrayList< ExifHolder>  getExifData(Context context,String dir, String file){
		static public ArrayList< ExifHolder>  getExifData(Context context,String f){
	
		ArrayList< ExifHolder> al = new ArrayList< ExifHolder>();
//		   File Path = new File(Environment.getExternalStorageDirectory(), dir);
//		    File f = new File(Path, file);
			ExifInterface exx = null;
			Toast.makeText(context, "EXIF  "+f.toString(), Toast.LENGTH_SHORT).show(); 			
//			if (!f.exists()) {
//				Toast.makeText(context, "no such file  "+f, Toast.LENGTH_SHORT).show(); 
//				al.clear();
//				al.add(new ExifHolder("STATUS", "NOT OK !!!!!!!!!!! ,  file does not exist  "));
//
//			}
			
//			else {			
			
			try {
				exx =new   ExifInterface(f.toString());
				al.add(new ExifHolder("STATUS", "OK"));
				al.add(new ExifHolder("DATETIME", exx.getAttribute(ExifInterface.TAG_DATETIME)));
				al.add(new ExifHolder("APERTURE", exx.getAttribute(ExifInterface.TAG_APERTURE)));
				al.add(new ExifHolder("EXPOSURE_TIME", exx.getAttribute(ExifInterface.TAG_EXPOSURE_TIME)));
				al.add(new ExifHolder("FLASH", exx.getAttribute(ExifInterface.TAG_FLASH)));
				al.add(new ExifHolder("FOCAL_LENGTH", exx.getAttribute(ExifInterface.TAG_FOCAL_LENGTH)));
				al.add(new ExifHolder("IMAGE_LENGTH", exx.getAttribute(ExifInterface.TAG_IMAGE_LENGTH)));

				al.add(new ExifHolder("IMAGE_WIDTH", exx.getAttribute(ExifInterface.TAG_IMAGE_WIDTH)));
				al.add(new ExifHolder("ISO", exx.getAttribute(ExifInterface.TAG_ISO)));
				al.add(new ExifHolder("MAKE", exx.getAttribute(ExifInterface.TAG_MAKE)));
				al.add(new ExifHolder("MODEL", exx.getAttribute(ExifInterface.TAG_MODEL)));
				
				
				al.add(new ExifHolder("ORIENTATION", exx.getAttribute(ExifInterface.TAG_ORIENTATION)));
				al.add(new ExifHolder("WHITE_BALANCE", exx.getAttribute(ExifInterface.TAG_WHITE_BALANCE)));
				
				

//				al.add(new ExifHolder("WHITEBALANCE_AUTO",String.valueOf(exx.getAttribute(ExifInterface.WHITEBALANCE_AUTO))));
String s1=exx.getAttribute(String.valueOf(ExifInterface.WHITEBALANCE_AUTO));	
al.add(new ExifHolder("WHITEBALANCE_AUTO", s1));
				
String s=exx.getAttribute(String.valueOf(ExifInterface.WHITEBALANCE_MANUAL));	
al.add(new ExifHolder("WHITEBALANCE_MANUAL", s));
		
//				String m=exx.getAttribute(ExifInterface.TAG_DATETIME);

			} catch (IOException e) {
				al.clear();
				al.add(new ExifHolder("STATUS", "NOT OK !!!!!!!!!!! ,  parse error  "));
	
				e.printStackTrace();
			}
//			String m=exx.getAttribute(ExifInterface.TAG_DATETIME);
//	}
			return al;
		
	}
	
	
	
	
	
	
	
	
}
