package cn.net.chenbao.qbyp.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.view.ActionItem;

import java.util.ArrayList;

/**
 * Created by ZXJ on 2017/6/18.
 */

public class TitlePopupAdapter extends BaseQuickAdapter<ActionItem,BaseViewHolder> {
    public TitlePopupAdapter(ArrayList<ActionItem> list) {
        super(R.layout.pop_title_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActionItem item) {
        helper.setText(R.id.tv_name, item.mTitle);
        helper.setImageDrawable(R.id.iv_image, item.mDrawable);
    }
}
