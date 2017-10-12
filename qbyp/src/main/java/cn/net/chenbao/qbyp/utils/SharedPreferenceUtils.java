package cn.net.chenbao.qbyp.utils;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * 获取SharedPreference数据的工具类
 *
 * @author lc
 */
public class SharedPreferenceUtils {
    private static final String USER_INFO = "shared_save_info";
    public static final String CYK = "cyk";

    private static SharedPreferenceUtils utils;
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;

    /**
     * 登录的sessionId
     */
    private static String SHARED_KEY_LOGIN_ID = "login_id";
    /**
     * 登录的userId
     */
    private static String SHARED_KEY_USER_ID = "userId";
    /**
     * 热门搜索
     */
    private static String SHARED_KEY_HOT_SEARCH = "hot_search";
    /**
     * 自营搜索
     */
    private static String SHARED_KEY_HOT_SEARCH_SELF = "hot_search_self";
    /**
     * 保存电话号码
     */
    private static String SHARED_KEY_PHONE_NUM = "phone_num";
    /**
     * 保存地理位置
     */
    private static String SHARED_KEY_LOCATION = "location";
    /**
     * 自营商城商品数量
     */
    private static String SHARED_KEY_CARTNUM = "cartnum";
    /**
     * 操作引导
     */
    private static String SHARED_KEY_GUIDE = "guide";
    /**
     * 引导页
     */
    private static String SHARED_KEY_SPLASH = "splash";
    /**
     * APP版本
     */
    private static String SHARED_KEY_APPVERSION = "app_version";
    /**
     * 用户密码
     */
    private static String SHARED_KEY_USER_PSW = "user_psw";

    private SharedPreferenceUtils(Context cxt) {
        mSharedPreferences = cxt.getSharedPreferences(USER_INFO,
                Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
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
     * @param sessionId
     */
    public void saveSessionId(String sessionId) {
        editor.putString(SHARED_KEY_LOGIN_ID, sessionId);
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
     */
    public void clearSearchHistory() {
        editor.putString(SHARED_KEY_HOT_SEARCH, "").commit();
    }


    /**
     * 保存搜索记录自营
     *
     * @param condition
     */
    public void saveSearchHistorySelf(String condition) {
        condition = condition.trim();
        String history = mSharedPreferences
                .getString(SHARED_KEY_HOT_SEARCH_SELF, "");
        String[] hisArrays = history.split(",");
        for (int i = 0; i < hisArrays.length; i++) {
            if (hisArrays[i].equals(condition)) {// 已经有了
                return;
            }
        }
        StringBuilder sb = new StringBuilder(history);
        sb.insert(0, condition + ",");
        editor.putString(SHARED_KEY_HOT_SEARCH_SELF, sb.toString()).commit();
    }

    /**
     * 获取搜索历史自营
     *
     * @return
     */
    public ArrayList<String> getSearchHistorySelf() {
        String history = mSharedPreferences
                .getString(SHARED_KEY_HOT_SEARCH_SELF, "");
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
     * 清除搜索历史自营
     */
    public void clearSearchHistorySelf() {
        editor.putString(SHARED_KEY_HOT_SEARCH_SELF, "").commit();
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

    /**
     * 保存自营商城购物车数量
     *
     * @param value
     */
    public void saveCartNum(int value) {
        editor.putInt(SHARED_KEY_CARTNUM, value);
        editor.commit();
    }

    /**
     * 获得自营商城购物车数量
     *
     * @return
     */
    public int getCartNum() {
        return mSharedPreferences.getInt(SHARED_KEY_CARTNUM, 0);
    }

    /**
     * 操作引导
     *
     * @param guide
     */
    public void saveIsGuide(boolean guide) {
        editor.putBoolean(SHARED_KEY_GUIDE, guide);
        editor.commit();
    }

    /**
     * 获得操作引导
     *
     * @return
     */
    public boolean getIsGuide() {
        return mSharedPreferences.getBoolean(SHARED_KEY_GUIDE, false);
    }

    /**
     * 保存地理位置
     *
     * @param value
     */
    public void saveLocation(String value) {
        editor.putString(SHARED_KEY_LOCATION, value);
        editor.commit();
    }

    /**
     * 获得地理位置
     *
     * @return
     */
    public String getLocation() {
        return mSharedPreferences.getString(SHARED_KEY_LOCATION, "");
    }

    /**
     * 个人的清除登录数据 ,需要记住密码和用户名
     */
    public void clearLoginData() {

        editor.putString(SHARED_KEY_LOGIN_ID, null);
        editor.commit();
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
     * 保存版本号
     *
     * @param value
     */
    public void saveAppVersion(int value) {
        editor.putInt(SHARED_KEY_APPVERSION, value);
        editor.commit();
    }

    /**
     * 获得版本号
     *
     * @return
     */
    public int getAppVersion() {
        return mSharedPreferences.getInt(SHARED_KEY_APPVERSION, 0);
    }

    /**
     * 保存用户密码
     *
     * @param value
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
