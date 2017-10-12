package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.ObservableScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.AccountInfo;
import cn.net.chenbao.qbyp.bean.UnfreezeingListBean;
import cn.net.chenbao.qbyp.bean.User;
import cn.net.chenbao.qbyp.bean.UserReal;
import cn.net.chenbao.qbyp.dialog.QbbNumInputDialog;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

public class MyWalletActivity extends FatherActivity implements OnClickListener {
    private AccountInfo accountInfo;
    private LinearLayout ll_yue,
            withdraw, ll_bank_card, ll_wait_unfreeze, ll_total_integral;
    private TextView tv_yue, withdraw_des,
            tv_can_use_integral, tv_wait_unfreeze, tv_total_integral, tv_qibaobinum,
            tv_qibaobides, tv_price_today, tv_qbball,tv_huigou,tv_zhuanzeng,tv_goumai;
    private String userJson;
    private User user;
    public static final int REQUEST_CODE = 10086;
    private PullToRefreshScrollView mScrollView;
    private int mCurrentPage = 0;
    private LinearLayout ll_container;
    private RelativeLayout ll_can_use_integral;
    private View ll_view;
    private View fl_back_to_top;
    private View emptyview;
    private int totalCount;
    private final int PullDown = 1;
    private final int PullUp = 2;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_yue:
                PublicWay.startPersonPublicListDesActivity(MyWalletActivity.this,
                        PersonPublicListDesActivity.YUEDES);
                break;
            case R.id.ll_can_use_integral:
                PublicWay.startPersonPublicListDesActivity(MyWalletActivity.this,
                        PersonPublicListDesActivity.WWBDES);
                break;
            case R.id.ll_total_integral:
                PublicWay.startPersonPublicListDesActivity(MyWalletActivity.this,
                        PersonPublicListDesActivity.TOTALINTEGRALDES);
                break;
            case R.id.withdraw:
                if (userReal != null && userReal.Status != 0) {
                    if (userReal.Status == 1) {
                        showIdentityDialog("实名认证进行中，查看进度？", "去查看");
                    } else if (userReal.Status == 3) {
                        showIdentityDialog("实名认证失败，查看结果？", "去查看");
                    } else {
                        if (accountInfo != null) {
                            PublicWay.stratWithdrawActivity(MyWalletActivity.this, accountInfo.CashLimit, WithdrawActivity.PERSON_WITHDRAW, 8888);
                        }
                    }
                } else {
                    showIdentityDialog("还未进行实名认证", "去认证");
                }
                break;
            case R.id.withdraw_des:
                PublicWay.startPersonPublicListDesActivity(MyWalletActivity.this,
                        PersonPublicListDesActivity.TIXIANDES);
                break;
            case R.id.ll_bank_card:
                if (userReal != null && userReal.Status != 0) {
                    if (userReal.Status == 1) {
                        showIdentityDialog("实名认证进行中，查看进度？", "去查看");
                    } else if (userReal.Status == 3) {
                        showIdentityDialog("实名认证失败，查看结果？", "去查看");
                    } else {
                        startActivity(new Intent(MyWalletActivity.this,
                                BankListActivity.class));
                    }
                } else {
                    showIdentityDialog("还未进行实名认证", "去认证");
                }

                break;
            case R.id.fl_back_to_top:
                ScrollView scrollview = mScrollView.getRefreshableView();
                scrollview.scrollTo(10, 10);
                break;
            case R.id.tv_qibaobides:
                //七宝币明细
                PublicWay.startPersonPublicListDesActivity(MyWalletActivity.this,
                        PersonPublicListDesActivity.CANCUSTOMEDES);
                break;

            case R.id.tv_huigou:
                final QbbNumInputDialog qbbNumDialog1=new QbbNumInputDialog(this,qbbPrice,"可兑换","确认回购");
                qbbNumDialog1.getTv_ok().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(qbbNumDialog1.getInput()>0){
                            showWaitDialog();
                            JSONObject object=new JSONObject();
                            object.put("sessionId", WWApplication.getInstance()
                                    .getSessionId());
                            object.put("num", qbbNumDialog1.getInput()+"");
                            x.http().post(ParamsUtils.getPostJsonParams(object,ApiUser.QbbSale()), new WWXCallBack("QbbSale") {

                                @Override
                                public void onAfterFinished() {
                                    dismissWaitDialog();
                                }

                                @Override
                                public void onAfterSuccessOk(JSONObject data) {
                                    WWToast.showShort("回购成功");
                                    qbbNumDialog1.dismiss();
                                    getMoneyNum();
                                }
                            });
                        }
                    }
                });
                qbbNumDialog1.show();
                break;
            case R.id.tv_zhuanzeng:
                Intent intent=new Intent(this,QbbsendActivity.class);
                intent.putExtra(Consts.KEY_DATA,accountInfo.InternalBalance);
                startActivity(intent);

                break;
            case R.id.tv_goumai:
                final QbbNumInputDialog qbbNumDialog=new QbbNumInputDialog(this,qbbPrice,"所需价格","确认购买");
                qbbNumDialog.getTv_ok().setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(qbbNumDialog.getInput()>0){
                            showWaitDialog();
                            RequestParams params = new RequestParams();



                            JSONObject object=new JSONObject();
                            object.put("sessionId", WWApplication.getInstance()
                                    .getSessionId());
                            object.put("num", qbbNumDialog.getInput()+"");
                            x.http().post( ParamsUtils.getPostJsonParams(object,ApiUser.QbbBuy()), new WWXCallBack("QbbBuy") {

                                @Override
                                public void onAfterFinished() {
                                    dismissWaitDialog();
                                }

                                @Override
                                public void onAfterSuccessOk(JSONObject data) {
                                  WWToast.showShort("购买成功");
                                    qbbNumDialog.dismiss();
                                    getMoneyNum();
                                }
                            });
                        }
                    }
                });
                qbbNumDialog.show();
                break;
            default:
                break;
        }
    }

    private void showIdentityDialog(String tipsStr, String confirmRight) {
        final CommonDialog commonDialog = DialogUtils.getCommonDialogTwiceConfirm(this, tipsStr, true);
        commonDialog.getButtonRight(confirmRight).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userReal != null && userReal.Status != 0) {
                    if (userReal.Status == 1) {
                        PublicWay.startIdentityAuthenticationResultActivity(
                                MyWalletActivity.this,
                                IdentityAuthenticationResultActivity.IDENTITYING,
                                "");
                    } else if (userReal.Status == 3) {
                        PublicWay
                                .startIdentityAuthenticationResultActivity(
                                        MyWalletActivity.this,
                                        IdentityAuthenticationResultActivity.IDENTITYDEFAULT,
                                        userReal.Explain);
                    }
                } else {
                    PublicWay.stratIdentityAuthenticationActivity(MyWalletActivity.this);
                }
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(arg0, arg1, arg2);
        if (arg0 == 8888 && arg1 == RESULT_OK) {
            getMoneyNum();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_mywallet;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initValues() {
        userJson = getIntent().getStringExtra(Consts.KEY_DATA);
        user = JSON.parseObject(userJson, User.class);
        initDefautHead(R.string.my_wallet, true);
    }

    @Override
    protected void initView() {
        ll_view = findViewById(R.id.ll_view);
        emptyview = LayoutInflater.from(this).inflate(
                R.layout.emptyview, null);
        mScrollView = (PullToRefreshScrollView) findViewById(R.id.scrollview_wallte);
        mScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);

        ll_yue = (LinearLayout) findViewById(R.id.ll_yue);
        tv_yue = (TextView) findViewById(R.id.tv_yue);
        withdraw = (LinearLayout) findViewById(R.id.withdraw);
        withdraw_des = (TextView) findViewById(R.id.withdraw_des);
        ll_bank_card = (LinearLayout) findViewById(R.id.ll_bank_card);
        ll_can_use_integral = (RelativeLayout) findViewById(R.id.ll_can_use_integral);
        tv_can_use_integral = (TextView) findViewById(R.id.tv_can_use_integral);
//        ll_wait_unfreeze = (LinearLayout) findViewById(R.id.ll_wait_unfreeze);
        tv_wait_unfreeze = (TextView) findViewById(R.id.tv_wait_unfreeze);
        ll_total_integral = (LinearLayout) findViewById(R.id.ll_total_integral);
        tv_total_integral = (TextView) findViewById(R.id.tv_total_integral);
        tv_qibaobinum = (TextView) findViewById(R.id.tv_qibaobinum);
        tv_qibaobides = (TextView) findViewById(R.id.tv_qibaobides);
        tv_qbball = (TextView) findViewById(R.id.tv_qbball);
        tv_price_today = (TextView) findViewById(R.id.tv_price_today);
        //tv_huigou,tv_zhuanzeng,tv_gouma
        tv_huigou = (TextView) findViewById(R.id.tv_huigou);
        tv_zhuanzeng = (TextView) findViewById(R.id.tv_zhuanzeng);
        tv_goumai = (TextView) findViewById(R.id.tv_goumai);
        fl_back_to_top = findViewById(R.id.fl_back_to_top);


        tv_huigou.setOnClickListener(this);
        tv_zhuanzeng.setOnClickListener(this);
        tv_goumai.setOnClickListener(this);
        ll_yue.setOnClickListener(this);
        withdraw_des.setOnClickListener(this);
        withdraw.setOnClickListener(this);
        tv_qibaobides.setOnClickListener(this);
        ll_bank_card.setOnClickListener(this);
        ll_can_use_integral.setOnClickListener(this);
//        ll_wait_unfreeze.setOnClickListener(this);
        ll_total_integral.setOnClickListener(this);
        fl_back_to_top.setOnClickListener(this);
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ObservableScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ObservableScrollView> refreshView) {
                mCurrentPage = 0;
                getMoneyNum();
                requestData(PullDown);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ObservableScrollView> refreshView) {
                if (mCurrentPage >= totalCount) {
                    WWToast.showShort(R.string.nomore_data);
                    mScrollView.onRefreshComplete();
                } else {
                    requestData(PullUp);
                }
            }
        });
    }

    /**
     * 获取解冻中积分数据
     */
    private void requestData(final int mode) {
        showWaitDialog();

        RequestParams params = new RequestParams(ApiUser.IntegralDeblockingGet());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        params.addBodyParameter("pageIndex", mCurrentPage + "");
        params.addBodyParameter("pageSize", 20 + "");
        x.http().get(params, new WWXCallBack("IntegralDeblockingGet") {

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                mScrollView.onRefreshComplete();
            }

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (mode == PullDown) {
                    ll_container.removeAllViews();
                } else if (mode == PullUp) {
                    ll_container.removeView(emptyview);
                }
                totalCount = data.getIntValue("PageCount");
                JSONArray jsonArray = data.getJSONArray("Data");
                List<UnfreezeingListBean> list = JSON.parseArray(jsonArray.toJSONString(), UnfreezeingListBean.class);
                if (list.size() == 0 && mCurrentPage == 0) {
                    ll_container.addView(emptyview);
                }

                mCurrentPage++;
                for (int i = 0; i < list.size(); i++) {//
                    View itemView = LayoutInflater.from(MyWalletActivity.this).inflate(R.layout.wallet_unfreeze_item, null);
                    ImageView iv_unfreeze_star = (ImageView) itemView.findViewById(R.id.iv_unfreeze_star);
                    TextView tv_integral = (TextView) itemView.findViewById(R.id.tv_integral);
                    TextView tv_unfreeze_rate = (TextView) itemView.findViewById(R.id.tv_unfreeze_rate);
                    TextView tv_unfreeze_finished = (TextView) itemView.findViewById(R.id.tv_unfreeze_finished);
                    TextView tv_not_unfreeze_or_finish = (TextView) itemView.findViewById(R.id.tv_not_unfreeze_or_finish);
                    tv_integral.setText(String.format(getString(R.string.integral_), WWViewUtil.numberFormatWithTwo(list.get(i).Integral)));
                    tv_unfreeze_rate.setText(String.format(getString(R.string.freeze_rate_), WWViewUtil.numberFormatWithTwo(list.get(i).DeblockingRate * 100, "0.000")));
                    tv_unfreeze_finished.setText(String.format(getString(R.string.freezed_), WWViewUtil.numberFormatWithTwo(list.get(i).DeblockedIntegral)));

                    // 根据是否完成确定星星图标
                    if (list.get(i).Status == 0) {
                        iv_unfreeze_star.setBackgroundResource(list.get(i).Account < 5000 ? R.drawable.halfheart_icon : R.drawable.wholeheart_icon);
                        tv_not_unfreeze_or_finish.setText(String.format(getString(R.string.unfreeze_), WWViewUtil.numberFormatWithTwo(list.get(i).LeftIntegral)));
                    } else if (list.get(i).Status == 1) {//解冻完成
                        iv_unfreeze_star.setBackgroundResource(R.drawable.grayheart_icon);
                        tv_not_unfreeze_or_finish.setText(String.format(getString(R.string.finish_time_), TimeUtil.getOnlyDateToS(list.get(i).FinishTime * 1000)));
                    }
                    ll_container.addView(itemView);
                }
                int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                ll_view.measure(w, h);
                if (ll_view.getMeasuredHeight() > DensityUtil.getScreenHeight(MyWalletActivity.this)) {
                    fl_back_to_top.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    /**
     * 获取用户金额
     */
    private void getMoneyNum() {

        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.getAccountInfo());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        x.http().get(params, new WWXCallBack("AccountGet") {

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                accountInfo = JSON.parseObject(data.getString("Data"),
                        AccountInfo.class);
                // 设置数据
                tv_yue.setText(WWViewUtil.numberFormatPrice(accountInfo.Balance));
                tv_can_use_integral.setText(WWViewUtil.numberFormatWithTwo(accountInfo.VipAccount));
                tv_wait_unfreeze.setText(WWViewUtil.numberFormatWithTwo(accountInfo.DeblockingIntegral));
                tv_total_integral.setText(WWViewUtil.numberFormatWithTwo(accountInfo.TotalIntegral));
                tv_qibaobinum.setText(accountInfo.InternalBalance);
                getQiBaoBiPrice();
            }
        });
    }


    @Override
    protected void onResume() {
        realGet();
        getMoneyNum();
        super.onResume();
    }

    @Override
    protected void doOperate() {
        requestData(PullDown);
    }

    /**
     * 获取单日七宝币价格
     */
    private double qbbPrice=0;

    private void getQiBaoBiPrice() {

        RequestParams params = new RequestParams(ApiUser.QbbPrice());
        x.http().get(params, new WWXCallBack("QbbPrice") {

            @Override
            public void onAfterFinished() {

            }

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                qbbPrice = data.getDouble("Data");
                tv_price_today.setText("今日价值 ￥" + WWViewUtil.numberFormatWithTwo(qbbPrice) + "枚");
                double priceall=qbbPrice * Double.parseDouble(accountInfo.InternalBalance);
                tv_qbball.setText("总价值 ￥" + WWViewUtil.numberFormatWithTwo(priceall));

            }
        });

    }

    private UserReal userReal;

    private void realGet() {
        showWaitDialog();
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
                dismissWaitDialog();
            }
        });
    }
}
