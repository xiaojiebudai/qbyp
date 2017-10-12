package cn.net.chenbao.qbypseller.bean;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/***
 * Description:bank Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-8-4
 */
public class Bank {
	/** 会员ID */
	public String UserId;
	/** 银行缩写 */
	public String BankNo;
	/** 银行名称 */
	public String BankName;
	/** 帐户类型 0储蓄卡 1信用卡 */
	public int CardType;
	/** 开户行 */
	public String BankAddress;
	/** 图标 */
	public String BankIco;
	/** 账户号 */
	public String AccountNo;
	/** 账户名 */
	public String AccountName;
	/** 最后修改时间 */
	public long ModifiedTime;
	/** 自增序号 */
	public long FlowId;

	public JSONObject toJson() throws JSONException {
		JSONObject localItemObject = new JSONObject();
		localItemObject.put("UserId", UserId);
		localItemObject.put("BankNo", BankNo);
		localItemObject.put("BankName", BankName);
		localItemObject.put("CardType", CardType);
		localItemObject.put("BankAddress", BankAddress);
		localItemObject.put("BankIco", BankIco);
		localItemObject.put("AccountNo", AccountNo);
		localItemObject.put("AccountName", AccountName);
		localItemObject.put("ModifiedTime", ModifiedTime);
		localItemObject.put("FlowId", FlowId);
		return localItemObject;
	}

}
