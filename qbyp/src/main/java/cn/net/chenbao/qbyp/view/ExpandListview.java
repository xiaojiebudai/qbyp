package cn.net.chenbao.qbyp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 展开的listview
 * 
 * @author xl
 * @date 2016-7-26 下午9:54:18
 */
public class ExpandListview extends ListView {

	public ExpandListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ExpandListview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ExpandListview(Context context) {
		super(context);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
