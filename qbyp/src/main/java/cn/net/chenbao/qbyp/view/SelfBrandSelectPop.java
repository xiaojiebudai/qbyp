package cn.net.chenbao.qbyp.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.net.chenbao.qbyp.bean.ShopBrand;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 木头 on 2016/11/2.
 * 自营商城选择品牌
 */
public class SelfBrandSelectPop implements View.OnClickListener {
    private PopupWindow mPopupWindow;
    private View mParent;
    private Context context;
    private View content;
    private GridView gvBrand;
    //    private PopSelfBrandGridViewAdapter popSelfBrandGridViewAdapter;
    private AutoRankView mAv;
    private List<View> listV = new ArrayList<View>();
    private ArrayList<ShopBrand> brands;
    private SelectCallBack selectListener;
    public SelectCallBack getselectListener() {
        return selectListener;
    }

    public void setSelectListener(SelectCallBack listener) {
        this.selectListener = listener;
    }

    public interface SelectCallBack {
        void selectListener(ShopBrand brand);
    }
    public SelfBrandSelectPop(final Context context, int parentId, ArrayList<ShopBrand> list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.brands = list;
        LayoutInflater inflater = LayoutInflater.from(context);
        mParent = inflater.inflate(parentId, null);
        content = inflater.inflate(R.layout.pop_self_brand_select, null);
        mPopupWindow = new PopupWindow(content, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, true);
        content.findViewById(R.id.iv_cancel).setOnClickListener(this);
        content.findViewById(R.id.iv_black).setOnClickListener(this);
//        gvBrand=(GridView)content.findViewById(R.id.gv_brand);
        mAv = (AutoRankView) content.findViewById(R.id.av);
        setAv(brands);
//        popSelfBrandGridViewAdapter=new PopSelfBrandGridViewAdapter(context);
//        gvBrand.setAdapter(popSelfBrandGridViewAdapter);
//        popSelfBrandGridViewAdapter.setDatas(list);


        // 设置SelectPicPopupWindow弹出窗体可点击
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    public void setAv(ArrayList<ShopBrand> brands) {
        listV.clear();
        mAv.removeAllViews();
        for (final ShopBrand shopBrand : brands) {
            final TextView view = (TextView) View.inflate(
                    context,
                    R.layout.textview_hot_search, null);
            view.setText(shopBrand.BrandName);
            view.setTag(shopBrand);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!view.isSelected()) {
                        view.setSelected(true);
                        setTextSelectState(view);
                    }
                    if (selectListener != null) {
                        selectListener.selectListener((ShopBrand) view.getTag());
                        dismissWindow();
                    }

                }
            });
            listV.add(view);
            mAv.addView(view);
        }
    }

    public void setTextSelectState(View v) {
        for (int i = 0; i < listV.size(); i++) {
            if (listV.get(i) != v) {
                listV.get(i).setSelected(false);
            }
        }
    }

    public void showWindow(View view) {
        if (mPopupWindow != null) {
            WWViewUtil.setPopInSDK7(mPopupWindow,view);
//            mPopupWindow.showAsDropDown(view);
        }
    }

    public Boolean isShowing() {
        return mPopupWindow.isShowing();
    }

    public void dismissWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.iv_cancel:
                dismissWindow();
                break;
            case R.id.iv_black:
                dismissWindow();
                break;
        }
    }
}
