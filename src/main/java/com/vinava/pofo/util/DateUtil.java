package com.vinava.pofo.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    public static int getCurrentFinancialYear() {
        Calendar calendar = getCurrentCalendarInstance();
        int month = calendar.get(Calendar.MONTH);
        int advance = (month < 3) ? 0 : 1;
        return calendar.get(Calendar.YEAR) + advance;
    }

    public static int getCurrentYear() {
        Calendar calendar = getCurrentCalendarInstance();
        return calendar.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar calendar = getCurrentCalendarInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getCurrenDay() {
        Calendar calendar = getCurrentCalendarInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private static Calendar getCurrentCalendarInstance() {
        Date currentDate = getCurrentDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        return calendar;
    }

    private static Date getCurrentDate() {
        return new Date();
    }

}
