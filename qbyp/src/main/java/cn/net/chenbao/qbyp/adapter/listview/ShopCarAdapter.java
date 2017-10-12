package cn.net.chenbao.qbyp.adapter.listview;

import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.api.ApiTrade;
import cn.net.chenbao.qbyp.bean.Goods;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.utils.ZLog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
/**
 * 购物车adapter
 * 
 * @author licheng
 * 
 */
public class ShopCarAdapter extends FatherAdapter<Goods> {

	public ShopCarAdapter(Context ctx) {
		super(ctx);
	}

	private AddAndSubCallBack goodsPriceListener;

	public AddAndSubCallBack getGoodsPriceListener() {
		return goodsPriceListener;
	}

	public void setGoodsPriceListener(AddAndSubCallBack goodsPriceListener) {
		this.goodsPriceListener = goodsPriceListener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.listview_item_shop_car, null);
			viewHolder = new ViewHolder(convertView);
		} else
			viewHolder = (ViewHolder) convertView.getTag();
		final Goods item = getItem(position);
		viewHolder.tv_name.setText(item.GoodsName);
		viewHolder.tv_money.setText(WWViewUtil.numberFormatPrice(item.Price));
		viewHolder.tv_count.setText(item.Quantity + "");
		viewHolder.ll_jia.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (item.Quantity >= item.StockQty) {
					WWToast.showShort(R.string.buynum_dayu_kucun);
					return;
				}
				addOrSubCart(true, item, viewHolder);
			}
		});
		viewHolder.ll_jian.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				if (item.Quantity == 1) {
//					addOrSubCart(false, item, viewHolder);
//					removeData(item);
//					goodsPriceListener.subGoodsListener(item.Price, item);
//				} else {
					addOrSubCart(false, item, viewHolder);
//				}
			}
		});
		return convertView;
	}

	public class ViewHolder {

		public TextView tv_name;
		public TextView tv_money;
		public TextView tv_count;
		public View ll_jian;
		public View ll_jia;

		public ViewHolder(View convertView) {
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			tv_count = (TextView) convertView.findViewById(R.id.tv_count);
			ll_jian = convertView.findViewById(R.id.ll_jian);
			ll_jia = convertView.findViewById(R.id.ll_jia);
			convertView.setTag(this);
		}

	}

	public void addOrSubCart(final boolean isAdd, final Goods goods,
			final ViewHolder holder) {
		String url = "";
		String result = null;
		if (isAdd) {
			url = ApiTrade.addCart();
			result = "CartAdd";
		} else {
			url = ApiTrade.subCart();
			result = "CartSub";
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("sessionId", WWApplication.getInstance().getSessionId());
		jsonObject.put("goodsId", goods.GoodsId);
		ZLog.showPost(jsonObject.toJSONString());
		x.http().post(ParamsUtils.getPostJsonParams(jsonObject, url),
				new WWXCallBack(result) {

					@Override
					public void onAfterSuccessOk(JSONObject data) {
						if (isAdd) {
							goods.Quantity++;
							goodsPriceListener.addGoodsListener(goods.Price,
									goods);
							WWToast.showShort(R.string.add_cart_success);
							holder.tv_count.setText(goods.Quantity + "");
						} else {
							if (goods.Quantity == 1) {
								removeItem(goods);

							}
								goods.Quantity--;
								goodsPriceListener.subGoodsListener(
										goods.Price, goods);
							if (goods.Quantity != 0) {
								holder.tv_count.setText(goods.Quantity + "");
							}
							WWToast.showShort(R.string.remove_cart_success);
						}
					}

					@Override
					public void onAfterFinished() {

					}
				});

	}

	public interface AddAndSubCallBack {
		void addGoodsListener(double price, Goods goods);

		void subGoodsListener(double price, Goods goods);
	}
}
