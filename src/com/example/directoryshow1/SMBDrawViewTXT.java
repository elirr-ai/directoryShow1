package com.example.directoryshow1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class SMBDrawViewTXT extends View {
    Paint paint = new Paint();
    static int height=1,width=5;

    public SMBDrawViewTXT(Context context) {
        super(context);
        init();
    }
   
    public SMBDrawViewTXT(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SMBDrawViewTXT(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    private void init() {
        paint.setColor(Color.CYAN);
paint.setStrokeWidth(10);
paint.setStyle(Paint.Style.STROKE);
    }
    
    @Override
    public void onDraw(Canvas canvas) {
//    	canvas.drawBitmap(bitmap, 0, 0, paint);
//    	canvas.drawRect(0, 0, width, height, paint);
//       	canvas.drawRect(0, 
//       			(height*((0.95f-VOLpercent)*95))/100,
//       			width, (height)/1, paint);
            canvas.drawLine(0, 0, 0, 10, paint);
//            canvas.drawLine(120, 0, 0, 120, paint);
    }
}

