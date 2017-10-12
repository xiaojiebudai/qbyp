package cn.net.chenbao.qbypseller.dialog;

import org.xutils.common.util.DensityUtil;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;

public class EditCategoryDialog extends Dialog {

	private View contenView;

	public EditCategoryDialog(Context context) {
		super(context, R.style.DialogStyle);
		contenView = getLayoutInflater().inflate(R.layout.dialog_edit_category,
				null);
		setContentView(contenView);
		LayoutParams params = getWindow().getAttributes();
		params.width = (int) (DensityUtil.getScreenWidth() * 0.7F);
		contenView.findViewById(R.id.tv_left).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
		DialogHolder holder = new DialogHolder();
		holder.content = (EditText) findViewById(R.id.edt_content);
		holder.title = (TextView) findViewById(R.id.tv_title);
		holder.right = findViewById(R.id.tv_right);
		contenView.setTag(holder);
	}

	public DialogHolder getTag() {
		return (DialogHolder) contenView.getTag();
	}

	public static class DialogHolder {
		public View right;
		public EditText content;
		public TextView title;
	}

}
