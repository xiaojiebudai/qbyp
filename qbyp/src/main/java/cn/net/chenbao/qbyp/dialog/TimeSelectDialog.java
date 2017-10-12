package cn.net.chenbao.qbyp.dialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import cn.net.chenbao.qbyp.adapter.listview.SelectTimeAdapter;
import cn.net.chenbao.qbyp.bean.GoTime;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.ZLog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import cn.net.chenbao.qbyp.R;

public class TimeSelectDialog extends Dialog implements
		android.view.View.OnClickListener {

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	private ListView mListView;
	private long comeTime;
	private Context context;
	private long fasterTime;
	private long endTime;
	private boolean isOpen;

	private long startTime;
	private int count;
	private List<GoTime> list = new ArrayList<GoTime>();
	long currentTime;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	private SelectTimeAdapter adapter;
	private TimeCallBack timeListener;
	private long oneDaySum;

	public TimeCallBack getTimeListener() {
		return timeListener;
	}

	public void setTimeListener(TimeCallBack timeListener) {
		this.timeListener = timeListener;
	}

	public TimeSelectDialog(Context context, long endTime, long startTime) {
		super(context, R.style.TimeSelectDialogStyle);
		setContentView(R.layout.dialog_time_select);
		getWindow().getAttributes().width = context.getResources()
				.getDisplayMetrics().widthPixels / 10 * 9;
		this.context = context;
		this.endTime = endTime;
		this.startTime = startTime;
		oneDaySum = getSelectTimeInMillis(24 + "", 0 + "");
		Calendar instance = Calendar.getInstance();
		instance.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		int hour = instance.get(Calendar.HOUR_OF_DAY);
		int min = instance.get(Calendar.MINUTE);
		ZLog.showPost(hour + "---" + min);
		currentTime = getSelectTimeInMillis(hour + "", min + "");// 1970年的值
		comeTime = currentTime + 2 * 60 * 60 * 1000 + 10 * 60 * 1000;
		fasterTime = comeTime;
		isOpen = !(startTime > currentTime / 1000 || endTime < currentTime / 1000);
		initView();
		initData();
	}

	private void initData() {
		endTime = endTime * 1000;// 用于计算
		if (comeTime > oneDaySum) {
			comeTime = comeTime - oneDaySum;// 第二天开始重新计算
		}
		count = (int) ((endTime - comeTime) / 1000 / 60 / 60);
		ZLog.showPost("oneDaySum:" + oneDaySum + ",startTime:" + startTime
				+ ",endTime:" + endTime + ",comeTime" + comeTime
				+ ",currentTime:" + currentTime + "关门时间:"
				+ TimeUtil.getHourAndMin(endTime) + ",开门时间:"
				+ TimeUtil.getHourAndMin(startTime * 1000) + ",当前时间:"
				+ TimeUtil.getHourAndMin(currentTime) + ",差值" + count
				+ ",起始送达时间:" + TimeUtil.getHourAndMin(comeTime));
		if (count < 0) {
			isOpen = false;
		}
//
//		if (!isOpen) {
//			return;
//		}
		GoTime goTime = new GoTime();
		goTime.time = context.getString(R.string.come_soon) + "("
				+ context.getString(R.string.yuji)
				+ TimeUtil.getHourAndMin(comeTime)
				+ context.getString(R.string.songda) + ")";
		goTime.isSelect=true;
		list.add(goTime);
		if (count != 0) {
			for (int i = 0; i < (count>24?24:count + 1); i++) {
				long secondTime = 0;
				GoTime goTime2 = new GoTime();
				if (comeTime < endTime) {
					secondTime = comeTime;
					goTime2.time = TimeUtil.getHourAndMin(comeTime)
							+ "-"
							+ TimeUtil
									.getHourAndMin(secondTime + 1000 * 60 * 60);
					comeTime += 1000 * 60 * 60;
					if (comeTime > endTime) {// 再判断一下如果时间超过结束时间,最后已结束时间为准
						goTime2.time = TimeUtil
								.getHourAndMin(comeTime - 1000 * 60 * 60)
								+ "-"
								+ TimeUtil.getHourAndMin(endTime);
					}
					list.add(goTime2);
				}
			}
		}
		adapter = new SelectTimeAdapter(context);
		adapter.setDatas(list);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				List<GoTime> datas = adapter.getDatas();
				for (int i = 0; i < datas.size(); i++) {
					datas.get(i).isSelect = i == position;
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.lv);
		findViewById(R.id.tv_ok).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_ok:
			List<GoTime> datas = adapter.getDatas();
			for (GoTime goTime : datas) {
				if (goTime.isSelect) {
					timeListener.timeListener(goTime.time);
				}
			}

			for (int i = 0; i < datas.size(); i++) {
				if (datas.get(i).isSelect) {
						timeListener.timeListener(datas.get(i).time);
				}
			}
			dismiss();
			break;

		default:
			break;
		}
	}

	public interface TimeCallBack {
		void timeListener(String time);
	}

	/**
	 * 1970.01.01.H.M.0
	 *
	 * @author xl
	 * @date:2016-8-24下午5:41:36
	 * @description
	 * @param first
	 * @param second
	 * @return
	 */
	private long getSelectTimeInMillis(String first, String second) {
		Calendar instance = Calendar.getInstance();
		instance.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		instance.set(Calendar.HOUR_OF_DAY, (Integer.parseInt(first)));
		instance.set(Calendar.MINUTE, (Integer.parseInt(second)));
		instance.set(Calendar.YEAR, 1970);
		instance.set(Calendar.MONTH, Calendar.JANUARY);
		instance.set(Calendar.DATE, 1);
		instance.set(Calendar.SECOND, 0);
		instance.set(Calendar.MILLISECOND, 0);
		return instance.getTimeInMillis();
	}

}
