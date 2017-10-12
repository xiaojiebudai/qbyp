package cn.net.chenbao.qbyp.bean;

/**
 * 商品
 * 
 * @author lc(购物车,订单详情中goods集合,和Goods都用的这个)
 * @date 2016-8-5 上午1:05:05
 * @description
 */
public class Goods {
	/** 条形码 */
	public String Barcode;// 属性Barcode(商品条码)
	public int ClassId;// 属性ClassId(商品分类)
	public String Describe;// 属性Describe(商品描述)
	public long GoodsId;// 属性GoodsId(产品ID)
	public String GoodsImg;// 属性GoodsImg(主图)
	public String GoodsName;// 属性GoodsName(商品名称)
	public double Price;// 属性Price(商品价格)
	/** 已售 */
	public int SaleQty;// 属性SaleQty(已售数量)
	public int SellerId;// 属性SellerId(所属商家)
	public int Status;// 属性Status(状态)
	/** 库存数 */
	public int StockQty;// 属性StockQty(库存数量)
	public String Unit;// 属性Unit(单位)
	// public int buyNum;// 购买的数量(自定义)
	public int CartNum;// 购买数量

	public int pageIndex;// 自定义属性,页数

	/**
	 * 订单详情
	 */
	public long OrderGoodsId;
	public long OrderId;

	/**
	 * 购物车
	 */
	public int Quantity;// 购买的数量
	public int FlowId;
	public long ModifiedTime;
	public String SellerName;
	public int UserId;

	@Override
	public String toString() {
		return "Goods [Barcode=" + Barcode + ", ClassId=" + ClassId
				+ ", Describe=" + Describe + ", GoodsId=" + GoodsId
				+ ", GoodsImg=" + GoodsImg + ", GoodsName=" + GoodsName
				+ ", Price=" + Price + ", SaleQty=" + SaleQty + ", SellerId="
				+ SellerId + ", Status=" + Status + ", StockQty=" + StockQty
				+ ", Unit=" + Unit + ", CartNum=" + CartNum + ", OrderGoodsId="
				+ OrderGoodsId + ", OrderId=" + OrderId + ", Quantity="
				+ Quantity + ", FlowId=" + FlowId + ", ModifiedTime="
				+ ModifiedTime + ", SellerName=" + SellerName + ", UserId="
				+ UserId + "]";
	}

}
