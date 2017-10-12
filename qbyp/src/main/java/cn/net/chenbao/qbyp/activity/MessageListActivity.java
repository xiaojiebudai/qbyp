package cn.net.chenbao.qbyp.activity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.adapter.listview.MessageAdapter;
import cn.net.chenbao.qbyp.api.ApiUser;
import cn.net.chenbao.qbyp.bean.Message;
import cn.net.chenbao.qbyp.utils.Consts;
import cn.net.chenbao.qbyp.utils.WWToast;
import cn.net.chenbao.qbyp.utils.WWViewUtil;
import cn.net.chenbao.qbyp.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;

/***
 * Description:消息列表 Company: wangwanglife Version：1.0
 *
 * @author zxj
 * @date 2016-7-30
 */
public class MessageListActivity extends FatherActivity implements
        OnClickListener, OnRefreshListener<ListView> {
    private PullToRefreshListView mPullToRefreshListView;
    private MessageAdapter messageAdapter;
    // 分页数据
    private int mCurrentPage = 0;
    private int totalCount = 0;
    // 提现
    private ArrayList<Message> list;

    @Override
    protected int getLayoutId() {
        // 与收藏用同一个
        return R.layout.act_message;
    }

    @Override
    protected void initValues() {
        initDefautHead(R.string.message, true);

    }

    @Override
    protected void initView() {

        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listview_datas);
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView
                .setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {
                        if (mCurrentPage >= totalCount) {
                            WWToast.showShort(R.string.nomore_data);
                        } else {
                            requestData();
                        }

                    }
                });
        WWViewUtil.setEmptyView(mPullToRefreshListView.getRefreshableView());
        messageAdapter = new MessageAdapter(this);
        mPullToRefreshListView.setAdapter(messageAdapter);
        mPullToRefreshListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (!messageAdapter.getItem(position - 1).ReadFlag) {
                    readMessage(position - 1);
                }
            }
        });
    }

    @Override
    protected void doOperate() {
        requestData();
    }


    private void requestData() {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.getInterMessage());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("pageSize", 20 + "");
        params.addBodyParameter("pageIndex", mCurrentPage + "");
        x.http().get(params, new WWXCallBack("InterMessage") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                mCurrentPage++;
                list = (ArrayList<Message>) JSONArray.parseArray(
                        data.getString("Data"), Message.class);
                totalCount = data.getIntValue("PageCount");
                if (mCurrentPage > 1) {
                    messageAdapter.addDatas(list);
                } else {
                    messageAdapter.setDatas(list);
                }

            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
                mPullToRefreshListView.onRefreshComplete();
            }
        });
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        mCurrentPage = 0;
        requestData();
    }

    /**
     * 消息已读
     *
     * @param i
     */
    private void readMessage(final int i) {
        showWaitDialog();
        RequestParams params = new RequestParams(ApiUser.InterMessageRead());
        params.addBodyParameter(Consts.KEY_SESSIONID, WWApplication
                .getInstance().getSessionId());
        params.addBodyParameter("flowId", messageAdapter.getDatas().get(i).FlowId + "");
        x.http().get(params, new WWXCallBack("InterMessageRead") {

            @Override
            public void onAfterSuccessOk(JSONObject data) {
                messageAdapter.getDatas().get(i).ReadFlag = true;
                messageAdapter.notifyDataSetChanged();
                WWToast.showShort(R.string.message_read);
            }

            @Override
            public void onAfterFinished() {
                dismissWaitDialog();
            }
        });

    }
}
