package cn.net.chenbao.qbypseller.adapter.listview;

import cn.net.chenbao.qbypseller.adapter.FatherAdapter;
import cn.net.chenbao.qbypseller.bean.TradesCategory;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;

/**
 * 行业类目
 * 
 * @author lc
 * @date:2016-8-5下午3:40:33
 * @description xl补描述
 */
public class TradesCategoryAdapter extends FatherAdapter<TradesCategory> {

	public TradesCategoryAdapter(Context ctx) {
		super(ctx);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView inflate = (TextView) View.inflate(mContext,
				R.layout.textview_comm, null);
		inflate.setText(getItem(position).TradeName);
		return inflate;
	}

}
