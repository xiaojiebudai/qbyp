package cn.net.chenbao.qbyp.bean;

/**
 * Description:区域商专区,代理区域实体信息类
 * 
 * @author wuri
 * @date 2016-10-11
 * 
 */
public class AgencyInfo {
	public double AgentAmt;
	public double Account;
	public int AgentId;
	public String AgentName;
	public String AgentNo;
	public double AlreadyAmt;
	public String AreaName;
	public double ArrearAmt;
	public double CashAmt;
	public int City;
	public int County;
	public long CreateTime;
	public int Provice;
	public String Address;
	public String SellerName;
	public String ShopPicture;
	public int ChargeType;
	public String TradeName;
	public String UserMobile;
	public Integer Status;
	public int UserId;
	public int TradeId;
	public String TradeType;
	public long ValidTime;
	public long BusinessStart;
	public long BusinessEnd;
	public double Latitude;
	public double Longitude;		
	public long ExpireTime;
	public String StateName;

	// 代理人信息
	public String LegalMobile;
	public String LegalName;
	public String LegalNo;
	public String LicenceNo;
	public String CropName;

	public String SellerTel;

	public AgencyInfo() {
		super();
	}

	public AgencyInfo(String stateName) {
		super();
		StateName = stateName;
	}

	public AgencyInfo(String stateName, Integer status) {
		super();
		Status = status;
		StateName = stateName;
	}
}
