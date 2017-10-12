package cn.net.chenbao.qbypseller.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;


import cn.net.chenbao.qbypseller.R;

import java.util.ArrayList;

/***
 * Description:选择时间pop Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-7-29
 */
public class SelectTimePop {
	private ArrayList<String> yearList = new ArrayList<>();
	private ArrayList<String> monthList = new ArrayList<>();
	private String selectYear;
	private String selectMonth;
	private EasyPickerView wvYear;
	private EasyPickerView wvMonth;

	private PopupWindow mPopupWindow;
	private OnSelectOKLisentner mLisentner;
	private View mParent;
	private Context context;
	private View view;

	public SelectTimePop(final Context context, int parentId,
						 OnSelectOKLisentner lisentner) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mLisentner = lisentner;

		LayoutInflater inflater = LayoutInflater.from(context);
		mParent = inflater.inflate(parentId, null);
		view = inflater.inflate(R.layout.pop_select_time, null);

		mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		// 设置SelectPicPopupWindow弹出窗体可点击
		mPopupWindow.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
		mPopupWindow.setAnimationStyle(R.style.AnimBottom);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				LayoutParams params = ((Activity) context).getWindow()
						.getAttributes();
				params.alpha = 1.0F;
				((Activity) context).getWindow().setAttributes(params);
			}
		});

		view.findViewById(R.id.tv_ok).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mLisentner.selectOk(Integer.parseInt(selectYear),
						Integer.parseInt(selectMonth));
				mPopupWindow.dismiss();
			}
		});
		view.findViewById(R.id.tv_cancel).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						mPopupWindow.dismiss();
					}
				});
		view.findViewById(R.id.tv_all).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						mLisentner.selectAll();
						mPopupWindow.dismiss();
					}
				});
		// 先把时间给上
		setDate();
		wvYear = (EasyPickerView) view.findViewById(R.id.epv_year);
		wvMonth = (EasyPickerView) view.findViewById(R.id.epv_month);

		wvYear.setDataList(yearList);
		wvYear.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
			@Override
			public void onScrollChanged(int curIndex) {
				selectYear = yearList.get(curIndex);

			}

			@Override
			public void onScrollFinished(int curIndex) {
				selectYear = yearList.get(curIndex);

			}
		});
		wvMonth.setDataList(monthList);
		wvMonth.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
			@Override
			public void onScrollChanged(int curIndex) {
				selectMonth = monthList.get(curIndex);
			}

			@Override
			public void onScrollFinished(int curIndex) {
				selectMonth = monthList.get(curIndex);

			}
		});
		view.post(new Runnable() {
			@Override
			public void run() {
				updateMonths();
			}
		});
//		new Handler().postDelayed(new Runnable(){
//			public void run() {
//				updateMonths();
//			}
//		}, 200);

	}

	public void showChooseWindow() {
		if (mPopupWindow != null) {
			mPopupWindow.showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
			LayoutParams params = ((Activity) context).getWindow()
					.getAttributes();
			params.alpha = 0.6F;
			((Activity) context).getWindow().setAttributes(params);
		}
	}

	public Boolean isShowing() {
		return mPopupWindow.isShowing();
	}

	public void dismissWindow() {
		if (mPopupWindow != null && mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

	private void setDate() {
		Time time1 = new Time("GMT+8");
		time1.setToNow();
		for (int i = time1.year - 10; i < time1.year + 11; i++) {
			yearList.add(i + "");
		}
		for (int i = 1; i < 13; i++) {
			monthList.add(i + "");
		}
	}
	private void updateMonths() {
		Time time1 = new Time("GMT+8");
		time1.setToNow();
		wvYear.moveTo(10);
		wvMonth.moveTo(time1.month);

	}

	public interface OnSelectOKLisentner {
		void selectOk(int years, int months);

		void selectAll();
	}
}
