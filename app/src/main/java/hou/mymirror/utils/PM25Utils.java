package hou.mymirror.utils;

import hou.mymirror.R;

/**
 * Created by hou on 2016-1-12.
 */
public class PM25Utils {

    public static int getPm25Warning(String pm) {
        int pm25 = Integer.parseInt(pm);
        int resId = 0;
        if (pm25 <= 50) {
            resId = R.string.pm_level_1;
        } else if (pm25 <= 100) {
            resId = R.string.pm_level_2;
        } else if (pm25 <= 150) {
            resId = R.string.pm_level_3;
        } else if (pm25 <= 200) {
            resId = R.string.pm_level_4;
        } else if (pm25 <= 300) {
            resId = R.string.pm_level_5;
        } else if (pm25 <= 1000) {
            resId = R.string.pm_level_6;
        }
        return resId;
    }
}
