package tip.tdc.com.tip.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by phamngocthanh on 7/17/17.
 */

public class DateUtils {
    public static Date randomDate(){
        SimpleDateFormat dfDateTime  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        int year = randBetween(1900, 2013);// Here you can set Range of years you need
        int month = randBetween(0, 11);
        int hour = randBetween(9, 22); //Hours will be displayed in between 9 to 22
        int min = randBetween(0, 59);
        int sec = randBetween(0, 59);


        Calendar gc = Calendar.getInstance();
        gc.set(year, month, 1);
        int day = randBetween(1, gc.getActualMaximum(gc.DAY_OF_MONTH));

        gc.set(year, month, day, hour, min,sec);

        return gc.getTime();
    }

    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }


    static SimpleDateFormat dfComments  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
    public static String convertDateToStringComment(Date mDate){
        return  dfComments.format(mDate);
    }
}
