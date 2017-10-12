package cn.net.chenbao.qbyp.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.bean.Bank;
import cn.net.chenbao.qbyp.utils.ImageUtils;

/***
 * Description:银行卡选择列表 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-8-4
 */
public class BankListAdapter extends FatherAdapter<Bank> {
    public int model = -1;
    public static final int WITHDRAW = 0;
    private int selectPostion = 0;

    public BankListAdapter(Context ctx) {
        super(ctx);
        // TODO Auto-generated constructor stub
    }

    public BankListAdapter(Context ctx, int model) {
        super(ctx);
        this.model = model;
    }

    public void setSelectPostion(int position) {
        this.selectPostion = position;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.list_text_one, null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();
        if (model == WITHDRAW) {
            if (position == selectPostion) {
                convertView.setBackgroundColor(mContext.getResources()
                        .getColor(R.color.transparent));
            } else {
                convertView.setBackgroundColor(mContext.getResources()
                        .getColor(R.color.white));
            }
        }
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
                tv_name.setText(String.format(mContext.getString(R.string.bank_card_last_number_card_type),
                        item.BankName,
                        item.AccountNo.length() < 6 ? item.AccountNo : item.AccountNo.substring(item.AccountNo.length() - 5),
                        (item.CardType == 0) ? mContext.getString(R.string.deposit_card) : mContext.getString(R.string.credit_card)));
            } else {
                ImageUtils.setCommonImage(mContext, item.BankIco, iv_img);
                tv_name.setText(item.BankName);
                tv_no.setText(item.BankNo);
            }
        }

    }

}
