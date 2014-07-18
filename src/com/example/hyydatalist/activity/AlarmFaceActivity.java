package com.example.hyydatalist.activity;

import com.example.hyydatalist.R;
import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;

public class AlarmFaceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_face);
		
		String text = getIntent().getStringExtra("messageId");
		
		TextView tvtest = (TextView)findViewById(R.id.alarm_face_text_test1);
		
		tvtest.setText(text);
		
		
	}

	

}
