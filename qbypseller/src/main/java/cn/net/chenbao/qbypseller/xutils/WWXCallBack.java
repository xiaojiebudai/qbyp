package cn.net.chenbao.qbypseller.xutils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.ZLog;

import org.xutils.common.Callback.CommonCallback;

/**
 * 旺旺x3请求回掉
 * 
 * @author xl
 * @date 2016-8-1 下午10:15:59
 * @description
 */
public abstract class WWXCallBack implements CommonCallback<String> {

	public static final String TAG = "WWXCallBack";
	public static final String RESULT = "Result";
	/** 业务的数据字段 */
	private String modeKey;
	/** 不需要错误提示 */
	private boolean unUsePublicToast;


	public WWXCallBack(String modeKey) {
		super();
		this.modeKey = modeKey;
	}
	public WWXCallBack(String modeKey,boolean unUsePublicToast) {
		super();
		this.modeKey = modeKey;
		this.unUsePublicToast = unUsePublicToast;
	}

	@Override
	public void onCancelled(CancelledException arg0) {
	}

	@Override
	public void onError(Throwable arg0, boolean arg1) {
	}

	@Override
	public void onFinished() {
		onAfterFinished();
	}

	@Override
	public void onSuccess(String result) {

		ZLog.showPost(result);
		if (TextUtils.isEmpty(modeKey)) {
			onAfterSuccessOk(JSON.parseObject(result));
		} else {
			JSONObject data = JSON.parseObject(result).getJSONObject(
					modeKey + RESULT);
			if (data != null) {
				boolean success = data.getBooleanValue("Success");
				if (success) {
					onAfterSuccessOk(data);
				} else {
					onAfterSuccessError(data);

					switch (data.getIntValue("ErrCode")) {// 错误码,处理不同业务
						case 201:// session为空，以防万一（在需要使用session的时候就需要处理）
							WWApplication.getInstance().dealSessionPast();
							break;
					case 202:// session过期
						WWApplication.getInstance().dealSessionPast();
						break;
					default:
						if (!unUsePublicToast){
						WWToast.showShort(data.getString("Message"));// Toast错误信息
							 }
						break;
					}
				}
			}
		}
	}

	public abstract void onAfterSuccessOk(JSONObject data);

	public abstract void onAfterFinished();

	public void onAfterSuccessError(JSONObject data) {

	}
}
