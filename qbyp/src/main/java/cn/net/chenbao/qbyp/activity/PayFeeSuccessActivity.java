package cn.net.chenbao.qbyp.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.PublicWay;

import java.util.Timer;
import java.util.TimerTask;

import cn.net.chenbao.qbyp.R;

/**
 * Created by ppsher on 2017/2/14.
 */

public class PayFeeSuccessActivity extends FatherActivity {

    public static final int FAILE = -1;
    public static final int SUCCESS = 1;
    private TextView tv_time;
    private int mode;
    private int i = 3;
    private Timer timer;
    private View ll_success;
    private View ll_fail;
    private View tv_pay_again;
    private double money;
    private long orderId;

    @Override
    protected int getLayoutId() {
        return R.layout.act_pay_fee_success;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.service_fee_pay, true);
    }

    @Override
    protected void initView() {
        tv_time = (TextView) findViewById(R.id.tv_time);
        ll_success = findViewById(R.id.ll_success);
        ll_fail = findViewById(R.id.ll_fail);
        tv_pay_again = findViewById(R.id.tv_pay_again);
        mode = getIntent().getIntExtra(Consts.KEY_MODULE, SUCCESS);
    }

    @Override
    protected void doOperate() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mode == SUCCESS) {
            timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    Message message = mHandler.obtainMessage();
                    message.what = 1;
                    message.arg1 = --i;
                    mHandler.sendMessage(message);
                }
            }, 1000, 1000);
        }
//        else {
//            ll_success.setVisibility(View.GONE);
//            ll_fail.setVisibility(View.VISIBLE);
//            tv_pay_again.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.arg1 == 0) {
                timer.cancel();
                PublicWay.startMainActivity(PayFeeSuccessActivity.this, MainActivity.TAB_PERSONCENTER);
            }
            if (msg.what == 1) {
                tv_time.setText("" + msg.arg1);
            }
        }
    };

}
