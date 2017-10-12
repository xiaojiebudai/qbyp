package cn.net.chenbao.qbypseller.bean;

/**
 * 类目
 * 
 * @author xl
 * @description 同后台的Class
 */
public class Category {

	/** 自定制全部 */
	public static final int CLASS_ALL = -1;
	/** 自定制下架 */
	public static final int CLASS_OFF = -2;

	public long ClassId;
	public String ClassName;
	public int ParentId;
	public int SellerId;
	public int GoodsCount;

}
