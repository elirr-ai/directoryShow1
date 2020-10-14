package com.example.directoryshow1;

import android.content.res.Resources;
import android.util.DisplayMetrics;

public class displ {
//double wp;
//double xdpi;
//double x;
//double hp;
//double ydpi;
//double y ;
DisplayMetrics dm;

	public displ( ) {
		super();
		dm = Resources.getSystem().getDisplayMetrics();
		}
		
		//(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		
		//DisplayMetrics dm = new DisplayMetrics();
	//	getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		 //wp=dm.widthPixels;
		// xdpi=dm.xdpi;
		// double x = Math.pow(dm.widthPixels/dm.xdpi,2);
		
		// hp=dm.heightPixels;
		// ydpi=dm.ydpi;
		// double y = Math.pow(dm.heightPixels/dm.ydpi,2);

	
public double getScreenInches(){
	return Math.sqrt(Math.pow(dm.widthPixels/dm.xdpi,2)+Math.pow(dm.heightPixels/dm.ydpi,2));
}	
	
public double getScreenWidth(){
	return dm.widthPixels;
}	

public double getScreenheight(){
	return dm.heightPixels;
}	
	
}
