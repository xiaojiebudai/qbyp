package cn.net.chenbao.qbyp.activity;

import cn.net.chenbao.qbyp.bean.Location;
import cn.net.chenbao.qbyp.fragment.SearchSelectFragment;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.PublicWay;
import cn.net.chenbao.qbyp.utils.SharedPreferenceUtils;
import cn.net.chenbao.qbyp.utils.WWToast;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;

import cn.net.chenbao.qbyp.R;

/**
 * 搜索界面
 *
 * @author xl
 */
public class SearchActiivty extends FatherActivity implements OnClickListener {

    private EditText mEditText_search;
    private View mView_clear;
    private int model;
    /**
     * 本地生活
     */
    public final static int LOCATION = 0;
    /**
     * 自营商城
     */
    public final static int SELFSHOP = 1;
    private SearchSelectFragment searchSelectFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.act_search;
    }

    @Override
    protected void initValues() {
        model = getIntent().getIntExtra(Consts.KEY_MODULE, LOCATION);
    }

    @Override
    protected void initView() {
        initHeadBack();
        searchSelectFragment = new SearchSelectFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Consts.KEY_MODULE, model);
        searchSelectFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.searchselectfragment, searchSelectFragment).commit();

        searchSelectFragment.setSearchListener(new SearchSelectFragment.SearchCallBack() {
            @Override
            public void searchKeyWordListener(String keyword) {
                mEditText_search.setText(keyword);
                if (model == LOCATION) {
                    startLocalSearch(keyword);
                } else {
                    startSelfShop(keyword);
                }

            }
        });

        initTextHeadRigth(R.string.search, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String data = mEditText_search.getText().toString();
                if (TextUtils.isEmpty(data)) {
                    return;
                }
            }
        });
        mEditText_search = (EditText) findViewById(R.id.edt_search);

        if(model==LOCATION){
            mEditText_search.setHint(R.string.please_input_seller_name);
        }else{
            mEditText_search.setHint(R.string.search_you_want);
        }



        mView_clear = findViewById(R.id.iv_search_clear);
        findViewById(R.id.rl_head_right).setOnClickListener(this);
        mView_clear.setOnClickListener(new View.OnClickListener() {

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
    }


    @Override
    protected void doOperate() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_head_right:// 点击搜索
                final String trim = mEditText_search.getText().toString().trim();
                if (trim.indexOf(",") != -1 || trim == null) {// 检查是否有逗号
                    WWToast.showShort(R.string.input_error);
                    return;
                }
                if (model == LOCATION) {
                    startLocalSearch(trim);
                } else {
                    startSelfShop(trim);
                }
                if (searchSelectFragment != null) {
                    searchSelectFragment.resetHistory();
                }
                hideSoftKeyboard();
                break;

            default:
                break;
        }
    }

    private void startLocalSearch(String trim) {
        SharedPreferenceUtils.getInstance().saveSearchHistory(trim);
        Location location = JSON.parseObject(SharedPreferenceUtils.getInstance().getLocation(), Location.class);
        if (location != null) {
            PublicWay.startSearchResultActivity(SearchActiivty.this,
                    trim, 0, location.latitudes, location.Longitudes,
                    SearchDataActiivty.SEARCH_MODE);
        } else {
            PublicWay.startSearchResultActivity(SearchActiivty.this,
                    trim, 0, 0, 0,
                    SearchDataActiivty.SEARCH_MODE);
        }

    }

    private void startSelfShop(String keyword) {
        SharedPreferenceUtils.getInstance().saveSearchHistorySelf(keyword);
        PublicWay.stratSelfSupportSearchDataActivity(this, keyword, "");
    }
}
