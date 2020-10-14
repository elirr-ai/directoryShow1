package com.example.directoryshow1;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.os.Environment;
import android.os.StatFs;

public class MSTGetMemory {

	public static boolean externalMemoryAvailable() {
	    return android.os.Environment.
	            getExternalStorageState().equals(
	            android.os.Environment.MEDIA_MOUNTED);
	}
	
	  public static String getMemorySpecs(Activity a) {
		  String[] s=new String[]{
			"Available Blocks","Available GBytes","Block Count","Block Size (bytes)","Total GBytes", 
			"Total RAM memory (MB)   ","Available RAM memory (MB)   ","Threshold RAM memory (MB)   ",
			"Threshold low Ram?"   };
		  ArrayList<String> al = new ArrayList<String>();
		  final String ACTIVITY_SERVICE  =  "activity"; 		  
			ActivityManager actManager = (ActivityManager) a.getSystemService(ACTIVITY_SERVICE);
			MemoryInfo memInfo = new ActivityManager.MemoryInfo();
			actManager.getMemoryInfo(memInfo);
			
			float t1=(float)memInfo.totalMem/(1024.0f*1024.0f);
			float t2=(float)memInfo.availMem/(1024.0f*1024.0f);
			float t3=(float)memInfo.threshold/(1024.0f*1024.0f);
			boolean lowMem=memInfo.lowMemory;

	    File path = Environment.getDataDirectory();
	    StatFs stat = new StatFs(path.getPath());

	    	al.add(s[0]+":    "+stat.getAvailableBlocksLong());
	    	al.add(s[1]+":    "+(float)stat.getAvailableBytes() /(1024.0f*1024.0f*1024.0f)     );
	    	al.add(s[2]+":    "+stat.getBlockCountLong());
	    	al.add(s[3]+":    "+stat.getBlockSizeLong());
	    	al.add(s[4]+":    "+(float)stat.getTotalBytes() /(1024.0f*1024.0f*1024.0f)     );

	    	al.add(s[5]+":    "+t1      );
	    	al.add(s[6]+":    "+t2      );
	    	al.add(s[7]+":    "+t3     );
	    	al.add(s[8]+":    "+String.valueOf(lowMem) ) ;
	    	return 	convArray2String(al);

	}
	  
		public static String getVerboseRAM() {
		    RandomAccessFile reader = null;
		    String load = null;
			  ArrayList<String> al1 = new ArrayList<String>();
			  
		    try {
		        reader = new RandomAccessFile("/proc/meminfo", "r");
		     
		        do {
		        	load = reader.readLine();
		        		al1.add(load);
		        }
	        
		while ( load !=null);
	if (al1.size()>2){
		al1.remove(al1.size()-1);
	}
		        
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    } finally {
//		    	reader=null;
//		         Streams.close(reader);
		    }


		return convArray2String(al1);
		}

public static String convArray2String (ArrayList<String> alc ){
	StringBuilder sbb=new StringBuilder();
	for (int i=0;i<alc.size();i++){
		sbb.append(alc.get(i)+"\n");
	}
	return sbb.toString();
		}	
		
}
