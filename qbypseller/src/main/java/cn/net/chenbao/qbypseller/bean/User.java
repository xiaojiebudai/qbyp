package cn.net.chenbao.qbypseller.bean;

/***
 * Description:用户信息 Company: wangwanglife Version：1.0
 * @author ZXJ
 * @date @2016-7-30
 ***/
public class User {
	/** 金额 */
	public String Consume;
	/** 注册时间 */
	public long CreateTime;
	/** 邀请人 */
	public String InviterId;
	/** 是否验证邮箱 */
	public boolean IsValidEmail;
	/** 实名认证 */
	public boolean IsReal;
	/** 商家开通标志*/
	public boolean IsSeller;
	/** 手机号*/
	public String Mobile;
	/** 会员ID */
	public String UserId;
	/** 会员名 */
	public String UserName;
	/**性别 */
	public String Sex;
	/**分享码 */
	public String IviterNo;
	/**头像 */
	public String HeadUrl;
	/** 状态 */
	public int  Status;
	/** (real_name)  */
	public String  RealName;
	/** (分享二维码)  */
	public String  BarcodeUrl;
	/** 邮箱  */
	public String  Email;
	/** 1表示1级会员，2表示2级 */
	public int  RelLevel;

}

