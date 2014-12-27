package com.example.hyydatalist.database;

import android.content.Context;

public class DatabaseManager {

	private static IDataBaseWork databaseWork = null;

	private DatabaseManager() {

	}

	public static IDataBaseWork getInstance(Context context) {
		if (null == databaseWork) {
			initDatabaseManager(context);
		}

		return databaseWork;
	}

	public static void initDatabaseManager(Context context) {

		//Initial GreenDao
		if (null == databaseWork && context != null) {
			databaseWork = new DataBaseWorkGreen(context);

		}

	}

}
