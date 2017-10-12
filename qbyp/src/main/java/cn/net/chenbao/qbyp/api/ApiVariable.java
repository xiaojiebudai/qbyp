package cn.net.chenbao.qbyp.api;

public class ApiVariable {
    /**
     * 用户类接口公共字段
     */
    public static final String VARIABLE_JSON = "/Variable/Json/";
    /**
     * 用户信息
     */
    public static final String VARIABLE_API = Api.WW_ONLINE + VARIABLE_JSON;

    /**
     * App版本说明
     */
    public static final String AndroidUserAppExplain () {
        return VARIABLE_API + "AndroidUserAppExplain";
    }

    /**
     * 关于我们
     */
    public static final String AboutUs() {
        return VARIABLE_API + "AboutUs";
    }

    /**
     * 联系我们
     */
    public static final String LinkUs() {
        return VARIABLE_API + "LinkUs";
    }

    /**
     * 商家注册协议
     */
    public static final String SellerProtocol() {
        return VARIABLE_API + "SellerProtocol";
    }

    /**
     * 用户注册协议
     */
    public static final String UserProtocol() {
        return VARIABLE_API + "UserProtocol";
    }

    /**
     * 服务商费用
     */
    public static final String ServiceCost() {
        return VARIABLE_API + "ServiceCost";
    }

    /**
     * 代理协议
     */
    public static final String FenxiaoAgentProcotol() {
        return VARIABLE_API + "FenxiaoAgentProcotol";
    }
}
