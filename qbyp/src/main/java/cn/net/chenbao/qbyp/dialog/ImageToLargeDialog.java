package cn.net.chenbao.qbyp.dialog;

import org.xutils.common.util.DensityUtil;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class ImageToLargeDialog extends Dialog implements
		android.view.View.OnClickListener {

	private Context context;
	private ImageView image;
	private String url;
	private ImageView back;

	public ImageToLargeDialog(Context context, String url) {
		super(context, R.style.DialogStyle);
		this.context = context;
		this.url = url;
		setContentView(R.layout.dialog_img);
		getWindow().getAttributes().width = (int) (DensityUtil.getScreenWidth() * 0.9F);
		initView();
	}

	private void initView() {
		image = (ImageView) findViewById(R.id.iv_image);
		back = (ImageView) findViewById(R.id.iv_back);
		LayoutParams layoutParams = image.getLayoutParams();
		layoutParams.width = (int) (DensityUtil.getScreenWidth() * 0.9F);
		layoutParams.height = (int) (DensityUtil.getScreenWidth() * 0.9F);
		image.setLayoutParams(layoutParams);
		setDataToUi();
		back.setOnClickListener(this);
	}

	private void setDataToUi() {
		ImageUtils.setCommonImage(context, url, image);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			dismiss();
			break;

		default:
			break;
		}
	}

}
