package cn.net.chenbao.qbypseller.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
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

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		curtX = event.getX();
		curtY = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isMove = false;
			startX = event.getX();
			startY = event.getY();
			break;

		case MotionEvent.ACTION_MOVE:
			isMove = true;
			break;
		case MotionEvent.ACTION_UP:
			isMove = false;
			break;
		case MotionEvent.ACTION_CANCEL:
			isMove = false;
			break;
		default:
			break;
		}

		return super.onTouchEvent(event);
	}
}
