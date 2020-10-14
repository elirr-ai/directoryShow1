package com.example.directoryshow1;

import java.io.IOException;
import android.content.Context;
import android.net.ConnectivityManager;

public class SMBLyricsGetNetworkConnecionStatus {
		
	public static boolean isNetworkAvailable(Context c) {
	    final ConnectivityManager connectivityManager = 
	    		((ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE));
	    return connectivityManager.getActiveNetworkInfo() != 
	    		null && connectivityManager.getActiveNetworkInfo().isConnected();
	}
	
	public static boolean isInternetAvailable() {
		String command = "ping -c 1 -w 2000    www.google.com";
		boolean v = false;
	    try {
			 v= (Runtime.getRuntime().exec (command).waitFor() == 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return v;
	}
	
}
