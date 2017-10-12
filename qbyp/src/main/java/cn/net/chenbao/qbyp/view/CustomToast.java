package cn.net.chenbao.qbyp.view;

import cn.net.chenbao.qbyp.R;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Description:自定义view,位置的Toast(宽match,高wrap)
 *
 * @author wuri
 * @date 2016-10-12
 */
public class CustomToast {

    /**
     * 窗体管理者
     */
    public static WindowManager wm = null;
    private WindowManager.LayoutParams mParams;
    public static View mView = null;
    private double time;
    private static Handler handler = new Handler();
    private static Runnable taskRunable;
    private String text;
    private TextView tvToast;
    private static boolean isShow = false;

    private CustomToast(Context context, View v, int yOffset, String text,
                        double time) {
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        mView = v;
        tvToast = (TextView) mView.findViewById(R.id.tv_toast);
        tvToast.setText(text);
        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mParams.setTitle("Toast");
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mParams.gravity = Gravity.TOP;
        mParams.y = yOffset;

        this.time = time;
    }

    /**
     * @param context
     * @param v       自定义的显示空间(默认是全宽,高度包裹显示)
     * @param yOffset y方向偏移量
     * @param text    显示文字
     * @param time    显示持续时间(秒)
     * @return
     */
    public static CustomToast makeText(Context context, View v, int yOffset,
                                       String text, double time) {
        CustomToast toastCustom = new CustomToast(context, v, yOffset, text,
                time);
        return toastCustom;
    }

    public void show() {
        wm.addView(mView, mParams);
        isShow = true;
        taskRunable = new Runnable() {

            @Override
            public void run() {
                wm.removeViewImmediate(mView);
                isShow = false;
            }
        };
        handler.postDelayed(taskRunable, (long) (time * 1000));
    }

    public void cancel() {
        handler.removeCallbacks(taskRunable);
        wm.removeViewImmediate(mView);
        isShow = false;
    }

    public void setText(String text) {
        tvToast.setText(text);
    }

    public boolean isShow() {
        return isShow;
    }

    public static void removeWindow() {
        handler.removeCallbacks(taskRunable);
        if (isShow) {
            wm.removeViewImmediate(mView);
        }
        isShow = false;
    }

}
