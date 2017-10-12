package cn.net.chenbao.qbyp.distribution.been;

import cn.net.chenbao.qbyp.bean.Logs;

import java.util.ArrayList;

/**
 * Created by zxj on 2017/4/16.
 *
 * @description 分销订单
 */

public class DistributionOrder {
    public String Address;      //收货人地址
    public long AddressId;      //地址ID
    public long BuyUserId;      //购买人ID
    public String BuyUserName; //购买人会员名
    public String BuyMemo;
    public int City;            //市
    public String Consignee;    //收货人姓名
    public double Consume;      //赠送积分
    public int County;          //县
    public long CreateTime;     //创建时间
    public String DeliverName;  //物流公司名称
    public String DeliverNo;    //物流公司编号
    public ArrayList<FenxiaoOrderGoods> Goods;
    public ArrayList<Logs> OrderLogs;
    public double GoodsAmt;     //商品金额
    public boolean IsSeller;     //当前用户身份为分销商
    public boolean IsUser;     //当前身份为用户
    public String LogisticsNo; //物流单号
    public long OrderId;        //订单ID
    public String PayCode;      //支付方式
    public long PayId;          //支付ID
    public long PayTime;        //支付时间
    public double PostFee;      //物流费
    public double TotalWeight;      //物流总重
    public int Province;        //省
    public int Quantity;        //购买数量
    public String ReceiverMobile; //收货人电话
    public long ReceiverTime; //收货时间
    public long CancelTime; //取消时间
    public long SellerUserId; //上级代理ID
    public String SellerUserName; //代理名称
    public int SendMode; //物流方式
    public long SendTime; //发货时间
    public int Status; //状态
    public int Street; //街道
    public double TotalAmt; //订单总金额
    public double TotalConsume; //商品积分
    public String ProcotolTitle; //协议标题
    public String ProtcolName; //协议名称
    public int UpLevelId; //(本次升级等级)
    //0-待付款，1-待发货，2-待收货，3-已完成，4-已取消  5 收款审核中
    /**
     * 待付款
     */
    public static final int STATE_WAIT_PAY = 0;
    /**
     * 待发货
     */
    public static final int STATE_WAIT_SEND = 1;
    /**
     * 待收货
     */
    public static final int STATE_WAIT_GET = 2;
    /**
     * 已完成
     */
    public static final int STATE_COMPLETE = 3;
    /**
     * 已取消
     */
    public static final int STATE_CANCEL = 4;
    /**
     * 收款审核中
     */
    public static final int STATE_CHECK = 5;

    public static final String getStateString(int state) {
        String stateString = "";
        switch (state) {
            case STATE_WAIT_PAY:
                stateString = "待支付";
                break;
            case STATE_WAIT_SEND:
                stateString = "待发货";
                break;
            case STATE_WAIT_GET:
                stateString = "待收货";
                break;
            case STATE_COMPLETE:
                stateString = "完成";
                break;
            case STATE_CANCEL:
                stateString = "取消";
                break;
            case STATE_CHECK:
                stateString = "收款审核中";
                break;
            default:
                break;
        }
        return stateString;
    }

}
