package com.example.directoryshow1;

import java.io.File;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

public class GetMemoryStatistics  {

	public static String getInternalMemory (Context c){
		//internal Total memory

    double giga=Math.pow(2, 30);
    double avaliableSpace = -1L;
    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
    	avaliableSpace = (long) stat.getBlockSizeLong() * (long) stat.getAvailableBlocksLong();
    else
    	avaliableSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
    
    double fullSpace= -1L;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2)
    	fullSpace = (long) stat.getBlockSizeLong() * (long) stat.getBlockCountLong();
    else
    	fullSpace = (long) stat.getAvailableBlocks() * (long) stat.getBlockSize();
     
    
	return "\nAvailable memory "+avaliableSpace/giga+" GB \nTotal memory "+fullSpace/giga+" GB";
	}
	
	
//	External free and total memory
	public static String geExternalMemory (Context c){
	 long freeBytesExternal =  new File(c.getExternalFilesDir(null).toString()).getFreeSpace();
	       int free = (int) (freeBytesExternal/ (1024 * 1024));
	        long totalSize =  new File(c.getExternalFilesDir(null).toString()).getTotalSpace();
	        int total= (int) (totalSize/ (1024 * 1024));
	       String availableMb = free+"Mb out of "+total+"MB";
		return availableMb;
	
	}
	
	
	
	
	public static long freeRamMemorySize(Context c) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) c.getSystemService("activity");
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.availMem / 1048576L;
        return availableMegs;
    }

    public static long totalRamMemorySize(Context c) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager)c.getSystemService("activity");
        activityManager.getMemoryInfo(mi);
        long availableMegs = mi.totalMem / 1048576L;
        return availableMegs;
    }
	
	
	
	
	
	
	
	
	
}
