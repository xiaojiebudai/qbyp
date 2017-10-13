package cn.net.chenbao.qbypseller.api;

public class Api {

	private static final boolean isDebug = false;

	public static final String WW_ONLINE_API = isDebug ? "http://api.51wanj.com"
			: "http://api.qbypsc.com";

	/** Image */
	public static final String WW_ONLINE_IMAGE = isDebug ? "http://img.51wanj.com/"
			: "http://img.chenbao.net.cn/";

	/** sessionId */
	public static final String KEY_SESSIONID = "sessionId";
	/** json数据key(result解析使用) */
	public static final String KEY_DATA = "Data";
	/** sellerId */
	public static final String KEY_SELLERID = "sellerId";
	/** 用于提交的时候的data键字段 */
	public static final String KEY_data = "data";
	/** file 文件 */
	public static final String KEY_FILE = "file";

	/** 数据量 */
	public static final String KEY_PAGE_SIZE = "pageSize";
	/** 页数 */
	public static final String KEY_PAGE_INDEX = "pageIndex";
	public static final String DEFAULT_PAGE_SIZE = "20";
	/**
	 * 用户分享码前缀
	 *
	 * @return
	 */
	public static final String getShareUrl() {
		return Api.isDebug ? "http://www.51wanj.com/mobile/register.html?inviter="
				: "http://www.qbypsc.com/mobile/register.html?inviter=";
	}
}
