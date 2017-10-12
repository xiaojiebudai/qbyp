package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.jpush.android.api.JPushInterface;
import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;

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
		iv_botton=(ImageView)findViewById(R.id.iv_botton);
		int width= DensityUtil.getScreenWidth(this);
		ViewGroup.LayoutParams layoutParams=iv_botton.getLayoutParams();
		layoutParams.height=width;
		layoutParams.width=width;
		iv_botton.setLayoutParams(layoutParams);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}
//TODO 引导页暂时没更换 暂时关闭 图片 splash0  splash1
//				if(SharedPreferenceUtils.getInstance().getSplash()&&SharedPreferenceUtils.getInstance().getAppVersion()>= PublicWay.getVersionCode(WelcomeActivity.this)){
					startActivity(new Intent(WelcomeActivity.this,
							MainActivity.class));
//				}else{
//					startActivity(new Intent(WelcomeActivity.this,
//							SplashActivity.class));
//				}
				finish();
			}
		}).start();
	}

	@Override
	protected void doOperate() {
		// TODO Auto-generated method stub

	}

}
