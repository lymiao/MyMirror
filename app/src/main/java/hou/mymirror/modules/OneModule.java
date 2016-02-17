package hou.mymirror.modules;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by hou on 2016-1-12.
 */
public class OneModule {

    private static String TAG = "OneModule";
    private static String ONE_URL = "http://wufazhuce.com/one/";
    private static String ONE_URL1 = "http://caodan.org/";
    private static String ONE_URL2 = "http://wufazhuce.com/";
    private static final int VERSION = 1251;
    private static Calendar mCalendar;

    static {
        mCalendar = Calendar.getInstance();
        mCalendar.clear();
        //calendar里的月份是从0开始的
        mCalendar.set(2016, 1, 15);
    }


    public interface OneContentListener {
        void onOneContent(String content);
    }

    public static void getOneContent(final OneContentListener listener) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String content = "";
                try {
                    String version = parseONEVersionByDate();
                    Document document = Jsoup.connect(ONE_URL + version).
                            timeout(5000)
                            .get();
                    Elements element = document.getElementsByClass("one-cita");
                    if (element != null) {
                        content = element.text();
                    }

                    Log.v(TAG, content);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return content;
            }

            @Override
            protected void onPostExecute(String content) {
                if (content.length() != 0) {
                    listener.onOneContent(content);
                }
            }
        }.execute();
    }


    public static String parseONEVersionByDate() {
        Log.v(TAG, "now is " + mCalendar.get(Calendar.YEAR) + "." + mCalendar.get(Calendar.MONTH) + "." + mCalendar.get(Calendar.DAY_OF_MONTH));
        Calendar calendar = Calendar.getInstance();
        Log.v(TAG, "now is " + calendar.get(Calendar.YEAR) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.DAY_OF_MONTH));
        long dayDiff = (calendar.getTime().getTime() - mCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24);
        Log.v(TAG, "dayDiff is " + dayDiff);
        return VERSION + dayDiff * 3 + "";
    }


}
