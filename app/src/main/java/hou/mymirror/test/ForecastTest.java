package hou.mymirror.test;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import hou.mymirror.requests.ForecastResponse;

/**
 * Created by hou on 2016-1-8.
 */
public class ForecastTest {


    private static final String URL = "http://apis.baidu.com/heweather/weather/free";
    private static final String URL1 = "http://api.map.baidu.com/telematics/v3/weather";
    private static final String MCODE = "E3:21:3E:48:58:27:D9:84:A8:C5:95:BF:1A:43:33:71:E5:01:FD:1D;hou.mymirror";
    private static final String API_KEY = "42ec5de2548de34ceae0baee5c631742";
    private static final String API_KEY1 = "SHGUnplH6peQ8DH8KyXaMmOU";

    /*
     * 用GSON解析得到的json数据
     */
    public static ForecastResponse formatResponse(String latitude ,String longitude) {
        String url = setParams(latitude,longitude);
        String jsonRes = doGet(url);
        Gson gson = new Gson();
        ForecastResponse result = null;
        try {
            result = gson.fromJson(jsonRes, ForecastResponse.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String doGet(String url) {
        String result = "";
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            java.net.URL urlNet = new java.net.URL(url);
            try {
                HttpURLConnection con = (HttpURLConnection) urlNet
                        .openConnection();
                con.setReadTimeout(5 * 1000);
                con.setConnectTimeout(5 * 1000);
                con.setRequestMethod("GET");
                is = con.getInputStream();
                int length = -1;
                byte[] buf = new byte[256];
                baos = new ByteArrayOutputStream();
                while ((length = is.read(buf)) != -1) {
                    baos.write(buf, 0, length);
                }
                baos.flush();
                result = new String(baos.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;

    }

    private static String setParams(String latitude,String longitue) {
        String url = "";
        try {
            url = URL1 + "?location="
                    + longitue+","+latitude+ "&output=json&ak=" + API_KEY1 + "&mcode=" + MCODE;
            Log.v("LLLLLLLL", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }


}
