package cn.net.chenbao.qbyp.view;

import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.ZLog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class PullLinearLayout extends LinearLayout {

	public PullLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PullLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullLinearLayout(Context context) {
		super(context);
	}

	private float startX;
	private float startY;
	private float curtX;
	private float curtY;

	private boolean isMove;
	private boolean isIntercept;

	private boolean inRangeOfView(View view, MotionEvent ev) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		int x = location[0];
		int y = location[1];
		return !(ev.getRawX() < x || ev.getRawX() > (x + view.getWidth())
				|| ev.getRawY() < y || ev.getRawY() > (y + view.getHeight()));
	}

	private boolean isClickChild(MotionEvent ev) {
		int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {
			ZLog.showPost("ACTION_UP" + i);
			if (inRangeOfView(getChildAt(i), ev)) {
				WWToast.showShort("click__" + i);
				return true;
			}
		}
		return false;
	}

}
