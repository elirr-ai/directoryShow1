package com.example.directoryshow1;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.widget.Toast;

public class HandlePDF {

	Context c;
	public HandlePDF(Context c){
		this.c=c;
	}
	
	public void saveImagePdf(String d, String f,Bitmap b){
		int duration = Toast.LENGTH_SHORT;
		
		PdfDocument  document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = 
           new PdfDocument.PageInfo.Builder(b.getWidth(), b.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(5f);
        paint.setTextSize(35f);

        Rect rect =new Rect(0,0,b.getWidth(), b.getHeight());
        canvas.drawBitmap(b, null,rect,  null);
        canvas.drawText(f+".pdf", 30f, 30f, paint);
        document.finishPage(page);

        String pdffile=f+".pdf";
//		String path = Environment.getExternalStorageDirectory()+
//				d+"/"+pdffile; 
		String path=d+File.separator+pdffile;
				File pdfFile = new File(path);

				   if (pdfFile.exists ()) pdfFile.delete (); 

        try {
            document.writeTo(new FileOutputStream(pdfFile));
            Toast toast = Toast.makeText(c, "Done", duration);
            toast.show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast toast = Toast.makeText(c, "Something wrong: " + e.toString(), duration);
            toast.show();
                    }
	
        document.close();
    }
	
	
	
	
}
