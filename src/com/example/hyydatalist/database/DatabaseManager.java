package com.example.hyydatalist.database;

import java.util.ArrayList;
import java.util.List;

import com.example.hyydatalist.application.HyyDLApplication;
import com.example.hyydatalist.model.HyyAlarm;
import com.example.hyydatalist.model.HyyMessage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.webkit.WebView.FindListener;
import android.widget.Toast;

public class DatabaseManager {

	private static DatabaseManager databaseManager = null;

	private static DatabaseOpenHelper openHelper = null;

	private DatabaseManager() {

	}

	public static DatabaseManager getInstance(Context context) {
		if (null == databaseManager) {
			initDatabaseManager(context);
		}

		return databaseManager;
	}

	public static void initDatabaseManager(Context context) {
		if (null == databaseManager && context != null) {
			databaseManager = new DatabaseManager();
		}

		if (null == openHelper) {
			openHelper = new DatabaseOpenHelper(context);
		}
	}

	public DatabaseOpenHelper getHelper() {
		return openHelper;
	}

	/**
	 * Message related functions
	 */

	public List<HyyMessage> queryMessage() {
		SQLiteDatabase db = openHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM messages", null);

		List<HyyMessage> list = new ArrayList<HyyMessage>();

		while (cursor.moveToNext()) {
			HyyMessage message = new HyyMessage();
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String shortcut = String.valueOf(cursor.getString(cursor
					.getColumnIndex("shortcut")));
			String id = String.valueOf(cursor.getInt(cursor
					.getColumnIndex("_id")));

			String content = String.valueOf(cursor.getString(cursor
					.getColumnIndex("content")));

			String alarmStatus = String.valueOf(cursor.getString(cursor
					.getColumnIndex("alarmstatus")));

			message.setId(id);
			message.setTitle(title);
			message.setShortcut(shortcut);
			message.setContent(content);
			message.setAlarmStatus(alarmStatus);

			list.add(message);
		}

		cursor.close();
		db.close();

		return list;

	}

	public List<HyyMessage> queryMessageById(String conditon) {
		SQLiteDatabase db = openHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM messages WHERE _id = ?",
				new String[] { conditon });

		List<HyyMessage> list = new ArrayList<HyyMessage>();

		while (cursor.moveToNext()) {
			HyyMessage message = new HyyMessage();
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String shortcut = String.valueOf(cursor.getString(cursor
					.getColumnIndex("shortcut")));
			String id = String.valueOf(cursor.getInt(cursor
					.getColumnIndex("_id")));

			String content = String.valueOf(cursor.getString(cursor
					.getColumnIndex("content")));

			message.setId(id);
			message.setTitle(title);
			message.setShortcut(shortcut);
			message.setContent(content);

			list.add(message);
		}

		cursor.close();
		db.close();

		return list;

	}

	public void saveOrUpdateMessage(List<HyyMessage> persons) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		try {

			db.beginTransaction();
			for (HyyMessage person : persons) {

				int count = 0;

				if (null != person.getId()) {
					Cursor cursor = db.rawQuery(
							"SELECT * FROM messages WHERE _id = ?",
							new String[] { person.getId() });
					count = cursor.getCount();
					cursor.close();
				}

				if (count > 0) {
					// update logic
					db.execSQL(
							"UPDATE messages SET title = ?, shortcut = ?, content = ? WHERE _id = ?",
							new Object[] { person.getTitle(),
									person.getShortcut(), person.getContent(),
									person.getId() });

				} else {
					// insert logic
					db.execSQL(
							"INSERT INTO messages VALUES (null,?,?,?,null)",
							new Object[] { person.getTitle(),
									person.getShortcut(), person.getContent() });
				}

			}

			db.setTransactionSuccessful();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void deleteMessage(HyyMessage mess) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		try {

			db.beginTransaction();

			db.execSQL("DELETE FROM messages WHERE _id = ?",
					new String[] { mess.getId() });

			db.setTransactionSuccessful();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.endTransaction();
			db.close();
		}
		HyyAlarm alarm = new HyyAlarm();
		alarm.setMessageId(mess.getId());
		this.deleteAlarm(alarm);

	}

	/**
	 * Alarm related functions active
	 */

	public List<HyyAlarm> queryAlarms() {
		SQLiteDatabase db = openHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM alarms WHERE ispause = ?",
				new String[] { "0" });

		Log.i("hyy", String.valueOf(cursor.getCount()));
		List<HyyAlarm> list = new ArrayList<HyyAlarm>();

		while (cursor.moveToNext()) {
			HyyAlarm alarm = new HyyAlarm();
			String alarmTime = cursor.getString(cursor
					.getColumnIndex("alarmtime"));
			String dayOfWeek = String.valueOf(cursor.getString(cursor
					.getColumnIndex("dayofweek")));
			String id = String.valueOf(cursor.getInt(cursor
					.getColumnIndex("_id")));

			String messageId = String.valueOf(cursor.getString(cursor
					.getColumnIndex("messageid")));

			String isPause = String.valueOf(cursor.getString(cursor
					.getColumnIndex("ispause")));

			alarm.setId(id);
			alarm.setAlarmTime(alarmTime);
			alarm.setDayOfWeek(dayOfWeek);
			alarm.setMessageId(messageId);
			alarm.setIsPause(isPause);

			list.add(alarm);
		}

		cursor.close();
		db.close();

		return list;

	}

	/**
	 * Alarm related functions all
	 */

	public List<HyyAlarm> queryAlarmsWithoutCondition() {
		SQLiteDatabase db = openHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM alarms", null);

		Log.i("hyy", String.valueOf(cursor.getCount()));
		List<HyyAlarm> list = new ArrayList<HyyAlarm>();

		while (cursor.moveToNext()) {
			HyyAlarm alarm = new HyyAlarm();
			String alarmTime = cursor.getString(cursor
					.getColumnIndex("alarmtime"));
			String dayOfWeek = String.valueOf(cursor.getString(cursor
					.getColumnIndex("dayofweek")));
			String id = String.valueOf(cursor.getInt(cursor
					.getColumnIndex("_id")));

			String messageId = String.valueOf(cursor.getString(cursor
					.getColumnIndex("messageid")));

			String isPause = String.valueOf(cursor.getString(cursor
					.getColumnIndex("ispause")));

			alarm.setId(id);
			alarm.setAlarmTime(alarmTime);
			alarm.setDayOfWeek(dayOfWeek);
			alarm.setMessageId(messageId);
			alarm.setIsPause(isPause);

			list.add(alarm);
		}

		cursor.close();
		db.close();

		return list;

	}

	public void saveOrUpdateAlarm(List<HyyAlarm> alarms) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		try {

			db.beginTransaction();

			for (HyyAlarm alarm : alarms) {

				Cursor cursor = db.rawQuery(
						"SELECT * FROM alarms WHERE messageid = ?",
						new String[] { alarm.getMessageId() });
				int count = cursor.getCount();
				cursor.close();

				if (count > 0) {
					// update logic
					db.execSQL(
							"UPDATE alarms SET alarmtime = ?, dayofweek = ?, messageid = ? WHERE messageid = ?",
							new Object[] { alarm.getAlarmTime(),
									alarm.getDayOfWeek(), alarm.getMessageId(),
									alarm.getMessageId() });

				} else {
					// insert logic
					db.execSQL(
							"INSERT INTO alarms(_id,alarmtime,dayofweek,messageid) VALUES (null,?,?,?)",
							new Object[] { alarm.getAlarmTime(),
									alarm.getDayOfWeek(), alarm.getMessageId() });
				}

			}

			db.setTransactionSuccessful();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void deleteAlarm(HyyAlarm alarm) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		try {

			db.beginTransaction();

			db.execSQL("DELETE FROM alarms WHERE messageid = ?",
					new String[] { alarm.getMessageId() });

			db.setTransactionSuccessful();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public List<HyyAlarm> queryAlarmById(String conditon) {
		SQLiteDatabase db = openHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM alarms WHERE messageid = ?",
				new String[] { conditon });

		List<HyyAlarm> list = new ArrayList<HyyAlarm>();

		while (cursor.moveToNext()) {
			HyyAlarm alarm = new HyyAlarm();
			String alarmtime = cursor.getString(cursor
					.getColumnIndex("alarmtime"));
			String dayofweek = String.valueOf(cursor.getString(cursor
					.getColumnIndex("dayofweek")));
			String id = String.valueOf(cursor.getInt(cursor
					.getColumnIndex("_id")));

			String messageid = String.valueOf(cursor.getString(cursor
					.getColumnIndex("messageid")));

			String isPause = String.valueOf(cursor.getString(cursor
					.getColumnIndex("ispause")));

			alarm.setId(id);
			alarm.setAlarmTime(alarmtime);
			alarm.setDayOfWeek(dayofweek);
			alarm.setMessageId(messageid);
			alarm.setIsPause(isPause);

			list.add(alarm);
		}

		cursor.close();
		db.close();

		return list;

	}

	public void pauseAlarm(String messageId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		try {

			db.beginTransaction();

			// update logic
			db.execSQL("UPDATE alarms SET ispause = '1' WHERE messageid = ?",
					new Object[] { messageId });

			db.execSQL("UPDATE messages SET alarmstatus = '1' WHERE _id = ?",
					new Object[] { messageId });

			db.setTransactionSuccessful();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.endTransaction();
			db.close();
		}

	}

	public void resumeAlarm(String messageId) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		try {

			db.beginTransaction();

			// update logic
			db.execSQL("UPDATE alarms SET ispause = '0' WHERE messageid = ?",
					new Object[] { messageId });

			db.execSQL("UPDATE messages SET alarmstatus = '0' WHERE _id = ?",
					new Object[] { messageId });

			db.setTransactionSuccessful();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.endTransaction();
			db.close();
		}
	}
}
