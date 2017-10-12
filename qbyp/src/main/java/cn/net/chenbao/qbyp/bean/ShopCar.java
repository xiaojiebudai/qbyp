package cn.net.chenbao.qbyp.bean;

/**
 * 购物车商品信息,有些字段和goods一样,还是新搞一个 (废弃,Goods替代)
 * 
 * @author licheng
 *
 */
public class ShopCar {

	public int Barcode;
	public int FlowId;
	public long GoodsId;
	public String GoodsImg;
	public String GoodsName;
	public long ModifiedTime;
	public double Price;
	public int Quantity;
	public int SellerId;
	public String SellerName;
	public int StockQty;
	public int UserId;


	@Override
	public String toString() {
		return "ShopCar [Barcode=" + Barcode + ", FlowId=" + FlowId
				+ ", GoodsId=" + GoodsId + ", GoodsImg=" + GoodsImg
				+ ", GoodsName=" + GoodsName + ", ModifiedTime=" + ModifiedTime
				+ ", Price=" + Price + ", Quantity=" + Quantity + ", SellerId="
				+ SellerId + ", SellerName=" + SellerName + ", StockQty="
				+ StockQty + ", UserId=" + UserId + "]";
	}

}
