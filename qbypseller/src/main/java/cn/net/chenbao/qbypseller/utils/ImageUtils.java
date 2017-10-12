package cn.net.chenbao.qbypseller.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.net.chenbao.qbypseller.R;

/**
 * 图片加载工具类
 * 
 * @author xl
 * @date:2016-7-28下午3:04:50
 * @description 使用Glide
 */
public class ImageUtils {

	/** 默认圆角 */
	private static final int DEFAULT_RADIUS = 5;

	/** 普通加载 */
	public static void setCommonImage(Context context, String url, ImageView iv) {
		Glide.with(context).load(url).placeholder(R.drawable.default_img).error(R.drawable.lodingfail).thumbnail(0.1F).dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop()
				.into(iv);
	}

	/**
	 * 普通加载
	 */
	public static void setCommonImage(Context context, String url, ImageView iv, int lodfailres) {
		Glide.with(context).load(url).placeholder(R.drawable.default_img).error(lodfailres).thumbnail(0.1F).dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop()
				.into(iv);
	}
	/** 普通加载，无默认图片 */
	public static void setCommonImageNo(Context context, String url, ImageView iv) {
		Glide.with(context).load(url).placeholder(R.drawable.default_img).thumbnail(0.1F).dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop()
				.into(iv);
	}

	/** 默认圆角加载 */
	public static void setCommonRadiusImage(Context context, String url,
			ImageView iv) {
		Glide.with(context).load(url).placeholder(R.drawable.default_img).dontAnimate().error(R.drawable.lodingfail)
				.transform(new GlideRoundTransform(context, DEFAULT_RADIUS))
				.into(iv);
	}

	/** 自定圆角加载 */
	public static void setOwnRadiusImage(Context context, String url,
			ImageView iv, int dp) {
		Glide.with(context).load(url).placeholder(R.drawable.default_img).dontAnimate().error(R.drawable.lodingfail)
				.transform(new GlideRoundTransform(context, dp)).into(iv);
	}

	/** 圆形加载 */
	public static void setCircleImage(Context context, String url, ImageView iv) {
		Glide.with(context).load(url).placeholder(R.drawable.default_img).dontAnimate().error(R.drawable.lodingfail)
				.transform(new GlideCircleTransform(context)).into(iv);
	}
	/** 圆形加载头像 */
	public static void setCircleHeaderImage(Context context, String url, ImageView iv) {
		Glide.with(context).load(url).placeholder(R.drawable.default_photo).dontAnimate().error(R.drawable.default_photo)
		.transform(new GlideCircleTransform(context)).into(iv);
	}

	/** 圆形加载头像 自定义尺寸 */
	public static void setCircleHeaderImage(Context context, String url, ImageView iv, int width, int height) {
		Glide.with(context).load(url.replace("__", "_" + width + "x" + height)).placeholder(R.drawable.default_photo).dontAnimate().error(R.drawable.default_photo)
				.transform(new GlideCircleTransform(context)).into(iv);
	}
	public static  String getRightImgScreen(String picUrl, int width, int height) {
		return picUrl.replace("__", "_" + width + "x" + height);
	}
}
