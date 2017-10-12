package cn.net.chenbao.qbyp.bean;

/**
 * 商家评论
 * 
 * @author licheng
 * 
 */
public class Comment {
	/** 评论内容 */
	public String Content;
	/** 时间 */
	public long CreateTime;
	public int FlowId;
	/** 是否匿名 */
	public boolean IsAnonymous;
	/** 分数 */
	public float JudgeLevel;
	public int OrderAmt;
	public long OrderId;
	public long SellerId;
	public String SellerName;
	public long UserId;
	public String UserName;

	public String HeadUrl;

	@Override
	public String toString() {
		return "Comment [Content=" + Content + ", CreateTime=" + CreateTime
				+ ", FlowId=" + FlowId + ", IsAnonymous=" + IsAnonymous
				+ ", JudgeLevel=" + JudgeLevel + ", OrderAmt=" + OrderAmt
				+ ", OrderId=" + OrderId + ", SellerId=" + SellerId
				+ ", SellerName=" + SellerName + ", UserId=" + UserId
				+ ", UserName=" + UserName + ", HeadUrl=" + HeadUrl + "]";
	}
}
