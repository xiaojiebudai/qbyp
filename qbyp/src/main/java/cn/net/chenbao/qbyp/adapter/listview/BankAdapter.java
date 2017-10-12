package cn.net.chenbao.qbyp.adapter.listview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.Bank;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DialogUtils;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.view.CommonDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;

/***
 * Description:银行卡 Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-8-4
 */
public class BankAdapter extends FatherAdapter<Bank> {

	public BankAdapter(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.list_bank_item, null);
			viewHolder = new ViewHolder(convertView);
		} else
			viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.setData(position);
		return convertView;
	}

	public class ViewHolder implements OnClickListener {
		public TextView tv_bank_name;
		public TextView tv_card_type;
		public TextView tv_card;
		public TextView tv_del;
		public ImageView iv_logo;
		Bank item;
		int positionNow;

		public ViewHolder(View convertView) {
			tv_bank_name = (TextView) convertView
					.findViewById(R.id.tv_bank_name);
			tv_card_type = (TextView) convertView
					.findViewById(R.id.tv_card_type);
			tv_card = (TextView) convertView.findViewById(R.id.tv_card);
			tv_del = (TextView) convertView.findViewById(R.id.tv_del);
			iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
			tv_del.setOnClickListener(this);
			convertView.setTag(this);
		}

		public void setData(int Position) {
			positionNow = Position;
			item = getItem(Position);
			ImageUtils.setCommonImage(mContext, item.BankIco, iv_logo);
			tv_bank_name.setText(item.BankName);
			if(item.CardType==0){
				tv_card_type.setText(R.string.deposit_card);
			}else{
				tv_card_type.setText(R.string.credit_card);
			}
			tv_card.setText(item.AccountNo);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_del:
				final CommonDialog commonDialog= DialogUtils.getCommonDialog((Activity)mContext, R.string.make_sure_delete);
				commonDialog.getButtonLeft(R.string.cancel).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						commonDialog.dismiss();
					}
				});
				commonDialog.getButtonRight(R.string.ok).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						del();
						commonDialog.dismiss();
					}
				});
				commonDialog.show();
				
				
				break;

			default:
				break;
			}

		}
		public void del() {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(Consts.KEY_SESSIONID, WWApplication.getInstance()
					.getSessionId());
			jsonObject.put("bankId", item.FlowId);
			x.http().post(
					ParamsUtils.getPostJsonParams(jsonObject,
							ApiUser.getBankDelete()),
					new WWXCallBack("BankDelete") {

						@Override
						public void onAfterSuccessOk(JSONObject data) {
							if(data.getBooleanValue("Data")){
								getDatas().remove(positionNow);
								notifyDataSetChanged();	
							}
						}

						@Override
						public void onAfterFinished() {

						}
					});
		}
	}



}
