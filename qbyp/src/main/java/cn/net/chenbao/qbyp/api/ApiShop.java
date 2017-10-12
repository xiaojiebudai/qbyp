package cn.net.chenbao.qbyp.api;

/**
 * Created by 木头 on 2016/11/4.
 * 自营接口
 */

public class ApiShop {
    /** 自营类接口公共字段 */
    public static final String SHOP_JSON = "/Shop/Json/";
    public static final String SHOP_API = Api.WW_ONLINE + SHOP_JSON;

    /**
     * 加入购物车(数量+1)
     *
     * @return
     */
    public static final String CartAdd() {
        return SHOP_API + "CartAdd";
    }
    /**
     * 删除购物车记录
     *
     * @return
     */
    public static final String CartDelete() {
        return SHOP_API + "CartDelete";
    }
    /**
     * 批量删除购物车记录
     *
     * @return
     */
    public static final String CartsDelete() {
        return SHOP_API + "CartsDelete";
    }
    /**
     * 取购物车产品列表,按最后修改时间倒序
     *
     * @return
     */
    public static final String CartGet() {
        return SHOP_API + "CartGet";
    }
    /**
     * 修改购物车数量
     *
     * @return
     */
    public static final String CartNum() {
        return SHOP_API + "CartNum";
    }
    /**
     * 减购物车数量(数量-1)
     *
     * @return
     */
    public static final String CartSub() {
        return SHOP_API + "CartSub";
    }
    /**
     *  会员取购物车数量，金额等
     *
     * @return
     */
    public static final String CartSumGet() {
        return SHOP_API + "CartSumGet";
    }
    /**
     *   取自营类目
     *
     * @return
     */
    public static final String Classes() {
        return SHOP_API + "Classes";
    }/**
     *   取类目树
     *
     * @return
     */
    public static final String ClassTree() {
        return SHOP_API + "ClassTree";
    }
    /**
     * 取会员商品收藏列表
     *
     * @return
     */
    public static final String Favorites() {
        return SHOP_API + "Favorites";
    }
    /**
     * 取消订单
     *
     * @return
     */
    public static final String OrderCancel() {
        return SHOP_API + "OrderCancel";
    }
    /**
     * 确认收货
     *
     * @return
     */
    public static final String OrderConfirm() {
        return SHOP_API + "OrderConfirm";
    }
    /**
     * 取订单详情
     *
     * @return
     */
    public static final String OrderInfo() {
        return SHOP_API + "OrderInfo";
    }
    /**
     *订单评价
     *
     * @return
     */
    public static final String OrderJudge() {
        return SHOP_API + "OrderJudge";
    }
    /**
     *取订单日志(包括物流信息)
     *
     * @return
     */
    public static final String OrderLog() {
        return SHOP_API + "OrderLog";
    }
    /**
     *订单概要列表
     *
     * @return
     */
    public static final String OrderOutlines() {
        return SHOP_API + "OrderOutlines";
    }
    /**
     *订交订单预览
     *
     * @return
     */
    public static final String OrderPreview() {
        return SHOP_API + "OrderPreview";
    }
    /**
     *订单提交
     *
     * @return
     */
    public static final String OrderSubmit() {
        return SHOP_API + "OrderSubmit";
    }
    /**
     *取产品详情
     *
     * @return
     */
    public static final String ProductDescribe() {
        return SHOP_API + "ProductDescribe";
    }
    /**
     *取产品规格参数，扩展属性
     *
     * @return
     */
    public static final String ProductExtends() {
        return SHOP_API + "ProductExtends";
    }
    /**
     *收藏，或者取消收藏该产品
     *
     * @return
     */
    public static final String ProductFav() {
        return SHOP_API + "ProductFav";
    }
    /**
     *判断当前用户是否有收藏该产品
     *
     * @return
     */
    public static final String ProductFaved() {
        return SHOP_API + "ProductFaved";
    }
    /**
     *取最新热卖列表
     *
     * @return
     */
    public static final String ProductHot() {
        return SHOP_API + "ProductHot";
    }
    /**
     *取楼层列表
     *
     * @return
     */
    public static final String ShopFloorProductsGet() {
        return SHOP_API + "ShopFloorProductsGet";
    }
    /**
     * 取产品信息
     *
     * @return
     */
    public static final String ProductInfo() {
        return SHOP_API + "ProductInfo";
    }
    /**
     * 取产品评价列表，按时间倒序
     * @return
     */
    public static final String ProductJudge() {
        return SHOP_API + "ProductJudge";
    }
    /**
     * 取推荐产品列表
     * @return
     */
    public static final String ProductRecommends() {
        return SHOP_API + "ProductRecommends";
    }
    /**
     *取产品SKU列表
     * @return
     */
    public static final String ProductSkus() {
        return SHOP_API + "ProductSkus";
    }
    /**
     *产品搜索
     * @return
     */
    public static final String Search() {
        return SHOP_API + "Search";
    }
    /**
     *Vip产品列表
     * @return
     */
    public static final String ProductVip() {
        return SHOP_API + "ProductVip";
    }
    /**
     *立即购买
     * @return
     */
    public static final String CartAtOnce() {
        return SHOP_API + "CartAtOnce";
    }
    /**
     *自营订单状态(待发货,待收货)数量
     * @return
     */
    public static final String OrderStatusCount() {
        return SHOP_API + "OrderStatusCount";
    }
}
