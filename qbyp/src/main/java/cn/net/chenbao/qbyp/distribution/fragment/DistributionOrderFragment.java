package cn.net.chenbao.qbyp.distribution.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.PayOrdersActivity;
import cn.net.chenbao.qbyp.activity.SelfSupportFollowDeliverActivity;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.distribution.activity.DistributionOrderActivity;
import cn.net.chenbao.qbyp.distribution.adapter.DistributionOrderAdapter;
import cn.net.chenbao.qbyp.distribution.been.DistributionOrder;
import cn.net.chenbao.qbyp.distribution.been.DistributionPublicAccount;
import cn.net.chenbao.qbyp.fragment.FatherFragment;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/**
 * Created by zxj on 2017/4/16.
 *
 * @description 分销订单列表 订单状态：0-待付款，1-待发货，2-待收货，3-已完成，4-已取消  5 收款审核中
 */

public class DistributionOrderFragment extends FatherFragment {
    public static final int REQUEST_CODE = 666;
    public static final int REQUEST_CODE1 = 999;
    PullToRefreshListView listviewOrders;

    private DistributionOrderAdapter adapter;
    private int model;
    private int flag;

    public static DistributionOrderFragment newInstance(int model, int flag) {
        DistributionOrderFragment fragment = new DistributionOrderFragment();
        Bundle args = new Bundle();
        args.putInt(Consts.KEY_MODULE, model);
        args.putInt(Consts.KEY_FLAG, flag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.frag_distribution_order;
    }

    @Override
    protected void initView() {
        model = getArguments().getInt(Consts.KEY_MODULE);
        flag = getArguments().getInt(Consts.KEY_FLAG);
        listviewOrders = (PullToRefreshListView) mGroup.findViewById(R.id.listview_orders);
        WWViewUtil.setEmptyView(listviewOrders.getRefreshableView());
        listviewOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PublicWay.startDistributionOrderDetailActivity(getActivity(),adapter.getItem(position-1).OrderId);
            }
        });
        listviewOrders.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshData();

            }
        });
        listviewOrders.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {
            @Override
            public void onLastItemVisible() {
                if (HaveNextPage) {
                    requestData();
                } else {
                    WWToast.showShort(R.string.nomore_data);
                }
            }
        });
        adapter = new DistributionOrderAdapter(getActivity(), model);
        listviewOrders.setAdapter(adapter);
        adapter.setOrderOperateCallback(new DistributionOrderAdapter.OrderOperateCallback() {
            @Override
            public void cancelOrder(final DistributionOrder distributionOrder) {
                final CommonDialog dialog = DialogUtils.getCommonDialogTwiceConfirm(getActivity(), "您确定要取消该订单么？", true);
                dialog.getButtonRight().setTextColor(getResources().getColor(R.color.yellow_ww));
                dialog.setRightButtonCilck(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        orderOperate(distributionOrder, ApiDistribution.OrderCancel(), "OrderCancel");
                    }
                });
                dialog.show();
            }

            @Override
            public void payOrder(DistributionOrder distributionOrder) {
                //支付成功之后再做刷新
                PublicWay.startPayOrderActivity(DistributionOrderFragment.this, distributionOrder.TotalAmt, distributionOrder.OrderId, REQUEST_CODE, PayOrdersActivity.DISTRIBUTION);
            }

            @Override
            public void checkOrderLogs(DistributionOrder distributionOrder) {
                //无订单操作
                PublicWay.stratSelfSupportFollowDeliverActivity(getActivity(), distributionOrder.OrderId, SelfSupportFollowDeliverActivity.FENXIAO, 1);
            }

            @Override
            public void receivingOrder(final DistributionOrder distributionOrder) {
                final CommonDialog dialog = DialogUtils.getCommonDialogTwiceConfirm(getActivity(), "您确定已经收到该商品了么？", true);
                dialog.getButtonRight().setTextColor(getResources().getColor(R.color.yellow_ww));
                dialog.setRightButtonCilck(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        orderOperate(distributionOrder, ApiDistribution.OrderReceive(), "OrderReceive");

                    }
                });
                dialog.show();
            }

            @Override
            public void noGoodOrder(final DistributionOrder distributionOrder) {
                final CommonDialog dialog = DialogUtils.getCommonDialogTwiceConfirm(getActivity(), "释放该订单，您的上级代理将为该订单发货，确定放弃该订单么？", true);
                dialog.getButtonRight().setTextColor(getResources().getColor(R.color.yellow_ww));
                dialog.setRightButtonCilck(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        orderOperate(distributionOrder, ApiDistribution.OrderNoGoods(), "OrderNoGoods");
                    }
                });
                dialog.show();
            }

            @Override
            public void sendOrder(final DistributionOrder distributionOrder) {
                //发货成功了再刷新，先取用户积分
                showWaitDialog();
                x.http().get(ParamsUtils.getSessionParams(ApiDistribution.MyAccount()), new WWXCallBack("MyAccount") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        DistributionPublicAccount distributionPublicAccount = JSONObject.parseObject(data.getString("Data"), DistributionPublicAccount.class);
                        if (distributionPublicAccount.ConsumeUnfrozen < distributionOrder.TotalConsume ) {
                            final CommonDialog dialog = DialogUtils.getSingleBtnDialog(getActivity(), "操作发货，商品积分将一同赠出，目前您的未解冻积分已经不够赠送，请购买相应商品获得积分后再操作发货", "确定", true);
                            dialog.getButtonRight().setTextColor(getResources().getColor(R.color.yellow_ww));
                            dialog.setRightButtonCilck(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                        } else {
                            PublicWay.startDistributionSendPackageActivity(DistributionOrderFragment.this, distributionOrder.OrderId,distributionOrder.TotalConsume, REQUEST_CODE1);
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
            }
        });
        requestData();
    }

    public void refreshData() {
        mCurrentPage = 0;
        requestData();
    }

    /**
     * 订单操作
     *
     * @param distributionOrder
     * @param apiUrl
     * @param resultCode
     */
    private void orderOperate(final DistributionOrder distributionOrder, String apiUrl, String resultCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        jsonObject.put("orderId", distributionOrder.OrderId);
        showWaitDialog();
        x.http().post(ParamsUtils.getPostJsonParams(jsonObject, apiUrl), new WWXCallBack(resultCode) {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if(data.getBoolean("Data")){
                    getActivity().sendBroadcast(new Intent(DistributionOrderActivity.IS_REFRESH));
                    WWToast.showShort(R.string.set_succuss);
                }

            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE||requestCode==REQUEST_CODE1) {
            if (resultCode == getActivity().RESULT_OK) {
                //刷新所有订单
                getActivity().sendBroadcast(new Intent(DistributionOrderActivity.IS_REFRESH));
            }
        }
    }

    // 分页数据
    private int mCurrentPage = 0;
    private boolean HaveNextPage;

    private void requestData() {
        showWaitDialog();
        RequestParams params = new RequestParams(model == DistributionOrderActivity.IN ? ApiDistribution.MyOrdersBuy() : ApiDistribution.MyOrdersSeller());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        params.addBodyParameter("pageIndex", mCurrentPage + "");

        switch (flag) {
            case DistributionOrderActivity.ALL:
                break;
            case DistributionOrderActivity.DELLING:
                params.addBodyParameter("status", "0,1,5,2");
                break;
            case DistributionOrderActivity.FINISH:
                params.addBodyParameter("status", "3,4");
                break;
        }
        x.http().get(params, new WWXCallBack(model == DistributionOrderActivity.IN ? "MyOrdersBuy" : "MyOrdersSeller") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mCurrentPage++;
                HaveNextPage = data.getBoolean("HaveNextPage");
                ArrayList<DistributionOrder> list = (ArrayList<DistributionOrder>) JSONArray.parseArray(
                        data.getString("Data"), DistributionOrder.class);
                if (mCurrentPage > 1) {
                    adapter.addDatas(list);
                } else {
                    adapter.setDatas(list);
                }

            }

            @Override
            public void onAfterFinished() {
                listviewOrders.onRefreshComplete();
                dismissWaitDialog();
            }
        });

    }
}
