package cn.net.chenbao.qbypseller.dialog;

import cn.net.chenbao.qbypseller.activity.ImageSelectActivity;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;

public class ChoosePhotoDialog extends Dialog implements
		android.view.View.OnClickListener {

	private int requestCode;

	private boolean doCrop;
	private boolean isSquare;

	private Context context;

	public ChoosePhotoDialog(Context context, boolean doCrop,  boolean isSquare, int requestCode) {
		super(context, R.style.DialogStyle);
		this.context = context;
		setContentView(R.layout.pop_choose_photo);
		LayoutParams params = getWindow().getAttributes();
		params.width = context.getResources().getDisplayMetrics().widthPixels;
		params.gravity = Gravity.BOTTOM;
		this.doCrop = doCrop;
		this.isSquare = isSquare;
		this.requestCode = requestCode;
		TextView cancel = (TextView) findViewById(R.id.cancel);
		TextView tv_choose = (TextView) findViewById(R.id.tv_choose);
		TextView tv_takephoto = (TextView) findViewById(R.id.tv_takephoto);
		cancel.setOnClickListener(this);
		tv_choose.setOnClickListener(this);
		tv_takephoto.setOnClickListener(this);
	}

	private Fragment fragment;

	public ChoosePhotoDialog(Fragment fragment, boolean doCrop,  boolean isSquare, int requestCode) {
		this(fragment.getActivity(), doCrop,isSquare, requestCode);
		this.fragment = fragment;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancel:
			dismiss();
			break;
		case R.id.tv_takephoto:
			if (fragment != null) {
				PublicWay.startImageSelectActivity(fragment, requestCode, true,isSquare,
						ImageSelectActivity.MODE_TAKE_PICTURE);
			} else {
				PublicWay.startImageSelectActivity((Activity) context,
						requestCode, true,isSquare,
						ImageSelectActivity.MODE_TAKE_PICTURE);
			}

			dismiss();
			break;
		case R.id.tv_choose:
			if (fragment != null) {
				PublicWay.startImageSelectActivity(fragment, requestCode, true,isSquare,
						ImageSelectActivity.MODE_PHOTO_ALBUM);
			} else {
				PublicWay
						.startImageSelectActivity((Activity) context,
								requestCode, true,isSquare,
								ImageSelectActivity.MODE_PHOTO_ALBUM);
			}
			dismiss();
			break;
		}
	}
}
