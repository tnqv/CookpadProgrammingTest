package vutnq.cookpadprogrammingtest.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import vutnq.cookpadprogrammingtest.models.ResponseData;
import vutnq.cookpadprogrammingtest.models.WeeklyResponseData;

/**
 * Created by bipug on 7/19/17.
 */

public interface CookpadProgrammingTestService {
    @GET("/data/2.5/find")
    Call<ResponseData> getCurrentWeatherWithLatLon(@Query("lat") double lat,
                                                   @Query("lon") double lon,
                                                   @Query("APPID") String appid,
                                                   @Query("units") String metrics);
    @GET("/data/2.5/forecast/daily")
    Call<WeeklyResponseData> getWeeklyWeatherWithLatLon(@Query("lat") double lat,
                                                        @Query("lon") double lon,
                                                        @Query("APPID") String appid,
                                                        @Query("units") String metrics);
}
