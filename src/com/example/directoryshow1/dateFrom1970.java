package com.example.directoryshow1;

import java.text.SimpleDateFormat;
import java.util.Date;

public class dateFrom1970 {
	Date dt;
	String dt1;
	long curMillis;
	
	public dateFrom1970(){
		super();
	}
	
	public String getmillisString() {
		dt= new Date();
		return String.valueOf(dt.getTime());
	}
	
	public long getmillisLong() {
		dt= new Date();
		long curMillis = dt.getTime();
		return curMillis;
	}
	
	public String getDateString(){
		dt= new Date();
        SimpleDateFormat dateformat = new SimpleDateFormat("MMM_dd_yyyy_HH_mm_ss");
        return dateformat.format(dt);
        
		
	}
	
	
	
	
}
