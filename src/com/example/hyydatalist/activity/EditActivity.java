package com.example.hyydatalist.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.hyydatalist.R;
import com.example.hyydatalist.application.HyyDLApplication;
import com.example.hyydatalist.database.DatabaseManager;
import com.hyy.hyydatalist.generator.Messages;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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

	Messages savedMessage = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_edit);

		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(true);

		id = getIntent().getStringExtra("id");

		findItemById(id);
		init();
		initData();

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
		super.onPause();
	}

	private void saveItem() {

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
		} else {
			Toast.makeText(HyyDLApplication.getContext(), "You input nothing",
					Toast.LENGTH_SHORT).show();
		}

	}

}
