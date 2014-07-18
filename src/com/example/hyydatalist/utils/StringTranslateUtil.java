package com.example.hyydatalist.utils;

import com.example.hyydatalist.R;
import com.example.hyydatalist.application.HyyDLApplication;

public class StringTranslateUtil {

	/**
	 * intput style:3:3 output style:03:03
	 * 
	 * @param originTime
	 * @return
	 */
	public static String transRegularTime(String originTime) {

		String[] times = originTime.split(":");

		String hour = (times[0].length() == 2) ? times[0] : ("0" + times[0]);
		String miniute = (times[1].length() == 2) ? times[1] : ("0" + times[1]);

		return hour + ":" + miniute;
	}

	/**
	 * input 0:1:0:0:0:0:0 output Mon
	 * 
	 * @param originWeek
	 * @return
	 */
	public static String transRegularWeek(String originWeek) {

		String[] days = originWeek.split(":");
		StringBuilder sb = new StringBuilder();

		if ("1".equals(days[0])) {
			// Sun
			sb.append(HyyDLApplication.getContext().getResources()
					.getString(R.string.day0));
			sb.append(",");
		}

		if ("1".equals(days[1])) {
			// Mon
			sb.append(HyyDLApplication.getContext().getResources()
					.getString(R.string.day1));
			sb.append(",");
		}

		if ("1".equals(days[2])) {
			// Tues
			sb.append(HyyDLApplication.getContext().getResources()
					.getString(R.string.day2));
			sb.append(",");
		}

		if ("1".equals(days[3])) {
			// Wed
			sb.append(HyyDLApplication.getContext().getResources()
					.getString(R.string.day3));
			sb.append(",");
		}

		if ("1".equals(days[4])) {
			// Thur
			sb.append(HyyDLApplication.getContext().getResources()
					.getString(R.string.day4));
			sb.append(",");
		}

		if ("1".equals(days[5])) {
			// Fri
			sb.append(HyyDLApplication.getContext().getResources()
					.getString(R.string.day5));
			sb.append(",");
		}

		if ("1".equals(days[6])) {
			// Sat
			sb.append(HyyDLApplication.getContext().getResources()
					.getString(R.string.day6));
			// sb.append(",");
		}

		return sb.toString();
	}
}
