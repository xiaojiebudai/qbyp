package cn.net.chenbao.qbypseller.api;

/**
 * 基础信息
 * 
 * @author licheng
 * 
 */
public class ApiBaseData extends Api {

	public static final String BASEDATA_JSON = "/BaseData/Json/";

	public static final String BASEDATA_API = Api.WW_ONLINE_API + BASEDATA_JSON;

	/**
	 * 获得行业类别
	 * 
	 * @return
	 */
	public static final String getTradesCategory() {

		return BASEDATA_API + "TradesGet";
	}

	/**
	 * 获得地区
	 * 
	 * @return
	 */
	public static final String getRegions() {
		return BASEDATA_API + "RegionsGet";
	}

	/**
	 * 上传图片
	 */
	public static final String upImage() {
		return WW_ONLINE_IMAGE + "UpImage";
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
	 * @return
	 */
	public static final String AppVerGet() {
		return BASEDATA_API + "AppVerGet?appName=AndroidSeller";
	}
}
