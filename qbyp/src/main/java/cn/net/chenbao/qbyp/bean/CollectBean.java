package cn.net.chenbao.qbyp.bean;

/***
 * Description:收藏商家数据 Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-8-2
 */
public class CollectBean {
	public long CreateTime;
	public String FlowId;
	public float JudgeLevel;
	public long SellerId;
	public String SellerName;
	public String ShopPicture;
	public String TradeName;
	public int TradeType;
	public String UserId;
	public String UserName;

	@Override
	public String toString() {
		return "CollectBean [CreateTime=" + CreateTime + ", FlowId=" + FlowId
				+ ", JudgeLevel=" + JudgeLevel + ", SellerId=" + SellerId
				+ ", SellerName=" + SellerName + ", ShopPicture=" + ShopPicture
				+ ", TradeName=" + TradeName + ", TradeType=" + TradeType
				+ ", UserId=" + UserId + ", UserName=" + UserName + "]";
	}

}
