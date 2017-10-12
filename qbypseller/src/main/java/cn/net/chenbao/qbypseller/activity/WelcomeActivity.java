package cn.net.chenbao.qbypseller.activity;

import cn.jpush.android.api.JPushInterface;
import cn.net.chenbao.qbypseller.WWApplication;

import android.content.Intent;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.net.chenbao.qbypseller.utils.DensityUtil;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.SharedPreferenceUtils;

public class WelcomeActivity extends FatherActivity {
    private static final int sleepTime = 2000;
    private ImageView iv_botton;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }


    @Override
    protected void initValues() {
        JPushInterface.init(getApplicationContext());
    }
    @Override
    protected void setStatusBar() {
        toTop(this);
    }

    @Override
    protected void initView() {
        iv_botton = (ImageView) findViewById(R.id.iv_botton);
        int width = DensityUtil.getScreenWidth(this);
        ViewGroup.LayoutParams layoutParams = iv_botton.getLayoutParams();
        layoutParams.height = width;
        layoutParams.width = width;
        iv_botton.setLayoutParams(layoutParams);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                }
                //TODO 引导页没换  hide掉
//                if(SharedPreferenceUtils.getInstance().getSplash()&&SharedPreferenceUtils.getInstance().getAppVersion()>= PublicWay.getVersionCode(WelcomeActivity.this)){
                    if (TextUtils.isEmpty(WWApplication.getInstance()
                            .getSessionId())) {
                        startActivity(new Intent(WelcomeActivity.this,
                                BusinessLoginActivity.class));
                    } else {
                        startActivity(new Intent(WelcomeActivity.this,
                                MainActivity.class));
                    }
//                }else{
//                    startActivity(new Intent(WelcomeActivity.this,
//                            SplashActivity.class));
//                }


                finish();
            }
        }).start();

    }

    @Override
    protected void doOperate() {

    }

}
