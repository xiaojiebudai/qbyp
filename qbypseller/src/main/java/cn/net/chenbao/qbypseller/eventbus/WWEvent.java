package cn.net.chenbao.qbypseller.eventbus;

/**
 * EventBus事件基类
 * 
 * @author xl
 * @date 2016-8-30 下午10:07:38
 * @description
 */
abstract public class WWEvent {

	public String data;

	public WWEvent(String data) {
		super();
		this.data = data;
	}

}
