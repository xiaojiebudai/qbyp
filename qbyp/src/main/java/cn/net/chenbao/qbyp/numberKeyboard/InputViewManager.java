package cn.net.chenbao.qbyp.numberKeyboard;

import cn.net.chenbao.qbyp.numberKeyboard.KeyboardEnum.ActionEnum;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import cn.net.chenbao.qbyp.R;

public class InputViewManager implements OnClickListener, OnLongClickListener {

	View one;
	View two;
	View three;
	View four;
	View five;
	View six;
	View seven;
	View eight;
	View nine;
	View zero;
	View delete;
	PasswordView mPasswordView;
	StringBuffer mPassword;
	private int mMax_input;

	public InputViewManager(View keyboard, PasswordView passwordView, int max) {
		one = keyboard.findViewById(R.id.pay_keyboard_one);
		one.setOnClickListener(this);
		mPassword = new StringBuffer();
		two = keyboard.findViewById(R.id.pay_keyboard_two);
		two.setOnClickListener(this);
		three = keyboard.findViewById(R.id.pay_keyboard_three);
		three.setOnClickListener(this);
		four = keyboard.findViewById(R.id.pay_keyboard_four);
		four.setOnClickListener(this);
		five = keyboard.findViewById(R.id.pay_keyboard_five);
		five.setOnClickListener(this);
		six = keyboard.findViewById(R.id.pay_keyboard_sex);
		six.setOnClickListener(this);
		seven = keyboard.findViewById(R.id.pay_keyboard_seven);
		seven.setOnClickListener(this);
		eight = keyboard.findViewById(R.id.pay_keyboard_eight);
		eight.setOnClickListener(this);
		nine = keyboard.findViewById(R.id.pay_keyboard_nine);
		nine.setOnClickListener(this);
		zero = keyboard.findViewById(R.id.pay_keyboard_zero);
		zero.setOnClickListener(this);
		delete = keyboard.findViewById(R.id.pay_keyboard_del);
		delete.setOnClickListener(this);
		delete.setOnLongClickListener(this);
		mPasswordView = passwordView;
		mMax_input = max;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pay_keyboard_one:
			parseActionType(KeyboardEnum.one);
			break;
		case R.id.pay_keyboard_two:
			parseActionType(KeyboardEnum.two);
			break;
		case R.id.pay_keyboard_three:
			parseActionType(KeyboardEnum.three);
			break;
		case R.id.pay_keyboard_four:
			parseActionType(KeyboardEnum.four);
			break;
		case R.id.pay_keyboard_five:
			parseActionType(KeyboardEnum.five);
			break;
		case R.id.pay_keyboard_sex:
			parseActionType(KeyboardEnum.sex);
			break;
		case R.id.pay_keyboard_seven:
			parseActionType(KeyboardEnum.seven);
			break;
		case R.id.pay_keyboard_eight:
			parseActionType(KeyboardEnum.eight);
			break;
		case R.id.pay_keyboard_nine:
			parseActionType(KeyboardEnum.nine);
			break;
		case R.id.pay_keyboard_zero:
			parseActionType(KeyboardEnum.zero);
			break;
		case R.id.pay_keyboard_del:
			parseActionType(KeyboardEnum.del);
			break;

		default:
			break;
		}
	}

	private void parseActionType(KeyboardEnum keyboardEnum) {
		if (keyboardEnum.getType() == ActionEnum.add) {
			if (mPassword.length() < mMax_input) {
				mPassword.append(keyboardEnum.getValue());
				mPasswordView.setText(mPassword);
//				if (mPassword.length() == mMax_input) {
//					if (mListener != null) {
						mListener.onInputFinish(mPassword.toString());
//					}
//				}
			}
		} else if (keyboardEnum.getType() == ActionEnum.delete) {
			if (mPassword.length() > 0) {
				mPassword.deleteCharAt(mPassword.length() - 1);
				mPasswordView.setText(mPassword);
				mListener.onInputFinish(mPassword.toString());
			}
		} else if (keyboardEnum.getType() == ActionEnum.longClick) {
			mPassword.delete(0, mPassword.length());
			mPasswordView.setText(mPassword);
			mListener.onInputFinish(mPassword.toString());
		}
	}

	private OnInputListener mListener;

	public void setOnInputListener(OnInputListener listener) {
		mListener = listener;
	}

	/**
	 * 清楚记录
	 */
	public void clear() {
		mPassword.delete(0, mMax_input);
		if (mPasswordView != null) {
			mPasswordView.setText(mPassword);
		}
	}

	public interface OnInputListener {
		void onInputFinish(String s);

		void onInputCancel();
	}

	@Override
	public boolean onLongClick(View arg0) {
		parseActionType(KeyboardEnum.longdel);
		return true;
	}
}
