package tr.wolflame.framework.base.util.helper;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import tr.wolflame.framework.R;
import tr.wolflame.framework.base.util.LogApp;


/**
 * Created by SADIK on 01/12/15.
 */
@SuppressLint("SimpleDateFormat")
public class DateFormatter {

    final private static String TAG = "DateFormatter";

    final private static String INPUT_PATTERN = "dd-MM-yyyy HH:mm:ss"; //"yyyy-MM-dd HH:mm:ss";
    final private static String OUTPUT_PATTERN = "dd MMM yyyy HH:mm"; //"dd MMM yyyy h:mm a"; 18-jun-2013 12:41 pm

    public static String FormatDate(Date date) {
        return FormatDate(date, OUTPUT_PATTERN);
    }

    public static String FormatDate(Date date, String outputFormat) {
        String str = "";
        try {
            str = GetOutputFormat(outputFormat).format(date);
        } catch (Exception e) {
            LogApp.e(TAG, String.valueOf(e.toString()));
        }
        return str;
    }

    public static Date StringToDate(String dString) {
        return StringToDate(dString, INPUT_PATTERN);
    }

    public static Date StringToDate(String dString, String inputPattern) {
        Date date = new Date();
        try {
            date = GetInputFormat(inputPattern).parse(dString.replace("T", " "));
        } catch (java.text.ParseException e) {
            LogApp.e(TAG, String.valueOf(e.toString()));
        }
        return date;
    }

    public static String DateToString(Date sDate) {
        return DateToString(sDate, INPUT_PATTERN);
    }

    public static String DateToString(Date sDate, String inputPattern) {
        String sDatetime = "";
        try {
            sDatetime = GetInputFormat(inputPattern).format(sDate);
            System.out.println("Current Date Time : " + sDatetime);

        } catch (android.net.ParseException e) {
            e.printStackTrace();
        }
        return sDatetime;
    }

    public static String DateToTimeStamp(Date date) {
        return String.valueOf(date.getTime());
    }

    public static Calendar DateToCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    private static SimpleDateFormat GetInputFormat() {
        return new SimpleDateFormat(INPUT_PATTERN, Locale.getDefault());
    }


    private static SimpleDateFormat GetInputFormat(String inputPattern) {
        return new SimpleDateFormat(inputPattern, Locale.getDefault());
    }


    private static SimpleDateFormat GetOutputFormat() {
        return new SimpleDateFormat(OUTPUT_PATTERN, Locale.getDefault());
    }

    private static SimpleDateFormat GetOutputFormat(String outputPattern) {
        return new SimpleDateFormat(outputPattern, Locale.getDefault());
    }


    public static String GetTodayDate(Context mContext, Date inputDate) {

        if (inputDate != null) {

            try {
                Calendar mCalendar = DateToCalendar(inputDate);

                String weekDay = "";
                final int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK);

                LogApp.d(TAG, "dayOfWeek: " + String.valueOf(dayOfWeek));

                switch (dayOfWeek) {
                    case Calendar.MONDAY:
                        weekDay = mContext.getResources().getString(R.string.monday);
                        break;
                    case Calendar.TUESDAY:
                        weekDay = mContext.getResources().getString(R.string.tuesday);
                        break;
                    case Calendar.WEDNESDAY:
                        weekDay = mContext.getResources().getString(R.string.wednesday);
                        break;
                    case Calendar.THURSDAY:
                        weekDay = mContext.getResources().getString(R.string.thursday);
                        break;
                    case Calendar.FRIDAY:
                        weekDay = mContext.getResources().getString(R.string.friday);
                        break;
                    case Calendar.SATURDAY:
                        weekDay = mContext.getResources().getString(R.string.saturday);
                        break;
                    case Calendar.SUNDAY:
                        weekDay = mContext.getResources().getString(R.string.sunday);
                        break;
                }


                String monthName = "";
                final int month = mCalendar.get(Calendar.MONTH);
                LogApp.d(TAG, "month: " + String.valueOf(month));


                switch (month) {
                    case Calendar.JANUARY:
                        monthName = mContext.getResources().getString(R.string.january);
                        break;
                    case Calendar.FEBRUARY:
                        monthName = mContext.getResources().getString(R.string.february);
                        break;
                    case Calendar.MARCH:
                        monthName = mContext.getResources().getString(R.string.march);
                        break;
                    case Calendar.APRIL:
                        monthName = mContext.getResources().getString(R.string.april);
                        break;
                    case Calendar.MAY:
                        monthName = mContext.getResources().getString(R.string.may);
                        break;
                    case Calendar.JUNE:
                        monthName = mContext.getResources().getString(R.string.june);
                        break;
                    case Calendar.JULY:
                        monthName = mContext.getResources().getString(R.string.july);
                        break;
                    case Calendar.AUGUST:
                        monthName = mContext.getResources().getString(R.string.august);
                        break;
                    case Calendar.SEPTEMBER:
                        monthName = mContext.getResources().getString(R.string.september);
                        break;
                    case Calendar.OCTOBER:
                        monthName = mContext.getResources().getString(R.string.october);
                        break;
                    case Calendar.NOVEMBER:
                        monthName = mContext.getResources().getString(R.string.november);
                        break;
                    case Calendar.DECEMBER:
                        monthName = mContext.getResources().getString(R.string.december);
                        break;
                }


                final String curHour = String.format("%02d:%02d", mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE));
                LogApp.d(TAG, "curHour: " + String.valueOf(curHour));

                return mCalendar.get(Calendar.DAY_OF_MONTH) + " " + monthName + " " + mCalendar.get(Calendar.YEAR)
                        + " " + weekDay + ", " + curHour;

            } catch (Exception e) {
                LogApp.e(TAG, String.valueOf(e.toString()));

                return "";
            }
        } else {
            return "";
        }
    }

}
