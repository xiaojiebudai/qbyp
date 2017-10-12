package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.ChoiceStateCategoryAdapter;
import cn.net.chenbao.qbyp.adapter.listview.ServiceProviderShopListAdapter;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.AgencyInfo;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CustomToast;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/14.
 * 服务商家列表
 */

public class ServiceProviderShopListActivity extends FatherActivity {
    @BindView(R.id.tv_industry)
    TextView tvIndustry;
    @BindView(R.id.iv_industry)
    ImageView ivIndustry;
    @BindView(R.id.ll_select)
    LinearLayout llSelect;
    @BindView(R.id.listview_datas)
    PullToRefreshListView listviewDatas;
    private ServiceProviderShopListAdapter mAdapter;
    private List<AgencyInfo> statusList = new ArrayList<AgencyInfo>();
    private final int SHOW_fIND_TOAST = 0;
    private long totalCount;
    private CustomToast customToast;
    private int status = -1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_fIND_TOAST:
                    if (customToast == null) {
                        int headAddChoiceItemH = DensityUtil.dip2px(ServiceProviderShopListActivity.this, 84);// 44+40
                        View view = View.inflate(ServiceProviderShopListActivity.this, R.layout.toast_layout,
                                null);
                        customToast = CustomToast.makeText(ServiceProviderShopListActivity.this, view,
                                headAddChoiceItemH, String.format(getString(R.string.query_seller_number_tips), totalCount), 3);
                    } else {
                        if (customToast.isShow()) {
                            CustomToast.removeWindow();
                        }
                        customToast.setText(String.format(getString(R.string.query_seller_number_tips), totalCount));
                    }
                    customToast.show();
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    protected int getLayoutId() {
        return R.layout.act_service_provider_shoplist;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.service_shop, true);
        statusList.add(new AgencyInfo("全部", -1));
        statusList.add(new AgencyInfo("已认证", 1));
        statusList.add(new AgencyInfo("未认证", 0));
        statusList.add(new AgencyInfo("试用", 4));
        statusList.add(new AgencyInfo("冻结", 3));
        statusList.add(new AgencyInfo("停业中", 2));
        statusList.add(new AgencyInfo("服务过期", 5));
    }

    @Override
    protected void initHeadBack() {
        View left = findViewById(R.id.rl_head_left);
        if (left != null) {
            left.findViewById(R.id.tv_head_left).setBackgroundResource(
                    R.drawable.arrow_back);
            left.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    clearToast();
                    finish();
                }
            });
        }
    }

    @Override
    protected void initView() {
        WWViewUtil.setEmptyView(listviewDatas.getRefreshableView());

        listviewDatas
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

                    @Override
                    public void onRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        mCurrentPage = 0;
                        getData();
                    }
                });
        listviewDatas
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (mCurrentPage >= pageCount) {
                            WWToast.showShort(R.string.nomore_data);
                        } else {
                            getData();
                        }
                    }
                });
        listviewDatas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clearToast();
                Intent intent = new Intent(ServiceProviderShopListActivity.this, ServiceProviderShopDetailActivity.class);
                intent.putExtra(Consts.KEY_DATA, JSONObject.toJSONString(mAdapter.getItem(position - 1)));
                startActivity(intent);
            }
        });
        mAdapter = new ServiceProviderShopListAdapter(this);
        listviewDatas.setAdapter(mAdapter);
    }

    @Override
    protected void doOperate() {
        getData();
    }

    // 分页数据
    private int mCurrentPage = 0;
    private int pageCount = 0;

    private void getData() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.MySellers());
        params.addBodyParameter("sessionId", WWApplication.getInstance()
                .getSessionId());
        params.addBodyParameter("pageSize", 10 + "");
        if (status != -1) {
            params.addBodyParameter("status", status + "");
        }
        params.addBodyParameter("pageIndex", mCurrentPage + "");

        x.http().get(params, new WWXCallBack("MySellers") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mCurrentPage++;
                ArrayList<AgencyInfo> list = (ArrayList<AgencyInfo>) JSONArray.parseArray(
                        data.getString("Data"), AgencyInfo.class);
                pageCount = data.getIntValue("PageCount");
                totalCount = data.getLongValue("TotalCount");
                if (mCurrentPage > 1) {
                    mAdapter.addDatas(list);
                } else {
                    mAdapter.setDatas(list);
                }
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                listviewDatas.onRefreshComplete();
                mHandler.sendEmptyMessage(SHOW_fIND_TOAST);
            }
        });
    }


    @OnClick(R.id.ll_select)
    public void onClick() {
        clearToast();
        showSelectPop(statusList);
        ivIndustry.setImageResource(R.drawable.fuwus_uparrow_icon);
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
     * 区域选择与状态选择的pop
     */
    private PopupWindow popupWindowR;

    private void showSelectPop(final List<AgencyInfo> list) {
        if (popupWindowR == null) {

            View inflate = LayoutInflater.from(this).inflate(R.layout.layout_pop_listview_alpha, null);
            popupWindowR = new PopupWindow(inflate, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            ListView lv_pop = (ListView) inflate
                    .findViewById(R.id.lv_category_pop_list);
            inflate.findViewById(R.id.iv_alpha_bg).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindowR.dismiss();
                }
            });
            final ChoiceStateCategoryAdapter choiceStateCategoryAdapter = new ChoiceStateCategoryAdapter(
                    this, ChoiceStateCategoryAdapter.RPOP);
            choiceStateCategoryAdapter.setDatas(list);
            lv_pop.setAdapter(choiceStateCategoryAdapter);
            popupWindowR.setBackgroundDrawable(new BitmapDrawable());
            popupWindowR.setOutsideTouchable(true);
            popupWindowR.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    ivIndustry.setImageResource(R.drawable.fuwus_downarrow_icon);
                }
            });
            //默认选中0
            choiceStateCategoryAdapter.setSelectPos(0);
            lv_pop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tvIndustry.setText(list.get(position).StateName);
                    status = list.get(position).Status;
                    choiceStateCategoryAdapter.setSelectPos(position);
                    popupWindowR.dismiss();
                    mCurrentPage = 0;
                    getData();
                }
            });
        }
        WWViewUtil.setPopInSDK7(popupWindowR, llSelect);
//        popupWindowR.showAsDropDown(llSelect);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearToast();// 销毁时防止窗体泄露
        if (popupWindowR != null) {
            popupWindowR.dismiss();
            popupWindowR = null;
        }
    }

    @Override
    public void onBackPressed() {
        clearToast();// 防止上个页面有显示
        super.onBackPressed();
    }
}
