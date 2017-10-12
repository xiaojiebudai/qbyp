package cn.net.chenbao.qbyp.adapter.listview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.bean.ShopCarSelfSupport;
import cn.net.chenbao.qbyp.bean.ShopCarSelfSupportGroup;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

import java.util.List;
import java.util.Map;

import static cn.net.chenbao.qbyp.R.id.cb_headcheckbox;

/**
 * Created by wuri on 2016/11/1.
 */

public class SelfSupportShopCartAdapter extends BaseExpandableListAdapter {
    private List<ShopCarSelfSupportGroup> groups;
    private Map<Integer, List<ShopCarSelfSupport>> children;
    private Context mContext;
    private CheckInterface checkInterface;
    private ModifyCountInterface modifyCountInterface;

    public SelfSupportShopCartAdapter(Context context) {
        super();
        this.mContext = context;
    }

    public void setData(List<ShopCarSelfSupportGroup> groups, Map<Integer, List<ShopCarSelfSupport>> children) {
        this.groups = groups;
        this.children = children;
        notifyDataSetChanged();
    }

    public List<ShopCarSelfSupportGroup> getData() {
        return this.groups;
    }

    public void setCheckInterface(CheckInterface checkInterface) {
        this.checkInterface = checkInterface;
    }

    public void setModifyCountInterface(ModifyCountInterface modifyCountInterface) {
        this.modifyCountInterface = modifyCountInterface;
    }

    @Override
    public int getGroupCount() {
        return groups == null ? 0 : groups.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return children.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ShopCarSelfSupport> childs = children.get(groupPosition);
        return childs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupHolder gholder;
        if (convertView == null) {
            gholder = new GroupHolder();
            convertView = View.inflate(mContext, R.layout.lv_selfsupport_shopcart_headitem, null);
            gholder.gray_width_divider = convertView.findViewById(R.id.gray_width_divider);
            gholder.ll_shop = convertView.findViewById(R.id.ll_shop);
            gholder.cb_headcheckbox = (CheckBox) convertView.findViewById(cb_headcheckbox);
            gholder.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            convertView.setTag(gholder);
        } else {
            gholder = (GroupHolder) convertView.getTag();
        }

        gholder.gray_width_divider.setVisibility(groupPosition == 0 ? View.GONE : View.VISIBLE);

        final ShopCarSelfSupportGroup group = (ShopCarSelfSupportGroup) getGroup(groupPosition);
        if (group != null) {
            gholder.tv_shop_name.setText(group.VenderName);
            gholder.ll_shop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    group.setChoosed(!gholder.cb_headcheckbox.isChecked());
                    checkInterface.checkGroup(groupPosition, (!gholder.cb_headcheckbox.isChecked()));// 暴露组选接口
                }
            });
            gholder.cb_headcheckbox.setChecked(group.isChoosed());
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder cholder;
        if (convertView == null) {
            cholder = new ChildHolder();
            convertView = View.inflate(mContext, R.layout.lv_selfsupport_shopcart_item, null);
            cholder.ll_check = convertView.findViewById(R.id.ll_check);
            cholder.ll_good = convertView.findViewById(R.id.ll_good);
            cholder.cb_itemcheckbox = (CheckBox) convertView.findViewById(R.id.cb_itemcheckbox);
            cholder.iv_good_img = (ImageView) convertView.findViewById(R.id.iv_good_img);
            cholder.tv_good_name = (TextView) convertView.findViewById(R.id.tv_good_name);
            cholder.tv_sku = (TextView) convertView.findViewById(R.id.tv_sku);
            cholder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            cholder.tv_not_enough = (TextView) convertView.findViewById(R.id.tv_not_enough);
            cholder.tv_good_num = (TextView) convertView.findViewById(R.id.tv_good_num);
            cholder.iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
            cholder.iv_sub = (ImageView) convertView.findViewById(R.id.iv_sub);
//            childrenMap.put(groupPosition, convertView);
            convertView.setTag(cholder);
        } else {
//            convertView = childrenMap.get(groupPosition);
            cholder = (ChildHolder) convertView.getTag();
        }
        final ShopCarSelfSupport product = (ShopCarSelfSupport) getChild(groupPosition, childPosition);
        System.out.println("groupPosition==" + groupPosition + "childPosition==" + childPosition);

        if (product != null) {
            ImageUtils.setCommonImage(mContext, ImageUtils.getRightImgScreen(product.ImageUrl, DensityUtil.dip2px(mContext, 80), DensityUtil.dip2px(mContext, 80)), cholder.iv_good_img);
            cholder.ll_good.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublicWay.stratSelfSupportGoodsDetailActivity((Activity) mContext, product.ProductId);
                }
            });
            WWViewUtil.textInsertDrawable(mContext, cholder.tv_good_name, product.ProductName, false, product.IsVipLevel);
            cholder.tv_sku.setText(product.ProName);
            cholder.tv_price.setText(WWViewUtil.numberFormatPrice(product.SalePrice));
            cholder.tv_good_num.setText(product.Quantity + "");
            if (product.Quantity > product.StockQty) {//TODO 库存不足
                cholder.tv_not_enough.setVisibility(View.VISIBLE);
            } else {
                cholder.tv_not_enough.setVisibility(View.GONE);
            }

            cholder.cb_itemcheckbox.setChecked(product.isChoosed());
            cholder.ll_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cholder.cb_itemcheckbox.setChecked(!cholder.cb_itemcheckbox.isChecked());
                    product.setChoosed(cholder.cb_itemcheckbox.isChecked());
                    checkInterface.checkChild(groupPosition, childPosition, cholder.cb_itemcheckbox.isChecked());// 暴露子选接口
                }
            });
            cholder.iv_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doIncrease(groupPosition, childPosition, cholder.tv_good_num, cholder.cb_itemcheckbox.isChecked());// 暴露增加接口
                }
            });
            cholder.iv_sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modifyCountInterface.doDecrease(groupPosition, childPosition, cholder.tv_good_num, cholder.cb_itemcheckbox.isChecked(), cholder.tv_not_enough);// 暴露删减接口
                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * 组元素绑定器
     */
    private class GroupHolder {
        View gray_width_divider;
        View ll_shop;
        CheckBox cb_headcheckbox;
        TextView tv_shop_name;
    }

    /**
     * 子元素绑定器
     */
    private class ChildHolder {
        View ll_check;
        View ll_good;
        CheckBox cb_itemcheckbox;
        ImageView iv_good_img;
        TextView tv_good_name;
        TextView tv_sku;
        TextView tv_price;
        TextView tv_not_enough;
        TextView tv_good_num;
        ImageView iv_add;
        ImageView iv_sub;
    }

    /**
     * 复选框接口
     */
    public interface CheckInterface {
        /**
         * 组选框状态改变触发的事件
         *
         * @param groupPosition 组元素位置
         * @param isChecked     组元素选中与否
         */
        void checkGroup(int groupPosition, boolean isChecked);

        /**
         * 子选框状态改变时触发的事件
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param isChecked     子元素选中与否
         */
        void checkChild(int groupPosition, int childPosition, boolean isChecked);
    }

    /**
     * 改变数量的接口
     */
    public interface ModifyCountInterface {
        /**
         * 增加操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked);

        /**
         * 删减操作
         *
         * @param groupPosition 组元素位置
         * @param childPosition 子元素位置
         * @param showCountView 用于展示变化后数量的View
         * @param isChecked     子元素选中与否
         */
        void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked, TextView tv_not_enough);
    }
}


