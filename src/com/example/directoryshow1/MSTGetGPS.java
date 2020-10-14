package com.example.directoryshow1;

import java.util.List;

import android.content.Context;
import android.location.LocationManager;

public class MSTGetGPS {

	public static String   getGPSPrividers(Context c){
		
		LocationManager lmanager = (LocationManager)c.getSystemService(Context.LOCATION_SERVICE);
		return conv(lmanager.getAllProviders());
		
	}
	
	private static String conv(List<String> l  ){
		String nl="\n";
		StringBuffer sb=new StringBuffer();
		
		for (int i=0;i<l.size();i++){
			sb.append(l.get(i)+nl);
		}
		return sb.toString();
		
	}
	
	
	
}
