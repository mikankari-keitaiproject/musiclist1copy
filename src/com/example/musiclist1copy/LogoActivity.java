package com.example.musiclist1copy;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

public class LogoActivity extends Activity {

	View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		view = new LogoView(this);
		FrameLayout layout = (FrameLayout)findViewById(R.id.container);
		layout.addView(view);
		
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO 自動生成されたメソッド・スタブ
				Intent intent = new Intent(LogoActivity.this, PlayerActivity.class);
				startActivity(intent);
				finish();
			}
		}, 3000);
	}
	
}
