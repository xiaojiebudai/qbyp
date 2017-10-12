package cn.net.chenbao.qbyp.bean;

/**
 * Created by wuri on 2016/11/7.
 */

public class ShopOrderExplain {
    public String Explain;// 留言内容
    public long VenderId;// 厂家ID

    public ShopOrderExplain() {
    }

    public ShopOrderExplain(long key, String content) {
        this.VenderId = key;
        this.Explain = content;
    }
}
