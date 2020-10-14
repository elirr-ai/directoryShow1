package com.example.directoryshow1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;

public class getAbout1 {

	Context context;
	
	public getAbout1(Context c) {
		this.context=c;
	}
	public ArrayList<String> getAppInfo(){
		ArrayList<String> al=new ArrayList<String>();
		long uncompressedSize=0;
		long compressedSize=0;
		 try{
		     ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0);
		     ZipFile zf = new ZipFile(ai.sourceDir);
		     Enumeration<? extends ZipEntry> e = zf.entries();
		     while (e.hasMoreElements()) {
		         ZipEntry ze = (ZipEntry) e.nextElement();
		         uncompressedSize += ze.getSize();
		         compressedSize += ze.getCompressedSize();
		       }
   
		     al.add("APK Name:" +zf.getName());
		     al.add("APK Entries\t"+String.valueOf(zf.size()));
		     
		     ZipEntry ze = zf.getEntry("META-INF/MANIFEST.MF");
		     long time = ze.getTime();
		     SimpleDateFormat formatter = (SimpleDateFormat) SimpleDateFormat.getInstance();
		     formatter.setTimeZone(TimeZone.getTimeZone("gmt"));
		     al.add("Create Time:\t"+formatter.format(new java.util.Date(time)) +"  (GMT TZ)");
		     zf.close();
		     al.add("CompressedSize:\t"+String.valueOf(compressedSize)+" bytes");
		     al.add("UNcompressedSize:\t"+String.valueOf(uncompressedSize)+" bytes");
		     
		  }catch(Exception e){
		  }
		 return al;
	 
	}

	public void showAlertDialog1(String s1,String s2){
		
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	    alertDialogBuilder.setIcon(R.drawable.filebrowser1);
	    alertDialogBuilder.setTitle(s1);
	    alertDialogBuilder.setMessage(s2);
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();	
	
		
	}
	
	public void showAlertDialog2(ArrayList<String> al){
		String s2="Directory Show - about\n\r";
		for (int i=0;i<al.size();i++){
			s2+=al.get(i)+"\n\r";
		}
		
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	    alertDialogBuilder.setIcon(R.drawable.filebrowser1);
	    alertDialogBuilder.setTitle(" ");
	    alertDialogBuilder.setMessage(s2);
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();	
	
		
	}
	
}
