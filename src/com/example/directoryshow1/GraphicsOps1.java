package com.example.directoryshow1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.widget.ImageView;

public class GraphicsOps1 {

	public static int HEIGHT=0,WIDTH,XX=0,YY=0,invertXY=0;
	public int screenWidth,screenHeight;
	public Bitmap src;
	float xSCALE,ySCALE;
	
	
	public GraphicsOps1 (int screenWidth, int screenHeight){
		this.screenWidth=screenWidth;
		this.screenHeight=screenHeight;
	}
	
	public int getX(){
		return XX;
	} 
	

	
	public void putBitmap(Bitmap b){
		this.src=b;
		this.WIDTH=src.getWidth();
		this.HEIGHT=src.getHeight();
	}
	
	public Bitmap getBitmap(){
		return src;
	}
	
	public Bitmap ConvGrey(){
	invertXY=0;// no inv
	final double GS_RED = 0.299;
    final double GS_GREEN = 0.587;
    final double GS_BLUE = 0.114;
 
    // create output bitmap
    Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
    // pixel information
    int A, R, G, B;
    int pixel;
    // get image size
    int width = src.getWidth();
    int height = src.getHeight();
    // scan through every single pixel
    for(int x = 0; x < width; ++x) {
        for(int y = 0; y < height; ++y) {
        	this.XX=x;this.YY=y;
            // get one pixel color
            pixel = src.getPixel(x, y);
            // retrieve color of all channels
            A = Color.alpha(pixel);
            R = Color.red(pixel);
            G = Color.green(pixel);
            B = Color.blue(pixel);
            // take conversion up to one single value
            R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
            // set new pixel color to output bitmap
            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
        }
    }
	this.src=bmOut;
	this.WIDTH=src.getWidth();
	this.HEIGHT=src.getHeight();
    return bmOut;
}
	
	
	public Bitmap GammaCorrection (double red, double green, double blue){
		invertXY=0;// no inv
		// create output image
	    Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
	    // get image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	    // color information
	    int A, R, G, B;
	    int pixel;
	    // constant value curve
	    final int    MAX_SIZE = 256;
	    final double MAX_VALUE_DBL = 255.0;
	    final int    MAX_VALUE_INT = 255;
	    final double REVERSE = 1.0;
	 
	    // gamma arrays
	    int[] gammaR = new int[MAX_SIZE];
	    int[] gammaG = new int[MAX_SIZE];
	    int[] gammaB = new int[MAX_SIZE];
	 
	    // setting values for every gamma channels
	    for(int i = 0; i < MAX_SIZE; ++i) {
	        gammaR[i] = (int)Math.min(MAX_VALUE_INT,
	                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red)) + 0.5));
	        gammaG[i] = (int)Math.min(MAX_VALUE_INT,
	                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green)) + 0.5));
	        gammaB[i] = (int)Math.min(MAX_VALUE_INT,
	                (int)((MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue)) + 0.5));
	    }
	 
	    // apply gamma table
	    for(int x = 0; x < width; ++x) {
	        for(int y = 0; y < height; ++y) {
	        	this.XX=x;this.YY=y;
	            // get pixel color
	            pixel = src.getPixel(x, y);
	            A = Color.alpha(pixel);
	            // look up gamma
	            R = gammaR[Color.red(pixel)];
	            G = gammaG[Color.green(pixel)];
	            B = gammaB[Color.blue(pixel)];
	            // set new color to output bitmap
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
		this.src=bmOut;
		this.WIDTH=src.getWidth();
		this.HEIGHT=src.getHeight();
	    // return final image
	    return bmOut;
	}
	
	
	public Bitmap TintColor (int degree){
		  final double PI = 3.14159d;
		    final double FULL_CIRCLE_DEGREE = 360d;
		    final double HALF_CIRCLE_DEGREE = 180d;
		    final double RANGE = 256d;
		    invertXY=0;// no inv
		        int width = src.getWidth();
		        int height = src.getHeight();
		 
		        int[] pix = new int[width * height];
		        src.getPixels(pix, 0, width, 0, 0, width, height);
		 
		        int RY, GY, BY, RYY, GYY, BYY, R, G, B, Y;
		        double angle = (PI * (double)degree) / HALF_CIRCLE_DEGREE;
		        
		        int S = (int)(RANGE * Math.sin(angle));
		        int C = (int)(RANGE * Math.cos(angle));
		 
		        for (int y = 0; y < height; y++)
		            for (int x = 0; x < width; x++) {
		            	this.XX=x;this.YY=y;
		                int index = y * width + x;
		                int r = ( pix[index] >> 16 ) & 0xff;
		                int g = ( pix[index] >> 8 ) & 0xff;
		                int b = pix[index] & 0xff;
		                RY = ( 70 * r - 59 * g - 11 * b ) / 100;
		                GY = (-30 * r + 41 * g - 11 * b ) / 100;
		                BY = (-30 * r - 59 * g + 89 * b ) / 100;
		                Y  = ( 30 * r + 59 * g + 11 * b ) / 100;
		                RYY = ( S * BY + C * RY ) / 256;
		                BYY = ( C * BY - S * RY ) / 256;
		                GYY = (-51 * RYY - 19 * BYY ) / 100;
		                R = Y + RYY;
		                R = ( R < 0 ) ? 0 : (( R > 255 ) ? 255 : R );
		                G = Y + GYY;
		                G = ( G < 0 ) ? 0 : (( G > 255 ) ? 255 : G );
		                B = Y + BYY;
		                B = ( B < 0 ) ? 0 : (( B > 255 ) ? 255 : B );
		                pix[index] = 0xff000000 | (R << 16) | (G << 8 ) | B;
		            }
		         
		        Bitmap outBitmap = Bitmap.createBitmap(width, height, src.getConfig());    
		        outBitmap.setPixels(pix, 0, width, 0, 0, width, height);
		        
		        pix = null;
		    	this.src=outBitmap;
				this.WIDTH=src.getWidth();
				this.HEIGHT=src.getHeight();
		        return outBitmap;
		    
		}	
		
		
	public Bitmap applySnowEffect() {
		int COLOR_MAX=250;
		invertXY=0;// no inv
	    // get image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	    int[] pixels = new int[width * height];
	    // get pixel array from source
	    src.getPixels(pixels, 0, width, 0, 0, width, height);
	    // random object
	    Random random = new Random();
	     
	    int R, G, B, index = 0, thresHold = 50;
	    // iteration through pixels
	    for(int y = 0; y < height; ++y) {
	        for(int x = 0; x < width; ++x) {
	        	this.XX=x;this.YY=y;
	            // get current index in 2D-matrix
	            index = y * width + x;              
	            // get color
	            R = Color.red(pixels[index]);
	            G = Color.green(pixels[index]);
	            B = Color.blue(pixels[index]);
	            // generate threshold
	            thresHold = random.nextInt(COLOR_MAX);
	            if(R > thresHold && G > thresHold && B > thresHold) {
	                pixels[index] = Color.rgb(COLOR_MAX, COLOR_MAX, COLOR_MAX);
	            }                           
	        }
	    }
	    // output bitmap                
	    Bitmap bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	    bmOut.setPixels(pixels, 0, width, 0, 0, width, height);
		this.src=bmOut;
		this.WIDTH=src.getWidth();
		this.HEIGHT=src.getHeight();
	    return bmOut;
	}
	
	public Bitmap sharpen(double weight) {
	    double[][] SharpConfig = new double[][] {
	        { 0 , -2    , 0  },
	        { -2, weight, -2 },
	        { 0 , -2    , 0  }
	    };
	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
	    convMatrix.applyConfig(SharpConfig);
	    convMatrix.Factor = weight - 8;
		this.src=ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
		this.WIDTH=src.getWidth();
		this.HEIGHT=src.getHeight();
	    return src;
	}
	
	
	public Bitmap doInvert(Bitmap src) {
	    // create new bitmap with the same settings as source bitmap
	    Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
	    // color info
	    int A, R, G, B;
	    int pixelColor;
	    // image size
	    int height = src.getHeight();
	    int width = src.getWidth();
	    invertXY=1;//inv
	    // scan through every pixel
	    for (int y = 0; y < height; y++)
	    {
	        for (int x = 0; x < width; x++)
	        {
	        	this.XX=x;this.YY=y;     /////////////////////////////////////////////// 
	        	// get one pixel
	            pixelColor = src.getPixel(x, y);
	            // saving alpha channel
	            A = Color.alpha(pixelColor);
	            // inverting byte for each R/G/B channel
	            R = 255 - Color.red(pixelColor);
	            G = 255 - Color.green(pixelColor);
	            B = 255 - Color.blue(pixelColor);
	            // set newly-inverted pixel to output image
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
	 
	    // return final bitmap
		this.src=bmOut;
		this.WIDTH=src.getWidth();
		this.HEIGHT=src.getHeight();
	    return bmOut;
	}

    public Bitmap doColorFilter(double red, double green, double blue) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        invertXY=0;// no inv
        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
            	this.XX=x;this.YY=y;
                // get pixel color
                pixel = src.getPixel(x, y);
                // apply filtering on each channel R, G, B
                A = Color.alpha(pixel);
                R = (int)(Color.red(pixel) * red);
                G = (int)(Color.green(pixel) * green);
                B = (int)(Color.blue(pixel) * blue);
                // set new color pixel to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
 
        // return final image
		this.src=bmOut;
		this.WIDTH=src.getWidth();
		this.HEIGHT=src.getHeight();
        return bmOut;
    }
	
	public Bitmap flipHorizontal (){
		Matrix matrix = new Matrix();
		matrix.preScale(-1.0f, 1.0f);
this.src=Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
this.WIDTH=src.getWidth();
this.HEIGHT=src.getHeight();
return src;
		
		
	}
    
	public Bitmap flipVertical (){
		Matrix matrix = new Matrix();
		matrix.preScale(1.0f, -1.0f);
this.src=Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
		this.WIDTH=src.getWidth();
		this.HEIGHT=src.getHeight();	
return src;
		
		
	}  
    
	public Bitmap applyMeanRemoval() {
	    double[][] MeanRemovalConfig = new double[][] {
	        { -1 , -1, -1 },
	        { -1 ,  9, -1 },
	        { -1 , -1, -1 }
	    };
	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
	    convMatrix.applyConfig(MeanRemovalConfig);
	    convMatrix.Factor = 1;
	    convMatrix.Offset = 0;
this.src=ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
		this.WIDTH=src.getWidth();
		this.HEIGHT=src.getHeight();		    
	    return src;
	} 
    
	public Bitmap emboss() {
	    double[][] EmbossConfig = new double[][] {
	        { -1 ,  0, -1 },
	        {  0 ,  4,  0 },
	        { -1 ,  0, -1 }
	    };
	    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
	    convMatrix.applyConfig(EmbossConfig);
	    convMatrix.Factor = 1;
	    convMatrix.Offset = 127;
	    this.src=ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
		this.WIDTH=src.getWidth();
		this.HEIGHT=src.getHeight();		
	    return src;
	}
	
	public Bitmap doBrightness(int value) {
	    // image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
	    // color information
	    int A, R, G, B;
	    int pixel;
	    invertXY=0;// no inv
	    // scan through all pixels
	    for(int x = 0; x < width; ++x) {
	        for(int y = 0; y < height; ++y) {
	            // get pixel color
	        	this.XX=x;this.YY=y;
	            pixel = src.getPixel(x, y);
	            A = Color.alpha(pixel);
	            R = Color.red(pixel);
	            G = Color.green(pixel);
	            B = Color.blue(pixel);
	 
	            // increase/decrease each channel
	            R += value;
	            if(R > 255) { R = 255; }
	            else if(R < 0) { R = 0; }
	 
	            G += value;
	            if(G > 255) { G = 255; }
	            else if(G < 0) { G = 0; }
	 
	            B += value;
	            if(B > 255) { B = 255; }
	            else if(B < 0) { B = 0; }
	 
	            // apply new pixel color to output bitmap
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
	 
	    // return final image
	    this.src=bmOut;
		this.WIDTH=src.getWidth();
		this.HEIGHT=src.getHeight();	
	    return src;
	}
	
	public Bitmap createContrast(double value) {
	    // image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
	    // color information
	    int A, R, G, B;
	    int pixel;
	    // get contrast value
	    double contrast = Math.pow((100 + value) / 100, 2);
	 
	    // scan through all pixels
	    for(int x = 0; x < width; ++x) {
	        for(int y = 0; y < height; ++y) {
	        	this.XX=x;this.YY=y;
	            // get pixel color
	            pixel = src.getPixel(x, y);
	            A = Color.alpha(pixel);
	            // apply filter contrast for every channel R, G, B
	            R = Color.red(pixel);
	            R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
	            if(R < 0) { R = 0; }
	            else if(R > 255) { R = 255; }
	 
	            G = Color.green(pixel);
	            G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
	            if(G < 0) { G = 0; }
	            else if(G > 255) { G = 255; }
	 
	            B = Color.blue(pixel);
	            B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
	            if(B < 0) { B = 0; }
	            else if(B > 255) { B = 255; }
	 
	            // set new pixel color to output bitmap
	            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
	        }
	    }
	    this.src=bmOut;
		this.WIDTH=src.getWidth();
		this.HEIGHT=src.getHeight();	
	    // return final image
	    return src;
	}
	
public Bitmap mark(Context c,
		String watermark, float upx,float upy, int color, float tsize,ImageView iv) {

		int w = src.getWidth();
	    int h = src.getHeight();
	    float wScale=(iv.getRight()+iv.getLeft())/src.getWidth();
	    
	    float i8=(iv.getBottom()-iv.getTop()-iv.getRight())/2;  //y ofset
	    
	    float i9=-(iv.getBottom()-3*src.getHeight())/2;  
	    float hScale=wScale;
	    
	    
//  	    float i9=-(iv.getBottom()-3*src.getHeight())/2;
//	    float hScale=(i9+iv.getBottom()+iv.getTop())/src.getHeight();

	    Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());
	 
	    Canvas canvas = new Canvas(result);
	    canvas.drawBitmap(src, 0, 0, null);
	 
	    Paint paint = new Paint();
	    paint.setColor(color);
	    paint.setAlpha(255);
	    paint.setTextSize(tsize);
	    paint.setAntiAlias(true);
	    
	    canvas.drawText(watermark,(float)((float)src.getWidth()/2),50 
//	    		upx*(float)((float)(src.getWidth())/(float)screenWidth),
//	    		upy-200
	    		//*(float)((float)(src.getHeight())/(float)iv.getHeight()) 
	    		,paint);
//	    canvas.drawText(watermark, upx/wScale, (upy-i8)/hScale, paint);
//	    canvas.drawText(watermark, upx/4, upy/4-60, paint);

	    
//  write bitmap	    
//	    File file = new File(Environment.getExternalStorageDirectory() + "/sign.png");
//try {
//	result.compress(Bitmap.CompressFormat.PNG, 100,
//			new FileOutputStream(file));
//} catch (FileNotFoundException e) {
//	// TODO Auto-generated catch block
//	e.printStackTrace();
//}
	    this.src=result;
	    return src;
	}

public Bitmap engrave() {
    ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
    convMatrix.setAll(0);
    convMatrix.Matrix[0][0] = -2;
    convMatrix.Matrix[1][1] = 2;
    convMatrix.Factor = 1;
    convMatrix.Offset = 95;
    return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
	}
	
	
	
	
}
