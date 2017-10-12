package cn.net.chenbao.qbypseller.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import cn.net.chenbao.qbypseller.R;

/**
 * 赠送积分
 * Created by Administrator on 2017/2/13.
 */

public class PresentIntegralDialog extends Dialog {
    private TextView tv_order_num,tv_zengsongjinge,tv_rangli,tv_ok;
    private RadioGroup rgJifen;
    private double bili = 0.99;
    private double num = 0;
    private DecimalFormat df;

    public PresentIntegralDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.dialog_present_intergral);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
        getWindow().setAttributes(attributes);
        df = new DecimalFormat("######0.00");
        df.setRoundingMode(RoundingMode.DOWN);
        initView();
    }

    private void initView() {
        tv_order_num = (TextView) findViewById(R.id.tv_order_num);
        tv_zengsongjinge = (TextView) findViewById(R.id.tv_zengsongjinge);
        tv_rangli = (TextView) findViewById(R.id.tv_rangli);
        tv_ok = (TextView) findViewById(R.id.tv_ok);
        rgJifen = (RadioGroup) findViewById(R.id.rg_jifen);
        rgJifen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_0:
                        bili = 0.20;
                        setJifenAndRangli(num, bili);
                        break;
                    case R.id.rb_1:
                        bili = 0.48;
                        setJifenAndRangli(num, bili);
                        break;
                    case R.id.rb_2:
                        bili = 0.99;
                        setJifenAndRangli(num, bili);
                        break;
                }
            }
        });
    }

    /**
     * 设置积分和让利
     *
     * @param num
     * @param bili
     */
    private void setJifenAndRangli(double num, double bili) {
        if (bili == 0.2) {
            tv_zengsongjinge.setText(df.format((num * bili)) + "");
            tv_rangli.setText(df.format((num * 0.05)) + "");
        } else if (bili == 0.48) {
            tv_zengsongjinge.setText(df.format((num * bili)) + "");
            tv_rangli.setText(df.format((num * 0.10)) + "");
        } else {
            tv_zengsongjinge.setText(df.format((num * bili)) + "");
            tv_rangli.setText(df.format((num * 0.20)) + "");
        }
    }
    public TextView getTv_ok() {
        return tv_ok;
    }
    public  void setNum(double num){
        this.num=num;
        tv_order_num.setText(df.format(num));
        setJifenAndRangli(num, bili);
    }
    public double getBili() {
        return bili;
    }
}
