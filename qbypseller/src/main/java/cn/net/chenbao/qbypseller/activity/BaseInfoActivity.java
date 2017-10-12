package cn.net.chenbao.qbypseller.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.adapter.listview.TradesCategoryAdapter;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiBaseData;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.Location;
import cn.net.chenbao.qbypseller.bean.TradesCategory;
import cn.net.chenbao.qbypseller.dialog.ChoosePhotoDialog;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.DensityUtil;
import cn.net.chenbao.qbypseller.utils.FileUtils;
import cn.net.chenbao.qbypseller.utils.ImageUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PermissionUtil;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.RegexUtil;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;
import cn.net.chenbao.qbypseller.view.RegisterTimeLine;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;

/**
 * 基本资料
 *
 * @author licheng
 */
public class BaseInfoActivity extends FatherActivity implements OnClickListener, PermissionUtil.PermissionCallbacks {

    public static final int REQUEST_CODE1 = 888;
    private ImageView mShopHead;

    public static final int REQUEST_CODE = 10086;
    public static final int REQUEST_LOCTION = 10087;

    /**
     * 会话id
     */
    private String sessionId;
    private PopupWindow popupWindow, popupWindowSub;
    private TextView mTrades, tv_trade_sub;
    private EditText mPhone;
    private EditText mShopName;
    private Location location;
    private TextView mAddress;
    private EditText mMenpaihao;
//    private EditText ed_agency_phone;

    private TextView tv_agency;

    private String path;

    public static final int REQUEST_CODE_ASK_LOCATION = 10010;

    private ChoosePhotoDialog mChoosePhotoDialog;
    private String province_id = "";
    private String city_id = "";
    private String county_id = "";

    @Override
    protected int getLayoutId() {

        return R.layout.act_baseinfo;
    }

    @Override
    protected void initValues() {
        sessionId = getIntent().getStringExtra(Consts.KEY_DATA);
        initDefautHead(R.string.business_register, false);
        WWApplication.getInstance().setisHome(false);
    }

    @Override
    protected void initView() {
        RegisterTimeLine timeLine = (RegisterTimeLine) findViewById(R.id.rt);
        timeLine.setStep(1);
        TextView baseInfo = (TextView) findViewById(R.id.tv_base_info);
        baseInfo.setTextColor(getResources().getColor(R.color.yellow_ww));
        findViewById(R.id.tv_next).setOnClickListener(this);
        mShopHead = (ImageView) findViewById(R.id.iv_shop_head);
        mTrades = (TextView) findViewById(R.id.tv_trade);
        tv_trade_sub = (TextView) findViewById(R.id.tv_trade_sub);
        mPhone = (EditText) findViewById(R.id.ed_shop_phone);
//        ed_agency_phone = (EditText) findViewById(R.id.ed_agency_phone);
        mShopName = (EditText) findViewById(R.id.ed_shop_name);
        mAddress = (TextView) findViewById(R.id.tv_address);
        tv_agency = (TextView) findViewById(R.id.tv_agency);
        mMenpaihao = (EditText) findViewById(R.id.ed_men_pai);


        findViewById(R.id.ll_address).setOnClickListener(this);

        findViewById(R.id.tv_updata_pic).setOnClickListener(this);
        findViewById(R.id.iv_shop_head).setOnClickListener(this);
        findViewById(R.id.ll_trades_select).setOnClickListener(this);
        findViewById(R.id.ll_trades_select_sub).setOnClickListener(this);

        findViewById(R.id.ll_agency).setOnClickListener(this);
    }

    // 回显子类行业时需要
    private JSONObject object;
    // 是否是回显
    boolean isReShow;

    /*
     * 当回显时初始化数据(1.审核失败;2.第二步提交资料未提)
     */
    private void mayInitData() {
        // TODO Auto-generated method stub
        showWaitDialog();
        RequestParams params = new RequestParams(ApiSeller.infoGet());
        params.addQueryStringParameter(Api.KEY_SESSIONID, sessionId);
        x.http().get(
                params,
                new WWXCallBack("InfoGet") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        object = data.getJSONObject(Api.KEY_DATA);
                        if (object != null) {
                            path = (String) object.get("ShopPicture");
                            ImageUtils.setCircleHeaderImage(BaseInfoActivity.this,
                                    ImageUtils.getRightImgScreen(path, DensityUtil.dip2px(BaseInfoActivity.this,60),DensityUtil.dip2px(BaseInfoActivity.this,60)), mShopHead);
//                            ed_agency_phone.setText((String) object.get("ServicePhone"));
                            mShopName.setText((String) object.get("SellerName"));
                            mPhone.setText((String) object.get("SellerTel"));

                            String TradesID = ((String) object.get("TradeType"))
                                    .substring(0, 2);
                            for (int i = 0; i < parseArray.size(); i++) {
                                if (tradesCategoryAdapter.getItem(i).TradeId
                                        .equals(TradesID)) {
                                    parseArray.get(i).isSelect = true;
                                    mTrades.setText(tradesCategoryAdapter
                                            .getItem(i).TradeName);
                                    isReShow = true;
                                    getSubTrades(TradesID);
                                    break;
                                }
                            }
                            location = new Location();
                            location.name = (String) object.get("LocationAddress");
                            location.latitudes = ((BigDecimal) object
                                    .get("Latitude")).doubleValue();
                            location.Longitudes = ((BigDecimal) object
                                    .get("Longitude")).doubleValue();

                            mAddress.setText(location.name);
                            mMenpaihao.setText((String) object.get("Address"));
                            // 地级区域回显
                            province_id = object.getString("Province");
                            city_id = object.getString("City");
                            county_id = object.getString("County");
                            tv_agency.setText(object.getString("AreaName"));

                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }


    @Override
    protected void doOperate() {
        getTadeList();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_next:// 下一步
                getAllParams();
                break;

            case R.id.tv_updata_pic:// 上传图片
                if (null == mChoosePhotoDialog) {
                    mChoosePhotoDialog = new ChoosePhotoDialog(this, true,true,
                            REQUEST_CODE);
                }
                mChoosePhotoDialog.show();

                break;
            case R.id.iv_shop_head:// 上传图片
                if (null == mChoosePhotoDialog) {
                    mChoosePhotoDialog = new ChoosePhotoDialog(this, true,true,
                            REQUEST_CODE);
                }
                mChoosePhotoDialog.show();

                break;

            case R.id.ll_address:// 定位
                //没有定位权限,提示开启
                if (!PermissionUtil.hasPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})) {
                    PermissionUtil.requestPermissions(this, REQUEST_CODE_ASK_LOCATION, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
                } else {
                    PublicWay.startLocateActivity(this, REQUEST_LOCTION);
                }

                break;

            case R.id.ll_trades_select:// 类目
                popupWindow.setWidth(v.getWidth());
                if (!popupWindow.isShowing()) {
                    WWViewUtil.setPopInSDK7(popupWindow, v);
//                    popupWindow.showAsDropDown(v);
                }

                break;
            case R.id.ll_trades_select_sub:// 子类目
                if (parseArraySub != null && parseArraySub.size() > 0) {
                    showTradesSelectSub(v);
                }

                break;

            case R.id.ll_agency:

                PublicWay.startRegionPickActivity(this, REQUEST_CODE1);

                break;
            default:
                break;
        }
    }

    /**
     * 获得子行业列表
     */
    List<TradesCategory> parseArraySub;
    private String TradesID;

    private void showTradesSelectSub(final View v) {
            ListView inflate = (ListView) View.inflate(BaseInfoActivity.this,
                    R.layout.listview_trades, null);
            final TradesCategoryAdapter tradesCategoryAdapter = new TradesCategoryAdapter(
                    BaseInfoActivity.this);
            //重新设置数据
            tradesCategoryAdapter.setDatas(parseArraySub);
            inflate.setAdapter(tradesCategoryAdapter);
            inflate.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    for (int i = 0; i < parseArraySub.size(); i++) {
                        parseArraySub.get(i).isSelect = false;
                    }
                    // int tradeRate = (int)
                    // (tradesCategoryAdapter.getItem(position).TradeRate
                    // * 100);
                    parseArraySub.get(position).isSelect = true;
                    tradesCategoryAdapter.getItem(position).isSelect = true;
                    tv_trade_sub.setText(tradesCategoryAdapter.getItem(position).TradeName);
                    popupWindowSub.dismiss();
                }
            });
            popupWindowSub = new PopupWindow(inflate);
            popupWindowSub.setBackgroundDrawable(new BitmapDrawable());
            popupWindowSub.setWidth(LayoutParams.WRAP_CONTENT);
            popupWindowSub.setHeight(LayoutParams.WRAP_CONTENT);
            popupWindowSub.setOutsideTouchable(true);
            popupWindowSub.setFocusable(true);
            popupWindowSub.setWidth(v.getWidth());
        if (!popupWindowSub.isShowing()) {
            WWViewUtil.setPopInSDK7(popupWindowSub, v);
//            popupWindowSub.showAsDropDown(v);
        }

    }

    private void getSubTrades(String parentId) {
        showWaitDialog();
        RequestParams params = new RequestParams(
                ApiBaseData.getTradesCategory());
        params.addBodyParameter("parentId", parentId);
        x.http().get(params, new WWXCallBack("TradesGet") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                JSONArray jsonArray = data.getJSONArray("Data");
                parseArraySub = JSON.parseArray(jsonArray.toJSONString(),
                        TradesCategory.class);
                if (parseArraySub != null && parseArraySub.size() > 0) {
                    if (isReShow) {
                        isReShow = false;
                        for (int j = 0; j < parseArraySub.size(); j++) {
                            parseArraySub.get(j).isSelect = false;
                            if (object.get("TradeType")
                                    .equals(parseArraySub.get(j).TradeId)) {
                                TradesCategoryAdapter tradesCategoryAdapter = new TradesCategoryAdapter(
                                        BaseInfoActivity.this);
                                tradesCategoryAdapter.setDatas(parseArraySub);
                                parseArraySub.get(j).isSelect = true;
                                tradesCategoryAdapter.getItem(j).isSelect = true;
                                tv_trade_sub.setText(parseArraySub.get(j).TradeName);
                            }
                        }
                    } else {
                        tv_trade_sub.setText(parseArraySub.get(0).TradeName);
                        parseArraySub.get(0).isSelect = true;
                        showTradesSelectSub(findViewById(R.id.ll_trades_select_sub));

                    }
                }
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    /**
     * 准备上传
     */
    private void getAllParams() {
        String name = mShopName.getText().toString().trim();
        String menpai = mMenpaihao.getText().toString().trim();
        String phone = mPhone.getText().toString().trim();
//        String agency_phone = ed_agency_phone.getText().toString().trim();
        String texttrade_sub = tv_trade_sub.getText().toString();
        String LocationAddress = mAddress.getText().toString().trim();

//        if (TextUtils.isEmpty(agency_phone)) {
//            WWToast.showShort(R.string.agency_phone_empty);
//            return;
//        }
//
//        if (!RegexUtil.isMobile(agency_phone)) {
//
//            WWToast.showShort(R.string.agency_phone_error);
//            return;
//        }
        if (TextUtils.isEmpty(name)) {
            WWToast.showShort(R.string.shop_name_not_empty);
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            WWToast.showShort(R.string.please_input_shop_phone);
            return;
        }
        if (!(RegexUtil.isMobile(phone) || RegexUtil.isGuHua(phone) || RegexUtil.isGUHUA400_800(phone))) {
            WWToast.showShort(R.string.shop_phone_error);
            return;
        }
        if (TextUtils.isEmpty(texttrade_sub)) {
            WWToast.showShort(R.string.shop_trade_hint);
            return;
        }
        if (TextUtils.isEmpty(LocationAddress)) {
            WWToast.showShort(R.string.please_locate_shop_address);
            return;
        }

        if (TextUtils.isEmpty(menpai)) {
            WWToast.showShort(R.string.menpaihao_input_tips);
            return;
        }
        if (TextUtils.isEmpty(path)) {
            WWToast.showShort(R.string.please_upload_headimg);
            return;
        }

        if (TextUtils.isEmpty(province_id)) {
            WWToast.showShort(R.string.please_choice_area);
            return;
        }
        if (TextUtils.isEmpty(city_id)) {
            WWToast.showShort(R.string.please_choice_area);
            return;
        }
        if (TextUtils.isEmpty(county_id)) {
            WWToast.showShort(R.string.please_choice_area);
            return;
        }
        final JSONObject jsonObject = new JSONObject();
        final JSONObject childObject = new JSONObject();
        childObject.put("SellerName", name);
        childObject.put("Province", province_id);
        childObject.put("City", city_id);
        childObject.put("County", county_id);
//        childObject.put("ServicePhone", agency_phone);
        childObject.put("Address",menpai);
        childObject.put("LocationAddress",LocationAddress);

        childObject.put("Longitude", location.Longitudes);
        childObject.put("Latitude", location.latitudes);
        for (int i = 0; i < parseArraySub.size(); i++) {
            if (parseArraySub.get(i).isSelect) {
                childObject.put("TradeName", parseArraySub.get(i).TradeName);
                childObject.put("TradeType", parseArraySub.get(i).TradeId);
            }
        }

        childObject.put("TradeRate", 0);
        childObject.put("SellerTel", phone);
        RequestParams params = new RequestParams(ApiBaseData.upImage());
        final String newUrl = FileUtils.getCompressedImageFileUrl(path);
        params.addBodyParameter("file", new File(newUrl));
        params.setMultipart(true);

        if (path.startsWith("http")) {
            jsonObject.put("sessionId", sessionId);
            childObject.put("ShopPicture", path);
            jsonObject.put("data", childObject);
            regInfoUpdate(jsonObject);
            return;
        }
        showWaitDialog();
        x.http().post(params, new WWXCallBack("UpImage") {// 先上传图片

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                String url = data.getString("Data");
                childObject.put("ShopPicture", url);
                jsonObject.put("sessionId", sessionId);
                jsonObject.put("data", childObject);
                regInfoUpdate(jsonObject);
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                super.onError(arg0, arg1);
                WWToast.showShort(R.string.head_img_upload_fail_try_again);
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }

    private void regInfoUpdate(JSONObject jsonObject) {
        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiSeller.regInfoUpdate()),
                new WWXCallBack("RegInfoUpdate") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        PublicWay.startCommitInfoActivity(
                                BaseInfoActivity.this,
                                sessionId);
                        finish();
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }


    /**
     * 获得行业列表
     */
    List<TradesCategory> parseArray;
    private TradesCategoryAdapter tradesCategoryAdapter;

    private void getTadeList() {
        showWaitDialog();
        x.http().get(new RequestParams(ApiBaseData.getTradesCategory()),
                new WWXCallBack("TradesGet") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        JSONArray jsonArray = data.getJSONArray("Data");
                        parseArray = JSON.parseArray(jsonArray.toJSONString(),
                                TradesCategory.class);
                        ListView inflate = (ListView) View.inflate(
                                BaseInfoActivity.this,
                                R.layout.listview_trades, null);
                        tradesCategoryAdapter = new TradesCategoryAdapter(
                                BaseInfoActivity.this);
                        tradesCategoryAdapter.setDatas(parseArray);
                        inflate.setAdapter(tradesCategoryAdapter);
                        inflate.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent,
                                                    View view, int position, long id) {
                                for (int i = 0; i < parseArray.size(); i++) {
                                    parseArray.get(i).isSelect = false;
                                }
                                parseArray.get(position).isSelect = true;
                                TradesID = tradesCategoryAdapter
                                        .getItem(position).TradeId;
                                getSubTrades(TradesID);
                                mTrades.setText(tradesCategoryAdapter
                                        .getItem(position).TradeName);
                                popupWindow.dismiss();
                            }
                        });
                        popupWindow = new PopupWindow(inflate);
                        popupWindow.setBackgroundDrawable(new BitmapDrawable());
                        popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
                        popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
                        popupWindow.setOutsideTouchable(true);
                        popupWindow.setFocusable(true);
                        mayInitData();
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }

                });

    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        if (arg1 == RESULT_OK) {
            if (arg0 == REQUEST_CODE) {
                if (arg2 != null) {
                    path = arg2.getStringExtra(Consts.KEY_DATA);
                    ImageUtils.setCircleHeaderImage(this, path, mShopHead);
                }
            } else if (arg0 == REQUEST_LOCTION) {
                // 地理位置
                location = JSON.parseObject(arg2.getStringExtra("location"),
                        Location.class);
                mAddress.setText(location.name);
                mMenpaihao.setText(location.street);
            }
            if (arg0 == REQUEST_CODE1) {
                if (arg2 != null) {


                    province_id = arg2.getStringExtra("province_id");
                    city_id = arg2.getStringExtra("city_id");
                    county_id = arg2.getStringExtra("county_id");
                    StringBuffer sbf = new StringBuffer();
                    sbf.append(arg2.getStringExtra("province_name") + " ");
                    sbf.append(arg2.getStringExtra("city_name") + " ");
                    sbf.append(arg2.getStringExtra("county_name"));
                    tv_agency.setText(sbf.toString());

                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_ASK_LOCATION) {
            PublicWay.startLocateActivity(this, REQUEST_LOCTION);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_ASK_LOCATION) {
            WWToast.showShort(R.string.locate_permission_deny_cannot_locate);
        }
    }
}
