package cn.net.chenbao.qbyp.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.SelfSupportMakesureOrderActivity;
import cn.net.chenbao.qbyp.adapter.listview.SelfSupportShopCartAdapter;
import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.bean.CartSum;
import cn.net.chenbao.qbyp.bean.ShopCarSelfSupport;
import cn.net.chenbao.qbyp.bean.ShopCarSelfSupportGroup;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wuri on 2016/11/22.
 */

public class ShopCartFragment extends FatherFragment implements SelfSupportShopCartAdapter.CheckInterface
        , SelfSupportShopCartAdapter.ModifyCountInterface
        , View.OnClickListener {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_head_center)
    TextView tvHeadCenter;
    @BindView(R.id.tv_head_right)
    TextView tvHeadRight;
    ExpandableListView evShopcart;
    @BindView(R.id.cb_all_select)
    CheckBox CbAllSelect;
    @BindView(R.id.ll_all_select)
    LinearLayout llAllSelect;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.ll_total_money)
    LinearLayout llTotalMoney;
    @BindView(R.id.btn_go_order_detail)
    TextView btnGoOrderDetail;
    private double totalPrice = 0.00;
    private int totalCount = 0;
    private int cartTotalNum;
    private Map<Integer, List<ShopCarSelfSupport>> children = new HashMap<Integer, List<ShopCarSelfSupport>>();
    private SelfSupportShopCartAdapter selfSupportShopCartAdapter;
    boolean isAddClicked = true;
    boolean isSubClicked = true;
    private View fake_status_bar, ll_head;

    @Override
    protected int getLayoutId() {
        return R.layout.act_self_support_shopcart;
    }

    @Override
    protected void initView() {
        evShopcart = (ExpandableListView) mGroup.findViewById(R.id.ev_shopcart);
        WWViewUtil.setEmptyViewNew(evShopcart);
        selfSupportShopCartAdapter = new SelfSupportShopCartAdapter(getActivity());
        initTitle();
        fake_status_bar = mGroup.findViewById(R.id.fake_status_bar);
        ll_head = mGroup.findViewById(R.id.ll_head);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            ViewGroup.LayoutParams params1 = (ViewGroup.LayoutParams) fake_status_bar.getLayoutParams();
            params1.height = WWViewUtil.getStatusBarHeight(getActivity());
            fake_status_bar.setLayoutParams(params1);
            fake_status_bar.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ll_head.getLayoutParams();
            params.topMargin = WWViewUtil.getStatusBarHeight(getActivity());
            ll_head.setLayoutParams(params);

        }
    }

    private void initTitle() {
        TextView center = (TextView) mGroup.findViewById(R.id.tv_head_center);
        center.setText(R.string.shop_car);
        center.setTextColor(getResources().getColor(R.color.white));
        center.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        TextView right = (TextView) mGroup.findViewById(R.id.tv_head_right);
        right.setTextColor(getResources().getColor(R.color.white));
        right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        right.setText(R.string.edit);
    }

    @Override
    public void onResume() {
        super.onResume();
        back.setVisibility(View.INVISIBLE);
        if (WWApplication.getInstance().isLogin()) {
            doRequest();
        }

    }

    public void doRequest() {
        requestData();
        requestCartSumGet();
    }

    private void requestData() {
//        showWaitDialog();   getActivity()为空,注释掉
        RequestParams params = ParamsUtils.getSessionParams(ApiShop.CartGet());
        x.http().get(params, new WWXCallBack("CartGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                List<ShopCarSelfSupportGroup> groups = JSONObject.parseArray(
                        data.getString("Data"), ShopCarSelfSupportGroup.class);
                for (int i = 0; i < groups.size(); i++) {
                    children.put(i, groups.get(i).Products);
                }
                selfSupportShopCartAdapter.setData(groups, children);
                selfSupportShopCartAdapter.setCheckInterface(ShopCartFragment.this);
                selfSupportShopCartAdapter.setModifyCountInterface(ShopCartFragment.this);
                evShopcart.setAdapter(selfSupportShopCartAdapter);
                for (int i = 0; i < selfSupportShopCartAdapter.getGroupCount(); i++) {
                    evShopcart.expandGroup(i);
                }

                if (selfSupportShopCartAdapter.getData().size() == 0) {
                    llTotalMoney.setVisibility(View.INVISIBLE);
                    CbAllSelect.setChecked(false);
                }
                defaultChecked(groups);
            }

            @Override
            public void onAfterFinished() {
//                dismissWaitDialog();
            }
        });
    }


    //默认4小时内选中
    private void defaultChecked(List<ShopCarSelfSupportGroup> groups) {
        long time = 4 * 60 * 60 * 1000;
        long currentTimeMillis = System.currentTimeMillis();
        int allCheckedNum = 0;
        for (int i = 0; i < groups.size(); i++) {
            List<ShopCarSelfSupport> products = groups.get(i).Products;
            int choosedNum = 0;
            for (int i1 = 0; i1 < products.size(); i1++) {
                long dValue = currentTimeMillis - products.get(i1).ModifiedTime * 1000;
                if (dValue < time) {
                    if (products.get(i1).Quantity <= products.get(i1).StockQty) {
                        products.get(i1).setChoosed(true);
                    } else {
                        products.get(i1).setChoosed(false);
                    }
                }
                if (products.get(i1).isChoosed()) {
                    choosedNum += 1;
                }
                if (i1 == products.size() - 1 && choosedNum == products.size()) {
                    groups.get(i).setChoosed(true);
                }
            }
            if (groups.get(i).isChoosed()) {
                allCheckedNum += 1;
            }
            if (allCheckedNum == groups.size()) {
                CbAllSelect.setChecked(true);
            } else {
                CbAllSelect.setChecked(false);
            }
        }
        selfSupportShopCartAdapter.notifyDataSetChanged();
        calculate();
    }


    private void excuteOrderPreview(final long[] selectGoodsFlowId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("flowIds", selectGoodsFlowId);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiShop.OrderPreview()),
                new WWXCallBack("OrderPreview") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        String string = data.getString("Data");
                        Intent intent = new Intent(getActivity(), SelfSupportMakesureOrderActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putLongArray("FlowsId", selectGoodsFlowId);
                        bundle.putString(Consts.KEY_DATA, string);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {

                    }

                    @Override
                    public void onAfterFinished() {
                        // TODO Auto-generated method stub
                    }
                });
    }

    @OnClick({R.id.back, R.id.tv_head_right, R.id.ll_all_select, R.id.btn_go_order_detail})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
//                getActivity().finish();
                break;
            case R.id.tv_head_right://编辑
                if (selfSupportShopCartAdapter.getData() == null) {
                    WWToast.showShort(R.string.date_no_get);
                    return;
                }
                changeTextAndDoOperate();
                break;
            case R.id.ll_all_select://全选
                if (selfSupportShopCartAdapter.getData() == null) {
                    WWToast.showShort(R.string.date_no_get);
                    return;
                }
                if (selfSupportShopCartAdapter.getData().size() > 0) {
                    CbAllSelect.setChecked(!CbAllSelect.isChecked());
                    doCheckAll();
                }
                break;
            case R.id.btn_go_order_detail:
                if (selfSupportShopCartAdapter.getData() == null) {
                    WWToast.showShort(R.string.date_no_get);
                    return;
                }
                if (btnGoOrderDetail.getText().toString().trim().equals(getString(R.string.go_settle))) {
                    long[] selectGoodsFlowId = getSelectGoodsFlowId();
                    if (selectGoodsFlowId.length != 0) {
                        excuteOrderPreview(selectGoodsFlowId);
                    } else {
                        if (selfSupportShopCartAdapter.getData().size() == 0) {
                            WWToast.show(getActivity(), R.string.please_add_goods_to_cart, Toast.LENGTH_SHORT);
                        } else {
                            WWToast.show(getActivity(), R.string.check_you_want_buy, Toast.LENGTH_SHORT);
                        }
                    }
                } else {
                    List<ShopCarSelfSupport> doDeleteFlowIds = recordDeleteData();// 获取要删除的数据
                    if (doDeleteFlowIds.size() > 0) {
                        showSureDeleteDialog(doDeleteFlowIds);
                    } else {
                        if (selfSupportShopCartAdapter.getData().size() == 0) {
                            WWToast.show(getActivity(), R.string.your_cart_none, Toast.LENGTH_SHORT);
                        } else {
                            WWToast.show(getActivity(), R.string.check_you_want_delete, Toast.LENGTH_SHORT);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private void showSureDeleteDialog(final List<ShopCarSelfSupport> doDeleteFlowIds) {
        final CommonDialog commonDialog = DialogUtils.getCommonDialog(getActivity(), String.format(getString(R.string.make_sure_delete_that_goods), doDeleteFlowIds.size()));
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
                excuteDeleteRequest(doDeleteFlowIds);//批量删除后台数据
                commonDialog.dismiss();
            }
        });
        commonDialog.show();
    }


    private void excuteDeleteRequest(List<ShopCarSelfSupport> toBeDeleteFlowIds) {
        long[] arr = new long[toBeDeleteFlowIds.size()];
        int toBeDeleteNum;
        for (int i = 0; i < toBeDeleteFlowIds.size(); i++) {
            arr[i] = toBeDeleteFlowIds.get(i).FlowId;
            toBeDeleteNum = +toBeDeleteFlowIds.get(i).Quantity;
            if (i == toBeDeleteFlowIds.size() - 1) {
                cartTotalNum -= toBeDeleteNum;
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("flowIds", arr);
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiShop.CartsDelete()),
                new WWXCallBack("CartsDelete") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        SharedPreferenceUtils.getInstance().saveCartNum(cartTotalNum);
                        requestData();
                        requestCartSumGet();
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {

                    }

                    @Override
                    public void onAfterFinished() {
                        // TODO Auto-generated method stub

                    }
                });

    }

    private void doCheckAll() {
        for (int i = 0; i < selfSupportShopCartAdapter.getData().size(); i++) {
            selfSupportShopCartAdapter.getData().get(i).setChoosed(CbAllSelect.isChecked());
            List<ShopCarSelfSupport> childs = children.get(i);
            for (int j = 0; j < childs.size(); j++) {
                childs.get(j).setChoosed(CbAllSelect.isChecked());
            }
        }
        selfSupportShopCartAdapter.notifyDataSetChanged();
        calculate();
    }

    /**
     * 记录删除操作数据
     */
    protected List<ShopCarSelfSupport> recordDeleteData() {
        List<ShopCarSelfSupport> recordToBeDeleteProduct = new ArrayList<ShopCarSelfSupport>();// 记录待删除的子元素列表

        for (int i = 0; i < selfSupportShopCartAdapter.getData().size(); i++) {
            List<ShopCarSelfSupport> childs = children.get(i);
            for (int j = 0; j < childs.size(); j++) {
                if (childs.get(j).isChoosed()) {
                    recordToBeDeleteProduct.add(childs.get(j));//记录返回值
                }
            }
        }

        calculate();
        return recordToBeDeleteProduct;
    }

    @Override
    public void doIncrease(int groupPosition, int childPosition, View showCountView, boolean isChecked) {
        ShopCarSelfSupport product = (ShopCarSelfSupport) selfSupportShopCartAdapter.getChild(groupPosition, childPosition);
        if (isAddClicked) {
            isAddClicked = false;
            excuteCartAdd(showCountView, product);
        } else {
            WWToast.showShort(R.string.click_too_fast);
        }
    }

    @Override
    public void doDecrease(int groupPosition, int childPosition, View showCountView, boolean isChecked, TextView tv_not_enough) {
        ShopCarSelfSupport product = (ShopCarSelfSupport) selfSupportShopCartAdapter.getChild(groupPosition, childPosition);
        if (product.Quantity == 1)
            return;
        if (isSubClicked) {
            isSubClicked = false;
            excuteCartSub(showCountView, product);
        } else {
            WWToast.showShort(R.string.click_too_fast);
        }
    }

    @Override
    public void checkGroup(int groupPosition, boolean isChecked) {
        List<ShopCarSelfSupport> childs = children.get(groupPosition);
        for (int i = 0; i < childs.size(); i++) {
            childs.get(i).setChoosed(isChecked);
        }
        if (isAllCheck())
            CbAllSelect.setChecked(true);
        else
            CbAllSelect.setChecked(false);
        selfSupportShopCartAdapter.notifyDataSetChanged();
        calculate();
    }

    @Override
    public void checkChild(int groupPosition, int childPosiTion, boolean isChecked) {
        boolean allChildSameState = true;// 判断改组下面的所有子元素是否是同一种状态
        ShopCarSelfSupportGroup group = selfSupportShopCartAdapter.getData().get(groupPosition);
        List<ShopCarSelfSupport> childs = children.get(groupPosition);
        for (int i = 0; i < childs.size(); i++) {
            if (childs.get(i).isChoosed() != isChecked) {
                allChildSameState = false;
                break;
            }
        }
        if (allChildSameState) {
            group.setChoosed(isChecked);// 如果所有子元素状态相同，那么对应的组元素被设为这种统一状态
        } else {
            group.setChoosed(false);// 否则，组元素一律设置为未选中状态
        }

        if (isAllCheck()) {
            CbAllSelect.setChecked(true);
        } else {
            CbAllSelect.setChecked(false);
        }
        selfSupportShopCartAdapter.notifyDataSetChanged();
        calculate();
    }

    private boolean isAllCheck() {
        for (ShopCarSelfSupportGroup group : selfSupportShopCartAdapter.getData()) {
            if (!group.isChoosed())
                return false;
        }
        return true;
    }

    private void changeTextAndDoOperate() {
        if (tvHeadRight.getText().toString().equals(getString(R.string.edit))) {
            tvHeadRight.setText(R.string.finishe);
            llTotalMoney.setVisibility(View.INVISIBLE);
            btnGoOrderDetail.setText(R.string.delete);
            btnGoOrderDetail.setBackgroundColor(getResources().getColor(R.color.red_b));
        } else if (tvHeadRight.getText().toString().equals(getString(R.string.finishe))) {
            tvHeadRight.setText(R.string.edit);
            if (selfSupportShopCartAdapter.getData().size() == 0) {
                llTotalMoney.setVisibility(View.INVISIBLE);
            } else {
                llTotalMoney.setVisibility(View.VISIBLE);//点击完成之后总价格需要重新计算再显示,条目刷新
            }
            btnGoOrderDetail.setText(R.string.go_settle);
            btnGoOrderDetail.setBackgroundColor(getResources().getColor(R.color.yellow_ww));
        }
    }

    /**
     * 统计操作<br>
     */
    private void calculate() {
        totalCount = 0;
        totalPrice = 0.00;
        for (int i = 0; i < selfSupportShopCartAdapter.getData().size(); i++) {
            List<ShopCarSelfSupport> childs = children.get(i);
            for (int j = 0; j < childs.size(); j++) {
                ShopCarSelfSupport product = childs.get(j);
                if (product.isChoosed()) {
                    totalCount++;
                    totalPrice += product.SalePrice * product.Quantity;
                }
            }
        }
        tvTotalPrice.setText(WWViewUtil.numberFormatPrice(totalPrice));
    }

    private void excuteCartAdd(final View showCountView, final ShopCarSelfSupport product) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("productId", product.ProductId + "");
        if (product.SkuId != 0) {
            jsonObject.put("skuId", product.SkuId);
        }
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiShop.CartAdd()),
                new WWXCallBack("CartAdd") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        product.Quantity = data.getIntValue("Data");
                        cartTotalNum += 1;
                        SharedPreferenceUtils.getInstance().saveCartNum(cartTotalNum);
                        ((TextView) showCountView).setText(product.Quantity + "");
                        excuteCartNumModify(showCountView, product);
                        selfSupportShopCartAdapter.notifyDataSetChanged();
                        calculate();
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
                    }
                });
    }

    private void excuteCartSub(final View showCountView, final ShopCarSelfSupport product) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("productId", product.ProductId + "");
        if (product.SkuId != 0) {
            jsonObject.put("skuId", product.SkuId);
        }
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiShop.CartSub()),
                new WWXCallBack("CartSub") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        product.Quantity = data.getIntValue("Data");
                        cartTotalNum -= 1;
                        SharedPreferenceUtils.getInstance().saveCartNum(cartTotalNum);
                        ((TextView) showCountView).setText(product.Quantity + "");
                        excuteCartNumModify(showCountView, product);
                        selfSupportShopCartAdapter.notifyDataSetChanged();
                        calculate();
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {
                        isSubClicked = true;
                    }

                    @Override
                    public void onAfterFinished() {
                        isSubClicked = true;
                    }
                });
    }

    private void excuteCartNumModify(final View showCountView, ShopCarSelfSupport product) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
        jsonObject.put("productId", product.ProductId + "");
        if (product.SkuId != 0) {
            jsonObject.put("skuId", "skuId");
        }
        jsonObject.put("quantity", product.Quantity + "");
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiShop.CartNum()),
                new WWXCallBack("CartNum") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        int count = data.getIntValue("Data");
                        ((TextView) showCountView).setText(count + "");
//                        selfSupportShopCartAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {

                    }

                    @Override
                    public void onAfterFinished() {
                        // TODO Auto-generated method stub

                    }
                });
    }

    private void requestCartSumGet() {
        RequestParams params = ParamsUtils.getSessionParams(ApiShop.CartSumGet());
        x.http().get(params, new WWXCallBack("CartSumGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                CartSum shopCartSum = JSONObject.parseObject(
                        data.getString("Data"), CartSum.class);
//                tvTotalPrice.setText(WWViewUtil.numberFormatPrice(shopCartSum.SumAmt));
                cartTotalNum = shopCartSum.Quantity;
                SharedPreferenceUtils.getInstance().saveCartNum(cartTotalNum);
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {

            }

            @Override
            public void onAfterFinished() {
                // TODO Auto-generated method stub

            }
        });
    }

    public long[] getSelectGoodsFlowId() {
        List<ShopCarSelfSupport> shopCarSelfSupports = new ArrayList<ShopCarSelfSupport>();
        for (int i = 0; i < selfSupportShopCartAdapter.getData().size(); i++) {
            List<ShopCarSelfSupport> childs = children.get(i);
            for (int j = 0; j < childs.size(); j++) {
                ShopCarSelfSupport product = childs.get(j);
                if (product.isChoosed()) {
                    shopCarSelfSupports.add(product);
                }
            }
        }
        int size = shopCarSelfSupports.size();
        long[] selectGoodsFlowId = new long[size];
        for (int i = 0; i < size; i++) {
            selectGoodsFlowId[i] = shopCarSelfSupports.get(i).FlowId;
        }
        return selectGoodsFlowId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
