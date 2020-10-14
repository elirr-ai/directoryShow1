package com.example.directoryshow1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class FileReadWriteText {

	Context context;

	public FileReadWriteText(Context c){
		this.context=c;
		
	}
	
public String FileReadText (String d,String f)	{
	String text="";	
		try {
		  		
			File Path = new File(d);
			if(!Path.exists()) {
//			    Path.mkdirs();
			}
//			File file = new File(d+File.separator+f);
			if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				Toast.makeText(context, "Cannot use storage.", Toast.LENGTH_SHORT).show();
			    return text;
			}
			
			FileInputStream fis=new FileInputStream (new File(d+File.separator+f));
			InputStreamReader isr = new InputStreamReader(fis);
			char[] data=new char[256];  // data is char array with size=100
			String final_data="";
			int size;					
				try {
					while(  ( size=isr.read(data))>0 )
					{
					String read_data=String.copyValueOf(data, 0, size); 
					final_data+=read_data;	
					data = new char[256];
					text=final_data;
					}
										
				} catch (IOException e) {
					e.printStackTrace();
					}
						}
							
		 catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
	
	return text;
	
	
}
	
public void write_file_string(String dwrite_,String fname_, String data) {
	File rootPath = new File(dwrite_);
  if(!rootPath.exists()) {
      rootPath.mkdirs();
  }
 
//  File dataFile = new File(dwrite_+File.separator+fname_);  //get file name string
  if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
      Toast.makeText(context, "Cannot use storage.", Toast.LENGTH_SHORT).show();
      return;
  }
  
  try {           
      FileOutputStream mOutput = new FileOutputStream(new File(dwrite_+File.separator+fname_), false);
      mOutput.write(data.getBytes());
      mOutput.close();
  } catch (FileNotFoundException e) {
      e.printStackTrace();
  } catch (IOException e) {
      e.printStackTrace();
  }
  
  
}






}
