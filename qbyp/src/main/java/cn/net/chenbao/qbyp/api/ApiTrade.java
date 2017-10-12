package cn.net.chenbao.qbyp.api;

/**
 * 购物车,订单,相关
 *
 * @author licheng
 */
public class ApiTrade {

    public static final String TRADE_JSON = "/Trade/Json/";
    public static final String TRADE_API = Api.WW_ONLINE + TRADE_JSON;

    /**
     * 购物车商品添加一
     *
     * @return
     */
    public static final String addCart() {
        return TRADE_API + "CartAdd";
    }

    /**
     * 购物车商品减一
     *
     * @return
     */
    public static final String subCart() {
        return TRADE_API + "CartSub";
    }

    /**
     * 获取订单列表
     *
     * @return
     */
    public static final String ordersGet() {
        return TRADE_API + "OrdersGet";
    }

    /**
     * 获取购物车信息
     *
     * @return
     */

    public static final String shopCarGet() {
        return TRADE_API + "CartGet";
    }

    /**
     * 清空购物车信息
     *
     * @return
     */

    public static final String clearShopCar() {
        return TRADE_API + "CartClear";
    }

    /**
     * 订单信息预览
     *
     * @return
     */
    public static final String getOrderDetail() {
        return TRADE_API + "OrderPreview";
    }

    /**
     * 订单信息获取(收货人信息等)
     *
     * @return
     */
    public static final String getOrderInfo() {
        return TRADE_API + "OrderInfo";
    }

    /**
     * 提交订单
     *
     * @return
     */
    public static final String orderSubmit() {
        return TRADE_API + "OrderSubmit";
    }

    /**
     * 确认收货
     *
     * @return
     */
    public static final String OrderUserConfirm() {
        return TRADE_API + "OrderUserConfirm";
    }

    /**
     * 评价页面订单详情
     *
     * @return
     */
    public static final String OrderOutline() {
        return TRADE_API + "OrderOutline";
    }

    /**
     * 评价
     *
     * @return
     */
    public static final String OrderJudge() {
        return TRADE_API + "OrderJudge";
    }

    /**
     * 取消订单
     *
     * @return
     */
    public static final String OrderCancel() {
        return TRADE_API + "OrderCancel";
    }

    /**
     * 本地订单状态(待付款,待接单,待收货)数量
     *
     * @return
     */
    public static final String OrderStatusCount() {
        return TRADE_API + "OrderStatusCount";
    }

}
