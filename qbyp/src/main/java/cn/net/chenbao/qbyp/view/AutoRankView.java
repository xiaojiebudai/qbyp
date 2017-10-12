package cn.net.chenbao.qbyp.view;

import cn.net.chenbao.qbyp.myinterface.OnOnelineChildCountListener;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * 自动排列换行的View (添加了是否换行监听)
 * 
 * @author xl
 * 
 */
public class AutoRankView extends ViewGroup {
	private int PADDING_HOR = 25;// 水平方向padding
	private int PADDING_VERTICAL = 10;// 垂直方向padding
	private int SIDE_MARGIN = 20;// 左右间距
	private int TEXT_MARGIN = 30;
	private Context mContext;

	/**
	 * @param context
	 */
	public AutoRankView(Context context) {
		super(context);
		mContext = context;
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public AutoRankView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public AutoRankView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	public void setChildPadding(int paddingH, int paddingV) {
		PADDING_HOR = DensityUtil.dip2px(mContext, paddingH);
		PADDING_VERTICAL = DensityUtil.dip2px(mContext, paddingV);

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		int autualWidth = r - l;
		int x = SIDE_MARGIN + getPaddingLeft();// 横坐标开始
		int y = 0;// 纵坐标开始
		int rows = 1;
		for (int i = 0; i < childCount; i++) {
			View view = getChildAt(i);
			int width = view.getMeasuredWidth();
			int height = view.getMeasuredHeight();

			// ZLog.e(width + "   " + height);

			x += width + TEXT_MARGIN;
			if (x + getPaddingRight() > autualWidth) {// 加上了paddingRight值
				x = width + SIDE_MARGIN + getPaddingLeft();
				rows++;
			}
			y = rows * (height + TEXT_MARGIN);
			if (i == 0) {
				view.layout(x - width - TEXT_MARGIN, y - height, x
						- TEXT_MARGIN, y);
				x -= TEXT_MARGIN;
			} else {
				view.layout(x - width, y - height, x, y);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// measureChildren(widthMeasureSpec, heightMeasureSpec);
		int x = 0;// 横坐标
		int y = 0;// 纵坐标
		int rows = 1;// 总行数
		int specWidth = MeasureSpec.getSize(widthMeasureSpec);
		int actualWidth = specWidth - SIDE_MARGIN * 2;// 实际宽度
		int childCount = getChildCount();
		for (int index = 0; index < childCount; index++) {
			View child = getChildAt(index);
			child.setPadding(PADDING_HOR, PADDING_VERTICAL, PADDING_HOR,
					PADDING_VERTICAL);

			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			x += width + TEXT_MARGIN;
			if (x > actualWidth && !isOneLine) {// 换行
				x = width;
				rows++;

			} else if (x > actualWidth && isOneLine) {
				if (listener != null) {
					listener.childCount(index); // index进来的时候已经超出一行了
					break;
				}
			}
			y = rows * (height + TEXT_MARGIN);
		}
		y += (getPaddingBottom() + getPaddingTop());
		setMeasuredDimension(actualWidth, y);
	}

	/**
	 * 是否只设置一行
	 */
	private boolean isOneLine = false;

	public void setOnlyOneLine(boolean oneLine) {
		isOneLine = oneLine;
	}

	private OnOnelineChildCountListener listener;

	public void setOnOnelineChildCountListener(
			OnOnelineChildCountListener listener) {
		this.listener = listener;
	}

}
