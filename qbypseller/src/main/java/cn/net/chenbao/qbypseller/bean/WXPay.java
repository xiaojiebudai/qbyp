package cn.net.chenbao.qbypseller.bean;

public class WXPay {

	public String appid;
	public String appsecret;
	public String noncestr;
	public String partnerid;
	public String prepayid;
	public String sign;
	public String timestamp;

	@Override
	public String toString() {
		return "WXPay [appid=" + appid + ", appsecret=" + appsecret
				+ ", noncestr=" + noncestr + ", partnerid=" + partnerid
				+ ", prepayid=" + prepayid + ", sign=" + sign + ", timestamp="
				+ timestamp + "]";

	}

}
