package cn.net.chenbao.qbyp.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.net.chenbao.qbyp.activity.SearchActiivty;
import cn.net.chenbao.qbyp.adapter.listview.HotHistoryAdapter;
import cn.net.chenbao.qbyp.api.ApiBaseData;
import cn.net.chenbao.qbyp.bean.HotSearchCategory;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.JsonUtils;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.ZLog;
import cn.net.chenbao.qbyp.view.AutoRankView;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.x;
import org.xutils.http.RequestParams;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbyp.R;

/**
 * 搜索选择
 *
 * @author xl
 * @date 2016-7-26 下午9:46:46
 */
public class SearchSelectFragment extends FatherFragment implements
        OnClickListener {

    private AutoRankView mAv;
    private ListView mHistoryListView;
    private List<View> list = new ArrayList<View>();
    private HotHistoryAdapter hotHistoryAdapter;
    private SearchCallBack searchListener;
    private int model;

    public SearchCallBack getSearchListener() {
        return searchListener;
    }

    public void setSearchListener(SearchCallBack searchListener) {
        this.searchListener = searchListener;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        model = getArguments().getInt(Consts.KEY_MODULE);
        ZLog.showPost("moshidier   "+model);
        getHistory();
        doRequest();
    }

    private void getHistory() {
        List<String> hotSearch;
        if (model == SearchActiivty.LOCATION) {
            hotSearch = SharedPreferenceUtils.getInstance()
                    .getSearchHistory();
        } else {
            hotSearch = SharedPreferenceUtils.getInstance()
                    .getSearchHistorySelf();
        }
        if (hotSearch != null) {
            hotHistoryAdapter.setDatas(hotSearch);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_select;
    }

    @Override
    protected void initView() {
        mAv = (AutoRankView) mGroup.findViewById(R.id.av);
        hotHistoryAdapter = new HotHistoryAdapter(getActivity());
        mHistoryListView = (ListView) mGroup
                .findViewById(R.id.listview_search_history);
        mHistoryListView.setAdapter(hotHistoryAdapter);
        mHistoryListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (searchListener != null) {
                    searchListener.searchKeyWordListener(hotHistoryAdapter
                            .getItem(position));
                }
            }
        });
        mGroup.findViewById(R.id.tv_clear_history).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_clear_history:// 清除历史记录
                if (model == SearchActiivty.LOCATION) {
                    SharedPreferenceUtils.getInstance().clearSearchHistory();
                } else {
                    SharedPreferenceUtils.getInstance().clearSearchHistorySelf();
                }

                getHistory();
                break;
            default:
                break;
        }

    }

    /**
     * 获取热门搜索
     */
    private void doRequest() {
        RequestParams params = new RequestParams(ApiBaseData.getHotSearch());
        if (model == SearchActiivty.SELFSHOP) {
            params.addBodyParameter("stype", "1");
        }
        x.http().get(params,
                new WWXCallBack("SearchHot") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        String parserJsonDataArray = JsonUtils
                                .parserJsonDataArray(data);
                        List<HotSearchCategory> parseArray = JSON.parseArray(
                                parserJsonDataArray, HotSearchCategory.class);

                        for (final HotSearchCategory hotSearchCategory : parseArray) {
                            final TextView view = (TextView) View.inflate(
                                    getActivity(),
                                    R.layout.textview_hot_search, null);
                            view.setText(hotSearchCategory.KeyWord);
                            view.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!view.isSelected()) {
                                        searchListener
                                                .searchKeyWordListener(hotSearchCategory.KeyWord);
                                        view.setSelected(true);
                                        setTextSelectState(view);
                                    }
                                }
                            });
                            list.add(view);
                            mAv.addView(view);
                        }

                    }

                    @Override
                    public void onAfterFinished() {

                    }
                });

    }

    public void setTextSelectState(View v) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != v) {
                list.get(i).setSelected(false);
            }
        }
    }

    public void resetHistory() {
        getHistory();
    }

    public interface SearchCallBack {
        void searchKeyWordListener(String keyword);
    }

}
