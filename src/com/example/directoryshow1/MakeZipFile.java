package com.example.directoryshow1;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class MakeZipFile extends AsyncTask<Void, Integer, Integer> {
	public MyTaskInformer delegate = null;
	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	private ProgressDialog mProgressDialog;
		String dirfile;
		ArrayList<String> allFiles=new ArrayList<String>();	
		String zipFileName;
		Context context;
		int typeOfOp=0;
		ArrayList<DirectoryHolder1> af =new ArrayList<DirectoryHolder1>();
		boolean ready;

	
	public MakeZipFile(Context context1, String dirfile, String zipFileName, int op){ //dirfiel and zipfilename are same
		this.dirfile=dirfile;
		this.zipFileName=zipFileName;
		this.context=context1;
		this.typeOfOp=op;
		setProgressBar();
		af.clear();
		
		
	}
	
	public MakeZipFile(Context context1, String dirfile,int op){ //for Direcory file holder construction only
		this.dirfile=dirfile;
		this.context=context1;
		this.typeOfOp=op;
		setProgressBar();
		af.clear();
		
	}
	
	
	
	 private void setProgressBar() {
	     mProgressDialog = new ProgressDialog(context);
	     mProgressDialog.setMessage("Zipping files...");
	     mProgressDialog.setIndeterminate(false);
	     mProgressDialog.setMax(100);
	     mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	     mProgressDialog.setCancelable(true);		
	}

	public void zipper() throws IOException  //used to process flat memo dir
	    {

		 final int BUFFER=2048;
			File rootPath = new File(Environment.getExternalStorageDirectory(), dirfile);
		    if(!rootPath.exists()) {
		        rootPath.mkdirs();
		    }
			File zippath = new File(rootPath, zipFileName+".zip");		    
			try  { 
				  BufferedInputStream origin = null; 
				  FileOutputStream dest = new FileOutputStream(zippath); 

				  ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest)); 

				  byte data[] = new byte[BUFFER]; 

//				  for(int i=0; i < allFiles.size(); i++) { 
//					  publishProgress(  i  );
					  FileInputStream fi = new FileInputStream(rootPath.toString()+"/"+zipFileName); 
					  origin = new BufferedInputStream(fi, BUFFER); 
					  ZipEntry entry = new ZipEntry(zipFileName); 
					  out.putNextEntry(entry); 
					  int count; 
					  while ((count = origin.read(data, 0, BUFFER)) != -1) { 
				      out.write(data, 0, count); 
				    } 
				    origin.close(); 
//				  } 

				  out.close(); 
				} catch(Exception e) { 
				  e.printStackTrace(); 
				} 
			
	    }

public void ZipFolder(){  // do recursive directory view 
			File rootPath = new File(Environment.getExternalStorageDirectory(), dirfile);
		    if(!rootPath.exists()) {
		        rootPath.mkdirs();
		    }
	    
walk(rootPath);
addAllFilestoZip(rootPath);  
		    
	    }

public void walk(File root) {

    File[] list = root.listFiles();

    for (File f : list) {

        if (f.isDirectory()) {
        	af.add(new DirectoryHolder1(f.getAbsoluteFile(), f.isDirectory()));
            walk(f.getAbsoluteFile());
        }
        else {

        	
        	af.add(new DirectoryHolder1(f.getAbsoluteFile(), f.isDirectory(),f.isHidden(),
        			f.canExecute(),f.canRead(),f.canWrite(), f.length(),f.lastModified() 			
        			));    
// fname,isFile,isHidden,canExecute,canRead,canWrite,lenth,lastMod        	
        }
    }
}   

public ArrayList<DirectoryHolder1> getDirectoryAllfiles(){
	return af;
}
public boolean   getready(){
	return ready;
}

private void addAllFilestoZip(File rootPath2){
	 final int BUFFER=2048;
		File rootPath = new File(Environment.getExternalStorageDirectory(), dirfile);
	    if(!rootPath.exists()) {
	        rootPath.mkdirs();
	    }
		File zippath = new File(rootPath, dirfile+".zip");		    
		try  { 
			  BufferedInputStream origin = null; 
			  FileOutputStream dest = new FileOutputStream(zippath); 

			  ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest)); 

			  byte data[] = new byte[BUFFER]; 
			  for(int i=0; i < af.size(); i++) { 
				  publishProgress(  i  );

				  if (!af.get(i).isDir){
				  FileInputStream fi = new FileInputStream(af.get(i).getFile().toString()); 
			    origin = new BufferedInputStream(fi, BUFFER); 
			    ZipEntry entry = new ZipEntry(af.get(i).getFile().toString()); 
			    out.putNextEntry(entry); 
			    int count; 
			    while ((count = origin.read(data, 0, BUFFER)) != -1) { 
			      out.write(data, 0, count); 
			    } 
			    origin.close(); 
			  } 
			  }
			  out.close(); 
			} catch(Exception e) { 
			  e.printStackTrace(); 
			} 
		
	
}

	 @Override
	 protected void onPreExecute() {
	     super.onPreExecute();
	     ready=false;
	     mProgressDialog.show();
	 }

	 @Override
	 protected void onPostExecute(Integer unused) {
	     mProgressDialog.dismiss();
	     ready=true;
//Toast.makeText(context, "@@@ "+"   "+af.size(), Toast.LENGTH_SHORT).show();
	     delegate.onTaskDone(136785);
	 } 

	 protected void onProgressUpdate(Integer... progress) {
		 if (typeOfOp==1) {
int i1=(int) (((float) progress[0] / (float) allFiles.size()) * 100);
		 
mProgressDialog.setMessage("Zipping: "+allFiles.get(progress[0])+
		"    ("+String.valueOf(progress[0]+"/"+String.valueOf(allFiles.size()))+")");
	     mProgressDialog.setProgress(i1);
	     }
		 
		 if (typeOfOp==2) {
int i1=(int) (((float) progress[0] / (float) af.size()) * 100);
		 
mProgressDialog.setMessage("Zipping: "+af.get(progress[0]).getFile().toString()+
		"    ("+String.valueOf(progress[0]+"/"+String.valueOf(af.size()))+")");
	     mProgressDialog.setProgress(i1);
	     }
		 
	}	 
	 
	@Override
	protected Integer doInBackground(Void... params) {
try {
	if (typeOfOp==1)  zipper();
	if (typeOfOp==2)  ZipFolder();
	if (typeOfOp==3){
//		File rootPath = new File(Environment.getExternalStorageDirectory(), dirfile);
//	    if(!rootPath.exists()) {
//	        rootPath.mkdirs();
//	    }
    
//walk(rootPath);
		
		
	}
} catch (IOException e) {
	e.printStackTrace();
}
return null;
	}
	 
	
}
