package cn.net.chenbao.qbyp.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.net.chenbao.qbyp.R;

/***
 * Description:性别选择 Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-7-30
 */
public class SexSelectDialog extends Dialog implements
		android.view.View.OnClickListener {
	private ImageView img_woman, img_man;
	private LinearLayout ll_woman, ll_man;
	private Context context;
	private boolean isMan = true;

	public SexSelectDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		setContentView(R.layout.dialog_select_sex);
		LayoutParams attributes = getWindow().getAttributes();
		attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.6);
		getWindow().setAttributes(attributes);
		initView();
	}

	private void initView() {
		img_woman = (ImageView) findViewById(R.id.img_woman);
		img_man = (ImageView) findViewById(R.id.img_man);
		ll_woman = (LinearLayout) findViewById(R.id.ll_woman);
		ll_man = (LinearLayout) findViewById(R.id.ll_man);
		ll_woman.setOnClickListener(this);
		ll_man.setOnClickListener(this);
	}

	public void setSex(boolean isMan) {
		this.isMan = isMan;
		setImg(isMan);
	}

	private void setImg(boolean isMan2) {
		if (isMan) {
			img_woman.setImageResource(R.drawable.shangpinzhuangtai_nor);
			img_man.setImageResource(R.drawable.shangpinzhuangtai_select);
		} else {
			img_man.setImageResource(R.drawable.shangpinzhuangtai_nor);
			img_woman.setImageResource(R.drawable.shangpinzhuangtai_select);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_woman:
			isMan = false;
			setImg(false);
			dismiss();
			break;
		case R.id.ll_man:
			isMan = true;
			setImg(true);
			dismiss();
			break;

		default:
			break;
		}

	}

	public String getResult() {
		return isMan?"男":"女";
	}

}
