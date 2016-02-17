package hou.mymirror.requests;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by hou on 2016-1-7.
 * 天气预报的请求接口
 */
public interface ForecastRequest {

    @GET("/weather")
    ForecastResponse getForecast(@Query("location")String location,@Query("ak")String apiKey,@Query("mcode")String mcode,@Query("output")String output);
}
