package cn.net.chenbao.qbypseller.wxapi;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.utils.Constants;
import cn.net.chenbao.qbypseller.utils.WWToast;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


/**
 * 微信分享回调
 * 
 * @author licheng
 *
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false);
		api.handleIntent(getIntent(), this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onReq(BaseReq arg0) {
	}

	@Override
	public void onResp(BaseResp resp) {
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			// 分享成功
			WWToast.showShort(R.string.share_success);
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			WWToast.showShort(R.string.share_cancel);
			// 分享取消
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			WWToast.showShort(R.string.share_fail);
			// 分享拒绝
			break;
		}
		finish();
	}
}