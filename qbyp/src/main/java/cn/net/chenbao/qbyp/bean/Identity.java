package cn.net.chenbao.qbyp.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author 木头 身份证
 */
public class Identity {
	@JSONField(name = "姓名")
	String name;
	@JSONField(name = "公民身份号码")
	String number;
	@JSONField(name = "签发机关")
	String qianfajigou;
	@JSONField(name = "有效期至")
	String time;
	@JSONField(name = "性别")
	String sex;
	@JSONField(name = "出生")
	 String birthday;
	@JSONField(name = "住址")
	String address;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getQianfajigou() {
		return qianfajigou;
	}

	public void setQianfajigou(String qianfajigou) {
		this.qianfajigou = qianfajigou;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Identity [name=" + name + ", number=" + number
				+ ", qianfajigou=" + qianfajigou + ", time=" + time + ", sex="
				+ sex + ", birthday=" + birthday + ", address=" + address + "]";
	}
	
}
