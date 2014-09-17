package com.example.musiclist1copy;

//import com.example.appname5.R;

import com.example.musiclist1copy.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class PlayerView extends View {
	
	Paint paint;
	Bitmap[] bitmaps;
	Point[] points_lefttop;
	Point[] points_rightbottom;
	String title;
	String artist;
	int BPM;
	double seek;
	byte[] waveform;
	
	public PlayerView(Context context){
		super(context);
		
		paint = new Paint();
    	Resources resources = this.getContext().getResources();
		bitmaps = new Bitmap[8];
		points_lefttop = new Point[8];
		// TODO: display.getWidth()よりもthis.getWidth()のほうがいいかも、クライアント領域で計算してくれる
    	WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();        
		bitmaps[0] = BitmapFactory.decodeResource(resources, R.drawable.backgroundnote);
		points_lefttop[0] = new Point((int)(screenWidth * 0.5) - (int)(bitmaps[0].getWidth() * 0.5), 400 - 55);
		bitmaps[1] = BitmapFactory.decodeResource(resources, R.drawable.title);
		points_lefttop[1] = new Point(130, 490 - 55);
		bitmaps[2] = BitmapFactory.decodeResource(resources, R.drawable.artist);
		points_lefttop[2] = new Point(130, 520 - 55);
		bitmaps[3] = BitmapFactory.decodeResource(resources, R.drawable.prevbutton);
		points_lefttop[3] = new Point(15, 490 - 55);
		bitmaps[4] = BitmapFactory.decodeResource(resources, R.drawable.nextbutton);
		points_lefttop[4] = new Point(screenWidth - bitmaps[4].getWidth() - 15, 490 - 55);
		bitmaps[5] = BitmapFactory.decodeResource(resources, R.drawable.tosetting);
		points_lefttop[5] = new Point(5, screenHeight - bitmaps[5].getHeight() - 45);
		bitmaps[6] = BitmapFactory.decodeResource(resources, R.drawable.playbutton);
		points_lefttop[6] = new Point(points_lefttop[5].x + bitmaps[5].getWidth(), screenHeight - bitmaps[6].getHeight() - 45);
		bitmaps[7] = BitmapFactory.decodeResource(resources, R.drawable.loopbutton);
		points_lefttop[7] = new Point(points_lefttop[6].x + bitmaps[6].getWidth(), screenHeight - bitmaps[7].getHeight() - 45);
//		bitmaps[8] = BitmapFactory.decodeResource(resources, R.drawable.wave);
//		points_lefttop[8] = new Point(0, 45 - 55);
		title = "";
		artist = "";
		BPM = 0;
		waveform = null;
 	}
	
	public void onDraw(Canvas canvas){
//		canvas.scale(0.5f, 0.5f);
		
		paint.reset();
		paint.setAntiAlias(true);
		
		// 波形表示
		paint.setARGB(255, 36, 170, 0);
		int zero_y = 88;
		int wave_width = 480;
		int wave_height = 176;
		if(waveform != null){
	        for (int i = 0; i < waveform.length - 1; i++) {
	            int x1 = wave_width * i / (waveform.length - 1);
	            int y1 = zero_y + ((byte)(waveform[i] + 128)) * (wave_height / 64);
	            int x2 = wave_width * (i + 1) / (waveform.length - 1);
	            int y2 = zero_y + ((byte)(waveform[i + 1] + 128)) * (wave_height / 64);
		        canvas.drawLine(x1, y1, x2, y2, paint);
	        }
		}else{
			canvas.drawLine(0, zero_y, getWidth(), zero_y, paint);
		}
		
		
		// ビットマップ
		for(int i = 0; i < bitmaps.length; i++){
			canvas.drawBitmap(bitmaps[i], points_lefttop[i].x, points_lefttop[i].y, paint);
		}
		
		// シークバー
		paint.setStyle(Paint.Style.STROKE);		
		paint.setARGB(230, 0, 52, 83);
		paint.setStrokeWidth(5);
		canvas.drawCircle((int)(canvas.getWidth() * 0.5), points_lefttop[0].y + (int)(bitmaps[0].getHeight() * 0.5) + 15, 150, paint);
		paint.setStyle(Paint.Style.FILL);
		canvas.save();
		canvas.translate((int)(canvas.getWidth() * 0.5), points_lefttop[0].y + (int)(bitmaps[0].getHeight() * 0.5) + 15);
		canvas.rotate((int)(360 * seek - 90));// -90 ~ 270
		paint.setARGB(230, 2, 150, 86);
		canvas.drawCircle(150, 0, 20, paint);
		canvas.restore();

		// タイトル表示の背景
		paint.setStyle(Paint.Style.FILL);
		paint.setARGB(100, 200, 200, 200);
		canvas.drawArc(new RectF(100, points_lefttop[0].y + bitmaps[0].getHeight() - 30, canvas.getWidth() - 100, points_lefttop[0].y + bitmaps[0].getHeight() + 30), 0, 360, true, paint);
		paint.setARGB(100, 0, 52, 83);
		canvas.drawRect(new RectF(points_lefttop[1].x - 10, points_lefttop[1].y - 25, canvas.getWidth() - points_lefttop[1].x + 10, points_lefttop[1].y + 85), paint);
		
		// テキスト
		paint.setStyle(Paint.Style.FILL);
		paint.setARGB(255, 255, 255, 255);
		paint.setTextSize(40);
		paint.setTextAlign(Align.CENTER);
		canvas.drawText("BPM:" + (BPM >= 0 ? BPM : "---"), (int)(canvas.getWidth() * 0.5), points_lefttop[0].y - 110, paint);
		paint.setTextSize(28);
		paint.setTextAlign(Align.LEFT);
		canvas.drawText(title, points_lefttop[1].x + bitmaps[1].getWidth() + 10, points_lefttop[1].y + bitmaps[1].getHeight() - 5, paint);
		paint.setTextSize(20);
		canvas.drawText(artist, points_lefttop[2].x + bitmaps[2].getWidth() + 10, points_lefttop[2].y + bitmaps[2].getHeight() - 5, paint);
		
		
		if(true){
			invalidate();
		}
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
        }
	}

    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP){
        	for(int i = 0; i < bitmaps.length; i++){
            	if(event.getX() > points_lefttop[i].x && event.getX() < points_lefttop[i].x + bitmaps[i].getWidth() && event.getY() > points_lefttop[i].y && event.getY() < points_lefttop[i].y + bitmaps[i].getHeight()){
            		switch(i){
            		case 3:
            			((PlayerActivity)getContext()).prevbuttonClicked();
            			break;
            		case 4:
            			((PlayerActivity)getContext()).nextbuttonClicked();
            			break;
            		case 5:
            			((PlayerActivity)getContext()).situationbuttonClicked();
            			break;
            		case 6:
                    	((PlayerActivity)getContext()).playbuttonClicked();            			
            			break;
            		}
            		break;
            	}
        	}
        }

        return true;
    }
    
    public void setData(PlayerActivity.Data data){
    	this.title = data.title;
    	this.artist = data.artist;
    	this.BPM = data.BPM;
    }
    
    public void setSeek(double seek){
    	this.seek = seek;
    }
    
    public void setWaveform(byte[] waveform){
    	this.waveform = waveform;
    }
}
