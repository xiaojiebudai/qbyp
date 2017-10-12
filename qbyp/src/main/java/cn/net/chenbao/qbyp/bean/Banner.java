package cn.net.chenbao.qbyp.bean;

public class Banner {
    /*
    本地首页banner
     */
    public static final int LOCAL_BANNER = 0;
    /*
自营首页banner
 */
    public static final int SELF_BANNER = 1;
    /*
VIP界面banner
*/
    public static final int VIP_BANNER = 2;
    /*
自营广告列表
*/
    public static final int SELF_AD = 3;
    /*
本地首页广告第一行左边
*/
    public static final int LOCAL_AD_0_LEFT = 4;
    /*
本地首页广告第一行右边上
*/
    public static final int LOCAL_AD_0_RIGHT_UP = 5;
    /*
本地首页广告第一行右边下
*/
    public static final int LOCAL_AD_0_RIGHT_DOWN = 6;
    /*
本地首页广告第二行左边
*/
    public static final int LOCAL_AD_1_LEFT = 7;
    /*
本地首页广告第二行右边
*/
    public static final int LOCAL_AD_1_RIGHT = 8;
    /*
本地首页广告第三行
*/
    public static final int LOCAL_AD_2 = 9;
    /*
本地生活轮播
*/
    public static final int LOCAL_Life_BANNER = 10;
    /*
充值中心轮播
*/
    public static final int RECHARGE_CENTER_BANNER = 11;
    /*
幸福专区轮播
*/
    public static final int HAPPY_AREA_BANNER = 12;
    /*
幸福专区左边广告位
*/
    public static final int HAPPY_AREA_AD_LEFT = 13;
    /*
幸福专区右边上
*/
    public static final int HAPPY_AREA_AD_RIGHT_UP = 14;
    /*
幸福专区右边下
*/
    public static final int HAPPY_AREA_AD_RIGHT_DOWN = 15;
    /*
        /*
3.20号修改本地首页四个图
*/
    public static final int LOCAL_LIFE_AD = 16;
    /*
    分销首页banner
    */
    public static final int DISTRIBUTION_BANNER = 17;
    /*
    分销商品AD
    */
    public static final int DISTRIBUTION_AD_LIST = 18;
    /*
自营首页
*/
    public static final String PAGECODE_SELF = "ShopArea";
    /*
VIP首页
*/
    public static final String PAGECODE_VIP = "VipArea";
    /*
积分商城
*/
    public static final String CONSUME_AREA = "ConsumeArea";


    public int BannerId;
    public String BannerName;
    public String PicUrl;
    public int PlaceType;
    public int SortId;
    public int OperType;
    public String OperValue;

}
