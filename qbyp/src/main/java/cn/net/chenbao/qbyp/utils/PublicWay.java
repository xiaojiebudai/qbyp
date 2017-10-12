package cn.net.chenbao.qbyp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;

import com.karics.library.zxing.android.CaptureActivity;

import cn.net.chenbao.qbyp.R;
import cn.net.chenbao.qbyp.WWApplication;
import cn.net.chenbao.qbyp.activity.AddAddressActivity;
import cn.net.chenbao.qbyp.activity.AddBankActivity;
import cn.net.chenbao.qbyp.activity.AddressListActivity;
import cn.net.chenbao.qbyp.activity.AgencyDetailActivity;
import cn.net.chenbao.qbyp.activity.AgencyMoneyDayDetailActivity;
import cn.net.chenbao.qbyp.activity.AgencyMoneyDetailActivity;
import cn.net.chenbao.qbyp.activity.AgencyWithdrawActivity;
import cn.net.chenbao.qbyp.activity.BankListActivity;
import cn.net.chenbao.qbyp.activity.CategoryActivity;
import cn.net.chenbao.qbyp.activity.EditPswActivity;
import cn.net.chenbao.qbyp.activity.EmptyViewActivity;
import cn.net.chenbao.qbyp.activity.IdentityAuthenticationActivity;
import cn.net.chenbao.qbyp.activity.IdentityAuthenticationResultActivity;
import cn.net.chenbao.qbyp.activity.ImageSelectActivity;
import cn.net.chenbao.qbyp.activity.LocalLifeOrderActivity;
import cn.net.chenbao.qbyp.activity.LocateActivityNew;
import cn.net.chenbao.qbyp.activity.LoginActivity;
import cn.net.chenbao.qbyp.activity.MainActivity;
import cn.net.chenbao.qbyp.activity.MakeSuerOrderActivity;
import cn.net.chenbao.qbyp.activity.OrderDetailActivity;
import cn.net.chenbao.qbyp.activity.PayFeeSuccessActivity;
import cn.net.chenbao.qbyp.activity.PayOrdersActivity;
import cn.net.chenbao.qbyp.activity.PayResultActivity;
import cn.net.chenbao.qbyp.activity.PersonPublicListDesActivity;
import cn.net.chenbao.qbyp.activity.RechargeResultActivity;
import cn.net.chenbao.qbyp.activity.RegionPickActivity;
import cn.net.chenbao.qbyp.activity.SearchActiivty;
import cn.net.chenbao.qbyp.activity.SearchDataActiivty;
import cn.net.chenbao.qbyp.activity.SelfSupportFollowDeliverActivity;
import cn.net.chenbao.qbyp.activity.SelfSupportGoodsDetailActivity;
import cn.net.chenbao.qbyp.activity.SelfSupportMainActivity;
import cn.net.chenbao.qbyp.activity.SelfSupportOrderActivity2;
import cn.net.chenbao.qbyp.activity.SelfSupportOrderDetailActivity;
import cn.net.chenbao.qbyp.activity.SelfSupportOrderEvaluateActivity;
import cn.net.chenbao.qbyp.activity.SelfSupportSearchDataActivity;
import cn.net.chenbao.qbyp.activity.SelfSupportShopCartActivity;
import cn.net.chenbao.qbyp.activity.SetPasswordActivity;
import cn.net.chenbao.qbyp.activity.ShopCommentActivity;
import cn.net.chenbao.qbyp.activity.StoreActivity;
import cn.net.chenbao.qbyp.activity.VerifyPasswordActivity;
import cn.net.chenbao.qbyp.activity.VerifyPhoneNumberActivity;
import cn.net.chenbao.qbyp.activity.WebViewActivity;
import cn.net.chenbao.qbyp.activity.WithdrawActivity;
import cn.net.chenbao.qbyp.bigimage.ImagePagerActivity;
import cn.net.chenbao.qbyp.distribution.activity.DistributionGoodDetailActivity;
import cn.net.chenbao.qbyp.distribution.activity.DistributionMakeSureOrderActivity;
import cn.net.chenbao.qbyp.distribution.activity.DistributionOfflinePayActivity;
import cn.net.chenbao.qbyp.distribution.activity.DistributionOrderActivity;
import cn.net.chenbao.qbyp.distribution.activity.DistributionOrderDetailActivity;
import cn.net.chenbao.qbyp.distribution.activity.DistributionPublicListActivity;
import cn.net.chenbao.qbyp.distribution.activity.DistributionSendPackageActivity;
import cn.net.chenbao.qbyp.imageSelector.MultiImageSelectorActivity;
import cn.net.chenbao.qbyp.view.ActionItem;
import cn.net.chenbao.qbyp.view.TitlePopup;

import java.util.ArrayList;
import java.util.List;

/**
 * 开启界面类
 *
 * @author xl
 * @date:2016-7-27下午5:04:46
 * @description
 */
public class PublicWay {

    /**
     * 搜索界面
     */
    public static void startSearchActivity(Activity act, int model, int requestCode) {
        Intent intent = new Intent(act, SearchActiivty.class);
        intent.putExtra(Consts.KEY_MODULE, model);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 搜索结果界面
     */
    public static void startSearchResultActivity(Activity act, String keyWord,
                                                 int requestCode, double latitude, double longitude, int mode) {
        Intent intent = new Intent(act, SearchDataActiivty.class);
        if (mode == SearchDataActiivty.MORE_MODE) {// 更多
            intent.putExtra(Consts.KEY_DATA, keyWord);
        } else if (mode == SearchDataActiivty.SEARCH_MODE) {// 搜索结果
            intent.putExtra(Consts.KEY_DATA, keyWord);
        }
        intent.putExtra(Consts.KEY_MODULE, mode);
        intent.putExtra(Consts.LATITUDE, latitude);
        intent.putExtra(Consts.LONGITUDE, longitude);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 商铺界面
     */
    public static void startStoreActivity(Activity act, long sellerId,
                                          int requestCode) {
        Intent intent = new Intent(act, StoreActivity.class);
        intent.putExtra(Consts.KEY_DATA, sellerId);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 商铺界面
     */
    public static void startStoreActivity(Fragment act, long sellerId,
                                          int requestCode) {
        Intent intent = new Intent(act.getActivity(), StoreActivity.class);
        intent.putExtra(Consts.KEY_DATA, sellerId);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 商铺界面
     */
    public static void startStoreActivity(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), StoreActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 验证数据到修改密码
     */
    public static void startSetPasswordActivity(Activity act, String code,
                                                String phone, int mode) {
        Intent intent = new Intent(act, SetPasswordActivity.class);
        intent.putExtra(Consts.KEY_MODULE, mode);
        if (mode == SetPasswordActivity.LOGIN) {
            intent.putExtra(SetPasswordActivity.DATA_CODE, code);
            intent.putExtra(SetPasswordActivity.DATA_PHONE, phone);
        } else {
            intent.putExtra(SetPasswordActivity.DATA_CODE, code);
        }
        act.startActivity(intent);
    }

    /**
     * 注册带数据到登陆
     */
    public static void startLoginActivity(Activity act, String phone, String psw) {
        Intent intent = new Intent(act, LoginActivity.class);
        intent.putExtra(LoginActivity.DATA_PHONE, phone);
        intent.putExtra(LoginActivity.DATA_PSW, psw);
        act.startActivity(intent);
    }

    /**
     * 定位界面
     */
    public static void startLocateActivity(Fragment act, int requestCode,
                                           String data) {
        Intent intent = new Intent(act.getActivity(), LocateActivityNew.class);
        intent.putExtra(Consts.KEY_DATA, data);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 定位界面
     */
    public static void startLocateActivity(Activity act, int requestCode,
                                           String data) {
        Intent intent = new Intent(act, LocateActivityNew.class);
        intent.putExtra(Consts.KEY_DATA, data);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 个人中心数据列表界面
     */
    public static void startPersonPublicListDesActivity(Activity act, int model) {
        Intent intent = new Intent(act, PersonPublicListDesActivity.class);
        intent.putExtra(Consts.KEY_MODULE, model);
        act.startActivity(intent);
    }

    /**
     * 分销中心数据列表界面
     */
    public static void startDistributionPublicListActivity(Activity act, int model) {
        Intent intent = new Intent(act, DistributionPublicListActivity.class);
        intent.putExtra(Consts.KEY_MODULE, model);
        act.startActivity(intent);
    }

    /**
     * 分销中心数据列表界面
     */
    public static void startDistributionPublicListActivity(Activity act, int model, String data) {
        Intent intent = new Intent(act, DistributionPublicListActivity.class);
        intent.putExtra(Consts.KEY_MODULE, model);
        intent.putExtra(Consts.KEY_DATA, data);
        act.startActivity(intent);
    }

    /**
     * 大图界面
     */
    public static void startBigImagesActivity(Context mContext,
                                              ArrayList<String> photoUrlList, int index) {
        // Intent intent = new Intent(mContext, BigImagesActivity.class);
        // intent.putExtra(BigImagesActivity.EXTRA_IMAGE_URLS, photoUrlList);
        // intent.putExtra(BigImagesActivity.EXTRA_IMAGE_INDEX, index);
        // mContext.startActivity(intent);
    }

    /**
     * 开启图片选择界面
     *
     * @param act
     * @param number      选择数
     * @param reqeustCode selected多选模式下已经选过
     *                    执行裁剪              (暂支持number=1生效)
     * @author xl
     * @description
     */
    public static void startSelectImageActivity(Activity act, int number,
                                                ArrayList<String> selected, boolean showCamera, boolean doCrop,
                                                int reqeustCode) {
        Intent intent = new Intent(act, MultiImageSelectorActivity.class);

        intent.putExtra(MultiImageSelectorActivity.EXTRA_DO_CROP,
                number == 1 && doCrop);// 是否执行裁剪
        // 是否显示可以拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA,
                showCamera);

        if (number > 1) {
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
                    MultiImageSelectorActivity.MODE_MULTI);
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT,
                    number);
            if (selected != null && selected.size() > 0) {
                intent.putExtra(
                        MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
                        selected);
            }
            // 多选模式
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
                    MultiImageSelectorActivity.MODE_MULTI);
        } else {
            // 单选模式
            intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
                    MultiImageSelectorActivity.MODE_SINGLE);
        }
        act.startActivityForResult(intent, reqeustCode);
    }

    /**
     * 开启扫码界面
     *
     * @author xl
     * @description 结果通过Consts.DECODED_CONTENT_KEY和Consts.DECODED_BITMAP_KEY获得
     */
    public static void startCaptureActivityForResult(Activity context,
                                                     int requestCode) {
        Intent intent = new Intent(context, CaptureActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 开启扫码界面
     *
     * @author xl
     * @description 结果通过Consts.DECODED_CONTENT_KEY和Consts.DECODED_BITMAP_KEY获得
     */
    public static void startCaptureActivityForResult(Fragment fragment,
                                                     int requestCode) {
        Intent intent = new Intent(fragment.getActivity(),
                CaptureActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 选择图片
     *
     * @param fragment
     * @param requestCode
     * @param doCrop      裁剪
     * @param mode        模式
     * @author xl
     * @date:2016-8-2下午4:05:51
     * @description 支持单图选择模式
     */
    public static void startImageSelectActivity(Fragment fragment,
                                                int requestCode, boolean doCrop, boolean isSquare, boolean isIdCard, int mode) {
        Intent intent = new Intent(fragment.getActivity(),
                ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.KEY_MODE, mode);
        intent.putExtra(ImageSelectActivity.KEY_DO_CROP, doCrop);
        intent.putExtra(ImageSelectActivity.KEY_ISIDCARD, isIdCard);
        intent.putExtra(ImageSelectActivity.KEY_IS_SQUARE, isSquare);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * 选择图片
     *
     * @param requestCode
     * @param doCrop      裁剪
     * @param mode        模式
     * @author xl
     * @date:2016-8-2下午4:06:03
     * @description 支持单图选择模式
     */
    public static void startImageSelectActivity(Activity act, int requestCode,
                                                boolean doCrop, boolean isSquare, boolean isIdCard, int mode) {
        Intent intent = new Intent(act, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.KEY_MODE, mode);
        intent.putExtra(ImageSelectActivity.KEY_DO_CROP, doCrop);
        intent.putExtra(ImageSelectActivity.KEY_ISIDCARD, isIdCard);
        intent.putExtra(ImageSelectActivity.KEY_IS_SQUARE, isSquare);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 修改密码
     *
     * @param act
     * @param mode
     */
    public static void startEditPswActivity(Activity act, int mode) {
        Intent intent = new Intent(act, EditPswActivity.class);
        intent.putExtra(Consts.KEY_MODULE, mode);
        act.startActivity(intent);
    }

    /**
     * 新增和修改地址
     *
     * @param act
     * @param mode
     */
    public static void startAddAddressActivity(Activity act, int requestCode,
                                               int mode, String data) {
        Intent intent = new Intent(act, AddAddressActivity.class);
        intent.putExtra(Consts.KEY_MODULE, mode);
        intent.putExtra(Consts.KEY_DATA, data);
        if (data != null) {
            intent.putExtra(Consts.KEY_DATA, data);
        }
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * <<<<<<< HEAD 跳转更多界面
     *
     * @param act
     * @param latitude
     * @param Longitude
     */
    public static void startCategoryActivity(Activity act, double latitude,
                                             double Longitude) {
        Intent intent = new Intent(act, CategoryActivity.class);
        intent.putExtra(Consts.LATITUDE, latitude);
        intent.putExtra(Consts.LONGITUDE, Longitude);
        act.startActivity(intent);
    }

    public static void startAddBankActivity(Activity act, int requestCode,
                                            int mode, String data) {
        Intent intent = new Intent(act, AddBankActivity.class);
        intent.putExtra(Consts.KEY_MODULE, mode);
        if (data != null) {
            intent.putExtra(Consts.KEY_DATA, data);
        }
        act.startActivityForResult(intent, requestCode);
    }

    public static void startAddBankActivity(Fragment act, int requestCode,
                                            int mode, String data) {
        Intent intent = new Intent(act.getActivity(), AddBankActivity.class);
        intent.putExtra(Consts.KEY_MODULE, mode);
        if (data != null) {
            intent.putExtra(Consts.KEY_DATA, data);
        }
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转订单详情页
     *
     * @param act
     */
    public static void startOrderDetailActivity(Activity act, long orderId) {
        Intent intent = new Intent(act, OrderDetailActivity.class);
        intent.putExtra("orderId", orderId);
        act.startActivity(intent);
    }

    /**
     * 跳转订单详情页
     *
     * @param act
     */
    public static void startOrderDetailActivity(Activity act, long orderId, int requestCode) {
        Intent intent = new Intent(act, OrderDetailActivity.class);
        intent.putExtra("orderId", orderId);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 带刷新业务
     *
     * @param act
     * @param orderId
     * @param requestCode
     */
    public static void startOrderDetailActivity(Fragment act,
                                                long orderId, int requestCode) {
        Intent intent = new Intent(act.getActivity(), OrderDetailActivity.class);
        intent.putExtra("orderId", orderId);
        if (act.getParentFragment() != null) {
            act.getParentFragment().startActivityForResult(intent, requestCode);
        } else {
            act.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * @param act         银行卡列表
     * @param requestCode
     * @param mode
     */
    public static void startBankListActivity(Activity act, int requestCode,
                                             int mode) {
        Intent intent = new Intent(act, BankListActivity.class);
        intent.putExtra(Consts.KEY_MODULE, mode);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * @param act         银行卡列表
     * @param requestCode
     * @param mode
     */
    public static void startBankListActivity(Fragment act, int requestCode,
                                             int mode) {
        Intent intent = new Intent(act.getActivity(), BankListActivity.class);
        intent.putExtra(Consts.KEY_MODULE, mode);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * @param act         地址列表列表
     * @param requestCode
     * @param mode
     */
    public static void startAddressListActivity(Activity act, int requestCode,
                                                int mode) {
        Intent intent = new Intent(act, AddressListActivity.class);
        intent.putExtra(Consts.KEY_MODULE, mode);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * @param act  找回密码
     * @param mode
     */
    public static void startVerifyPhoneNumberActivity(Activity act, int mode,
                                                      String phoneNum, int requestCode) {
        Intent intent = new Intent(act, VerifyPhoneNumberActivity.class);
        if (mode == VerifyPhoneNumberActivity.PAY) {
            intent.putExtra(Consts.KEY_DATA, phoneNum);
        }
        intent.putExtra(Consts.KEY_MODULE, mode);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * @param act  找回密码
     * @param mode
     */
    public static void startVerifyPhoneNumberActivity(Fragment act, int mode,
                                                      String phoneNum, int requestCode) {
        Intent intent = new Intent(act.getActivity(), VerifyPhoneNumberActivity.class);
        if (mode == VerifyPhoneNumberActivity.PAY) {
            intent.putExtra(Consts.KEY_DATA, phoneNum);
        }
        intent.putExtra(Consts.KEY_MODULE, mode);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * @param act         选择地址
     * @param requestCode
     */
    public static void startRegionPickActivity(Activity act, int requestCode, int mode) {
        Intent intent = new Intent(act, RegionPickActivity.class);
        intent.putExtra(Consts.KEY_MODULE, mode);
        act.startActivityForResult(intent, requestCode);
    }

    public static void startMakeSureOrderActivity(Activity act, long sellerId,
                                                  int requestCode, String json, String data) {
        Intent intent = new Intent(act, MakeSuerOrderActivity.class);
        intent.putExtra(Consts.SELLER_ID, sellerId);
        intent.putExtra("json", json);
        intent.putExtra(Consts.KEY_DATA, data);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转支付页面
     *
     * @param act
     * @param requestCode
     */
    public static void startPayOrderActivity(Activity act, double money,
                                             long orderId, int requestCode, int model) {
        Intent intent = new Intent(act, PayOrdersActivity.class);
        intent.putExtra("money", money);
        intent.putExtra("orderId", orderId);
        intent.putExtra(Consts.KEY_MODULE, model);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转支付页面
     *
     * @param act
     * @param requestCode
     */
    public static void startPayOrderActivity(Fragment act, double money,
                                             long orderId, int requestCode, int model) {
        Intent intent = new Intent(act.getActivity(), PayOrdersActivity.class);
        intent.putExtra("money", money);
        intent.putExtra("orderId", orderId);
        intent.putExtra(Consts.KEY_MODULE, model);
        if (act.getParentFragment() != null) {
            act.getParentFragment().startActivityForResult(intent, requestCode);
        } else {
            act.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 跳转支付页面
     *
     * @param act
     * @param requestCode
     */
    public static void startPayOrderActivity(Activity act, double money,
                                             long[] orderIds, int requestCode, int model) {
        Intent intent = new Intent(act, PayOrdersActivity.class);
        intent.putExtra("money", money);
        intent.putExtra("orderId", orderIds);
        intent.putExtra(Consts.KEY_MODULE, model);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转支付页面
     *
     * @param act
     * @param requestCode
     */
    public static void startPayOrderActivity(Fragment act, double money,
                                             long[] orderIds, int requestCode, int model) {
        Intent intent = new Intent(act.getActivity(), PayOrdersActivity.class);
        intent.putExtra("money", money);
        intent.putExtra("orderId", orderIds);
        intent.putExtra(Consts.KEY_MODULE, model);
        if (act.getParentFragment() != null) {
            act.getParentFragment().startActivityForResult(intent, requestCode);
        } else {
            act.startActivityForResult(intent, requestCode);
        }

    }

    /**
     * 跳转内部支付弹窗
     *
     * @param act
     * @param mode
     * @param money
     * @param requestCode
     */
    public static void startVerifyPasswordActivity(Activity act, int mode,
                                                   String money, long cardId, String payWay, long orderId,
                                                   int requestCode) {
        Intent intent = new Intent(act, VerifyPasswordActivity.class);
        intent.putExtra(Consts.KEY_MODULE, mode);
        intent.putExtra("withdraw_count", money);
        if (mode == VerifyPasswordActivity.MODE_WITH_DRAW || mode == VerifyPasswordActivity.MODE_WITH_DRAW_DISTRIBUTION) {
            intent.putExtra("card_id", cardId);
        } else if (mode == VerifyPasswordActivity.MODE_ORDER_PAY) {
            intent.putExtra("payway", payWay);
            intent.putExtra("orderId", orderId);
        } else if (mode == VerifyPasswordActivity.MODE_WITH_DRAW_AGENCY) {
            intent.putExtra("agentId", orderId);
            intent.putExtra("card_id", cardId);
        }
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转内部支付弹窗
     *
     * @param act
     * @param mode
     * @param money
     * @param requestCode
     */
    public static void startVerifyPasswordActivity(Fragment act, int mode,
                                                   String money, long cardId, String payWay, long orderId,
                                                   int requestCode) {
        Intent intent = new Intent(act.getActivity(), VerifyPasswordActivity.class);
        intent.putExtra(Consts.KEY_MODULE, mode);
        intent.putExtra("withdraw_count", money);
        if (mode == VerifyPasswordActivity.MODE_WITH_DRAW) {
            intent.putExtra("card_id", cardId);
        } else if (mode == VerifyPasswordActivity.MODE_ORDER_PAY) {
            intent.putExtra("payway", payWay);
            intent.putExtra("orderId", orderId);
        } else if (mode == VerifyPasswordActivity.MODE_WITH_DRAW_AGENCY) {
            intent.putExtra("agentId", orderId);
            intent.putExtra("card_id", cardId);
        }
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开网页
     */
    public static void startWebViewActivity(Activity act, String title,
                                            String webHtml, int model) {
        Intent intent = new Intent(act, WebViewActivity.class);
        intent.putExtra(Consts.KEY_DATA, webHtml);
        intent.putExtra(Consts.TITLE, title);
        intent.putExtra(Consts.KEY_MODULE, model);
        act.startActivity(intent);
    }

    /**
     * 打开网页
     */
    public static void startWebViewActivity(Activity act, int titleRes,
                                            String webHtml, int model) {
        Intent intent = new Intent(act, WebViewActivity.class);
        intent.putExtra(Consts.KEY_DATA, webHtml);
        intent.putExtra(Consts.TITLE, act.getString(titleRes));
        intent.putExtra(Consts.KEY_MODULE, model);
        act.startActivity(intent);
    }

    /**
     * 打开网页
     */
    public static void startWebViewActivityForResult(Activity act,
                                                     String title, String webHtml, int model, int requestCode) {
        Intent intent = new Intent(act, WebViewActivity.class);
        intent.putExtra(Consts.KEY_DATA, webHtml);
        intent.putExtra(Consts.TITLE, title);
        intent.putExtra(Consts.KEY_MODULE, model);
        act.startActivityForResult(intent, requestCode);
    }


    /**
     * 到评价页面
     *
     * @param act
     * @param orderId
     */
    public static void startShopCommentActivity(Activity act, long orderId) {
        Intent intent = new Intent(act, ShopCommentActivity.class);
        intent.putExtra("orderId", orderId);
        act.startActivity(intent);
    }

    public static void startBigImageActivity(Activity act, String url) {
        String datas[] = new String[]{url};
        Intent intent = new Intent(act, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, datas);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
        act.startActivity(intent);
    }

    public static void startBigImageActivity(Activity act, List<String> urls,
                                             int postion) {
        String[] datas = urls.toArray(new String[0]);
        Intent intent = new Intent(act, ImagePagerActivity.class);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, datas);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, postion);
        act.startActivity(intent);
    }

    /**
     * 认证结果
     *
     * @param model
     */
    public static void startIdentityAuthenticationResultActivity(Activity act,
                                                                 int model, String data) {
        Intent intent = new Intent(act,
                IdentityAuthenticationResultActivity.class);
        intent.putExtra(Consts.KEY_MODULE, model);
        intent.putExtra(Consts.KEY_DATA, data);
        act.startActivity(intent);
    }

    /**
     * 支付結果
     *
     * @param act
     * @param mode
     * @param orderId
     * @param money
     * @param requestCode
     */
    public static void stratPayResultActivity(Activity act, int mode,
                                              long orderId, String money, int requestCode, int goodType) {
        Intent intent = new Intent(act, PayResultActivity.class);
        if (mode == PayResultActivity.SUCCESS) {
            intent.putExtra("orderId", orderId);
            intent.putExtra("money", money);
        }
        intent.putExtra(Consts.KEY_MODULE, mode);
        intent.putExtra(PayResultActivity.GOODTYPE, goodType);
        act.startActivityForResult(intent, requestCode);
    }

    /***
     * Description:提现明细和还款明细 Company: jsh Version：1.0
     *
     * @param act
     * @param mode
     * @author ZXJ
     * @date 2016-10-11
     ***/
    public static void stratAgencyWithdrawActivity(Fragment act, int mode,
                                                   int agentId) {
        Intent intent = new Intent(act.getActivity(),
                AgencyWithdrawActivity.class);
        intent.putExtra(Consts.KEY_MODULE, mode);
        intent.putExtra(Consts.AGENT_ID, agentId);
        act.startActivity(intent);
    }

    /***
     * Description:提现明细和还款明细 Company: jsh Version：1.0
     *
     * @param act
     * @param mode
     * @author ZXJ
     * @date 2016-10-11
     ***/
    public static void stratAgencyWithdrawActivity(Activity act, int mode,
                                                   int agentId) {
        Intent intent = new Intent(act, AgencyWithdrawActivity.class);
        intent.putExtra(Consts.KEY_MODULE, mode);
        if (mode != AgencyWithdrawActivity.WITHDRAW) {
            intent.putExtra(Consts.AGENT_ID, agentId);
        }
        act.startActivity(intent);
    }

    /***
     * Description:代理日明细 Company: jsh Version：1.0
     *
     * @param act
     * @author ZXJ
     * @date 2016-10-11
     ***/
    public static void stratAgencyMoneyDayDetailActivity(Activity act,
                                                         int agentId, String date) {
        Intent intent = new Intent(act, AgencyMoneyDayDetailActivity.class);
        intent.putExtra(Consts.AGENT_ID, agentId);
        intent.putExtra(Consts.KEY_DATA, date);
        act.startActivity(intent);
    }

    /***
     * Description:代理明细 Company: jsh Version：1.0
     *
     * @param act
     * @author ZXJ
     * @date 2016-10-11
     ***/
    public static void stratAgencyMoneyDetailActivity(Activity act,
                                                      int agentId) {
        Intent intent = new Intent(act, AgencyMoneyDetailActivity.class);
        intent.putExtra(Consts.AGENT_ID, agentId);
        act.startActivity(intent);
    }

    /***
     * Description:区域信息 Company: jsh Version：1.0
     *
     * @param act
     * @author ZXJ
     * @date 2016-10-11
     ***/
    public static void stratAgencyDetailActivity(Activity act,
                                                 int agentId) {
        Intent intent = new Intent(act, AgencyDetailActivity.class);
        intent.putExtra(Consts.AGENT_ID, agentId);
        act.startActivity(intent);
    }

    /***
     * Description: 认证
     *
     * @param act
     * @author ZXJ
     * @date 2016-10-13
     ***/
    public static void stratIdentityAuthenticationActivity(Activity act) {
        Intent intent = new Intent(act, IdentityAuthenticationActivity.class);
        act.startActivity(intent);
    }

    /***
     * Description: 认证
     *
     * @param act
     * @author ZXJ
     * @date 2016-10-13
     ***/
    public static void stratIdentityAuthenticationActivity(Fragment act) {
        Intent intent = new Intent(act.getActivity(), IdentityAuthenticationActivity.class);
        act.startActivity(intent);
    }

    /***
     * Description: 自营商品搜索结果
     *
     * @param act
     * @author ZXJ
     ***/
    public static void stratSelfSupportSearchDataActivity(Activity act, String keyWord, String classId) {
        Intent intent = new Intent(act, SelfSupportSearchDataActivity.class);
        intent.putExtra(Consts.KEY_DATA, keyWord);
        intent.putExtra(SelfSupportSearchDataActivity.KEY_CLASS, classId);
        act.startActivity(intent);
    }

    /***
     * Description: 自营购物车
     *
     * @param act
     * @author ZXJ
     ***/
    public static void stratSelfSupportShopCartActivity(Activity act) {
        if (WWApplication.getInstance().isLogin()) {
            Intent intent = new Intent(act, SelfSupportShopCartActivity.class);
            act.startActivity(intent);
        } else {
            Intent intent = new Intent(act, LoginActivity.class);
            intent.putExtra(LoginActivity.ISFRAOMMAIN, false);
            act.startActivity(intent);
        }
    }

    /***
     * Description: 自营购物车
     *
     * @param act
     * @author ZXJ
     ***/
    public static void stratSelfSupportShopCartActivity(Fragment act) {
        if (WWApplication.getInstance().isLogin()) {
            Intent intent = new Intent(act.getActivity(), SelfSupportShopCartActivity.class);
            act.startActivity(intent);
        } else {
            Intent intent = new Intent(act.getActivity(), LoginActivity.class);
            intent.putExtra(LoginActivity.ISFRAOMMAIN, false);
            act.startActivity(intent);
        }
    }

    /***
     * Description:  自营商品详情
     *
     * @param act
     * @author ZXJ
     ***/
    public static void stratSelfSupportGoodsDetailActivity(Activity act, long ProductId) {
        Intent intent = new Intent(act, SelfSupportGoodsDetailActivity.class);
        intent.putExtra(Consts.KEY_DATA, ProductId);
        act.startActivity(intent);
    }

    /***
     * Description: 自营商品详情
     *
     * @param act
     * @author ZXJ
     ***/
    public static void stratSelfSupportGoodsDetailActivity(Fragment act, long ProductId) {
        Intent intent = new Intent(act.getActivity(), SelfSupportGoodsDetailActivity.class);
        intent.putExtra(Consts.KEY_DATA, ProductId);
        act.startActivity(intent);
    }

    /***
     * Description:  自营订单详情
     *
     * @param act
     * @author ZXJ
     ***/
    public static void stratSelfSupportOrderDetailActivity(Activity act, long orderId, int requestCode) {
        Intent intent = new Intent(act, SelfSupportOrderDetailActivity.class);
        intent.putExtra(Consts.KEY_DATA, orderId);
        act.startActivityForResult(intent, requestCode);
    }

    /***
     * Description: 自营订单详情
     *
     * @param act
     * @author ZXJ
     ***/
    public static void stratSelfSupportOrderDetailActivity(Fragment act, long orderId, int requestCode) {
        Intent intent = new Intent(act.getActivity(), SelfSupportOrderDetailActivity.class);
        intent.putExtra(Consts.KEY_DATA, orderId);
        if (act.getParentFragment() != null) {
            act.getParentFragment().startActivityForResult(intent, requestCode);
        } else {
            act.startActivityForResult(intent, requestCode);
        }
    }

    /***
     * Description:  自营订单物流详情
     *
     * @param act
     * @author ZXJ
     ***/
    public static void stratSelfSupportFollowDeliverActivity(Activity act, long orderId, int model, int requestCode) {
        Intent intent = new Intent(act, SelfSupportFollowDeliverActivity.class);
        intent.putExtra(Consts.KEY_DATA, orderId);
        intent.putExtra(Consts.KEY_MODULE, model);
        act.startActivityForResult(intent, requestCode);
    }

    /***
     * Description: 自营订单物流详情
     *
     * @param act
     * @author ZXJ
     ***/
    public static void stratSelfSupportFollowDeliverActivity(Fragment act, long orderId, int model, int requestCode) {
        Intent intent = new Intent(act.getActivity(), SelfSupportFollowDeliverActivity.class);
        intent.putExtra(Consts.KEY_DATA, orderId);
        intent.putExtra(Consts.KEY_MODULE, model);
        if (act.getParentFragment() != null) {
            act.getParentFragment().startActivityForResult(intent, requestCode);
        } else {
            act.startActivityForResult(intent, requestCode);
        }
    }

    /***
     * Description:  自营订单评价
     *
     * @param act
     * @author ZXJ
     ***/
    public static void stratSelfSupportOrderEvaluateActivity(Activity act, long orderId, String imgUrl, long finishTime, int requestCode) {
        Intent intent = new Intent(act, SelfSupportOrderEvaluateActivity.class);
        intent.putExtra(Consts.KEY_DATA, orderId);
        intent.putExtra(SelfSupportOrderEvaluateActivity.KEY_IMGURL, imgUrl);
        intent.putExtra(SelfSupportOrderEvaluateActivity.KEY_TIME, finishTime);
        act.startActivityForResult(intent, requestCode);
    }

    /***
     * Description: 自营订单评价
     *
     * @param act
     * @author ZXJ
     ***/
    public static void stratSelfSupportOrderEvaluateActivity(Fragment act, long orderId, String imgUrl, long finishTime, int requestCode) {
        Intent intent = new Intent(act.getActivity(), SelfSupportOrderEvaluateActivity.class);
        intent.putExtra(Consts.KEY_DATA, orderId);
        intent.putExtra(SelfSupportOrderEvaluateActivity.KEY_IMGURL, imgUrl);
        intent.putExtra(SelfSupportOrderEvaluateActivity.KEY_TIME, finishTime);
        if (act.getParentFragment() != null) {
            act.getParentFragment().startActivityForResult(intent, requestCode);
        } else {
            act.startActivityForResult(intent, requestCode);
        }
    }

    /***
     * Description: 跳转自营首页各个fragment
     *
     * @param act
     * @author ZXJ
     ***/
    public static void startSelfSupportMainActivity(Activity act, int tab) {
        Intent intent = new Intent(act, SelfSupportMainActivity.class);
        intent.putExtra(Consts.KEY_DATA, tab);
        act.startActivity(intent);
    }

    /***
     * Description: 跳转首页各个fragment
     *
     * @param act
     * @author ZXJ
     ***/
    public static void startMainActivity(Activity act, int tab) {
        Intent intent = new Intent(act, MainActivity.class);
        intent.putExtra(Consts.KEY_DATA, tab);
        act.startActivity(intent);
    }

    /**
     * Description: 通用模块功能pop
     */
    public static TitlePopup startCommonFunctionPop(final Context context) {
        TitlePopup popup = new TitlePopup(context);
        ArrayList<ActionItem> mActionItems = new ArrayList<ActionItem>();
        mActionItems.add(new ActionItem(context, context.getString(R.string.home_page), R.drawable.top_menu_index_icon));
        mActionItems.add(new ActionItem(context, context.getString(R.string.local_search), R.drawable.top_menu_search_icon));
        mActionItems.add(new ActionItem(context, context.getString(R.string.person_center), R.drawable.top_menu_user_icon));
        popup.setData(mActionItems);
        popup.setItemOnClickListener(new TitlePopup.OnItemOnClickListener() {
            @Override
            public void onItemClick(ActionItem item, int position) {
                switch (position) {
                    case 0:
                        context.startActivity(new Intent(context, MainActivity.class));
                        break;
                    case 1:
                        PublicWay.startSearchActivity((Activity) context, SearchActiivty.LOCATION, 0);
                        break;

                    case 2:
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("personCenterSelect", true);
                        context.startActivity(intent);
                        break;

                }
            }
        });
        return popup;
    }

    /**
     * Description: 个人中心 fragment 根据条件跳转到自营订单界面
     */
    public static void startSelfSupportOrderActivity(Fragment fragment, int status) {
        Intent intent = new Intent(fragment.getActivity(), SelfSupportOrderActivity2.class);
        intent.putExtra(Consts.KEY_DATA, status);
        fragment.getActivity().startActivity(intent);
    }

    /**
     * Description: 个人中心 fragment 根据条件跳转到本地订单界面
     */
    public static void startLocalLifeOrderActivity(Fragment fragment, int status) {
        Intent intent = new Intent(fragment.getActivity(), LocalLifeOrderActivity.class);
        intent.putExtra(Consts.KEY_DATA, status);
        fragment.getActivity().startActivity(intent);
    }

    /***
     * Description: 充值结果页面
     *
     * @param act
     * @author ZXJ
     ***/
    public static void stratRechargeResultActivity(Activity act, int model, int requestCode) {
        Intent intent = new Intent(act, RechargeResultActivity.class);
        intent.putExtra(Consts.KEY_MODULE, model);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * Description:服务费充值
     *
     * @param act
     * @param model
     */
    public static void stratPayFeeActivity(Activity act, int model, long orderId, double money) {
        Intent intent = new Intent(act, PayFeeSuccessActivity.class);
        intent.putExtra(Consts.KEY_MODULE, model);
        intent.putExtra("orderId", orderId);
        intent.putExtra("money", money);
        act.startActivity(intent);
    }

    /**
     * Description:提现
     *
     * @param act
     * @param model
     */
    public static void stratWithdrawActivity(Activity act, double withdraw_limit, int model, int requestCode) {
        Intent intent = new Intent(act, WithdrawActivity.class);
        intent.putExtra(Consts.KEY_MODULE, model);
        intent.putExtra(Consts.KEY_DATA, withdraw_limit);
        act.startActivityForResult(intent, requestCode);
    }

    /**
     * @throws Exception
     * @return获取版本信息
     */
    public static int getVersionCode(Context ctx) {
        // 获取packagemanager的实例
        PackageManager packageManager = ctx.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        int versioncode = 0;
        try {
            packInfo = packageManager.getPackageInfo(ctx.getPackageName(),
                    0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        versioncode = packInfo.versionCode;
        return versioncode;
    }

    /***
     * Description:  分销商品详情
     *
     * @param act
     * @author wuri
     ***/
    public static void startDistributionGoodsDetailActivity(Activity act, long productId) {
        Intent intent = new Intent(act, DistributionGoodDetailActivity.class);
        intent.putExtra(Consts.KEY_DATA, productId);
        act.startActivity(intent);
    }

    /***
     * Description:  分销确认订单
     *
     * @param act
     * @author wuri
     ***/
    public static void startDistributionMakeSureOrderActivity(Activity act, String json) {
        Intent intent = new Intent(act, DistributionMakeSureOrderActivity.class);
        intent.putExtra(Consts.KEY_DATA, json);
        act.startActivity(intent);
    }

    /***
     * Description:  分销订单详情
     *
     * @author wuri
     ***/
    public static void startDistributionOrderDetailActivity(Activity act, long orderId) {
        Intent intent = new Intent(act, DistributionOrderDetailActivity.class);
        intent.putExtra(Consts.KEY_DATA, orderId);
        act.startActivity(intent);
    }

    /***
     * Description:  分销订单
     *
     * @param fragment
     * @author zxj
     ***/
    public static void startDistributionOrderActivity(Fragment fragment, int model) {
        Intent intent = new Intent(fragment.getActivity(), DistributionOrderActivity.class);
        intent.putExtra(Consts.KEY_MODULE, model);
        fragment.startActivity(intent);
    }

    /***
     * Description:  分销订单
     *
     * @param act
     * @author zxj
     ***/
    public static void startDistributionOrderActivity(Activity act, int model) {
        Intent intent = new Intent(act, DistributionOrderActivity.class);
        intent.putExtra(Consts.KEY_MODULE, model);
        act.startActivity(intent);
    }

    /***
     * Description:  分销线下支付上传凭证
     *
     * @param act
     * @author zxj
     ***/
    public static void startDistributionOfflinePayActivity(Activity act, long data, int requestCode) {
        Intent intent = new Intent(act, DistributionOfflinePayActivity.class);
        intent.putExtra(Consts.KEY_DATA, data);
        act.startActivityForResult(intent, requestCode);
    }
  public static void startDistributionOfflinePayActivity(Activity act, int model, long[] data, int requestCode) {
        Intent intent = new Intent(act, DistributionOfflinePayActivity.class);
      intent.putExtra(Consts.KEY_MODULE, model);
        intent.putExtra("orderId", data);
        act.startActivityForResult(intent, requestCode);
    }

    /***
     * Description:  发货
     *

     * @author zxj
     ***/
    public static void startDistributionSendPackageActivity(Fragment fragment, long orderId, double jifen, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), DistributionSendPackageActivity.class);
        intent.putExtra(Consts.KEY_DATA, orderId);
        intent.putExtra(Consts.KEY_MONEYCOUNT, jifen);
        fragment.startActivityForResult(intent, requestCode);
    }

    /***
     * Description:  发货
     *

     * @author zxj
     ***/
    public static void startDistributionSendPackageActivity(Activity act, long orderId, double jifen, int requestCode) {
        Intent intent = new Intent(act, DistributionSendPackageActivity.class);
        intent.putExtra(Consts.KEY_DATA, orderId);
        intent.putExtra(Consts.KEY_MONEYCOUNT, jifen);
        act.startActivityForResult(intent, requestCode);
    }

    /***
     * Description:  空页面
     *

     * @author zxj
     ***/
    public static void startEmptyViewActivity(Activity act, int model) {
        Intent intent = new Intent(act, EmptyViewActivity.class);
        intent.putExtra(Consts.KEY_MODULE, model);
        act.startActivity(intent);
    }

    /***
     * Description:  空页面
     *

     * @author zxj
     ***/
    public static void startEmptyViewActivity(Fragment fragment, int model) {
        Intent intent = new Intent(fragment.getActivity(), EmptyViewActivity.class);
        intent.putExtra(Consts.KEY_MODULE, model);
        fragment.startActivity(intent);
    }
}
