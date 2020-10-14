package com.example.directoryshow1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.os.Build;

public class MSTGetSoftwareBuildData {

	
	public static ArrayList<String>   getBuild()	{
		final String nl="\n";
		ArrayList<String> al= new ArrayList<String>();
		
		al.add("ID: "+Build.ID+nl); 
		al.add("BOARD: "+Build.BOARD+nl); 
		al.add("BOOTLOADER: "+Build.BOOTLOADER+nl); 
		al.add("BRAND: "+Build.BRAND+nl); 
		al.add("CPU_ABI: "+Build.CPU_ABI+nl); 

		al.add("CPU_ABI: "+Build.CPU_ABI2+nl); 

		al.add("DEVICE: "+Build.DEVICE+nl); 
		al.add("DISPLAY: "+Build.DISPLAY+nl); 
		al.add("FINGERPRINT: "+Build.FINGERPRINT+nl); 
		al.add("MANUFACTURER: "+Build.MANUFACTURER+nl); 
		al.add("RadioVersion: "+Build.getRadioVersion()+nl); 
		al.add("TAGS: "+Build.TAGS+nl); 
		al.add("HARDWARE: "+Build.HARDWARE+nl); 
		al.add("HOST: "+Build.HOST+nl); 
		al.add("ID: "+Build.ID+nl); 
		al.add("SERIAL: "+Build.SERIAL+nl); 
		al.add("TIME: "+String.valueOf(Build.TIME+nl)); 
		al.add("TIME1: "+millisToDate()+nl); 
		al.add("UNKNOWN: "+Build.UNKNOWN+nl); 
		al.add("MODEL: "+Build.MODEL+nl); 
		al.add("PRODUCT: "+Build.PRODUCT+nl); 
		al.add("TYPE: "+Build.TYPE+nl); 
		al.add("USER: "+Build.USER+nl); 
		al.add("VERSION.RELEASE: "+Build.VERSION.RELEASE+nl); 
		al.add("VERSION.CODENAME: "+Build.VERSION.CODENAME+nl); 
		al.add("VERSION.INCREMENTAL: "+Build.VERSION.INCREMENTAL+nl); 
		al.add("VERSION.SDK: "+Build.VERSION.SDK+nl); 
		al.add("VERSION.SDK_INT: "+Build.VERSION.SDK_INT+nl); 
		
return al;
		
	}	
	
	public static String convArray2String (ArrayList<String> alc ){
		StringBuilder sbb=new StringBuilder();
		for (int i=0;i<alc.size();i++){
			sbb.append(alc.get(i)+"");
		}
		return sbb.toString();
			}	
	
	private static String millisToDate(){
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss"); 
		String dateString = formatter.format(new Date(Build.TIME));
		
		
		
		return dateString;
	}
	
}
