package com.example.directoryshow1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

public class MSTFileWriteString {
//String filename="",dirname="",stringdata="";
public MSTFileWriteString (){};
	
public static String setFileString(String dirname , String filename , String stringdata  ) {

	String  errorcode="0";
	File rootPath = new File(Environment.getExternalStorageDirectory(), dirname);
    if(!rootPath.exists()) {
        rootPath.mkdirs();
    }
    File dataFile = new File(rootPath, filename);
    if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    	errorcode="-1";
    }
    try {           
        FileOutputStream mOutput = new FileOutputStream(dataFile, false);
        mOutput.write(stringdata.getBytes());
        mOutput.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } 
	return errorcode; 
}


public static String setFileStringSingle(String f , String stringdata  ) {

	String  errorcode="0";
	File af=new File(f);

    try {           
        FileOutputStream mOutput = new FileOutputStream(af, false);
        mOutput.write(stringdata.getBytes());
        mOutput.close();
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    } 
	return errorcode; 
}


	
}



