package com.example.musiclist1copy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;


public class SituationActivity extends Activity {
	
	View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		view = new SituationView(this);
		FrameLayout layout = (FrameLayout)findViewById(R.id.container);
		layout.addView(view);
	}
	
	public void close(){
		finish();
	}

}
