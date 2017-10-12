
package cn.net.chenbao.qbyp.distribution.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.fragment.FatherFragment;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.FileUtils;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.view.ChoosePhotoPop;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;


    /**
     * 图片选择的fragment
     *
     * @author xl
     * @description 使用时注意width, fragment的设计就是屏幕的使用情况, 父容器的padding也不能设置
     */
    public class ImageSelecWithUploadtFragment extends FatherFragment {

        public static final String MAX_NUMBER = "max_number";
        private SelectImagesGirdAdapter mAdapter;
        /**
         * GridView numCount
         */
        private int GRIDVIEW_NUMCOUNT = 3;
        /**
         * 最多选5张
         */
        private int MAX_NUM = 5;
        /**
         * 图片路径
         */
        private ArrayList<UpImgItem> mSelectPath;

        private ChoosePhotoPop choosePhotoPop;
        private static final int RESULT_CODE_SELECTPHOTPO = 999;
        private GridView images;

        public static final String KEY_EXISTING = "existing";
        /**
         * 左边的margin值，根据需要给
         */
        public static final String OUTMARGIN = "outMargin";
        private int outMargin;

        @Override
        protected int getLayoutId() {
            return R.layout.fragment_image_select;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Bundle arguments = getArguments();
            if (arguments != null) {
                MAX_NUM = arguments.getInt(MAX_NUMBER);
                ArrayList<String> listImg = (ArrayList<String>) JSONArray.parseArray(getArguments().getString(KEY_EXISTING), String.class);
                if (listImg != null) {
                    if (mSelectPath == null) {
                        mSelectPath = new ArrayList<UpImgItem>();
                    }
                    for (int i = 0; i < listImg.size(); i++) {
                        UpImgItem item = new UpImgItem();
                        item.url = listImg.get(i);
                        item.isUp = false;
                        mSelectPath.add(item);
                    }
                }

                outMargin = arguments.getInt(OUTMARGIN);
            }
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            mAdapter = new SelectImagesGirdAdapter(getActivity());
            images = (GridView) mGroup.findViewById(R.id.gv_select_images);

            int gridWidth = DensityUtil.getScreenWidth(getActivity())
                    - 2
                    * getResources().getDimensionPixelOffset(
                    R.dimen.refund_images_margin2)
                    - DensityUtil.dip2px(getActivity(), outMargin);
            int columnSpace = getResources().getDimensionPixelOffset(
                    R.dimen.refund_images_space_size2);
            int size = (gridWidth - columnSpace * (GRIDVIEW_NUMCOUNT - 1))
                    / GRIDVIEW_NUMCOUNT;

            mAdapter.setItemSize(size);
            images.setAdapter(mAdapter);
            if (mSelectPath != null && mSelectPath.size() > 0) {
                mAdapter.setDatas(mSelectPath);
            }
        }

        @Override
        protected void initView() {

        }

        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == RESULT_CODE_SELECTPHOTPO) {
                    String imgPath = data.getStringExtra(Consts.KEY_DATA);
                    if (mSelectPath == null) {
                        mSelectPath = new ArrayList<UpImgItem>();
                    }

                    UpImgItem item = new UpImgItem();
                    item.isUp = true;
                    item.urlLocal = imgPath;
                    mSelectPath.add(item);

                    upImg(imgPath, mSelectPath.size());
                    mAdapter.setDatas(mSelectPath);
                }
            }
        }

        public ArrayList<UpImgItem> getSelectImages() {
            return mSelectPath;
        }

        /**
         * 上传图片到服务器
         */
        private void upImg(String localUrl, final int size) {
            mAdapter.setCanClick(false);
            RequestParams params = new RequestParams(ApiUser.getUpImg());
            final String newUrl = FileUtils.getCompressedImageFileUrl1(localUrl,3);
            params.addBodyParameter("file", new File(newUrl));
            params.setMultipart(true);
            x.http().post(params, new WWXCallBack("UpImage") {

                @Override
                public void onAfterSuccessOk(JSONObject data) {
                    WWToast.showShort("凭证上传成功");
                    mSelectPath.get(size - 1).isUp = false;
                    mSelectPath.get(size - 1).url = data.getString("Data");
                    mAdapter.setDatas(mSelectPath);

                }

                @Override
                public void onError(Throwable arg0, boolean arg1) {
                    WWToast.showShort("图片上传失败");
                    mSelectPath.remove(size - 1);
                    mAdapter.setDatas(mSelectPath);
                }

                @Override
                public void onAfterFinished() {
                    mAdapter.setCanClick(true);
                }
            });
        }

        public class UpImgItem {

            public String url;
            public String urlLocal;
            public boolean isUp;

        }


        /**
         * 选择的图片的GridView适配器
         *
         * @author xl date:2015-12-24上午10:41:58
         */
        private class SelectImagesGirdAdapter extends FatherAdapter<UpImgItem> {

            public SelectImagesGirdAdapter(Context ctx) {
                super(ctx);
            }


            /**
             * 预留一个点击项
             */
            @Override
            public int getCount() {
                return super.getCount() + 1;
            }

            private int mItemSize;

            private boolean canClick = true;

            public void setCanClick(boolean canClick) {
                this.canClick = canClick;
                notifyDataSetChanged();
            }


            /**
             * 设置条目的大小
             *
             * @param size
             * @return
             * @author xl
             * @date:2015-12-24上午11:05:20
             * @description
             */
            public void setItemSize(int size) {
                mItemSize = size;
            }

            @Override
            public View getView(final int position, View convertView,
                                ViewGroup parent) {
                ViewHolder holder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.distribiton_select_image_item,
                            parent, false);
                    convertView.setLayoutParams(new GridView.LayoutParams(
                            mItemSize, mItemSize));
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                }
                holder = (ViewHolder) convertView.getTag();
                if (position == getDatas().size()) {
                    holder.delete.setVisibility(View.GONE);
                    holder.image.setVisibility(View.GONE);
                    holder.ll_loading_bg.setVisibility(View.GONE);
                    holder.iv_bg_img.setVisibility(View.VISIBLE);
                    holder.add_text.setVisibility(View.VISIBLE);
                    convertView.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (choosePhotoPop == null) {
                                choosePhotoPop = new ChoosePhotoPop(mContext,
                                        R.layout.act_distribution_offline_pay, ImageSelecWithUploadtFragment.RESULT_CODE_SELECTPHOTPO);
                            }
                            if (canClick) {
                                choosePhotoPop.showChooseWindow();
                            }
                        }
                    });
                    if (position == MAX_NUM) {
                        convertView.setVisibility(View.GONE);
                    }
                } else {
                    ImageUtils.setCommonImage(mContext, TextUtils.isEmpty(getItem(position).urlLocal) ? getItem(position).url : getItem(position).urlLocal, holder.image, mItemSize, mItemSize);
                    holder.delete.setVisibility(View.VISIBLE);
                    holder.image.setVisibility(View.VISIBLE);
                    holder.iv_bg_img.setVisibility(View.INVISIBLE);
                    holder.add_text.setVisibility(View.INVISIBLE);
                    holder.delete.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(canClick){
                                removeItem(position);
                                mSelectPath.remove(position);
                                notifyDataSetChanged();
                            }
                        }
                    });


                    holder.image.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (canClick && (!TextUtils.isEmpty(getItem(position).url))) {
                                PublicWay.startBigImageActivity((Activity) mContext, getItem(position).url);
                            }
                        }
                    });

                    if (getItem(position).isUp) {
                        holder.ll_loading_bg.setVisibility(View.VISIBLE);
                    } else {
                        holder.ll_loading_bg.setVisibility(View.GONE);
                    }
                    convertView.setBackgroundResource(R.color.white);
                    convertView.setClickable(false);

                }
                return convertView;
            }
        }

        private static class ViewHolder {
            View delete;
            View ll_loading_bg;
            ImageView image;
            ImageView iv_bg_img;
            TextView add_text;

            public ViewHolder(View convertView) {
                delete = convertView.findViewById(R.id.tv_delete_image);
                ll_loading_bg = convertView.findViewById(R.id.ll_loading_bg);
                image = (ImageView) convertView
                        .findViewById(R.id.iv_selected_image);
                iv_bg_img = (ImageView) convertView
                        .findViewById(R.id.iv_bg_img);
                add_text = (TextView) convertView
                        .findViewById(R.id.add_text);
            }
        }
    }

