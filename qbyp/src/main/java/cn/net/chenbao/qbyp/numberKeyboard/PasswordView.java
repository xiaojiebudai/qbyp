package cn.net.chenbao.qbyp.numberKeyboard;

import cn.net.chenbao.qbyp.utils.DensityUtil;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.widget.EditText;

import cn.net.chenbao.qbyp.R;

/**
 * 密码框控件
 * 
 * @author xl
 * @date:2016-2-27下午1:40:59
 */
public class PasswordView extends EditText {

	private int mTextLength;

	private int mPasswordLength = 6;
	private float passwordWidth = 15;

	private Paint passwordPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private final int defaultSplitLineWidth = 2;

	private boolean isShowPassword;

	public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public PasswordView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.PasswordView);
		int count = array.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = array.getIndex(i);
			switch (attr) {
			case R.styleable.PasswordView_passwordColor:
				passwordPaint.setColor(array.getColor(attr, 0xFF222222));
				break;
			case R.styleable.PasswordView_passwordLength:
				mPasswordLength = array.getInt(attr, 6);
				break;
			case R.styleable.PasswordView_showPassword:
				isShowPassword = array.getBoolean(attr, false);
				break;
			default:
				break;
			}
		}
		array.recycle();
	}

	public PasswordView(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		int height = getHeight();
		int width = getWidth();

		// 分割线
		borderPaint.setColor(0xFFCCCCCC);
		borderPaint.setStrokeWidth(defaultSplitLineWidth);
		canvas.drawLine(0, 0, width, 0, borderPaint);
		canvas.drawLine(0, height, width, height, borderPaint);
		for (int i = 0; i < mPasswordLength + 1; i++) {
			float x = width * i / mPasswordLength;
			canvas.drawLine(x, 0, x, height, borderPaint);
		}

		// 密码
		float itemWidth = width / mPasswordLength;
		String input = getText().toString();
		if (isShowPassword) {
			FontMetrics fontMetrics = passwordPaint.getFontMetrics();
			float x;
			float baseline = height / 2 - fontMetrics.descent
					+ (fontMetrics.descent - fontMetrics.ascent) / 2;
			passwordPaint.setTextAlign(Align.CENTER);
			passwordPaint.setTextSize(DensityUtil.sp2px(getContext(), 30));// 需要的话再改自定义属性或者方法
			for (int i = 0; i < mTextLength; i++) {
				x = itemWidth * (i + 0.5F);
				canvas.drawText(input.charAt(i) + "", x, baseline,
						passwordPaint);
			}
		} else {
			passwordPaint.setColor(0xFF222222);
			float cx, cy = height / 2;
			float half = width / mPasswordLength / 2;
			for (int i = 0; i < mTextLength; i++) {
				cx = width * i / mPasswordLength + half;
				canvas.drawCircle(cx, cy, passwordWidth, passwordPaint);
			}
		}

	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
		this.mTextLength = text.length();
		invalidate();
	}
}
