package cn.net.chenbao.qbypseller.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiBaseData;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.RealInfo;
import cn.net.chenbao.qbypseller.imageSelector.MultiImageSelectorActivity;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.DensityUtil;
import cn.net.chenbao.qbypseller.utils.FileUtils;
import cn.net.chenbao.qbypseller.utils.ImageUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.RegexUtil;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.view.RegisterTimeLine;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * 提交资料
 *
 * @author licheng
 */
public class CommitInfoActivity extends FatherActivity implements
        OnClickListener {
    public static final int REQUEST_PIC1 = 10001;
    public static final int REQUEST_PIC2 = 10002;
    public static final int REQUEST_PIC3 = 10003;
    public static final int REQUEST_PIC4 = 10005;
    public static final int REQUEST_PIC5 = 10006;

    private EditText faRenName;
    private EditText mIdentityCard;
    private EditText mCompanyName;
    private EditText mYinteNum;
    private ImageView mSellerPromise;
    private ImageView mZhengMian;
    private ImageView mFanMian;
    private ImageView mZhiZhao;
    private ImageView mShopPic;
    private View tv_1;
    private View tv_2;
    private View tv_3;
    private View tv_4;
    private View tv_5;

    private String pic1;
    private String pic2;
    private String pic3;
    private String pic4;
    private String pic5;

    private String picUrl1;
    private String picUrl2;
    private String picUrl3;
    private String picUrl4;
    private String picUrl5;

    private String sessionId;


    @Override
    protected int getLayoutId() {
        return R.layout.act_commit_info;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.business_register, true);
        sessionId = getIntent().getStringExtra(Consts.KEY_DATA);
        WWApplication.getInstance().setisHome(false);
    }

    @Override
    protected void initView() {
        RegisterTimeLine rt = (RegisterTimeLine) findViewById(R.id.rt);
        rt.setStep(2);
        TextView commit = (TextView) findViewById(R.id.tv_commit);
        TextView base = (TextView) findViewById(R.id.tv_base);
        commit.setTextColor(getResources().getColor(R.color.yellow_ww));
        base.setTextColor(getResources().getColor(R.color.yellow_ww));
        faRenName = (EditText) findViewById(R.id.ed_faren_name);
        mIdentityCard = (EditText) findViewById(R.id.ed_identity_card);
        mCompanyName = (EditText) findViewById(R.id.ed_company_name);
        mYinteNum = (EditText) findViewById(R.id.ed_yinye_num);
        mSellerPromise = (ImageView) findViewById(R.id.iv_seller_promise_img);
        mZhengMian = (ImageView) findViewById(R.id.iv_shenfen_zheng);
        mFanMian = (ImageView) findViewById(R.id.iv_shenfen_fan);
        mZhiZhao = (ImageView) findViewById(R.id.iv_zhizhao);
        mShopPic = (ImageView) findViewById(R.id.iv_shop_pic);

        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        tv_4 = findViewById(R.id.tv_4);
        tv_5 = findViewById(R.id.tv_5);

        findViewById(R.id.tv1).setOnClickListener(this);
        findViewById(R.id.tv2).setOnClickListener(this);
        findViewById(R.id.tv3).setOnClickListener(this);
        findViewById(R.id.tv4).setOnClickListener(this);
        findViewById(R.id.tv5).setOnClickListener(this);
        findViewById(R.id.tv_next).setOnClickListener(this);
    }

    @Override
    protected void doOperate() {
        reShowData();
    }

    private RealInfo info;

    private void reShowData() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiSeller.realInfoGet());
        params.addQueryStringParameter(Api.KEY_SESSIONID, sessionId);
        x.http().get(
                params,
                new WWXCallBack("RealInfoGet") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        info = JSON.parseObject(data
                                        .getJSONObject(Api.KEY_DATA).toJSONString(),
                                RealInfo.class);
                        if (info != null) {
                            faRenName.setText(info.LegalName);
                            mIdentityCard.setText(info.LegalNo);
                            mCompanyName.setText(info.LicenceName);
                            mYinteNum.setText(info.LicenceNo);
                            picUrl1 = info.PromisePicture;
                            picUrl2 = info.LegalFrontPicture;
                            picUrl3 = info.LegalBackPicture;
                            picUrl4 = info.LicencePicture;
                            picUrl5 = info.ShopPicture;

                            tv_2.setVisibility(View.GONE);
                            tv_3.setVisibility(View.GONE);
                            tv_4.setVisibility(View.GONE);
                            tv_5.setVisibility(View.GONE);
                            if (!picUrl1.isEmpty()) {
                                tv_1.setVisibility(View.GONE);
                                ImageUtils.setCommonImageNo(CommitInfoActivity.this,
                                        getRightImgScreen(picUrl1, DensityUtil.dip2px(CommitInfoActivity.this, 90), DensityUtil.dip2px(CommitInfoActivity.this, 90)), mSellerPromise);
                            }
                            ImageUtils.setCommonImageNo(CommitInfoActivity.this,
                                    getRightImgScreen(picUrl2, DensityUtil.dip2px(CommitInfoActivity.this, 90), DensityUtil.dip2px(CommitInfoActivity.this, 90)), mZhengMian);
                            ImageUtils.setCommonImageNo(CommitInfoActivity.this,
                                    getRightImgScreen(picUrl3, DensityUtil.dip2px(CommitInfoActivity.this, 90), DensityUtil.dip2px(CommitInfoActivity.this, 90)), mFanMian);
                            ImageUtils.setCommonImageNo(CommitInfoActivity.this,
                                    getRightImgScreen(picUrl4, DensityUtil.dip2px(CommitInfoActivity.this, 90), DensityUtil.dip2px(CommitInfoActivity.this, 90)), mZhiZhao);
                            ImageUtils.setCommonImageNo(CommitInfoActivity.this,
                                    getRightImgScreen(picUrl5, DensityUtil.dip2px(CommitInfoActivity.this, 90), DensityUtil.dip2px(CommitInfoActivity.this, 90)), mShopPic);
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv1:
                PublicWay.startSelectImageActivity(this, 1, null, true, true, REQUEST_PIC1);
//                PublicWay.startImageSelectActivity(this, REQUEST_PIC1, false,
//                        ImageSelectActivity.MODE_PHOTO_ALBUM);
                break;
            case R.id.tv2:
                PublicWay.startSelectImageActivity(this, 1, null, true, true, REQUEST_PIC2);
//                PublicWay.startImageSelectActivity(this, REQUEST_PIC2, false,
//                        ImageSelectActivity.MODE_PHOTO_ALBUM);

                break;
            case R.id.tv3:
                PublicWay.startSelectImageActivity(this, 1, null, true, true, REQUEST_PIC3);
//                PublicWay.startImageSelectActivity(this, REQUEST_PIC3, false,
//                        ImageSelectActivity.MODE_PHOTO_ALBUM);
                break;
            case R.id.tv4:
                PublicWay.startSelectImageActivity(this, 1, null, true, true, REQUEST_PIC4);
//                PublicWay.startImageSelectActivity(this, REQUEST_PIC4, false,
//                        ImageSelectActivity.MODE_PHOTO_ALBUM);
                break;
            case R.id.tv5:
                PublicWay.startSelectImageActivity(this, 1, null, true, true, REQUEST_PIC5);
//                PublicWay.startImageSelectActivity(this, REQUEST_PIC5, false,
//                        ImageSelectActivity.MODE_PHOTO_ALBUM);
                break;

            case R.id.tv_next:
                uploadParams();
                break;
            default:
                break;
        }

    }

    private void uploadParams() {

        String name = faRenName.getText().toString().trim();
        String cardId = mIdentityCard.getText().toString().trim();
        String companyName = mCompanyName.getText().toString().trim();
        String yinyeNum = mYinteNum.getText().toString().trim();

        if (TextUtils.isEmpty(picUrl2) || TextUtils.isEmpty(picUrl3)
                || TextUtils.isEmpty(picUrl4) || TextUtils.isEmpty(picUrl5)) {
            WWToast.showShort(R.string.pic_not_all);
            return;
        }

        if (TextUtils.isEmpty(name)) {
            WWToast.showShort(R.string.please_input_name);
            return;
        }
        if (RegexUtil.checkHasSpecial(name)) {
            WWToast.showShort(R.string.name_cannot_contain_special_symbol);
            return;
        }
        if (RegexUtil.checkHasNum(name)) {
            WWToast.showShort(R.string.name_cannot_contain_number);
            return;
        }
        if (TextUtils.isEmpty(cardId)) {
            WWToast.showShort(R.string.please_input_id);
            return;
        }
        if (TextUtils.isEmpty(companyName)) {
            WWToast.showShort(R.string.please_input_company_name);
            return;
        }
        if (TextUtils.isEmpty(yinyeNum)) {
            WWToast.showShort(R.string.please_input_company_id);
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", sessionId);
        JSONObject childJson = new JSONObject();
        childJson.put("LegalName", name);
        childJson.put("LegalNo", cardId);
        childJson.put("LicenceName", companyName);
        childJson.put("LicenceNo", yinyeNum);
//        childJson.put("PromisePicture", picUrl1);
        childJson.put("LegalFrontPicture", picUrl2);
        childJson.put("LegalBackPicture", picUrl3);
        childJson.put("LicencePicture", picUrl4);
        childJson.put("ShopPicture", picUrl5);
        jsonObject.put("data", childJson);
        showWaitDialog();
        RequestParams postJsonParams = ParamsUtils.getPostJsonParams(
                jsonObject, ApiSeller.RealInfoUpdate());
        x.http().post(postJsonParams, new WWXCallBack("RegRealUpdate") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                regRealCommit();
                // PublicWay.startOpenServerActivity(CommitInfoActivity.this,
                // sessionId);
                // finish();
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }

    /**
     * 提交审核
     */
    protected void regRealCommit() {
        // TODO Auto-generated method stub
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sessionId", sessionId);
        RequestParams postJsonParams = ParamsUtils.getPostJsonParams(
                jsonObject, ApiSeller.regRealCommit());
        showWaitDialog();
        x.http().post(postJsonParams, new WWXCallBack("RegRealCommit") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (info != null && info.PayStatus == 1) {
                    //无需支付
                    PublicWay.startCheckActivity(CommitInfoActivity.this,
                            CheckActivity.MODE_CHECK, null);
                } else {
                    PublicWay.startOpenServerActivity(CommitInfoActivity.this,
                            sessionId);
                }
                finish();
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
            switch (arg0) {
                case REQUEST_PIC1:
                    pic1 = arg2
                            .getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0);
                    if (pic1 != null) {
                        uploadImg(pic1, REQUEST_PIC1, tv_1, mSellerPromise, R.string.seller_promise_upload_fail);
                    }
                    break;
                case REQUEST_PIC2:
                    pic2 = arg2
                            .getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0);
                    if (pic2 != null) {
                        uploadImg(pic2, REQUEST_PIC2, tv_2, mZhengMian, R.string.idcard_front_upload_fail);
                    }
                    break;
                case REQUEST_PIC3:
                    pic3 = arg2
                            .getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0);
                    if (pic3 != null) {
                        uploadImg(pic3, REQUEST_PIC3, tv_3, mFanMian, R.string.idcard_back_upload_fail);
                    }
                    break;
                case REQUEST_PIC4:
                    pic4 = arg2
                            .getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0);
                    if (pic4 != null) {
                        uploadImg(pic4, REQUEST_PIC4, tv_4, mZhiZhao, R.string.company_id_upload_fail);
                    }
                    break;
                case REQUEST_PIC5:
                    pic5 = arg2
                            .getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT).get(0);
                    if (pic5 != null) {
                        uploadImg(pic5, REQUEST_PIC5, tv_5, mShopPic, R.string.shop_img_upload_fail);
                    }
                    break;
                default:
                    break;
            }
        }

    }

    private void uploadImg(final String pic, final int REQUEST_PIC, final View tv, final ImageView iv, final int infoRes) {
        showWaitDialog();
        RequestParams params = new RequestParams(
                ApiBaseData.upImage());
        params.setMultipart(true);
        final String newUrl = FileUtils.getCompressedImageFileUrl1(pic);
        params.addBodyParameter("file",
                new File(newUrl));

        x.http().post(params, new WWXCallBack("UpImage") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                switch (REQUEST_PIC) {
                    case REQUEST_PIC1:
                        picUrl1 = data.getString("Data");
                        break;
                    case REQUEST_PIC2:
                        picUrl2 = data.getString("Data");
                        break;
                    case REQUEST_PIC3:
                        picUrl3 = data.getString("Data");
                        break;
                    case REQUEST_PIC4:
                        picUrl4 = data.getString("Data");
                        break;
                    case REQUEST_PIC5:
                        picUrl5 = data.getString("Data");
                        break;
                }
                tv.setVisibility(View.GONE);
                ImageUtils.setCommonImage(CommitInfoActivity.this, pic, iv);
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {

                super.onError(arg0, arg1);
                WWToast.showShort(infoRes);
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });


    }

    private String getRightImgScreen(String picUrl, int width, int height) {
        return picUrl.replace("__", "_" + width + "x" + height);
    }
}
