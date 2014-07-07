package com.example.hyydatalist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "test.db";

	private static final int DB_VERSION = 7;

	private static final String INITIAL_STR = "CREATE TABLE IF NOT EXISTS messages (_id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR, shortcut VARCHAR,content VARCHAR,alarmstatus VARCHAR)";

	private static final String DROP_STR = "DROP TABLE IF EXISTS messages";

	private static final String INITIAL_STR2 = "CREATE TABLE IF NOT EXISTS alarms (_id INTEGER PRIMARY KEY AUTOINCREMENT,alarmtime VARCHAR,dayofweek VARCHAR,messageid VARCHAR,ispause VARCHAR DEFAULT '0')";

	private static final String DROP_STR2 = "DROP TABLE IF EXISTS alarms";
	
	public DatabaseOpenHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
		Log.i("DatabaseOpenHelper", "open helper db created!!");

	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {

		//message initial
		arg0.execSQL(DROP_STR);
		arg0.execSQL(INITIAL_STR);
		
		//alarm initial
		arg0.execSQL(DROP_STR2);
		arg0.execSQL(INITIAL_STR2);
		
		Log.i("DatabaseOpenHelper", "onCreated db !!");

		// Person person = new Person();
		// person.setName ("john");
		// person.setAge(30);
		// arg0.execSQL("INSERT INTO person VALUES (NULL, ?, ?)", new
		// Object[]{person.getName(), person.getAge()});

		String welcomeTitle = "Welcome to DearList";
		String welcomeShortcut = "This is the sample shortcut";
		String welcomeContent = "This is the sample content";

		for (int i = 0; i < 1; i++) {
			arg0.execSQL("INSERT INTO messages VALUES (NULL, ?, ?, ?,null)",
					new Object[] { welcomeTitle, welcomeShortcut,
							welcomeContent });
		}

		Log.i("DatabaseOpenHelper", "inseart db !!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		arg0.execSQL(DROP_STR);
		arg0.execSQL(DROP_STR2);
		onCreate(arg0);

		Log.i("DatabaseOpenHelper", "reinstall db!!");

	}

}
