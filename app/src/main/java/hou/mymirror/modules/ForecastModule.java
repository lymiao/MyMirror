package hou.mymirror.modules;

import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import hou.mymirror.R;
import hou.mymirror.requests.ForecastResponse;
import hou.mymirror.utils.PM25Utils;

/**
 * Created by hou on 2016-1-7.
 */
public class ForecastModule {

    private static final String TAG = "ForecastModule";
    private static final String URL = "http://api.map.baidu.com/telematics/v3/weather";


    public interface ForecastListener {
        void onWeatherToday(String weatherToday, String pm, int pmWarning);
    }

    public static void getCurrentForecast(final Resources resources, final String latitude, final String longitude, final ForecastListener listener) {
        new AsyncTask<Void, Void, ForecastResponse>() {

            @Override
            protected ForecastResponse doInBackground(Void... params) {
                return formatResponse(resources, latitude, longitude);
            }


            @Override
            protected void onPostExecute(ForecastResponse forecastResponse) {
                ForecastResponse.Result result = forecastResponse.results.get(0);
                String temperature = result.weatherData.get(0).temperature + "   " + result.weatherData.get(0).weather;
                listener.onWeatherToday(temperature, result.pm25, PM25Utils.getPm25Warning(result.pm25));
            }
        }.execute();
    }

    /*
    * 用GSON解析得到的json数据
    */
    public static ForecastResponse formatResponse(Resources resources, String latitude, String longitude) {
        String url = setParams(resources, latitude, longitude);
        String jsonRes = doGet(url);
        Log.v(TAG, jsonRes);
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

    private static String setParams(Resources resources, String latitude, String longitude) {
        String url = "";
        try {
            url = URL + "?location=" + longitude + "," + latitude +
                    "&output=json&ak=" + resources.getString(R.string.api_key) + "&mcode=" + resources.getString(R.string.code);
            Log.v(TAG, "url = " + url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }


    //        new AsyncTask<Void, Void, ForecastResponse>() {
//            @Override
//            protected ForecastResponse doInBackground(Void... params) {
//                RestAdapter restAdapter = new RestAdapter.Builder()
//                        .setEndpoint("http://api.map.baidu.com/telematics/v3")
//                        .build();
//                ForecastRequest service = restAdapter.create(ForecastRequest.class);
//
//                return service.getForecast(location, resources.getString(R.string.api_key),
//                        resources.getString(R.string.code),resources.getString(R.string.output));
//
//            }
//
//            @Override
//            protected void onPostExecute(ForecastResponse forecastResponse) {
//                if (forecastResponse != null) {
//                    Log.v("LLLLLLLLLL", forecastResponse.toString());
//                    Log.v("LLLLLLLLLL", "status" + forecastResponse.status);
//                    Log.v("LLLLLLLLLL", "error" + forecastResponse.error);
//                    Log.v("LLLLLLLLLL", "date" + forecastResponse.date);
//                    if (forecastResponse.results.size() != 0) {
//                        ForecastResponse.Result result = forecastResponse.results.get(0);
//                        String weather = result.pm25 + result.weatherData.get(0).wind + result.weatherData.get(0).temperature;
//                        listener.onWeatherToday(weather);
//                        Log.v("LLLLLLLLLL", weather);
//                    } else {
//                        Log.v("LLLLLLLLLL", "result size is 0");
//                    }
//                }
//            }
//        }.execute();

}
