package cn.net.chenbao.qbyp.bean;

import java.util.ArrayList;

/**
 * Created by 木头 on 2016/11/7.
 */

public class ShopProductProAndVal {
    public String ClassId;// 属性ClassId(类目ID)
    public boolean IsSku;//  属性IsSku(是否为销售属性)
    public long ProId ;// 属性ProId(属性ID)
    public String ProName ;// 属性ProName(属性名称)
    public int SortId;//  属性SortId(sort_id)
    public String ValName ;// 手输值
    public int ValType ;// 属性ValType(值类型)
    public ArrayList<ShopProductProVal> Values ;// 值列表

}
