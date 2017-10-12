package cn.net.chenbao.qbypseller.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.adapter.listview.RegionPickAdapter;
import cn.net.chenbao.qbypseller.api.ApiBaseData;
import cn.net.chenbao.qbypseller.bean.REGIONS;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/***
 * Description:地址选择 Company: wangwanglife Version：1.0
 * 
 * @author zxj
 * @date 2016-8-7
 */
public class RegionPickActivity extends FatherActivity {

	private TextView title;
	private ListView listView;
	private RegionPickAdapter spinnerAdapter;
	private int i = 1;
	private String province_id = "";
	private String city_id = "";
	private String county_id = "";

	private String province_name = "";
	private String city_name = "";
	private String county_name = "";

	private ImageView back, quxiao;
	public ArrayList<REGIONS> regionsList0 = new ArrayList<REGIONS>();

	protected void region(int j) {
		showWaitDialog();
		RequestParams params = new RequestParams(ApiBaseData.getRegionsGet());
		if (j != 1) {
			params.addBodyParameter("parentId", j + "");
		}
		x.http().get(params, new WWXCallBack("RegionsGet") {

			@Override
			public void onAfterFinished() {
				dismissWaitDialog();
			}

			@Override
			public void onAfterSuccessOk(JSONObject data) {
				regionsList0 = (ArrayList<REGIONS>) JSON.parseArray(
						data.getString("Data"), REGIONS.class);
				setCountry();
			}
		});
	}

	public void setCountry() {
		if (i == 1) {
			back.setVisibility(View.GONE);
		}
		if (regionsList0.size() == 0 || i == 4) {
			Intent intent = new Intent();
			intent.putExtra("province_id", province_id);
			intent.putExtra("city_id", city_id);
			intent.putExtra("county_id", county_id);
			intent.putExtra("province_name", province_name);
			intent.putExtra("city_name", city_name);
			intent.putExtra("county_name", county_name);
			setResult(Activity.RESULT_OK, intent);
			finish();
		}
		i++;
		if (i == 2) {
			title.setText(R.string.choice_address);
		} else if (i == 3) {
			title.setText(province_name);
		} else if (i == 4) {
			title.setText(province_name + city_name);
		}

		spinnerAdapter = new RegionPickAdapter(this, regionsList0);
		listView.setAdapter(spinnerAdapter);
	}


	@Override
	protected int getLayoutId() {
		return R.layout.f3_region_pick;
	}

	@Override
	protected void initValues() {

	}

	@Override
	protected void initView() {
		title = (TextView) findViewById(R.id.address_title);
		listView = (ListView) findViewById(R.id.address_list);
		title.setText(R.string.choice_address);
		back = (ImageView) findViewById(R.id.back);
		quxiao = (ImageView) findViewById(R.id.quxiao);
		quxiao.setVisibility(View.VISIBLE);
		quxiao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				RegionPickActivity.this.finish();
			}
		});
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				i = i - 2;
				if (i == 1) {
					back.setVisibility(View.GONE);
					region(1);
				} else if (i == 2) {
					region(Integer.parseInt(province_id));
				} else if (i == 3) {
					region(Integer.parseInt(city_id));
				}
			}
		});

		region(1);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				// TODO Auto-generated method stub
				if (i == 2) {
					back.setVisibility(View.VISIBLE);
					back.setVisibility(View.VISIBLE);
					province_id = regionsList0.get(position).Id;
					province_name = regionsList0.get(position).Name;
				} else if (i == 3) {
					city_id = regionsList0.get(position).Id;
					city_name = regionsList0.get(position).Name;
				} else if (i == 4) {
					county_id = regionsList0.get(position).Id;
					county_name = regionsList0.get(position).Name;
				}

				region(Integer.parseInt(regionsList0.get(position).Id));
			}
		});
	}

	@Override
	protected void doOperate() {

	}

	@Override
	protected void setStatusBar() {

	}
}
