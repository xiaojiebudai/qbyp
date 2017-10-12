package cn.net.chenbao.qbyp.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.AgencyShopDetailActivity;
import cn.net.chenbao.qbyp.adapter.listview.ChoiceStateCategoryAdapter;
import cn.net.chenbao.qbyp.adapter.listview.SellerManageAdapter;
import cn.net.chenbao.qbyp.api.ApiAgency;
import cn.net.chenbao.qbyp.bean.AgencyInfo;
import cn.net.chenbao.qbyp.bean.TradeExt;
import cn.net.chenbao.qbyp.myinterface.PopCallBackListener;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CustomToast;
import cn.net.chenbao.qbyp.view.ErjiCategoryPop;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/***
 * Description:区域代理—店铺 Company: jsh Version：1.0
 *
 * @author ZXJ
 * @date 2016-10-10
 ***/
public class AgencyShopFragment extends FatherFragment implements
        OnClickListener, OnItemClickListener {

    private PopupWindow popupWindowL;
    private PopupWindow popupWindowR;
    private ErjiCategoryPop erjiCategoryPop;
    private CustomToast customToast;
    private PullToRefreshListView mPullToRefreshListView;
    private SellerManageAdapter sellerManageAdapter;

    private TextView tv_area;
    private TextView tv_industry;
    private TextView tv_state;
    private List<TextView> viewList;

    private ArrayList<AgencyInfo> agencyInfos;
    private List<AgencyInfo> statusList = new ArrayList<AgencyInfo>();
    private ArrayList<TradeExt> tradeList;// 行业leftList
    private ArrayList<TradeExt> tradeList2;// 行业rightList

    private int areaByAgentId;// 当前选择的区域id
    private int lastAreaAgentId;
    private String currentAgentName;
    private boolean isFirstItemChecked;// 行业左边listview被点击
    private boolean isRetry;
    private String tradeId;
    private Integer status;
    private String parentId;
    private int currentPager;
    private long totalCount;
    private int pageCount;
    private final int SHOW_fIND_TOAST = 0;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_fIND_TOAST:
                    if (customToast == null) {
                        int headAddChoiceItemH = DensityUtil.dip2px(getActivity(), 84);// 44+40
                        View view = View.inflate(getActivity(), R.layout.toast_layout,
                                null);
                        customToast = CustomToast.makeText(getActivity(), view,
                                headAddChoiceItemH, String.format(getString(R.string.query_seller_number_tips),totalCount), 3);
                    } else {
                        if (customToast.isShow()) {
                            CustomToast.removeWindow();
                        }
                        customToast.setText(String.format(getString(R.string.query_seller_number_tips),totalCount));
                    }
                    customToast.show();
                    break;
                default:
                    break;
            }
        }

    };

    private void showIndustryPop() {
        if (erjiCategoryPop != null) {
            if (isFirstItemChecked && erjiCategoryPop.isShow()) {
                erjiCategoryPop.transfer2ChildList(tradeList2);
                isFirstItemChecked = false;
            } else if (!erjiCategoryPop.isShow()) {
                erjiCategoryPop.transfer2RootList(tradeList, null);
            }
            return;
        }
        showSelectErJiListPop();
        isFirstItemChecked = false;
    }

    private ChoiceStateCategoryAdapter choiceStateCategoryAdapterL;
    private ChoiceStateCategoryAdapter choiceStateCategoryAdapterR;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_agency_shop;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView() {
        initTitle();
        tv_area = (TextView) mGroup.findViewById(R.id.tv_area);
        tv_industry = (TextView) mGroup.findViewById(R.id.tv_industry);
        tv_state = (TextView) mGroup.findViewById(R.id.tv_state);

        mPullToRefreshListView = (PullToRefreshListView) mGroup
                .findViewById(R.id.listview_datas);
        WWViewUtil.setEmptyView(mPullToRefreshListView.getRefreshableView());
        mPullToRefreshListView
                .setOnRefreshListener(new OnRefreshListener<ListView>() {

                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        if (agencyInfos == null) {
                            mPullToRefreshListView.onRefreshComplete();
                            return;
                        }
                        if (!isRequestSeller) {
                            currentPager = 0;
                            showAllSellerDates();
                        }
                    }
                });
        mPullToRefreshListView
                .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (!isRequestSeller) {
                            showAllSellerDates();
                        }
                    }
                });
        mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clearToast();
                Intent intent = new Intent(getActivity(), AgencyShopDetailActivity.class);
                intent.putExtra(Consts.KEY_DATA, JSONObject.toJSONString(sellerManageAdapter.getItem(position - 1)));
                startActivity(intent);
            }
        });
        tv_area.setOnClickListener(this);
        tv_industry.setOnClickListener(this);
        tv_state.setOnClickListener(this);

        initValue();
    }

    /**
     * 设置筛选栏文字颜色
     */
    private void setFilterChoiceTextColor(int id, boolean isAllNoChecked) {
        if (isAllNoChecked) {
            for (int i = 0; i < viewList.size(); i++) {
                viewList.get(i).setTextColor(
                        getResources().getColor(R.color.text_f7));
            }
            return;
        }

        for (int i = 0; i < viewList.size(); i++) {
            if (viewList.get(i).getId() == id) {
                viewList.get(i).setTextColor(
                        getResources().getColor(R.color.yellow_ww));
            } else {
                viewList.get(i).setTextColor(
                        getResources().getColor(R.color.text_f7));
            }
        }
    }

    private void initValue() {
        viewList = new ArrayList<TextView>();
        viewList.add(tv_area);
        viewList.add(tv_industry);
        viewList.add(tv_state);

        statusList.add(new AgencyInfo("全部"));
        statusList.add(new AgencyInfo("已认证", 1));
        statusList.add(new AgencyInfo("未认证", 0));
        statusList.add(new AgencyInfo("试用", 4));
        statusList.add(new AgencyInfo("冻结", 3));
        statusList.add(new AgencyInfo("停业中", 2));
        statusList.add(new AgencyInfo("服务过期", 5));

        sellerManageAdapter = new SellerManageAdapter(getActivity());
        mPullToRefreshListView.setAdapter(sellerManageAdapter);
        getAgencyAreaList();

    }

    /**
     * 获取代理区域列表
     */
    private void getAgencyAreaList() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiAgency.getMyAgents());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params, new WWXCallBack("MyAgents") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                agencyInfos = (ArrayList<AgencyInfo>) JSON.parseArray(
                        data.getString("Data"), AgencyInfo.class);
                tv_area.setText(agencyInfos.get(0).AreaName);
                if (!isRequestSeller) {
                    showAllSellerDates();
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                if (isRetry) {
                    if (popupWindowL != null) {
                        choiceStateCategoryAdapterL.setDatas(agencyInfos);
                        WWViewUtil.setPopInSDK7(popupWindowL, tv_area);
//                        popupWindowL.showAsDropDown(tv_area);
                    } else {
                        showSelectPop(tv_area, agencyInfos);
                    }
                    isRetry = false;
                }
            }
        });
    }

    boolean isRequestSeller = false;

    /**
     * 进入商家管理展示所有的商家信息list数据
     */
    private void showAllSellerDates() {
        if (agencyInfos == null) {
            return;
        }
        isRequestSeller = true;
        showWaitDialog();
        RequestParams params = ParamsUtils
                .getSessionParams(ApiAgency.Sellers());
        if (areaByAgentId != 0) {
            params.addBodyParameter("agentId", areaByAgentId + "");
        } else {
            currentAgentName = agencyInfos.get(0).AreaName;
            areaByAgentId = agencyInfos.get(0).AgentId;
            lastAreaAgentId = areaByAgentId;
            params.addBodyParameter("agentId", areaByAgentId + "");
        }

        if (tradeId != null) {
            params.addBodyParameter("tradeId", tradeId);
        }

        if (status != null) {
            params.addBodyParameter("status", status + "");
        }

        params.addBodyParameter("pageIndex", currentPager + "");

        x.http().get(params, new WWXCallBack("Sellers") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                currentPager++;
                pageCount = data.getIntValue("PageCount");
                totalCount = data.getLongValue("TotalCount");
                ArrayList<AgencyInfo> sellerList = (ArrayList<AgencyInfo>) JSONArray
                        .parseArray(data.getString("Data"), AgencyInfo.class);
                if (currentPager == 1) {
                    sellerManageAdapter.setDatas(sellerList);
                } else {
                    if (currentPager > pageCount - 1) {
                        WWToast.showShort(R.string.nomore_data);
                    } else {
                        sellerManageAdapter.addDatas(sellerList);
                    }
                }
                if (sellerList != null) {
                    for (int i = 0; i < sellerList.size(); i++) {
                        sellerList.get(i).AreaName = currentAgentName;
                    }
                }
                sellerManageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                mPullToRefreshListView.onRefreshComplete();
                isRequestSeller = false;
                mHandler.sendEmptyMessage(SHOW_fIND_TOAST);
            }
        });

    }

    private void initTitle() {
        mGroup.findViewById(R.id.back).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                });
        TextView center = (TextView) mGroup.findViewById(R.id.tv_head_center);
        center.setText(R.string.agency_shop);
        center.setTextColor(getResources().getColor(R.color.white));
        center.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_area:
                clearToast();
                setFilterChoiceTextColor(v.getId(), false);
                if (agencyInfos == null) {
                    isRetry = true;
                    getAgencyAreaList();
                    return;
                }
                if (areaByAgentId != lastAreaAgentId) {
                    parentId = null;
                    tradeId = null;
                    status = null;
                }
                if (popupWindowL != null) {
                    choiceStateCategoryAdapterL.setDatas(agencyInfos);
                    WWViewUtil.setPopInSDK7(popupWindowL, tv_area);
//                    popupWindowL.showAsDropDown(tv_area);
                } else {
                    showSelectPop(tv_area, agencyInfos);
                }
                break;
            case R.id.tv_industry:
                clearToast();
                setFilterChoiceTextColor(v.getId(), false);
                if (agencyInfos == null) {
                    showSelectPop(tv_area, agencyInfos);
                    return;
                }
                if (!isRequestLoading) {
                    tradeList2 = null;
                    parentId = null;
                    showIndustryListData();
                }
                break;
            case R.id.tv_state:
                clearToast();
                setFilterChoiceTextColor(v.getId(), false);

                showSelectPop(v, statusList);
                break;
            case R.id.iv_alpha_bg:
                pop1Dismiss();
                setFilterChoiceTextColor(v.getId(), true);
                break;
            default:
                break;
        }
    }

    boolean isRequestLoading = false;

    /**
     * 获取行业列表
     */
    private void showIndustryListData() {
        showWaitDialog();
        isRequestLoading = true;
        RequestParams params = ParamsUtils.getSessionParams(ApiAgency.Trades());
        if (areaByAgentId != 0) {
            params.addBodyParameter("agentId", areaByAgentId + "");
        } else {
            params.addBodyParameter("agentId", agencyInfos.get(0).AgentId + "");
        }

        if (parentId != null && isFirstItemChecked) {
            params.addBodyParameter("parentId", parentId);
        }
        x.http().get(params, new WWXCallBack("Trades") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (isFirstItemChecked) {
                    tradeList2 = (ArrayList<TradeExt>) JSONArray.parseArray(
                            data.getString("Data"), TradeExt.class);
                    extartAddListHead(tradeList2, 2);
                } else {
                    tradeList = (ArrayList<TradeExt>) JSONArray.parseArray(
                            data.getString("Data"), TradeExt.class);
                    extartAddListHead(tradeList, 1);
                }
            }

            @Override
            public void onAfterFinished() {
                showIndustryPop();
                isRequestLoading = false;
                dismissWaitDialog();
            }
        });
    }

    /**
     * 区域选择与状态选择的pop
     */
    private void showSelectPop(View v, List<AgencyInfo> list) {
        View inflate = View.inflate(getActivity(),
                R.layout.layout_pop_listview_alpha, null);
        ListView lv_pop = (ListView) inflate
                .findViewById(R.id.lv_category_pop_list);
        inflate.findViewById(R.id.iv_alpha_bg).setOnClickListener(this);
        int tag = v.getId() == R.id.tv_area ? R.id.tv_area : R.id.tv_state;
        lv_pop.setTag(tag);

        createPop(tag, list, v, lv_pop, inflate);

        lv_pop.setOnItemClickListener(this);
    }

    private void createPop(int tag, List list, View v, ListView lv_pop, View inflate) {
        switch (tag) {
            case R.id.tv_area:
                if (popupWindowL == null || list == null) {
                    popupWindowL = new PopupWindow(inflate, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
                    popupWindowL.setBackgroundDrawable(new BitmapDrawable());
                    popupWindowL.setOutsideTouchable(true);
                    choiceStateCategoryAdapterL = new ChoiceStateCategoryAdapter(
                            getActivity(), ChoiceStateCategoryAdapter.LPOP);
                    lv_pop.setAdapter(choiceStateCategoryAdapterL);
                    choiceStateCategoryAdapterL.setDatas(list);

                    choiceStateCategoryAdapterL.setSelectPos(0);
                    popupWindowL.setOnDismissListener(new OnDismissListener() {

                        @Override
                        public void onDismiss() {
                            // TODO Auto-generated method stub
                            setFilterChoiceTextColor(0, true);
                        }
                    });
                }
                WWViewUtil.setPopInSDK7(popupWindowL, v);
//                popupWindowL.showAsDropDown(v);

                break;
            case R.id.tv_state:
                if (popupWindowR == null) {
                    popupWindowR = new PopupWindow(inflate, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
                    popupWindowR.setBackgroundDrawable(new BitmapDrawable());
                    popupWindowR.setOutsideTouchable(true);
                    choiceStateCategoryAdapterR = new ChoiceStateCategoryAdapter(
                            getActivity(), ChoiceStateCategoryAdapter.RPOP);
                    lv_pop.setAdapter(choiceStateCategoryAdapterR);
                    choiceStateCategoryAdapterR.setDatas(list);
                    //默认选中0
                    choiceStateCategoryAdapterR.setSelectPos(0);
                    popupWindowR.setOnDismissListener(new OnDismissListener() {

                        @Override
                        public void onDismiss() {
                            // TODO Auto-generated method stub
                            setFilterChoiceTextColor(0, true);
                        }
                    });
                }
                WWViewUtil.setPopInSDK7(popupWindowR, v);
//                popupWindowR.showAsDropDown(v);

                break;
        }
    }

    int lastPosition;

    /**
     * 行业二级筛选Pop
     */
    private void showSelectErJiListPop() {
        erjiCategoryPop = new ErjiCategoryPop(getActivity(),
                R.layout.popup_category, new PopCallBackListener() {

            @Override
            public void onBlankSpaceClicked() {
                lastPosition = -111;//重置
                setFilterChoiceTextColor(0, true);
            }

            @Override
            public void onClickFirstitem(int position) {
                if (lastPosition == position) {
                    return;
                }
                if (position == 0) {
                    tv_industry.setText(R.string.all_sort);
                    erjiCategoryPop.disMissPop();
                    parentId = null;
                    tradeId = null;
                    currentPager = 0;
                    if (!isRequestSeller) {
                        showAllSellerDates();
                    }
                    return;
                }
                if (!isRequestLoading) {
                    parentId = tradeList.get(position).TradeId;
                    isFirstItemChecked = true;
                    lastPosition = position;
                    showIndustryListData();
                }
            }

            @Override
            public void onClickSeconditem(int position) {
                currentPager = 0;
                if (position == 0) {
                    try {
                        tradeId = tradeList2.get(1).ParentId;
                        for (int i = 0; i < tradeList.size(); i++) {
                            if (tradeId.equals(tradeList.get(i).TradeId)) {
                                tv_industry
                                        .setText(tradeList.get(i).TradeName);
                            }
                        }
                        if (!isRequestSeller) {
                            showAllSellerDates();
                        }
                    } catch (Exception e) {

                    }
                    return;
                }
                if (position < tradeList2.size()) {
                    tradeId = tradeList2.get(position).TradeId;
                    tv_industry.setText(tradeList2.get(position).TradeName);
                }
                if (!isRequestSeller) {
                    showAllSellerDates();
                }
            }
        });

        erjiCategoryPop.setDatas(tradeList, tradeList2);
        View view = mGroup.findViewById(R.id.ll_seller_mana);
        erjiCategoryPop.showPopupWindow(view.getWidth(), view.getHeight(),
                tv_industry);
    }

    private void extartAddListHead(ArrayList<TradeExt> tradeList, int j) {
        int totalRecCount = 0;
        for (int i = 0; i < tradeList.size(); i++) {
            totalRecCount += tradeList.get(i).RecCount;
        }
        TradeExt tradeExt = new TradeExt();
        tradeExt.TradeName = j == 1 ? "全部分类" : "全部";
        tradeExt.RecCount = totalRecCount;
        tradeList.add(0, tradeExt);
    }

    /**
     * 查询吐司的删除
     */
    private void clearToast() {
        if (customToast != null) {
            CustomToast.removeWindow();
        }
    }

    /**
     * 地域状态popupwindow消失
     */
    private void pop1Dismiss() {
        if (popupWindowL != null) {
            popupWindowL.dismiss();
        }
        if (popupWindowR != null) {
            popupWindowR.dismiss();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
//                dismissWaitDialog();
                mDialog_wait = null;
                CustomToast.removeWindow();
            }
        });

        if (popupWindowL != null) {
            popupWindowL.dismiss();
            popupWindowL = null;
        }
        if (popupWindowR != null) {
            popupWindowR.dismiss();
            popupWindowR = null;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            clearToast();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentPager = 0;
        setFilterChoiceTextColor(0, true);
        switch ((Integer) parent.getTag()) {
            case R.id.tv_area:
                currentAgentName = agencyInfos.get(position).AreaName;
                tv_area.setText(currentAgentName);
                choiceStateCategoryAdapterL.setSelectPos(position);
                areaByAgentId = agencyInfos.get(position).AgentId;
                if (areaByAgentId != lastAreaAgentId) {
                    parentId = null;
                    tradeId = null;
                    status = null;
                    tv_industry.setText(R.string.industry);
                    tv_state.setText(R.string.state);

                    lastAreaAgentId = areaByAgentId;
                }
                break;
            case R.id.tv_state:
                tv_state.setText(statusList.get(position).StateName);
                choiceStateCategoryAdapterR.setSelectPos(position);
                status = statusList.get(position).Status;
                if (statusList.get(position).ChargeType == 1) {
                    status = 4; // ChargeType=1,==>status=4试用
                }
                break;
        }
        pop1Dismiss();
        parentId = null;
        if (!isRequestSeller) {
            showAllSellerDates();
        }
    }
}
