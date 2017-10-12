package cn.net.chenbao.qbypseller.bean;

/**
 * 卖家信息
 * 
 * @author xl
 * @description
 */
public class SellerInfo {

	/** 待审核 */
	public static final int STATUS_WAIT_CHECK = 0;
	/** 营业 */
	public static final int STATUS_NORMAL = 1;
	/** 停业 */
	public static final int STATUS_STOP = 2;
	/** 冻结 */
	public static final int STATUS_FREEZE = 3;
	public String Address;
	public String LocationAddress;
	public String UserMobile;
	public long BusinessEnd;
	public long BusinessStart;
	public int City;
	public int County;
	public long CreateTime;
	public long DeliverAvgtime;
	public int FavNum;// 人气

	/** 配送費 */
	public String DeliverFee;
	public long ExpireTime;
	/** 说明 */
	public String Explain;
	/** 满免配送 */
	public String FullDelivery;
	/** 满免包装 */
	public String FullPackage;
	public String JudgeLevel;
	public String Latitude;
	public String Longitude;
	/** 包裝費 */
	public String PackageFee;
	public int Province;
	public long SellerId;
	public String SellerName;
	public String SellerTel;
	public String ShopPicture;
	public int Status;// 状态
	public int Street;
	/** 支持配送 */
	public boolean SupportDeliver;
	/** 支持到店 */
	public boolean SupportSince;
	/** 行业名称 */
	public String TradeName;
	public double TradeRate;
	public double consumePay;
	/** 所属行业 */
	public String TradeType;
	public int UserId;

	@Override
	public String toString() {
		return "SellerInfo [Address=" + Address + ", BusinessEnd="
				+ BusinessEnd + ", BusinessStart=" + BusinessStart + ", City="
				+ City + ", County=" + County + ", CreateTime=" + CreateTime
				+ ", DeliverAvgtime=" + DeliverAvgtime + ", FavNum=" + FavNum
				+ ", DeliverFee=" + DeliverFee + ", ExpireTime=" + ExpireTime
				+ ", Explain=" + Explain + ", FullDelivery=" + FullDelivery
				+ ", FullPackage=" + FullPackage + ", JudgeLevel=" + JudgeLevel
				+ ", Latitude=" + Latitude + ", Longitude=" + Longitude
				+ ", PackageFee=" + PackageFee + ", Province=" + Province
				+ ", SellerId=" + SellerId + ", SellerName=" + SellerName
				+ ", SellerTel=" + SellerTel + ", ShopPicture=" + ShopPicture
				+ ", Status=" + Status + ", Street=" + Street
				+ ", SupportDeliver=" + SupportDeliver + ", SupportSince="
				+ SupportSince + ", TradeName=" + TradeName + ", TradeRate="
				+ TradeRate + ", TradeType=" + TradeType + ", UserId=" + UserId
				+ "]";
	}

}
