package com.example.directoryshow1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint();
    Paint paint1 = new Paint();
//    Bitmap bitmap;
    static int height=1,width=5;
    	static float VOLpercent=0;

    public DrawView(Context context) {
        super(context);
        init();
    }


    
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    private void init() {
        paint.setColor(Color.BLUE);
        paint1.setColor(Color.GREEN);
//        Resources res = getResources();
//        bitmap = BitmapFactory.decodeResource(res, R.drawable.yellowvolume);
//        width=bitmap.getWidth();
//        height=bitmap.getHeight();
    }
    
    @Override
    public void onDraw(Canvas canvas) {
//    	canvas.drawBitmap(bitmap, 0, 0, paint);
    	canvas.drawRect(0, 0, width, height, paint1);
       	canvas.drawRect(0, 
       			(height*((0.95f-VOLpercent)*95))/100,
       			width, (height)/1, paint);
 //           canvas.drawLine(0, 0, 120, 120, paint);
 //           canvas.drawLine(120, 0, 0, 120, paint);
    }
}
