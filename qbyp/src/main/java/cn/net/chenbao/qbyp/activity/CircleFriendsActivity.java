package cn.net.chenbao.qbyp.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.FriendsCircleAdapter;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.User;
import cn.net.chenbao.qbyp.dialog.InputDialog;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.ParamsUtils;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.view.QrCodeDialog;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/***
 * Description:朋友圈 Company: wangwanglife Version：1.0
 * 
 * @author ZXJ
 * @date @2016-7-27
 ***/
public class CircleFriendsActivity extends FatherActivity implements
		OnClickListener, OnRefreshListener<ListView> {
	private TextView tv_num, my_inviter,tv_setting;
	private LinearLayout ll_inviterinfo;
	private PullToRefreshListView mPullToRefreshListView;
	private FriendsCircleAdapter friendsCircleAdapter;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_erweima:
			if (user==null) {
				WWToast.showShort(R.string.date_no_get);
				return;
			}
			QrCodeDialog codeDialog = new QrCodeDialog(
					CircleFriendsActivity.this, R.style.DialogStyle);
			codeDialog.setImg(user.BarcodeUrl);
			codeDialog.show();
			break;
		case R.id.tv_setting:
			final InputDialog dialog=new InputDialog(CircleFriendsActivity.this, R.style.DialogStyle);
			dialog.getTv_cancel().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.getTv_ok().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(!TextUtils.isEmpty(dialog.getInput())){
						showWaitDialog();
						JSONObject object=new JSONObject();
						object.put("inviterNo", dialog.getInput());
						object.put(Consts.KEY_SESSIONID, WWApplication
								.getInstance().getSessionId());
						RequestParams	params= ParamsUtils.getPostJsonParams(object,ApiUser.ChangeInviter());
						
						x.http().post(params, new WWXCallBack("ChangeInviter") {
							@Override
							public void onAfterSuccessOk(JSONObject data) {
								ll_inviterinfo.setVisibility(View.VISIBLE);
								tv_setting.setVisibility(View.GONE);
								my_inviter.setText(data.getString("Data"));
								dialog.dismiss();
								
							}
							
							@Override
							public void onAfterFinished() {
								// TODO Auto-generated method stub
								dismissWaitDialog();
							}
						});
					}else{
						WWToast.showShort(R.string.input_recommend_code);
					}
					
				}
			});
			dialog.show();
			break;

		default:
			break;
		}

	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.act_frinds_circle;
	}

	@Override
	protected void initValues() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initView() {
		initDefautHead(R.string.frinds_circle, true);
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listview_data);
		View headerview_info = View.inflate(this,
				R.layout.circlefriends_header, null);
		tv_num = (TextView) headerview_info.findViewById(R.id.tv_num);
		my_inviter = (TextView) headerview_info.findViewById(R.id.my_inviter);
		tv_setting = (TextView)headerview_info. findViewById(R.id.tv_setting);
		ll_inviterinfo = (LinearLayout)headerview_info. findViewById(R.id.ll_inviterinfo);
		tv_setting.setOnClickListener(this);
		headerview_info.findViewById(R.id.ll_erweima).setOnClickListener(this);
		mPullToRefreshListView.getRefreshableView().addHeaderView(
				headerview_info);
		mPullToRefreshListView.setOnRefreshListener(this);
		// 设置 end-of-list监听
		mPullToRefreshListView
				.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
					@Override
					public void onLastItemVisible() {
						if (mCurrentPage >= totalCount) {
							WWToast.showShort(R.string.nomore_data);
						} else {
							getInfo();
						}

					}
				});
		friendsCircleAdapter = new FriendsCircleAdapter(
				CircleFriendsActivity.this);
		mPullToRefreshListView.setAdapter(friendsCircleAdapter);
	}

	@Override
	protected void doOperate() {
		getInfo();
		getQrInfo();
	}

	private User user;

	/**
	 * 获取二维码信息
	 */
	private void getQrInfo() {
		showWaitDialog();
		RequestParams params = new RequestParams(ApiUser.getInviterGet());
		params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
				.getInstance().getSessionId());
		x.http().get(params, new WWXCallBack("InviterGet") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				user = JSON.parseObject(data.getString("Data"), User.class);
			}

			@Override
			public void onAfterSuccessError(JSONObject data) {
			}

			@Override
			public void onAfterFinished() {
				dismissWaitDialog();

			}
		});
	}

	// 分页数据

	// 分页数据
	private int mCurrentPage = 0;
	private int totalCount = 0;
	private ArrayList<User> list;

	private void getInfo() {
		showWaitDialog();
		RequestParams params = new RequestParams(ApiUser.getTeamsGet());
		params.addBodyParameter("sessionId", WWApplication.getInstance()
				.getSessionId());
		params.addBodyParameter("pageSize", 20 + "");
		params.addBodyParameter("pageIndex", mCurrentPage + "");

		x.http().get(params, new WWXCallBack("TeamsGet") {

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				mCurrentPage++;
				list = (ArrayList<User>) JSONArray.parseArray(
						data.getString("Data"), User.class);
				totalCount = data.getIntValue("PageCount");
				if (list != null && list.size() > 0) {
					if (mCurrentPage > 1) {
						friendsCircleAdapter.addDatas(list);
					} else {
						friendsCircleAdapter.setDatas(list);
					}
					
				}
				tv_num.setText(data.getString("TotalCount"));
				if(TextUtils.isEmpty(data.getString("Message"))){
					ll_inviterinfo.setVisibility(View.GONE);
					tv_setting.setVisibility(View.VISIBLE);
				}else{
					ll_inviterinfo.setVisibility(View.VISIBLE);
					tv_setting.setVisibility(View.GONE);
					my_inviter.setText(data.getString("Message"));
				}
			}

			@Override
			public void onAfterSuccessError(JSONObject data) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAfterFinished() {
				dismissWaitDialog();
				mPullToRefreshListView.onRefreshComplete();
			}
		});

	}

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		mCurrentPage = 0;
		getInfo();
	}
}
