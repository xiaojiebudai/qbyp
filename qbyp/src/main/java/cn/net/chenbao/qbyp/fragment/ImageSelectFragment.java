package cn.net.chenbao.qbyp.fragment;

import java.util.ArrayList;

import org.xutils.x;
import org.xutils.image.ImageOptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import cn.net.chenbao.qbyp.adapter.FatherAdapter;
import cn.net.chenbao.qbyp.imageSelector.MultiImageSelectorActivity;

import cn.net.chenbao.qbyp.R;

import cn.net.chenbao.qbyp.utils.DensityUtil;


/**
 * 图片选择的fragment
 *
 * @author xl
 * @description 使用时注意width, fragment的设计就是屏幕的使用情况, 父容器的padding也不能设置
 */
public class ImageSelectFragment extends FatherFragment {
    /**
     * 请求获取图片
     */
    private static final int REQUEST_IMAGE = 101;
    public static final String MAX_NUMBER = "max_number";
    private SelectImagesGirdAdapter mAdapter;
    /**
     * GridView numCount
     */
    private int GRIDVIEW_NUMCOUNT = 4;
    /**
     * 最多选5张
     */
    private int MAX_NUM = 5;
    /**
     * 图片路径
     */
    private ArrayList<String> mSelectPath;

    private SelectPhotoNumCallBack selectPhotoNumListener;

    public SelectPhotoNumCallBack getSelectPhotoNumListener() {
        return selectPhotoNumListener;
    }

    public void setSelectPhotoNumListener(
            SelectPhotoNumCallBack selectPhotoNumListener) {
        this.selectPhotoNumListener = selectPhotoNumListener;
    }

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
            mSelectPath = getArguments().getStringArrayList(KEY_EXISTING);
            outMargin = arguments.getInt(OUTMARGIN);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new SelectImagesGirdAdapter(getActivity());
        GridView images = (GridView) mGroup.findViewById(R.id.gv_select_images);

        int gridWidth = DensityUtil.getScreenWidth(getActivity())
                - 2
                * getResources().getDimensionPixelOffset(
                R.dimen.refund_images_margin)
                - DensityUtil.dip2px(getActivity(), outMargin);
        int columnSpace = getResources().getDimensionPixelOffset(
                R.dimen.refund_images_space_size);
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
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                mSelectPath = data
                        .getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

                if (selectPhotoNumListener != null && mSelectPath != null) {
                    selectPhotoNumListener.selectPhotoNumListener(mSelectPath
                            .size());
                }
                mAdapter.setDatas(mSelectPath);
            }
        }
    }

    public ArrayList<String> getSelectImages() {
        return mSelectPath;
    }

    /**
     * 选择的图片的GridView适配器
     *
     * @author xl date:2015-12-24上午10:41:58
     */
    private class SelectImagesGirdAdapter extends FatherAdapter<String> {

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
                convertView = mInflater.inflate(R.layout.item_select_image,
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
                convertView
                        .setBackgroundResource(R.drawable.icon_addpic_unfocused);
                convertView.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,
                                MultiImageSelectorActivity.class);

                        // 是否显示可以拍照
                        intent.putExtra(
                                MultiImageSelectorActivity.EXTRA_SHOW_CAMERA,
                                true);
                        // 多选模式
                        intent.putExtra(
                                MultiImageSelectorActivity.EXTRA_SELECT_MODE,
                                MultiImageSelectorActivity.MODE_MULTI);
                        intent.putExtra(
                                MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
                                MAX_NUM);
                        // 已选择的图片
                        if (mSelectPath != null && mSelectPath.size() > 0) {
                            intent.putExtra(
                                    MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
                                    mSelectPath);
                        } else {

                        }
                        startActivityForResult(intent, REQUEST_IMAGE);
                    }
                });
                if (position == 5) {
                    convertView.setVisibility(View.GONE);
                }
            } else {
                ImageOptions options = new ImageOptions.Builder()
                        .setAutoRotate(true).build();
                x.image().bind(holder.image, getItem(position), options);
                holder.delete.setVisibility(View.VISIBLE);
                holder.image.setVisibility(View.VISIBLE);
                holder.delete.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItem(position);
                        mSelectPath.remove(position);
                        if (selectPhotoNumListener != null) {
                            selectPhotoNumListener
                                    .selectPhotoNumListener(mSelectPath.size());
                        }
                        notifyDataSetChanged();
                    }
                });
                convertView.setBackgroundResource(R.color.white);
                convertView.setClickable(false);
            }
            return convertView;
        }
    }

    public interface SelectPhotoNumCallBack {

        void selectPhotoNumListener(int num);

    }

    private static class ViewHolder {
        View delete;
        ImageView image;

        public ViewHolder(View convertView) {
            delete = convertView.findViewById(R.id.tv_delete_image);
            image = (ImageView) convertView
                    .findViewById(R.id.iv_selected_image);
        }
    }
}
