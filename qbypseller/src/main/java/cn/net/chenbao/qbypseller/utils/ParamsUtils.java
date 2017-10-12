package cn.net.chenbao.qbypseller.utils;

import java.io.File;

import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiBaseData;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.Goods;

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

	/** 获取对应类目的商品列表 */
	public static RequestParams getGoodsGet(long classId, int pageIndex) {
		RequestParams params = new RequestParams(ApiSeller.goodsGet());
		if (classId > 0) {// 全部和下架不传类目id
			params.addQueryStringParameter("classId", classId + "");
			params.addQueryStringParameter("status", Goods.STATE_ONSALE + "");
		}
		params.addQueryStringParameter(Api.KEY_PAGE_INDEX, pageIndex + "");
		params.addQueryStringParameter(Api.KEY_PAGE_SIZE, Api.DEFAULT_PAGE_SIZE);
		params.addQueryStringParameter(Api.KEY_SESSIONID, WWApplication
				.getInstance().getSessionId());
		return params;
	}

	/** 只带session参数的请求 */
	public static RequestParams getSessionParams(String api) {
		RequestParams params = new RequestParams(api);
		params.addQueryStringParameter(Api.KEY_SESSIONID, WWApplication
				.getInstance().getSessionId());
		return params;
	}

	public static RequestParams getSessionParamsPostJson(String api) {
		RequestParams params = new RequestParams(api);
		JSONObject object = new JSONObject();
		object.put(Api.KEY_SESSIONID, WWApplication.getInstance()
				.getSessionId());
		params.setAsJsonContent(true);
		params.setBodyContent(object.toString());
		return params;
	}

	/** 带session的json post参数 */
	public static RequestParams getPostJsonParamsWithSession(
			JSONObject jsonObject, String url) {
		RequestParams params = new RequestParams(url);
		jsonObject.put(Api.KEY_SESSIONID, WWApplication.getInstance()
				.getSessionId());
		params.setAsJsonContent(true);
		params.setBodyContent(jsonObject.toString());
		return params;
	}

	/** 提交图片参数 */
	public static RequestParams getPostImageParams(String url) {
		RequestParams params = new RequestParams(ApiBaseData.upImage());
		params.addBodyParameter(Api.KEY_FILE, new File(url));
		params.setMultipart(true);
		return params;
	}

	/** 带page的get请求 */
	public static RequestParams getPageGetParams(String url, int page) {
		RequestParams params = new RequestParams(url);
		params.addQueryStringParameter("pageSize", 20 + "");
		params.addQueryStringParameter("pageIndex", page + "");
		return params;
	}
}
