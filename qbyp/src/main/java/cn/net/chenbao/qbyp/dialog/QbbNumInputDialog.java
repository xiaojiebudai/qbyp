package cn.net.chenbao.qbyp.dialog;


import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

    /***
     * Description:输入邀请人dialog Company: Zhubaoyi Version：2.0
     *
     * @title CertificateInputDialog.java
     * @author ZXJ
     * @date 2016-7-29
     ***/
    public class QbbNumInputDialog extends Dialog {
        private EditText ed_comment;
        private TextView  tv_ok,tv_title,tv_0,tv_1;
        private double price=0;
        private String info="";
        private String okText="";

        public QbbNumInputDialog(Context context,@NonNull double price,@NonNull String info,@NonNull String okText) {
            super(context,  R.style.DialogStyle);
            setContentView(R.layout.dialog_qbbinput);
            LayoutParams attributes = getWindow().getAttributes();
            attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
            getWindow().setAttributes(attributes);
            this.info=info;
            this.okText=okText;
            this.price=price;
            initView();
        }

        private void initView() {
            ed_comment = (EditText) findViewById(R.id.ed_comment);
            tv_ok = (TextView) findViewById(R.id.tv_ok);
            tv_title = (TextView) findViewById(R.id.tv_title);
            tv_0 = (TextView) findViewById(R.id.tv_0);
            tv_1 = (TextView) findViewById(R.id.tv_1);

            findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });


            tv_0.setText(info);
            tv_ok.setText(okText);
            tv_title.setText("柒宝币今日价值 ￥"+price+"枚");
            ed_comment.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    String num=ed_comment.getText().toString().trim();
                if(TextUtils.isEmpty(num)){
                    tv_1.setText("￥0.00");
                }else{
                    tv_1.setText(WWViewUtil.numberFormatWithTwo(Integer.parseInt(num)*price));
                }
                }
            });


        }


        public int getInput() {
            if(TextUtils.isEmpty(ed_comment.getText().toString().trim())){
                WWToast.showShort("请输入数量");
                return 0;
            }else
            {
            return Integer.parseInt(ed_comment.getText().toString().trim());
            }
        }
        public TextView getTv_ok() {
            return tv_ok;
        }
    }
