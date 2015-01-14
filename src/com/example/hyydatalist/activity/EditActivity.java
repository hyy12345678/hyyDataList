package com.example.hyydatalist.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.hyydatalist.R;
import com.example.hyydatalist.application.HyyDLApplication;
import com.example.hyydatalist.database.DatabaseManager;
import com.hyy.hyydatalist.generator.Messages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends ActionBarActivity {

	EditText etTitle;
	EditText etShortcut;
	EditText etContent;

	String title;
	String shortcut;
	String id;
	String content;
	int ALARM_MENU = 0;

	static String savedId;

	Messages savedMessage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_edit);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		int type = getIntent().getIntExtra("forwardType", -1);

		if (-1 == type) {

			id = savedId;

		} else {
			id = getIntent().getStringExtra("id");
		}

		Log.i("test", "type:" + type);

		Log.i("test", "id:" + id);

		findItemById(id);
		init();
		initData();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.edit, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_alarm:

			Toast.makeText(HyyDLApplication.getContext(),
					"You press Alarm btn!", Toast.LENGTH_SHORT).show();
			forwardAlarm();
			return true;

		}

		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);

		MenuItem item = menu.getItem(ALARM_MENU);
		if (null == id || id.isEmpty()) {

			item.setVisible(false);
		} else {
			item.setVisible(true);
		}

		return true;
	}

	private void forwardAlarm() {

		Intent intent = new Intent(HyyDLApplication.getContext(),
				AlarmConfigActivity.class);
		intent.putExtra("messageId", Long.valueOf(id));
		this.startActivity(intent);
	}

	private void findItemById(String id) {
		// TODO Auto-generated method stub
		if ("".equals(id)) {
			title = "";
			shortcut = "";
			content = "";

		} else {
			List<Messages> selectedMessage = DatabaseManager.getInstance(
					HyyDLApplication.getContext()).queryMessageById(id);
			savedMessage = selectedMessage.get(0);
			title = selectedMessage.get(0).getTitle();
			shortcut = selectedMessage.get(0).getShortcut();
			content = selectedMessage.get(0).getContent();
		}

	}

	private void initData() {
		// TODO Auto-generated method stub
		etTitle.setText(title);
		etShortcut.setText(shortcut);
		etContent.setText(content);
	}

	private void init() {
		// TODO Auto-generated method stub
		etTitle = (EditText) findViewById(R.id.et_title);
		etShortcut = (EditText) findViewById(R.id.et_shortcut);
		etContent = (EditText) findViewById(R.id.et_content);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		saveItem();
		savedId = id;
		super.onPause();
	}

	private boolean saveItem() {

		if (!("".equals(etTitle.getText().toString().trim())
				&& "".equals(etShortcut.getText().toString().trim()) && ""
					.equals(etContent.getText().toString()))) {
			List<Messages> persons = new ArrayList<Messages>();

			// Messages person = new Messages();

			if ("".equals(id)) {
				savedMessage = new Messages();
			}

			savedMessage.setTitle(etTitle.getText().toString());
			savedMessage.setShortcut(etShortcut.getText().toString());
			savedMessage.setContent(etContent.getText().toString());

			persons.add(savedMessage);

			DatabaseManager.getInstance(HyyDLApplication.getContext())
					.saveOrUpdateMessage(persons);

			return true;

		} else {
			Toast.makeText(HyyDLApplication.getContext(), "You input nothing",
					Toast.LENGTH_SHORT).show();
		}
		return false;

	}

}
