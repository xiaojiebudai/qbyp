package cn.net.chenbao.qbypseller.utils;

import java.util.ArrayList;
import java.util.List;

import cn.net.chenbao.qbypseller.activity.AddBankActivity;
import cn.net.chenbao.qbypseller.activity.AddCategoryActivity;
import cn.net.chenbao.qbypseller.activity.AddGoodsActivity;
import cn.net.chenbao.qbypseller.activity.BaseInfoActivity;
import cn.net.chenbao.qbypseller.activity.CheckActivity;
import cn.net.chenbao.qbypseller.activity.CommitInfoActivity;
import cn.net.chenbao.qbypseller.activity.EditShopPhoneActivity;
import cn.net.chenbao.qbypseller.activity.FindlLoginPasswordActivity;
import cn.net.chenbao.qbypseller.activity.GoodsManageActivity;
import cn.net.chenbao.qbypseller.activity.ImageSelectActivity;
import cn.net.chenbao.qbypseller.activity.LocateActivity;
import cn.net.chenbao.qbypseller.activity.OpenServerActivity;
import cn.net.chenbao.qbypseller.activity.OrderAcountActivity;
import cn.net.chenbao.qbypseller.activity.OrderDetailsActivity;
import cn.net.chenbao.qbypseller.activity.PersonPublicListDesActivity;
import cn.net.chenbao.qbypseller.activity.RegionPickActivity;
import cn.net.chenbao.qbypseller.activity.SelectCategoryActivity;
import cn.net.chenbao.qbypseller.activity.SelectGoodsActivity;
import cn.net.chenbao.qbypseller.activity.SetPasswordActivity;
import cn.net.chenbao.qbypseller.activity.StoreSettingActivity;
import cn.net.chenbao.qbypseller.activity.WebViewActivity;
import cn.net.chenbao.qbypseller.bean.Category;
import cn.net.chenbao.qbypseller.bean.Goods;
import cn.net.chenbao.qbypseller.bigimage.ImagePagerActivity;
import cn.net.chenbao.qbypseller.fragment.OrderFragment;
import cn.net.chenbao.qbypseller.imageSelector.MultiImageSelectorActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.karics.library.zxing.android.CaptureActivity;

/**
 * 开启界面类
 * 
 * @author xl
 * @date:2016-7-27下午5:04:46
 * @description
 */
public class PublicWay {

	/** 货品管理 */
	public static void startGoodsManageActivity(Activity act, int requestCode) {
		Intent intent = new Intent(act, GoodsManageActivity.class);
		act.startActivityForResult(intent, requestCode);
	}

	/** 添加商品 */
	public static void startAddGoodsActivity(Activity act, int requestCode,
			int module) {
		Intent intent = new Intent(act, AddGoodsActivity.class);
		intent.putExtra(Consts.KEY_MODULE, module);// 处理不同模块需要的不同业务
		act.startActivityForResult(intent, requestCode);
	}

	/** 添加商品 */
	public static void startAddGoodsActivity(Activity act, Category data,
			int requestCode) {
		Intent intent = new Intent(act, AddGoodsActivity.class);
		intent.putExtra(Consts.KET_CATEGORY, JSON.toJSONString(data));
		intent.putExtra(Consts.KEY_MODE, AddGoodsActivity.MODE_ADD);
		act.startActivityForResult(intent, requestCode);
	}

	/** 编辑商品 */
	public static void startAddGoodsActivity(Activity act, Category category,
                                             Goods data, int position, int requestCode) {
		Intent intent = new Intent(act, AddGoodsActivity.class);
		intent.putExtra(Consts.KET_CATEGORY, JSON.toJSONString(category));
		intent.putExtra(Consts.KEY_DATA, JSON.toJSONString(data));
		intent.putExtra(Consts.KEY_MODE, AddGoodsActivity.MODE_EDIT);
		act.startActivityForResult(intent, requestCode);
	}

	/** 添加分类 */
	public static void startAddCategoryActivity(Activity act, int requestCode) {
		Intent intent = new Intent(act, AddCategoryActivity.class);
		act.startActivityForResult(intent, requestCode);
	}

	/** 订单统计 */
	public static void startOrderAcountActivity(Activity act, int state,
			int requestCode) {
		Intent intent = new Intent(act, OrderAcountActivity.class);
		intent.putExtra(Consts.KEY_STATE, state);
		act.startActivityForResult(intent, requestCode);
	}

	/** 订单统计 */
	public static void startOrderAcountActivity(Fragment fragment, int state,
			int requestCode) {
		Intent intent = new Intent(fragment.getActivity(),
				OrderAcountActivity.class);
		intent.putExtra(Consts.KEY_STATE, state);
		fragment.startActivityForResult(intent, requestCode);
	}

	/** 个人中心数据列表界面 */
	public static void startPersonPublicListDesActivity(Fragment act,
			int model, int requestCode) {
		Intent intent = new Intent(act.getActivity(),
				PersonPublicListDesActivity.class);
		intent.putExtra(Consts.KEY_MODULE, model);
		act.startActivity(intent);
	}
	/** 个人中心数据列表界面 */
	public static void startPersonPublicListDesActivity(Activity act,
			int model, int requestCode) {
		Intent intent = new Intent(act,
				PersonPublicListDesActivity.class);
		intent.putExtra(Consts.KEY_MODULE, model);
		act.startActivity(intent);
	}

	/**
	 * 店铺设置
	 * 
	 * @param act
	 * @param module
	 *            模块
	 * @param requestCode
	 */
	public static void startStoreSettingActivity(Activity act, int module,
			int requestCode) {
		Intent intent = new Intent(act, StoreSettingActivity.class);
		intent.putExtra(Consts.KEY_MODULE, module);
		act.startActivityForResult(intent, requestCode);
	}

	/**
	 * 开启图片选择界面
	 * 
	 * @author xl
	 * @description
	 * @param act
	 * @param number
	 *            选择数
	 * @param reqeustCode
	 * @param selected 多选模式下已经选过
	 * @param doCrop 执行裁剪
	 *            (暂支持number=1生效)
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
	 * @author xl
	 * @date:2016-8-2下午4:05:51
	 * @description 支持单图选择模式
	 * @param fragment
	 * @param requestCode
	 * @param doCrop
	 *            裁剪
	 * @param mode
	 *            模式
	 */
	public static void startImageSelectActivity(Fragment fragment,
			int requestCode, boolean doCrop, boolean isSquare, int mode) {
		Intent intent = new Intent(fragment.getActivity(),
				ImageSelectActivity.class);
		intent.putExtra(ImageSelectActivity.KEY_MODE, mode);
		intent.putExtra(ImageSelectActivity.KEY_DO_CROP, doCrop);
		intent.putExtra(ImageSelectActivity.KEY_IS_SQUARE, isSquare);
		fragment.startActivityForResult(intent, requestCode);
	}

	/**
	 * 选择图片
	 * 
	 * @author xl
	 * @date:2016-8-2下午4:06:03
	 * @description 支持单图选择模式
	 * @param requestCode
	 * @param doCrop
	 *            裁剪
	 * @param mode
	 *            模式
	 */
	public static void startImageSelectActivity(Activity act, int requestCode,
			boolean doCrop, boolean isSquare, int mode) {
		Intent intent = new Intent(act, ImageSelectActivity.class);
		intent.putExtra(ImageSelectActivity.KEY_MODE, mode);
		intent.putExtra(ImageSelectActivity.KEY_DO_CROP, doCrop);
		intent.putExtra(ImageSelectActivity.KEY_IS_SQUARE, isSquare);
		act.startActivityForResult(intent, requestCode);
	}

	/**
	 * 跳转 基本信息界面
	 * 
	 * @param act
	 * @param sessionId
	 */
	public static void startBaseInfoActivity(Activity act, String sessionId) {
		Intent intent = new Intent(act, BaseInfoActivity.class);
		intent.putExtra(Consts.KEY_DATA, sessionId);
		act.startActivity(intent);
	}

	/**
	 * 跳转实名信息界面
	 * 
	 * @param act
	 * @param sessionId
	 */
	public static void startCommitInfoActivity(Activity act, String sessionId) {
		Intent intent = new Intent(act, CommitInfoActivity.class);
		intent.putExtra(Consts.KEY_DATA, sessionId);
		act.startActivity(intent);
	}

	/**
	 * 跳转开启服务界面
	 * 
	 * @param act
	 * @param sessionId
	 */
	public static void startOpenServerActivity(Activity act, String sessionId) {
		Intent intent = new Intent(act, OpenServerActivity.class);
		intent.putExtra(Consts.KEY_DATA, sessionId);
		act.startActivity(intent);

	}

	/**
	 * 修改密码或忘记密码
	 * 
	 * @param act
	 * @param mode
	 */
	public static void startSetPassWordActivity(Activity act, int mode,
			String code, String phone) {
		Intent intent = new Intent(act, SetPasswordActivity.class);
		intent.putExtra(Consts.KEY_MODULE, mode);
		if (mode == SetPasswordActivity.MODE_FOR_PAY_PSW
				|| mode == SetPasswordActivity.MODE_FOR_PSW) {// 忘记密码或支付密码需要传验证码
			intent.putExtra(SetPasswordActivity.DATA_CODE, code);
			intent.putExtra(SetPasswordActivity.DATA_PHONE, phone);
		}
		act.startActivity(intent);
	}

	/** 订单详情 */
	public static void startOrderDetailsActivity(Activity act, String orderId,
			int position, int requestCode) {
		Intent intent = new Intent(act, OrderDetailsActivity.class);
		intent.putExtra(Consts.KEY_DATA, orderId);
		intent.putExtra(Consts.KEY_POSITION, position);
		act.startActivityForResult(intent, requestCode);
	}

	/** 订单详情 */
	public static void startOrderDetailsActivity(OrderFragment fragment,
			String orderId, int position, int requestCode) {
		Intent intent = new Intent(fragment.getActivity(),
				OrderDetailsActivity.class);
		intent.putExtra(Consts.KEY_DATA, orderId);
		intent.putExtra(Consts.KEY_POSITION, position);
		fragment.startActivityForResult(intent, requestCode);
	}

	/**
	 * 找回登录密码/找回支付密码
	 * 
	 * @param act
	 * @param mode
	 */
	public static void startfindLoginPasswordAcitivity(Activity act, int mode,
			String phone) {
		Intent intent = new Intent(act, FindlLoginPasswordActivity.class);
		if (mode == FindlLoginPasswordActivity.MODE_PAY_PSW) {
			intent.putExtra(Consts.KEY_DATA, phone);
		}
		intent.putExtra(Consts.KEY_MODULE, mode);
		act.startActivity(intent);
	}

	/** 选择类目 */
	public static void startSelectCategoryActivity(Activity act, int requestCode) {
		Intent intent = new Intent(act, SelectCategoryActivity.class);
		act.startActivityForResult(intent, requestCode);
	}

	/** 添加编辑银行卡 */
	public static void startAddBankActivity(Activity act, int requestCode,
			int mode, String data) {
		Intent intent = new Intent(act, AddBankActivity.class);
		intent.putExtra(Consts.KEY_MODULE, mode);
		if (data != null) {
			intent.putExtra(Consts.KEY_DATA, data);
		}
		act.startActivityForResult(intent, requestCode);
	}

	/** 打开网页 */
	public static void startWebViewActivity(Activity act, String title,
			String webHtml,int model) {
		Intent intent = new Intent(act, WebViewActivity.class);
		intent.putExtra(Consts.KEY_DATA, webHtml);
		intent.putExtra(Consts.TITLE, title);
		intent.putExtra(Consts.KEY_MODULE, model);
		act.startActivity(intent);
	}
	/** 打开网页 */
	public static void startWebViewActivityForResult(Activity act, String title,
			String webHtml,int model,int flag,int requestCode) {
		Intent intent = new Intent(act, WebViewActivity.class);
		intent.putExtra(Consts.KEY_DATA, webHtml);
		intent.putExtra(Consts.TITLE, title);
		intent.putExtra(Consts.KEY_MODULE, model);
		intent.putExtra(Consts.KEY_FLAG, flag);
		act.startActivityForResult(intent, requestCode);
	}
	/** 商家审核结果页面 */
	public static void startCheckActivity(Activity act, int mode,
			String sessionId) {
		Intent intent = new Intent(act, CheckActivity.class);
		if (mode == CheckActivity.MODE_UNPASS) {
			intent.putExtra(Consts.KEY_DATA, sessionId);
		}
		intent.putExtra(Consts.KEY_DATA, sessionId);//补加的代码mode == CheckActivity.MODE_PASS时,需要用到sessionId,不然审核通过点击不了下一步
		intent.putExtra(Consts.KEY_MODULE, mode);
		act.startActivity(intent);
	}
	/** 商家审核结果页面 */
	public static void startCheckActivity(Activity act, int mode, int requestCode,
			String sessionId) {
		Intent intent = new Intent(act, CheckActivity.class);
		if (mode == CheckActivity.MODE_UNPASS) {
			intent.putExtra(Consts.KEY_DATA, sessionId);
		}
		intent.putExtra(Consts.KEY_DATA, sessionId);//补加的代码mode == CheckActivity.MODE_PASS时,需要用到sessionId,不然审核通过点击不了下一步
		intent.putExtra(Consts.KEY_MODULE, mode);
		act.startActivityForResult(intent, requestCode);
	}

	public static void startBigImageActivity(Activity act, String url) {
		String datas[] = new String[] { url };
		Intent intent = new Intent(act, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, datas);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, 0);
		act.startActivity(intent);
	}

	public static void startBigImageActivity(Activity act, List<String> urls,
			int postion) {
		String[] datas = (String[]) urls.toArray();
		Intent intent = new Intent(act, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, datas);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, postion);
		act.startActivity(intent);
	}

	/** 定位界面 */
	public static void startLocateActivity(Fragment act, int requestCode) {
		Intent intent = new Intent(act.getActivity(), LocateActivity.class);
		act.startActivityForResult(intent, requestCode);
	}

	/** 定位界面 */
	public static void startLocateActivity(Activity act, int requestCode) {
		Intent intent = new Intent(act, LocateActivity.class);
		act.startActivityForResult(intent, requestCode);
	}

	/**
	 * @throws Exception
	 * @return获取版本信息
	 */
	public static int getVersionCode(Context ctx){
		// 获取packagemanager的实例
		PackageManager packageManager = ctx.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		int versioncode =0;
		try {
			packInfo = packageManager.getPackageInfo(ctx.getPackageName(),
					0);
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		versioncode = packInfo.versionCode;
		return versioncode;
	}

	/**
	 * @param act         选择地址
	 * @param requestCode
	 */
	public static void startRegionPickActivity(Activity act, int requestCode) {
		Intent intent = new Intent(act, RegionPickActivity.class);
		act.startActivityForResult(intent, requestCode);
	}

	/**
	 * 修改店铺电话
	 * @param act
	 * @param phone
	 * @param requestCode
	 */
	public static void startEditShopPhoneActivity(Fragment act,String phone, int requestCode) {
		Intent intent = new Intent(act.getActivity(), EditShopPhoneActivity.class);
		intent.putExtra(Consts.KEY_DATA,phone);
		act.startActivityForResult(intent, requestCode);
	}
	/**
	 * 选择商品
	 * @param act
	 * @param data
	 * @param requestCode
	 */
	public static void startSelectGoodsActivity(Activity act,String data, int requestCode) {
		Intent intent = new Intent(act, SelectGoodsActivity.class);
		intent.putExtra(Consts.KEY_DATA,data);
		act.startActivityForResult(intent, requestCode);
	}
}
