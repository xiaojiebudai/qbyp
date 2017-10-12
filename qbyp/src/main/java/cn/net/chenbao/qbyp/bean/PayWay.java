package cn.net.chenbao.qbyp.bean;

/**
 * 支付实体
 *
 * @author Administrator
 */
public class PayWay {

    /* 是否开启 */
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

    //busType获取业务所需的支付方式(PaymentValid)  业务类型 :  -1 商家续费    1订单付款     2充值    3自营订单付款    4线下收款   5服务商升级   6分销订单付款
}
