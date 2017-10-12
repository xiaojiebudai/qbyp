
    package cn.net.chenbao.qbyp.adapter.listview;

    import android.content.Context;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;

    import cn.net.chenbao.qbyp.utils.TimeUtil;

    import cn.net.chenbao.qbyp.R;

    import cn.net.chenbao.qbyp.adapter.FatherAdapter;
    import cn.net.chenbao.qbyp.bean.AgencyInfo;
    import cn.net.chenbao.qbyp.utils.ImageUtils;

    public class ServiceProviderShopListAdapter extends FatherAdapter<AgencyInfo> {

        public ServiceProviderShopListAdapter(Context ctx) {
            super(ctx);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(
                        R.layout.listview_service_provider_shop_item , parent, false);
                viewHolder = new ViewHolder(convertView);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.setData(position);

            return convertView;
        }

        public class ViewHolder  {
            public TextView tv_seller_name;
            public TextView tv_seller_address;
            public TextView tv_seller_join_time;
            public ImageView iv_seller_shop_img;
            public AgencyInfo agencyInfo;
            public TextView tv_seller_auth_state;
            public TextView tv_seller_bussiness_state;

            public ViewHolder(View convertView) {
                tv_seller_name = (TextView) convertView
                        .findViewById(R.id.tv_seller_name);
                tv_seller_address = (TextView) convertView
                        .findViewById(R.id.tv_seller_address);
                tv_seller_join_time = (TextView) convertView
                        .findViewById(R.id.tv_seller_join_time);
                iv_seller_shop_img = (ImageView) convertView
                        .findViewById(R.id.iv_seller_shop_img);
                tv_seller_auth_state = (TextView) convertView
                        .findViewById(R.id.tv_seller_auth_state);
                tv_seller_bussiness_state = (TextView) convertView
                        .findViewById(R.id.tv_seller_bussiness_state);
                convertView.setTag(this);
            }

            public void setData(int Position) {
                agencyInfo = getItem(Position);
                tv_seller_name.setText(agencyInfo.SellerName);
                tv_seller_address.setText(agencyInfo.Address);
                tv_seller_join_time.setText(TimeUtil
                        .getOnlyDateToS(agencyInfo.CreateTime * 1000));
                ImageUtils.setCommonImage(mContext, agencyInfo.ShopPicture,
                        iv_seller_shop_img);
                setAuthStatus();
                setPayStatus();
            }



            public void setAuthStatus() {
                switch (agencyInfo.Status) {
                    case 0:
                        tv_seller_auth_state.setText(R.string.no_auth);
                        tv_seller_auth_state
                                .setBackgroundResource(R.drawable.no_auth_state_shape);
                        break;
                    case 1:
                        tv_seller_auth_state.setText(R.string.authed);
                        tv_seller_auth_state
                                .setBackgroundResource(R.drawable.authed_state_shape);
                        break;
                    case 2:
                        tv_seller_auth_state.setText(R.string.close_business);
                        tv_seller_auth_state
                                .setBackgroundResource(R.drawable.no_auth_state_shape);
                        break;
                    case 3:
                        tv_seller_auth_state.setText(R.string.freeze);
                        tv_seller_auth_state
                                .setBackgroundResource(R.drawable.no_auth_state_shape);
                        break;
                    case 5:
                        tv_seller_auth_state.setText(R.string.past_due);
                        tv_seller_auth_state
                                .setBackgroundResource(R.drawable.no_auth_state_shape);
                        break;
                    default:
                        break;
                }
            }

            private void setPayStatus() {
                switch (agencyInfo.ChargeType) {
                    case 0:
                        tv_seller_bussiness_state.setText(R.string.no_pay2);
                        tv_seller_bussiness_state.setBackgroundResource(R.color.black);
                        break;
                    case 1:
                        tv_seller_bussiness_state.setText(R.string.try_use);
                        tv_seller_bussiness_state
                                .setBackgroundResource(R.color.yellow_ww);
                        break;
                    case 2:
                        tv_seller_bussiness_state.setText(R.string.month_fee);
                        tv_seller_bussiness_state
                                .setBackgroundResource(R.color.yellow_ww);
                        break;
                    case 3:
                        tv_seller_bussiness_state.setText(R.string.year_fee);
                        tv_seller_bussiness_state
                                .setBackgroundResource(R.color.yellow_ww);
                        break;
                    default:
                        break;
                }
            }
        }
    }

