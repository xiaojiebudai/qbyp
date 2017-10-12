package cn.net.chenbao.qbyp.bean;

import android.widget.CheckBox;

/**
 * 送达时间
 * 
 * @author licheng
 *
 */
public class GoTime {

	public String time;
	public boolean isSelect;

	public CheckBox cb;

	@Override
	public String toString() {
		return "GoTime [time=" + time + ", isSelect=" + isSelect + "]";
	}

}
