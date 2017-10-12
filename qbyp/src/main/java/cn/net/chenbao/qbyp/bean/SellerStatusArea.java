package cn.net.chenbao.qbyp.bean;

/**
 * 商家状态,区域(已认证 过期 停业..)
 * 
 * @author wuri
 * 
 */
public class SellerStatusArea {

	public String StatusName;
	public int Status;
	

	public SellerStatusArea() {
		super();
	}


	public SellerStatusArea(String statusName) {
		super();
		StatusName = statusName;
	}


	public SellerStatusArea(String statusName, int status) {
		super();
		StatusName = statusName;
		Status = status;
	}

}
