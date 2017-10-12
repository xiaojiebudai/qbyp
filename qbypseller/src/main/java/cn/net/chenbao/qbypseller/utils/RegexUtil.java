package cn.net.chenbao.qbypseller.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Title:RegexUtil
 * </p>
 * <p>
 * Description:(正则表达式工具类 90%的验证都调用了Regular方法 但是本类也可删除大部分方法 涉及到正则的判断都直接穿参数和正则表达式
 * 是为了方便业务类调用和有更直观的含义 建议不要这么做 Pattern的matcher已经被同步synchronized 所以
 * 此类的任何使用正则验证的方法都不需要同步)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * 
 * @version v1.0 2014-9-28
 */

public class RegexUtil {

	// ------------------常量定义
	/**
	 * Email正则表达式=^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$
	 */
	public static final String EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
	// 6-20位数字和字母组合
	public static final String PSW = "^(?![0-9]+$)(?![a-zA-Z]+$)(.{6,20})$";

	/**
	 * 电话号码正则表达式=
	 * (^(\d{2,4}[-_－—]?)?\d{3,8}([-_－—]?\d{3,8})?([-_－—]?\d{1,7})?$)|
	 * (^0?1[35]\d{9}$)
	 */
	public static final String PHONE = "(^(\\d{2,4}[-_－—]?)?\\d{3,8}([-_－—]?\\d{3,8})?([-_－—]?\\d{1,7})?$)|(^0?1[35]\\d{9}$)";
	/**
	 * 手机号码正则表达式=^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$
	 */
	public static final String MOBILE = "^1(3[0-9]|4[57]|5[0-35-9]|7[0135678]|8[0-9])\\d{8}$";
	/**
	 * 固定电话
	 */
	public static final String GUHUA = "0\\d{2,3}-?\\d{7,8}";
	/**
	 * 400，800
	 */
	public static final String GUHUA400_800 = "(400|800)([0-9\\\\-]{7,10})";

	/**
	 * IP地址正则表达式
	 * ((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\
	 * d|[01]?\\d?\\d))
	 */
	public static final String IPADDRESS = "((?:(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}(?:25[0-5]|2[0-4]\\d|[01]?\\d?\\d))";

	/**
	 * Integer正则表达式 ^-?(([1-9]\d*$)|0)
	 */
	public static final String INTEGER = "^-?(([1-9]\\d*$)|0)";
	/**
	 * 正整数正则表达式 >=0 ^[1-9]\d*|0$ ///INTEGER_POSITIVE
	 */
	public static final String INTEGER_POSITIVE = "^[1-9]\\d*|0$";
	/**
	 * 负整数正则表达式 <=0 ^-[1-9]\d*|0$ //INTEGER_NEGATIVE
	 */
	public static final String INTEGER_NEGATIVE = "^-[1-9]\\d*|0$";
	/**
	 * Double正则表达式 ^-?([1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0)$
	 */
	public static final String DOUBLE = "^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$";
	/**
	 * 正Double正则表达式 >=0 ^[1-9]\d*\.\d*|0\.\d*[1-9]\d*|0?\.0+|0$　 DOUBLE_POSITIVE
	 */
	public static final String DOUBLE_POSITIVE = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0$";
	/**
	 * 负Double正则表达式 <= 0 ^(-([1-9]\d*\.\d*|0\.\d*[1-9]\d*))|0?\.0+|0$
	 * DOUBLE_NEGATIVE
	 */
	public static final String DOUBLE_NEGATIVE = "^(-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*))|0?\\.0+|0$";
	/**
	 * 年龄正则表达式 ^(?:[1-9][0-9]?|1[01][0-9]|120)$ 匹配0-120岁
	 */
	public static final String AGE = "^(?:[1-9][0-9]?|1[01][0-9]|120)$";
	/**
	 * 邮编正则表达式 [1-9]\d{5}(?!\d) 国内6位邮编
	 */
	public static final String CODE = "[1-9]\\d{5}(?!\\d)";
	/**
	 * 匹配由数字、26个英文字母或者下划线组成的字符串 ^\w+$
	 */
	public static final String STR_ENG_NUM_ = "^\\w+$";
	/**
	 * 匹配由数字和26个英文字母组成的字符串 ^[A-Za-z0-9]+$
	 */
	public static final String STR_ENG_NUM = "^\\w+$";
	/**
	 * 匹配由26个英文字母组成的字符串 ^[A-Za-z]+$
	 */
	public static final String STR_ENG = "^[A-Za-z]+$";
	/**
	 * 匹配中文字符 ^[\Α-\￥]+$
	 */
	public static final String STR_CHINA = "^[\\Α-\\￥]+$";
	/**
	 * 过滤特殊字符串正则 regEx=
	 * "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
	 */
	public static final String STR_SPECIAL = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
	/**
	 * 包括数字
	 */
	public static final String STR_HAS_NUM = ".*\\d+.*";
	/**
	 * 只能输英文 数字 中文 ^[a-zA-Z0-9\u4e00-\u9fa5a]+$
	 */
	public static final String STR_ENG_CHA_NUM = "^[a-zA-Z0-9\\u4e00-\\u9fa5a]+$";
	/** 
     *   
     */
	/***
	 * 日期正则 支持： YYYY-MM-DD YYYY/MM/DD YYYY_MM_DD YYYYMMDD YYYY.MM.DD的形式
	 */
	public static final String DATE_ALL = "((^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(10|12|0?[13578])([-\\/\\._]?)(3[01]|[12][0-9]|0?[1-9])$)"
			+ "|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(11|0?[469])([-\\/\\._]?)(30|[12][0-9]|0?[1-9])$)"
			+ "|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-\\/\\._]?)(0?2)([-\\/\\._]?)(2[0-8]|1[0-9]|0?[1-9])$)|(^([2468][048]00)([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([3579][26]00)"
			+ "([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)"
			+ "|(^([1][89][0][48])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([2-9][0-9][0][48])([-\\/\\._]?)"
			+ "(0?2)([-\\/\\._]?)(29)$)"
			+ "|(^([1][89][2468][048])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|(^([2-9][0-9][2468][048])([-\\/\\._]?)(0?2)"
			+ "([-\\/\\._]?)(29)$)|(^([1][89][13579][26])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$)|"
			+ "(^([2-9][0-9][13579][26])([-\\/\\._]?)(0?2)([-\\/\\._]?)(29)$))";

	public static final String YEAR_MONTH = "^\\d{4}-?(?:0[1-9]|1[0-2])$";

	/**
	 * URL正则表达式 匹配 http www ftp
	 */
	public static final String URL = "^(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?"
			+ "(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*"
			+ "(\\w*:)*(\\w*\\+)*(\\w*\\.)*"
			+ "(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$";

	/**
	 * 身份证正则表达式
	 */
	public static final String IDCARD = "((11|12|13|14|15|21|22|23|31|32|33|34|35|36|37|41|42|43|44|45|46|50|51|52|53|54|61|62|63|64|65)[0-9]{4})"
			+ "(([1|2][0-9]{3}[0|1][0-9][0-3][0-9][0-9]{3}"
			+ "[Xx0-9])|([0-9]{2}[0|1][0-9][0-3][0-9][0-9]{3}))";
	/**
	 * 1.匹配科学计数 e或者E必须出现有且只有一次 不含Dd 正则
	 * ^[-+]?(\d+(\.\d*)?|\.\d+)([eE]([-+]?([012]
	 * ?\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))$
	 */
	public final static String SCIENTIFIC_A = "^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))$";
	/**
	 * 2.匹配科学计数 e或者E必须出现有且只有一次 结尾包含Dd 正则
	 * ^[-+]?(\d+(\.\d*)?|\.\d+)([eE]([-+]?([012
	 * ]?\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))[dD]?$
	 */
	public final static String SCIENTIFIC_B = "^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))[dD]?$";
	/**
	 * 3.匹配科学计数 是否含有E或者e都通过 结尾含有Dd的也通过（针对Double类型） 正则
	 * ^[-+]?(\d+(\.\d*)?|\.\d+)([
	 * eE]([-+]?([012]?\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?[dD]?$
	 */
	public final static String SCIENTIFIC_C = "^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?[dD]?$";
	/**
	 * 4.匹配科学计数 是否含有E或者e都通过 结尾不含Dd 正则
	 * ^[-+]?(\d+(\.\d*)?|\.\d+)([eE]([-+]?([012]?
	 * \d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?$
	 */
	public final static String SCIENTIFIC_D = "^[-+]?(\\d+(\\.\\d*)?|\\.\\d+)([eE]([-+]?([012]?\\d{1,2}|30[0-7])|-3([01]?[4-9]|[012]?[0-3])))?$";

	// //------------------验证方法
	/**
	 * 判断字段是否为空 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static synchronized boolean StrisNull(String str) {
		return null == str || str.trim().length() <= 0;
	}

	/**
	 * 判断字段是非空 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean StrNotNull(String str) {
		return !StrisNull(str);
	}

	/**
	 * 字符串null转空
	 * 
	 * @param str
	 * @return boolean
	 */
	public static String nulltoStr(String str) {
		return StrisNull(str) ? "" : str;
	}

	/**
	 * 字符串null赋值默认值
	 * 
	 * @param str
	 *            目标字符串
	 * @param defaut
	 *            默认值
	 * @return String
	 */
	public static String nulltoStr(String str, String defaut) {
		return StrisNull(str) ? defaut : str;
	}

	/**
	 * 判断字段是否为Email 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isEmail(String str) {
		return Regular(str, EMAIL);
	}

	/**
	 * 判断字段是否为PSW 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isPsw(String str) {
		return Regular(str, PSW);
	}

	/**
	 * 判断是否为电话号码 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isPhone(String str) {
		return Regular(str, PHONE);
	}

	/**
	 * 判断是否为手机号码 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isMobile(String str) {
		return Regular(str, MOBILE);
	}
	/**
	 * 判断是否为固定电话 符合返回true
	 * @param str
	 * @return
	 */
	public static boolean isGuHua(String str) {
		return Regular(str, GUHUA);
	}
	/**
	 * 判断是否为400_800电话 符合返回true
	 * @param str
	 * @return
	 */
	public static boolean isGUHUA400_800(String str) {
		return Regular(str, GUHUA400_800);
	}

	/**
	 * 判断是否为Url 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isUrl(String str) {
		return Regular(str, URL);
	}

	/**
	 * 判断是否为IP地址 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isIpaddress(String str) {
		return Regular(str, IPADDRESS);
	}

	/**
	 * 判断字段是否为数字 正负整数 正负浮点数 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isNumber1(String str) {
		return isInteger(str) || isDouble(str);
	}

	/**
	 * 判断字段是否为INTEGER 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isInteger(String str) {
		return Regular(str, INTEGER);
	}

	/**
	 * 判断字段是否为正整数正则表达式 >=0 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isINTEGER_POSITIVE(String str) {
		return Regular(str, INTEGER_POSITIVE);
	}

	/**
	 * 判断字段是否为负整数正则表达式 <=0 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isINTEGER_NEGATIVE(String str) {
		return Regular(str, INTEGER_NEGATIVE);
	}

	/**
	 * 判断字段是否为DOUBLE 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isDouble(String str) {
		return Regular(str, DOUBLE);
	}

	/**
	 * 判断字段是否为正浮点数正则表达式 >=0 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isDOUBLE_POSITIVE(String str) {
		return Regular(str, DOUBLE_POSITIVE);
	}

	/**
	 * 判断字段是否为负浮点数正则表达式 <=0 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isDOUBLE_NEGATIVE(String str) {
		return Regular(str, DOUBLE_NEGATIVE);
	}

	/**
	 * 判断字段是否为日期 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isDate(String str) {
		return Regular(str, DATE_ALL);
	}

	public static boolean isYearMonth(String str) {
		return Regular(str, YEAR_MONTH);
	}

	/**
	 * 判断字段是否为年龄 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isAge(String str) {
		return Regular(str, AGE);
	}

	/**
	 * 判断字段是否超长 字串为空返回fasle, 超过长度{leng}返回true 反之返回false
	 * 
	 * @param str
	 * @param leng
	 * @return boolean
	 */
	public static boolean isLengOut(String str, int leng) {
		return !StrisNull(str) && str.trim().length() > leng;
	}

	/**
	 * 判断字段是否为身份证 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isIdCard(String str) {
		if (StrisNull(str))
			return false;
		if (str.trim().length() == 15 || str.trim().length() == 18) {
			return Regular(str, IDCARD);
		} else {
			return false;
		}

	}

	/**
	 * 判断字段是否为邮编 符合返回true
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isCode(String str) {
		return Regular(str, CODE);
	}

	/**
	 * 判断字符串是不是全部是汉字
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isChina(String str) {
		return Regular(str, STR_CHINA);
	}

	/**
	 * 判断字符串是不是全部是英文字母
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isEnglish(String str) {
		return Regular(str, STR_ENG);
	}

	/**
	 * 判断字符串是不是全部是英文字母+数字
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isENG_NUM(String str) {
		return Regular(str, STR_ENG_NUM);
	}

	/**
	 * 判断字符串是不是全部是英文字母+数字+下划线
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean isENG_NUM_(String str) {
		return Regular(str, STR_ENG_NUM_);
	}

	/**
	 * 过滤特殊字符串 返回过滤后的字符串
	 * 
	 * @param str
	 * @return boolean
	 */
	public static String filterStr(String str) {
		Pattern p = Pattern.compile(STR_SPECIAL);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();
	}

	/**
	 * 匹配是否符合正则表达式pattern 匹配返回true
	 * 
	 * @param str
	 *            匹配的字符串
	 * @param pattern
	 *            匹配模式
	 * @return boolean
	 */
	private static boolean Regular(String str, String pattern) {
		if (null == str || str.trim().length() <= 0)
			return false;
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 判断是不是科学计数法 如果是 返回true 匹配科学计数 e或者E必须出现有且只有一次 结尾不含D 匹配模式可参考本类定义的
	 * SCIENTIFIC_A，SCIENTIFIC_B,SCIENTIFIC_C,SCIENTIFIC_D 若判断为其他模式可调用
	 * Regular(String str,String pattern)方法
	 * 
	 * @param str
	 *            科学计数字符串
	 * @return boolean
	 */
	public static boolean isScientific(String str) {
		if (StrisNull(str))
			return false;
		return Regular(str, RegexUtil.SCIENTIFIC_A);
	}

	public static void main(String[] args) {
		// System.out.println(RegexUtil.isDOUBLE_POSITIVE("7.0"));
		// System.out.println(RegexUtil.isINTEGER_POSITIVE("1440"));
		// System.out.println(""+RegexUtil.isYearMonth("1201-01"));;
		// System.out.println(RegexUtil.isDate("2013-01-01"));
		// System.out.println(RegexUtil.isDate("20131212"));
		// System.out.println(RegexUtil.isNumber1("2013.0606"));
		// System.out.println(RegexUtil.isNumber1("-2013.0606"));
		// System.out.println(RegexUtil.isNumber1("201366"));
		// System.out.println(RegexUtil.isNumber1("-201366"));
		// System.out.println(Regular("229573495732","^(11|22|268|888|588|688|368|468|568|668|768|868|968)[0-9]{9}$|^(11|22|268|888|588|688|368|468|568|668|768|868|968)[0-9]{10}$|^(STO)[0-9]{10}$"));//申通快递
		// System.out.println("560265641795".matches("^[0-9]{12}$"));//天天快递
		// System.out.println("5021616460304".matches("^[A-Z]{2}[0-9]{9}[A-Z]{2}$|^[0-9]{13}$"));//EMS
		// System.out.println("6152143001".matches("^[a-zA-Z0-9]{10}$"));//宅急送
		// System.out.println("18681852".matches("[0-9]{8}"));//新邦物流
		// 610002215997 百世汇通
		// System.out.println("210827085629".matches("^(A|B|C|D|E|H|0)(D|X|[0-9])(A|[0-9])[0-9]{10}$|^(21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39)[0-9]{10}$|^50[0-9]{12}$"));//百世汇通
		// System.out.println("610002215997".matches("^(A|B|C|D|E|H|0)(D|X|[0-9])(A|[0-9])[0-9]{10}$|^(21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|37|38|39|61)[0-9]{10}$|^(369|639|649|630|360)[0-9]{9}$|^(50|51)[0-9]{12}$|^(7)[0-9]{13}$"));//百世汇通
		// System.out.println("100275344460".matches("^VIP[0-9]{9}|V[0-9]{11}|[0-9]{12}$|^LBX[0-9]{15}-[2-9AZ]{1}-[1-9A-Z]{1}"));//优速快递
		// System.out.println("3100340673530".matches("^[\\s]*[1-9][0-9]{12}[\\s]*$"));//韵达快递
		// System.out.println("968238558390".matches("^[0-9]{12}$"));//顺丰速运
		// System.out.println("778584157604".matches("^((618|680|778|768|688|618|828|988|118|888|571|518|010|628|205|880|717|718|728|738|761|762|763|701|757)[0-9]{9})$|^((2008|2010|8050|7518)[0-9]{8})$"));//中通速递
		// System.out.println("363650831128".matches("^((359|528|751|358|618|680|778|768|688|689|618|828|988|118|888|571|518|010|628|205|880|717|718|728|738|761|762|763|701|757)[0-9]{9})$|^((2008|2010|8050|7518)[0-9]{8})$|^((36)[0-9]{10})$"));//中通速递

		// 上门取货
		// 运费到付
		// 申通快递
		// ^(11|22|268|888|588|688|368|468|568|668|768|868|968)[0-9]{9}$|^(11|22|268|888|588|688|368|468|568|668|768|868|968)[0-9]{10}$|^(STO)[0-9]{10}$
		// 圆通速递
		// 中通速递
		// ^((618|680|778|768|688|618|828|988|118|888|571|518|010|628|205|880|717|718|728|738|761|762|763|701|757)[0-9]{9})$|^((2008|2010|8050|7518)[0-9]{8})$
		// 顺丰速运 ^[0-9]{12}$
		// 韵达快递 ^[\s]*[1-9][0-9]{12}[\s]*$
		// 天天快递 ^[0-9]{12}$
		// 优速快递
		// ^VIP[0-9]{9}|V[0-9]{11}|[0-9]{12}$|^LBX[0-9]{15}-[2-9AZ]{1}-[1-9A-Z]{1}
		// 百世汇通
		// ^(A|B|C|D|E|H|0)(D|X|[0-9])(A|[0-9])[0-9]{10}$|^(21|22|23|24|25|26|27|28|29|30|31|32|33|34|35|36|37|38|39)[0-9]{10}$|^50[0-9]{12}$
		// EMS 国内邮政特快专递 ^[A-Z]{2}[0-9]{9}[A-Z]{2}$|^[0-9]{13}$
		// 宅急送 ^[a-zA-Z0-9]{10}$
		// 新邦物流 [0-9]{8}
		// 国通快递
		// 全峰快递

		System.out.println(RegexUtil.isIpaddress("255.255.255.255"));
		System.out.println(RegexUtil.isIpaddress("256.256.256.256"));

	}

	/**
	 * 判断是否属于正整数
	 * 
	 * @param str
	 *            输入字符
	 * @return 如果正确返回true，如果失败返回false
	 * @author 赵汉江
	 */
	public static boolean isNumber(String str) {
		java.util.regex.Pattern pattern = java.util.regex.Pattern
				.compile("^[0-9]*[1-9][0-9]*$");
		java.util.regex.Matcher match = pattern.matcher(str);
		return match.matches() != false;
	}

	/**
	 * 【替换HTML代码Font样式中的汉字】
	 * 
	 * @param inputstr
	 * @return
	 */

	public static String replaceHTMLFontCN(String inputstr) {
		String sFont = "font.*?[;\"']";
		String reg = "/^[u0391-uffe5]+$/";

		if (!Pattern.matches(reg, inputstr)) {
			inputstr = inputstr.replaceAll(sFont, "");
		}
		return inputstr;
	}

	/**
	 * @param mString
	 * @return 如果是null或则是空返回的是false 这个方法是输入字符串判断是否
	 * @date 2015年8月6日
	 * @author liuyonghong
	 */
	public static boolean isNullEmpty(String mString) {
		if (mString == null) {
			return false;
		} else if (mString.isEmpty()) {
			return false;
		} else return !mString.equals("null");
	}

	/**
	 * 判定输入汉字
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
	}

	/**
	 * 检测String是否包含中文
	 * 
	 * @param name
	 * @return
	 */
	public static boolean checkNameChese(String name) {
		boolean res = false;
		char[] cTemp = name.toCharArray();
		for (int i = 0; i < name.length(); i++) {
			if (isChinese(cTemp[i])) {
				res = true;
				break;
			}
		}
		return res;
	}
	/**
	 * 检测String是否包含特殊字符
	 *
	 * @param str
	 * @return
	 */
	public static boolean checkHasSpecial(String str) {
		boolean res = false;
		char[] cTemp = str.toCharArray();
		for (int i = 0; i < str.length(); i++) {
			if (Regular(cTemp[i] + "", RegexUtil.STR_SPECIAL)) {
				res = true;
				break;
			}
		}
		return res;
	}

	/**
	 * 检测String是否包含数字
	 *
	 * @param str
	 * @return
	 */
	public static boolean checkHasNum(String str) {
		return Regular(str, STR_HAS_NUM);
	}
	/**
	 * 银行卡卡号采用 Luhm 校验算法获得校验位
	 *
	 * @return boolean
	 */
	public static boolean getBankCardCheckCode(String checkCodeCardId) {
		char bit = checkCodeCardId.charAt(checkCodeCardId.length() - 1);
		checkCodeCardId = checkCodeCardId.substring(0,
				checkCodeCardId.length() - 1);
		if (checkCodeCardId == null || checkCodeCardId.trim().length() == 0
				|| !checkCodeCardId.matches("\\d+")) {
			return false;
		}
		char[] chs = checkCodeCardId.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return ((luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0')) == bit;
	}

}