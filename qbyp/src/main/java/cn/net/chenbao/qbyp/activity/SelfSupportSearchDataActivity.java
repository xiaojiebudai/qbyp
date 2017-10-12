package cn.net.chenbao.qbyp.activity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.SelfSupportGoodsItemTwoAdapter;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.ShopBrand;
import cn.net.chenbao.qbyp.bean.ShopProduct;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.SelfBrandSelectPop;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 木头 on 2016/11/1.
 * 自营商城搜索结果
 */

public class SelfSupportSearchDataActivity extends FatherActivity implements View.OnClickListener {
    @BindView(R.id.head_iv_back)
    LinearLayout headIvBack;
    @BindView(R.id.tv_shop_cart_count)
    TextView tvShopCartCount;
    @BindView(R.id.rl_head_right)
    RelativeLayout rlHeadRight;
    @BindView(R.id.ll_synthesize)
    LinearLayout llSynthesize;
    @BindView(R.id.ll_new)
    LinearLayout llNew;
    @BindView(R.id.tv_price_img)
    TextView tvPriceImg;
    @BindView(R.id.ll_price)
    LinearLayout llPrice;
    @BindView(R.id.ll_sales)
    LinearLayout llSales;
    @BindView(R.id.ll_brand)
    LinearLayout llBrand;
    @BindView(R.id.listview)
    PullToRefreshListView listview;
    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.iv_search_clear)
    ImageView ivSearchClear;
    /**
     * 当前的筛选栏目
     */
    private int mCurrentFilterTab = 0;
    /**
     * 排序tab
     */
    private View mFilterTabs[];
    private boolean isPriceUp;

    /**
     * 类目
     */
    public static final String KEY_CLASS = "class";
    private String keyWord = "";
    private String classId;
    private String sortCol;
    private int brandId = -1;
    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;
    private SelfSupportGoodsItemTwoAdapter mAdapter;
    private ArrayList<ShopBrand> shopBrands;
    private SelfBrandSelectPop selfBrandSelectPop;

    @OnClick({R.id.head_iv_back, R.id.iv_search_clear, R.id.rl_head_right, R.id.ll_synthesize, R.id.ll_new, R.id.ll_price, R.id.ll_sales, R.id.ll_brand})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.head_iv_back:
                finish();
                break;
            case R.id.iv_search_clear:
                edtSearch.setText("");
                v.setVisibility(View.GONE);
                break;
            case R.id.rl_head_right:
                PublicWay.stratSelfSupportShopCartActivity(this);
                break;
            case R.id.ll_synthesize:
                changFilterTab(0);
                sortCol = "";
                brandId = -1;
                mCurrentPage = 0;
                getListGoods();
                break;
            case R.id.ll_new:
                changFilterTab(1);
                sortCol = "time,d";
                brandId = -1;
                mCurrentPage = 0;
                getListGoods();
                break;
            case R.id.ll_price:
                if (!v.isSelected()) {// 从没选中到选中
                    changFilterTab(2);
                    tvPriceImg.setBackgroundResource(R.drawable.mall_price_up_icon);// 设置选中图
                    isPriceUp = true;
                    sortCol = "price,a";
                } else {// 处于选中状态下
                    if (isPriceUp) {
                        tvPriceImg.setBackgroundResource(R.drawable.mall_price_down_icon);// 改变状态
                        sortCol = "price,d";
                    } else {
                        tvPriceImg.setBackgroundResource(R.drawable.mall_price_up_icon);
                        sortCol = "price,a";
                    }
                    isPriceUp = !isPriceUp;
                }
                brandId = -1;
                mCurrentPage = 0;
                getListGoods();
                break;
            case R.id.ll_sales:
                changFilterTab(3);
                sortCol = "sale";
                brandId = -1;
                mCurrentPage = 0;
                getListGoods();
                break;
            case R.id.ll_brand:
                changFilterTab(4);
                if (selfBrandSelectPop == null) {
                    selfBrandSelectPop = new SelfBrandSelectPop(this, R.layout.act_self_support_searchdata, shopBrands);
                    selfBrandSelectPop.setSelectListener(new SelfBrandSelectPop.SelectCallBack() {
                        @Override
                        public void selectListener(ShopBrand brand) {
                            sortCol = "";
                            brandId = brand.BrandId;
                            mCurrentPage = 0;
                            getListGoods();
                        }
                    });
                }
                selfBrandSelectPop.showWindow(v);
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.act_self_support_searchdata;
    }

    @Override
    protected void initValues() {
        keyWord = getIntent().getStringExtra(Consts.KEY_DATA);
        classId = getIntent().getStringExtra(KEY_CLASS);
    }

    @Override
    protected void initView() {

        mAdapter = new SelfSupportGoodsItemTwoAdapter(this);
        listview.setAdapter(mAdapter);
        llSynthesize.setSelected(true);
        mFilterTabs = new View[5];
        mFilterTabs[0] = llSynthesize;
        mFilterTabs[1] = llNew;
        mFilterTabs[2] = llPrice;
        mFilterTabs[3] = llSales;
        mFilterTabs[4] = llBrand;
        WWViewUtil.setEmptyView(listview.getRefreshableView());
        // 设置 end-of-list监听
        listview
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (mCurrentPage >= totalCount) {
                            WWToast.showShort(R.string.nomore_data);
                        } else {
                            getListGoods();
                        }

                    }
                });
        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                mCurrentPage = 0;
                getListGoods();
            }
        });
        // 输入框操作
        edtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return false;
            }
        });
        edtSearch
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId,
                                                  KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEND
                                || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                            // 搜索条件不能为空
                            if (edtSearch.getText().toString().trim()
                                    .equals("")) {
                            } else {
                                keyWord = edtSearch.getText().toString()
                                        .trim();
                                mCurrentPage = 0;
                                hideSoftKeyboard();
                                getListGoods();

                            }
                            return true;
                        }
                        return false;
                    }
                });
        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    ivSearchClear.setVisibility(View.VISIBLE);
                } else {
                    ivSearchClear.setVisibility(View.GONE);
                }
            }
        });
        edtSearch.setText(keyWord);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (WWApplication.getInstance().isLogin()) {
            int Quantity = SharedPreferenceUtils.getInstance().getCartNum();
            if (Quantity > 0) {
                tvShopCartCount.setVisibility(View.VISIBLE);
                if (Quantity > 99) {
                    tvShopCartCount.setText("99+");
                } else {
                    tvShopCartCount.setText(Quantity + "");
                }
            } else {
                tvShopCartCount.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 获取商品
     */
    public void getListGoods() {
        showWaitDialog();
        RequestParams requestParams = new RequestParams(ApiShop.Search());
        requestParams.addBodyParameter("pageIndex", mCurrentPage
                + "");
        if (!TextUtils.isEmpty(classId)) {
            requestParams.addBodyParameter("classId", classId);
        }
        if (!TextUtils.isEmpty(keyWord)) {
            requestParams.addBodyParameter("key", keyWord);
        }
        if (!TextUtils.isEmpty(sortCol)) {
            requestParams.addBodyParameter("sortCol", sortCol);
        }
        if (brandId != -1) {
            requestParams.addBodyParameter("brand", Integer.toString(brandId));
        }

        x.http().get(requestParams, new WWXCallBack("Search") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mCurrentPage++;
                ArrayList<ShopProduct> list = (ArrayList<ShopProduct>) JSONArray.parseArray(
                        data.getString("Data"), ShopProduct.class);
                totalCount = data.getIntValue("PageCount");
                if (mCurrentPage > 1) {
                    mAdapter.addDatas(list);
                } else {
                    mAdapter.setDatas(list);
                    shopBrands = (ArrayList<ShopBrand>) JSONArray.parseArray(
                            data.getString("Brands"), ShopBrand.class);
                    if (selfBrandSelectPop != null && shopBrands != null && shopBrands.size() > 0) {
                        selfBrandSelectPop.setAv(shopBrands);
                    }
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                listview.onRefreshComplete();
            }
        });
    }

    private void changFilterTab(int tab) {
        if (mCurrentFilterTab == tab) {
            return;
        } else {
            if (mCurrentFilterTab == 2) {// 处于选中价格的栏目
                tvPriceImg.setBackgroundResource(R.drawable.mall_price_icon);
                isPriceUp = false;
            }
            mFilterTabs[mCurrentFilterTab].setSelected(false);
            mFilterTabs[tab].setSelected(true);
            mCurrentFilterTab = tab;
        }
    }

    @Override
    protected void doOperate() {
        getListGoods();
    }

}
