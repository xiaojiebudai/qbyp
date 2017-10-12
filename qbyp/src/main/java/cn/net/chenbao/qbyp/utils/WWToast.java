package cn.net.chenbao.qbyp.utils;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Binder;
import android.os.Build;
import android.view.Gravity;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperToast;

import cn.net.chenbao.qbyp.R;

import java.lang.reflect.Method;

/**
 * Toast类
 *
 * @author xl
 * @date:2016-7-25下午12:54:02
 * @description
 */
public class WWToast {
    // Toast
    private static SuperToast toast;

    private static Context context;

    /**
     * 全局初始化
     *
     * @param context
     * @author xl
     * @date:2016-1-27下午2:34:10
     * @description
     */
    public static void init(Context context) {
        if (toast == null) {
            toast = new SuperToast(context);
            toast.setText(context.getResources().getString(R.string.app_name))
                    .setDuration(1000)
                    .setFrame(Style.FRAME_STANDARD)
                    .setAnimations(Style.ANIMATIONS_FLY)
                    .setColor(context.getResources().getColor(R.color.black_transparent_CC));
            toast.setGravity(Gravity.CENTER, 0, 0);
            WWToast.context = context;
        }
    }

    /**
     * 短时间显示全局toast
     *
     * @param message
     * @author xl
     * @date:2016-1-27下午2:39:13
     * @description
     */
    public static void showShort(String message) {
        if (null == toast) {
            toast = new SuperToast(context);
            toast.setText(message)
                    .setDuration(1000)
                    .setFrame(Style.FRAME_STANDARD)
                    .setAnimations(Style.ANIMATIONS_FLY)
                    .setColor(context.getResources().getColor(R.color.black_transparent_CC));
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message);
        }

        toast.show();
    }

    /**
     * * 短时间显示全局toast
     *
     * @param messageId
     * @author xl
     * @date:2016-3-10下午1:49:07
     * @description
     */
    public static void showShort(int messageId) {
        if (null == toast) {
            toast = new SuperToast(context);
            toast.setText(context.getResources().getString(messageId))
                    .setDuration(1000)
                    .setFrame(Style.FRAME_STANDARD)
                    .setAnimations(Style.ANIMATIONS_FLY)
                    .setColor(context.getResources().getColor(R.color.black_transparent_CC));
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(context.getResources().getString(messageId));
        }

        toast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        if (null == toast) {
            toast = new SuperToast(context);
            toast.setText(message + "")
                    .setDuration(1000)
                    .setFrame(Style.FRAME_STANDARD)
                    .setAnimations(Style.ANIMATIONS_FLY)
                    .setColor(context.getResources().getColor(R.color.black_transparent_CC));
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message + "");
        }

        toast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (null == toast) {
            toast = new SuperToast(context);
            toast.setText(context.getResources().getString(message))
                    .setDuration(1000)
                    .setFrame(Style.FRAME_STANDARD)
                    .setAnimations(Style.ANIMATIONS_FLY)
                    .setColor(context.getResources().getColor(R.color.black_transparent_CC));
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(context.getResources().getString(message));
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (null == toast) {
            toast = new SuperToast(context);
            toast.setText(message + "")
                    .setDuration(3000)
                    .setFrame(Style.FRAME_STANDARD)
                    .setAnimations(Style.ANIMATIONS_FLY)
                    .setColor(context.getResources().getColor(R.color.black_transparent_CC));
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message + "");
        }
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (null == toast) {
            toast = new SuperToast(context);
            toast.setText(context.getResources().getString(message))
                    .setDuration(3000)
                    .setFrame(Style.FRAME_STANDARD)
                    .setAnimations(Style.ANIMATIONS_FLY)
                    .setColor(context.getResources().getColor(R.color.black_transparent_CC));
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(context.getResources().getString(message));
        }
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (null == toast) {
            toast = new SuperToast(context);
            toast.setText(message + "")
                    .setDuration(duration)
                    .setFrame(Style.FRAME_STANDARD)
                    .setAnimations(Style.ANIMATIONS_FLY)
                    .setColor(context.getResources().getColor(R.color.black_transparent_CC));
            toast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toast.setText(message + "");
        }
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (null == toast) {
            toast = new SuperToast(context);
            toast.setText(context.getResources().getString(message))
                    .setDuration(duration)
                    .setFrame(Style.FRAME_STANDARD)
                    .setAnimations(Style.ANIMATIONS_FLY)
                    .setColor(context.getResources().getColor(R.color.black_transparent_CC));
            toast.setGravity(Gravity.CENTER, 0, 0);

        } else {
            toast.setText(context.getResources().getString(message));
        }
        toast.show();
    }

    /**
     * Hide the toast, if any.
     */
    public static void hideToast() {
        if (null != toast) {
            toast.dismiss();
        }
    }
    private static SuperToast toastImg;

    public static void showCostomSuccess(int message) {
        if (null == toastImg) {
            toastImg = new SuperToast(context);
            toastImg.setText(context.getResources().getString(message))
                    .setDuration(1000)
                    .setFrame(Style.FRAME_STANDARD)
                    .setAnimations(Style.ANIMATIONS_FLY)
                    .setColor(context.getResources().getColor(R.color.black_transparent_CC));
            toastImg.setGravity(Gravity.CENTER, 0, 0);
        } else {
            toastImg.setText(context.getResources().getString(message));
        }
        toastImg.setIconResource(Style.ICONPOSITION_TOP, R.drawable.icon_right);

        toastImg.show();
    }

    /**
     * 4.4 以上可以直接判断准确
     *
     * 4.4 以下非MIUI直接返回true
     *
     * 4.4 以下MIUI 可 判断 上一次打开app 时 是否开启了悬浮窗权限
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isFloatWindowOpAllowed(Context context) {
        final int version = Build.VERSION.SDK_INT;

        if(!AndtoidRomUtil.isFlyme() && !AndtoidRomUtil.isMIUI()){
            return true;
        }

        if (version >= 19) {
            return checkOp(context, 24);  //自己写就是24 为什么是24?看AppOpsManager //AppOpsManager.OP_SYSTEM_ALERT_WINDOW
        } else {
            if(AndtoidRomUtil.isMIUI()){
                if ((context.getApplicationInfo().flags & 1 << 27) == 1 <<27 ) {
                    return true;
                } else {
                    return false;
                }
            }else{
                return true;
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;

        if (version >= 19) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class managerClass = manager.getClass();
                Method method = managerClass.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                int isAllowNum = (Integer) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());

                if (AppOpsManager.MODE_ALLOWED == isAllowNum) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
