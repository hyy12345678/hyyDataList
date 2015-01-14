package com.example.hyydatalist.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.hyydatalist.R;
import com.example.hyydatalist.application.HyyDLApplication;
import com.example.hyydatalist.constants.HyyConstants;
import com.example.hyydatalist.database.DatabaseManager;
import com.example.hyydatalist.utils.StringTranslateUtil;
import com.example.hyydatalist.viewholder.ViewHolder;
import com.hyy.hyydatalist.generator.Alarms;
import com.hyy.hyydatalist.generator.Messages;

public class HyyDataListAdapter extends BaseAdapter implements Filterable {
	private Context mContext;
	private List<Messages> list;
	private List<Messages> listWhole;
	private Handler handler;
	private Map<String, Alarms> mapAlarms;
	private int isCheckBoxShown = View.GONE;
	private boolean isCheckBoxReset = false;

	private Map<Integer, Boolean> deleteItems = new HashMap<Integer, Boolean>();

	public HyyDataListAdapter(Context context, Handler handler) {
		// TODO Auto-generated constructor stub
		this.mContext = context;
		this.handler = handler;
		// getData();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_view, parent,false);

			// holder.title = (TextView)
			// convertView.findViewById(R.id.ItemTitle);
			// holder.shortcut = (TextView) convertView
			// .findViewById(R.id.ItemShortcut);
			holder.checkBox = (CheckBox) convertView
					.findViewById(R.id.ItemCheckBox);

			holder.content = (TextView) convertView
					.findViewById(R.id.ItemContent);
			holder.alarmTime = (TextView) convertView
					.findViewById(R.id.ItemAlarmTime);
			holder.alarmDay = (TextView) convertView
					.findViewById(R.id.ItemAlarmDays);

			holder.toggleAlarm = (ToggleButton) convertView
					.findViewById(R.id.ItemToggleAlarm);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// holder.title.setText((String) list.get(position).getTitle());
		// holder.shortcut.setText((String) list.get(position).getShortcut());
		holder.checkBox.setVisibility(isCheckBoxShown);
		if (isCheckBoxReset) {
			holder.checkBox.setChecked(false);
		}

		holder.content.setText((String) list.get(position).getContent());

		// get related alarm
		String status = list.get(position).getAlarmstatus();

		if (status == null || "".equals(status) || "null".equals(status)) {
			holder.alarmTime.setText("No Alarm");
			holder.alarmDay.setText("You may add one");
		} else {
			Alarms alarm = mapAlarms.get(list.get(position).getId().toString());

			String alarmTime = alarm.getAlarmtime();
			String alarmDay = alarm.getDayofweek();

			holder.alarmTime.setText(StringTranslateUtil
					.transRegularTime(alarmTime));
			holder.alarmDay.setText(StringTranslateUtil
					.transRegularWeek(alarmDay));
		}

		if (status == null || "".equals(status) || "null".equals(status)) {
			holder.toggleAlarm.setVisibility(View.INVISIBLE);

		} else if (HyyConstants.ALARM_STATUS_ZERO.equals(status)) {
			holder.toggleAlarm.setVisibility(View.VISIBLE);
			holder.toggleAlarm.setChecked(true);
		} else if (HyyConstants.ALARM_STATUS_ONE.equals(status)) {
			holder.toggleAlarm.setVisibility(View.VISIBLE);
			holder.toggleAlarm.setChecked(false);
		}

		// final boolean isChecked = holder.toggleAlarm.isChecked();
		final int messageIdInt = list.get(position).getId().intValue();
		final String messageId = list.get(position).getId().toString();

		holder.toggleAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switchAlarmStatus(messageId);
			}
		});

		holder.checkBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						deleteItems.put(messageIdInt, isChecked);

					}
				});

		return convertView;
	}

	private void switchAlarmStatus(String messageId) {

		String isPause = DatabaseManager
				.getInstance(HyyDLApplication.getContext())
				.queryAlarmById(messageId).get(0).getIspause();

		if (HyyConstants.ALARM_STATUS_ZERO.equals(isPause)) {
			// pause it
			DatabaseManager.getInstance(HyyDLApplication.getContext())
					.pauseAlarm(messageId);

		} else {
			// resume it
			DatabaseManager.getInstance(HyyDLApplication.getContext())
					.resumeAlarm(messageId);
		}
	}

	public void getData() {

		new Thread() {
			@Override
			public void run() {
				list = DatabaseManager.getInstance(mContext).queryMessage();
				listWhole = new ArrayList<Messages>(list);
				List<Alarms> listAlarms = DatabaseManager.getInstance(mContext)
						.queryAlarmsWithoutCondition();

				mapAlarms = new HashMap<String, Alarms>();
				for (Alarms alarm : listAlarms) {
					mapAlarms.put(alarm.getMessageid(), alarm);
				}

				handler.sendEmptyMessage(HyyConstants.REFRESH_LIST);
			};
		}.start();

	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub

		return new Filter() {

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				// TODO Auto-generated method stub
				FilterResults result = new FilterResults();
				result.values = getFilteredList(constraint.toString());
				return result;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {
				// TODO Auto-generated method stub
				list = (List<Messages>) results.values;
				notifyDataSetChanged();

			}
		};
	}

	protected List<Messages> getFilteredList(String condition) {
		// TODO Auto-generated method stub
		List<Messages> newList = new ArrayList<Messages>();

		for (Messages o : listWhole) {
			if (o.getContent().contains(condition)) {
				newList.add(o);
			}
		}

		return newList;
	}

	public void showCheckBox() {
		this.isCheckBoxShown = View.VISIBLE;
		this.isCheckBoxReset = false;
	}

	public void clearCheckBox() {
		this.isCheckBoxShown = View.GONE;
		this.isCheckBoxReset = true;

	}

	public void deleteSelected() {

		for (Entry<Integer, Boolean> en : deleteItems.entrySet()) {

			if (!en.getValue()) {
				continue;
			}
			
			Log.i("hello", "messageId:"+en.getKey());

			Messages selected = DatabaseManager
					.getInstance(HyyDLApplication.getContext())
					.queryMessageById(en.getKey().toString()).get(0);

			if (selected.getAlarmstatus() != null) {
				Alarms alarm = DatabaseManager
						.getInstance(HyyDLApplication.getContext())
						.queryAlarmById(selected.getId().toString()).get(0);

				DatabaseManager.getInstance(HyyDLApplication.getContext())
						.deleteAlarm(alarm);
			}

			DatabaseManager.getInstance(HyyDLApplication.getContext())
					.deleteMessage(selected);
		}
		
		getData();

	}
}
