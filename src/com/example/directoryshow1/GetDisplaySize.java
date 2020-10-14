package com.example.directoryshow1;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

public class GetDisplaySize {
int widthPixels ,heightPixels;
Context context;

public GetDisplaySize(Context c){
	this.context=c;
	WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    Display d = w.getDefaultDisplay();
    DisplayMetrics metrics = new DisplayMetrics();
    d.getMetrics(metrics);
// since SDK_INT = 1;
widthPixels = metrics.widthPixels;
heightPixels = metrics.heightPixels;
// includes window decorations (statusbar bar/menu bar)
if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
try {
    widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
    heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
} catch (Exception ignored) {
}
// includes window decorations (statusbar bar/menu bar)
if (Build.VERSION.SDK_INT >= 17)
try {
    Point realSize = new Point();
    Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
    widthPixels = realSize.x;
    heightPixels = realSize.y;
} catch (Exception ignored) {
}

}

	public int getwidthPixels(){
		return widthPixels; 
	} 
	
	public int getheightPixels(){
		return heightPixels; 
	} 
	
	public int getViewParams (ImageView iv){
			
		
		return 0;
		
	}
	
}
