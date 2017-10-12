package cn.net.chenbao.qbyp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.xutils.common.util.FileUtil;

import java.io.File;

import cn.net.chenbao.qbyp.R;

/**
 * 图片加载工具类
 *
 * @author xl
 * @date:2016-7-28下午3:04:50
 * @description 使用Glide
 */
public class ImageUtils {

    /**
     * 默认圆角
     */
    private static final int DEFAULT_RADIUS = 5;

    /**
     * 普通加载
     */
    public static void setCommonImage(Context context, String url, ImageView iv) {
        Glide.with(context).load(url).placeholder(R.drawable.default_img).thumbnail(0.1F).dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop()
               .into(iv);
    }

    /**
     * 普通加载
     */
    public static void setCommonImage(Context context, String url, ImageView iv, int lodfailres) {
        Glide.with(context).load(url).thumbnail(0.1F).placeholder(lodfailres).error(lodfailres).dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop()
              .into(iv);
    }

    /**
     * 设置图片尺寸
     */
    public static void setCommonImage(Context context, String url, ImageView iv, int width, int height) {
        Glide.with(context).load(url.replace("__", "_" + width + "x" + height)).placeholder(R.drawable.default_img).dontAnimate().error(R.drawable.lodingfail).override(width, height).thumbnail(0.1F).diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop()
               .into(iv);
    }

    /**
     * 普通加载，无默认图片
     */
    public static void setCommonImageNo(Context context, String url, ImageView iv) {
        Glide.with(context).load(url).thumbnail(0.1F).dontAnimate().diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop()
                .into(iv);
    }

    /**
     * 默认圆角加载
     */
    public static void setCommonRadiusImage(Context context, String url,
                                            ImageView iv) {
        Glide.with(context).load(url).placeholder(R.drawable.default_img).dontAnimate().error(R.drawable.lodingfail).thumbnail(0.1F).diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop()
                .transform(new GlideRoundTransform(context, DEFAULT_RADIUS))
                .into(iv);
    }

    /**
     * 自定圆角加载
     */
    public static void setOwnRadiusImage(Context context, String url,
                                         ImageView iv, int dp) {
        Glide.with(context).load(url).placeholder(R.drawable.default_img).dontAnimate().error(R.drawable.lodingfail).thumbnail(0.1F).diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop()
                .transform(new GlideRoundTransform(context, dp)).into(iv);
    }

    /**
     * 圆形加载
     */
    public static void setCircleImage(Context context, String url, ImageView iv) {
        Glide.with(context).load(url).placeholder(R.drawable.default_img).dontAnimate().error(R.drawable.lodingfail).thumbnail(0.1F).diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop()
                .transform(new GlideCircleTransform(context)).into(iv);
    }

    /**
     * 圆形加载头像
     */
    public static void setCircleHeaderImage(Context context, String url, ImageView iv) {
        Glide.with(context).load(url).placeholder(R.drawable.default_photo).dontAnimate().error(R.drawable.default_photo).thumbnail(0.1F).diskCacheStrategy(DiskCacheStrategy.RESULT).centerCrop()
                .transform(new GlideCircleTransform(context)).into(iv);
    }

    /***
     * Description:通过链接获取分享图片的对象
     *
     * @author ZXJ
     * @date 2016-4-9
     * @param act
     * @param imgUrl
     * @return
     */

    public static Bitmap getImgBitmap(Context act, String imgUrl) {
        Bitmap compressedBitmap = null;
        File file = FileUtil.getCacheDir(imgUrl);

        compressedBitmap = FileUtils.getCompressedBitmap(
                file.getAbsolutePath(), 80, 80); // 图片大小有规定
        return compressedBitmap;
    }

    /**
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    @SuppressWarnings("finally")
    public static Bitmap getImageThumbnail(Activity act, int drawable, int maxWidth,
                                           int maxHeight, boolean isDeleteFile) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inPreferredConfig = Config.RGB_565;
        bitmap = BitmapFactory.decodeResource(act.getResources(), drawable, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
        bitmap = BitmapFactory.decodeResource(act.getResources(), drawable, options);
        return bitmap;
    }

    /**
     * 计算像素压缩的缩放比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;


        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }
    public static  String getRightImgScreen(String picUrl, int width, int height) {
        return picUrl.replace("__", "_" + width + "x" + height);
    }
}
