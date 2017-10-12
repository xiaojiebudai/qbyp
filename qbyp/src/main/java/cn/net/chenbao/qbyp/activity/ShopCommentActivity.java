package cn.net.chenbao.qbyp.activity;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.api.ApiTrade;
import cn.net.chenbao.qbyp.bean.Order;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.view.RatingBar;
import cn.net.chenbao.qbyp.view.RatingBar.OnRatingChangeListener;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;

/**
 * 商户评价
 * 
 * @author licheng
 * 
 */
public class ShopCommentActivity extends FatherActivity implements
		OnClickListener, OnRatingChangeListener {
	private CheckBox comm;
	private RatingBar rb;
	private LinearLayout container;
	private EditText commDetail;
	private long orderId;
	private float star;
	private TextView shopName;
	private TextView goodsName;
	private TextView buyNum;
	private TextView price;
	private Order order;
	private ImageView goodsPic;
	private TextView headRight;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_shop_comment;
	}

	@Override
	protected void initValues() {
		initDefautHead(R.string.shop_comm, false);
		orderId = getIntent().getLongExtra("orderId", -1);
	}

	@Override
	protected void initView() {
		headRight = (TextView) findViewById(R.id.tv_head_right);
		shopName = (TextView) findViewById(R.id.tv_shop_name);
		goodsName = (TextView) findViewById(R.id.tv_what);
		goodsPic = (ImageView) findViewById(R.id.iv_goods);
		buyNum = (TextView) findViewById(R.id.tv_num);
		price = (TextView) findViewById(R.id.tv_price);
		comm = (CheckBox) findViewById(R.id.cb_no_name_comm);
		findViewById(R.id.rl_head_right).setOnClickListener(this);
		findViewById(R.id.tv_commit_comm).setOnClickListener(this);
		rb = (RatingBar) findViewById(R.id.rb);
		rb.setStar(5);
		container = (LinearLayout) findViewById(R.id.ll_goods_container);
		commDetail = (EditText) findViewById(R.id.ed_comm_detail);
		rb.setOnRatingChangeListener(this);
		LayoutParams params = headRight.getLayoutParams();
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.MATCH_PARENT;
		headRight.setLayoutParams(params);
		headRight.setTextColor(getResources().getColor(R.color.white));
		headRight.setText(R.string.jump);
		headRight.setGravity(Gravity.CENTER);
		headRight.setPadding(10, 0, 0, 0);
	}

	@Override
	protected void doOperate() {
		getOrderDetail();
	}

	/**
	 * 获取订单详情
	 */
	private void getOrderDetail() {
		showWaitDialog();
		RequestParams params = new RequestParams(ApiTrade.OrderOutline());
		params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
				.getInstance().getSessionId());
		params.addBodyParameter("orderId", orderId + "");
		x.http().get(params, new WWXCallBack("OrderOutline") {
			@Override
			public void onAfterSuccessOk(JSONObject data) {
				JSONObject jsonObject = data.getJSONObject("Data");
				order = JSON.parseObject(jsonObject.toJSONString(), Order.class);
				shopName.setText(order.SellerName);
				ImageUtils.setCommonImage(ShopCommentActivity.this,
						order.GoodsImg, goodsPic);
				buyNum.setText(order.Quantity+"");
				price.setText(WWViewUtil.numberFormatPrice(order.GoodsAmt));
				goodsName.setText(order.GoodsName);
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
		case R.id.tv_commit_comm:// 提交评价
			pingjiaCommit(false);
			break;

		case R.id.rl_head_right:// 跳過就5星評價
			pingjiaCommit(true);
			break;
		}
	}

	private void pingjiaCommit(final boolean isFive) {
		String comm = commDetail.getText().toString();
		showWaitDialog();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
				.getSessionId());
		jsonObject.put("orderId", orderId);
		jsonObject.put("judgeLevel", star == 0 ? 5 : star);
		if (!comm.equals("")) {
			jsonObject.put("content", comm);
		}
		jsonObject.put("isAnonymous", this.comm.isChecked());
		x.http()
				.post(ParamsUtils.getPostJsonParams(jsonObject,
						ApiTrade.OrderJudge()), new WWXCallBack("OrderJudge") {
					@Override
					public void onAfterSuccessOk(JSONObject data) {
						if(!isFive){
							WWToast.showShort(R.string.comm_success);
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
	public void onRatingChange(float RatingCount) {
		star =RatingCount;
	}
}
