package com.example.directoryshow1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MSTGetCPUID {

	
	   public static String getCPUInfo ()  {
String nl="\n";
	        BufferedReader br = null;
			try {
				br = new BufferedReader (new FileReader ("/proc/cpuinfo"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

	        String str;
	        ArrayList<String> al= new ArrayList<String>();
	        try {
				while ((str = br.readLine ()) != null) {
				    String[] data = str.split (":");
				    if (data.length > 1) {
				        String key = data[0].trim ().replace (" ", "_");
				        String value = data[1].trim ();
				        al.add(key+": "+value+nl);
				    				}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

	        try {
				br.close ();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return convArray2String(al);
	    }
	
	   public static String convArray2String (ArrayList<String> alc ){
			StringBuilder sbb=new StringBuilder();
			for (int i=0;i<alc.size();i++){
				sbb.append(alc.get(i)+"\n");
			}
			return sbb.toString();
				}	
	   
	   
}
