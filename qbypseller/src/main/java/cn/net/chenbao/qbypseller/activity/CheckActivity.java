package cn.net.chenbao.qbypseller.activity;

import org.xutils.x;
import org.xutils.http.RequestParams;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.RealInfo;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.view.RegisterTimeLine;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import cn.net.chenbao.qbypseller.R;

/**
 * 商家审核
 * 
 * @author licheng
 * 
 */
public class CheckActivity extends FatherActivity implements OnClickListener {
	/** 通过 */
	public static final int MODE_PASS = 1;
	/** 未通过 */
	public static final int MODE_UNPASS = 2;
	/** 审核中 */
	public static final int MODE_CHECK = 3;
	public int mMode;
	private String sessionId;

	@Override
	protected int getLayoutId() {
		return R.layout.act_check;
	}

	@Override
	protected void initValues() {
		initDefautHead(R.string.business_register, true);
		mMode = getIntent().getIntExtra(Consts.KEY_MODULE, -1);
		sessionId = getIntent().getStringExtra(Consts.KEY_DATA);
		WWApplication.getInstance().setisHome(false);
	}

	@Override
	protected void initView() {
		RegisterTimeLine rt = (RegisterTimeLine) findViewById(R.id.rt);
		rt.setStep(4);
		switch (mMode) {
		case MODE_PASS:
			findViewById(R.id.tv_next).setOnClickListener(this);
			findViewById(R.id.tv_next).setVisibility(View.VISIBLE);
			findViewById(R.id.ll_pass).setVisibility(View.VISIBLE);
			break;
		case MODE_UNPASS:
			findViewById(R.id.ll_unpass).setVisibility(View.VISIBLE);
			findViewById(R.id.tv_input_again).setVisibility(View.VISIBLE);
			findViewById(R.id.tv_input_again).setOnClickListener(this);
			getRealInfo();
			break;
		case MODE_CHECK:
			findViewById(R.id.ll_checking).setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	private void getRealInfo() {
		RequestParams params = new RequestParams(ApiSeller.realInfoGet());
		params.addQueryStringParameter(Api.KEY_SESSIONID, sessionId);
		showWaitDialog();
		x.http().get(params, new WWXCallBack("RealInfoGet") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				RealInfo info = JSON.parseObject(
						data.getJSONObject(Api.KEY_DATA).toJSONString(),
						RealInfo.class);
				if (info != null) {
					((TextView) findViewById(R.id.fail_reason))
							.setText(info.Explain);
				}
			}
			@Override
			public void onAfterFinished() {
				dismissWaitDialog();
			}
		});
	}

	@Override
	protected void doOperate() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_input_again:
			if (sessionId != null) {
				// 更改认证状态状态
				showWaitDialog();
				JSONObject jsonObject = new JSONObject();
				jsonObject.put(Consts.KEY_SESSIONID, sessionId);
				x.http().post(
						ParamsUtils.getPostJsonParams(jsonObject,
								ApiSeller.regInfoRewrite()),
						new WWXCallBack("RegInfoRewrite") {

							@Override
							public void onAfterSuccessOk(JSONObject data) {
								PublicWay.startBaseInfoActivity(CheckActivity.this, sessionId);
								finish();
							}

							@Override
							public void onAfterFinished() {
								 dismissWaitDialog();
							}
						});
			}
			break;
		case R.id.tv_next:// 下一步
			if (sessionId != null) {
				WWApplication.getInstance().setSessionId(sessionId);
				startActivity(new Intent(CheckActivity.this, MainActivity.class));
				setResult(RESULT_OK);
				finish();
			}
			break;
		default:
			break;
		}
	}

}
