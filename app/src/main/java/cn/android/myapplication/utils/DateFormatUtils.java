
package cn.android.myapplication.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtils {
    private static final String DATE_FORMAT_PATTERN_D = "dd";
    private static final String DATE_FORMAT_PATTERN_DHM = "dd HH:mm";
    private static final String DATE_FORMAT_PATTERN_HM = "HH:mm";
    public static final String DATE_FORMAT_PATTERN_YM = "yyyy年MM月";
    public static final String DATE_FORMAT_PATTERN_YMD = "yyyy年MM月dd日";
    public static final String DATE_FORMAT_PATTERN_YMD_HM = "yyyy-MM-dd HH:mm";
    public static String dateFormat_day = "HH:mm";
    public static String dateFormat_month = "MM-dd";

    private static String getFormatPattern() {
        return DATE_FORMAT_PATTERN_HM;
    }

    private static String getFormatPattern(boolean z) {
        return z ? DATE_FORMAT_PATTERN_YMD_HM : DATE_FORMAT_PATTERN_YMD;
    }

    public static String dateToString(long j) {
        return dateToString(j, "yyyy.MM.dd HH:mm");
    }

    public static String dateToString(long j, String str) {
        return new SimpleDateFormat(str).format(new Date(j));
    }

    public static String long2Str(long j, boolean z) {
        return long2Str(j, getFormatPattern(z));
    }

    public static String long2Str(long j, String str) {
        return new SimpleDateFormat(str, Locale.CHINA).format(new Date(j));
    }

    public static String long2StrTimeDHM(long j, String str) {
        return long2StrTime(j, str);
    }

    public static String long2StrTime(long j) {
        return long2StrTime(j, getFormatPattern());
    }

    private static String long2StrTime(long j, String str) {
        return new SimpleDateFormat(str, Locale.CHINA).format(new Date(j));
    }

    public static long str2Long(String str, boolean z) {
        return str2Long(str, getFormatPattern(z));
    }

    private static long str2Long(String str, String str2) {
        try {
            return new SimpleDateFormat(str2, Locale.CHINA).parse(str).getTime();
        } catch (Throwable unused) {
            return 0;
        }
    }
}