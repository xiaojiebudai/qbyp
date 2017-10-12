package cn.net.chenbao.qbyp.utils;

import java.io.File;

import cn.net.chenbao.qbyp.R;

import android.app.Activity;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class ShareUtils {
	/**
	 * 网络图片的分享
	 * 
	 * @param act
	 * @param url
	 * @param title
	 * @param content
	 * @param listener
	 * @param tagUrl
	 */
	public static ShareAction doShare(Activity act, String url, String title,
			String content, UMShareListener listener, String tagUrl) {
		ShareAction shareAction = new ShareAction(act);
		shareAction.setDisplayList(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.QQ,
				SHARE_MEDIA.QZONE);
		shareAction.withMedia(new UMImage(act, url));
		shareAction.withTitle(title);
		shareAction.withText(content);
		shareAction.withTargetUrl(tagUrl);
		shareAction.setCallback(listener);
		return shareAction;
	}

	/**
	 * 本地资源文件share
	 * 
	 * @param act
	 * @param title
	 * @param content
	 * @param listener
	 * @param tagUrl
	 */
	public static ShareAction doShare(Activity act, int res, String title,
			String content, UMShareListener listener, String tagUrl) {
		ShareAction shareAction = new ShareAction(act);
		shareAction.setDisplayList(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,
				SHARE_MEDIA.QZONE);
//		Bitmap image = ImageUtils.getImageThumbnail(act,res, 50, 50, false);
		UMImage umImage = new UMImage(act.getApplicationContext(), R.drawable.logo);
		shareAction.withMedia(umImage);
		shareAction.withTitle(title);
		shareAction.withText(content);
		shareAction.withTargetUrl(tagUrl);
		shareAction.setListenerList(listener);
		return shareAction;
	}

	/**
	 * 本地路径图片的share
	 * 
	 * @param act
	 * @param file
	 * @param title
	 * @param content
	 * @param listener
	 * @param tagUrl
	 */
	public static ShareAction doShare(Activity act, File file, String title,
			String content, UMShareListener listener, String tagUrl) {
		ShareAction shareAction = new ShareAction(act);
		shareAction.setDisplayList(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,
				SHARE_MEDIA.QZONE);
		shareAction.withMedia(new UMImage(act, file));
		shareAction.withTitle(title);
		shareAction.withText(content);
		shareAction.withTargetUrl(tagUrl);
		shareAction.setListenerList(listener);
		return shareAction;
	}

}
