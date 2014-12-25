package com.example.hyydatalist.database;

import com.example.hyydatalist.constants.HyyConstants;

import android.content.Context;

public class DatabaseManager {

	private static IDataBaseWork databaseWork = null;

	private static DatabaseOpenHelper openHelper = null;

	/*
	 * db solution now
	 */
	private static int db_sol_now = HyyConstants.DB_SOL_GREEN;

	private DatabaseManager() {

	}

	public static IDataBaseWork getInstance(Context context) {
		if (null == databaseWork) {
			initDatabaseManager(context);
		}

		return databaseWork;
	}

	public static void initDatabaseManager(Context context) {

		if (HyyConstants.DB_SOL_ORI == db_sol_now) {
			// orginal way

			if (null == openHelper) {
				openHelper = new DatabaseOpenHelper(context);
			}

			if (null == databaseWork && context != null) {
				databaseWork = new DataBaseWorkOrigin(openHelper);
			}

		}

		else if (HyyConstants.DB_SOL_GREEN == db_sol_now) {
			// Green way
			// TO DO
			if (null == databaseWork && context != null) {
				databaseWork =  new DataBaseWorkGreen(context);
			}

		}

	}

}
