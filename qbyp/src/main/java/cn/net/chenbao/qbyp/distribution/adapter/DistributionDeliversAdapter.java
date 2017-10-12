package cn.net.chenbao.qbyp.distribution.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.distribution.been.ShopDeliver;

/**
 * Created by zxj on 2017/4/19.
 *
 * @description
 */

public class DistributionDeliversAdapter  extends FatherAdapter<ShopDeliver>{

    public DistributionDeliversAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView inflate = (TextView) View.inflate(mContext,
                R.layout.textview_comm, null);
        inflate.setText(getItem(position).DeliverName);
        return inflate;
    }

}
