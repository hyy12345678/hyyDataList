package com.example.hyydatalist.activity;

import java.lang.reflect.Field;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Message;

import com.example.hyydatalist.R;
import com.example.hyydatalist.adapter.HyyDataListAdapter;
import com.example.hyydatalist.application.HyyDLApplication;
import com.example.hyydatalist.constants.HyyConstants;
import com.example.hyydatalist.database.DatabaseManager;
import com.example.hyydatalist.model.HyyMessage;

public class MainActivity extends ActionBarActivity {

	ListView listView;
	EditText etSearchCondition;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case HyyConstants.REFRESH_LIST:
				Toast.makeText(HyyDLApplication.getContext().getApplicationContext(), "Refresh list", Toast.LENGTH_SHORT).show();
				initList();
				break;

			default:
				break;
			}
			
		};
	};
	HyyDataListAdapter adapter = new HyyDataListAdapter(
			HyyDLApplication.getContext().getApplicationContext(), handler);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		disableMenu();

		init();

		initListener();

		itemOnLongClick();

	}

	private void disableMenu() {
		// TODO Auto-generated method stub
		try {
			ViewConfiguration mconfig = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class
					.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(mconfig, false);
			}
		} catch (Exception ex) {
			Log.i("hyy", "disable menu failed");
		}
	}

	private void itemOnLongClick() {
		// TODO Auto-generated method stub
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				menu.add(0, 0, 0, "Delete");
				menu.add(0, 1, 0, "Alarm");

			}
		});
	}

	// 长按菜单响应函数
	public boolean onContextItemSelected(MenuItem item) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int mstion = info.position;

		HyyMessage selected = (HyyMessage) adapter.getItem(mstion);

		switch (item.getItemId()) {

		case 0:
			Toast.makeText(HyyDLApplication.getContext(), "delete pressed",
					Toast.LENGTH_SHORT).show();

			HyyMessage mess = new HyyMessage();
			mess.setId(selected.getId());
			mess.setTitle(selected.getTitle());
			mess.setShortcut(selected.getShortcut());

			DatabaseManager.getInstance(HyyDLApplication.getContext())
					.deleteMessage(mess);
//			initList();
			adapter.getData();
			break;

		case 1:
			Toast.makeText(HyyDLApplication.getContext(), "Alarm pressed",
					Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(HyyDLApplication.getContext(),
					AlarmConfigActivity.class);
			intent.putExtra("messageId", selected.getId());
			this.startActivity(intent);
			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);

	}

	private void initListener() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				HyyMessage map = (HyyMessage) listView.getItemAtPosition(arg2);

				String name = map.getTitle();
				String age = map.getShortcut();
				String id = map.getId();

				Toast.makeText(HyyDLApplication.getContext(),
						"Name Hyy :" + name + "; Age Hyy :" + age,
						Toast.LENGTH_SHORT).show();

				Intent intent = new Intent(HyyDLApplication.getContext(),
						EditActivity.class);
				intent.putExtra("title", name);
				intent.putExtra("shortcut", age);
				intent.putExtra("id", id);

				startActivity(intent);
			}
		});

		etSearchCondition.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				adapter.getFilter().filter(etSearchCondition.getText());
			}
		});

	}

	private void createNewMessage() {
		Toast.makeText(HyyDLApplication.getContext(), "Add a new item.",
				Toast.LENGTH_SHORT).show();

		Intent intent = new Intent(HyyDLApplication.getContext(),
				EditActivity.class);
		intent.putExtra("title", "");
		intent.putExtra("shortcut", "");
		intent.putExtra("id", "");

		startActivity(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
//		initList();
		adapter.getData();
	}

	private void initList() {
//		adapter = new HyyDataListAdapter(HyyDLApplication.getContext());
		listView.setAdapter(adapter);
	}

	private void init() {
		// TODO Auto-generated method stub
		listView = (ListView) findViewById(R.id.list);
		etSearchCondition = (EditText) findViewById(R.id.tvSearchCondititon);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Toast.makeText(HyyDLApplication.getContext(),
					"You press setting btn!", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_add:
			createNewMessage();
			return true;
		case R.id.action_photo:
			Toast.makeText(HyyDLApplication.getContext(),
					"You press photo btn!", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_weather:
			Toast.makeText(HyyDLApplication.getContext(),
					"You press weather btn!", Toast.LENGTH_SHORT).show();
			return true;
			
		}
		return false; // should never happen
	}

}
