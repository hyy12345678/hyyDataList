package com.example.hyydatalist.activity;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewConfiguration;
import android.widget.AbsListView.MultiChoiceModeListener;
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
import com.example.hyydatalist.utils.HyyCommonUtils;
import com.hyy.hyydatalist.generator.Alarms;
import com.hyy.hyydatalist.generator.Messages;

/***
 * Main list page for hyydatalist
 * 
 * @author hyylj
 * 
 */
public class MainActivity extends ActionBarActivity {

	ListView listView;
	EditText etSearchCondition;

	private int index;
	private int top;

	private Handler handler;

	private HyyDataListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();

		initListener();

		// do inital work according to different sys version
		initByVersion();

	}

	/***
	 * change for 4.0 UI
	 */
	private void initByVersion() {
		// TODO Auto-generated method stub
		if (HyyCommonUtils.getHandSetSDKVer() < 11) {
			// work for UI below 4.0
			itemOnLongClick();
		} else {
			// only works fine with 4.0
			disableMenu();

			// initial for Contextual action Mode in 4.0
			initialContextualAction();

		}
	}

	/***
	 * only works fine with android 4.0 and beyond
	 */
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
			Log.i(HyyConstants.HYY_TAG, "disable menu failed");
		}
	}

	/***
	 * initial Contextual action mode
	 */
	@SuppressLint("NewApi")
	private void initialContextualAction() {
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

			@Override
			public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// TODO Auto-generated method stub
				adapter.showCheckOrNot(false);
				etSearchCondition.setVisibility(View.VISIBLE);

			}

			@Override
			public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				// TODO Auto-generated method stub

				MenuInflater inflater = mode.getMenuInflater();
				inflater.inflate(R.menu.contextual_main, menu);

				adapter.showCheckOrNot(true);
				etSearchCondition.setVisibility(View.GONE);

				return true;
			}

			@Override
			public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
				case R.id.action_cont_del:

					adapter.deleteSelected();
					mode.finish();

					return true;

				default:
					return false;
				}

			}

			@Override
			public void onItemCheckedStateChanged(ActionMode mode,
					int position, long id, boolean checked) {
				// TODO Auto-generated method stub

				Log.i("big found", "you click " + position + " item");
				adapter.updateSelected(position, id, listView, checked);

			}

		});

	}

	/***
	 * used for floating context menu
	 */
	private void itemOnLongClick() { // TODO Auto-generated method stub
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) { // TODO Auto-generated method
												// stub
				menu.add(0, 0, 0, "Delete");
				menu.add(0, 1, 0, "Alarm");

			}
		});
	}

	/***
	 * delete selected item from db
	 * 
	 * @param selecedPos
	 */
	private void deleteSelectedFromDB(List<Integer> selecedPos) {

		for (int mstion : selecedPos) {
			Messages selected = (Messages) adapter.getItem(mstion);
			if (selected.getAlarmstatus() != null) {
				Alarms alarm = DatabaseManager
						.getInstance(HyyDLApplication.getContext())
						.queryAlarmById(selected.getId().longValue()).get(0);

				DatabaseManager.getInstance(HyyDLApplication.getContext())
						.deleteAlarm(alarm);
			}

			DatabaseManager.getInstance(HyyDLApplication.getContext())
					.deleteMessage(selected);
		}

	}

	/***
	 * floating context menu long press list Item
	 */
	public boolean onContextItemSelected(MenuItem item) {

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		int mstion = info.position;

		switch (item.getItemId()) {

		case 0:
			// Toast.makeText(HyyDLApplication.getContext(), "delete pressed",
			// Toast.LENGTH_SHORT).show();

			List<Integer> list = new ArrayList<Integer>();
			list.add(mstion);

			deleteSelectedFromDB(list);

			adapter.getData();
			break;

		case 1:
			// Toast.makeText(HyyDLApplication.getContext(), "Alarm pressed",
			// Toast.LENGTH_SHORT).show();

			Messages selected = (Messages) adapter.getItem(mstion);

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

	/***
	 * initial for listeners
	 */
	private void initListener() {
		// TODO Auto-generated method stub
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Messages message = (Messages) listView.getItemAtPosition(arg2);

				String name = message.getTitle();
				String age = message.getShortcut();
				Long idd = message.getId();

				// Toast.makeText(HyyDLApplication.getContext(),
				// "Name Hyy :" + name + "; Age Hyy :" + age,
				// Toast.LENGTH_SHORT).show();

				// store last position in listView
				saveIndexAndTopPosition();

				Intent intent = new Intent(HyyDLApplication.getContext(),
						EditActivity.class);
				intent.putExtra("title", name);
				intent.putExtra("shortcut", age);
				intent.putExtra("id", idd);
				intent.putExtra("forwardType", HyyConstants.FORWARD_EDIT);

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

	/***
	 * Add new Message Intent
	 */
	private void createNewMessage() {
		// Toast.makeText(HyyDLApplication.getContext(), "Add a new item.",
		// Toast.LENGTH_SHORT).show();

		// set last press position to the end of listview
		saveIndexAndTopPosition();

		Intent intent = new Intent(HyyDLApplication.getContext(),
				EditActivity.class);
		intent.putExtra("title", "");
		intent.putExtra("shortcut", "");
		intent.putExtra("id", -1L);
		intent.putExtra("forwardType", HyyConstants.FORWARD_NEW);

		startActivity(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// initList();
		adapter.getData();
	}

	/***
	 * initial ListView
	 */
	private void initList() {
		listView.setAdapter(adapter);
		restoreIndexAndTopPosition();
	}

	private void init() {

		// init UI elements
		listView = (ListView) findViewById(R.id.list);
		etSearchCondition = (EditText) findViewById(R.id.tvSearchCondititon);

		// init handler&adapter
		handler = new MyHandler(this);
		adapter = new HyyDataListAdapter(HyyDLApplication.getContext()
				.getApplicationContext(), handler);

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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		handler.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

	/***
	 * save index and top position
	 */
	private void saveIndexAndTopPosition() {
		index = listView.getFirstVisiblePosition();
		View v = listView.getChildAt(0);
		top = (v == null) ? 0 : v.getTop();
	}

	/***
	 * restore index and top position
	 */
	private void restoreIndexAndTopPosition() {
		listView.setSelectionFromTop(index, top);
	}

	/***
	 * inner class for hander
	 * 
	 * @author hyylj
	 * 
	 */
	static class MyHandler extends Handler {

		// Weakreference to the outer class's instance
		private WeakReference<MainActivity> mouter;

		public MyHandler(MainActivity main) {
			// TODO Auto-generated constructor stub
			mouter = new WeakReference<MainActivity>(main);
		}

		@Override
		public void handleMessage(Message msg) {

			MainActivity outer = mouter.get();

			switch (msg.what) {
			case HyyConstants.REFRESH_LIST:
				// Toast.makeText(
				// HyyDLApplication.getContext().getApplicationContext(),
				// "Refresh list", Toast.LENGTH_SHORT).show();
				if (outer != null) {
					outer.initList();
				}
				break;

			default:
				break;
			}
		}
	}

}
