package cn.net.chenbao.qbypseller.adapter;

import java.util.List;

import cn.net.chenbao.qbypseller.fragment.FatherFragment;
import cn.net.chenbao.qbypseller.utils.ZLog;
import cn.net.chenbao.qbypseller.view.TabScrollView.TabInterface;

import org.xutils.common.util.DensityUtil;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;

/**
 * 通用fragment+viewpager适配器
 *
 * @param <T>
 * @author xl
 * @date:2016-7-22上午9:59:22
 * @description 实例的时候设置加载摸模式
 */
public class CommonPageAdapter<T extends FatherFragment> extends PagerAdapter
        implements OnPageChangeListener, TabInterface {

    private Context mContext;

    /**
     * tab列表
     */
    private List<String> mTabs;

    private List<T> mFragments; // 每个Fragment对应一个Page
    private FragmentManager mFragmentManager;
    private ViewPager mViewPager; // viewPager对象
    private int mCurrentPageIndex = 0; // 当前page索引（切换之前）
    private OnExtraPageChangeListener onExtraPageChangeListener; // ViewPager切换页面时的额外功能添加接口

    /**
     * 切换时fragment刷新
     */
    private boolean doChangeRefresh;

    public CommonPageAdapter(Context mContext, List<String> mTabs,
                             List<T> fragments, FragmentManager fragmentManager,
                             ViewPager viewPager, int currentPageIndex, boolean doChangeRefresh) {
        super();
        this.mContext = mContext;
        this.mTabs = mTabs;
        this.mFragments = fragments;
        this.mFragmentManager = fragmentManager;
        this.mViewPager = viewPager;
        this.mCurrentPageIndex = currentPageIndex;
        if (mTabs != null && mTabs.size() > 0) {
            this.mViewPager.setOnPageChangeListener(this);
        }
        this.doChangeRefresh = doChangeRefresh;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mFragments.get(position).getView()); // 移出viewpager两边之外的page布局
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = mFragments.get(position);
        if (!fragment.isAdded()) { // 如果fragment还没有added
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.add(fragment, fragment.getClass().getSimpleName());
            ft.commitAllowingStateLoss();// 解决 java.lang.IllegalStateException:
            // Can not perform this action after
            // onSaveInstanceState
            /**
             * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
             * 会在进程的主线程中，用异步的方式来执行。 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
             * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
             */
            mFragmentManager.executePendingTransactions();
        }

        if (fragment.getView().getParent() == null) {
            container.addView(fragment.getView()); // 为viewpager增加布局
        }

        return fragment.getView();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (null != onExtraPageChangeListener) { // 如果设置了额外功能接口
            onExtraPageChangeListener.onExtraPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {
        if (null != onExtraPageChangeListener) { // 如果设置了额外功能接口
            onExtraPageChangeListener.onExtraPageScrolled(position,
                    positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        mFragments.get(mCurrentPageIndex).onPause(); // 调用切换前Fargment的onPause()
        mFragments.get(mCurrentPageIndex).onStop(); // 调用切换前Fargment的onStop()
        if (mFragments.get(position).isAdded()) {
            mFragments.get(mCurrentPageIndex).onStart(); // 调用切换后Fargment的onStart()

            if (doChangeRefresh) {
                ZLog.showPost(position + "___");
                // fragment.onResume(); // 调用切换后Fargment的onResume()
                FatherFragment fragment = mFragments.get(position);
                fragment.onRefresh();
            }

        }
        mCurrentPageIndex = position;
        if (null != onExtraPageChangeListener) { // 如果设置了额外功能接口
            onExtraPageChangeListener.onExtraPageSelected(position);
        }
    }

    public OnExtraPageChangeListener getOnExtraPageChangeListener() {
        return onExtraPageChangeListener;
    }

    /**
     * 设置页面切换额外功能监听器
     *
     * @param onExtraPageChangeListener
     */
    public void setOnExtraPageChangeListener(
            OnExtraPageChangeListener onExtraPageChangeListener) {
        this.onExtraPageChangeListener = onExtraPageChangeListener;
    }

    /**
     * 开放page切换的额外接口
     */
    static class OnExtraPageChangeListener {
        public void onExtraPageScrolled(int position, float positionOffset,
                                        int positionOffsetPixels) {
        }

        public void onExtraPageSelected(int position) {

        }

        public void onExtraPageScrollStateChanged(int state) {
        }
    }

    @Override
    public View getTabView() {
        ImageView bottom = new ImageView(mContext);
        bottom.setMaxHeight(DensityUtil.dip2px(2F));
        bottom.setBackgroundColor(mContext.getResources().getColor(
                R.color.yellow_ww));
        return bottom;
    }

    private View[] mTabs_custom_made;

    public void setCustomMadeTab(View[] tabs) {
        mTabs_custom_made = tabs;
    }

    @Override
    public View getTabItem(int position) {
        if (null != mTabs_custom_made) {
            return mTabs_custom_made[position];

        } else {
            TextView tab = new TextView(mContext);
            tab.setPadding(0, 12, 0, 12);
            tab.setText(mTabs.get(position));
            tab.setBackgroundColor(Color.TRANSPARENT);
            tab.setGravity(Gravity.CENTER);
            tab.setTextSize(mContext.getResources().getDimension(
                    R.dimen.tab_text_size));
            tab.setTextColor(mContext.getResources().getColorStateList(
                    R.color.tab_text_selector));
            return tab;
        }
    }

    @Override
    public int getTabCount() {
        return mTabs == null ? 0 : mTabs.size();
    }

    public T getCurrentFragment() {
        if (mFragments != null) {
            return mFragments.get(mCurrentPageIndex);
        }
        return null;// throw?
    }
}
