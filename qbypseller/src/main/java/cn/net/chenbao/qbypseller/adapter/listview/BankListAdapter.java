package cn.net.chenbao.qbypseller.adapter.listview;

import cn.net.chenbao.qbypseller.adapter.FatherAdapter;
import cn.net.chenbao.qbypseller.bean.Bank;
import cn.net.chenbao.qbypseller.utils.ImageUtils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;

/***
 * Description:银行卡选择列表 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-8-4
 */
public class BankListAdapter extends FatherAdapter<Bank> {
    public int model = -1;
    public static final int WITHDRAW = 0;

    public BankListAdapter(Context ctx) {
        super(ctx);
        // TODO Auto-generated constructor stub
    }

    public BankListAdapter(Context ctx, int model) {
        super(ctx);
        this.model = model;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_text_one, null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.setData(position);
        return convertView;
    }

    public class ViewHolder {
        public TextView tv_name;
        public TextView tv_no;
        public ImageView iv_img;

        public ViewHolder(View convertView) {
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_no = (TextView) convertView.findViewById(R.id.tv_no);
            iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            convertView.setTag(this);
        }

        public void setData(int Position) {
            Bank item = getItem(Position);
            if (model == BankListAdapter.WITHDRAW) {
                ImageUtils.setCommonImage(mContext, item.BankIco, iv_img);
                tv_no.setVisibility(View.GONE);
                tv_name.setText(item.BankName + "(尾号"
                        + (item.AccountNo.length() < 6 ? item.AccountNo : item.AccountNo.substring(item.AccountNo.length() - 5))
                        + ((item.CardType == 0) ? "公司账户" : "法人个人账户") + ")");
            } else {
                ImageUtils.setCommonImage(mContext, item.BankIco, iv_img);
                tv_name.setText(item.BankName);
                tv_no.setText(item.BankNo);
            }

        }

    }

}
