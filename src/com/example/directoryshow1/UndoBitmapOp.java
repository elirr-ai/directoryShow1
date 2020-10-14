package com.example.directoryshow1;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class UndoBitmapOp {

	
	static ArrayList<Bitmap> bmparr =new ArrayList<Bitmap>();
	
	
	
	public UndoBitmapOp (){
			}
	
	public void push(Bitmap b){
		bmparr.add(b);
	}
	public void clear(){
		bmparr.clear();
	}
	public int getSize(){
		return bmparr.size();
	}
	public Bitmap pop(){
		Bitmap b=null;
	if ( bmparr.size()>0){
		if (bmparr.get(bmparr.size()-1) != null){
	b=bmparr.get(bmparr.size()-1);
	bmparr.remove(bmparr.size()-1);
		
		}
	}	
		return b;
		
	}
	
}
