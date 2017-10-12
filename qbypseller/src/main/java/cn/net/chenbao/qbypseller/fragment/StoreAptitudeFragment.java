package cn.net.chenbao.qbypseller.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.RealInfo;
import cn.net.chenbao.qbypseller.utils.DensityUtil;
import cn.net.chenbao.qbypseller.utils.ImageUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.x;

/**
 * 店铺资质
 *
 * @author xl
 * @date 2016-7-31 下午8:31:35
 * @description 之前的资质用的"zizhi,res",aptitude之前感觉不准确,所以没用这个
 */
public class StoreAptitudeFragment extends FatherFragment implements
        OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_store_aptitude;
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        requestData();
    }

    private RealInfo info;

    private void requestData() {
        showWaitDialog();
        x.http().get(
                ParamsUtils.getSessionParams(ApiSeller.realInfoGet()),
                new WWXCallBack("RealInfoGet") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        info = JSON.parseObject(data
                                        .getJSONObject(Api.KEY_DATA).toJSONString(),
                                RealInfo.class);
                        ImageView front = (ImageView) mGroup
                                .findViewById(R.id.iv_card_front);
                        ImageView back = (ImageView) mGroup
                                .findViewById(R.id.iv_card_back);
                        ImageView store = (ImageView) mGroup
                                .findViewById(R.id.iv_store_image);
                        ImageView licence = (ImageView) mGroup
                                .findViewById(R.id.iv_licence);
                        ImageView promise = (ImageView) mGroup
                                .findViewById(R.id.iv_seller_promise_image);

                        front.setOnClickListener(StoreAptitudeFragment.this);
                        back.setOnClickListener(StoreAptitudeFragment.this);
                        store.setOnClickListener(StoreAptitudeFragment.this);
                        licence.setOnClickListener(StoreAptitudeFragment.this);
                        promise.setOnClickListener(StoreAptitudeFragment.this);

                        setImg(info.LegalFrontPicture, front);
                        setImg(info.LegalBackPicture, back);
                        setImg(info.LicencePicture, licence);
                        setImg(info.ShopPicture, store);
                        setImg(info.PromisePicture, promise);

                        ((TextView) mGroup.findViewById(R.id.tv_card_name))
                                .setText(info.LicenceName);
                        ((TextView) mGroup
                                .findViewById(R.id.tv_shopkeeper_name))
                                .setText(info.LegalName);
                        ((TextView) mGroup
                                .findViewById(R.id.tv_shopkeeper_idcard))
                                .setText(info.LegalNo);// 身份证
                        ((TextView) mGroup.findViewById(R.id.tv_license_number))
                                .setText(info.LicenceNo);// 营业执照
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }

    private void setImg(String url, ImageView iv) {
        ImageUtils.setCommonImage(getActivity(), ImageUtils.getRightImgScreen(url, DensityUtil.dip2px(getActivity(), 200), DensityUtil.dip2px(getActivity(), 0))
                , iv,R.drawable.lodingfail);
    }

    @Override
    public void onClick(View v) {
        if (info == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.iv_card_front:
                PublicWay.startBigImageActivity(getActivity(),
                        info.LegalFrontPicture);
                break;
            case R.id.iv_card_back:
                PublicWay.startBigImageActivity(getActivity(),
                        info.LegalBackPicture);
                break;
            case R.id.iv_store_image:
                PublicWay.startBigImageActivity(getActivity(), info.ShopPicture);
                break;
            case R.id.iv_licence:
                PublicWay.startBigImageActivity(getActivity(), info.LicencePicture);
                break;
            case R.id.iv_seller_promise_image:
                PublicWay.startBigImageActivity(getActivity(), info.PromisePicture);
                break;

            default:
                break;
        }
    }
}
