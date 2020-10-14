package com.example.directoryshow1;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.qoppa.android.pdf.DocumentInfo;
import com.qoppa.android.pdf.PDFException;
import com.qoppa.android.pdfProcess.PDFDocument;
import com.qoppa.android.pdfProcess.PDFPage;
import com.qoppa.android.pdfViewer.fonts.StandardFontTF;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.os.Environment;

public class HandleFromPDF {

	String fileLocation,path1;
	Context c;
	public HandleFromPDF(Context c,String d, String f){
		
		String path = Environment.getExternalStorageDirectory()+"/"+
				d+"/"+f; 
		String path1 = Environment.getExternalStorageDirectory()+"/"+
				d+"/"+f; 
		File pdfFile = new File(path);
		this.fileLocation=pdfFile.toString();
        this.c=c;
        this.path1=d+File.separator+f;
	}
	
	public Bitmap getBitmapFromPDFFile(int pageNumber){
		Bitmap bm = null;
		StandardFontTF.mAssetMgr = c.getAssets();
        try {
            PDFDocument pdf = new PDFDocument(path1, null);
            PDFPage page = pdf.getPage(pageNumber);
            int width = (int)(page.getDisplayWidth());
            int height = (int)(page.getDisplayHeight());
            bm = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas c = new Canvas(bm);
            page.paintPage(c);
////            OutputStream os = new FileOutputStream("/sdcard/test.jpg");  //Saving the Bitmap
////            bm.compress(CompressFormat.PNG, 100, os);
////            os.close();
        } catch (PDFException e) {
            e.printStackTrace();
        } 	
		return bm;
	}
	
	public int getPDFCountPages(){
		int y=-1;
		StandardFontTF.mAssetMgr = c.getAssets();
        try {
            PDFDocument pdf = new PDFDocument(path1, null);
            DocumentInfo di =pdf.getDocumentInfo();
//            di.
            
            
            y=pdf.getPageCount();
////            OutputStream os = new FileOutputStream("/sdcard/test.jpg");  //Saving the Bitmap
////            bm.compress(CompressFormat.PNG, 100, os);
////            os.close();
        } catch (PDFException e) {
            e.printStackTrace();
        } 	
		return y;
	}
	
	public ArrayList<String> getPDFMetadata(){
		ArrayList<String> al=new ArrayList<String>();
		al.clear();
		int y=-1;
		StandardFontTF.mAssetMgr = c.getAssets();
        try {
            PDFDocument pdf = new PDFDocument(path1, null);
            DocumentInfo di =pdf.getDocumentInfo();
            al.add("\nAuthor  "+di.getAuthor());
            al.add("\nCreator  "+di.getCreator());
            al.add("\nProducer  "+di.getProducer());
            al.add("\nSubject  "+di.getSubject());
            al.add("\nTitle  "+di.getTitle());
            al.add("\n"+"Number of pages:  "+String.valueOf(di.getPageCount()));
            
            Date dt=di.getCreationDate();
            SimpleDateFormat spf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
  //          spf = new SimpleDateFormat("dd MMM yyyy");
            al.add("\nCreated:  "+  spf.format(dt)  );

        } catch (PDFException e) {
            e.printStackTrace();
        } 	
		return al;
	}
	
}
