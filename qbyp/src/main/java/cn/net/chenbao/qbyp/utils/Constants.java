package cn.net.chenbao.qbyp.utils;

/**
 * 常量
 * 
 * @author licheng
 * 
 */
public class Constants {
    /**分享出去的链接*/
	public static final String SHARE_URL = "http://www.qbypsc.com/mobile/register.html?inviter=";
	

	/** qq AppId */
	public static final String QQ_APP_ID = "1106257921";
	/** qq app key */
	public static final String QQ_APP_KEY = "sz6h2wI9XWVW5fEd";
	/** 微信id */
	public static final String WX_APP_ID = "wx4c222a346a33ee5b";
	/** 微信key */
	public static final String WX_APP_KEY = "db43fe16e9be2ebff3de84d9425edbeb";
	/** 新浪SECRET */
	public static final String SINA_APP_SECRET = "3e86eafa9b4a6492779f1d37f429669c";//   39f8003b02dd7491ca58856166873489
	/** 新浪key */
	public static final String SINA_APP_KEY = "1255594834"; //    1031626214
	/**
	 * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
	 * 
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	public static final String REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";

	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
	 * 
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
	 * 
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 * 
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";
}
