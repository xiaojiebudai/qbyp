package cn.net.chenbao.qbypseller.api;

public class ApiUser {
	/** 用户类接口公共字段 */
	public static final String USER_JSON = "/User/Json/";
	/** 用户信息 */
	public static final String USER_API = Api.WW_ONLINE_API + USER_JSON;

	/**
	 * 用户忘记密码(商家和用户一个接口)
	 * 
	 * @return
	 */
	public static final String forgetPsw() {

		return USER_API + "LogPsdForget";

	}

	/**
	 * 用户修改密码(商家和用户一个接口)
	 * 
	 * @return
	 */
	public static final String updataPsw() {

		return USER_API + "LogpsdChange";

	}

	/**
	 * 用户忘记支付密码(商家和用户一个接口)
	 * 
	 * @return
	 */
	public static final String forgetPayPsw() {
		return USER_API + "PaypsdForget";

	}

	/**
	 * 用户修改支付密码(商家和用户一个接口)
	 * 
	 * @return
	 */
	public static final String updataPayPsw() {
		return USER_API + "PaypsdChange";
	}

	/** 分享中的邀請信息 */
	public static final String inviterGet() {
		return USER_API + "InviterGet";
	}

	/**
	 * 获取会员分享条码，该接口会判断当前会员是否有生成分享码，如果没有则生成，如果有则直接返回，邀请码暂时等于手机号
	 */
	public static final String getInviterGet() {
		return USER_API + "InviterGet";
	}

	/**
	 * 取我的团队列表
	 */
	public static final String getTeamsGet() {
		return USER_API + "TeamsGet";
	}

	/** 更新头像 */
	public static final String infoHeadUpdate() {
		return USER_API + "InfoHeadUpdate";
	}

	/** 是否设置过支付密码 */
	public static final String HavePaypsd() {
		return USER_API + "HavePaypsd";
	}

	/**
	 * 忘记密码验证手机号
	 * 
	 * @return
	 */
	public static final String LogPsdForgetSms() {
		return USER_API + "LogPsdForgetSms";
	}

	/**
	 * 忘记支付密码验证手机号
	 * 
	 * @return
	 */
	public static final String PaypsdForgetSms() {
		return USER_API + "PaypsdForgetSms";
	}
	/**
	 * 设置推荐人
	 * @return
	 */
	public static final String ChangeInviter() {
		return USER_API + "ChangeInviter";
	}
	/**
	 * 获取验证码
	 *
	 * @return
	 */
	public static final String RegisterSms () {
		return USER_API + "RegisterSms";
	}
	/**
	 * 获取极验验证参数
	 *
	 * @return
	 */
	public static final String getGeeValid() {
		return USER_API + "GeeValid";
	}

}
