package cn.net.chenbao.qbyp.api;

public class Api {
	private static final boolean isDebug =false;

	/** 主机地址 */
	public static final String WW_ONLINE = isDebug ? "http://api.51wanj.com"
			: "http://api.qbypsc.com";//http://cykapi.szhysy.cn
	/** Image */
	public static final String WW_ONLINE_IMAGE = isDebug ? "http://img.51wanj.com/"
			: "http://img.chenbao.net.cn/";//http://img.szhysy.cn/
	/** 下载app地址 */
	public static final String API_DOWNLOAD_APP = "http://www.szhysy.cn/mobile/download.html";
}
