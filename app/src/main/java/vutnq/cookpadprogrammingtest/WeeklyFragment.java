package vutnq.cookpadprogrammingtest;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vutnq.cookpadprogrammingtest.adapter.RecyclerViewWeeklyForecastAdapter;
import vutnq.cookpadprogrammingtest.models.ListDataWeekly;
import vutnq.cookpadprogrammingtest.models.ResponseData;
import vutnq.cookpadprogrammingtest.models.WeeklyResponseData;
import vutnq.cookpadprogrammingtest.rest.CookpadProgrammingTestClient;

/**
 * Created by bipug on 7/19/17.
 */

public class WeeklyFragment extends Fragment {
    private static final String TAG = "WeeklyFragment";

    RecyclerView recyclerView;

    WeeklyResponseData dataSave;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.weekly_fragment,container,false);
        if(savedInstanceState != null){
            dataSave = (WeeklyResponseData) savedInstanceState.getSerializable("objectData");
        }
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewWeeklyForecast);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(dataSave != null) setAdapterForRecyclerView(dataSave);

//        Call<WeeklyResponseData> call = CookpadProgrammingTestClient.getApiService().getWeeklyWeatherWithLatLon(10.767854,106.668824,CookpadProgrammingTestClient.APP_ID,"metric");
//        call.enqueue(new Callback<WeeklyResponseData>() {
//            @Override
//            public void onResponse(Call<WeeklyResponseData> call, Response<WeeklyResponseData> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<WeeklyResponseData> call, Throwable t) {
//                Log.e(TAG,t.getMessage());
//            }
//        });

        return view;
    }

    public void setAdapterForRecyclerView(WeeklyResponseData responseData){
        RecyclerViewWeeklyForecastAdapter adapter = new RecyclerViewWeeklyForecastAdapter(responseData.getList());
        recyclerView.setAdapter(adapter);
        dataSave = responseData;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(dataSave != null) outState.putSerializable("objectData",dataSave);
    }

}
