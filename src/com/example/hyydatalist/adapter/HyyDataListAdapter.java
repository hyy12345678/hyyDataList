package com.example.hyydatalist.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.hyydatalist.R;
import com.example.hyydatalist.application.HyyDLApplication;
import com.example.hyydatalist.constants.HyyConstants;
import com.example.hyydatalist.database.DatabaseManager;
import com.example.hyydatalist.model.HyyMessage;
import com.example.hyydatalist.viewholder.ViewHolder;

public class HyyDataListAdapter extends BaseAdapter implements Filterable {
	private Context mContext;
	private List<HyyMessage> list;
	private List<HyyMessage> listWhole;
	private Handler handler;

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
					R.layout.list_view, null);

			holder.title = (TextView) convertView.findViewById(R.id.ItemTitle);
			holder.shortcut = (TextView) convertView
					.findViewById(R.id.ItemShortcut);
			holder.toggleAlarm = (ToggleButton) convertView
					.findViewById(R.id.ItemToggleAlarm);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText((String) list.get(position).getTitle());
		holder.shortcut.setText((String) list.get(position).getShortcut());

		String status = list.get(position).getAlarmStatus();
		if (status == null || "".equals(status) || "null".equals(status)) {
			holder.toggleAlarm.setVisibility(View.GONE);
		} else if (HyyConstants.ALARM_STATUS_ZERO.equals(status)) {
			holder.toggleAlarm.setVisibility(View.VISIBLE);
			holder.toggleAlarm.setChecked(true);
		} else if (HyyConstants.ALARM_STATUS_ONE.equals(status)) {
			holder.toggleAlarm.setVisibility(View.VISIBLE);
			holder.toggleAlarm.setChecked(false);
		}

		// final boolean isChecked = holder.toggleAlarm.isChecked();
		final String messageId = (String) list.get(position).getId();

		holder.toggleAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switchAlarmStatus(messageId);
			}
		});

		return convertView;
	}

	private void switchAlarmStatus(String messageId) {

		String isPause = DatabaseManager
				.getInstance(HyyDLApplication.getContext())
				.queryAlarmById(messageId).get(0).getIsPause();

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
				listWhole = new ArrayList<HyyMessage>(list);
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
				list = (List<HyyMessage>) results.values;
				notifyDataSetChanged();

			}
		};
	}

	protected List<HyyMessage> getFilteredList(String condition) {
		// TODO Auto-generated method stub
		List<HyyMessage> newList = new ArrayList<HyyMessage>();

		for (HyyMessage o : listWhole) {
			if (o.getTitle().contains(condition)
					|| o.getShortcut().contains(condition)) {
				newList.add(o);
			}
		}

		return newList;
	}
}
