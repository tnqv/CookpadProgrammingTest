package vutnq.cookpadprogrammingtest.rest;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bipug on 7/19/17.
 */

public class CookpadProgrammingTestClient {
    private static final String API_URL = "http://api.openweathermap.org/";
    public static final String APP_ID = "fa27cf3819fcfcb55dfa3c82876609f4";
    public static final String ICON_URL = "http://openweathermap.org/img/w/";


    /**
     * Get retrofit instance
     */
    private static Retrofit getRetrofitInstance(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60,TimeUnit.SECONDS)
                .writeTimeout(60,TimeUnit.SECONDS)
                .build();
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * get API service
     */
    public static CookpadProgrammingTestService getApiService(){
        return getRetrofitInstance().create(CookpadProgrammingTestService.class);
    }
}
