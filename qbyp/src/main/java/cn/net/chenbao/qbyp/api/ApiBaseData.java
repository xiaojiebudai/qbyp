package cn.net.chenbao.qbyp.api;

/**
 * 基础信息
 *
 * @author licheng
 */
public class ApiBaseData {

    public static final String BASEDATA_JSON = "/BaseData/Json/";

    public static final String BASEDATA_API = Api.WW_ONLINE + BASEDATA_JSON;

    /**
     * 获得行业类别
     *
     * @return
     */
    public static final String getTradesCategory() {
        return BASEDATA_API + "TradesGet";
    }

    /**
     * 获得热门搜索
     *
     * @return
     */
    public static final String getHotSearch() {
        return BASEDATA_API + "SearchHot";
    }

    /**
     * 运行的银行类型
     *
     * @return
     */
    public static final String getBanksGet() {
        return BASEDATA_API + "BanksGet";
    }

    /**
     * 取地区列表
     *
     * @return
     */
    public static final String getRegionsGet() {
        return BASEDATA_API + "RegionsGet";
    }

    /**
     * 版本更新
     *
     * @return
     */
    public static final String AppVerGet() {
        return BASEDATA_API + "AppVerGet?appName=AndroidUser";
    }

    public static final String BannersGet() {
        return BASEDATA_API + "BannersGet";
    }

    /**
     * Account
     *
     * @return
     */
    public static final String CompanyAccounts() {
        return BASEDATA_API + "CompanyAccounts";
    }

    /**
     * Account
     *
     * @return
     */
    public static final String Delivers() {
        return BASEDATA_API + "Delivers";
    }

}
