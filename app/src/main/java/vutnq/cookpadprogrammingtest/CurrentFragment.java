package vutnq.cookpadprogrammingtest;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vutnq.cookpadprogrammingtest.models.ListData;
import vutnq.cookpadprogrammingtest.models.ListDataWeekly;
import vutnq.cookpadprogrammingtest.models.ResponseData;
import vutnq.cookpadprogrammingtest.models.WeeklyResponseData;
import vutnq.cookpadprogrammingtest.rest.CookpadProgrammingTestClient;
import vutnq.cookpadprogrammingtest.utils.HelperUtils;

/**
 * Created by bipug on 7/19/17.
 */

public class CurrentFragment extends Fragment {
    private static final String TAG = "CurrentFragment";

    WeeklyResponseData dataSave;
    TextView humidity,wind,tempMinMax,mainStatus,date,temp;
    TextView tomorrowTemp,tomorrowStatus;
    ImageView weatherImg,tomorrowWeatherImg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        if(savedInstanceState != null){
            dataSave = (WeeklyResponseData) savedInstanceState.getSerializable("objectData");
        }
        View view = inflater.inflate(R.layout.current_fragment,container,false);
        humidity = (TextView) view.findViewById(R.id.textViewIcHumidity);
        wind = (TextView) view.findViewById(R.id.textViewIcWind);
        tempMinMax = (TextView) view.findViewById(R.id.textViewIcTemperature);
        mainStatus = (TextView) view.findViewById(R.id.textViewMainWeatherStatus);
        date = (TextView) view.findViewById(R.id.textViewDate);
        temp = (TextView) view.findViewById(R.id.textViewTemperature);
        weatherImg = (ImageView) view.findViewById(R.id.imageViewMainTemp);
        tomorrowTemp = (TextView) view.findViewById(R.id.textViewTomorrowTemperature);
        tomorrowStatus = (TextView) view.findViewById(R.id.textViewTomorrowMainStatus);
        tomorrowWeatherImg = (ImageView) view.findViewById(R.id.imageViewTomorrow);
        if(dataSave != null) updateViewFragmentWhenUserSelectLocation(dataSave);
        return view;
    }

    public void loadData(WeeklyResponseData a){
        this.dataSave = a;
    }

    public void updateViewFragmentWhenUserSelectLocation(WeeklyResponseData responseWeeklyData){
        //responseFrag = response;
        // dataSave = responseWeeklyData;
        loadData(responseWeeklyData);
        List<ListDataWeekly> item = dataSave.getList();
        humidity.setText(String.valueOf(item.get(0).getHumidity()));
        wind.setText(String.valueOf(item.get(0).getSpeed()));
        tempMinMax.setText(String.format("%s℃/%s℃", String.valueOf(item.get(0).getTemp().getMin()), String.valueOf(item.get(0).getTemp().getMax())));
        mainStatus.setText(item.get(0).getWeather().get(0).getMain());
        String dateString = HelperUtils.convertUnixTimeStampToDate(item.get(0).getDt(),"MMMMMM dd,yyyy");
        date.setText(dateString);
        temp.setText(String.format("%s℃", String.valueOf(item.get(0).getTemp().getDay())));
        Picasso.with(getActivity().getApplicationContext())
                .load(String.format("%s%s.png", CookpadProgrammingTestClient.ICON_URL, item.get(0).getWeather().get(0).getIcon()))
                .resize(150,150)
                .into(weatherImg);
        //For tomorrow
        tomorrowTemp.setText(String.format("%s\u2103 - %s\u2103",String.valueOf(item.get(1).getTemp().getMin()), String.valueOf(item.get(1).getTemp().getMax())));
        tomorrowStatus.setText(item.get(1).getWeather().get(0).getMain());
        Picasso.with(getActivity().getApplicationContext())
                .load(String.format("%s%s.png", CookpadProgrammingTestClient.ICON_URL, item.get(1).getWeather().get(0).getIcon()))
                .resize(150,150)
                .into(tomorrowWeatherImg);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(dataSave != null) outState.putSerializable("objectData",dataSave);
    }
}
