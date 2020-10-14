package com.example.directoryshow1;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.util.DisplayMetrics;
import android.view.Display;

public class MSTGetDisplay {

	public static String getDisplay1(Context c, Activity a){
		StringBuilder sb=new StringBuilder();
		final String DISPLAY_SERVICE="display",nl="\n";
		
		DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
		DisplayManager displayMetrics = (DisplayManager) c.getSystemService(DISPLAY_SERVICE);
		Display[] displays = displayMetrics.getDisplays();
//Display ddd=displayManager.getDisplay(0);

//		DisplayMetrics displayMetrics1 = new DisplayMetrics();
		
//ddd.getMetrics(displayMetrics);


//int qi=ddd.getDisplayId();
	
		
		sb.append("density: "+dm.density+nl);
		sb.append("scaledDensity: "+dm.scaledDensity+nl);
		sb.append("xdpi: "+dm.xdpi+nl);
		sb.append("ydpi: "+dm.ydpi+nl);
		sb.append("densityDpi: "+dm.densityDpi+nl);
		sb.append("heightPixels: "+dm.heightPixels+nl);
		sb.append("widthPixels: "+dm.widthPixels+nl);

//		getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
//		Display display = a.getWindowManager().getDefaultDisplay();
	for (int i=0;i<displays.length;i++){
		sb.append("getDisplayId: "+displays[i].getDisplayId()+nl);
		sb.append("getName: "+displays[i].getName()+nl);
		sb.append("getHeight: "+displays[i].getHeight()+nl);
		sb.append("getOrientation: "+displays[i].getOrientation()+nl);
		sb.append("getPixelFormat: "+displays[i].getPixelFormat()+nl);
		sb.append("getWidth: "+displays[i].getWidth()+nl);
		sb.append("getRefreshRate: "+displays[i].getRefreshRate()+nl);
		sb.append("getRotation: "+displays[i].getRotation()+nl);
		sb.append("isValid: "+displays[i].isValid()+nl);
	
	}
		
	return sb.toString();
		
		
	}
	
	
}
