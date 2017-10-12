package cn.net.chenbao.qbyp.bean;

import java.util.List;


/**
 * 行业
 *
 * @author licheng
 */
public class TradesCategory {
    public String ParentId;
    public String TradeIco;

    /**
     * 品类id
     */
    public String TradeId;
    /**
     * 品类名
     */
    public String TradeName;

    public double TradeRate;

    public boolean isSelect;
    /**
     * 当前页数
     */
    public int currentPager;
    /**
     * 记录当前的商家数
     */
    public List<TradesMessage> tradesList;
    /***
     * 记录当前标签
     */
    public List<TradesCategory> categoryList;

    public TradesCategory() {
    }

    public TradesCategory(String tradeIco, String tradeName) {
        TradeIco = tradeIco;
        TradeName = tradeName;
    }

    @Override
    public String toString() {
        return "TradesCategory [ParentId=" + ParentId + ", TradeIco="
                + TradeIco + ", TradeId=" + TradeId + ", TradeName="
                + TradeName + ", TradeRate=" + TradeRate + "]";
    }

}
