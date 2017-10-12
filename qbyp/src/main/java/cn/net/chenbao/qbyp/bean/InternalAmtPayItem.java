package cn.net.chenbao.qbyp.bean;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by ZXJ on 2017/6/30.
 */

public class InternalAmtPayItem {
    public long FlowId;
    public long OrderId;//（订单号），
    public String PayName;//（名称），
    public double UseNum;//（应付额度），
    public double UseAmt;//（实扣额度），
    public float UseRate;//（比率），
    public long UseMode;//（方式，1积分，2现金），
    public int Status;
    public double Balance;//（可用额度）
    public String PayCode;//（支付编号），
    public boolean UseFlag;//（是否使用）

    public JSONObject toJson() throws JSONException {
        JSONObject localItemObject = new JSONObject();
        localItemObject.put("PayCode", PayCode);
        localItemObject.put("UseFlag", UseFlag);
        return localItemObject;
    }
}
