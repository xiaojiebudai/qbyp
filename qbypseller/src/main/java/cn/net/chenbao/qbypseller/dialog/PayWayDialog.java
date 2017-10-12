
package cn.net.chenbao.qbypseller.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.ApiPay;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.ImageUtils;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import cn.net.chenbao.qbypseller.R;

import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.bean.PayWay;

/**
 * 支付方式
 * Created by Administrator on 2017/2/13.
 */

public class PayWayDialog extends Dialog {
    private Context ctx;
    private OnSelectOKLisentner mLisentner;
    private boolean isShowJifen;
    private boolean isYue;

    public PayWayDialog(Context context, int theme, boolean isShowJifen, boolean isYue,
                        OnSelectOKLisentner lisentner) {
        super(context, theme);
        this.ctx = context;
        this.isShowJifen = isShowJifen;
        this.isYue = isYue;
        mLisentner = lisentner;
        setContentView(R.layout.dialog_payway);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.8);
        getWindow().setAttributes(attributes);
        initView();
    }

    private void initView() {
        requestPayWay();
    }


    private void requestPayWay() {
        RequestParams params = new RequestParams(ApiPay.PaymentValid());
        params.addBodyParameter(Api.KEY_SESSIONID, WWApplication.getInstance().getSessionId());
        params.addBodyParameter("busType", Consts.OFFLINE_COLLECTION_PAY_WAY + "");
        x.http().get(params, new WWXCallBack("PaymentValid") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                List<PayWay> payways = JSON.parseArray(
                        data.getJSONArray(Api.KEY_DATA).toJSONString(),
                        PayWay.class);
                LinearLayout ll_ways = (LinearLayout) findViewById(R.id.ll_pay_ways);
                if (payways != null) {
                    for (int i = 0; i < payways.size(); i++) {
                        final PayWay payWay = payways.get(i);
                        if ((!payWay.PayCode.equals(Consts.INTEGRAL_PAY) && !payWay.PayCode.equals(Consts.SEL_ACC)) ||
                                (payWay.PayCode.equals(Consts.INTEGRAL_PAY) && isShowJifen) ||
                                (payWay.PayCode.equals(Consts.SEL_ACC) && isYue)) {
                            View view = getLayoutInflater().inflate(
                                    R.layout.item_pay_way, null);
                            ImageUtils.setCommonImage(ctx,
                                    payWay.PayIco,
                                    (ImageView) view.findViewById(R.id.iv_image));
                            view.findViewById(R.id.iv_right).setVisibility(View.GONE);
                            ((TextView) view.findViewById(R.id.tv_content))
                                    .setText(payWay.PayName);
                            view.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    mLisentner.selectPayWay(payWay);
                                    dismiss();
                                }

                            });
                            ll_ways.addView(view);
                        }
                    }
                }
            }

            @Override
            public void onAfterFinished() {

            }
        });
    }

    public interface OnSelectOKLisentner {
        void selectPayWay(PayWay payWay);
    }
}
