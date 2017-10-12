package cn.net.chenbao.qbyp.distribution.been;

/**
 * Created by zxj on 2017/4/17.
 *
 * @description  分销等级中展示的商品
 */

public class DistributionProduct {
    public int AgentNum1;// 属性AgentNum1(一级代理数量)
    public int AgentNum2;// 属性AgentNum2(二级代理数量)
    public int AgentNum3;// 属性AgentNum3(三级代理数量)
    public int AgentNum4 ;//属性AgentNum4(四级代理数量)
    public int AgentNum5 ;//属性AgentNum5(五级代理数量)
    public double AgentPrice1 ;//属性AgentPrice1(一级代理价格) 总代价
    public double AgentPrice2 ;//属性AgentPrice2(二级代理价格) 小代价
    public double AgentPrice3 ;//属性AgentPrice3(三级代理价格)  专卖店价
    public double AgentPrice4 ;//属性AgentPrice4(四级代理价格)
    public double AgentPrice5 ;//属性AgentPrice5(五级代理价格)
    public double ConsumeNum ;//属性ConsumeNum(赠送积分)
    public long ProductId ;//属性ProductId(产品ID)
    public String ProductName ;
    public double SalePrice ;//属性SalePrice(零售价)   // 购物车新增   (售价)
    public int Status ;// 属性Status(状态)  //   购物车新增商品状态  0-下架  1-在售
    public int StockQty ;

    //购物车新增
    public long FlowId;
    public String ImageUrl;
    public long ModifyTime;
    public int  Quantity;
    public double SourcePrice; //购物车(原价)
    public long UserId;
    public boolean isChecked;//自己需要增加的

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setStatus(int checked) {
        Status = checked;
    }
}
