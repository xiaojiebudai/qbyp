package cn.net.chenbao.qbypseller.utils;

import cn.net.chenbao.qbypseller.dialog.CommonDialog;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;

/**
 * 弹窗工具类
 * 
 * @author xl
 * @date:2016-8-2下午4:49:53
 * @description
 */
public class DialogUtils {

	/** 等待弹窗 */
	public static Dialog getWaitDialog(Activity context, boolean cancelable) {

		final Dialog dialog = new Dialog(context, R.style.DialogStyle2);

		dialog.setContentView(R.layout.custom_progress_dialog);
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
					return true;
				}
				return false;
			}
		});
		dialog.setCancelable(cancelable);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();

		int screenW = context.getResources().getDisplayMetrics().widthPixels;
		lp.width = (int) (0.6 * screenW);
		TextView titleTxtv = (TextView) dialog.findViewById(R.id.dialogText);
		titleTxtv.setText(R.string.data_downloading);
		return dialog;
	}

	public static int getScreenWidth(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static int getScreenHeight(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 通用类型dialog
	 * 
	 * @author xl
	 * @date:2016-4-12下午6:01:29
	 * @description
	 * @param context
	 * @return
	 */
	public static CommonDialog getCommonDialog(Activity context, String content) {
		CommonDialog dialog = new CommonDialog(context, R.style.DialogStyle);
		dialog.setContentView(R.layout.dialog_style_common);
		dialog.getWindow().getAttributes().width = (int) (getScreenWidth(context) * 0.7);
		final TextView info = (TextView) dialog.findViewById(R.id.tv_content);
		info.setText(content);
		//view加载完成时回调
		info.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (info.getLineCount() > 1) {
					info.setGravity(Gravity.LEFT);
				}
			}
		});
		return dialog;
	}

	public static CommonDialog getCommonDialog(Activity context, int contentRes) {
		CommonDialog dialog = new CommonDialog(context, R.style.DialogStyle);
		dialog.setContentView(R.layout.dialog_style_common);
		dialog.getWindow().getAttributes().width = (int) (getScreenWidth(context) * 0.7);
		final TextView info = (TextView) dialog.findViewById(R.id.tv_content);
		info.setText(contentRes);
		//view加载完成时回调
		info.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				if (info.getLineCount() > 1) {
					info.setGravity(Gravity.LEFT);
				}
			}
		});
		return dialog;
	}

	/**
	 * 通用类型的对话框
	 * 
	 * @author xl
	 * @date:2016-5-13上午9:43:21
	 * @description 二次确认,取消/确定按钮
	 * @param act
	 * @param content
	 * @return
	 */
	public static CommonDialog getCommonDialogTwiceConfirm(Activity act,
			String content, boolean cancelable) {
		final CommonDialog dialog = getCommonDialog(act, content);
		dialog.getButtonLeft().setText(R.string.cancel);
		dialog.getButtonLeft().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.getButtonRight().setText(R.string.ok);
		dialog.setCancelable(true);
		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
					return true;
				}
				return false;
			}
		});
		dialog.setCancelable(cancelable);
		return dialog;
	}

	public static CommonDialog getCommonDialogTwiceConfirm(Activity act,
			int content, boolean cancelable) {
		return getCommonDialogTwiceConfirm(act, act.getString(content),
				cancelable);
	}
}
