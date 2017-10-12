package cn.net.chenbao.qbyp.fragment;

import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.AccountSafeActivity;
import cn.net.chenbao.qbyp.activity.AddressListActivity;
import cn.net.chenbao.qbyp.activity.AgencyMainActivity;
import cn.net.chenbao.qbyp.activity.CircleFriendsActivity;
import cn.net.chenbao.qbyp.activity.CollectActivity;
import cn.net.chenbao.qbyp.activity.EmptyViewActivity;
import cn.net.chenbao.qbyp.activity.IdentityAuthenticationResultActivity;
import cn.net.chenbao.qbyp.activity.MessageListActivity;
import cn.net.chenbao.qbyp.activity.MyWalletActivity;
import cn.net.chenbao.qbyp.activity.OfflineOrderActivity;
import cn.net.chenbao.qbyp.activity.PersonalDataActivity;
import cn.net.chenbao.qbyp.activity.ServiceProviderApplyActivity;
import cn.net.chenbao.qbyp.activity.ServiceProviderShopListActivity;
import cn.net.chenbao.qbyp.activity.SettingActivity;
import cn.net.chenbao.qbyp.activity.ShareActivity;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.api.ApiTrade;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.Order;
import cn.net.chenbao.qbyp.bean.User;
import cn.net.chenbao.qbyp.bean.UserReal;
import cn.net.chenbao.qbyp.distribution.activity.DistributionLevelActivity;
import cn.net.chenbao.qbyp.distribution.activity.DistributionOrderActivity;
import cn.net.chenbao.qbyp.distribution.activity.DistributionWalletActivity;
import cn.net.chenbao.qbyp.distribution.been.DistributionPublicAccount;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.view.QrCodeDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

/**
 * PersonCenterFragment
 *
 * @author xl
 * @date:2016-7-25����10:56:48
 * @description
 */
public class PersonCenterFragment extends FatherFragment implements
        OnClickListener {
    private TextView tv_username,
            tv_news_count;
    private ImageView img_header;
    private ImageView iv_agency;
    private User user;
    private LinearLayout ll_person_info, ll_setting, ll_circle, ll_share, ll_agency,
            ll_address, ll_collect, my_wallet, ll_acountsafe, ll_offline_order, ll_apply_for_fuwushang;
    private RelativeLayout rl_message;
    private boolean isRefresh = true;
    private String userJson;
    private TextView tv_self_wait_pay_tips_num;
    private TextView tv_self_wait_post_tips_num;
    private TextView tv_self_wait_receive_tips_num;
    private TextView tv_ey_wait_pay_tips_num;
    private TextView tv_ey_wait_post_tips_num;
    private TextView tv_ey_wait_receive_tips_num;
    private TextView tv_service;
    private ImageView iv_fuwushang;
    private ImageView user_fenxiaototal;
    private View fake_status_bar;
    private QrCodeDialog codeDialog;
    private PullToRefreshScrollView scrollView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_person_center;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_person_info:// 个人信息
                isRefresh = true;
                PersonCenterFragment.this.startActivity(new Intent(getActivity(),
                        PersonalDataActivity.class));
                break;
            case R.id.rl_message:// 消息列表
                startActivity(new Intent(getActivity(), MessageListActivity.class));
                break;
            case R.id.my_wallet:// 我的钱包
                Intent intent1 = new Intent(getActivity(), MyWalletActivity.class);
                intent1.putExtra(Consts.KEY_DATA, userJson);
                startActivity(intent1);
                break;
            case R.id.ll_collect:  // 我的收藏
                startActivity(new Intent(getActivity(), CollectActivity.class));
                break;
            case R.id.ll_address:// 我的收获地址
                PublicWay.startAddressListActivity(getActivity(), 0,
                        AddressListActivity.DISPLAY);
                break;
            case R.id.ll_agency://代理专区
                startActivity(new Intent(getActivity(), AgencyMainActivity.class));
                break;
            case R.id.ll_offline_order://线下订单
                startActivity(new Intent(getActivity(), OfflineOrderActivity.class));
                break;
            case R.id.ll_apply_for_fuwushang://申请成为服务商
                if (user == null) {
                    return;
                }
                if (user.IsServiceShop) {
                    startActivity(new Intent(getActivity(), ServiceProviderShopListActivity.class));
                } else {
                    if (userReal != null && userReal.Status != 0) {
                        if (userReal.Status == 1) {
                            showIdentityDialog("实名认证进行中，查看进度？", "去查看");
                        } else if (userReal.Status == 3) {
                            showIdentityDialog("实名认证失败，查看结果？", "去查看");
                        } else {
                            isRefresh = true;
                            Intent intent = new Intent(getActivity(),
                                    ServiceProviderApplyActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        showIdentityDialog("为了保证您的资金安全,申请成为服务商,需要先实名认证", "实名认证");
                    }
                }
                break;
            case R.id.ll_circle://推广团队
                startActivity(new Intent(getActivity(), CircleFriendsActivity.class));
                break;
            case R.id.ll_share://分享
                startActivity(new Intent(getActivity(), ShareActivity.class));
                break;
            case R.id.ll_acountsafe://账户安全
                if (user == null) {
                    return;
                }
                Intent intent = new Intent(getActivity(),
                        AccountSafeActivity.class);
                intent.putExtra("phone", user.Mobile);
                startActivity(intent);
                break;
            case R.id.ll_setting://系统设置
                startActivity(new Intent(getActivity(),
                        SettingActivity.class));
                break;
            case R.id.ll_self_wait_pay://自营订单
                PublicWay.startSelfSupportOrderActivity(this, Order.STATE_WAIT_PAY);
                break;
            case R.id.ll_self_wait_post:
                PublicWay.startSelfSupportOrderActivity(this, Order.STATE_WAIT_SELLER_CONFIRM);
                break;
            case R.id.ll_self_wait_receive:
                PublicWay.startSelfSupportOrderActivity(this, Order.STATE_WAIT_BUYER_CONFIRM);
                break;
            case R.id.ll_self_all_order:
                PublicWay.startSelfSupportOrderActivity(this, -1);
                break;
            case R.id.ll_ey_wait_pay://本地订单
                PublicWay.startLocalLifeOrderActivity(this, Order.STATE_WAIT_PAY);
                break;
            case R.id.ll_ey_wait_post:
                PublicWay.startLocalLifeOrderActivity(this, Order.STATE_WAIT_SELLER_CONFIRM);
                break;
            case R.id.ll_ey_wait_receive:
                PublicWay.startLocalLifeOrderActivity(this, Order.STATE_WAIT_BUYER_CONFIRM);
                break;
            case R.id.ll_ey_all_order:
                PublicWay.startLocalLifeOrderActivity(this, -1);
                break;
            case R.id.iv_erweima:
                getQrInfo();
                break;
//            case R.id.ll_u_in:
//                //进货
//                PublicWay.startDistributionOrderActivity(this, DistributionOrderActivity.IN);
//                break;
//            case R.id.ll_u_out:
//                //销货
//                showWaitDialog();
//                x.http().get(ParamsUtils.getSessionParams(ApiDistribution.isFenxiaoAgent()), new WWXCallBack("IsFenxiaoAgent") {
//                    @Override
//                    public void onAfterSuccessOk(JSONObject data) {
//                        if (data.getBoolean("Data")) {
//                            PublicWay.startDistributionOrderActivity(PersonCenterFragment.this, DistributionOrderActivity.OUT);
//                        } else {
//                            PublicWay.startEmptyViewActivity(PersonCenterFragment.this, EmptyViewActivity.DISTRIBUTION_ORDER);
//                        }
//                    }
//
//                    @Override
//                    public void onAfterFinished() {
//                        dismissWaitDialog();
//                    }
//                });
//
//
//                break;
//            case R.id.ll_u_wallet:
//                //收益
//                startActivity(new Intent(getActivity(),
//                        DistributionWalletActivity.class));
//                break;
//            case R.id.ll_level:
//                //等级
//                startActivity(new Intent(getActivity(),
//                        DistributionLevelActivity.class));
//                break;
            default:
                break;
        }
    }

    /**
     * 获取二维码信息
     */
    private void getQrInfo() {
        RequestParams params = new RequestParams(ApiUser.getInviterGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("InviterGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                user = JSON.parseObject(data.getString("Data"), User.class);
                if (codeDialog == null) {
                    codeDialog = new QrCodeDialog(
                            getActivity(), R.style.DialogStyle);
                    codeDialog.setTitle("我的二维码");
                }
                codeDialog.setImg(user.BarcodeUrl);
                codeDialog.show();
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    @Override
    protected void initView() {
        tv_username = (TextView) mGroup.findViewById(R.id.tv_username);
        tv_news_count = (TextView) mGroup.findViewById(R.id.tv_news_count);
        img_header = (ImageView) mGroup.findViewById(R.id.img_header);
        user_fenxiaototal = (ImageView) mGroup.findViewById(R.id.distribution_level);

        ll_person_info = (LinearLayout) mGroup
                .findViewById(R.id.ll_person_info);
        iv_agency = (ImageView) mGroup
                .findViewById(R.id.iv_agency);
        iv_fuwushang = (ImageView) mGroup
                .findViewById(R.id.iv_fuwushang);
        ll_setting = (LinearLayout) mGroup.findViewById(R.id.ll_setting);
        ll_circle = (LinearLayout) mGroup.findViewById(R.id.ll_circle);
        ll_share = (LinearLayout) mGroup.findViewById(R.id.ll_share);
        ll_address = (LinearLayout) mGroup.findViewById(R.id.ll_address);
        ll_collect = (LinearLayout) mGroup.findViewById(R.id.ll_collect);
        ll_offline_order = (LinearLayout) mGroup.findViewById(R.id.ll_offline_order);
        ll_apply_for_fuwushang = (LinearLayout) mGroup.findViewById(R.id.ll_apply_for_fuwushang);
        tv_service = (TextView) mGroup.findViewById(R.id.tv_service);
        my_wallet = (LinearLayout) mGroup.findViewById(R.id.my_wallet);
        ll_acountsafe = (LinearLayout) mGroup.findViewById(R.id.ll_acountsafe);
        ll_agency = (LinearLayout) mGroup.findViewById(R.id.ll_agency);
        rl_message = (RelativeLayout) mGroup.findViewById(R.id.rl_message);

        tv_self_wait_pay_tips_num = (TextView) mGroup.findViewById(R.id.tv_self_wait_pay_tips_num);
        tv_self_wait_post_tips_num = (TextView) mGroup.findViewById(R.id.tv_self_wait_post_tips_num);
        tv_self_wait_receive_tips_num = (TextView) mGroup.findViewById(R.id.tv_self_wait_receive_tips_num);
        mGroup.findViewById(R.id.ll_self_wait_pay).setOnClickListener(this);
        mGroup.findViewById(R.id.ll_self_wait_post).setOnClickListener(this);
        mGroup.findViewById(R.id.ll_self_wait_receive).setOnClickListener(this);
        mGroup.findViewById(R.id.ll_self_all_order).setOnClickListener(this);
        //TODO 分销系统注释掉
//        mGroup.findViewById(R.id.ll_u_in).setOnClickListener(this);
//        mGroup.findViewById(R.id.ll_u_out).setOnClickListener(this);
//        mGroup.findViewById(R.id.ll_u_wallet).setOnClickListener(this);
//        mGroup.findViewById(R.id.ll_level).setOnClickListener(this);


        tv_ey_wait_pay_tips_num = (TextView) mGroup.findViewById(R.id.tv_ey_wait_pay_tips_num);
        tv_ey_wait_post_tips_num = (TextView) mGroup.findViewById(R.id.tv_ey_wait_post_tips_num);
        tv_ey_wait_receive_tips_num = (TextView) mGroup.findViewById(R.id.tv_ey_wait_receive_tips_num);
        scrollView = (PullToRefreshScrollView) mGroup.findViewById(R.id.scroll_container);
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ObservableScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ObservableScrollView> refreshView) {
                getUserInfo();
//                getDistribution();
                realGet();
                getMessageInfo();
                getSelfOrderStatusCount();//1待发货  2待收货
                getLocalOrderStautsCount();//0待付款 1待接单 2待收货
            }
        });
        mGroup.findViewById(R.id.ll_ey_wait_pay).setOnClickListener(this);
        mGroup.findViewById(R.id.ll_ey_wait_post).setOnClickListener(this);
        mGroup.findViewById(R.id.ll_ey_wait_receive).setOnClickListener(this);
        mGroup.findViewById(R.id.ll_ey_all_order).setOnClickListener(this);
        mGroup.findViewById(R.id.iv_erweima).setOnClickListener(this);
        my_wallet.setOnClickListener(this);
        ll_person_info.setOnClickListener(this);
        ll_setting.setOnClickListener(this);
        ll_address.setOnClickListener(this);
        ll_collect.setOnClickListener(this);
        ll_circle.setOnClickListener(this);
        ll_share.setOnClickListener(this);
        rl_message.setOnClickListener(this);
        ll_acountsafe.setOnClickListener(this);
        ll_agency.setOnClickListener(this);
        ll_offline_order.setOnClickListener(this);
        ll_apply_for_fuwushang.setOnClickListener(this);

        fake_status_bar = mGroup.findViewById(R.id.fake_status_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            ViewGroup.LayoutParams params1 = (ViewGroup.LayoutParams) fake_status_bar.getLayoutParams();
            params1.height = WWViewUtil.getStatusBarHeight(getActivity());
            fake_status_bar.setLayoutParams(params1);
            fake_status_bar.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onResume() {
        if (isRefresh) {
            getUserInfo();
            isRefresh = false;
        }
//        getDistribution();
        realGet();
        getMessageInfo();
        getSelfOrderStatusCount();//1待发货  2待收货
        getLocalOrderStautsCount();//0待付款 1待接单 2待收货
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            getDistribution();
        }
    }

    private void getDistribution() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiDistribution.MyAccount());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        x.http().get(params, new WWXCallBack("MyAccount") {

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                DistributionPublicAccount accountInfo = JSON.parseObject(data.getString("Data"),
                        DistributionPublicAccount.class);
                switch (accountInfo.LevelId) {
                    //LevelId ;//0消费者，2小代，3总代，4专卖店
                    case 0:
                        user_fenxiaototal.setVisibility(View.GONE);
                        break;
                    case 2:
                        user_fenxiaototal.setVisibility(View.VISIBLE);
                        user_fenxiaototal.setImageResource(R.drawable.xiaodai_icon);
                        break;
                    case 3:
                        user_fenxiaototal.setVisibility(View.VISIBLE);
                        user_fenxiaototal.setImageResource(R.drawable.zondai_icon);
                        break;
                    case 4:
                        user_fenxiaototal.setVisibility(View.VISIBLE);
                        user_fenxiaototal.setImageResource(R.drawable.zhuanmaidian_icon);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void getSelfOrderStatusCount() {
        RequestParams params = new RequestParams(ApiShop.OrderStatusCount());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("status", "0,1,2");
        x.http().get(params, new WWXCallBack("OrderStatusCount") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                List<Integer> list = JSONArray.parseArray(data.getString("Data"), Integer.class);
                showOrHideCountTip(tv_self_wait_pay_tips_num, list.get(0));
                showOrHideCountTip(tv_self_wait_post_tips_num, list.get(1));
                showOrHideCountTip(tv_self_wait_receive_tips_num, list.get(2));
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    private void getLocalOrderStautsCount() {
        RequestParams params = new RequestParams(ApiTrade.OrderStatusCount());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("status", "0,1,2");
        x.http().get(params, new WWXCallBack("OrderStatusCount") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                List<Integer> list = JSONArray.parseArray(data.getString("Data"), Integer.class);
                showOrHideCountTip(tv_ey_wait_pay_tips_num, list.get(0));
                showOrHideCountTip(tv_ey_wait_post_tips_num, list.get(1));
                showOrHideCountTip(tv_ey_wait_receive_tips_num, list.get(2));
            }

            @Override
            public void onAfterFinished() {
                scrollView.onRefreshComplete();
            }
        });
    }

    private void showOrHideCountTip(TextView view, int count) {
        if (count == 0) {
            view.setVisibility(View.GONE);
        } else {
            view.setText((count < 100) ? count + "" : "99");
            view.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.getUserInfo());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        x.http().get(params, new WWXCallBack("InfoGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                userJson = data.getString("Data");
                user = JSON.parseObject(data.getString("Data"), User.class);
                SharedPreferenceUtils.getInstance().savePhoneNum(user.Mobile);
                ImageUtils.setCircleHeaderImage(getActivity(), user.HeadUrl,
                        img_header);
                tv_username.setText(user.UserName);
                if (user.IsAgent) {
                    ll_agency.setVisibility(View.VISIBLE);
                    iv_agency.setVisibility(View.VISIBLE);
                } else {
                    ll_agency.setVisibility(View.GONE);
                    iv_agency.setVisibility(View.GONE);
                }

                if (user.IsServiceShop) {
                    tv_service.setText(R.string.service_shop);
                    iv_fuwushang.setVisibility(View.VISIBLE);
                } else {
                    iv_fuwushang.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }

    /**
     * 获取消息数据
     */
    private void getMessageInfo() {
        RequestParams params = new RequestParams(ApiUser.getMessageCount());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());

        x.http().get(params, new WWXCallBack("InterMessageCount") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (data.getString("Data").equals("0")) {
                    tv_news_count.setVisibility(View.GONE);
                } else {
                    tv_news_count.setVisibility(View.VISIBLE);
                    tv_news_count.setText((data.getIntValue("Data") < 100) ? data.getString("Data") : "99");
                }
            }

            @Override
            public void onAfterFinished() {
            }
        });
    }

    private UserReal userReal;

    private void realGet() {
        RequestParams params = new RequestParams(ApiUser.RealGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("RealGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                // TODO Auto-generated method stub
                // 0 待提交，1待审核，2审核通过，3审核不通过
                userReal = JSON.parseObject(data.getString("Data"), UserReal.class);
            }

            @Override
            public void onAfterFinished() {
                // TODO Auto-generated method stub

            }
        });
    }

    private void showIdentityDialog(String tipsStr, String confirmRight) {
        final CommonDialog commonDialog = DialogUtils.getSingleBtnDialog(getActivity(), tipsStr, confirmRight, true);
        commonDialog.getButtonRight().setTextColor(getResources().getColor(R.color.yellow_ww));
        commonDialog.getButtonRight(confirmRight).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userReal != null && userReal.Status != 0) {
                    if (userReal.Status == 1) {
                        PublicWay.startIdentityAuthenticationResultActivity(
                                getActivity(),
                                IdentityAuthenticationResultActivity.IDENTITYING,
                                "");
                    } else if (userReal.Status == 3) {
                        PublicWay
                                .startIdentityAuthenticationResultActivity(
                                        getActivity(),
                                        IdentityAuthenticationResultActivity.IDENTITYDEFAULT,
                                        userReal.Explain);
                    }
                } else {
                    PublicWay.stratIdentityAuthenticationActivity(getActivity());
                }
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }
}
