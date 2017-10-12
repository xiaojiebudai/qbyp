package cn.net.chenbao.qbypseller.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;


/**
 * 开放dialog的修改方法
 * 
 * @author xl
 * @date:2016-4-12下午6:46:31
 */
public class CommonDialog extends Dialog {

	private TextView tv_title_cutline;
	private TextView mButton_left;
	private TextView mButton_right;
	private TextView mContent;
	private ImageView mImage;
	private TextView mTitle;
	private Context context;

	public CommonDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public TextView getmContent() {
		return mContent;
	}

	public void setmContent(TextView mContent) {
		this.mContent = mContent;
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		mButton_left = (TextView) findViewById(R.id.tv_left);
		tv_title_cutline = (TextView) findViewById(R.id.tv_title_cutline);
		mButton_right = (TextView) findViewById(R.id.tv_right);
		mContent = (TextView) findViewById(R.id.tv_content);
		mTitle = (TextView) findViewById(R.id.tv_title);
		mImage = (ImageView) findViewById(R.id.iv_image);
	}

	public TextView getButtonLeft() {
		return mButton_left;
	}

	public TextView getButtonLeft(String text) {
		mButton_left.setText(text);
		return mButton_left;
	}

	public TextView getButtonLeft(int textRes) {
		mButton_left.setText(textRes);
		return mButton_left;
	}

	public TextView getButtonRight() {
		return mButton_right;
	}

	public TextView getButtonRight(String text) {
		mButton_right.setText(text);
		return mButton_right;
	}

	public TextView getButtonRight(int textRes) {
		mButton_right.setText(textRes);
		return mButton_right;
	}

	public TextView getContent() {
		return mContent;
	}

	public ImageView getImage() {
		return mImage;
	}

	public TextView getTitle() {
		return mTitle;
	}

	public void setContentText(int id) {
		mContent.setVisibility(View.VISIBLE);
		mContent.setText(context.getResources().getString(id));
	}

	public void setTitleImage(int resId) {
		mImage.setVisibility(View.VISIBLE);
		mImage.setBackgroundResource(resId);
	}

	public void setTitleText(String txt) {
		tv_title_cutline.setVisibility(View.VISIBLE);
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(txt);
	}

	public void setTitleText(int id) {
		tv_title_cutline.setVisibility(View.VISIBLE);
		mTitle.setVisibility(View.VISIBLE);
		mTitle.setText(context.getResources().getString(id));
	}

	public void setLeftButtonText(String txt) {
		mButton_left.setText(txt);
	}

	public void setLeftButtonText(int txtRes) {
		mButton_left.setText(txtRes);
	}

	public void setRightButtonText(String txt) {
		mButton_right.setText(txt);
	}

	public void setRightButtonText(int txtRes) {
		mButton_right.setText(txtRes);
	}

	public void setRightButtonCilck(android.view.View.OnClickListener listener) {
		mButton_right.setOnClickListener(listener);
	}

	public void setLeftButtonOnClick(android.view.View.OnClickListener listener) {
		mButton_left.setOnClickListener(listener);
	}

	public void setTextColor(int index, int colorID) {
		if (index == 0) {
			mButton_left.setTextColor(context.getResources().getColor(colorID));
		} else {
			mButton_right
					.setTextColor(context.getResources().getColor(colorID));
		}
	}

	public void setTitleColor(int colorID) {
		mTitle.setTextColor(context.getResources().getColor(colorID));
	}

	public void setButtonGone(int index) {
		if (index == 0) {
			mButton_left.setVisibility(View.GONE);
		} else {
			mButton_right.setVisibility(View.GONE);
		}
	}

	/**
	 * 自動消失
	 */
	public void setDialogGone() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				dismiss();
			}
		}, 2000);
	}


}
