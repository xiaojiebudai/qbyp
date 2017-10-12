package cn.net.chenbao.qbyp.activity;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import com.alibaba.fastjson.JSONObject;
import cn.net.chenbao.qbyp.R;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IdentityAuthenticationResultActivity extends FatherActivity
		implements OnClickListener {
	private int model;
	/** 认证中 */
	public static final int IDENTITYING = 0;
	/** 认证失败 */
	public static final int IDENTITYDEFAULT = 1;
	private LinearLayout ll_default, ll_odentity_ing;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.replay:
			showWaitDialog();
			JSONObject object = new JSONObject();
			object.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
					.getSessionId());
			RequestParams params = ParamsUtils.getPostJsonParams(object,
					ApiUser.RealRewrite());
			x.http().post(params, new WWXCallBack("RealRewrite") {

				@Override
				public void onAfterSuccessOk(JSONObject data) {
					Intent intent = new Intent(
							IdentityAuthenticationResultActivity.this,
							IdentityAuthenticationActivity.class);
					intent.putExtra(Consts.KEY_MODULE,
							IdentityAuthenticationActivity.REPLAY);
					startActivity(intent);
					finish();
				}

				@Override
				public void onAfterFinished() {
					dismissWaitDialog();
				}
			});

			break;
		case R.id.to_index:
			Intent intent1 = new Intent(
					IdentityAuthenticationResultActivity.this,
					MainActivity.class);
			intent1.putExtra("indexSelect", true);
			startActivity(intent1);
			finish();
			break;
		case R.id.to_person:
			Intent intent = new Intent(
					IdentityAuthenticationResultActivity.this,
					MainActivity.class);
			intent.putExtra("personCenterSelect", true);
			startActivity(intent);
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_identity_result;
	}

	@Override
	protected void initValues() {
		model = getIntent().getIntExtra(Consts.KEY_MODULE, IDENTITYING);
	}

	@Override
	protected void initView() {
		ll_default = (LinearLayout) findViewById(R.id.ll_default);
		ll_odentity_ing = (LinearLayout) findViewById(R.id.ll_odentity_ing);
		if (model == IDENTITYING) {
			initDefautHead(R.string.identity_ing, true);
			ll_default.setVisibility(View.GONE);
			ll_odentity_ing.setVisibility(View.VISIBLE);
		} else {
			initDefautHead(R.string.identity_default, true);
			ll_default.setVisibility(View.VISIBLE);
			ll_odentity_ing.setVisibility(View.GONE);
			((TextView) findViewById(R.id.tv_message)).setText(getIntent()
					.getStringExtra(Consts.KEY_DATA));
		}
		findViewById(R.id.replay).setOnClickListener(this);
		findViewById(R.id.to_index).setOnClickListener(this);
		findViewById(R.id.to_person).setOnClickListener(this);

	}

	@Override
	protected void doOperate() {

	}

}
