package com.example.musiclist1copy;

//import com.example.appname5.GameView;
//import com.example.appname5.R;

import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class PlayerActivity extends Activity {

    private PlayerView view;
	private MediaPlayer player;
    private ArrayAdapter musiclist;
    private int musiclist_index;
    private Visualizer visualizer;
    private boolean isVisualizerSupported;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		
		view = new PlayerView(this);
		Data mirai = new Data();
//		mirai.title = "�i����100��";
		mirai.title = "-";
//		mirai.artist = "Mirai K-tai Project";
		mirai.artist = "-";
		mirai.BPM = 0;
		mirai.data = null;
		view.setData(mirai);
		FrameLayout layout = (FrameLayout)findViewById(R.id.container);
		layout.addView(view);
		
		player = null;
		musiclist = executeFindMusicFiles();
		musiclist.sort(new Comparator<Data>() {

			@Override
			public int compare(Data lhs, Data rhs) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				return (int)(Math.random() * 2) - 1;
			}
		});
		musiclist_index = 0;
		isVisualizerSupported = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;

		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				if(player != null){
					view.setSeek((double)player.getCurrentPosition() / (double)player.getDuration());
				}
			}
		}, 0, 1000);
		// �V�[�N�o�[����
		new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				
				if(player != null){
					player.seekTo((int)(player.getDuration() * (seekBar.getProgress() / 100.0)));
				}
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				
			}
		};
		
	}
	
	protected void onStop(){
		super.onStop();
		
		// �o�b�O�O���E���h�Đ�
		if(player != null){
			if(player.isPlaying()){
				player.start();
			}else{
//				player.stop();
			}
		}
	}
	
	protected void onDestroy(){
		super.onDestroy();
		
		stopItem();

	}

	/**
	 * �����ɉ��y�t�@�C����T�������������Ă����܂�
	 * ���\�b�h���͂Ȃ�ł��ǂ��ł�
	 */
	private ArrayAdapter executeFindMusicFiles(){
		
		ArrayAdapter<Data> adapter = new ArrayAdapter<Data>(PlayerActivity.this, android.R.layout.simple_list_item_1);
	
		ContentResolver cr = getContentResolver();
		// URI�A�ˉe�A�I���A�����A
		Cursor cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		
		// 1�s�ڂ����邩
		if(cursor.moveToFirst()){
			
			// �^�C�g���̃J���������Ԗڂ�
			int columnIndex_title = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
			int columnIndex_artist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
			int columnIndex_data = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
			
			do{
				
				// �^�C�g�����擾
				String title = cursor.getString(columnIndex_title);
				String artist = cursor.getString(columnIndex_artist);
				String data = cursor.getString(columnIndex_data);

				Data item = new Data();
				item.title = title;
				item.artist = artist;
				item.data = data;
				item.BPM = -1;

				adapter.add(item);
				
			}while(cursor.moveToNext());
			
		}
		
		return adapter;
		
	}
	
	public Data getData(){
		Data data = (Data)musiclist.getItem(musiclist_index);
		return data;
	}
	
	public void playbuttonClicked(){
		if(player != null){
			if(player.isPlaying()){
				stopItem();
			}else{
				startItem();
			}			
		}else{
			startItemAt(0);
		}
	}
	
	public void prevbuttonClicked(){
		int position = musiclist_index - 1;
		if(position < 0){
			position = 0;
		}
		
		startItemAt(position);		
	}
	
	public void nextbuttonClicked(){
		int position = musiclist_index + 1;
		if(position >= musiclist.getCount()){
			position = musiclist.getCount() - 1;
		}
		
		startItemAt(position);		
	}
	
	public void situationbuttonClicked(){
		Intent intent = new Intent(PlayerActivity.this, SituationActivity.class);
		startActivity(intent);
	}
	
	public void startItemAt(int position){
		if(player != null){
			player.stop();
		}
		Data data = (Data)musiclist.getItem(position);
		// TODO �Đ��J�n���ɖ��񏉊�������̂͌��������̂��ȁHplayer.reset();player.setDataSource();������͂ǂ���
		player = MediaPlayer.create(this, Uri.parse(data.data));
		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO �����������ꂽ���\�b�h�E�X�^�u
				nextbuttonClicked();
			}
		});
		musiclist_index = position;
		view.setData(data);
		player.start();
		if(isVisualizerSupported){
	    	visualizer = new Visualizer(player.getAudioSessionId());
	    	visualizer.setEnabled(false);
	    	visualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
	    	visualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
				
				@Override
				public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform,
						int samplingRate) {
					// TODO �����������ꂽ���\�b�h�E�X�^�u
		   	        for (int h = 0; h < 1; h++) {
						int i = waveform.length;
		   	        	for (int j = 1; j < i; j++) {
//							waveform[j] = (byte)((waveform[j - 1] + waveform[j]) * 0.5);
//							waveform[j - 1] = waveform[j];
//		   	        		waveform[j] = (byte)(waveform[j - 1] - waveform[j]);
//							waveform[j] *= 0.5;
						}
					}
					updateWaveform(waveform);
				}
				
				@Override
				public void onFftDataCapture(Visualizer visualizer, byte[] fft,
						int samplingRate) {
					// TODO �����������ꂽ���\�b�h�E�X�^�u
				}
			},
			Visualizer.getMaxCaptureRate(),
			true, false);	// waveform or fft
	    	visualizer.setEnabled(true);			
		}else{
			visualizer = null;
		}
	}
	
	public void updateWaveform(byte[] waveform){
		view.setWaveform(waveform);
	}
	
	public void startItem(){
		if(player != null){
			player.start();
		}
	}
	
	public void stopItem(){
		if(player != null){
			player.pause();
		}
	}

	public static class Data{
		String title;
		String artist;
		int BPM;
		String data;
		
		public String getData(String key){
			String value;
			if(key.equals("title")){
				value = title;
			}else if(key.equals("artist")){
				value = artist;
			}else if(key.equals("bpm")){
				value = "" + BPM;
			}else{
				value = toString();
			}
			return value;
		}
		
		// ���������X�g�r���[�ɕ\�������
		public String toString(){
			return title;
		}
	}


}
