package hou.mymirror.modules;

import java.util.Calendar;

/**
 * Created by hou on 2016-1-7.
 */
public class DayModule {

    private static final String[] week = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};



    public static String getDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR) + "." +
                (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH) + "    " + getDayOfWeek();
    }

    public static String getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return week[day - 1];
    }





}
