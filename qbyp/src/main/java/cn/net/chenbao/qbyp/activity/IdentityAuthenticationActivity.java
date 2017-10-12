package cn.net.chenbao.qbyp.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.umeng.socialize.net.utils.Base64;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.UserReal;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.FileUtils;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.RegexUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/***
 * Description:身份认证 Company: wangwanglife Version：1.0
 *
 * @author ZXJ
 * @date @2016-7-27
 ***/
public class IdentityAuthenticationActivity extends FatherActivity implements
        OnClickListener {
    private ImageView iv_front, iv_back;
    private EditText ed_cardname, ed_cardnumder, idcard_data, et_qianfajigou;
    private String frontStr, backStr;
    private UserReal userReal;
    private static final int SELECTPHOTPO_FRONT = 999;
    private static final int SELECTPHOTPO_BACK = 888;
    private int model;
    /**
     * NORMAL
     */
    public static final int NORMAL = 0;
    /**
     * REPLAY
     */
    public static final int REPLAY = 1;

    @Override
    protected int getLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.act_identity;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.idcard_check, true);
    }

    @Override
    protected void initView() {
        iv_front = (ImageView) findViewById(R.id.iv_front);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ed_cardname = (EditText) findViewById(R.id.ed_cardname);
        ed_cardnumder = (EditText) findViewById(R.id.ed_cardnumder);
        idcard_data = (EditText) findViewById(R.id.idcard_data);
        et_qianfajigou = (EditText) findViewById(R.id.et_qianfajigou);
        findViewById(R.id.tv_back_card_choose).setOnClickListener(this);
        findViewById(R.id.tv_back_card).setOnClickListener(this);
        findViewById(R.id.tv_front_card).setOnClickListener(this);
        findViewById(R.id.tv_front_card_choose).setOnClickListener(this);
        findViewById(R.id.commit).setOnClickListener(this);
        userReal = new UserReal();
        getDataInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back_card_choose:
                PublicWay.startImageSelectActivity(this, SELECTPHOTPO_BACK, true,false, true,
                        ImageSelectActivity.MODE_PHOTO_ALBUM);
                break;
            case R.id.tv_back_card:
                PublicWay.startImageSelectActivity(this, SELECTPHOTPO_BACK, true,false, true,
                        ImageSelectActivity.MODE_TAKE_PICTURE);
                break;
            case R.id.tv_front_card:
                PublicWay.startImageSelectActivity(this, SELECTPHOTPO_FRONT, true,false, true,
                        ImageSelectActivity.MODE_TAKE_PICTURE);
                break;
            case R.id.tv_front_card_choose:
                PublicWay.startImageSelectActivity(this, SELECTPHOTPO_FRONT, true,false, true,
                        ImageSelectActivity.MODE_PHOTO_ALBUM);
                break;
            case R.id.commit:
                commit();
                break;

            default:
                break;
        }

    }

    /**
     * 获取信息
     */
    private UserReal user;

    private void getDataInfo() {
        RequestParams params = new RequestParams(ApiUser.RealGet());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        showWaitDialog();
        x.http().get(params, new WWXCallBack("RealGet") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                user = JSON.parseObject(data.getString("Data"), UserReal.class);
                if (user != null) {
                    if (!TextUtils.isEmpty(user.FrontPicture)) {//未认证状态 不加载图片 否则会出现默认底图
                        ImageUtils.setCommonImage(IdentityAuthenticationActivity.this, getRightImgScreen(user.FrontPicture, DensityUtil.dip2px(IdentityAuthenticationActivity.this, 180), DensityUtil.dip2px(IdentityAuthenticationActivity.this, 120)), iv_front);
                        ImageUtils.setCommonImage(IdentityAuthenticationActivity.this, getRightImgScreen(user.BackPicture, DensityUtil.dip2px(IdentityAuthenticationActivity.this, 180), DensityUtil.dip2px(IdentityAuthenticationActivity.this, 120)), iv_back);
                    }
                    backStr = user.BackPicture;
                    frontStr = user.FrontPicture;
                    ed_cardname.setText(user.CredName);
                    ed_cardnumder.setText(user.CredNo);
                    idcard_data.setText(user.ValidityTime);
                    et_qianfajigou.setText(user.IssuingAuthority);
                }
            }

            @Override
            public void onAfterSuccessError(JSONObject data) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAfterFinished() {
                // TODO Auto-generated method stub
                dismissWaitDialog();
            }
        });
    }

    private void commit() {
        String cardname = ed_cardname.getText().toString().trim();
        String number = ed_cardnumder.getText().toString().trim();
        String time = idcard_data.getText().toString().trim();
        String qianfajigou = et_qianfajigou.getText().toString().trim();

        if (TextUtils.isEmpty(frontStr)) {
            WWToast.showShort(R.string.identity_front_img_upload_tips);
            return;
        }
        if (TextUtils.isEmpty(backStr)) {
            WWToast.showShort(R.string.identity_back_img_upload_tips);
            return;
        }
        if (TextUtils.isEmpty(cardname)) {
            WWToast.showShort(R.string.identity_cardname_input);
            return;
        }
        if(RegexUtil.checkHasSpecial(cardname)){
            WWToast.showShort(R.string.name_cannot_contain_special_symbol);
            return;
        }
        if(RegexUtil.checkHasNum(cardname)){
            WWToast.showShort(R.string.name_cannot_contain_number);
            return;
        }
        if (TextUtils.isEmpty(number)) {
            WWToast.showShort(R.string.identity_number_input);
            return;
        }
        if(RegexUtil.checkHasSpecial(number)){
            WWToast.showShort(R.string.id_card_cannot_contain_special_symbol);
            return;
        }
        if (TextUtils.isEmpty(qianfajigou)) {
            WWToast.showShort(R.string.identity_qianfajigou_input);
            return;
        }
        if (TextUtils.isEmpty(time)) {
            WWToast.showShort(R.string.identity_time_input);
            return;
        }
        userReal.BackPicture = backStr;
        userReal.CredName = cardname;
        userReal.CredNo = number;
        userReal.ValidityTime = time;
        userReal.IssuingAuthority = qianfajigou;
        userReal.FrontPicture = frontStr;
        userReal.CredType = "身份证";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        jsonObject.put("data", userReal.toJson());
        showWaitDialog();
        x.http().post(
                ParamsUtils.getPostJsonParams(jsonObject,
                        ApiUser.getRealSubmit()),
                new WWXCallBack("RealSubmit") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        WWToast.showShort(R.string.set_succuss);
                        PublicWay.startIdentityAuthenticationResultActivity(
                                IdentityAuthenticationActivity.this,
                                IdentityAuthenticationResultActivity.IDENTITYING,
                                "");
                        finish();
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || data != null) {
            switch (requestCode) {
                case SELECTPHOTPO_BACK:
                    upImg(data.getStringExtra(Consts.KEY_DATA), SELECTPHOTPO_BACK);
                    upLocalImg(data.getStringExtra(Consts.KEY_DATA), SELECTPHOTPO_BACK);

                    break;
                case SELECTPHOTPO_FRONT:
                    upImg(data.getStringExtra(Consts.KEY_DATA), SELECTPHOTPO_FRONT);
                    upLocalImg(data.getStringExtra(Consts.KEY_DATA), SELECTPHOTPO_FRONT);

                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 上传图片检测
     */
    private void upImg(final String localUrl, final int selectphotpo) {

        String newUrl1 = FileUtils.getCompressedImageFileUrl1(localUrl,3);

        // 对图像进行base64编码
        String imgBase64 = "";
        try {
            File file = new File(newUrl1);
            byte[] content = new byte[(int) file.length()];
            FileInputStream finputstream = new FileInputStream(file);
            finputstream.read(content);
            finputstream.close();
            imgBase64 = new String(Base64.encodeBase64(content, false));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // 拼装请求body的json字符串
        JSONObject requestObj = new JSONObject();
        try {
            JSONObject configObj = new JSONObject();
            JSONObject obj = new JSONObject();
            JSONArray inputArray = new JSONArray();
            configObj.put("side", (selectphotpo == SELECTPHOTPO_FRONT) ? "face" : "back");
            obj.put("image", getParam(50, imgBase64));
            obj.put("configure", getParam(50, configObj.toString()));
            inputArray.add(obj);
            requestObj.put("inputs", inputArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showWaitDialog();
        RequestParams requestParams = ParamsUtils.getPostJsonParams(requestObj, "https://dm-51.data.aliyun.com/rest/160601/ocr/ocr_idcard.json");
        requestParams.addHeader("Authorization", "APPCODE 3b2c4943ea3c4a95b32d7c5c4be38f3c");
        x.http().post(requestParams, new CommonCallback<String>() {

            @Override
            public void onCancelled(CancelledException arg0) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                WWToast.showShort(R.string.img_upload_fail);
            }

            @Override
            public void onFinished() {
                dismissWaitDialog();
            }

            @Override
            public void onSuccess(String arg0) {
                // 解析请求结果
                try {
                    JSONObject resultObj = JSONObject.parseObject(arg0);
                    JSONArray outputArray = resultObj.getJSONArray("outputs");
                    String output = outputArray.getJSONObject(0).getJSONObject("outputValue").getString("dataValue"); // 取出结果json字符串
                    JSONObject out = JSONObject.parseObject(output);
                    if (out.getBoolean("success")) {

                        if (selectphotpo == SELECTPHOTPO_FRONT) {
                            String addr = out.getString("address"); // 获取地址
                            String name = out.getString("name"); // 获取名字
                            String num = out.getString("num"); // 获取身份证号
                            ed_cardname.setText(name);
                            ed_cardnumder.setText(num);
                        } else {
                            String start_date = out.getString("start_date"); // 有效期起始时间
                            String end_date = out.getString("end_date"); //有效期结束时间
                            String issue = out.getString("issue"); // 签发机关
                            et_qianfajigou.setText(issue);
                            idcard_data.setText(start_date + "-" + end_date);
                        }
                    } else {
                        WWToast.showShort(R.string.img_error);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        });


    }

    /*
    * 获取参数的json对象
    */
    public static JSONObject getParam(int type, JSONObject dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    /*
     * 获取参数的json对象
     */
    public static JSONObject getParam(int type, String dataValue) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("dataType", type);
            obj.put("dataValue", dataValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    protected void upLocalImg(final String localUrl, final int selectphotpo) {
        RequestParams params = new RequestParams(ApiUser.getUpImg());
        final String newUrl = FileUtils.getCompressedImageFileUrl1(localUrl,4);
        params.addBodyParameter("file", new File(newUrl));
        params.setMultipart(true);
        showWaitDialog();
        x.http().post(params, new WWXCallBack("UpImage") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                if (selectphotpo == SELECTPHOTPO_FRONT) {
                    frontStr = data.getString("Data");
                    iv_front.setImageBitmap(FileUtils.getCompressedImageBitmap(
                            localUrl, 0, 0));
                } else {
                    backStr = data.getString("Data");
                    iv_back.setImageBitmap(FileUtils.getCompressedImageBitmap(
                            localUrl, 0, 0));
                }
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                WWToast.showShort(R.string.img_upload_fail);
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });
    }

    @Override
    protected void doOperate() {

    }

    private String getRightImgScreen(String picUrl, int width, int height) {
        return picUrl.replace("__", "_" + width + "x" + height);
    }
}
