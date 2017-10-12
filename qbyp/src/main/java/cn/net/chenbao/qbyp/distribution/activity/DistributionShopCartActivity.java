package cn.net.chenbao.qbyp.distribution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.FatherActivity;
import cn.net.chenbao.qbyp.api.ApiDistribution;
import cn.net.chenbao.qbyp.distribution.adapter.DistributionShopCartAdapter;
import cn.net.chenbao.qbyp.distribution.been.DistributionProduct;
import cn.net.chenbao.qbyp.distribution.been.DistributionPublicAccount;
import cn.net.chenbao.qbyp.distribution.been.FenXiaoCart;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.view.DistributionInputNumDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 木头 on 2017-6-16 19:19:29.
 * 优分销购物车
 */

public class DistributionShopCartActivity extends FatherActivity
        implements DistributionShopCartAdapter.ModifyCountInterface,
        DistributionShopCartAdapter.CheckedInterface,
        View.OnClickListener {

    @BindView(R.id.lv_shopcart)
    PullToRefreshListView lvShopcart;
    TextView tvHeadRight;
    @BindView(R.id.cb_all_select)
    CheckBox CbAllSelect;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.btn_go_order_detail)
    TextView btnGoOrderDetail;
    @BindView(R.id.ll_all_select)
    LinearLayout llAllSelect;
    @BindView(R.id.ll_total_money)
    LinearLayout llTotalMoney;
    @BindView(R.id.tv_sub_money)
    TextView tv_sub_money;//优惠金额
    @BindView(R.id.tv_tips)
    TextView tv_tips;

    private double totalPrice = 0.00;// 购买的商品总价
    private int totalCount = 0;// 购买的商品总数量
    private DistributionShopCartAdapter mAdapter;
    boolean isAddClicked = true;
    boolean isSubClicked = true;
    boolean isFirstLoad = true;
    private FenXiaoCart fenXiaoCart;
    private DistributionPublicAccount levelInfo;
    private DistributionInputNumDialog inputNumDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.act_distribution_shopcart;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.shop_car, true);
        tvHeadRight = ((TextView) findViewById(R.id.tv_head_right));
        tvHeadRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        initTextHeadRigth(R.string.edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTextAndDoOperate();
            }
        });
    }

    private void changeTextAndDoOperate() {
        if (tvHeadRight.getText().toString().equals("编辑")) {
            tvHeadRight.setText("完成");
            llTotalMoney.setVisibility(View.INVISIBLE);
            btnGoOrderDetail.setText("删除");
            btnGoOrderDetail.setBackgroundColor(getResources().getColor(R.color.red_b));
            mAdapter.setIsdelete(true);
        } else if (tvHeadRight.getText().toString().equals("完成")) {
            tvHeadRight.setText("编辑");
            mAdapter.setIsdelete(false);
            if (mAdapter.getDatas().size() == 0) {
                llTotalMoney.setVisibility(View.INVISIBLE);
            } else {
                llTotalMoney.setVisibility(View.VISIBLE);//点击完成之后总价格需要重新计算再显示,条目刷新
            }
            btnGoOrderDetail.setBackgroundColor(getResources().getColor(R.color.yellow_ww));
            calculate();
        }
    }

    @Override
    protected void initView() {
        WWViewUtil.setEmptyViewNew(lvShopcart.getRefreshableView());
    }

    @Override
    protected void doOperate() {
        mAdapter = new DistributionShopCartAdapter(DistributionShopCartActivity.this);
        mAdapter.setModifyCountInterface(this);
        mAdapter.setCheckInterface(this);
        lvShopcart.setAdapter(mAdapter);

        lvShopcart.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (levelInfo != null) {
                    requestData();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

    }

    private void getFenXiaoLevel() {
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
                levelInfo = JSON.parseObject(data.getString("Data"),
                        DistributionPublicAccount.class);
                requestData();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFenXiaoLevel();
    }

    @OnClick({R.id.ll_all_select, R.id.btn_go_order_detail})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_all_select://全选
                if (mAdapter.getDatas().size() > 0) {
                    CbAllSelect.setChecked(!CbAllSelect.isChecked());
                    doCheckAll();
                }
                break;
            case R.id.btn_go_order_detail:
                if (tvHeadRight.getText().toString().trim().equals("编辑")) {
                    long[] selectGoodsFlowId = getSelectGoodsFlowId();
                    if (selectGoodsFlowId.length != 0) {
                        performOrderPreview(selectGoodsFlowId);
                    } else {
                        if (mAdapter.getDatas().size() == 0) {
                            WWToast.show(DistributionShopCartActivity.this, R.string.please_add_goods_to_cart, Toast.LENGTH_SHORT);
                        } else {
                            WWToast.show(DistributionShopCartActivity.this, R.string.check_you_want_buy, Toast.LENGTH_SHORT);
                        }
                    }
                } else {
                    List<DistributionProduct> doDeleteFlowIds = recordDeleteData();// 获取要删除的数据
                    if (doDeleteFlowIds.size() > 0) {
                        showSureDeleteDialog(doDeleteFlowIds);
                    } else {
                        if (mAdapter.getDatas().size() == 0) {
                            WWToast.show(DistributionShopCartActivity.this, R.string.your_cart_none, Toast.LENGTH_SHORT);
                        } else {
                            WWToast.show(DistributionShopCartActivity.this, R.string.check_you_want_delete, Toast.LENGTH_SHORT);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showSureDeleteDialog(final List<DistributionProduct> doDeleteFlowIds) {
        final CommonDialog commonDialog = DialogUtils.getCommonDialog(this, "确定要删除这" + doDeleteFlowIds.size() + "种商品?");
        commonDialog.getButtonLeft(R.string.cancel);
        commonDialog.getButtonRight(R.string.ok);
        commonDialog.setLeftButtonOnClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonDialog.dismiss();
            }
        });

        commonDialog.setRightButtonCilck(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (levelInfo != null) {
                    performDeleteRequest(doDeleteFlowIds);//批量删除后台数据
                }
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }

    private void performDeleteRequest(List<DistributionProduct> toBeDeleteFlowIds) {
        long[] arr = new long[toBeDeleteFlowIds.size()];
        for (int i = 0; i < toBeDeleteFlowIds.size(); i++) {
            arr[i] = toBeDeleteFlowIds.get(i).FlowId;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("flowIds", arr);
        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiDistribution.CartsDelete()),
                new WWXCallBack("CartsDelete") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        requestData();
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {

                    }

                    @Override
                    public void onAfterFinished() {
                        // TODO Auto-generated method stub
                        dismissWaitDialog();
                    }
                });
    }

    private List<DistributionProduct> recordDeleteData() {
        List<DistributionProduct> recordToBeDeleteProduct = new ArrayList<DistributionProduct>();// 记录待删除的子元素列表

        for (int i = 0; i < mAdapter.getDatas().size(); i++) {
            DistributionProduct product = mAdapter.getItem(i);
            if (product.isChecked()) {
                recordToBeDeleteProduct.add(product);//记录返回值
            }
        }

        calculate();
        return recordToBeDeleteProduct;
    }

    private void performOrderPreview(final long[] selectGoodsFlowId) {
        showWaitDialog();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("flowIds", selectGoodsFlowId);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiDistribution.OrderPreviewByCart()),
                new WWXCallBack("OrderPreviewByCart") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        String string = data.getString("Data");
                        Intent intent = new Intent(DistributionShopCartActivity.this, DistributionMakeSureOrderActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putLongArray("FlowsId", selectGoodsFlowId);
                        bundle.putString(Consts.KEY_DATA, string);
                        bundle.putInt(Consts.KEY_MODULE, DistributionMakeSureOrderActivity.FROM_DISTRIBUTION_SHOP_CART);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {
                        int errCode = data.getIntValue("ErrCode");
                        if (errCode == 501 || errCode == 502) {//501 不存在  502 下架
                            requestData();
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        // TODO Auto-generated method stub
                        dismissWaitDialog();
                    }
                });
    }

    private long[] getSelectGoodsFlowId() {
        List<DistributionProduct> shopCarDistribution = new ArrayList<DistributionProduct>();
        for (int i = 0; i < mAdapter.getDatas().size(); i++) {
            DistributionProduct product = mAdapter.getItem(i);
            if (product.isChecked() && product.Status == 1) {
                shopCarDistribution.add(product);
            }
        }

        int size = shopCarDistribution.size();
        long[] selectGoodsFlowId = new long[size];
        for (int i = 0; i < size; i++) {
            selectGoodsFlowId[i] = shopCarDistribution.get(i).FlowId;
        }
        return selectGoodsFlowId;
    }

    private void doCheckAll() {
        ArrayList<DistributionProduct> products = mAdapter.getDatas();
        for (int i = 0; i < products.size(); i++) {
            products.get(i).setChecked(CbAllSelect.isChecked());
        }
        mAdapter.notifyDataSetChanged();
        calculate();
    }

    private double agentCalcuMoney1;//总代单价计算和
    private double agentCalcuMoney2;//小代单价计算和
    private double agentCalcuMoney3;//专卖店单价计算和

    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        agentCalcuMoney1 = 0;
        agentCalcuMoney2 = 0;
        agentCalcuMoney3 = 0;
        for (int i = 0; i < mAdapter.getDatas().size(); i++) {
            DistributionProduct product = mAdapter.getItem(i);
            if (product.isChecked() && product.Status == 1) {
                updateAddComparePrice(product, product.Quantity);
            }
        }
        basicIdToShowTotal();
    }

    /**
     * 根据levelId以及特定条件确定本次享受的价格
     */
    private void basicIdToShowTotal() {
        if (levelInfo == null) {
            return;
        }

        setBtnText();
        switch (levelInfo.LevelId) {
            case 0://消费者
                if (agentCalcuMoney3 >= fenXiaoCart.Agent4UpSaleAmt) {
                    tvTotalPrice.setText(WWViewUtil.numberFormatPrice(agentCalcuMoney3));
                    tv_sub_money.setText("已优惠 : - " + WWViewUtil.numberFormatPrice(totalPrice - agentCalcuMoney3));
                } else if (agentCalcuMoney1 >= fenXiaoCart.Agent3UpSaleAmt) {
                    tvTotalPrice.setText(WWViewUtil.numberFormatPrice(agentCalcuMoney1));
                    tv_sub_money.setText("已优惠 : - " + WWViewUtil.numberFormatPrice(totalPrice - agentCalcuMoney1));
                } else {
                    if (agentCalcuMoney2 >= fenXiaoCart.Agent2UpSaleAmt) {
                        tvTotalPrice.setText(WWViewUtil.numberFormatPrice(agentCalcuMoney2));
                        tv_sub_money.setText("已优惠 : - " + WWViewUtil.numberFormatPrice(totalPrice - agentCalcuMoney2));
                    } else {
                        tvTotalPrice.setText(WWViewUtil.numberFormatPrice(totalPrice));
                        tv_sub_money.setText("已优惠 : - ¥0.00");
                    }
                }
                break;
            case 2://小代
                if (agentCalcuMoney3 >= fenXiaoCart.Agent4UpSaleAmt) {
                    tvTotalPrice.setText(WWViewUtil.numberFormatPrice(agentCalcuMoney3));
                    tv_sub_money.setText("已优惠 : - " + WWViewUtil.numberFormatPrice(totalPrice - agentCalcuMoney3));
                } else if (agentCalcuMoney1 >= fenXiaoCart.Agent3UpSaleAmt) {
                    tvTotalPrice.setText(WWViewUtil.numberFormatPrice(agentCalcuMoney1));
                    tv_sub_money.setText("已优惠 : - " + WWViewUtil.numberFormatPrice(totalPrice - agentCalcuMoney1));
                } else {
                    tvTotalPrice.setText(WWViewUtil.numberFormatPrice(agentCalcuMoney2));
                    tv_sub_money.setText("已优惠 : - " + WWViewUtil.numberFormatPrice(totalPrice - agentCalcuMoney2));
                }
                break;
            case 3://总代
                if (agentCalcuMoney3 >= fenXiaoCart.Agent4UpSaleAmt) {
                    tvTotalPrice.setText(WWViewUtil.numberFormatPrice(agentCalcuMoney3));
                    tv_sub_money.setText("已优惠 : - " + WWViewUtil.numberFormatPrice(totalPrice - agentCalcuMoney3));
                } else {
                    tvTotalPrice.setText(WWViewUtil.numberFormatPrice(agentCalcuMoney1));
                    tv_sub_money.setText("已优惠 : - " + WWViewUtil.numberFormatPrice(totalPrice - agentCalcuMoney1));
                }
                break;
            case 4://专卖店
                tvTotalPrice.setText(WWViewUtil.numberFormatPrice(agentCalcuMoney3));
                tv_sub_money.setText("已优惠 : - " + WWViewUtil.numberFormatPrice(totalPrice - agentCalcuMoney3));
                break;
        }
    }

    private void setBtnText() {
        String string = tvHeadRight.getText().toString();
        if (string.equals("编辑")) {
            btnGoOrderDetail.setText("去结算(" + (totalCount == 0 ? "0" : totalCount) + ")");
        } else if (string.equals("完成")) {
            btnGoOrderDetail.setText("删除");
        }
    }

    @Override
    public void doIncrease(int position) {
        DistributionProduct product = mAdapter.getItem(position);
        if (isAddClicked) {
            isAddClicked = false;
            performCartAdd(product);
        } else {
            WWToast.showShort(R.string.click_too_fast);
        }
    }

    private void performCartAdd(final DistributionProduct product) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("productId", product.ProductId + "");

        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiDistribution.CartAdd()),
                new WWXCallBack("CartAdd") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        product.Quantity++;
                        mAdapter.notifyDataSetChanged();
                        if (product.isChecked) {
                            updateAddComparePrice(product, 1);
                            basicIdToShowTotal();
                        }
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {
                        if (data.getIntValue("ErrCode") == -1) {
                        }//超过库存
                        isAddClicked = true;
                    }

                    @Override
                    public void onAfterFinished() {
                        isAddClicked = true;
                        dismissWaitDialog();
                    }
                });
    }

    @Override
    public void doDecrease(int position) {
        DistributionProduct product = mAdapter.getItem(position);
        if (product.Quantity == 1)
            return;

        if (isSubClicked) {
            isSubClicked = false;
            performCartSub(product);
        } else {
            WWToast.showShort(R.string.click_too_fast);
        }
    }

    private int selectPos = 0;

    @Override
    public void doMultiModify(int position) {
        selectPos = position;
        DistributionProduct product = mAdapter.getItem(selectPos);
        if (inputNumDialog == null) {
            inputNumDialog = new DistributionInputNumDialog(this, null);
            inputNumDialog.setMakesureListener(new DistributionInputNumDialog.MakesureListener() {
                @Override
                public void onClickOk(int quantity) {
                    //这里判断数量是否改变执行接口,偶现(极少次数)数量更改时return掉了,没有调接口,难以复现,所以用initvalue值在dialog中判断
                    performCartNumModify(quantity, selectPos);
                }
            });
        }
        inputNumDialog.setInitValue(product.Quantity);
        inputNumDialog.show();
    }

    private void performCartSub(final DistributionProduct product) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("productId", product.ProductId + "");

        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiDistribution.CartSub()),
                new WWXCallBack("CartSub") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        product.Quantity--;
                        mAdapter.notifyDataSetChanged();
                        if (product.isChecked) {
                            updateSubComparePrice(product, 1);
                            basicIdToShowTotal();
                        }
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {
                        isSubClicked = true;
                    }

                    @Override
                    public void onAfterFinished() {
                        isSubClicked = true;
                        dismissWaitDialog();
                    }
                });
    }

    private void performCartNumModify(int quantity, final int position) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("productId", mAdapter.getItem(position).ProductId + "");
        jsonObject.put("quantity", quantity + "");

        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiDistribution.CartNum()),
                new WWXCallBack("CartNum") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        mAdapter.getItem(position).Quantity = data.getIntValue("Data");
                        mAdapter.notifyDataSetChanged();
                        calculate();
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {

                    }

                    @Override
                    public void onAfterFinished() {
                        // TODO Auto-generated method stub
                        dismissWaitDialog();
                    }
                });
    }

    private void requestData() {
        showWaitDialog();
        RequestParams params = ParamsUtils.getSessionParams(ApiDistribution.CartGet());
        x.http().get(params, new WWXCallBack("CartGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                fenXiaoCart = JSONObject.parseObject(
                        data.getString("Data"), FenXiaoCart.class);

                if (mAdapter.getDatas() != null) {//对刷新前的数据勾选状态进行保存赋值
                    for (int i = 0; i < fenXiaoCart.Goods.size(); i++) {
                        for (int j = 0; j < mAdapter.getDatas().size(); j++) {
                            if (mAdapter.getDatas().get(j).ProductId == fenXiaoCart.Goods.get(i).ProductId) {
                                fenXiaoCart.Goods.get(i).setChecked(mAdapter.getDatas().get(j).isChecked());
                            }
                        }
                    }
                }
                mAdapter.setDatas(fenXiaoCart.Goods);

                if (!TextUtils.isEmpty(fenXiaoCart.PriceExplain)) {
                    tv_tips.setVisibility(View.VISIBLE);
                    tv_tips.setText(fenXiaoCart.PriceExplain);
                }
                if (mAdapter.getDatas().size() == 0) {//删除时到最后可能没有数据
                    llTotalMoney.setVisibility(View.INVISIBLE);
                    CbAllSelect.setChecked(false);
                }

                if (isFirstLoad) {
                    int onlineNum = 0;
                    for (int i = 0; i < fenXiaoCart.Goods.size(); i++) {
                        if (fenXiaoCart.Goods.get(i).Status == 1) {
                            onlineNum++;
                            fenXiaoCart.Goods.get(i).setChecked(true);
                        } else
                            fenXiaoCart.Goods.get(i).setChecked(false);
                    }
                    if (mAdapter.getDatas().size() > 0) {
                        if (onlineNum == fenXiaoCart.Goods.size()) {
                            CbAllSelect.setChecked(true);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
                isFirstLoad = false;

                calculate();
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                lvShopcart.onRefreshComplete();
            }
        });
    }

    @Override
    public void onChecked(boolean delete, int position) {
        boolean isAllCheck = true;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            if (!mAdapter.getItem(i).isChecked) {
                isAllCheck = false;
                break;
            }
        }
        CbAllSelect.setChecked(isAllCheck);

        if (!delete) {//删除点击不计算
            DistributionProduct product = mAdapter.getItem(position);
            if (product.isChecked) {
                updateAddComparePrice(product, product.Quantity);
            } else {
                updateSubComparePrice(product, product.Quantity);
            }
            basicIdToShowTotal();
        }
    }

    /**
     * 根据指定产品 算出对应的各个总价 改变本地变量
     * 加
     *
     * @param product
     * @param quantity
     */
    private void updateAddComparePrice(DistributionProduct product, int quantity) {
        totalCount += quantity;
        agentCalcuMoney1 += product.AgentPrice1 * quantity;
        agentCalcuMoney2 += product.AgentPrice2 * quantity;
        agentCalcuMoney3 += product.AgentPrice3 * quantity;
        totalPrice += product.SourcePrice * quantity;
    }

    //减
    private void updateSubComparePrice(DistributionProduct product, int quantity) {
        totalCount -= quantity;
        agentCalcuMoney1 -= product.AgentPrice1 * quantity;
        agentCalcuMoney2 -= product.AgentPrice2 * quantity;
        agentCalcuMoney3 -= product.AgentPrice3 * quantity;
        totalPrice -= product.SourcePrice * quantity;
    }
}
