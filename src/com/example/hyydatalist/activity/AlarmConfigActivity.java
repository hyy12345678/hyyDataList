package com.example.hyydatalist.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.hyydatalist.R;
import com.example.hyydatalist.application.HyyDLApplication;
import com.example.hyydatalist.database.DatabaseManager;
import com.hyy.hyydatalist.generator.Alarms;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

/**
 * Interface for user config Alarm
 * 
 * @author hyylj
 * 
 */
public class AlarmConfigActivity extends ActionBarActivity {

	TimePicker tp;

	String hourOfDay;

	String minute;

	Button btnSave;

	Button btnPause;

	Button btnResume;

	Long messageId;

	// Day of week textview
	TextView tvDay0;
	TextView tvDay1;
	TextView tvDay2;
	TextView tvDay3;
	TextView tvDay4;
	TextView tvDay5;
	TextView tvDay6;

	String day0 = "0";
	String day1 = "0";
	String day2 = "0";
	String day3 = "0";
	String day4 = "0";
	String day5 = "0";
	String day6 = "0";

	Alarms savedAlarm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarmconfig);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		init();
		initListener();
		
		initViews();
		
		
		

	}

	private void initViews() {
		// TODO Auto-generated method stub
		messageId = getIntent().getLongExtra("messageId", 0);
		hourOfDay = String.valueOf(tp.getCurrentHour());
		minute = String.valueOf(tp.getCurrentMinute());
		
		List<Alarms> saveAlarmLst = DatabaseManager.getInstance(
				HyyDLApplication.getContext()).queryAlarmById(messageId);

		tp.setIs24HourView(true);

		if (!saveAlarmLst.isEmpty()) {
			savedAlarm = saveAlarmLst.get(0);
			if (!(savedAlarm.getAlarmtime() == null || savedAlarm
					.getAlarmtime().isEmpty())) {
				String[] times = savedAlarm.getAlarmtime().split(":");
				tp.setCurrentHour(Integer.valueOf(times[0]));
				tp.setCurrentMinute(Integer.valueOf(times[1]));
			}

			if (!(savedAlarm.getDayofweek() == null || savedAlarm
					.getDayofweek().isEmpty())) {
				String[] days = savedAlarm.getDayofweek().split(":");
				day0 = days[0];
				day1 = days[1];
				day2 = days[2];
				day3 = days[3];
				day4 = days[4];
				day5 = days[5];
				day6 = days[6];

				refreshDays();

			}

			if (savedAlarm.getIspause() != null
					&& !savedAlarm.getIspause().isEmpty()) {
				if ("0".equals(savedAlarm.getIspause())) {
					// no pause status,so pause appears
					btnPause.setVisibility(View.VISIBLE);
					btnResume.setVisibility(View.GONE);

				} else {
					btnPause.setVisibility(View.GONE);
					btnResume.setVisibility(View.VISIBLE);
				}
			}

		} else {
			tp.setCurrentHour(0);
			tp.setCurrentMinute(0);
		}
	}


	private void refreshDays() {
		// TODO Auto-generated method stub
		// Sun
		if ("0".equals(day0)) {
			tvDay0.setBackgroundResource(R.drawable.btn_day_unselected);
		} else {
			tvDay0.setBackgroundResource(R.drawable.btn_day_normal);
		}

		// Mon
		if ("0".equals(day1)) {
			tvDay1.setBackgroundResource(R.drawable.btn_day_unselected);
		} else {
			tvDay1.setBackgroundResource(R.drawable.btn_day_normal);
		}

		// Tues
		if ("0".equals(day2)) {
			tvDay2.setBackgroundResource(R.drawable.btn_day_unselected);
		} else {
			tvDay2.setBackgroundResource(R.drawable.btn_day_normal);
		}

		// Wed
		if ("0".equals(day3)) {
			tvDay3.setBackgroundResource(R.drawable.btn_day_unselected);
		} else {
			tvDay3.setBackgroundResource(R.drawable.btn_day_normal);
		}

		// Thu
		if ("0".equals(day4)) {
			tvDay4.setBackgroundResource(R.drawable.btn_day_unselected);
		} else {
			tvDay4.setBackgroundResource(R.drawable.btn_day_normal);
		}

		// Fri
		if ("0".equals(day5)) {
			tvDay5.setBackgroundResource(R.drawable.btn_day_unselected);
		} else {
			tvDay5.setBackgroundResource(R.drawable.btn_day_normal);
		}

		// Sat
		if ("0".equals(day6)) {
			tvDay6.setBackgroundResource(R.drawable.btn_day_unselected);
		} else {
			tvDay6.setBackgroundResource(R.drawable.btn_day_normal);
		}

	}

	private String getDays() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append(day0);
		sb.append(":");

		sb.append(day1);
		sb.append(":");

		sb.append(day2);
		sb.append(":");

		sb.append(day3);
		sb.append(":");

		sb.append(day4);
		sb.append(":");

		sb.append(day5);
		sb.append(":");

		sb.append(day6);

		return sb.toString();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		tp.setOnTimeChangedListener(new OnTimeChangedListener() {

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				refreshTime(hourOfDay, minute);
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				List<Alarms> alarms = new ArrayList<Alarms>();

				if (null == savedAlarm) {
					savedAlarm = new Alarms();
				}

				String saveTime = getSelectedTime();
				Toast.makeText(HyyDLApplication.getContext(),
						"saveTime:" + saveTime, Toast.LENGTH_SHORT).show();

				savedAlarm.setAlarmtime(saveTime);
				savedAlarm.setMessageid(messageId);

				String strDays = getDays();

				if (!"0:0:0:0:0:0:0".equals(strDays)) {
					savedAlarm.setDayofweek(strDays);

					alarms.add(savedAlarm);

					DatabaseManager.getInstance(HyyDLApplication.getContext())
							.saveOrUpdateAlarm(alarms);

					DatabaseManager.getInstance(HyyDLApplication.getContext())
							.resumeAlarm(messageId);

					finish();
				} else {
					Toast.makeText(HyyDLApplication.getContext(),
							"Please at least choose a day!!!",
							Toast.LENGTH_SHORT).show();

				}

			}

		});

		btnPause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatabaseManager.getInstance(HyyDLApplication.getContext())
						.pauseAlarm(messageId);

				btnPause.setVisibility(View.GONE);
				btnResume.setVisibility(View.VISIBLE);
			}
		});

		btnResume.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				DatabaseManager.getInstance(HyyDLApplication.getContext())
						.resumeAlarm(messageId);
				btnPause.setVisibility(View.VISIBLE);
				btnResume.setVisibility(View.GONE);
			}
		});

		tvDay0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("0".equals(day0)) {
					day0 = "1";
				} else {
					day0 = "0";
				}
				refreshDays();
			}
		});

		tvDay1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("0".equals(day1)) {
					day1 = "1";
				} else {
					day1 = "0";
				}
				refreshDays();
			}
		});

		tvDay2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("0".equals(day2)) {
					day2 = "1";
				} else {
					day2 = "0";
				}
				refreshDays();
			}
		});

		tvDay3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("0".equals(day3)) {
					day3 = "1";
				} else {
					day3 = "0";
				}
				refreshDays();
			}
		});

		tvDay4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("0".equals(day4)) {
					day4 = "1";
				} else {
					day4 = "0";
				}
				refreshDays();
			}
		});

		tvDay5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("0".equals(day5)) {
					day5 = "1";
				} else {
					day5 = "0";
				}
				refreshDays();
			}
		});

		tvDay6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("0".equals(day6)) {
					day6 = "1";
				} else {
					day6 = "0";
				}
				refreshDays();
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

		tvDay0 = (TextView) findViewById(R.id.day0);
		tvDay1 = (TextView) findViewById(R.id.day1);
		tvDay2 = (TextView) findViewById(R.id.day2);
		tvDay3 = (TextView) findViewById(R.id.day3);
		tvDay4 = (TextView) findViewById(R.id.day4);
		tvDay5 = (TextView) findViewById(R.id.day5);
		tvDay6 = (TextView) findViewById(R.id.day6);

	}
}
