package cn.net.chenbao.qbypseller.wxapi;

import cn.net.chenbao.qbypseller.utils.Constants;
import cn.net.chenbao.qbypseller.utils.Consts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 微信支付回调界面
 * 
 * @author xl
 * 
 * @version 创建时间：2015-12-12 上午11:53:32
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "WXPayEntryActivity";
	public static final String WXPAYRESULT = "WXPayResult";
	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			Intent intent = new Intent(WXPAYRESULT);
			intent.putExtra(Consts.KEY_DATA, resp.errCode);
			sendBroadcast(intent);
			finish();
		}
	}

}