package com.example.hyydatalist.activity;

import java.util.List;

import com.example.hyydatalist.R;
import com.example.hyydatalist.application.HyyDLApplication;
import com.example.hyydatalist.database.DatabaseManager;
import com.example.hyydatalist.utils.HyyCommonUtils;
import com.hyy.hyydatalist.generator.Messages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.Toast;

/***
 * 提醒事项编辑界面
 * 
 * @author hyylj
 * 
 */
public class EditActivity extends ActionBarActivity implements OnTouchListener,
		OnGestureListener {

	/**
	 * 编辑区域的EditText控件
	 */
	EditText etContent;

	/**
	 * 传入的需要本页显示的message_id
	 */
	Long id;

	/**
	 * 提醒内容
	 */
	String content;

	/**
	 * 设置alarm的按钮在menu上的对应位置
	 */
	int ALARM_MENU = 0;

	/***
	 * 保存的之前传入的message_id(用作alarm界面返回时判定)
	 */
	static Long savedId;

	/**
	 * 保存的message
	 */
	Messages savedMessage = null;

	/***
	 * Gesture Detector
	 */
	private GestureDetector mDetector;

	private final int X_MOVE_INSTANCE = 100;// X轴位移距离
	private final int X_MOVE_SPEED = 100;// X轴移动速度 随便大点都几千

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_edit);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		mDetector = new GestureDetector(this, this);
		mDetector.setIsLongpressEnabled(true);

		int type = getIntent().getIntExtra("forwardType", -1);

		if (-1 == type) {

			id = savedId;

		} else {
			id = getIntent().getLongExtra("id", -1L);
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

			// Toast.makeText(HyyDLApplication.getContext(),
			// "You press Alarm btn!", Toast.LENGTH_SHORT).show();
			forwardAlarm();
			return true;

		case android.R.id.home:
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
				TaskStackBuilder.create(this)
						.addNextIntentWithParentStack(upIntent)
						.startActivities();
			} else {
				upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				NavUtils.navigateUpTo(this, upIntent);
			}
			return true;

		}

		return false;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);

		// MenuItem item = menu.getItem(ALARM_MENU);
		// if (id == -1L) {
		//
		// item.setVisible(false);
		// } else {
		// item.setVisible(true);
		// }

		return true;
	}

	private void forwardAlarm() {

		if (-1 == id && !saveItem()) {
			Toast.makeText(
					HyyDLApplication.getContext(),
					HyyDLApplication.getContext().getResources()
							.getString(R.string.write_first),
					Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = new Intent(HyyDLApplication.getContext(),
				AlarmConfigActivity.class);

		intent.putExtra("messageId", id);
		this.startActivity(intent);
	}

	private void findItemById(Long id) {
		// TODO Auto-generated method stub
		if (id == -1L) {
			content = "";

		} else {
			List<Messages> selectedMessage = DatabaseManager.getInstance(
					HyyDLApplication.getContext()).queryMessageById(id);
			savedMessage = selectedMessage.get(0);
			content = selectedMessage.get(0).getContent();
		}

	}

	private void initData() {
		// TODO Auto-generated method stub
		etContent.setText(content);

		// set cursor to last
		Editable etext = etContent.getText();
		Selection.setSelection(etext, etext.length());
	}

	private void init() {
		etContent = (EditText) findViewById(R.id.et_content);

		// 只有这样，view才能够处理不同于Tap（轻触）的hold（即ACTION_MOVE，或者多个ACTION_DOWN）
		etContent.setLongClickable(true);

		// register listener
		etContent.setOnTouchListener(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		saveItem();
		savedId = id;
		super.onPause();
	}

	private boolean saveItem() {

		if (!("".equals(etContent.getText().toString()))) {

			if (-1L == id) {
				savedMessage = new Messages();
			}

			savedMessage.setContent(etContent.getText().toString());
			savedMessage.setShortcut(HyyCommonUtils.getPokerFaceRandom());

			id = DatabaseManager.getInstance(HyyDLApplication.getContext())
					.saveOrUpdateMessage(savedMessage);

			return true;

		}
		
		return false;

	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i("HYY", "onDown presented");
		//触发onScroll和onFling,必须让监听器的onDown的返回值设为true
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i("HYY", "onShowPress presented");

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i("HYY", "onSingleTapUp presented");
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		Log.i("HYY", "onScroll presented");
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		Log.i("HYY", "onLongPress presented");
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub

		Log.i("HYY", "onFling presented");

		Log.i("e2.getX() - e1.getX():", String.valueOf(e2.getX() - e1.getX()));
		Log.i("X_MOVE_INSTANCE", String.valueOf(X_MOVE_INSTANCE));
		Log.i("velocityX", String.valueOf(velocityX));
		Log.i("X_MOVE_SPEED", String.valueOf(X_MOVE_SPEED));

		// 向右划动
		if ((e2.getX() - e1.getX() > X_MOVE_INSTANCE)
				&& Math.abs(velocityX) > X_MOVE_SPEED) {

			this.onPause();
			this.finish();

			Toast.makeText(HyyDLApplication.getContext(), "You slide right",
					Toast.LENGTH_SHORT).show();
			return true;

		} else // 向左划动
		if ((e1.getX() - e2.getX() > X_MOVE_INSTANCE)
				&& Math.abs(velocityX) > X_MOVE_SPEED) {
			Toast.makeText(HyyDLApplication.getContext(), "You slide left",
					Toast.LENGTH_SHORT).show();
		}
		return false;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.i("HYY", "onTouch presented");
		return mDetector.onTouchEvent(event);
	}

}
