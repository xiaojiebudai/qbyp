package cn.net.chenbao.qbyp.bean;

/**
 * 热门搜索
 * 
 * @author licheng
 *
 */
public class HotSearchCategory {

	public String KeyWord;
	public int LastTime;
	public int OperNum;
	public int ResultCount;

	@Override
	public String toString() {
		return "HotSearchCategory [KeyWord=" + KeyWord + ", LastTime="
				+ LastTime + ", OperNum=" + OperNum + ", ResultCount="
				+ ResultCount + "]";
	}

}
