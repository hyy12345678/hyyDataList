package com.example.hyydatalist.application;

import com.example.hyydatalist.database.DatabaseManager;
import com.example.hyydatalist.service.TimeWatchService;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class HyyDLApplication extends Application {

	private static Context context = null;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		context = getApplicationContext();

		DatabaseManager.initDatabaseManager(context);

		Intent intent = new Intent(this, TimeWatchService.class);
		this.startService(intent);

	}

	public static Context getContext() {
		return context;
	}

}
