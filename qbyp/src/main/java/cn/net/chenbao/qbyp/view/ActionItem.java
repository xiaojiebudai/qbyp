package cn.net.chenbao.qbyp.view;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2016/12/27.
 *
 * 弹窗内部子类项（绘制标题和图标）
 */

public class ActionItem {
    // 定义图片对象
    public Drawable mDrawable;
    // 定义文本对象
    public CharSequence mTitle;
    public ActionItem(Context context, CharSequence title, int drawableId) {
        this.mTitle = title;
        this.mDrawable = context.getResources().getDrawable(drawableId);
    }
}
