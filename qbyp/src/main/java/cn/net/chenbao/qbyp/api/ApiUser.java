package cn.net.chenbao.qbyp.api;

public class ApiUser {
    /**
     * 用户类接口公共字段
     */
    public static final String USER_JSON = "/User/Json/";
    /**
     * 用户信息
     */
    public static final String USER_API = Api.WW_ONLINE + USER_JSON;

    /**
     * 注册用户
     *
     * @return
     */
    public static final String registerUser() {
        return USER_API + "Register";
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public static final String getVerificationCode() {
        return USER_API + "AppRegisterSms";
    }

    /**
     * 获取极验验证参数
     *
     * @return
     */
    public static final String getGeeValid() {
        return USER_API + "GeeValid";
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public static final String RegisterSms() {
        return USER_API + "RegisterSms";
    }

    /**
     * 登录
     *
     * @return
     */
    public static final String login() {
        return USER_API + "login";
    }

    /**
     * 忘记密码验证手机号
     *
     * @return
     */
    public static final String findPswVerify() {
        return USER_API + "LogPsdForgetSms";
    }

    /**
     * 忘记支付密码验证手机号
     *
     * @return
     */
    public static final String PaypsdForgetSms() {
        return USER_API + "PaypsdForgetSms";
    }

    /**
     * 修改密码
     *
     * @return
     */
    public static final String setNewPassword() {
        return USER_API + "LogPsdForget";
    }

    /**
     * 修改支付密码
     *
     * @return
     */
    public static final String PaypsdForget() {
        return USER_API + "PaypsdForget";
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static final String getUserInfo() {
        return USER_API + "InfoGet";
    }

    /***
     * Description:获取会员资金账号 Company: wangwanglife Version：1.0
     *
     * @return
     * @author ZXJ
     * @date @2016-7-30
     ***/
    public static final String getAccountInfo() {
        return USER_API + "AccountGet";
    }

    /**
     * 获取未读消息数量
     */
    public static final String getMessageCount() {
        return USER_API + "InterMessageCount";
    }

    /**
     * 修改用户名
     */
    public static final String getUpdateName() {
        return USER_API + "InfoNameUpdate";
    }

    /**
     * 修改性别
     */
    public static final String getUpdateSex() {
        return USER_API + "InfoUpdate";
    }

    /**
     * 修改性别
     */
    public static final String getUpdateHeader() {
        return USER_API + "InfoHeadUpdate";
    }

    /**
     * 上传图片
     */
    public static final String getUpImg() {
        return Api.WW_ONLINE_IMAGE + "UpImage";
    }

    /**
     * 提现明细
     */
    public static final String getTixianDes() {
        return USER_API + "CashDetail";
    }

    /**
     * wwb明细
     */
    public static final String getWWBDes() {
        return USER_API + "WwbDetail";
    }

    /**
     * 消费返现明细
     */
    public static final String getRebateSaleDetail() {
        return USER_API + "RebateSaleDetail";
    }

    /**
     * 获取会员预返现变化明细
     */
    public static final String getRebateDetail() {
        return USER_API + "RebateDetail";
    }

    /**
     * 获取会员可消费返现变化明细
     */
    public static final String getInternalDetail() {
        return USER_API + "InternalDetail";
    }

    /**
     * 会员专属余额变化明细
     */
    public static final String RebateVipaccDetail() {
        return USER_API + "RebateVipaccDetail";
    }

    /**
     * 静态金额明细
     */
    public static final String RebateStaticDetail() {
        return USER_API + "RebateStaticDetail";
    }

    /**
     * 获取会员余额变化明细
     */
    public static final String getBalanceDetail() {
        return USER_API + "BalanceDetail";
    }

    /**
     * 地区代理分润明细
     */
    public static final String getRebateRegionDetail() {
        return USER_API + "RebateRegionDetail";
    }

    /**
     * 推荐商家分润明细
     */
    public static final String getRebateRecSellerDetail() {
        return USER_API + "RebateRecSellerDetail";
    }

    /**
     * 粉丝分润明细
     */
    public static final String getRebateParentDetail() {
        return USER_API + "RebateParentDetail";
    }

    /**
     * 取会员站内信列表
     */
    public static final String getInterMessage() {
        return USER_API + "InterMessage";
    }

    /**
     * 取会员收藏列表
     */
    public static final String getFavSellerGet() {
        return USER_API + "FavSellerGet";
    }

    /**
     * 读取站内信
     */
    public static final String getInterMessageRead() {
        return USER_API + "InterMessageRead";
    }

    /**
     * 取我的团队列表
     */
    public static final String getTeamsGet() {
        return USER_API + "TeamsGet";
    }

    /**
     * 实名认证资料提交 POST
     */
    public static final String getRealSubmit() {
        return USER_API + "RealSubmit";
    }

    /**
     * 退出登录
     */
    public static final String getLogout() {
        return USER_API + "Logout";
    }

    /**
     * 会员定位地址上报
     */
    public static final String getLocationSubmit() {
        return USER_API + "LocationSubmit";
    }

    /**
     * 取会员最后定位地址
     */
    public static final String getLocationGet() {
        return USER_API + "LocationGet";
    }

    /**
     * 获取会员分享条码，该接口会判断当前会员是否有生成分享码，如果没有则生成，如果有则直接返回，邀请码暂时等于手机号
     */
    public static final String getInviterGet() {
        return USER_API + "InviterGet";
    }

    /**
     * 判断当前会员收藏状态
     */
    public static final String getFavSellerStatus() {
        return USER_API + "FavSellerStatus";
    }

    /**
     * 移除收藏商家
     */
    public static final String getFavSellerRemove() {
        return USER_API + "FavSellerRemove";
    }

    /**
     * 收藏商家
     */
    public static final String getFavSellerAdd() {
        return USER_API + "FavSellerAdd";
    }

    /**
     * 设置会员默认地址
     */
    public static final String getDefaultAddressSet() {
        return USER_API + "DefaultAddressSet";
    }

    /**
     * 取会员默认地址
     */
    public static final String getDefaultAddressGet() {
        return USER_API + "DefaultAddressGet";
    }

    /**
     * 地址信息提交 post
     */
    public static final String getAddressSubmit() {
        return USER_API + "AddressSubmit";
    }

    /**
     * 取会员地址
     */
    public static final String getAddressGet() {
        return USER_API + "AddressGet";
    }

    /**
     * 删除会员地址
     */
    public static final String getAddressDelete() {
        return USER_API + "AddressDelete";
    }

    /**
     * 会员申请提现
     */
    public static final String getCashSubmit() {
        return USER_API + "CashSubmit";
    }

    /**
     * 银行卡信息提交 POST
     */
    public static final String getBankSubmit() {
        return USER_API + "BankSubmit";
    }

    /**
     * 取会员银行卡列表
     */
    public static final String getBanksGet() {
        return USER_API + "BanksGet";
    }

    /**
     * 会员银行卡删除 POST
     */
    public static final String getBankDelete() {
        return USER_API + "BankDelete";
    }

    /**
     * 收藏商家
     *
     * @return
     */
    public static final String addCollectShop() {
        return USER_API + "FavSellerAdd";
    }

    /**
     * 移除收藏
     *
     * @return
     */
    public static final String removeCollectShop() {
        return USER_API + "FavSellerRemove";
    }

    /**
     * 获得分享二维码和邀请码
     *
     * @return
     */
    public static final String getSharErweima() {
        return USER_API + "InviterGet";
    }

    /***
     * Description:修改登录密码
     *
     * @return
     * @author ZXJ
     * @date 2016-8-2
     */
    public static final String getLogpsdChange() {
        return USER_API + "LogpsdChange";
    }

    /***
     * Description:修改登录密码
     *
     * @return
     * @author ZXJ
     * @date 2016-8-2
     */
    public static final String getPaypsdChange() {
        return USER_API + "PaypsdChange";
    }

    /***
     * Description:是否有支付密码
     *
     * @return
     * @author
     * @date 2016-8-2
     */
    public static final String HavePaypsd() {
        return USER_API + "HavePaypsd";
    }

    /**
     * 获取认证状态
     *
     * @return
     */
    public static final String RealGet() {
        return USER_API + "RealGet";
    }

    /**
     * 修改认证状态
     *
     * @return
     */
    public static final String RealRewrite() {
        return USER_API + "RealRewrite";
    }

    /**
     * 设置推荐人
     *
     * @return
     */
    public static final String ChangeInviter() {
        return USER_API + "ChangeInviter";
    }

    /**
     * 消息已读
     *
     * @return
     */
    public static final String InterMessageRead() {
        return USER_API + "InterMessageRead";
    }

    /**
     * 取未领红包个数
     *
     * @return
     */
    public static final String BonusCount() {
        return USER_API + "BonusCount";
    }

    /**
     * 打开红包
     *
     * @return
     */
    public static final String BonusOpen() {
        return USER_API + "BonusOpen";
    }

    /***
     * Description: 取会员可提现金额
     *
     * @return
     * @author ZXJ
     * @date 2016-10-13
     ***/
    public static final String AllowCashAccount() {
        return USER_API + "AllowCashAccount";
    }

    /**
     * 充值获取订单接口
     *
     * @return
     */
    public static final String VipSaving() {
        return USER_API + "VipSaving";
    }

    /**
     * 解冻中积分列表
     *
     * @return
     */
    public static final String IntegralDeblockingGet() {
        return USER_API + "IntegralDeblockingGet";
    }

    /**
     * 待解冻积分明细或总积分明细
     *
     * @return
     */
    public static final String IntegralDetailGet() {
        return USER_API + "IntegralDetailGet";
    }

    /**
     * 可用积分明细
     *
     * @return
     */
    public static final String IntegralConsumeGet() {
        return USER_API + "IntegralConsumeGet";
    }
    /**
     * 服务商申请
     *
     * @return
     */
    public static final String ServiceShopSubmit() {
        return USER_API + "ServiceShopSubmit";
    }
    /**
     * 我的服务商家
     *
     * @return
     */
    public static final String MySellers() {
        return USER_API + "MySellers";
    }

    /**
     * 取柒宝币当前价格
     *
     * @return
     */
    public static final String QbbPrice() {
        return USER_API + "QbbPrice";
    }
    /**
     * 使用余额购买柒宝币
     *
     * @return
     */
    public static final String QbbBuy() {
        return USER_API + "QbbBuy";
    }
    /**
     * 售卖柒宝币，得到余额
     *
     * @return
     */
    public static final String QbbSale() {
        return USER_API + "QbbSale";
    }
    /**
     * 柒宝币转赠
     *
     * @return
     */
    public static final String QbbGive() {
        return USER_API + "QbbGive";
    }
}
