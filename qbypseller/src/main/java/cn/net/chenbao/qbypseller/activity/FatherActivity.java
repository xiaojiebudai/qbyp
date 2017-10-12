package cn.net.chenbao.qbypseller.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.utils.DialogUtils;
import cn.net.chenbao.qbypseller.utils.StatusBarUtil;

import butterknife.ButterKnife;

/**
 * activity基类
 *
 * @author xl
 * @date:2016-7-24下午3:06:09
 * @description
 */
public abstract class FatherActivity extends FragmentActivity {
    protected Bundle mSavedInstanceState;
    protected InputMethodManager inputMethodManager;
    protected Dialog mDialog_wait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSavedInstanceState = savedInstanceState;
        setContentView(getLayoutId());
        setStatusBar();
        ButterKnife.bind(this);
        initValues();
        initView();
        doOperate();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.yellow_ww),0);

    }


    @TargetApi(19)
    protected static void toTop(Activity activity) {
        StatusBarUtil.setTranslucentForImageViewInFragment(activity,0,null);
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    protected static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 设置布局ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始值
     */
    protected abstract void initValues();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 进行操作
     */
    protected abstract void doOperate();

    /**
     * 初始默认头部
     *
     * @param titleId
     * @param withLeft 看UI图好像有些界面左侧没返回按钮
     * @author xl
     * @date:2016-7-24下午3:11:37
     * @description 左侧按钮返回, 中间title文本
     */
    protected void initDefautHead(int titleId, boolean withLeft) {
        TextView center = (TextView) findViewById(R.id.tv_head_center);
        if (center != null) {
            center.setText(titleId);
            center.setTextColor(getResources().getColor(R.color.white));
            center.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        }
        if (withLeft) {
            initHeadBack();
        }

    }

    /**
     * 初始化头部右侧的文本按钮
     *
     * @param resId
     * @param listener
     */
    protected void initTextHeadRigth(int resId, OnClickListener listener) {
        View right = findViewById(R.id.rl_head_right);
        TextView text = (TextView) findViewById(R.id.tv_head_right);
        if (right != null && text != null) {
            right.setOnClickListener(listener);
            text.setText(resId);
        }
    }

    /**
     * 初始化头部左侧返回
     */
    protected void initHeadBack() {

        View left = findViewById(R.id.rl_head_left);
        if (left != null) {
            left.findViewById(R.id.tv_head_left).setBackgroundResource(
                    R.drawable.arrow_back);
            left.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard() {
        if (inputMethodManager == null) {
            inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 改变背景色
     */
    public void changeBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        if (alpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// 不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);// 此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
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
            mDialog_wait = DialogUtils.getWaitDialog(this, cancelable);
        }
    }

    /**
     * 显示WaitDialog
     */
    protected void showWaitDialog() {
        try{
        runOnUiThread(new Runnable() {

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
        }catch (Exception exception){
            //android.view.ViewRootImpl$CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
            //只有创建这个控件的线程才能去更新该控件的内容。
        }

    }

    /**
     * 隐藏WaitDialog
     */
    protected void dismissWaitDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mDialog_wait != null && mDialog_wait.isShowing()) {
                    mDialog_wait.dismiss();
                }
            }
        });
    }

    /**
     * WaitDialog是否处于显示状态,用于处理显示被用于手动取消显示的逻辑处理
     */
    protected boolean isWaitDialogShowing() {
        if (mDialog_wait != null) {
            return mDialog_wait.isShowing();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissWaitDialog();
                mDialog_wait = null;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}
