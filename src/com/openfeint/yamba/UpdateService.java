package com.openfeint.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdateService extends Service {
	private final static String TAG = "UpdateService";
	
	static final int DELAY = 6 * 1000; // 1min
	private boolean runFlag = false;
	private Updater updater;
	private YambaApplication yamba;

	@Override
	public void onCreate() {
		super.onCreate();
		this.updater = new Updater();
		this.yamba = (YambaApplication) getApplication();
		Log.d(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.runFlag = false;
		this.updater.interrupt();
		this.updater = null;
		this.yamba.setServiceRunning(false);
		Log.d(TAG, "onDestroy");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		this.runFlag = true;
		this.updater.start();
		this.yamba.setServiceRunning(true);
		Log.d(TAG, "onStartCommand");
		
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	private class Updater extends Thread {
		List<Twitter.Status> timeline;
		
		public Updater() {
			super("UpdaterService-Updater");
		}
		
		@Override
		public void run() {
			UpdateService updateService = UpdateService.this;
			
			while (updateService.runFlag) {
				Log.d(TAG, "Updater running...");
				try {
					try {
						timeline = yamba.getTwitter().getFriendsTimeline();	
					} catch (TwitterException e) {
						Log.e(TAG, "Failed to connect to twitter service", e);						
					}
					
					for (Twitter.Status status : timeline) {
						Log.d(TAG, String.format("%s: %s", status.user.name, status.text));
					}
					
					Log.d(TAG, "Updater ran");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					updateService.runFlag = false;
				}
			}
		}
	}

}
