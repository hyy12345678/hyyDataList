package com.example.hyydatalist.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.hyydatalist.R;
import com.example.hyydatalist.application.HyyDLApplication;
import com.example.hyydatalist.database.DatabaseManager;
import com.example.hyydatalist.model.HyyAlarm;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

public class AlarmConfigActivity extends Activity {

	TimePicker tp;

	String hourOfDay;

	String minute;

	Button btnSave;

	Button btnPause;

	Button btnResume;

	String messageId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarmconfig);

		init();
		initListener();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		messageId = getIntent().getStringExtra("messageId");
		hourOfDay = String.valueOf(tp.getCurrentHour());
		minute = String.valueOf(tp.getCurrentMinute());

		List<HyyAlarm> saveAlarmLst = DatabaseManager.getInstance(
				HyyDLApplication.getContext()).queryAlarmById(messageId);

		if (!saveAlarmLst.isEmpty()) {
			HyyAlarm savedAlarm = saveAlarmLst.get(0);
			String[] times = savedAlarm.getAlarmTime().split(":");

			tp.setCurrentHour(Integer.valueOf(times[0]));
			tp.setCurrentMinute(Integer.valueOf(times[1]));
		}

	}

	private void initListener() {
		// TODO Auto-generated method stub
		tp.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
				Log.i("hyy", "hourOfDay:" + hourOfDay);
				Log.i("hyy", "minute:" + minute);
				refreshTime(hourOfDay, minute);
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				List<HyyAlarm> alarms = new ArrayList<HyyAlarm>();
				HyyAlarm alarm = new HyyAlarm();
				String saveTime = getSelectedTime();
				Toast.makeText(HyyDLApplication.getContext(),
						"saveTime:" + saveTime, Toast.LENGTH_SHORT).show();

				alarm.setAlarmTime(saveTime);
				alarm.setMessageId(messageId);

				alarms.add(alarm);
				DatabaseManager.getInstance(HyyDLApplication.getContext())
						.saveOrUpdateAlarm(alarms);

				DatabaseManager.getInstance(HyyDLApplication.getContext())
						.resumeAlarm(messageId);
				
				finish();
			}
		});

		btnPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatabaseManager.getInstance(HyyDLApplication.getContext())
						.pauseAlarm(messageId);
			}
		});

		btnResume.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatabaseManager.getInstance(HyyDLApplication.getContext())
						.resumeAlarm(messageId);
			}
		});
	}

	private String getSelectedTime() {
		return hourOfDay + ":" + minute;
	}

	private void refreshTime(int hour, int minute) {
		this.hourOfDay = String.valueOf(hour);
		this.minute = String.valueOf(minute);
	}

	private void init() {
		// TODO Auto-generated method stub
		tp = (TimePicker) findViewById(R.id.tpPicker);
		tp.setIs24HourView(true);
		btnSave = (Button) findViewById(R.id.btnSaveAlarm);
		btnPause = (Button) findViewById(R.id.btnPauseAlarm);
		btnResume = (Button) findViewById(R.id.btnResumeAlarm);

	}

}
