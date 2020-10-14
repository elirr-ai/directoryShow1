package com.example.directoryshow1;

import java.io.File;
import java.util.ArrayList;

import android.os.Environment;

public class getListofFileTypes {
	
	public getListofFileTypes(){
		super();
	}
	
	public static ArrayList<String>  getListofFiles (String dir, String[] p) {
	
	ArrayList<String> arr = new ArrayList<String>();
	arr.clear();
	File rootPath = new File(dir);
    if(!rootPath.exists()) {
        rootPath.mkdirs();
    }
    File[] filex = rootPath.listFiles();
    for (int i=0;i<filex.length;i++) {
     	String[] arr1=filex[i].toString().split("/");
     	for (int j=0;j<p.length;j++){
     	if ((arr1[arr1.length-1].endsWith(p[j])))
      			arr.add(arr1[arr1.length-1]);
     		}	    
    			}
	return arr;
	}
/*	
	public Integer  getNumberofFiles (String dir, String fileNmae , String Pattern) {
		int j=0;
		File rootPath = new File(Environment.getExternalStorageDirectory(), dir);
	    if(!rootPath.exists()) {
	        rootPath.mkdirs();
	    }
	    File[] filex = rootPath.listFiles();
	    for (int i=0;i<filex.length;i++) {
	     	String[] arr1=filex[i].toString().split("/");
	     	String[] arr2=arr1[5].split("_");
	//     	if (arr1[5].startsWith(fileNmae) && arr1[5].endsWith(Pattern)   )
	     		if (arr2[0].length()==fileNmae.length() &&
     			arr2[0].equalsIgnoreCase(fileNmae) && 
     			arr1[5].endsWith(Pattern)
     			)
     			j++;
	    	}	    
		return j;			
		}
*/	
	public static int getFileIndexLocation(String file,ArrayList<String> al){
		int j=-1;
		for (int i=0;i<al.size();i++){
			if (al.get(i).equals(file)){
				return i;
			}
	
		}

		return j;
		
	}
	
	
	
	
public ArrayList<String> getFiles (String dir){
	boolean error=false;
	File[] filex =null;
	ArrayList<String> temp_note = new ArrayList<String>();
	
	File rootPath = new File(Environment.getExternalStorageDirectory(), dir);
    if(!rootPath.exists()) {
        rootPath.mkdirs();
    }
    if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    	error=true;
    	//Toast.makeText(getApplicationContext(), "Cannot use storage.", Toast.LENGTH_SHORT).show();
        }
    if (!error){
    filex = rootPath.listFiles();
    temp_note.clear();
    for (int i=0;i<filex.length;i++) {      //get list of memo files 

      	String[] separated = filex[i].toString().split("/");//remove extra path and txt
      	if (!separated[5].endsWith(".mp3")
      		&& !separated[5].endsWith(".3gp") 
      		&&	!separated[5].endsWith(".JPG")
      		&& !separated[5].endsWith(".PNG") 
      		&& !separated[5].endsWith(".txt")	
      		&& !separated[5].endsWith(".hnd"))//  exclude all non note file types  
      		{
      		temp_note.add(separated[5]);	}
    		}
       	} 
  	return temp_note;
	}
	

public ArrayList<String> getFilesFullPath (String dir,String file){
	boolean error=false;
	File[] filex =null;
	ArrayList<String> t = new ArrayList<String>();
	
	File rootPath = new File(Environment.getExternalStorageDirectory(), dir);
    if(!rootPath.exists()) {
        rootPath.mkdirs();
    }
    if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
    	error=true;
    	//Toast.makeText(getApplicationContext(), "Cannot use storage.", Toast.LENGTH_SHORT).show();
        }
    if (!error){
    filex = rootPath.listFiles();
    t.clear();
    for (int i=0;i<filex.length;i++) {      //get list of memo files 

     	String[] arr1=filex[i].toString().split("/");
     	String[] arr2=arr1[5].split("_");
     	if (arr2[0].length()==file.length() &&
     			arr2[0].equalsIgnoreCase(file) &&	
     					(	arr1[arr1.length-1].endsWith("JPG") ||
     						arr1[arr1.length-1].endsWith("3gp")	||
     						arr1[arr1.length-1].endsWith("mp4") ||
     						arr1[arr1.length-1].endsWith("hnd")	)
     			)
     		t.add(arr1[arr1.length-1]);
    		}
       	} 
  	return t;
	}


	
}
