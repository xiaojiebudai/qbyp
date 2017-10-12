package cn.net.chenbao.qbyp.fragment;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.bean.TradesMessage;
import cn.net.chenbao.qbyp.utils.BaiDuMapUtils;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.PermissionUtil;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.view.CommonDialog;

import java.util.List;

/**
 * 商家模块
 *
 * @author xl
 * @date 2016-7-27 下午10:03:14
 * @description
 */
public class SellerFragment extends FatherFragment implements PermissionUtil.PermissionCallbacks {
    /**
     * 百度地图
     */
    private TextureMapView mBaiDuMap;
    private TradesMessage mTradesMessage;
    private BaiduMap map;
    private TextView mTvIntroduce;
    private TextView mTvAddress;
    private TextView mTvOpenTime;
    private TextView mTvPhone;
    private final int REQUEST_CODE_CALL_PHONE = 110;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_seller;
    }

    @Override
    protected void initView() {
        String json = getArguments().getString(Consts.KEY_DATA);
        mTradesMessage = JSON.parseObject(json, TradesMessage.class);
        mBaiDuMap = (TextureMapView) mGroup.findViewById(R.id.mapview);
        map = mBaiDuMap.getMap();
        map.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mTvIntroduce = (TextView) mGroup.findViewById(R.id.tv_introduce);
        mTvAddress = (TextView) mGroup.findViewById(R.id.tv_address);
        mTvOpenTime = (TextView) mGroup.findViewById(R.id.tv_open_time);
        mTvPhone = (TextView) mGroup.findViewById(R.id.tv_phone);
        mTvPhone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final CommonDialog commonDialog = DialogUtils.getCommonDialog(getActivity(), R.string.connect_seller_right_now);
                commonDialog.getButtonLeft(R.string.cancel).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        commonDialog.dismiss();
                    }
                });
                commonDialog.getButtonRight(R.string.call_phone).setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //检查打电话权限
                        if (PermissionUtil.hasPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE})) {
                            call();
                        } else {
                            PermissionUtil.requestPermissions(SellerFragment.this, REQUEST_CODE_CALL_PHONE, new String[]{Manifest.permission.CALL_PHONE});
                        }
                        commonDialog.dismiss();
                    }
                });
                commonDialog.show();
            }
        });
        mTvAddress.setText(mTradesMessage.Address);
        mTvPhone.setText(mTradesMessage.SellerTel);
        mTvIntroduce.setText(mTradesMessage.Explain);
        mTvOpenTime.setText(TimeUtil
                .getHourAndMin(mTradesMessage.BusinessStart * 1000)
                + "-"
                + TimeUtil.getHourAndMin(mTradesMessage.BusinessEnd * 1000));
        LatLng latLng = new LatLng(mTradesMessage.Latitude,
                mTradesMessage.Longitude);
        BaiDuMapUtils.setLoactionToMap(map, 17, latLng);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBaiDuMap.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mBaiDuMap.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mBaiDuMap.onPause();
    }

    public void call() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri
                .parse("tel:" + mTradesMessage.SellerTel));
        startActivity(intent);
    }

    public static SellerFragment newInstance(String json) {
        SellerFragment sellerFragment = new SellerFragment();
        Bundle args = new Bundle();
        args.putString(Consts.KEY_DATA, json);
        sellerFragment.setArguments(args);
        return sellerFragment;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, SellerFragment.this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        call();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if (!PermissionUtil.hasPermissions(getActivity(), list.get(i))) {
                WWToast.showShort(String.format(getString(R.string.permission_deny_), list.get(i)));
            }
        }
    }
}
