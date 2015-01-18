package com.example.hyydatalist.database;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.hyydatalist.constants.HyyConstants;
import com.hyy.hyydatalist.generator.Alarms;
import com.hyy.hyydatalist.generator.AlarmsDao;
import com.hyy.hyydatalist.generator.DaoMaster;
import com.hyy.hyydatalist.generator.DaoSession;
import com.hyy.hyydatalist.generator.DaoMaster.DevOpenHelper;
import com.hyy.hyydatalist.generator.Messages;
import com.hyy.hyydatalist.generator.MessagesDao;
import com.hyy.hyydatalist.generator.MessagesDao.Properties;

public class DataBaseWorkGreen implements IDataBaseWork {

	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private AlarmsDao alarmDao;
	private MessagesDao messageDao;

	public DataBaseWorkGreen(Context context) {
		// TODO Auto-generated constructor stub
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "notes-db",
				null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		messageDao = daoSession.getMessagesDao();
		alarmDao = daoSession.getAlarmsDao();
	}

	@Override
	public List<Messages> queryMessage() {
		// TODO Auto-generated method stub
		return messageDao.loadAll();

	}

	@Override
	public List<Messages> queryMessageById(Long conditon) {
		// TODO Auto-generated method stub
		return messageDao.queryBuilder().where(Properties.Id.eq(conditon))
				.list();
	}

	@Override
	public void saveOrUpdateMessage(List<Messages> messages) {
		// TODO Auto-generated method stub
		messageDao.insertOrReplaceInTx(messages);

	}

	@Override
	public void deleteMessage(Messages mess) {
		// TODO Auto-generated method stub
		messageDao.delete(mess);
		
		/***
		 *Test for daoSession 
		
		Messages messTemp = new Messages();
		messTemp.setContent("This is made by daoSession");
		daoSession.insert(messTemp);
		 */

	}

	@Override
	public List<Alarms> queryAlarms() {
		// TODO Auto-generated method stub
		return alarmDao
				.queryBuilder()
				.where(com.hyy.hyydatalist.generator.AlarmsDao.Properties.Ispause
						.eq("0")).list();
	}

	@Override
	public List<Alarms> queryAlarmsWithoutCondition() {
		// TODO Auto-generated method stub
		return alarmDao.loadAll();
	}

	@Override
	public void saveOrUpdateAlarm(List<Alarms> alarms) {
		// TODO Auto-generated method stub
		alarmDao.insertOrReplaceInTx(alarms);

	}

	@Override
	public void deleteAlarm(Alarms alarm) {
		// TODO Auto-generated method stub
		alarmDao.delete(alarm);

	}

	@Override
	public List<Alarms> queryAlarmById(Long messageId) {
		// TODO Auto-generated method stub

		Log.i(HyyConstants.HYY_TAG, "messageId is:" + messageId);
		return alarmDao
				.queryBuilder()
				.where(com.hyy.hyydatalist.generator.AlarmsDao.Properties.Messageid
						.eq(messageId)).list();
	}

	@Override
	public void pauseAlarm(Long messageId) {
		// TODO Auto-generated method stub
		Alarms alarm = this.queryAlarmById(messageId).get(0);
		alarm.setIspause(HyyConstants.ALARM_STATUS_ONE);
		alarmDao.update(alarm);

		Messages mess = this.queryMessageById(messageId).get(0);
		mess.setAlarmstatus(HyyConstants.ALARM_STATUS_ONE);
		messageDao.update(mess);

	}

	@Override
	public void resumeAlarm(Long messageId) {
		// TODO Auto-generated method stub
		Alarms alarm = this.queryAlarmById(messageId).get(0);
		alarm.setIspause(HyyConstants.ALARM_STATUS_ZERO);
		alarmDao.update(alarm);

		Messages mess = this.queryMessageById(messageId).get(0);
		mess.setAlarmstatus(HyyConstants.ALARM_STATUS_ZERO);
		messageDao.update(mess);
	}

}
