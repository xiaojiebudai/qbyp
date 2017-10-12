package cn.net.chenbao.qbyp.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.TimeUtil;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.view.RatingBar;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.api.ApiShop;
import cn.net.chenbao.qbyp.fragment.ImageSelectFragment;
import cn.net.chenbao.qbyp.utils.FileUtils;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 木头 on 2016/11/1.
 * 自营商城商品评价
 */

public class SelfSupportOrderEvaluateActivity extends FatherActivity {
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_good_img)
    ImageView ivGoodImg;
    @BindView(R.id.rb)
    RatingBar rb;
    @BindView(R.id.ed_introduce)
    EditText edIntroduce;
    @BindView(R.id.tv_introduce_remain)
    TextView tvIntroduceRemain;
    @BindView(R.id.cb_no_name_comm)
    CheckBox cbNoNameComm;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    private long orderId;
    private float star;
    private ImageSelectFragment imageFragment;
    /**
     * 图片
     */
    public static final String KEY_IMGURL = "imgUrl";
    /**
     * 时间
     */
    public static final String KEY_TIME = "time";


    @Override
    protected int getLayoutId() {
        return R.layout.act_self_support_orderevaluate;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.order_evaluate, true);
        orderId = getIntent().getLongExtra(Consts.KEY_DATA, -1);
    }

    @Override
    protected void initView() {
        imageFragment = new ImageSelectFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ImageSelectFragment.MAX_NUMBER, 5);
        imageFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.image_fragment_container, imageFragment).commit();
        tvTime.setText(getString(R.string.finish_time_with_colon) + TimeUtil.getTimeToS(getIntent().getLongExtra(KEY_TIME, 0) * 1000));
        ImageUtils.setCommonImage(this, getIntent().getStringExtra(KEY_IMGURL), ivGoodImg);
        rb.setOnRatingChangeListener(new RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float RatingCount) {
                star = RatingCount;
            }
        });
        rb.setStar(5);
        tvIntroduceRemain.setText(getString(R.string.can_input_tips, 80));
        edIntroduce.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                tvIntroduceRemain.setText(getString(R.string.can_input_tips, 50 - s.length()));
            }
        });
    }

    @Override
    protected void doOperate() {

    }

    private ArrayList<String> upImagesUrl = new ArrayList<String>();

    private void pingjiaCommit() {
        final ArrayList<String> selectImages = imageFragment.getSelectImages();
        //必须要先清除掉
        upImagesUrl.clear();
        if (selectImages != null && selectImages.size() > 0) {
            //有图片必须先上传
            for (int i = 0; i < selectImages.size(); i++) {
                showWaitDialog();
                RequestParams params = new RequestParams(ApiUser.getUpImg());
                final String newUrl = FileUtils.getCompressedImageFileUrl(selectImages.get(i));
                params.addBodyParameter("file", new File(newUrl));
                params.setMultipart(true);
                x.http().post(params, new WWXCallBack("UpImage") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        upImagesUrl.add(data.getString("Data"));
                        //数量一致表示上传完成了，需要确保图片上传不会失败
                        if (upImagesUrl.size() == selectImages.size()) {

                            dataCommit();
                        }
                    }

                    @Override
                    public void onAfterSuccessError(JSONObject data) {
                        super.onAfterSuccessError(data);
                    }

                    @Override
                    public void onError(Throwable arg0, boolean arg1) {
                        super.onError(arg0, arg1);
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });
            }
        } else {

            dataCommit();
        }
    }

    //真实提交数据
    private void dataCommit() {
        showWaitDialog();
        String comm = edIntroduce.getText().toString();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
                .getSessionId());
        jsonObject.put("orderId", orderId);
        jsonObject.put("judgeLevel", star == 0 ? 5 : star);
        if (TextUtils.isEmpty(comm)) {
            comm = "好评!";
        }
        jsonObject.put("content", comm);
        if (upImagesUrl.size() > 0) {
            for (int i = 0; i < upImagesUrl.size(); i++) {
                jsonObject.put("imageUrl" + (i + 1), upImagesUrl.get(i));
            }
        }
        jsonObject.put("isAnonymous", this.cbNoNameComm.isChecked());
        x.http()
                .post(ParamsUtils.getPostJsonParams(jsonObject,
                        ApiShop.OrderJudge()), new WWXCallBack("OrderJudge") {
                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        if(data.getBoolean("Data")){
                            WWToast.showShort(R.string.comm_success);
//                            Intent intent = new Intent(
//                                    SelfSupportMainActivity.ORDER_LIST_REFRESH);
//                            sendBroadcast(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();

                    }
                });
    }

    @OnClick(R.id.tv_commit)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_commit:
                pingjiaCommit();
                break;
        }
    }
}
