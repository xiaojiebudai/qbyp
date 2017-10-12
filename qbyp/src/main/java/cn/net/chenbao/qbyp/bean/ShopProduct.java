package cn.net.chenbao.qbyp.bean;

import java.util.ArrayList;

/**
 * Created by 木头 on 2016/11/4.
 */

public class ShopProduct {
    public String Barcode;// 属性Barcode(商品条码)
    public String Brand ;//属性Brand(brand)
    public int BrandId;// 属性BrandId(品牌)
    public String ClassId;// 属性ClassId(类目ID)
    public String ClassName;// 属性ClassName(类目名称)
    public double ConsumePayRate;// 属性ConsumePayRate(允许使用积分比率)
    public String CreateName;// 属性CreateName(创建人)
    public long CreateTime ;//属性CreateTime(创建时间)
    public int FavNum ;//属性FavNum(收藏量)
    public boolean HaveSku;// 属性HaveSku(是否存在sku)
    public String ImageUrl ;//属性ImageUrl(商品主图)
    public boolean IsRecommend ;//属性IsRecommend(推荐产品)
    public double JudgeLevel ;//属性JudgeLevel(评分)
    public int JudgeCount ;//JudgeCount
    public ShopProductJudge Judge;
    public String ModifiedName;// 属性ModifiedName(修改人)
    public long ModifiedTime ;//属性ModifiedTime(修改时间)
    public double PostFee ;//属性PostFee(邮费)
    public String ProductBrief ;//属性ProductBrief(商品简介)
    public long ProductId ;//属性ProductId(商品ID)
    public String ProductName ;//属性ProductName(商品名称)
    public double SalePrice ;//属性SalePrice(销售价格)
    public int SaleQty ;//属性SaleQty(已售数量)
    public double SettlePrice ;//属性SettlePrice(结算价格)
    public double SourcePrice;// 属性SourcePrice(原售价)
    public String Spec ;//属性Spec(商品规格)
    public int Status ;//属性Status(状态)(0 下架 1 上架，2删除)
    public int StockQty ;//属性StockQty(库存数量)
    public String VenderBrief;// 属性VenderBrief(vender_brief)
    public long VenderId ;//属性VenderId(所属厂家)
    public String VenderLocation ;//厂家所在地
    public ArrayList<ShopProductImage> Images ;//商品图组
    public boolean IsHaitao;
    public boolean IsVipLevel;
    public double ConsumePayAmt;
}
