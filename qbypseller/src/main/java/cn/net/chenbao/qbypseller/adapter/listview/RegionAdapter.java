package cn.net.chenbao.qbypseller.adapter.listview;

import cn.net.chenbao.qbypseller.adapter.FatherAdapter;
import cn.net.chenbao.qbypseller.bean.Region;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;

/**
 * 地区适配器
 * 
 * @author licheng
 *
 */
public class RegionAdapter extends FatherAdapter<Region> {

	public RegionAdapter(Context ctx) {
		super(ctx);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView inflate = (TextView) View.inflate(mContext,
				R.layout.textview_comm, null);
		inflate.setText(getItem(position).Name);
		return inflate;
	}

}
