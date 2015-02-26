package com.example.hyydatalist.receiver;

import com.example.hyydatalist.activity.MainActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

	static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(ACTION_BOOT)) {
			Intent ootIntent = new Intent(context, MainActivity.class);
			ootIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(ootIntent);
		}
	}

}
