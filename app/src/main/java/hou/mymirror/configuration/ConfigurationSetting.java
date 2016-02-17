package hou.mymirror.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Created by hou on 2016-1-6.
 */
public class ConfigurationSetting {

    private static final String PREFS_MIRROR = "MirrorPrefs";
    private static final String LAT = "lat";
    private static final String LON = "lon";
    private static final String CITY = "city";
    private static final String DETAILADDRESS = "detailAddress";

    private static final String LAST_PICTURE_PATH = "LastPicturePath";


    private static final String USE_MOOD_DETECTION = "mood_detection";

    @NonNull
    private SharedPreferences sp;

    private boolean mShowMoodDetection;

    private String latitude;
    private String longitude;


    private String city;
    private String detailAddress;

    private String mLastPicturePath;


    public ConfigurationSetting(Context context) {
        sp = context.getSharedPreferences(PREFS_MIRROR, Context.MODE_PRIVATE);
        readConfiguration();
    }

    private void readConfiguration() {
        mShowMoodDetection = sp.getBoolean(USE_MOOD_DETECTION, false);
        latitude = sp.getString(LAT, "");
        longitude = sp.getString(LON, "");
        city = sp.getString(CITY, "");
        detailAddress = sp.getString(DETAILADDRESS, "");
        mLastPicturePath = sp.getString(LAST_PICTURE_PATH, "");
    }


    public void setShowMoodDetection(boolean show) {
        mShowMoodDetection = show;
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(USE_MOOD_DETECTION, show);
        editor.apply();
    }

    public boolean isShowMoodDetection() {
        return mShowMoodDetection;
    }


    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }


    public void setLatLon(String lat, String lon) {
        latitude = lat;
        longitude = lon;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LAT, latitude);
        editor.putString(LON, longitude);
        editor.apply();
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CITY, city);
        editor.apply();
    }


    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(DETAILADDRESS, detailAddress);
        editor.apply();
    }


    public String getLastPicturePath() {
        return mLastPicturePath;
    }

    public void setLastPicturePath(String mLastPicturePath) {
        this.mLastPicturePath = mLastPicturePath;
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(LAST_PICTURE_PATH, mLastPicturePath);
        editor.apply();
    }
}
