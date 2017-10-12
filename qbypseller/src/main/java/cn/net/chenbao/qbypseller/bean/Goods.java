package cn.net.chenbao.qbypseller.bean;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 商品
 * 
 * @author xl
 * @date 2016-8-5 上午1:05:05
 * @description
 */
public class Goods {

	/** 在售 */
	public static final int STATE_ONSALE = 0;
	/** 下架 */
	public static final int STATE_OFFSALE = 1;

	/** 条形码 */
	public String Barcode;
	public long ClassId;
	public String Describe;
	public long GoodsId;
	public String GoodsImg;
	public String GoodsName;
	public double Price;
	/** 已售 */
	public int SaleQty;
	public int SellerId;
	public int Status;
	/** 库存数 */
	public int StockQty;
	public String Unit;

	/** 订单中的商品对应的数量 */
	public int Quantity;

	@Override
	public boolean equals(Object o) {
		return this.GoodsId == ((Goods) o).ClassId;
	}


	public JSONObject toJson() throws JSONException {
		JSONObject localItemObject = new JSONObject();
		localItemObject.put("GoodsId", GoodsId);
		localItemObject.put("Quantity", Quantity);

		return localItemObject;
	}
}
