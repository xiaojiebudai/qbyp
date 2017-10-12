package cn.net.chenbao.qbyp.activity;

import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.bean.RedPacket;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.DecimalFormat;

public class RedPacketActivity extends FatherActivity {
    private int bonusCount;
    private RelativeLayout rl_open;
    private TextView tv_num, tv_red_money, tv_description, tv_tips;
    private ImageView iv_open;
    private AnimationDrawable animationDrawable;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_red_packet;
    }

    @Override
    protected void initValues() {
        bonusCount = getIntent().getIntExtra(Consts.KEY_DATA, 0);
    }

    @Override
    protected void initView() {
        findViewById(R.id.view_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rl_open = (RelativeLayout) findViewById(R.id.rl_open);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_red_money = (TextView) findViewById(R.id.tv_red_money);
        tv_description = (TextView) findViewById(R.id.tv_description);
        tv_tips = (TextView) findViewById(R.id.tv_tips);
        tv_num.setText(String.format(getString(R.string.red_packet), bonusCount));

        iv_open = (ImageView) findViewById(R.id.iv_open);
        animationDrawable = (AnimationDrawable) iv_open.getBackground();

        rl_open.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (bonusCount > 0) {
                    if (!animationDrawable.isRunning()) {
                        animationDrawable.start();
                        iv_open.postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                animationDrawable.stop();
                                openRedPacketInfo();
                            }
                        }, 2800);
                    }
                } else {
                    WWToast.showShort(R.string.no_red_package_open);
                }

            }
        });
    }

    @Override
    protected void doOperate() {
    }

    DecimalFormat df = new DecimalFormat("#0.00");

    private void openRedPacketInfo() {
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        RequestParams params = ParamsUtils.getPostJsonParams(jsonObject,
                ApiUser.BonusOpen());
        x.http().post(params, new WWXCallBack("BonusOpen") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                RedPacket packet = JSON.parseObject(data.getString("Data"),
                        RedPacket.class);
                bonusCount = data.getIntValue("TotalCount");
                tv_num.setText(String.format(getString(R.string.red_packet), bonusCount));
                tv_red_money.setText(WWViewUtil.numberFormatPrice(packet.BonusAmt));
                tv_description.setText(packet.BonusName);
                tv_tips.setText(String.format(getString(R.string.red_packet_position), packet.AccountName));
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }

}
