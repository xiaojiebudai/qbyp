package cn.net.chenbao.qbypseller.bean;

/**
 * 商业品类
 * 
 * @author licheng
 *
 */
public class TradesCategory {
	public String ParentId;

	public String TradeIco;

	/** 品类id */
	public String TradeId;
	/** 品类名 */
	public String TradeName;

	public double TradeRate;

	public boolean isSelect;

	@Override
	public String toString() {
		return "TradesCategory [ParentId=" + ParentId + ", TradeIco="
				+ TradeIco + ", TradeId=" + TradeId + ", TradeName="
				+ TradeName + ", TradeRate=" + TradeRate + "]";
	}

}
