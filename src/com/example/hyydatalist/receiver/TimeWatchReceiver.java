package com.example.hyydatalist.receiver;

import java.util.Calendar;
import java.util.List;

import com.example.hyydatalist.R;
import com.example.hyydatalist.activity.MainActivity;
import com.example.hyydatalist.application.HyyDLApplication;
import com.example.hyydatalist.constants.HyyConstants;
import com.example.hyydatalist.database.DatabaseManager;
import com.example.hyydatalist.model.HyyAlarm;
import com.example.hyydatalist.model.HyyMessage;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.Toast;

public class TimeWatchReceiver extends BroadcastReceiver {

	String title;
	String shortcut;
	String content;

	@Override
	public void onReceive(Context context, Intent intent) {

		// TODO Auto-generated method stub
		List<HyyAlarm> alarms = DatabaseManager.getInstance(
				HyyDLApplication.getContext()).queryAlarms();

		for (HyyAlarm alarm : alarms) {
			if (null != alarm.getAlarmTime()) {
				if (decideHitted(alarm)) {
					Toast.makeText(HyyDLApplication.getContext(),
							"It is the time! " + alarm.getAlarmTime(),
							Toast.LENGTH_SHORT).show();
					startNotification(alarm.getMessageId());
				}

			}

		}
	}

	private boolean decideHitted(HyyAlarm alarm) {
		// TODO Auto-generated method stub
		Calendar curCal = Calendar.getInstance();
		String strHour = String.valueOf(curCal.get(Calendar.HOUR_OF_DAY));
		String strMinute = String.valueOf(curCal.get(Calendar.MINUTE));
		int intDay = curCal.get(Calendar.DAY_OF_WEEK); // sun=1,mon=2,...,sat=7

		String[] alarmHourTime = alarm.getAlarmTime().split(":");
		String[] alarmDays = alarm.getDayOfWeek().split(":");

		boolean isTimeHitted = strHour.equals(alarmHourTime[0])
				&& strMinute.equals(alarmHourTime[1]);

		boolean isDayHitted = "1".equals(alarmDays[intDay - 1]);

		return isTimeHitted && isDayHitted;
	}

	private void startNotification(String messageId) {

		findItemById(messageId);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				HyyDLApplication.getContext()).setSmallIcon(R.drawable.panda)
				.setContentTitle(title).setContentText(shortcut);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(HyyDLApplication.getContext(),
				MainActivity.class);

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder
				.create(HyyDLApplication.getContext());
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) HyyDLApplication
				.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.

		Notification noti = mBuilder.build();
		noti.flags = Notification.FLAG_AUTO_CANCEL;
		noti.flags |= Notification.FLAG_INSISTENT;
		noti.defaults = Notification.DEFAULT_SOUND;
		noti.defaults |= Notification.DEFAULT_VIBRATE;
		mNotificationManager.notify(HyyConstants.mId, noti);
	}

	private void findItemById(String id) {
		// TODO Auto-generated method stub
		List<HyyMessage> selectedMessage = DatabaseManager.getInstance(
				HyyDLApplication.getContext()).queryMessageById(id);
		title = selectedMessage.get(0).getTitle();
		shortcut = selectedMessage.get(0).getShortcut();
		content = selectedMessage.get(0).getContent();
	}

}
