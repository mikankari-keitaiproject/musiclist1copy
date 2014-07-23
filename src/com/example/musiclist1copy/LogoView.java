package com.example.musiclist1copy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class LogoView extends View {
	
	String logo_text;
	Point logo_point;
	String loading_text;
	Point loading_point;
	Paint paint;
	
	public LogoView(Context context){
		super(context);
		
    	WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        int screenWidth = disp.getWidth();
        int screenHeight = disp.getHeight();        

        logo_text = "Rhyth-Walk";
		logo_point = new Point((int)(screenWidth * 0.5), 300);
		loading_text = "Loading...";
		loading_point = new Point(screenWidth - 10, screenHeight - 50);
		paint = new Paint();
	}
	
	public void onDraw(Canvas canvas){
		paint.reset();
		paint.setAntiAlias(true);
		
		paint.setARGB(255, 255, 255, 255);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(70);
		canvas.drawText(logo_text, logo_point.x, logo_point.y, paint);
		paint.setTextAlign(Align.RIGHT);
		paint.setTextSize(30);
		canvas.drawText(loading_text, loading_point.x, loading_point.y, paint);
	}

}
