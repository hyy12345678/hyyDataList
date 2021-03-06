package com.example.hyydatalist.activity;

import java.util.List;

import com.example.hyydatalist.R;
import com.example.hyydatalist.application.HyyDLApplication;
import com.example.hyydatalist.database.DatabaseManager;
import com.hyy.hyydatalist.generator.Messages;

import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;

public class AlarmFaceActivity extends Activity {

	Long messageId;

	TextView tvtest;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_face);

		messageId = getIntent().getLongExtra("messageId",-1L);
		tvtest = (TextView) findViewById(R.id.alarm_face_content);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		List<Messages> selectedMessage = DatabaseManager.getInstance(
				HyyDLApplication.getContext()).queryMessageById(messageId);

		tvtest.setText(selectedMessage.get(0).getContent());
		
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

}
