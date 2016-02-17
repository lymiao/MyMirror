package hou.mymirror.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hou on 2016-1-7.
 */
public class ForecastResponse {

    @SerializedName("error")
    @Expose
    public int error;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("date")
    @Expose
    public String date;
    @SerializedName("results")
    @Expose
    public List<Result> results = new ArrayList<>();

    public class Result {
        @SerializedName("currentCity")
        @Expose
        public String currentCity;
        @SerializedName("pm25")
        @Expose
        public String pm25;
        @SerializedName("index")
        @Expose
        public List<Index> index = new ArrayList<>();
        @SerializedName("weather_data")
        @Expose
        public List<WeatherDatum> weatherData = new ArrayList<>();

        /**
         * title: "紫外线强度",
         * zs: "弱",
         * tipt: "紫外线强度指数",
         * des: "紫外线强度较弱，建议出门前涂擦SPF在12-15之间、PA+的防晒护肤品。"
         */
        public class Index {
            @SerializedName("title")
            @Expose
            public String title;
            @SerializedName("zs")
            @Expose
            public String zs;
            @SerializedName("tipt")
            @Expose
            public String tipt;
            @SerializedName("des")
            @Expose
            public String des;
        }

        public class WeatherDatum {

            @SerializedName("date")
            @Expose
            public String date;
            @SerializedName("dayPictureUrl")
            @Expose
            public String dayPictureUrl;
            @SerializedName("nightPictureUrl")
            @Expose
            public String nightPictureUrl;
            @SerializedName("weather")
            @Expose
            public String weather;
            @SerializedName("wind")
            @Expose
            public String wind;
            @SerializedName("temperature")
            @Expose
            public String temperature;

            public String getWeatherCondition() {
                return weather + temperature + wind;
            }
        }

    }

}



