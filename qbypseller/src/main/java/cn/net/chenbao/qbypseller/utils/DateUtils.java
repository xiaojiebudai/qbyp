package cn.net.chenbao.qbypseller.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	public static String formatDateDT(long date) {
		Date currentdate = new Date(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(currentdate);
	}

	public static String formatDateD(long date) {
		Date currentdate = new Date(date);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(currentdate);
	}

	public static String formatPickerDate(int year, int month, int day) {

		month++;

		StringBuilder formatStr = new StringBuilder();
		formatStr.append(year);
		formatStr.append("年");
		if (month < 10) {
			formatStr.append(0);
		}
		formatStr.append(month);
		formatStr.append("月");
		if (day < 10) {
			formatStr.append(0);
		}
		formatStr.append(day);
		formatStr.append("日");

		return formatStr.toString();
	}
}
