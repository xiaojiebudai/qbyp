package cn.net.chenbao.qbyp.bean;

import java.util.List;

/**
 * Created by wuri on 2016/11/5.
 * Description:订单日志(包括物流)
 */

public class ShopOrderLog {
    public String DeliverName;
    public String DeliverNo;
    public long LastTime;
    public String LogisticsNo;
    public String Status;
    public List<Logs> Logs;

}
