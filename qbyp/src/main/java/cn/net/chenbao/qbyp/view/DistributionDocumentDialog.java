package cn.net.chenbao.qbyp.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import cn.net.chenbao.qbyp.R;

/**
 * Created by ZXJ on 2017/6/17.
 */

public class DistributionDocumentDialog extends Dialog {
    private Context context;
    private TextView mButton_right;
    private TextView mContent;
    private TextView tv_document;
    private ImageView iv_checkbox;
    private boolean ischeck;

    public DistributionDocumentDialog(Context context) {
        super(context, R.style.DialogStyle);
        this.context = context;
        setContentView(R.layout.dialog_distribution_document);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.7);
        getWindow().setAttributes(attributes);
        mButton_right = (TextView) findViewById(R.id.tv_right);
        mContent = (TextView) findViewById(R.id.tv_content);
        tv_document = (TextView) findViewById(R.id.tv_document);
        iv_checkbox = (ImageView) findViewById(R.id.iv_checkbox);
        iv_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCheck(!ischeck);
            }
        });
    }

    public void setContentText(String info) {
        mContent.setText(info);
    }

    public void setDocumentText(String document) {
        tv_document.setText(document);
    }

    public boolean getIsCheck() {
        return ischeck;
    }

    public void setCheck(boolean ischeck) {
        this.ischeck = ischeck;
        iv_checkbox.setImageResource(ischeck ? R.drawable.shoppingcat_pitchon_icon : R.drawable.shoppingcat_unchecked_icon);
    }

    public void setRightButtonCilck(android.view.View.OnClickListener listener) {
        mButton_right.setOnClickListener(listener);
    }

    public void setDocumentCilck(android.view.View.OnClickListener listener) {
        tv_document.setOnClickListener(listener);
    }

}
