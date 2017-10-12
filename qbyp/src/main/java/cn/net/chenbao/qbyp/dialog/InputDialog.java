package cn.net.chenbao.qbyp.dialog;

import cn.net.chenbao.qbyp.R;
import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

/***
 * Description:输入邀请人dialog Company: Zhubaoyi Version：2.0
 * 
 * @title CertificateInputDialog.java
 * @author ZXJ
 * @date 2016-7-29
 ***/
public class InputDialog extends Dialog {
	private EditText ed_comment;
	private TextView tv_cancel, tv_ok;

	public InputDialog(Context context, int theme) {
		super(context, theme);
		setContentView(R.layout.dialog_input);
		LayoutParams attributes = getWindow().getAttributes();
		attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
		getWindow().setAttributes(attributes);
		initView();
	}

	private void initView() {
		ed_comment = (EditText) findViewById(R.id.ed_comment);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		tv_ok = (TextView) findViewById(R.id.tv_ok);

	}

	public TextView getTv_cancel() {
		return tv_cancel;
	}

	public TextView getTv_ok() {
		return tv_ok;
	}

	public String getInput() {
		return ed_comment.getText().toString().trim();
	}

}
