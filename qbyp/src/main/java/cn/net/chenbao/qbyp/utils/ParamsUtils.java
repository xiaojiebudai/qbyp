package cn.net.chenbao.qbyp.utils;

import cn.net.chenbao.qbyp.WWApplication;

import org.xutils.http.RequestParams;

import com.alibaba.fastjson.JSONObject;

public class ParamsUtils {
	/**
	 * post传json
	 * 
	 * @param jsonObject
	 * @param url
	 * @return
	 */
	public static RequestParams getPostJsonParams(JSONObject jsonObject,
			String url) {
		RequestParams params = new RequestParams(url);
		params.setAsJsonContent(true);
		params.setBodyContent(jsonObject.toString());
		return params;
	}
	/** 只带session参数的请求 */
	public static RequestParams getSessionParams(String api) {
		RequestParams params = new RequestParams(api);
		params.addQueryStringParameter(Consts.KEY_SESSIONID, WWApplication
				.getInstance().getSessionId());
		return params;
	}
}
