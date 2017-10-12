package cn.net.chenbao.qbyp.myinterface;

/**
 * 请求服务器错误信息回掉
 * 
 * @author xl
 * @date 2016-8-1 下午10:57:42
 * @description
 */
public interface SeverErrorCallBack {
	/** session过期/失效 */
	void onSessionPast();
}
