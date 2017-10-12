package cn.net.chenbao.qbyp.distribution.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.distribution.been.DistributionProduct;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

/**
 * Created by wuri on 2017/6/16.
 */

public class DistributionShopCartAdapter extends FatherAdapter<DistributionProduct> {
    CheckedInterface mCheckInterface;
    ModifyCountInterface mModifyCountInterface;

    public void setCheckInterface(CheckedInterface checkInterface) {
        mCheckInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        mModifyCountInterface = modifyCountInterface;
    }

    public DistributionShopCartAdapter(Context ctx) {
        super(ctx);
    }

    private boolean delete;

    public void setIsdelete(boolean delete) {
        this.delete = delete;
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext,
                    R.layout.lv_distribution_shopcart_item, null);
            viewHolder = new ViewHolder(convertView);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        setClickEvent(viewHolder, position);

        viewHolder.setData(getItem(position));
        return convertView;
    }

    private void setClickEvent(final ViewHolder viewHolder, final int position) {
        viewHolder.ll_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!viewHolder.cb_itemcheckbox.isEnabled()) {
                    return;
                }
                viewHolder.cb_itemcheckbox.setChecked(!viewHolder.cb_itemcheckbox.isChecked());
                getItem(position).setChecked(viewHolder.cb_itemcheckbox.isChecked());
                if (mCheckInterface != null) {
                    mCheckInterface.onChecked(delete,position);//计算 notify
                }
            }
        });
        viewHolder.iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mModifyCountInterface != null) {
                    mModifyCountInterface.doIncrease(position);
                }
            }
        });
        viewHolder.iv_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mModifyCountInterface != null) {
                    mModifyCountInterface.doDecrease(position);
                }
            }
        });
        viewHolder.tv_good_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mModifyCountInterface != null && !TimeUtil.isFastClick()) {
                    mModifyCountInterface.doMultiModify(position);
                }
            }
        });
        viewHolder.ll_good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DistributionProduct product = getItem(position);
                PublicWay.startDistributionGoodsDetailActivity((Activity) mContext, product.ProductId);
            }
        });
    }

    public class ViewHolder {
        View ll_check;
        View ll_good;
        CheckBox cb_itemcheckbox;
        ImageView iv_good_img;
        TextView tv_good_name;
        TextView tv_price;
        TextView tv_good_num;
        ImageView iv_add;
        ImageView iv_sub;
        ImageView iv_offline;
        View ll_num_add_sub;

        public ViewHolder(View convertView) {
            ll_check = convertView.findViewById(R.id.ll_check);
            cb_itemcheckbox = (CheckBox) convertView.findViewById(R.id.cb_itemcheckbox);
            ll_good = convertView.findViewById(R.id.ll_good);
            iv_good_img = (ImageView) convertView.findViewById(R.id.iv_good_img);
            tv_good_name = (TextView) convertView.findViewById(R.id.tv_good_name);
            tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            tv_good_num = (TextView) convertView.findViewById(R.id.tv_good_num);
            iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
            iv_sub = (ImageView) convertView.findViewById(R.id.iv_sub);
            iv_offline = (ImageView) convertView.findViewById(R.id.iv_offline);
            ll_num_add_sub = convertView.findViewById(R.id.ll_num_add_sub);
            convertView.setTag(this);
        }

        public void setData(final DistributionProduct product) {
            ImageUtils.setCommonImage(mContext, product.ImageUrl, iv_good_img);

            if (product.Status == 0) {
                tv_good_name.setTextColor(mContext.getResources().getColor(R.color.text_gray_s1));
                tv_price.setTextColor(mContext.getResources().getColor(R.color.text_gray_s1));
                iv_offline.setVisibility(View.VISIBLE);
                ll_num_add_sub.setVisibility(View.INVISIBLE);
            } else {
                tv_good_name.setTextColor(mContext.getResources().getColor(R.color.text_f7));
                tv_price.setTextColor(mContext.getResources().getColor(R.color.yellow_ww));
                iv_offline.setVisibility(View.GONE);
                ll_num_add_sub.setVisibility(View.VISIBLE);
            }

            if (product.Status == 0 && !delete) {//下架商品
                cb_itemcheckbox.setEnabled(false);
            } else {
                cb_itemcheckbox.setEnabled(true);
                cb_itemcheckbox.setChecked(product.isChecked());
            }

            tv_good_name.setText(product.ProductName);
            tv_price.setText(WWViewUtil.numberFormatPrice(product.SourcePrice));
            tv_good_num.setText(product.Quantity + "");
        }
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        void doIncrease(int position);

        void doDecrease(int position);

        void doMultiModify(int position);
    }

    public interface CheckedInterface {
        void onChecked(boolean delete, int position);
    }
}
