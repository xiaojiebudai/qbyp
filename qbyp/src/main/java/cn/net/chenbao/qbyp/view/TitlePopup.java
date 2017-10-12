package cn.net.chenbao.qbyp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.TitlePopupAdapter;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

/**
 * Created by Administrator on 2016/12/27.
 * 标题按钮上的弹窗
 * TitlePopup popup=new TitlePopup(getActivity());
 * ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();
 * mActionItems.add(new ActionItem(getActivity(),"test0",R.drawable.accountsafe));
 * mActionItems.add(new ActionItem(getActivity(),"test1",R.drawable.accountsafe));
 * mActionItems.add(new ActionItem(getActivity(),"test2",R.drawable.accountsafe));
 * mActionItems.add(new ActionItem(getActivity(),"test3",R.drawable.accountsafe));
 * mActionItems.add(new ActionItem(getActivity(),"test4",R.drawable.accountsafe));
 * mActionItems.add(new ActionItem(getActivity(),"test5",R.drawable.accountsafe));
 * mActionItems.add(new ActionItem(getActivity(),"test6",R.drawable.accountsafe));
 * mActionItems.add(new ActionItem(getActivity(),"test7",R.drawable.accountsafe));
 * mActionItems.add(new ActionItem(getActivity(),"test8",R.drawable.accountsafe));
 * mActionItems.add(new ActionItem(getActivity(),"test9",R.drawable.accountsafe));
 * mActionItems.add(new ActionItem(getActivity(),"test10",R.drawable.accountsafe));
 * mActionItems.add(new ActionItem(getActivity(),"test11",R.drawable.accountsafe));
 * popup.setData(mActionItems);
 * popup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
 *
 * @Override public void onItemClick(ActionItem item, int position) {
 * WWToast.showShort(item.mTitle+"");
 * switch (position){
 * case 0:break;
 * case 1:break;
 * case 2:break;
 * case 3:break;
 * case 4:break;
 * case 5:break;
 * }
 * }
 * });
 * popup.showWindow(v);
 */

public class TitlePopup extends PopupWindow {
    private Context mContext;

    // 定义列表对象
    private RecyclerView mListView;
    // 弹窗子类项选中时的监听
    private OnItemOnClickListener mItemOnClickListener;
    // 定义弹窗子类项列表
    private ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();
    private TitlePopupAdapter mAdapter;
    private LayoutInflater mInflater;

    public TitlePopup(Context context) {
        // 设置布局的参数
        this(context, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    public void setData(ArrayList<ActionItem> mActionItems) {
        this.mActionItems = mActionItems;
        mAdapter.setNewData(mActionItems);
    }

    public TitlePopup(Context context, int width, int height) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        // 设置可以获得焦点
        setFocusable(true);
        // 设置弹窗内可点击
        setTouchable(true);
        // 设置弹窗外可点击
        setOutsideTouchable(true);

        // 设置弹窗的宽度和高度
        setWidth(width);
        setHeight(height);
        setBackgroundDrawable(new BitmapDrawable());
        // 设置弹窗的布局界面
        setContentView(LayoutInflater.from(mContext).inflate(
                R.layout.title_popup, null));
        // 设置动画
//        setAnimationStyle(R.style.AnimTop);
        initUI();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow()
                .getAttributes();
        params.alpha = 1.0F;
        ((Activity) mContext).getWindow().setAttributes(params);
    }

    /**
     * 初始化弹窗列表
     */
    private void initUI() {
        mListView = (RecyclerView) getContentView().findViewById(
                R.id.lv_data);
        mAdapter=new TitlePopupAdapter(mActionItems);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ActionItem item = (ActionItem) mAdapter.getItem(position);
                // 点击子类项后，弹窗消失
                dismiss();
                if (mItemOnClickListener != null)
                    mItemOnClickListener.onItemClick(item,
                            position);
            }
        });
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new GridLayoutManager(mContext, 5));
        mListView.setItemAnimator(new DefaultItemAnimator());
        mListView.setAdapter(mAdapter);
    }

    public void showWindow(View view) {
        WWViewUtil.setPopInSDK7(this,view);
        WindowManager.LayoutParams params = ((Activity) mContext).getWindow()
                .getAttributes();
        params.alpha = 0.6F;
        ((Activity) mContext).getWindow().setAttributes(params);
    }


    /**
     * 设置监听事件
     */
    public void setItemOnClickListener(
            OnItemOnClickListener onItemOnClickListener) {
        this.mItemOnClickListener = onItemOnClickListener;
    }

    /**
     * 功能描述：弹窗子类项按钮监听事件
     */
    public interface OnItemOnClickListener {
        void onItemClick(ActionItem item, int position);
    }
}
