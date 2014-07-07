package com.example.hyydatalist.service;

import com.example.hyydatalist.receiver.TimeWatchReceiver;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class TimeWatchService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		IntentFilter intentfilter = new IntentFilter();
		intentfilter.addAction(Intent.ACTION_TIME_TICK);
		
		TimeWatchReceiver twr = new TimeWatchReceiver();
		this.registerReceiver(twr, intentfilter);
		
	}
}
