
package cn.net.chenbao.qbyp.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.bean.PayWay;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.api.ApiPay;
import cn.net.chenbao.qbyp.utils.ZLog;

/**
 * 支付方式
 * Created by Administrator on 2017/2/13.
 */

public class PayWayDialog extends Dialog {
    private Context ctx;
    private int model;
    /**
     * 本地生活
     */
    public final static int LOCAL = 1;
    /**
     * 自营
     */
    public final static int SELF = 2;
    /**
     * 分销
     */
    public final static int DISTRIBUTION = 3;
    /**
     * 申请服务商
     */
    public final static int SERVICE_APPLY = 4;
    private OnSelectOKLisentner mLisentner;

    public PayWayDialog(Context context, int model, int theme,
                        OnSelectOKLisentner lisentner) {
        super(context, theme);
        this.ctx = context;
        this.model = model;
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
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication.getInstance().getSessionId());
        switch (model) {
            case LOCAL:
                params.addBodyParameter("busType", Consts.ORDER_PAY_WAY + "");
                break;
            case SELF:
                params.addBodyParameter("busType", Consts.SELF_ORDER_PAY_WAY + "");
                break;
            case DISTRIBUTION:
                params.addBodyParameter("busType", Consts.FENXIAO_PAY_WAY + "");
                break;
            case SERVICE_APPLY:
                params.addBodyParameter("busType", Consts.SERVICE_UPDATE_PAY_WAY + "");
                break;
            default:
                break;
        }
        x.http().get(params, new WWXCallBack("PaymentValid") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {

                List<PayWay> payways = JSON.parseArray(
                        data.getJSONArray("Data").toJSONString(),
                        PayWay.class);
                LinearLayout ll_ways = (LinearLayout) findViewById(R.id.ll_pay_ways);
                if (payways != null) {
                    for (int i = 0; i < payways.size(); i++) {
                        ZLog.showPost(payways.toString());
                        final PayWay payWay = payways.get(i);
                        View view = getLayoutInflater().inflate(
                                R.layout.item_pay_way, null);
                        ImageUtils.setCommonImage(ctx,
                                payWay.PayIco,
                                (ImageView) view.findViewById(R.id.iv_image));
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

            @Override
            public void onAfterFinished() {

            }
        });
    }

    public interface OnSelectOKLisentner {
        void selectPayWay(PayWay payWay);
    }
}
