package com.example.directoryshow1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.os.Environment;

public class FileReadStringMP3list {
//String filename="",dirname="";
	public FileReadStringMP3list (  ) 
	{
		};

	
	public static String getFileString(String dirname , String filename ) {		
		String Memo="";
		int data_block=8000;
		  try {			  
			  if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {					
					//Toast.makeText(getBaseContext(), "Cannot use storage.", Toast.LENGTH_SHORT).show();
					return "-1";
				}				
				File Path = new File(dirname);
				if(!Path.exists()) {
				    Path.mkdirs();
				}
				File file = new File(Path, filename);
				if (!file.exists()){
					return "-1";
				}
				
				
				
				FileInputStream fis=new FileInputStream (file);
				
				InputStreamReader isr = new InputStreamReader(fis);
				char[] data=new char[data_block];  // data is char array with size=100
				String final_data="";
				int size;					
					try {
						while(  ( size=isr.read(data))>0 ) 	{
						String read_data=String.copyValueOf(data, 0, size); 
						final_data+=read_data;	
						data = new char[data_block];
						Memo=final_data;
														}
											
					} catch (IOException e) {
						e.printStackTrace();
					}
		  			  
		  }
		 		  
		  catch (FileNotFoundException e) {
	  			// TODO Auto-generated catch block
	  			e.printStackTrace();
	  		}	
	    
						
		return Memo;
	}
	
	public static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line).append("\n");
	    }
	    reader.close();
	    return sb.toString();
	}

	public static String getStringFromFile (String filePath) throws Exception {
		
	    File fl = new File(filePath);
	    FileInputStream fin = new FileInputStream(fl);
	    String ret = convertStreamToString(fin);
	    //Make sure you close all streams.
	    fin.close();        
	    return ret;
	}
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
}  // End of class



