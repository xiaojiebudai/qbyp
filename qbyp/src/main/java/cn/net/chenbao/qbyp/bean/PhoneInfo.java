package cn.net.chenbao.qbyp.bean;

public class PhoneInfo {
	private String phoneNum;
	private String phoneName;

	public PhoneInfo(String phoneNum, String phoneName) {
		super();
		this.phoneNum = phoneNum;
		this.phoneName = phoneName;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getPhoneName() {
		return phoneName;
	}

	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}

}
