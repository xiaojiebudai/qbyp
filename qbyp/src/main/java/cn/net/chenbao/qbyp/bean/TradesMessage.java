package cn.net.chenbao.qbyp.bean;

/**
 * 商家信息
 * 
 * @author licheng
 *
 */
public class TradesMessage {

	public String Address;// 商家地址

	public long BusinessEnd;// 营业结束时间
	public long BusinessStart;// 营业起始时间
	public int City;// 属性City(市)
	public int County;// 属性County(县)
	public int CreateTime;// 属性CreateTime(注册时间)
	public long DeliverAvgtime;// 属性DeliverAvgtime(deliver_avgtime)
	public double DeliverFee;// 属性DeliverFee(配送费)
	public int Distance;// 距离
	public long ExpireTime;// 属性ExpireTime(认证到期时间)
	public String Explain;// 属性Explain(说明)
	public double FullDelivery;// 属性FullDelivery(满多少免配送费)
	public double FullPackage;// 属性FullPackage(满多少免包装费)
	public float JudgeLevel;// 属性JudgeLevel(评价星级)
	public double Latitude;// 属性Latitude(纬度)
	public double Longitude;// 属性Longitude(经度)
	public double PackageFee;// 属性PackageFee(包装费)

	public int Province;// 属性Province(省)
	public String TradeType;// 属性TradeType(所属行业)
	public long SellerId;// 属性SellerId(商家ID)
	public String SellerName;// 属性SellerName(商家名称)
	public String SellerTel;// 属性SellerTel(店铺电话)
	public String ShopPicture;// 属性ShopPicture(首图)
	public int Status;// 属性Status(状态)

	public int Street;// 属性Street(街道)
	public boolean SupportDeliver;// 属性SupportDeliver(支持配送)
	public boolean SupportSince;// 属性SupportSince(支持到店)
	public String TradeName;// 属性TradeName(行业名称)
	public double TradeRate;// 属性TradeRate(行业扣点)
	public int UserId;// 属性UserId(所属会员)
	public long OrderCount;// 已售单数
	public double ConsumePay;// 配比比率

	@Override
	public String toString() {
		return "TradesMessage [Address=" + Address + ", BusinessEnd="
				+ BusinessEnd + ", BusinessStart=" + BusinessStart + ", City="
				+ City + ", County=" + County + ", CreateTime=" + CreateTime
				+ ", DeliverAvgtime=" + DeliverAvgtime + ", DeliverFee="
				+ DeliverFee + ", Distance=" + Distance + ", ExpireTime="
				+ ExpireTime + ", Explain=" + Explain + ", FullDelivery="
				+ FullDelivery + ", FullPackage=" + FullPackage
				+ ", JudgeLevel=" + JudgeLevel + ", Latitude=" + Latitude
				+ ", Longitude=" + Longitude + ", PackageFee=" + PackageFee
				+ ", Province=" + Province + ", TradeType=" + TradeType
				+ ", SellerId=" + SellerId + ", SellerName=" + SellerName
				+ ", SellerTel=" + SellerTel + ", Street=" + Street
				+ ", SupportDeliver=" + SupportDeliver + ", SupportSince="
				+ SupportSince + ", TradeName=" + TradeName + ", TradeRate="
				+ TradeRate + ", UserId=" + UserId + "]";
	}

}
