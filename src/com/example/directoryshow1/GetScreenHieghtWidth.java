package com.example.directoryshow1;

import android.graphics.Point;
import android.view.Display;

public class GetScreenHieghtWidth {

	Point size = new Point();
	
	public GetScreenHieghtWidth(Display d) {
		d.getSize(size);
				}

	public int getmWidthScreen() {
		return size.x;
	}


	public int getmHeightScreen() {
		return size.y;
	}
	
}
