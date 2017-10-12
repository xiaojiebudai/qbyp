package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

public class HotHistoryAdapter extends FatherAdapter<String> {

	public HotHistoryAdapter(Context ctx) {
		super(ctx);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tv = new TextView(mContext);
		tv.setPadding(10, 10, 10, 10);
		tv.setTextColor(mContext.getResources().getColor(R.color.text_f7));
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		tv.setSingleLine();
		tv.setEllipsize(TextUtils.TruncateAt.END);
		tv.setText(getItem(position));
		return tv;
	}

}
