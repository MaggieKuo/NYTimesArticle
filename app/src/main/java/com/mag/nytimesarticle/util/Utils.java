package com.mag.nytimesarticle.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by perfumekuo on 2017/2/24.
 */

public class Utils {
    public static String getStringDate(int day){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        if (day!=0)
            date = getDate(new Date(), Calendar.DATE, day);
        return sdf.format(date);
    }

    public static Date getDate(Date date, int ymd, int diff){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(ymd) + diff);
        return calendar.getTime();

    }
}
