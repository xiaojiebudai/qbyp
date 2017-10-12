package cn.net.chenbao.qbyp.view;

import java.util.List;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.adapter.listview.CategoryList2Adapter;
import cn.net.chenbao.qbyp.adapter.listview.CategoryListAdapter;
import cn.net.chenbao.qbyp.bean.TradeExt;
import cn.net.chenbao.qbyp.myinterface.PopCallBackListener;
import cn.net.chenbao.qbyp.utils.WWViewUtil;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

/***
 * Description:商家管理行业筛选二级pop
 *
 * @author wuri
 */
public class ErjiCategoryPop implements OnItemClickListener {
    private Context context;
    private ListView rootList;
    private ListView childList;
    private List<TradeExt> firstList;
    private List<TradeExt> secondList;
    private LinearLayout layout;
    private FrameLayout flChild;
    private PopupWindow mPopWin;
    private int parentId;
    public PopCallBackListener popCallBackListener;
    private CategoryListAdapter cla;
    private CategoryList2Adapter childcla;
    public View v;
    private boolean isShow;

    public ErjiCategoryPop(final Context context, int parentId,
                           PopCallBackListener popCallBackListener) {
        this.context = context;
        this.parentId = parentId;
        this.popCallBackListener = popCallBackListener;
    }

    public void setDatas(List<TradeExt> firstList, List<TradeExt> secondList) {
        this.firstList = firstList;
        this.secondList = secondList;
    }

    /**
     * @param width  pop的宽
     * @param height pop的高
     * @param v      位于v下方
     */
    public void showPopupWindow(int width, int height, View v) {
        this.v = v;
        if (firstList.size() == 0) {//初次显示secondList==null
            return;
        }

        layout = (LinearLayout) LayoutInflater.from(context).inflate(parentId,
                null);
        ImageView iv_alpha_bg = (ImageView) layout
                .findViewById(R.id.iv_alpha_bg);

        iv_alpha_bg.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (popCallBackListener != null) {
                    popCallBackListener.onBlankSpaceClicked();
                }
                mPopWin.dismiss();
                isShow = false;
            }
        });
        rootList = (ListView) layout.findViewById(R.id.rootcategory);

        cla = new CategoryListAdapter(context,
                firstList);
        rootList.setAdapter(cla);
        cla.notifyDataSetChanged();
        flChild = (FrameLayout) layout.findViewById(R.id.child_lay);
        childList = (ListView) layout.findViewById(R.id.childcategory);
        flChild.setVisibility(View.VISIBLE);

        mPopWin = new PopupWindow(layout, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        mPopWin.setBackgroundDrawable(new BitmapDrawable());
        mPopWin.setOutsideTouchable(true);
        mPopWin.setFocusable(true);
        WWViewUtil.setPopInSDK7(mPopWin,v);
//        mPopWin.showAsDropDown(v);
        isShow = true;
        mPopWin.update();
        mPopWin.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                isShow = false;
                if (popCallBackListener != null) {
                    popCallBackListener.onBlankSpaceClicked();
                }
            }
        });

        setChildItemData();

        rootList.setTag(R.id.rootcategory);
        childList.setTag(R.id.childcategory);
        rootList.setOnItemClickListener(this);
        childList.setOnItemClickListener(this);
    }

    public boolean isShow() {
        return isShow;
    }

    private void show() {
        WWViewUtil.setPopInSDK7(mPopWin,v);
//        mPopWin.showAsDropDown(v);
        isShow = true;
    }

    public void disMissPop() {
        if (mPopWin != null) {
            mPopWin.dismiss();
            isShow = false;
        }
    }

    /*
     * 设置子类列表数据
     */
    private void setChildItemData() {
        //初次显示pop,secondList为null,adapter已经做了非空处理
        childcla = new CategoryList2Adapter(context, secondList);
        childList.setAdapter(childcla);
        childcla.notifyDataSetChanged();
    }

    /*刷新子集合*/
    public void transfer2ChildList(List<TradeExt> list2) {
        childcla.itemList = list2;
        childcla.notifyDataSetChanged();
    }

    /*刷新根集合以及子集合并显示pop*/
    public void transfer2RootList(List<TradeExt> list1, List<TradeExt> list2) {
        cla.itemList = list1;
        cla.notifyDataSetChanged();
        childcla.itemList = list2;
        childcla.notifyDataSetChanged();
        show();
    }

    private Handler hander = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                if (mPopWin != null) {
                    mPopWin.dismiss();
                    isShow = false;
                }
            }
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        switch ((Integer) parent.getTag()) {
            case R.id.rootcategory:
                // 获取选中的parentId,得到子行业数据
                if (popCallBackListener != null) {
                    popCallBackListener.onClickFirstitem(position);
                }
                view.setSelected(true);
                break;
            case R.id.childcategory:
                // childItem被选,重置筛选颜色
                if (popCallBackListener != null) {
                    popCallBackListener.onBlankSpaceClicked();
                    popCallBackListener.onClickSeconditem(position);
                }
                hander.sendEmptyMessage(0);
                break;
            default:
                break;
        }

    }
}
