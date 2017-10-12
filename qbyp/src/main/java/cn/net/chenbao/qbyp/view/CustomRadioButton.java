package cn.net.chenbao.qbyp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RadioButton;

public class CustomRadioButton extends RadioButton {

	private int lastX;
	private int lastY;

	public CustomRadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);// 父容器不拦截点击事件，子控件拦截点击事件
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = x - lastX;
			int deltaY = y - lastY;
			if (Math.abs(deltaY) - Math.abs(deltaX) > 0) {// 竖直滑动的父容器拦截事件
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			break;
		}
		lastX = x;
		lastY = y;
		return super.onTouchEvent(event);
	}
}
