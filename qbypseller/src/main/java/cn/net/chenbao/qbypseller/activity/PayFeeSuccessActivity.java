package cn.net.chenbao.qbypseller.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.utils.Consts;


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
    @Override
    protected int getLayoutId() {
        return R.layout.act_pay_fee_success;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.pay_result, true);
    }

    @Override
    protected void initView() {
        tv_time = (TextView) findViewById(R.id.tv_time);
        ll_success = findViewById(R.id.ll_success);
        mode = getIntent().getIntExtra(Consts.KEY_MODULE, SUCCESS);
    }

    @Override
    protected void doOperate() {
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

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.arg1 == 0) {
                timer.cancel();
             finish();
            }
            if (msg.what == 1) {
                tv_time.setText("" + msg.arg1);
            }
        }
    };
}
