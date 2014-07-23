package com.example.musiclist1copy;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;


public class SituationView extends View {
	
	Bitmap[] bitmaps;
	Point[] points_lefttop;
	String[] texts;
	Point[] point_center;
	Color[] on_colors;
	Paint paint;

	public SituationView(Context context) {
		super(context);
		// TODO 自動生成されたコンストラクター・スタブ
		
    	WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        int screenWidth = disp.getWidth();
        int screenHeight = disp.getHeight();        
    	Resources resources = this.getContext().getResources();
		bitmaps = new Bitmap[3];
		points_lefttop = new Point[3];
		bitmaps[0] = BitmapFactory.decodeResource(resources, R.drawable.tosetting);
		points_lefttop[0] = new Point(5, screenHeight - bitmaps[0].getHeight() - 45, 0);
		bitmaps[1] = BitmapFactory.decodeResource(resources, R.drawable.playbutton);
		points_lefttop[1] = new Point(points_lefttop[0].x + bitmaps[0].getWidth(), screenHeight - bitmaps[1].getHeight() - 45, 0);
		bitmaps[2] = BitmapFactory.decodeResource(resources, R.drawable.loopbutton);
		points_lefttop[2] = new Point(points_lefttop[1].x + bitmaps[1].getWidth(), screenHeight - bitmaps[2].getHeight() - 45, 0);

		texts = new String[5];
		point_center = new Point[5];
		on_colors = new Color[5];
		texts[0] = "BPM";
		point_center[0] = new Point(380, 80 - 40, 80);
		on_colors[0] = new Color(255, 255, 150, 0);
		texts[1] = "Weather";
		point_center[1] = new Point(170, 220 - 40, 50);
		on_colors[1] = new Color(255, 108, 0, 114);
		texts[2] = "Time";
		point_center[2] = new Point(410, 360 - 40, 80);
		on_colors[2] = new Color(255, 178, 0, 1);
		texts[3] = "Place";
		point_center[3] = new Point(130, 430 - 40, 130);
		on_colors[3] = new Color(255, 0, 10, 178);
		texts[4] = "Season";
		point_center[4] = new Point(340, 600 - 40, 100);
		on_colors[4] = new Color(255, 70, 160, 0);
		
		paint = new Paint();
	}
	
	public void onDraw(Canvas canvas){
		paint.reset();
		paint.setAntiAlias(true);
		
		// 設定円
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(48);
		for(int i = 0; i < texts.length; i++){
			paint.setARGB(on_colors[i].a, on_colors[i].r, on_colors[i].g, on_colors[i].b);
			canvas.drawCircle(point_center[i].x, point_center[i].y, point_center[i].radius, paint);
			if(on_colors[i].r + on_colors[i].g + on_colors[i].b < 380){
				paint.setARGB(255, 255, 255, 255);				
			}else{
				paint.setARGB(255, 0, 0, 0);
			}
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			canvas.drawText(texts[i], point_center[i].x, point_center[i].y + 10, paint);
		}
		
		// 画像
		for(int i = 0; i < bitmaps.length; i++){
			canvas.drawBitmap(bitmaps[i], points_lefttop[i].x, points_lefttop[i].y, paint);
		}
	}
	
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
        	for(int i = 0; i < bitmaps.length; i++){
            	if(event.getX() > points_lefttop[i].x && event.getX() < points_lefttop[i].x + bitmaps[i].getWidth() && event.getY() > points_lefttop[i].y && event.getY() < points_lefttop[i].y + bitmaps[i].getHeight()){
            		switch(i){
            		case 0:
            			((SituationActivity)getContext()).close();
            			break;
            		}
            		break;
            	}
        	}
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
        	for(int i = 0; i < texts.length; i++){
            	if(false){
            		switch(i){
            		}
            		break;
            	}
        	}        	
        }
		return true;
    }
	
	private static class Point{
		int x;
		int y;
		int radius;
		Point(int x, int y, int r){
			this.x = x;
			this.y = y;
			this.radius = r;
		}
	}
	
	private static class Color{
		int a;
		int r;
		int g;
		int b;
		Color(int a, int r, int g, int b){
			this.a = a;
			this.r = r;
			this.g = g;
			this.b = b;
		}
	}

}
