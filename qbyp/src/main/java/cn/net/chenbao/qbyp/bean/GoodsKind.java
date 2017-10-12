package cn.net.chenbao.qbyp.bean;

import java.util.List;

/**
 * 商品品类
 * 
 * @author xl
 * @date 2016-7-29 上午2:33:18
 * @description
 */
public class GoodsKind {
	public int ClassId;
	public String ClassName;

	public int GoodsCount;
	public int ParentId;
	public int SellerId;
	/** 自定义属性,是否有数据 */
	public boolean haveData;
	/** 保存数据 */
	public List<Goods> goodsList;

	public boolean HaveNextPage = true;// 默认有下一页
	public int pageIndex;// 页数

	public int classIndex;//类目排名

	@Override
	public String toString() {
		return "GoodsKind [ClassId=" + ClassId + ", ClassName=" + ClassName
				+ ", GoodsCount=" + GoodsCount + ", ParentId=" + ParentId
				+ ", SellerId=" + SellerId + ", haveData=" + haveData
				+ ", goodsList=" + goodsList + "]";
	}

}
