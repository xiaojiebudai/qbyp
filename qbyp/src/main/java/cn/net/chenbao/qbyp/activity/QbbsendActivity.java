package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.PhoneInfo;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.GetPhoneInfo;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

/**
 * Created by admin on 2017/10/1.
 * 赠送柒宝币
 */

public class QbbsendActivity extends FatherActivity {
    public String InternalBalance;
    @BindView(R.id.tv_qibaobinum)
    TextView tvQibaobinum;
    @BindView(R.id.ed_username)
    EditText edUsername;
    @BindView(R.id.iv_select)
    ImageView ivSelect;
    @BindView(R.id.ed_num)
    EditText edNum;
    @BindView(R.id.tv_ok)
    TextView tvOk;

    @Override
    protected int getLayoutId() {
        return R.layout.act_qbbsend;
    }

    @Override
    protected void initValues() {
        initDefautHead("柒宝币转增", true);
        InternalBalance = getIntent().getStringExtra(Consts.KEY_DATA);
    }

    @Override
    protected void initView() {
        tvQibaobinum.setText(InternalBalance);
    }

    @Override
    protected void doOperate() {

    }

    @OnClick({R.id.iv_select, R.id.tv_ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_select:
                // 选择联系人
                Intent it = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                this.startActivityForResult(it, 2);
                break;
            case R.id.tv_ok:
                String mobile=edUsername.getText().toString().trim();
                String num=edNum.getText().toString().trim();
                if(TextUtils.isEmpty(mobile)){
                    WWToast.showShort("请输入会员手机号");
                    return;
                }else if(TextUtils.isEmpty(num)){
                    WWToast.showShort("请输入转赠数量");
                    return;
                }else{
                    showWaitDialog();

                    JSONObject object=new JSONObject();

                    object.put("sessionId", WWApplication.getInstance()
                            .getSessionId());
                    object.put("mobile", mobile);
                    object.put("num", num);


                    x.http().post(ParamsUtils.getPostJsonParams(object,ApiUser.QbbGive()), new WWXCallBack("QbbGive") {

                        @Override
                        public void onAfterFinished() {
                            dismissWaitDialog();
                        }

                        @Override
                        public void onAfterSuccessOk(JSONObject data) {
                            WWToast.showShort("转赠成功");
                            finish();
                        }
                    });
                }
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if (requestCode == 2&&resultCode == RESULT_OK) {

                Uri contactData = data.getData();
                Cursor cursor = getContentResolver().query(contactData, null,
                        null, null, null);
                cursor.moveToFirst();
                PhoneInfo phonenum = GetPhoneInfo.getContactPhone(cursor, this);
                if (phonenum != null) {
                    edUsername.setText(phonenum.getPhoneNum().replace(" ", ""));
                }
        }
    }

}
