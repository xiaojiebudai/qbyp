package cn.net.chenbao.qbypseller.bean;

/**
 * 支付实体
 * 
 * @author Administrator
 * 
 */
public class PayWay {

	/** 是否开启 */
	public boolean IsOnline;
	public String PayCode;
	public String PayIco;
	public String PayName;
	public int Status;
	public boolean isSelect;

	@Override
	public String toString() {
		return "PayWay [IsOnline=" + IsOnline + ", PayCode=" + PayCode
				+ ", PayIco=" + PayIco + ", PayName=" + PayName + ", Status="
				+ Status + "]";
	}

}
