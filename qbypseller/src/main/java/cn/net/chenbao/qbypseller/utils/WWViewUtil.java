package cn.net.chenbao.qbypseller.utils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Rect;
import android.os.Build;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;

public class WWViewUtil {
	/**
	 * 给listView设置emptyView
	 */
	public static void setEmptyView(ListView listView, String text) {
		View view = LayoutInflater.from(listView.getContext()).inflate(
				R.layout.emptyview, listView, false);
		listView.setEmptyView(view);
	}
	/**
	 * 给listView设置emptyView
	 */
	public static void setEmptyView(ListView listView) {
		View view = LayoutInflater.from(listView.getContext()).inflate(
				R.layout.emptyview, listView, false);
		listView.setEmptyView(view);
	}
	/**
	 * 给listView设置emptyView
	 */
	public static void setEmptyView(ListView listView,int resource) {
		View view = LayoutInflater.from(listView.getContext()).inflate(
				resource, listView, false);
		listView.setEmptyView(view);
	}

	/***
	 * <p>
	 * Description:设置空字符
	 * </p>
	 * 
	 * @author ZXJ
	 * @date 2016-3-31
	 * @param text
	 * @param old
	 * @param change
	 ***/
	public static void setNullText(TextView text, String old, String change) {
		if (old == null || old.isEmpty()) {
			text.setText(change);
		} else {
			text.setText(old);
		}
	}
	 /***
		 * Description:输出两位小数
		 * @author ZXJ
		 * @date 2016-8-6
		 * @param d
		 * @return
		 */
		public static String numberFormatWithTwo(double d){
			DecimalFormat df = new DecimalFormat("0.00");
			return df.format(d);
			
		}

	/***
	 * Description:输出两位小数价格带符号
	 * @author ZXJ
	 * @date 2016-8-6
	 * @param d
	 * @return
	 */
	public static String numberFormatPrice(double d) {
		DecimalFormat df = new DecimalFormat("0.00");
		return "¥" + df.format(d);
	}
	/**
	 * showAsDropDown适配7.0
	 *
	 * @param p
	 * @param v
	 */
	public static void setPopInSDK7(PopupWindow p, View v) {
		if (Build.VERSION.SDK_INT == 24) {
			Rect rect = new Rect();
			v.getGlobalVisibleRect(rect);
			p.showAtLocation(v, Gravity.NO_GRAVITY, rect.left, rect.bottom);
		}else{
			p.showAsDropDown(v);
		}
	}

	/**
	 * 输入限制  整数限制  小数点后位数限制
	 *
	 * @param view
	 */
	public static void inputLimit(final EditText view, final double maxVALUE, final int pointerLength) {
		view.setFilters(new InputFilter[]{new InputFilter() {
			Pattern mPattern = Pattern.compile("([0-9]|\\.)*");

			//输入的最大金额
			private double MAX_VALUE = maxVALUE;
			//小数点后的位数
			private int POINTER_LENGTH = pointerLength;

			private final String POINTER = ".";

			private final String ZERO = "0";

			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				String sourceText = source.toString();
				String destText = dest.toString();
				//验证删除等按键
				if (TextUtils.isEmpty(sourceText)) {
					return "";
				}

				Matcher matcher = mPattern.matcher(source);
				//已经输入小数点的情况下，只能输入数字
				if (destText.contains(POINTER)) {
					if (!matcher.matches()) {
						return "";
					} else {
						if (POINTER.equals(source)) {  //只能输入一个小数点
							return "";
						}
					}
					//验证小数点精度，保证小数点后只能输入两位
					int index = destText.indexOf(POINTER);
					int length = destText.length() - index;
					//如果长度大于2，并且新插入字符在小数点之后
					if (length > POINTER_LENGTH && index < dstart) {
						//超出2位返回null
						return "";
					}
				} else {
					//没有输入小数点的情况下，只能输入小数点和数字，但首位不能输入小数点
					if (!matcher.matches()) {
						return "";
					} else {
						if ((POINTER.equals(source)) && TextUtils.isEmpty(destText)) {
							return "";
						}
					}
					if (view.getText().toString().startsWith(ZERO) && !sourceText.equals(POINTER)) {
						return "";
					}
				}
				//验证输入金额的大小
				double sumText = Double.parseDouble(destText + sourceText);
				if (sumText > MAX_VALUE) {
					return dest.subSequence(dstart, dend);
				}
				return dest.subSequence(dstart, dend) + sourceText;
			}
		}});
	}
}
