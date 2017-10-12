package cn.net.chenbao.qbyp.view;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RadioGroup;

public class CustomRadioGroup extends RadioGroup {
	/** 触摸时按下的点 **/
	PointF downP = new PointF();
	/** 触摸时当前的点 **/
	PointF curP = new PointF();

	public CustomRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		curP.x = event.getX();
		curP.y = event.getY();

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			downP.x = event.getX();
			downP.y = event.getY();
		}

		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			float distanceX = curP.x - downP.x;
			float distanceY = curP.y - downP.y;
			// 接近垂直滚动
			if (Math.abs(distanceX) > Math.abs(distanceY)) {
				return false;
			}
		}

		return super.onTouchEvent(event);
	}
}
