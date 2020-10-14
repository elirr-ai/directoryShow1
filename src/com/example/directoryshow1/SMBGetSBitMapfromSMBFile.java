package com.example.directoryshow1;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

public class SMBGetSBitMapfromSMBFile {

	
	public static Bitmap getBitmapFromSMBFile(String url, String username, String password,
			Bitmap badBitmap, int thumbnailx, int thumbnaily){
		Bitmap bitmap=null;
        NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",username,password);

	 	int inWidth = 0; int inHeight = 0;				    
	    int dstWidth=thumbnailx; int dstHeight=thumbnaily;
	    try {
	    InputStream in = new SmbFileInputStream(new SmbFile(url, auth2));
	    // decode image size (decode metadata only, not the whole image)
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeStream(in, null, options);
	    in.close();
	    in = null;

	    // save width and height
	    inWidth = options.outWidth;
	    inHeight = options.outHeight;
	    // decode full image pre-resized
	    in = new SmbFileInputStream(new SmbFile(url, auth2));
	    options = new BitmapFactory.Options();
	    // calc rought re-size (this is no exact resize)
	    options.inSampleSize = Math.max(inWidth/dstWidth, inHeight/dstHeight);
	    // decode full image
	    Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

	    // calc exact destination size
	    Matrix m = new Matrix();
	    RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
	    RectF outRect = new RectF(0, 0, dstWidth, dstHeight);
	    m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
	    float[] values = new float[9];
	    m.getValues(values);

	    // resize bitmap
	    bitmap = Bitmap.createScaledBitmap(roughBitmap,
	    		(int) (roughBitmap.getWidth() * values[0]),
	    		(int) (roughBitmap.getHeight() * values[4]), true);
} catch (Exception e) {
	 bitmap = badBitmap;
	e.printStackTrace();
}				 
		return bitmap;
	}
	//////////////////////////////////////////////////////////////////
	public static Bitmap getBitmapSMB(String url, String username, String password,
			Bitmap badBitmap, int thumbnailx, int thumbnaily) {
		Bitmap bitmap=null;
        NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",username,password);
		InputStream in = null;
		try {
		    final int IMAGE_MAX_SIZE = thumbnailx* thumbnaily; // 1.2MP
		    in = new SmbFileInputStream(new SmbFile(url, auth2));
		    BitmapFactory.Options options = new BitmapFactory.Options();
		    options.inJustDecodeBounds = true;
		    BitmapFactory.decodeStream(in, null, options);
		    in.close();

		    int scale = 1;
		    while ((options.outWidth * options.outHeight) * (1 / Math.pow(scale, 2)) > 
		          IMAGE_MAX_SIZE) {
		       scale++;
		    }
		    Bitmap resultBitmap = null;
		    in = new SmbFileInputStream(new SmbFile(url, auth2));
		    if (scale > 1) {
		        scale--;
		        // scale to max possible inSampleSize that still yields an image
		        // larger than target
		        options = new BitmapFactory.Options();
		        options.inSampleSize = scale;
		        resultBitmap = BitmapFactory.decodeStream(in, null, options);

		        // resize to desired dimensions
		        int height = resultBitmap.getHeight();
		        int width = resultBitmap.getWidth();
		        double y = Math.sqrt(IMAGE_MAX_SIZE
		                / (((double) width) / height));
		        double x = (y / height) * width;

		        Bitmap scaledBitmap = Bitmap.createScaledBitmap(resultBitmap, (int) x, 
		           (int) y, true);
		        resultBitmap.recycle();
		        resultBitmap = scaledBitmap;

//		        System.gc();
		    } else {
		        resultBitmap = BitmapFactory.decodeStream(in);
		    }
		    in.close();

		    return resultBitmap;
		} catch (IOException e) {
		    return badBitmap;
		}
	}
	/////////////////////////////////////////////////////////////////////
	public static Bitmap getScaledBitmap(String url, String username, String password,
			Bitmap badBitmap, int thumbnailx, int thumbnaily) {

        NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",username,password);
	    InputStream in = null;
		try {
			in = new SmbFileInputStream(new SmbFile(url, auth2));
		} catch (SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		File image = new File(path);

	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    options.inInputShareable = true;
	    options.inPurgeable = true;
	    BitmapFactory.decodeStream(in, null, options);
	    try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    in = null;	    	    
	    if ((options.outWidth == -1) || (options.outHeight == -1))
	        return badBitmap;

//	    int originalSize = (options.outHeight > options.outWidth) ? options.outHeight
//	            : options.outWidth;

	    BitmapFactory.Options opts = new BitmapFactory.Options();
//	    opts.inSampleSize = originalSize / thumbnailx;
//	    opts.inSampleSize = 4;
	    opts.inSampleSize = Math.max(options.outWidth/thumbnailx, options.outHeight/thumbnaily);
	    try {
			in = new SmbFileInputStream(new SmbFile(url, auth2));
		} catch (SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    options = new BitmapFactory.Options();
//	    // calc rought re-size (this is no exact resize)
//	    options.inSampleSize = Math.max(inWidth/dstWidth, inHeight/dstHeight);
	    // decode full image
	    Bitmap scaledBitmap = BitmapFactory.decodeStream(in, null, opts);

	    
	    
	    
	    
//	    Bitmap scaledBitmap = BitmapFactory.decodeFile(image.getPath(), opts);

	    return scaledBitmap;     
	}
	
	
	////////////////////////////////////////////////
	
}
