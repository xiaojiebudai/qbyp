package cn.net.chenbao.qbyp.adapter.listview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.AddAddressActivity;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.Address;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;

/**
 * 收货地址adapter
 * 
 * @author licheng
 * 
 */
public class AddressAdapter extends FatherAdapter<Address> {

	public AddressAdapter(Context ctx) {
		super(ctx);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.listview_address_item, null);
			viewHolder = new ViewHolder(convertView);
		} else
			viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.setData(position);
		return convertView;
	}

	public class ViewHolder implements OnClickListener {
		public TextView tv_address;
		public TextView tv_phone;
		public TextView tv_name;
		public ImageView check;
		public TextView tv_nor_address;
		public TextView tv_edit;
		public TextView tv_delete;
		Address addressItem;
		int positionNow;

		public ViewHolder(View convertView) {
			tv_address = (TextView) convertView.findViewById(R.id.tv_address);
			tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
			tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			check = (ImageView) convertView.findViewById(R.id.check);
			tv_nor_address = (TextView) convertView
					.findViewById(R.id.tv_nor_address);
			tv_edit = (TextView) convertView.findViewById(R.id.tv_edit);
			tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
			convertView.setTag(this);
		}

		public void setData(int position) {
			positionNow = position;
			addressItem = getItem(position);
			tv_edit.setOnClickListener(this);
			tv_delete.setOnClickListener(this);
			
			tv_address.setText(addressItem.AddressPre+" "+addressItem.Address);
			tv_phone.setText(addressItem.Mobile);
			tv_name.setText(addressItem.Consignee);
			if(addressItem.IsDefault){
				check.setImageResource(R.drawable.cricle_small_selector);
			}else{
				check.setImageResource(R.drawable.cricle_small_nor);
			}
			check.setOnClickListener(this);
			tv_nor_address.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_edit:// 编辑
				PublicWay
						.startAddAddressActivity((Activity) mContext, 888,
								AddAddressActivity.EDIT,
								JSON.toJSONString(addressItem));
				break;

			case R.id.tv_delete:// 删除
				final CommonDialog commonDialog = DialogUtils
						.getCommonDialogTwiceConfirm((Activity) mContext,
								R.string.make_sure_delete_, true);
				commonDialog.setRightButtonCilck(new OnClickListener() {

					@Override
					public void onClick(View v) {
						del(addressItem, positionNow);
						commonDialog.dismiss();
					}
				});
				commonDialog.show();
				break;
			case R.id.check:
				if (!addressItem.IsDefault) {
					setDefault(addressItem, positionNow);
				}
			case R.id.tv_nor_address:
				if (!addressItem.IsDefault) {
					setDefault(addressItem, positionNow);
				}
				break;
			}
		}

		protected void del(Address addressItem2, final int positionNow2) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
					.getSessionId());
			jsonObject.put("addressId", addressItem2.AddressId);
			x.http().post(
					ParamsUtils.getPostJsonParams(jsonObject,
							ApiUser.getAddressDelete()),
					new WWXCallBack("AddressDelete") {

						@Override
						public void onAfterSuccessOk(JSONObject data) {
							getDatas().remove(positionNow2);
							notifyDataSetChanged();
						}

						@Override
						public void onAfterFinished() {
							// TODO Auto-generated method stub

						}
					});

		}

	}

	protected void setDefault(Address addressItem, final int positionNow2) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
				.getSessionId());
		jsonObject.put("addressId", addressItem.AddressId);
		x.http().post(
				ParamsUtils.getPostJsonParams(jsonObject,
						ApiUser.getDefaultAddressSet()),
				new WWXCallBack("DefaultAddressSet") {
					@Override
					public void onAfterSuccessOk(JSONObject data) {
						for (int i = 0; i < getDatas().size(); i++) {
							getDatas().get(i).IsDefault = positionNow2 == i;
						}
						notifyDataSetChanged();
					}

					@Override
					public void onAfterFinished() {
						// TODO Auto-generated method stub

					}
				});
	}

}
