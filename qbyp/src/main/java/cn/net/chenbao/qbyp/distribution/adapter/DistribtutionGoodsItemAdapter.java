package cn.net.chenbao.qbyp.distribution.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.distribution.been.DistributionGood;
import cn.net.chenbao.qbyp.utils.DensityUtil;
import cn.net.chenbao.qbyp.utils.ImageUtils;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

import java.util.ArrayList;

/**
 * Created by ZXJ on 2017/6/18.
 */

public class DistribtutionGoodsItemAdapter extends BaseQuickAdapter<DistributionGood, BaseViewHolder> {
    private  int widthImg;
    public DistribtutionGoodsItemAdapter(ArrayList<DistributionGood> list,int imgwidth) {
        super(R.layout.list_distribution_goods_item_recycle, list);
        widthImg=imgwidth;

    }

    @Override
    protected void convert(BaseViewHolder helper, DistributionGood item) {

        ImageView imageView = (ImageView) helper.getView(R.id.iv_good_img);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height=widthImg;
        layoutParams.width=widthImg;
        imageView.setLayoutParams(layoutParams);
        ImageUtils.setCommonImage(mContext, DensityUtil.getRightImgScreen(item.ImageUrl, widthImg, widthImg), imageView);
        WWViewUtil.textInsertDrawable(mContext, (TextView) helper.getView(R.id.tv_good_name), item.FenXiao.ProductName, false, false);
        helper.setText(R.id.tv_good_price, "￥" + WWViewUtil.numberFormatWithTwo(item.FenXiao.SalePrice))
                .setText(R.id.tv_good_jifen, "积分" + WWViewUtil.numberFormatWithTwo(item.FenXiao.ConsumeNum))
                .setText(R.id.tv_good_sales_num, "销量" + item.SaleQty + "件");
    }
}
