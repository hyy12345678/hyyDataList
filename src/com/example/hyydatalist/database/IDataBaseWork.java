package com.example.hyydatalist.database;

import java.util.List;

import com.hyy.hyydatalist.generator.Alarms;
import com.hyy.hyydatalist.generator.Messages;

/**
 * Interface for furture new DB solution(GreenDao eg).
 * @author hyylj
 *
 */
public interface IDataBaseWork {
	
	public List<Messages> queryMessage();
	
	public List<Messages> queryMessageById(String conditon);
	
	public void saveOrUpdateMessage(List<Messages> persons);
	
	public void deleteMessage(Messages mess);
	
	public List<Alarms> queryAlarms();
	
	public List<Alarms> queryAlarmsWithoutCondition();
	
	public void saveOrUpdateAlarm(List<Alarms> alarms);
	
	public void deleteAlarm(Alarms alarm);
	
	public List<Alarms> queryAlarmById(String messageId);
	
	public void pauseAlarm(String messageId);
	
	public void resumeAlarm(String messageId);

}
