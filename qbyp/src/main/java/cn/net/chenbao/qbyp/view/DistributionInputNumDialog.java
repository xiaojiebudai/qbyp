package cn.net.chenbao.qbyp.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.distribution.been.DistributionProduct;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

/**
 * Created by ppsher on 2017/4/18.
 */

public class DistributionInputNumDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private EditText et_num;
    private DistributionProduct FenXiao;
    private TextView tv_total_money;
    private TextView tv_total_integral;
    private double salePrice;
    private double integral;
    private int totalNum;

    public DistributionInputNumDialog(@NonNull Context context, DistributionProduct FenXiao) {
        super(context, R.style.DialogStyle);
        this.context = context;
        if (FenXiao != null) {
            this.FenXiao = FenXiao;
            this.salePrice = FenXiao.SalePrice;
            this.integral = FenXiao.ConsumeNum;
        }
        setContentView(R.layout.dialog_input_num);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.85);
        getWindow().setAttributes(attributes);
        initView();
    }

    private void initView() {
        findViewById(R.id.num_jia).setOnClickListener(this);
        findViewById(R.id.num_jian).setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_ok).setOnClickListener(this);
        et_num = (EditText) findViewById(R.id.et_num);
        totalNum = Integer.parseInt(et_num.getText().toString().trim());
        tv_total_money = (TextView) findViewById(R.id.tv_total_money);
        tv_total_money.setText(WWViewUtil.numberFormatPrice(salePrice));
        tv_total_integral = (TextView) findViewById(R.id.tv_total_integral);
        tv_total_integral.setText(WWViewUtil.numberFormatWithTwo(integral));

        et_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    modifiNum(false, false);
                } else {
                    totalNum = 0;
                    tv_total_money.setText("¥0.00");
                    tv_total_integral.setText("0.00");
                }

            }
        });

        et_num.setFocusable(true);
        et_num.setFocusableInTouchMode(true);
        et_num.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void modifiNum(boolean isSub, boolean isAdd) {
        String s = et_num.getText().toString().trim();
        totalNum = Integer.parseInt(s);
        if (isAdd) {
            totalNum = totalNum + 1;
        }
        if (isSub) {
            totalNum = totalNum - 1;
        }

//        tv_total_integral.setText(WWViewUtil.numberFormatWithTwo(totalNum * integral));
//        if (totalNum >= FenXiao.AgentNum1) {
//            tv_total_money.setText(WWViewUtil.numberFormatPrice(totalNum * FenXiao.AgentPrice1));
//        } else if (totalNum >= FenXiao.AgentNum2 && totalNum < FenXiao.AgentNum1) {
//            tv_total_money.setText(WWViewUtil.numberFormatPrice(totalNum * FenXiao.AgentPrice2));
//        } else {
//            tv_total_money.setText(WWViewUtil.numberFormatPrice(totalNum * salePrice));
//        }
    }

    private int initValue = -1;

    public void setInitValue(int value) {
        initValue = value;
        totalNum = value;
        et_num.setText(value + "");
        et_num.setSelection(et_num.getEditableText().length());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.num_jian:
                if (totalNum > 1) {
                    modifiNum(true, false);
                    et_num.setText(totalNum + "");
                    et_num.setSelection(et_num.getEditableText().length());
                }
                break;
            case R.id.num_jia:
                if (totalNum == 9999) {
                    return;
                }
                if (et_num.getEditableText().length() != 0) {
                    modifiNum(false, true);
                    et_num.setText(totalNum + "");
                    et_num.setSelection(et_num.getEditableText().length());
                } else {
                    totalNum = 1;
                    et_num.setText(totalNum + "");
                    et_num.setSelection(et_num.getEditableText().length());
                    tv_total_money.setText(WWViewUtil.numberFormatPrice(totalNum * salePrice));
                    tv_total_integral.setText(WWViewUtil.numberFormatWithTwo(totalNum * integral));
                }

                break;
            case R.id.tv_cancel:
                hideInput();
                dismiss();
                break;
            case R.id.tv_ok:
                if (totalNum == 0) {
                    WWToast.showShort(R.string.buy_number_cannot_0);
                    return;
                }
                if (makesureListener != null) {
                    hideInput();
                    dismiss();
                    if (initValue != totalNum) {
                        makesureListener.onClickOk(totalNum);
                    }
                }
                break;
        }
    }

    private void hideInput() {
        InputMethodManager m = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        m.hideSoftInputFromWindow(et_num.getWindowToken(), 0);
    }

    private MakesureListener makesureListener;

    public interface MakesureListener {
        void onClickOk(int quantity);
    }

    public void setMakesureListener(MakesureListener makesureListener) {
        this.makesureListener = makesureListener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //触摸弹窗外部
        if (isOutOfBounds(getContext(), event)) {
            hideInput();
        }
        return super.onTouchEvent(event);
    }

    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }

}
