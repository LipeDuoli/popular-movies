package com.exemple.android.popularmovies.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String formatDate(Date date) {
        DateFormat dateInstance = SimpleDateFormat.getDateInstance();
        return dateInstance.format(date);
    }

    public static String formatDate(long longDate){
        Date date = new Date(longDate);
        return DateUtils.formatDate(date);
    }

    public static long dateToLong(Date date){
        return date.getTime();
    }
}
