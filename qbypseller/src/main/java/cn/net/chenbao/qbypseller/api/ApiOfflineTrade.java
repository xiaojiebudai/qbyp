package cn.net.chenbao.qbypseller.api;

/**
 * 线下收款
 * Created by Administrator on 2017/2/13.
 */

public class ApiOfflineTrade extends Api {
    public static final String TRADE_JSON = "/OfflineTrade/Json/";
    public static final String TRADE_API = WW_ONLINE_API + TRADE_JSON;

    /** 訂單提交 */
    public static final String OfflineOrderSubmit() {
        return TRADE_API + "OfflineOrderSubmit";
    }
    /** 訂單列表 */
    public static final String OfflineOrders() {
        return TRADE_API + "OfflineOrders";
    }

}
