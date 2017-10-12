package cn.net.chenbao.qbyp.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.net.chenbao.qbyp.adapter.CommonPageAdapter;
import cn.net.chenbao.qbyp.fragment.FatherFragment;
import cn.net.chenbao.qbyp.fragment.SearchDataFragment;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.view.TabScrollView;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import cn.net.chenbao.qbyp.R;

/**
 * 搜索结果界面
 *
 * @author xl
 * @description 原策略和搜索界面共用一个, 然后替换fragment, 避免改动, 还是用两个界面控制
 */
public class SearchDataActiivty extends FatherActivity implements
        OnClickListener {
    /**
     * 更多页面跳过来
     */
    public static final int MORE_MODE = 1;
    /**
     * 搜索页面跳过来
     */
    public static final int SEARCH_MODE = 2;

    private EditText mEditText_search;
    private View mView_clear;

    /**
     * 搜索数据
     */
    private String searchWords;// 不同模式,(more 为tradeId,search 为key)

    private double latitude;

    private double longitude;

    private int index;
    /**
     * 全部
     */
    private SearchDataFragment newInstance;
    /**
     * 支持配送
     */
    private SearchDataFragment newInstance2;
    /**
     * 当前模式
     */
    private int mode;

    @Override
    protected int getLayoutId() {
        return R.layout.act_search_result;
    }

    @Override
    protected void initValues() {
        searchWords = getIntent().getStringExtra(Consts.KEY_DATA);
        latitude = getIntent().getDoubleExtra(Consts.LATITUDE, 0);
        longitude = getIntent().getDoubleExtra(Consts.LONGITUDE, 0);
        mode = getIntent().getIntExtra(Consts.KEY_MODULE, -1);

    }

    @Override
    protected void initView() {
        initHeadBack();
        initTextHeadRigth(R.string.search, this);
        mEditText_search = (EditText) findViewById(R.id.edt_search);
        mEditText_search.setHint(R.string.please_input_seller_name);
        if (mode == SEARCH_MODE) {
            mEditText_search.setText(searchWords);
        }
        mView_clear = findViewById(R.id.iv_search_clear);
        mView_clear.setOnClickListener(new View.OnClickListener() {// 清空输入

            @Override
            public void onClick(View v) {
                mEditText_search.setText("");
                v.setVisibility(View.GONE);
            }
        });
        mEditText_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().length() > 0) {
                    mView_clear.setVisibility(View.VISIBLE);
                } else {
                    mView_clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        TabScrollView tabScrollView = (TabScrollView) findViewById(R.id.tabscrollview_tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        String[] array = getResources().getStringArray(
                R.array.search_result_tab);
        List<String> tabs = Arrays.asList(array);
        List<FatherFragment> fragments = new ArrayList<FatherFragment>();
        switch (mode) {
            case MORE_MODE:
                newInstance = SearchDataFragment.newInstance(
                        SearchDataFragment.MORE_SEARCH_MODE, latitude, longitude,
                        searchWords, null);
                newInstance2 = SearchDataFragment.newInstance(
                        SearchDataFragment.MORE_SEARCH_PEISONG, latitude,
                        longitude, searchWords, null);
                break;

            case SEARCH_MODE:
                newInstance = SearchDataFragment.newInstance(
                        SearchDataFragment.HOT_SEARCH_MODE, latitude, longitude,
                        null, searchWords);
                newInstance2 = SearchDataFragment.newInstance(
                        SearchDataFragment.HOT_SEARCH_PEISONG, latitude, longitude,
                        null, searchWords);
                break;
        }
        fragments.add(newInstance);
        fragments.add(newInstance2);
        CommonPageAdapter adapter = new CommonPageAdapter(this, tabs,
                fragments, getSupportFragmentManager(), viewPager, 0, false);
        viewPager.setAdapter(adapter);
        tabScrollView.setViewPager(viewPager);
    }

    @Override
    protected void doOperate() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_head_right:// 搜索
                String trim = mEditText_search.getText().toString().trim();
                if (!trim.equals("")) {
                    newInstance.setSearchData(trim);
                    newInstance2.setSearchData(trim);

                } else {
                    newInstance.setSearchData("");
                    newInstance2.setSearchData("");
                }
                hideSoftKeyboard();
                break;
            default:
                break;
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

}
