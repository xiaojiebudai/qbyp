package cn.net.chenbao.qbypseller.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.activity.RenewServiceActivity;
import cn.net.chenbao.qbypseller.activity.StoreEvaluateActivity;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.Location;
import cn.net.chenbao.qbypseller.bean.SellerInfo;
import cn.net.chenbao.qbypseller.dialog.CommonDialog;
import cn.net.chenbao.qbypseller.dialog.TwoLevelDialog;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.DialogUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PermissionUtil;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbypseller.utils.TimeUtil;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.view.RatingBar;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * 店铺信息
 *
 * @author xl
 * @date 2016-7-31 下午8:29:37
 * @description
 */
public class StoreInfoFragment extends FatherFragment implements
        OnClickListener, TwoLevelDialog.OnSelectOkLisentner, TimePickerDialog.OnTimeSetListener, PermissionUtil.PermissionCallbacks {
    public static final int REQUESTCODE_PAY = 888;
    public static final int REQUESTCODE_ADDRESS = 666;
    public static final int REQUESTCODE_PHONE = 777;
    private EditText mEdt_des;// 店铺说明

    private TextView tv_time;// 开始时间-结束营业时间


    private static final int SELECT_START = 0;
    private static final int SELECT_STOP = 1;
    private int currentSelect;

    private SellerInfo sellerInfo;

    private TextView mTv_doBusiness;

    private TextView mTv_shop_end_tiem;

    private TextView tv_shop_name;// 店铺名称

    private TextView tv_address;
    private TextView tv_shop_phone;
    private TextView tv_save_menpaihao;
    private EditText ed_menpai;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_store_info;
    }


    @Override
    protected void initView() {
        mGroup.findViewById(R.id.tv_renew).setOnClickListener(this);
        mEdt_des = (EditText) mGroup.findViewById(R.id.edt_description);

        tv_time = (TextView) mGroup.findViewById(R.id.tv_time);

        tv_shop_name = (TextView) mGroup.findViewById(R.id.tv_shop_name);

        mTv_shop_end_tiem = (TextView) mGroup.findViewById(R.id.shop_end_time);
        tv_address = (TextView) mGroup.findViewById(R.id.tv_address);
        tv_shop_phone = (TextView) mGroup.findViewById(R.id.tv_shop_phone);
        tv_save_menpaihao = (TextView) mGroup.findViewById(R.id.tv_save_menpaihao);
        ed_menpai = (EditText) mGroup.findViewById(R.id.ed_menpai);
//        tv_koudian = (EditText) mGroup.findViewById(R.id.tv_koudian);
        mGroup.findViewById(R.id.ll_time_choose).setOnClickListener(this);
        mGroup.findViewById(R.id.tv_save_info).setOnClickListener(this);
        mGroup.findViewById(R.id.ll_shop_phone).setOnClickListener(this);
        mGroup.findViewById(R.id.ll_select_address).setOnClickListener(this);
        tv_save_menpaihao.setOnClickListener(this);
        //设置下划线
        tv_save_menpaihao.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_save_menpaihao.getPaint().setAntiAlias(true);//抗锯齿



        sellerInfo = SharedPreferenceUtils.getInstance().getSellerInfo();
        if (sellerInfo != null) {
            mTv_doBusiness = (TextView) mGroup
                    .findViewById(R.id.tv_do_business);
            mTv_doBusiness.setOnClickListener(this);
            mTv_shop_end_tiem.setText(getString(R.string.shop_tiem_end) + ": "
                    + TimeUtil.getOnlyDate(sellerInfo.ExpireTime * 1000));

            RatingBar ratingBar = (RatingBar) mGroup
                    .findViewById(R.id.ratingbar);
            ratingBar.setStar(Float.parseFloat(sellerInfo.JudgeLevel));
            tv_shop_name.setText(sellerInfo.SellerName);
            tv_shop_phone.setText(sellerInfo.SellerTel);

            tv_address.setText(sellerInfo.LocationAddress);
            ed_menpai.setText(sellerInfo.Address);
            //获得焦点
            ed_menpai.requestFocus();
            ed_menpai.setSelection(TextUtils.isEmpty(sellerInfo.Address)?0:ed_menpai.getText().length());
//          tv_koudian.setText((int)(sellerInfo.consumePay * 100)  + "");
//			mEdt_des.setText(sellerInfo.Explain);

            startTime = sellerInfo.BusinessStart * 1000;
            stopTime = sellerInfo.BusinessEnd * 1000;
            tv_time.setText(getHourAndMin(startTime) + "  --  " + getHourAndMin(stopTime));


            switch (sellerInfo.Status) {
                case SellerInfo.STATUS_NORMAL:
                    mTv_doBusiness.setText(R.string.stop_do_business);
                    break;
                case SellerInfo.STATUS_STOP:
                    mTv_doBusiness.setText(R.string.start_do_business);
                    break;

                default:
                    break;
            }
            mGroup.findViewById(R.id.tv_do_business).setOnClickListener(this);
            mGroup.findViewById(R.id.ll_storeevaluate).setOnClickListener(this);

        }

    }

    //    private TwoLevelDialog mTwoLevelDialog;
    private TimePickerDialog tpd, tpd1;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_renew:
                Intent intent = new Intent(
                        getActivity(), RenewServiceActivity.class);
                intent.putExtra(Consts.KEY_MODULE, RenewServiceActivity.NORMAL_RENEW);
                StoreInfoFragment.this.startActivityForResult(intent, REQUESTCODE_PAY);
                break;
            case R.id.tv_do_business:
                switch (sellerInfo.Status) {
                    case SellerInfo.STATUS_NORMAL:
                        final CommonDialog commonDialog = DialogUtils.getCommonDialogTwiceConfirm(
                                getActivity(), R.string.set_close_down_cannot_search_your_shop, true);
                        commonDialog.setRightButtonCilck(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                requestStop();
                                commonDialog.dismiss();
                            }
                        });
                        commonDialog.show();
                        break;
                    case SellerInfo.STATUS_STOP:
                        requestStart();
                        break;

                    default:
                        break;
                }
                break;
            case R.id.tv_save_info:
                requestUpdateInfo();
                break;
            case R.id.ll_storeevaluate:
                startActivity(new Intent(getActivity(), StoreEvaluateActivity.class));
                break;
            case R.id.ll_time_choose:
                if (tpd == null) {
                    Calendar now = Calendar.getInstance();
                    tpd = TimePickerDialog.newInstance(
                            StoreInfoFragment.this,
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            true
                    );

                    tpd.setAccentColor(getResources().getColor(R.color.yellow_ww));
                    tpd.setVersion(TimePickerDialog.Version.VERSION_2);

                    tpd.setTitle(getString(R.string.start_business_time));

                }
                currentSelect = SELECT_START;
                tpd.show(getActivity().getFragmentManager(), "Timepickerdialog");
                break;
            case R.id.ll_select_address:
                //选择地址
                //没有定位权限,提示开启
                if (!PermissionUtil.hasPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})) {
                    PermissionUtil.requestPermissions(StoreInfoFragment.this, REQUESTCODE_ADDRESS, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION});
                } else {
                    PublicWay.startLocateActivity(StoreInfoFragment.this, REQUESTCODE_ADDRESS);
                }
                break;
            case R.id.ll_shop_phone:
                //修改电话
                PublicWay.startEditShopPhoneActivity(StoreInfoFragment.this, sellerInfo.SellerTel, REQUESTCODE_PHONE);
                break;
            case R.id.tv_save_menpaihao:
                //保存地址
                if(TimeUtil.isFastClick()){
                    WWToast.showShort(R.string.operation_too_fast);
                }else{
                    updateAddress();
                }

                break;
            default:
                break;
        }
    }

    private void requestUpdateInfo() {

        JSONObject object = new JSONObject();
        final String des = mEdt_des.getText().toString().trim();


        object.put("explain", des);

        object.put("busStart", startTime / 1000);
        object.put("busEnd", stopTime / 1000);
        showWaitDialog();
        RequestParams params = ParamsUtils.getPostJsonParamsWithSession(object,
                ApiSeller.infoUpdate());


        x.http().post(
                params,
                new WWXCallBack("InfoUpdate") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        if (sellerInfo != null) {
                            sellerInfo.Explain = des;
//                            sellerInfo.consumePay = (Integer.parseInt(peibi)) / 100.00;
                            sellerInfo.BusinessStart = startTime / 1000;
                            sellerInfo.BusinessEnd = stopTime / 1000;
                            SharedPreferenceUtils.getInstance().saveSellerInfo(
                                    sellerInfo);// 更新本地信息
                        }
                        tv_time.setText(getHourAndMin(startTime) + "  --  " + getHourAndMin(stopTime));
                        WWToast.showCostomSuccess(R.string.set_success);

                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });

    }

    /**
     * 营业
     */
    private void requestStart() {
        showWaitDialog();
        x.http()
                .post(ParamsUtils.getSessionParamsPostJson(ApiSeller
                                .businessStart()),
                        new WWXCallBack("BusinessStart") {

                            @Override
                            public void onAfterSuccessOk(JSONObject data) {
                                sellerInfo.Status = SellerInfo.STATUS_NORMAL;
                                SharedPreferenceUtils.getInstance()
                                        .saveSellerInfo(sellerInfo);// 更新本地信息
                                mTv_doBusiness
                                        .setText(R.string.stop_do_business);

                            }

                            @Override
                            public void onAfterFinished() {
                                dismissWaitDialog();
                            }
                        });
    }

    /**
     * 停业
     */
    private void requestStop() {
        showWaitDialog();
        x.http().post(
                ParamsUtils.getSessionParamsPostJson(ApiSeller.businessStop()),
                new WWXCallBack("BusinessStop") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        sellerInfo.Status = SellerInfo.STATUS_STOP;
                        SharedPreferenceUtils.getInstance().saveSellerInfo(
                                sellerInfo);// 更新本地信息
                        mTv_doBusiness.setText(R.string.start_do_business);
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }

    private long startTime;
    private long stopTime;

    @Override
    public void selectOk(String first, String second) {

    }

    /**
     * 1970.01.01.H.M.0
     *
     * @param first
     * @param second
     * @return
     * @author xl
     * @date:2016-8-24下午5:41:36
     * @description
     */
    private long getSelectTimeInMillis(String first, String second) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        instance.set(Calendar.HOUR_OF_DAY, (Integer.parseInt(first)));
        instance.set(Calendar.MINUTE, (Integer.parseInt(second)));
        instance.set(Calendar.YEAR, 1970);
        instance.set(Calendar.MONTH, Calendar.JANUARY);
        instance.set(Calendar.DATE, 1);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);
        return instance.getTimeInMillis();
    }

    private String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return format.format(new Date(time));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUESTCODE_PAY) {
                updataSellerInfo();
            } else if (requestCode == REQUESTCODE_PHONE) {
                updataSellerInfo();
            } else if (requestCode == REQUESTCODE_ADDRESS) {
                // 地理位置
                 Location  location = JSON.parseObject(data.getStringExtra("location"),
                        Location.class);
                sellerInfo.Latitude=location.latitudes+"";
                sellerInfo.Longitude=location.Longitudes+"";
                tv_address.setText(location.name);
                ed_menpai.setText(location.street);
                //获得焦点
                ed_menpai.requestFocus();
                ed_menpai.setSelection(TextUtils.isEmpty(location.street)?0:ed_menpai.getText().length());
                //自动保存一次
                updateAddress();

            }
        }
    }

    /**
     * 修改商家地址
     */
    private void updateAddress() {
        final String menpai = ed_menpai.getText().toString().trim();
        final String locationAddress = tv_address.getText().toString().trim();
        if (TextUtils.isEmpty(locationAddress)||TextUtils.isEmpty(sellerInfo.Longitude)||TextUtils.isEmpty(sellerInfo.Latitude)) {
            WWToast.showShort(R.string.please_locate_shop_address);
            return;
        }

        if (TextUtils.isEmpty(menpai)) {
            WWToast.showShort(R.string.menpaihao_input_tips);
            return;
        }


        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Api.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        jsonObject.put("longitude", sellerInfo.Longitude);
        jsonObject.put("latitude", sellerInfo.Latitude);
        jsonObject.put("address", menpai);
        jsonObject.put("locationAddress", locationAddress);
        RequestParams params = ParamsUtils.getPostJsonParams(jsonObject, ApiSeller.InfoLocationUpdate());


        showWaitDialog();
        x.http().post(params, new WWXCallBack("InfoLocationUpdate") {
            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (sellerInfo != null) {
                    sellerInfo.Address = menpai;
                    sellerInfo.LocationAddress =locationAddress;
                    SharedPreferenceUtils.getInstance().saveSellerInfo(
                            sellerInfo);// 更新本地信息
                }
              WWToast.showShort(R.string.set_success);
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }

    /**
     * 更新可修改信息
     */
    private void updataSellerInfo() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiSeller.infoGet());
        params.addQueryStringParameter(Api.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        x.http().get(params,
                new WWXCallBack("InfoGet") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        JSONObject object = data.getJSONObject(Api.KEY_DATA);
                        if (object != null) {
                            String info_json = object.toJSONString();
                            SharedPreferenceUtils instance = SharedPreferenceUtils
                                    .getInstance();
                            instance.saveSellerInfo(info_json);
                             sellerInfo = JSON.parseObject(info_json,
                                    SellerInfo.class);
                            mTv_shop_end_tiem.setText(getString(R.string.shop_tiem_end) + ": "
                                    + TimeUtil.getOnlyDate(sellerInfo.ExpireTime * 1000));
                            tv_shop_phone.setText(sellerInfo.SellerTel);
                            tv_address.setText(sellerInfo.LocationAddress);
                            ed_menpai.setText(sellerInfo.Address);
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        switch (currentSelect) {
            case SELECT_START:

                startTime = getSelectTimeInMillis(hourOfDay + "", minute + "");

                if (tpd1 == null) {
                    Calendar now = Calendar.getInstance();
                    tpd1 = TimePickerDialog.newInstance(
                            StoreInfoFragment.this,
                            now.get(Calendar.HOUR_OF_DAY),
                            now.get(Calendar.MINUTE),
                            true
                    );

                    tpd1.setAccentColor(getResources().getColor(R.color.yellow_ww));
                    tpd1.setVersion(TimePickerDialog.Version.VERSION_2);

                    tpd1.setTitle(getString(R.string.close_business_time));

                }
                currentSelect = SELECT_STOP;
                tpd1.show(getActivity().getFragmentManager(), "Timepickerdialog1");


                break;
            case SELECT_STOP:
                stopTime = getSelectTimeInMillis(hourOfDay + "", minute + "");

                requestUpdateInfo();
                break;

            default:
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if (requestCode == REQUESTCODE_ADDRESS) {
            PublicWay.startLocateActivity(StoreInfoFragment.this, REQUESTCODE_ADDRESS);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUESTCODE_ADDRESS) {
            WWToast.showShort(R.string.locate_permission_deny_cannot_locate);
        }
    }
}
