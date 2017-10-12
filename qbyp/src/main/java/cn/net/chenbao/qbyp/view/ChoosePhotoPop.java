package cn.net.chenbao.qbyp.view;

import cn.net.chenbao.qbyp.activity.ImageSelectActivity;
import cn.net.chenbao.qbyp.utils.PublicWay;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

/***
 * Description:选择照片pop Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-7-29
 */
public class ChoosePhotoPop implements OnClickListener {
	private PopupWindow mPopupWindow;
	private View mParent;
	private Context context;
	private View content;
	private int requestcode;

	public ChoosePhotoPop(final Context context, int parentId, int requestcode) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.requestcode = requestcode;
		LayoutInflater inflater = LayoutInflater.from(context);
		mParent = inflater.inflate(parentId, null);
		content = inflater.inflate(R.layout.pop_choose_photo, null);
		TextView tv_takephoto = (TextView) content
				.findViewById(R.id.tv_takephoto);
		TextView cancel = (TextView) content.findViewById(R.id.cancel);
		TextView tv_choose = (TextView) content.findViewById(R.id.tv_choose);
		cancel.setOnClickListener(this);
		tv_choose.setOnClickListener(this);
		tv_takephoto.setOnClickListener(this);
		mPopupWindow = new PopupWindow(content, (int) (context.getResources()
				.getDisplayMetrics().widthPixels * 0.8),
				ViewGroup.LayoutParams.WRAP_CONTENT, true);

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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		dismissWindow();
		switch (v.getId()) {
		case R.id.cancel:
			dismissWindow();
			break;
		case R.id.tv_takephoto:
			PublicWay.startImageSelectActivity((Activity) context, requestcode,
					true,true,false, ImageSelectActivity.MODE_TAKE_PICTURE);
			dismissWindow();
			break;
		case R.id.tv_choose:
			PublicWay.startImageSelectActivity((Activity) context, requestcode,
					true,true, false,ImageSelectActivity.MODE_PHOTO_ALBUM);
			dismissWindow();
			break;
		}
	}
}
