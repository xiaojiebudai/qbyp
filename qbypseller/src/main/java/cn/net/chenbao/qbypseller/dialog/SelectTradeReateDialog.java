package cn.net.chenbao.qbypseller.dialog;

import cn.net.chenbao.qbypseller.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

/***
 * Description:修改扣点dialog Company: Zhubaoyi Version：2.0
 * 
 * @title CertificateInputDialog.java
 * @author ZXJ
 * @date 2016-7-29
 ***/
public class SelectTradeReateDialog extends Dialog implements
		android.view.View.OnClickListener {
	private ImageView img_1, img_2;
	private LinearLayout ll_1, ll_2;
	private Context context;
	private boolean isten = true;

	public SelectTradeReateDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		setContentView(R.layout.dialog_select_trrade);
		LayoutParams attributes = getWindow().getAttributes();
		attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.6);
		getWindow().setAttributes(attributes);
		initView();
	}

	private void initView() {
		img_1 = (ImageView) findViewById(R.id.img_1);
		img_2 = (ImageView) findViewById(R.id.img_2);
		ll_1 = (LinearLayout) findViewById(R.id.ll_1);
		ll_2 = (LinearLayout) findViewById(R.id.ll_2);
		ll_1.setOnClickListener(this);
		ll_2.setOnClickListener(this);
	}

	public void setReate(boolean isten) {
		this.isten = isten;
		setImg(isten);
	}

	private void setImg(boolean isMan2) {
		if (isten) {
			img_2.setImageResource(R.drawable.shangpinzhuangtai_nor);
			img_1.setImageResource(R.drawable.shangpinzhuangtai_select);
		} else {
			img_1.setImageResource(R.drawable.shangpinzhuangtai_nor);
			img_2.setImageResource(R.drawable.shangpinzhuangtai_select);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_1:
			isten = true;
			setImg(true);
			dismiss();
			break;
		case R.id.ll_2:
			isten = false;
			setImg(false);
			dismiss();
			break;

		default:
			break;
		}

	}

	public double getResult() {
		return isten ? 0.1 : 0.2;
	}

}
