package cn.net.chenbao.qbyp.distribution.been;

/**
 * Created by zxj on 2017/4/16.
 *
 * @description 分销等级
 */

public class DistributionLevel {
    public String Explain;//备注
    public String ImageUrl;//产品图片
    //1总经销 2二级 0消费者
    public int LevelId;//属性LevelId(当前等级)

    public long ParentId;//属性ParentId(上级分商销ID)

    public DistributionProduct Product;//分销产品信息
    public long ProductId;//属性ProductId(产品ID)
    public int SaleNum;//属性SaleNum(累计进货数量)

    public long UserId;//属性UserId(会员ID)


}
