package cn.net.chenbao.qbyp.api;

/**
 * Created by ppsher on 2017/2/15.
 */

public class ApiOfflineTrade {
    public static final String TRADE_JSON = "/OfflineTrade/Json/";
    public static final String TRADE_API = Api.WW_ONLINE + TRADE_JSON;

    /** 訂單提交 */
    public static final String OfflineOrders() {
        return TRADE_API + "OfflineOrders";
    }
}
