package cn.net.chenbao.qbyp.bean;

/***
 * Description:分享的内容 Company: Zhubaoyi Version：2.0
 * 
 * @title ShareInfo.java
 * @author ZXJ
 * @date 2016-4-8
 ***/
public class ShareInfo {
	// 标题
	public String title;
	// 图片链接
	public String imgUrl;
	// 链接
	public String linkUrl;
	// 文本说明
	public String shortDesc;

	/** iamgeloader */
	public final static int LOAD_TYPE_IMAGELOADER = 0;

	/** 图片加载工具(不同模式下的图片加载可能不同,需要处理不同路径) */
	public int imageLoadType;

	public final static int SHARE_TYPE_TEXT = 0;
	public final static int SHARE_TYPE_IMAGE = 1;
	public final static int SHARE_TYPE_WEBPAGE = 2;
	public final static int SHARE_TYPE_IMAGES = 3;
	/** 分享类型 */
	public int shareType;

	@Override
	public String toString() {
		return "ShareInfo [title=" + title + ", imgUrl=" + imgUrl
				+ ", linkUrl=" + linkUrl + ", shortDesc=" + shortDesc + "]";
	}

}
