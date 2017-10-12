package cn.net.chenbao.qbyp.bean;

/***
	 * Description:站内信  Company: wangwanglife Version：1.0
	 * 
	 * @author zxj
	 * @date 2016-8-4
     */
public class Message {
	/**消息内容*/
	public String Content;
	/**自增量*/
	public int FlowId;
	/**发送时间*/
	public long CreateTime;
	/**类型*/
	public String MsgType;
	/**阅读标志*/
	public boolean ReadFlag;
	/**阅读时间*/
	public long ReadTime;
	/**接收人*/
	public String ReceiverId;
	/**发送人名称*/
	public String SendName;
	

}
