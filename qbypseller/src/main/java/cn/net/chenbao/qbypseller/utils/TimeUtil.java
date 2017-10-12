package cn.net.chenbao.qbypseller.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;

/**
 * 时间工具
 * 
 * @author xl
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	/**
	 * 退款超时天数
	 * 
	 * @description 应该后台传超时逾期的时间
	 * */
	private static final int REFUND_OVERTIME = 3;

	public static String getTime() {
		long time = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		Date d1 = new Date(time);
		String time_str = format.format(d1);
		return time_str;
	}

	/**
	 * 获得时间
	 * 
	 * @author xl
	 * @description 年-月-日 时-分-秒
	 * @return
	 * @param time
	 * @return
	 */
	public static String getTimeToS(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		Date d1 = new Date(time);
		String time_str = format.format(d1);
		return time_str;
	}

	/**
	 * 获取日期
	 * 
	 * @param time
	 * @return
	 */
	public static String getOnlyDate(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		Date d1 = new Date(time);
		String time_str = format.format(d1);
		return time_str;
	}

	/**
	 * 获取日期
	 * 
	 * @param time
	 * @return
	 */
	public static String getOnlyDateToSTwo(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date d1 = new Date(time);
		String time_str = format.format(d1);
		return time_str;
	}

	/**
	 * 获取时间
	 * 
	 * @param time
	 * @return
	 */
	public static String getOnlyTimeToS(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date d1 = new Date(time);
		 format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		String time_str = format.format(d1);
		return time_str;
	}

	/**
	 * 获得时间
	 * 
	 * @author xl
	 * @date:2016-1-5下午2:41:12
	 * @description 年-月-日 时-分
	 * @return
	 * @param time
	 * @return
	 */
	public static String getTimeToM(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		return format.format(new Date(time));
	}

	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
		return format.format(new Date(time));
	}

	/**
	 * 获得退款申请的逾期时间
	 * 
	 * @author xl
	 * @date:2016-1-12下午4:42:10
	 * @description
	 * @param applyTime
	 * @param currentTime
	 * @return 天数
	 */
	public static String getRefundOverDueTime(long applyTime, long currentTime) {
		float overTime = applyTime + REFUND_OVERTIME * 24 * 60 * 60 * 1000;
		float overdue = overTime - currentTime;// 逾期
		int day = Math.round(overdue / (24 * 60 * 60 * 1000));// 逾期天数
		return day + "天";
	}

	/**
	 * 环信聊天用
	 * 
	 */
	public static String getChatTime(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			result = "今天 " + getHourAndMin(timesamp);
			break;
		case 1:
			result = "昨天 " + getHourAndMin(timesamp);
			break;
		case 2:
			result = "前天 " + getHourAndMin(timesamp);
			break;

		default:
			// result = temp + "天前 ";
			result = getTimeToM(timesamp);
			break;
		}

		return result;
	}

	/**
	 * 
	 * ImageSelector 使用方法
	 * 
	 * @author xl
	 * @version 创建时间：2015-11-11
	 */
	public static String timeFormat(long timeMillis, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
		return format.format(new Date(timeMillis));
	}

	public static String formatPhotoDate(long time) {
		return timeFormat(time, "yyyy-MM-dd");
	}

	public static String formatPhotoDate(String path) {
		File file = new File(path);
		if (file.exists()) {
			long time = file.lastModified();
			return formatPhotoDate(time);
		}
		return "1970-01-01";
	}

	/**
	 * 新的时间样式
	 * 
	 * @author xl
	 * @date:2016-4-26下午6:27:52
	 * @description
	 * @param time
	 * @return
	 */
	public static String getTimeNewsStyle(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yy/MM/dd HH:mm");
		return format.format(new Date(time));
	}

	/**
	 * MM-dd
	 * 
	 * @author xl
	 * @date:2016-5-4下午5:39:39
	 * @description
	 * @param time
	 * @return
	 */
	public static String getTimeMMdd(long time) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd");
		return format.format(new Date(time));
	}

	/**
	 * 红包详情的领取的时间格式
	 * 
	 * @author xl
	 * @date:2016-5-4下午6:31:22
	 * @description
	 * @param time
	 * @return
	 */
	public static String getTimeRedPacktReceive(long time) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
		return format.format(new Date(time));
	}

	/**
	 * 获取模糊时间 lc
	 * 
	 * @param time
	 * @return
	 */
	public static String getData(long time) {
		long current = System.currentTimeMillis() - time;
		if (current < 60 * 1000) {
			return "刚刚";
		} else if (current < 60 * 1000 * 60) {
			return current / 1000 / 60 + "分钟前";
		} else if (current < 60 * 1000 * 60 * 24) {
			return current / 1000 / 60 / 60 + "小时前";
		} else if (current < 60 * 1000 * 60 * 24 * 7) {
			return current / 1000 / 60 / 60 / 24 + "天前";
		}
		return getTimeToM(time);
	}

	/**
	 * 这个方法是用于防止点击过快
	 * 
	 * @date 2015年10月25日01:09:18
	 * @author zxj
	 */
	private static long lastClickTime;

	public synchronized static boolean isFastClick() {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < 1000) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
	/**
	 * 这个方法是用于防止点击过快,自己定义时间
	 *
	 * @date 2015年10月25日01:09:18
	 * @author zxj
	 */
	public synchronized static boolean isFastClick(long s) {
		long time = System.currentTimeMillis();
		if (time - lastClickTime < s) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
