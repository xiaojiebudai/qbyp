package cn.net.chenbao.qbypseller.utils;

import java.util.ArrayList;
import java.util.List;

import cn.net.chenbao.qbypseller.bean.Category;
import cn.net.chenbao.qbypseller.bean.SellerInfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * 获取SharedPreference数据的工具类
 * 
 * @author lc
 * 
 */
public class SharedPreferenceUtils {
	private static final String USER_INFO = "shared_save_info";

	private static SharedPreferenceUtils utils;
	private static SharedPreferences mSharedPreferences;
	private static SharedPreferences.Editor editor;

	/** 登录的userId */
	private static String SHARED_KEY_LOGIN_ID = "login_id";
	/** 热门搜索 */
	private static String SHARED_KEY_HOT_SEARCH = "hot_search";
	/** 賣家信息 */
	private static String SHARED_KEY_SELLER_INFO = "seller_info";
	/** 卖家id */
	private static String SHARED_KEY_SELLER_ID = "seller_id";
	/** 保存电话号码 */
	private static String SHARED_KEY_PHONE_NUM = "phone_num";
	/** 类目 */
	private static String SHARED_KEY_CATEGORIES = "categories";
	/** 引导页 */
	private static String SHARED_KEY_SPLASH= "splash";
	/** APP版本*/
	private static String SHARED_KEY_APPVERSION= "app_version";
	/** 用户密码*/
	private static String SHARED_KEY_USER_PSW= "user_psw";
	private SharedPreferenceUtils(Context cxt) {
		mSharedPreferences = cxt.getSharedPreferences(USER_INFO,
				Context.MODE_PRIVATE);
		editor = mSharedPreferences.edit();
	}

	public SharedPreferenceUtils() {
	}

	public static synchronized void init(Context cxt) {
		if (utils == null) {
			utils = new SharedPreferenceUtils(cxt);
		}
	}

	public synchronized static SharedPreferenceUtils getInstance() {
		if (utils == null) {
			throw new RuntimeException("please init first!");
		}
		return utils;
	}

	/**
	 * 保存绘画id
	 * 
	 * @param userId
	 */
	public void saveSessionId(String userId) {
		editor.putString(SHARED_KEY_LOGIN_ID, userId);
		editor.commit();
	}

	/**
	 * 获取会话id
	 * 
	 * @return
	 */
	public String getSessionId() {
		return mSharedPreferences.getString(SHARED_KEY_LOGIN_ID, "");
	}

	/**
	 * 保存搜索记录
	 *
	 * @param condition
	 */
	public void saveSearchHistory(String condition) {
		condition = condition.trim();
		String history = mSharedPreferences
				.getString(SHARED_KEY_HOT_SEARCH, "");
		String[] hisArrays = history.split(",");
		for (int i = 0; i < hisArrays.length; i++) {
			if (hisArrays[i].equals(condition)) {// 已经有了
				return;
			}
		}
		StringBuilder sb = new StringBuilder(history);
		sb.insert(0, condition + ",");
		editor.putString(SHARED_KEY_HOT_SEARCH, sb.toString()).commit();
	}

	/**
	 * 获取搜索历史
	 *
	 * @return
	 */
	public ArrayList<String> getSearchHistory() {
		String history = mSharedPreferences
				.getString(SHARED_KEY_HOT_SEARCH, "");
		String[] split = history.split(",");
		ArrayList<String> list = new ArrayList<String>();
		if (split.length == 1 && TextUtils.isEmpty(split[0])) {
			return list;
		}
		for (String string : split) {
			if (TextUtils.isEmpty(string)) {
				continue;
			}
			list.add(string);
		}
		return list;
	}

	/**
	 * 清除搜索历史
	 *
	 */
	public void clearSearchHistory() {
		editor.putString(SHARED_KEY_HOT_SEARCH, "").commit();
	}

	/** 卖家信息 */
	public void saveSellerInfo(String info) {
		editor.putString(SHARED_KEY_SELLER_INFO, info).commit();
	}

	/** 卖家信息 */
	public void saveSellerInfo(SellerInfo info) {
		if (info != null) {
			editor.putString(SHARED_KEY_SELLER_INFO, JSON.toJSONString(info))
					.commit();
		}
	}

	/** 卖家信息 */
	public SellerInfo getSellerInfo() {
		String info = mSharedPreferences.getString(SHARED_KEY_SELLER_INFO, "");
		if (!TextUtils.isEmpty(info)) {
			return JSON.parseObject(info, SellerInfo.class);
		}
		return null;
	}

	/** 卖家ID */
	public String getSellerId() {
		String info = mSharedPreferences.getString(SHARED_KEY_SELLER_INFO, "");
		if (!TextUtils.isEmpty(info)) {
			return JSON.parseObject(info, SellerInfo.class).SellerId + "";
		}
		return "";
	}

	/** 清楚登陆相关信息 */
	public void clearLoginData() {
		editor.putString(SHARED_KEY_LOGIN_ID, null);
		editor.putString(SHARED_KEY_SELLER_ID, null);
		editor.putString(SHARED_KEY_SELLER_INFO, null);
		editor.commit();
	}

	/**
	 * 保存电话
	 * 
	 * @param value
	 */
	public void savePhoneNum(String value) {
		editor.putString(SHARED_KEY_PHONE_NUM, value);
		editor.commit();
	}

	/**
	 * 获得电话
	 *
	 * @return
	 */
	public String getPhoneNum() {
		return mSharedPreferences.getString(SHARED_KEY_PHONE_NUM, "");
	}

	/** 存类目 */
	public void saveCategories(List<Category> list) {
		if (list != null) {
			editor.putString(SHARED_KEY_CATEGORIES,
					JSONArray.toJSONString(list));
			editor.commit();
		}
	}

	/** 类目 */
	public List<Category> getCategories() {
		String data = mSharedPreferences.getString(SHARED_KEY_CATEGORIES, null);
		if (TextUtils.isEmpty(data)) {
			return null;
		}
		return JSON.parseArray(data, Category.class);
	}
	/**
	 * 保存是否开启引导页
	 *
	 * @param value
	 */
	public void saveSplash(boolean value) {
		editor.putBoolean(SHARED_KEY_SPLASH, value);
		editor.commit();
	}

	/**
	 * 获得是否开启引导页
	 *

	 * @return
	 */
	public boolean getSplash() {
		return mSharedPreferences.getBoolean(SHARED_KEY_SPLASH, false);
	}
	/**
	 * 保存是否开启引导页
	 *
	 * @param value
	 */
	public void saveAppVersion(int value) {
		editor.putInt(SHARED_KEY_APPVERSION, value);
		editor.commit();
	}

	/**
	 * 获得是否开启引导页
	 *

	 * @return
	 */
	public int getAppVersion() {
		return mSharedPreferences.getInt(SHARED_KEY_APPVERSION, 0);
	}

	/** 、
	 *
	 *
	 * * 保存用户密码
	 ** @param value
	 */
	public void saveUserPsw(String value) {

		try {
			editor.putString(SHARED_KEY_USER_PSW,
					RSAUtils.encryptByPublicKey(value));
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获得用户密码
	 *

	 * @return
	 */
	public String getUserPsw() {
		try {
			return
					RSAUtils.decryptByPrivateKey(mSharedPreferences.getString(SHARED_KEY_USER_PSW, ""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";

	}
}
