package vutnq.cookpadprogrammingtest.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import vutnq.cookpadprogrammingtest.R;
import vutnq.cookpadprogrammingtest.models.ListDataWeekly;
import vutnq.cookpadprogrammingtest.rest.CookpadProgrammingTestClient;
import vutnq.cookpadprogrammingtest.utils.HelperUtils;

/**
 * Created by bipug on 7/20/17.
 */

public class RecyclerViewWeeklyForecastAdapter extends RecyclerView.Adapter<RecyclerViewWeeklyForecastAdapter.RecyclerViewHolder>{
    private List<ListDataWeekly> itemList;

    public RecyclerViewWeeklyForecastAdapter(List<ListDataWeekly> itemList) {
        this.itemList = itemList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_single_day_weekly_forecast_layout,parent,false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        ListDataWeekly item = itemList.get(position);
        String dayInWeek = HelperUtils.convertUnixTimeStampToDate(item.getDt(),"EEEE");
        String date = HelperUtils.convertUnixTimeStampToDate(item.getDt(),"MMMM-dd-yyyy");
        holder.dayInWeek.setText(dayInWeek);
        holder.date.setText(date);
        holder.minTemp.setText(String.format("%s℃", String.valueOf(item.getTemp().getMin())));
        holder.maxTemp.setText(String.format("%s℃", String.valueOf(item.getTemp().getMax())));
        holder.mainWeather.setText(item.getWeather().get(0).getMain());
        Picasso.with(holder.minTemp.getContext())
                .load(CookpadProgrammingTestClient.ICON_URL+ item.getWeather().get(0).getIcon() + ".png")
                .resize(150,150)
                .into(holder.iconWeather);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        private TextView dayInWeek;
        private TextView date;
        private ImageView iconWeather;
        private TextView minTemp;
        private TextView maxTemp;
        private TextView mainWeather;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            dayInWeek = (TextView) itemView.findViewById(R.id.textViewRowDayInWeek);
            date = (TextView) itemView.findViewById(R.id.textViewRowDate);
            iconWeather = (ImageView) itemView.findViewById(R.id.imageViewRowIconWeather);
            minTemp = (TextView) itemView.findViewById(R.id.textViewRowMinTemperature);
            maxTemp = (TextView) itemView.findViewById(R.id.textViewRowMaxTemperature);
            mainWeather = (TextView) itemView.findViewById(R.id.textViewRowMainWeather);
        }
    }
}
