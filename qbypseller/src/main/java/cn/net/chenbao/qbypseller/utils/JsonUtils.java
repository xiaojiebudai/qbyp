package cn.net.chenbao.qbypseller.utils;

import com.alibaba.fastjson.JSONObject;

/**
 * json工具类
 * 
 * @author licheng
 * 
 */
public class JsonUtils {

	public static boolean parseJsonSuccess(JSONObject jsonObject) {
		return jsonObject.getBooleanValue("Success");
	}

	public static int parserJsonErrcode(JSONObject jsonObject) {
		return jsonObject.getIntValue("ErrCode");
	}

	public static String parserJsonMessage(JSONObject jsonObject) {
		return jsonObject.getString("Message");
	}

	public static String parserJsonDataArray(JSONObject jsonObject) {
		return jsonObject.getJSONArray("Data").toJSONString();
	}

	public static int parserTotalCount(JSONObject jsonObject) {
		return jsonObject.getIntValue("TotalCount");
	}
}
