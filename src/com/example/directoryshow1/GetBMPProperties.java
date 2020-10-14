package com.example.directoryshow1;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class GetBMPProperties {

	static ArrayList<String> getProperties (Bitmap bm){
 ArrayList<String> al = new ArrayList<String>();
		
		al.add("getAllocationByteCount:  "+String.valueOf(bm.getAllocationByteCount())+"\n");
		al.add("getByteCount:  "+String.valueOf(bm.getByteCount())+"\n");
		al.add("getDensity:  "+String.valueOf(bm.getDensity())+"\n");
		al.add("getGenerationId:  "+String.valueOf(bm.getGenerationId())+"\n");
		al.add("getHeight:  "+String.valueOf(bm.getHeight())+"\n");
		al.add("getWidth:  "+String.valueOf(bm.getWidth())+"\n");
	return al;
		
	}
	
	
}
