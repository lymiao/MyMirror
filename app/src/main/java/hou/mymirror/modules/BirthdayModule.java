package hou.mymirror.modules;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by hou on 2016-1-12.
 */
public class BirthdayModule {
    private static HashMap<String,String> mBirthdayMap;
    static {
        mBirthdayMap = new HashMap<>();
        mBirthdayMap.put("11/5","miao");
        mBirthdayMap.put("3/19","hou");
    }

    public static String getBirthday(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/D", Locale.CHINA);
        return mBirthdayMap.get(simpleDateFormat.format(new Date()));

    }
}
