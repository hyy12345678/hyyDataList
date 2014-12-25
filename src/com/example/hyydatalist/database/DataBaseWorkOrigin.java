package com.example.hyydatalist.database;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hyy.hyydatalist.generator.Alarms;
import com.hyy.hyydatalist.generator.Messages;

public class DataBaseWorkOrigin implements IDataBaseWork {
	
	private static DatabaseOpenHelper openHelper = null;
	
	public DataBaseWorkOrigin(DatabaseOpenHelper helper) {
		// TODO Auto-generated constructor stub
		this.openHelper = helper;
	}

	/**
	 * Message related functions
	 */

	public List<Messages> queryMessage() {
		SQLiteDatabase db = openHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM messages", null);

		List<Messages> list = new ArrayList<Messages>();

		while (cursor.moveToNext()) {
			Messages message = new Messages();
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String shortcut = String.valueOf(cursor.getString(cursor
					.getColumnIndex("shortcut")));
			long id = cursor.getInt(cursor
					.getColumnIndex("_id"));

			String content = String.valueOf(cursor.getString(cursor
					.getColumnIndex("content")));

			String alarmStatus = String.valueOf(cursor.getString(cursor
					.getColumnIndex("alarmstatus")));

			message.setId(id);
			message.setTitle(title);
			message.setShortcut(shortcut);
			message.setContent(content);
			message.setAlarmstatus(alarmStatus);

			list.add(message);
		}

		cursor.close();
		db.close();

		return list;

	}

	public List<Messages> queryMessageById(String conditon) {
		SQLiteDatabase db = openHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM messages WHERE _id = ?",
				new String[] { conditon });

		List<Messages> list = new ArrayList<Messages>();

		while (cursor.moveToNext()) {
			Messages message = new Messages();
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String shortcut = String.valueOf(cursor.getString(cursor
					.getColumnIndex("shortcut")));
			long id = cursor.getInt(cursor
					.getColumnIndex("_id"));

			String content = String.valueOf(cursor.getString(cursor
					.getColumnIndex("content")));
			
			String alarmStatus = String.valueOf(cursor.getString(cursor
					.getColumnIndex("alarmstatus")));

			message.setId(id);
			message.setTitle(title);
			message.setShortcut(shortcut);
			message.setContent(content);
			message.setAlarmstatus(alarmStatus);

			list.add(message);
		}

		cursor.close();
		db.close();

		return list;

	}

	public void saveOrUpdateMessage(List<Messages> persons) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		try {

			db.beginTransaction();
			for (Messages person : persons) {

				int count = 0;

				if (null != person.getId()) {
					Cursor cursor = db.rawQuery(
							"SELECT * FROM messages WHERE _id = ?",
							new String[] { String.valueOf(person.getId())});
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

	public void deleteMessage(Messages mess) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		try {

			db.beginTransaction();

			db.execSQL("DELETE FROM messages WHERE _id = ?",
					new String[] { mess.getId().toString() });

			db.setTransactionSuccessful();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.endTransaction();
			db.close();
		}
		Alarms alarm = new Alarms();
		alarm.setMessageid(mess.getId().toString());
		this.deleteAlarm(alarm);

	}

	/**
	 * Alarm related functions active
	 */

	public List<Alarms> queryAlarms() {
		SQLiteDatabase db = openHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM alarms WHERE ispause = ?",
				new String[] { "0" });

		Log.i("hyy", String.valueOf(cursor.getCount()));
		List<Alarms> list = new ArrayList<Alarms>();

		while (cursor.moveToNext()) {
			Alarms alarm = new Alarms();
			String alarmTime = cursor.getString(cursor
					.getColumnIndex("alarmtime"));
			String dayOfWeek = String.valueOf(cursor.getString(cursor
					.getColumnIndex("dayofweek")));
			long id = cursor.getInt(cursor
					.getColumnIndex("_id"));

			String messageId = String.valueOf(cursor.getString(cursor
					.getColumnIndex("messageid")));

			String isPause = String.valueOf(cursor.getString(cursor
					.getColumnIndex("ispause")));

			alarm.setId(id);
			alarm.setAlarmtime(alarmTime);
			alarm.setDayofweek(dayOfWeek);
			alarm.setMessageid(messageId);
			alarm.setIspause(isPause);

			list.add(alarm);
		}

		cursor.close();
		db.close();

		return list;

	}

	/**
	 * Alarm related functions all
	 */

	public List<Alarms> queryAlarmsWithoutCondition() {
		SQLiteDatabase db = openHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM alarms", null);

		Log.i("hyy", String.valueOf(cursor.getCount()));
		List<Alarms> list = new ArrayList<Alarms>();

		while (cursor.moveToNext()) {
			Alarms alarm = new Alarms();
			String alarmTime = cursor.getString(cursor
					.getColumnIndex("alarmtime"));
			String dayOfWeek = String.valueOf(cursor.getString(cursor
					.getColumnIndex("dayofweek")));
			long id = cursor.getInt(cursor
					.getColumnIndex("_id"));

			String messageId = String.valueOf(cursor.getString(cursor
					.getColumnIndex("messageid")));

			String isPause = String.valueOf(cursor.getString(cursor
					.getColumnIndex("ispause")));

			alarm.setId(id);
			alarm.setAlarmtime(alarmTime);
			alarm.setDayofweek(dayOfWeek);
			alarm.setMessageid(messageId);
			alarm.setIspause(isPause);

			list.add(alarm);
		}

		cursor.close();
		db.close();

		return list;

	}

	public void saveOrUpdateAlarm(List<Alarms> alarms) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		try {

			db.beginTransaction();

			for (Alarms alarm : alarms) {

				Cursor cursor = db.rawQuery(
						"SELECT * FROM alarms WHERE messageid = ?",
						new String[] { alarm.getMessageid()});
				int count = cursor.getCount();
				cursor.close();

				if (count > 0) {
					// update logic
					db.execSQL(
							"UPDATE alarms SET alarmtime = ?, dayofweek = ?, messageid = ? WHERE messageid = ?",
							new Object[] { alarm.getAlarmtime(),
									alarm.getDayofweek(), alarm.getMessageid(),
									alarm.getMessageid()});

				} else {
					// insert logic
					db.execSQL(
							"INSERT INTO alarms(_id,alarmtime,dayofweek,messageid) VALUES (null,?,?,?)",
							new Object[] { alarm.getAlarmtime(),
									alarm.getDayofweek(), alarm.getMessageid() });
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

	public void deleteAlarm(Alarms alarm) {
		SQLiteDatabase db = openHelper.getWritableDatabase();
		try {

			db.beginTransaction();

			db.execSQL("DELETE FROM alarms WHERE messageid = ?",
					new String[] { alarm.getMessageid() });

			db.setTransactionSuccessful();

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			db.endTransaction();
			db.close();
		}
	}

	public List<Alarms> queryAlarmById(String conditon) {
		SQLiteDatabase db = openHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery("SELECT * FROM alarms WHERE messageid = ?",
				new String[] { conditon });

		List<Alarms> list = new ArrayList<Alarms>();

		while (cursor.moveToNext()) {
			Alarms alarm = new Alarms();
			String alarmtime = cursor.getString(cursor
					.getColumnIndex("alarmtime"));
			String dayofweek = String.valueOf(cursor.getString(cursor
					.getColumnIndex("dayofweek")));
			long id = cursor.getInt(cursor
					.getColumnIndex("_id"));

			String messageid = String.valueOf(cursor.getString(cursor
					.getColumnIndex("messageid")));

			String isPause = String.valueOf(cursor.getString(cursor
					.getColumnIndex("ispause")));

			alarm.setId(id);
			alarm.setAlarmtime(alarmtime);
			alarm.setDayofweek(dayofweek);
			alarm.setMessageid(messageid);
			alarm.setIspause(isPause);

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
