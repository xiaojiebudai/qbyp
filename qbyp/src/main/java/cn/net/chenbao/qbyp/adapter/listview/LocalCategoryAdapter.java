package cn.net.chenbao.qbyp.adapter.listview;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.LocalCategoryGridViewAdapter;
import cn.net.chenbao.qbyp.bean.TradesCategory;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ppsher on 2017/3/14.
 */

public class LocalCategoryAdapter extends PagerAdapter {
    int pageCount;
    HashMap<Integer, List<TradesCategory>> map;
    Context mContext;

    public LocalCategoryAdapter(Context context, HashMap<Integer, List<TradesCategory>> map, int pageCount) {
        super();
        this.map = map;
        this.pageCount = pageCount;
        mContext = context;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object); // 移出viewpager两边之外的page布局
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.local_catefory_layout, null);
        GridView gv = (GridView) view.findViewById(R.id.gv);
        final LocalCategoryGridViewAdapter categoryGridViewAdapter = new LocalCategoryGridViewAdapter(mContext);
        categoryGridViewAdapter.setDatas(map.get(position));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TradesCategory tradesCategory = categoryGridViewAdapter.getItem(i);
                if(categoryClickListener!=null){
                    categoryClickListener.categoryClick(tradesCategory);
                }
            }
        });
        gv.setAdapter(categoryGridViewAdapter);
        container.addView(view);
        return view;
    }

    CategoryClickListener categoryClickListener;

    public interface CategoryClickListener {
        void categoryClick(TradesCategory tradesCategory);
    }

    public void setCategoryClickListener(CategoryClickListener categoryClickListener) {
        this.categoryClickListener = categoryClickListener;
    }
}
