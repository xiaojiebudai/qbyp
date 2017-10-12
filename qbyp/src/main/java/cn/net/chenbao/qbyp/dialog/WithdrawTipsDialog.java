package cn.net.chenbao.qbyp.dialog;

import cn.net.chenbao.qbyp.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;

/***
 * Description:体现提示dialog Company: Zhubaoyi Version：2.0
 * 
 * @title WithdrawTipsDialog.java
 * @author ZXJ
 * @date 2016-7-29
 ***/
public class WithdrawTipsDialog extends Dialog {
	private ImageView iv_close;

	public WithdrawTipsDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.dialog_withdraw_tips);
		LayoutParams attributes = getWindow().getAttributes();
		attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
		getWindow().setAttributes(attributes);
		initView();
	}

	private void initView() {
		iv_close = (ImageView) findViewById(R.id.iv_close);
		iv_close.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

}
