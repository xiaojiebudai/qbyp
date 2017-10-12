package cn.net.chenbao.qbyp.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.net.chenbao.qbyp.utils.DialogUtils;

/**
 * fragment基类
 *
 * @author xl
 * @date:2016-7-25上午10:56:48
 * @description
 */
public abstract class FatherFragment extends Fragment {

    protected View mGroup;
    protected LayoutInflater mInflater;
    protected Dialog mDialog_wait;

    /**
     * 设置布局id
     *
     * @return
     */
    protected abstract int getLayoutId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        if (mGroup == null) {
            mGroup = inflater.inflate(getLayoutId(), container, false);
            initView();
        }
        return mGroup;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 初始化控件
     */
    protected abstract void initView();

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 开放的刷新方法
     */
    public void onRefresh() {

    }

    /**
     * 显示WaitDialog
     */
    public void showWaitDialog() {
        try{
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mDialog_wait == null) {

                        initWaitDialog(false);
                    }
                    if (mDialog_wait.isShowing()) {
                        return;
                    }
                    mDialog_wait.show();
                }
            });
        }catch (Exception e){

        }


    }

    /**
     * 初始化WaitDialog
     *
     * @param cancelable
     * @author xl
     * @description 开发设置是否取消
     */
    protected void initWaitDialog(boolean cancelable) {
        if (mDialog_wait == null) {
            mDialog_wait = DialogUtils.getWaitDialog(getActivity(), cancelable);
        }
    }

    /**
     * 隐藏WaitDialog
     */
    protected void dismissWaitDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog_wait != null && mDialog_wait.isShowing()) {
                    mDialog_wait.dismiss();
                }
            }
        });
    }
}
