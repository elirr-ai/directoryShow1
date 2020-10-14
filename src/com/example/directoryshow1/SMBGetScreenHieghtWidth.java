package com.example.directoryshow1;

import android.graphics.Point;
import android.view.Display;

public class SMBGetScreenHieghtWidth {

	Point size = new Point();
	
	public SMBGetScreenHieghtWidth(Display d) {
		d.getSize(size);
				}

	public int getmWidthScreen() {
		return size.x;
	}


	public int getmHeightScreen() {
		return size.y;
	}
	
}
