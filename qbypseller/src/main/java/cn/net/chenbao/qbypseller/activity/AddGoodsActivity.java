package cn.net.chenbao.qbypseller.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.net.chenbao.qbypseller.R;
import cn.net.chenbao.qbypseller.WWApplication;
import cn.net.chenbao.qbypseller.api.Api;
import cn.net.chenbao.qbypseller.api.ApiBaseData;
import cn.net.chenbao.qbypseller.api.ApiSeller;
import cn.net.chenbao.qbypseller.bean.Category;
import cn.net.chenbao.qbypseller.bean.Goods;
import cn.net.chenbao.qbypseller.dialog.ChoosePhotoDialog;
import cn.net.chenbao.qbypseller.dialog.CommonDialog;
import cn.net.chenbao.qbypseller.utils.Consts;
import cn.net.chenbao.qbypseller.utils.DensityUtil;
import cn.net.chenbao.qbypseller.utils.DialogUtils;
import cn.net.chenbao.qbypseller.utils.FileUtils;
import cn.net.chenbao.qbypseller.utils.ImageUtils;
import cn.net.chenbao.qbypseller.utils.ParamsUtils;
import cn.net.chenbao.qbypseller.utils.PublicWay;
import cn.net.chenbao.qbypseller.utils.WWToast;
import cn.net.chenbao.qbypseller.utils.WWViewUtil;
import cn.net.chenbao.qbypseller.xutils.WWXCallBack;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

/**
 * 添加商品
 *
 * @author xl
 * @date 2016-7-30 下午12:32:32
 * @description
 */
public class AddGoodsActivity extends FatherActivity implements OnClickListener {

    /**
     * 直接从主界面(以后或者其他位置)跳转过来,添加成功需要弹窗提示,继续或者去查看
     */
    public static final int MODULE_DIRECT = 1;
    public static final int MODULE_COMMON = 0;

    /**
     * 上传图片
     */
    private static final int REQUEST_CODE_IMAGE = 10;

    /**
     * 选择分类
     */
    private static final int REQUEST_CODE_SELECT_CATEGORY = 11;
    /**
     * 扫码
     */
    private static final int REQUEST_CODE_SCAN = 12;

    /**
     * 条形码
     */
    private EditText mEditText_barcode;
    /**
     * 名称
     */
    private EditText mEditText_name;
    /**
     * 价格
     */
    private EditText mEditText_price;
    /**
     * 库存
     */
    private EditText mEditText_stock;
    /**
     * 简介
     */
    private EditText mEditText_des;
    /**
     * 已售
     */
    private TextView mTextView_sold;
    /**
     * 分类
     */
    private TextView mTextView_class;
    /**
     * 图片
     */
    private String mImageUrl;
    /**
     * 状态
     */
    private int mState;
    /**
     * 已售
     */
    private int mSold;
    private ImageView mImage;
    private ChoosePhotoDialog mChoosePhotoDialog;

    private static int mode;
    /**
     * 新增
     */
    public static final int MODE_ADD = 0;
    /**
     * 编辑
     */
    public static final int MODE_EDIT = 1;

    private static final String ImageView = null;

    private RadioGroup mRadioGroup;

    @Override
    protected int getLayoutId() {
        return R.layout.act_add_goods;
    }

    private Category category;

    private Goods goods;

    private int mFromModule;

    @Override
    protected void initValues() {
        mFromModule = getIntent().getIntExtra(Consts.KEY_MODULE, MODULE_COMMON);

        String extra_c = getIntent().getStringExtra(Consts.KET_CATEGORY);
        if (!TextUtils.isEmpty(extra_c)) {// 对应类目的添加和编辑模式都会传一个类目过来
            category = JSON.parseObject(extra_c, Category.class);
        }
        switch (getIntent().getIntExtra(Consts.KEY_MODE, MODE_ADD)) {
            case MODE_ADD:
                initDefautHead(R.string.add_goods, true);
                break;
            case MODE_EDIT:
                initDefautHead(R.string.edit_goods, true);
                String data = getIntent().getStringExtra(Consts.KEY_DATA);
                if (!TextUtils.isEmpty(data)) {
                    goods = JSON.parseObject(data, Goods.class);
                }
                break;

            default:
                break;
        }

    }

    @Override
    protected void initView() {
        findViewById(R.id.tv_click_upload).setOnClickListener(this);
        mEditText_barcode = (EditText) findViewById(R.id.edt_goods_bar_code);
        mEditText_name = (EditText) findViewById(R.id.edt_goods_name);
        mEditText_price = (EditText) findViewById(R.id.edt_goods_price);
//        mEditText_price.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().contains(".")) {
//                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
//                        s = s.toString().subSequence(0,
//                                s.toString().indexOf(".") + 3);
//                        mEditText_price.setText(s);
//                        mEditText_price.setSelection(s.length());
//                    }
//                }
//                if (s.toString().trim().substring(0).equals(".")) {
//                    s = "0" + s;
//                    mEditText_price.setText(s);
//                    mEditText_price.setSelection(2);
//                }
//                if (s.toString().startsWith("0")
//                        && s.toString().trim().length() > 1) {
//                    if (!s.toString().substring(1, 2).equals(".")) {
//                        mEditText_price.setText(s.subSequence(0, 1));
//                        mEditText_price.setSelection(1);
//                        return;
//                    }
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
        WWViewUtil.inputLimit(mEditText_price,99999999.99,2);
        mEditText_stock = (EditText) findViewById(R.id.edt_stock_count);
        mEditText_des = (EditText) findViewById(R.id.edt_goods_des);
        mTextView_class = (TextView) findViewById(R.id.tv_goods_category);
        mTextView_sold = (TextView) findViewById(R.id.tv_has_sold);
        mEditText_barcode = (EditText) findViewById(R.id.edt_goods_bar_code);
        mImage = (android.widget.ImageView) findViewById(R.id.iv_image);
        mRadioGroup = (RadioGroup) findViewById(R.id.rg_goods_state);

        Drawable drawable0 = getResources().getDrawable(R.drawable.radiobutton_circle);
        drawable0.setBounds(0, 0, 38, 38);//buttonImg宽高38px
        ((RadioButton)mRadioGroup.getChildAt(0)).setCompoundDrawables(drawable0, null,  null, null);
        Drawable drawable1 = getResources().getDrawable(R.drawable.radiobutton_circle);
        drawable1.setBounds(0, 0, 38, 38);
        ((RadioButton)mRadioGroup.getChildAt(1)).setCompoundDrawables(drawable1, null,  null, null);

        findViewById(R.id.tv_confirm).setOnClickListener(this);// 确认提交
        findViewById(R.id.ll_select_class).setOnClickListener(this);// 选择分类
        findViewById(R.id.ll_scan).setOnClickListener(this);// 扫描

        if (category != null) {
            mTextView_class.setText(category.ClassName);
        }
        if (goods != null) {// 编辑商品的初始数据
            findViewById(R.id.ll_sale).setVisibility(View.VISIBLE);
            findViewById(R.id.v_sale_line).setVisibility(View.VISIBLE);
            mEditText_barcode.setText(goods.Barcode);
            mEditText_name.setText(goods.GoodsName);
            mEditText_price.setText(WWViewUtil.numberFormatWithTwo(goods.Price));
            mEditText_stock.setText(goods.StockQty + "");
            mTextView_sold.setText(goods.SaleQty + "");
            mEditText_des.setText(goods.Describe);
            mRadioGroup
                    .check(goods.Status == Goods.STATE_ONSALE ? R.id.rb_onsale
                            : R.id.rb_sold_out);
            ImageUtils.setCommonImage(this, ImageUtils.getRightImgScreen(goods.GoodsImg , DensityUtil.dip2px(AddGoodsActivity.this,130),DensityUtil.dip2px(AddGoodsActivity.this,130)), mImage);
            mImageUrl = goods.GoodsImg;
        } else {
            findViewById(R.id.ll_sale).setVisibility(View.GONE);
            findViewById(R.id.v_sale_line).setVisibility(View.GONE);
        }
    }

    @Override
    protected void doOperate() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_click_upload:
                if (null == mChoosePhotoDialog) {
                    mChoosePhotoDialog = new ChoosePhotoDialog(this, false,true,
                            REQUEST_CODE_IMAGE);
                }
                mChoosePhotoDialog.show();
                break;
            case R.id.ll_select_class:
                PublicWay.startSelectCategoryActivity(this,
                        REQUEST_CODE_SELECT_CATEGORY);
                break;
            case R.id.ll_scan:
                PublicWay.startCaptureActivityForResult(this, REQUEST_CODE_SCAN);
                break;
            case R.id.tv_confirm:
                String barcode = mEditText_barcode.getText().toString().trim();
                String name = mEditText_name.getText().toString().trim();
                String price = mEditText_price.getText().toString().trim();
                String stock_s = mEditText_stock.getText().toString().trim();
                int stock = 0;
                if (TextUtils.isEmpty(name)) {
                    WWToast.showShort(R.string.please_input_good_name);
                    return;
                }
                if (TextUtils.isEmpty(price)) {
                    WWToast.showShort(R.string.please_input_price);
                    return;
                }
                if (price.equals("0.00") || price.equals("0.0") || price.equals("0")) {
                    WWToast.showShort(R.string.price_not_zero);
                    return;
                }

                if (!TextUtils.isEmpty(stock_s)) {
                    //java.lang.NumberFormatException: Invalid int: "9999999704"
                    //                                         最大值2147483647
                    stock = Integer.parseInt(stock_s);
                } else {
                    WWToast.showShort(R.string.please_input_stq);
                    return;
                }

                String des = mEditText_des.getText().toString().trim();
                if (null == category) {
                    WWToast.showShort(R.string.please_choice_sort);
                    return;
                }
                JSONObject object = new JSONObject();
                Goods goods = new Goods();
                goods.Barcode = barcode;
                goods.GoodsName = name;
                goods.Price = Double.valueOf(price);
                goods.StockQty = stock;
                goods.Describe = des;
                goods.GoodsImg = mImageUrl;
                goods.Status = mRadioGroup.getCheckedRadioButtonId() == R.id.rb_onsale ? Goods.STATE_ONSALE
                        : Goods.STATE_OFFSALE;
                goods.ClassId = category.ClassId;
                goods.GoodsId = this.goods == null ? 0 : this.goods.GoodsId;
                object.put(Api.KEY_SESSIONID, WWApplication.getInstance()
                        .getSessionId());
                object.put(Api.KEY_data, goods);
                RequestParams params = ParamsUtils.getPostJsonParams(object,
                        ApiSeller.goodsUpdate());
                showWaitDialog();
                x.http().post(params, new WWXCallBack("GoodsUpdate") {

                    @Override
                    public void onAfterSuccessOk(JSONObject data) {
                        switch (mFromModule) {
                            case MODULE_DIRECT:
                                showAddSuccessDialog();
                                break;
                            default:
                                Goods goods = JSON
                                        .parseObject(data.getJSONObject(Api.KEY_DATA)
                                                .toJSONString(), Goods.class);
                                Intent intent = new Intent();
                                intent.putExtra(Consts.KEY_DATA,
                                        JSON.toJSONString(goods));
                                if (categoriesJson != null) {
                                    intent.putExtra(Consts.KEY_DATA_TWO, categoriesJson);
                                }
                                setResult(RESULT_OK, intent);
                                WWToast.showCostomSuccess(R.string.set_succuss);
                                finish();
                                break;
                        }

                    }

                    @Override
                    public void onAfterFinished() {
                        dismissWaitDialog();
                    }
                });

                break;

            default:
                break;
        }
    }

    /**
     * 添加成功
     */
    private void showAddSuccessDialog() {
        final CommonDialog dialog = DialogUtils.getCommonDialog(this,
                R.string.goods_publish_success);
        dialog.getButtonLeft(R.string.continue_add).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        reset();
                        dialog.dismiss();
                    }
                });
        dialog.getButtonRight(R.string.go_check).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        PublicWay.startGoodsManageActivity(
                                AddGoodsActivity.this, 0);
                        dialog.dismiss();
                        finish();
                    }
                });
        dialog.show();
    }

    /**
     * 重置
     */
    private void reset() {
        mImageUrl = "";
        mImage.setImageResource(R.drawable.commodity_uploading_icon);
        mEditText_barcode.setText("");
        mEditText_name.setText("");
        mEditText_price.setText("");
        mEditText_stock.setText("");
        mTextView_sold.setText(0 + getString(R.string.mei_fen));
        mEditText_des.setText("");
        mRadioGroup.check(R.id.rb_onsale);
        mTextView_class.setText("");
        category = null;
    }

    private String categoriesJson = null;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_IMAGE:
                    if (data != null) {
                        final String mImageUrlStr = data.getStringExtra(Consts.KEY_DATA);
                        RequestParams params = new RequestParams(
                                ApiBaseData.upImage());
                        params.addBodyParameter(Api.KEY_FILE, new File(FileUtils.getCompressedImageFileUrl1(mImageUrlStr)));
                        params.setMultipart(true);
                        showWaitDialog();
                        x.http().post(params, new WWXCallBack("UpImage") {

                            @Override
                            public void onAfterSuccessOk(JSONObject data) {
                                mImageUrl = data.getString(Api.KEY_DATA);
                                ImageUtils.setCommonImage(AddGoodsActivity.this,  ImageUtils.getRightImgScreen(mImageUrlStr, DensityUtil.dip2px(AddGoodsActivity.this,130),DensityUtil.dip2px(AddGoodsActivity.this,130)),
                                        (ImageView) findViewById(R.id.iv_image));
                            }

                            @Override
                            public void onError(Throwable arg0, boolean arg1) {
                                super.onError(arg0, arg1);
                                WWToast.showShort(R.string.img_upload_fail);
                            }

                            @Override
                            public void onAfterFinished() {
                                dismissWaitDialog();
                            }
                        });


                    }
                    break;
                case REQUEST_CODE_SELECT_CATEGORY:
                    if (data != null) {
                        category = JSON.parseObject(
                                data.getStringExtra(Consts.KEY_DATA),
                                Category.class);
                        categoriesJson = data.getStringExtra(Consts.KEY_DATA_TWO);// 可能新增了类目,就传回所有数据
                        mTextView_class.setText(category.ClassName);
                    }
                    break;
                case REQUEST_CODE_SCAN:
                    if (data != null) {
                        String code = data.getStringExtra(Consts.KEY_DATA);
                        mEditText_barcode.setText(code);
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
