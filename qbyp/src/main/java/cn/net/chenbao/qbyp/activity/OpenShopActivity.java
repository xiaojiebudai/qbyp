

	package cn.net.chenbao.qbyp.activity;

	import cn.net.chenbao.qbyp.api.Api;

	import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

    import cn.net.chenbao.qbyp.R;

	
	/***
	 * Description:我要开店 Company: wangwanglife Version：1.0
	 * @author ZXJ
	 * @date @2016-7-27
	 ***/
	public class OpenShopActivity extends FatherActivity implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.shop_download:
				
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(Api.API_DOWNLOAD_APP));
				startActivity(browserIntent);
				break;
			case R.id.person_download:
			 Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri
			 .parse(Api.API_DOWNLOAD_APP));
			 startActivity(browserIntent1);
				break;

			default:
				break;
			}
		}

		@Override
		protected int getLayoutId() {
			// TODO Auto-generated method stub
			return R.layout.act_open_shop;
		}

		@Override
		protected void initValues() {
			// TODO Auto-generated method stub

		}

		@Override
		protected void initView() {
			findViewById(R.id.person_download).setOnClickListener(this);
			findViewById(R.id.shop_download).setOnClickListener(this);

		}

		@Override
		protected void doOperate() {
			// TODO Auto-generated method stub

		}

	}
