package cn.net.chenbao.qbypseller.dialog;

import java.util.HashMap;
import java.util.Map;

import ww.wheel.widget.OnWheelChangedListener;
import ww.wheel.widget.WheelView;
import ww.wheel.widget.adapters.ArrayWheelAdapter;

import android.app.Dialog;
import android.content.Context;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import cn.net.chenbao.qbypseller.R;

/**
 * 二级联动弹窗
 *
 * @author xl
 * @date:2016-8-8下午5:41:50
 * @description
 */
public class TwoLevelDialog extends Dialog implements
        android.view.View.OnClickListener {

    private WheelView level_f;
    private WheelView level_s;
    /**
     * 数据
     */
    private Map<String, String[]> datas = new HashMap<String, String[]>();

    /**
     * 类型
     */
    private int mType = 0;
    /**
     * 时分选择
     */
    public static final int TYPE_HM = 0;
    /**
     * 年月选择
     */
    public static final int TYPE_YM = 1;

    /**
     * 一级数据
     */
    private String[] datas_f;
    /**
     * 二级数据
     */
    private String[] datas_s;
    /**
     * 一级选中数据
     */
    private String data_f;
    /**
     * 二选中数据
     */
    private String data_s;

    public TwoLevelDialog(Context context, int type) {
        super(context, R.style.DialogStyle);

        mType = type;
        setContentView(R.layout.dialog_two_level_select);
        LayoutParams params = getWindow().getAttributes();
        params.width = context.getResources().getDisplayMetrics().widthPixels;
        params.gravity = Gravity.BOTTOM;

        level_f = (WheelView) findViewById(R.id.wv_level_first);
        level_s = (WheelView) findViewById(R.id.wv_level_second);
        level_f.setVisibleItems(6);
        level_s.setVisibleItems(6);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_confirm).setOnClickListener(this);
        level_f.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            }
        });
        level_s.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
            }
        });
        initData();
    }

    private void initData() {
        switch (mType) {
            case TYPE_HM:// 时分
                datas_f = new String[24];
                for (int h = 0; h < 24; h++) {
                    datas_f[h] = h + "";
                }
                datas_s = new String[60];
                for (int m = 0; m < 60; m++) {
                    datas_s[m] = m + "";
                }
                level_f.setViewAdapter(new ArrayWheelAdapter<String>(getContext(),
                        datas_f));
                level_s.setViewAdapter(new ArrayWheelAdapter<String>(getContext(),
                        datas_s));
                break;

            case TYPE_YM:// 年月
                Time time = new Time("GTM+8");
                time.setToNow();
                int year = time.year;
                datas_f = new String[year - 2013];
                for (int y = 0; y < datas_f.length; y++) {
                    datas_f[y] = year + "";
                }
                for (int i = 2014; i < year + 1; i++) {
                    datas_f[i - 2014] = i + "";
                }
                datas_s = new String[12];
                for (int j = 1; j < 13; j++) {
                    datas_s[j - 1] = j + "";
                }


                level_f.setViewAdapter(new ArrayWheelAdapter<String>(getContext(),
                        datas_f));
                level_s.setViewAdapter(new ArrayWheelAdapter<String>(getContext(),
                        datas_s));
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm:
                if (onSelectOkLisentner != null) {
                    onSelectOkLisentner.selectOk(datas_f[level_f.getCurrentItem()],
                            datas_s[level_s.getCurrentItem()]);
                }
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 确定按钮的回调
     */
    private OnSelectOkLisentner onSelectOkLisentner;

    public void setSelectOkListener(OnSelectOkLisentner lisentner) {
        onSelectOkLisentner = lisentner;
    }

    public interface OnSelectOkLisentner {
        void selectOk(String first, String second);
    }
}
