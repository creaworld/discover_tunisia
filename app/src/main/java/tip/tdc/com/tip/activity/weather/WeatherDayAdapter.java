package tip.tdc.com.tip.activity.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.ArrayList;
import java.util.Calendar;

import tip.tdc.com.tip.model.ForecastDay;
import tip.tdc.com.tip.utils.FontUtils;
import tip.tdc.com.tip.utils.LoggerFactory;
import tip.tdc.com.tip.utils.Utils;
import com.bpackingapp.vietnam.travel.R;

public class WeatherDayAdapter extends ArrayAdapter<Integer> {

    private final Context mContext;
    private ArrayList<ForecastDay> weatherDays;

    public WeatherDayAdapter(final Context context) {
        mContext = context;
    }

    public ArrayList<ForecastDay> getWeatherDays() {
        return weatherDays;
    }

    public void setWeatherDays(ArrayList<ForecastDay> weatherDays) {
        this.weatherDays = weatherDays;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_list_weather_day, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvDay = (TextView) view.findViewById(R.id.tvDay);
            FontUtils.setFont(viewHolder.tvDay, FontUtils.TYPE_NORMAL);
            viewHolder.tvTempLo = (TextView) view.findViewById(R.id.tvTempLo);
            FontUtils.setFont(viewHolder.tvTempLo, FontUtils.TYPE_LIGHT);
            viewHolder.tvTempHi = (TextView) view.findViewById(R.id.tvTempHi);
            FontUtils.setFont(viewHolder.tvTempHi, FontUtils.TYPE_LIGHT);
            viewHolder.tvWeatherStatus = (TextView) view.findViewById(R.id.tvWeatherStatus);
            FontUtils.setFont(viewHolder.tvWeatherStatus, FontUtils.TYPE_LIGHT);

            view.setTag(viewHolder);

            viewHolder.imvWeatherThumb = (ImageView) view.findViewById(R.id.imvWeatherThumb);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ForecastDay forecastDay = weatherDays.get(position);
        LoggerFactory.d(forecastDay.getDay()+"");
        LoggerFactory.d(forecastDay.getDayMonth()+"");
        LoggerFactory.d(forecastDay.getHourOfForecast()+"");
        LoggerFactory.d(forecastDay.getIcon()+"");
        LoggerFactory.d(forecastDay.getTemperature()+"");
        LoggerFactory.d("getTempLowC"+forecastDay.getTempLowC()+"");
        LoggerFactory.d("getTempHighC"+forecastDay.getTempHighC()+"");

        viewHolder.tvDay.setText(forecastDay.getDay()+"");
        viewHolder.tvTempLo.setText(String.format(mContext.getString(R.string.degrees_placeholderC), (int)forecastDay.getTempLowC()));
        viewHolder.tvTempHi.setText(String.format(mContext.getString(R.string.degrees_placeholderC), (int)forecastDay.getTempHighC()));
        viewHolder.tvWeatherStatus.setText(forecastDay.getIcon()+"");
        Utils.setupWeatherIcon(viewHolder.imvWeatherThumb, forecastDay.getIcon(), Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        return view;
    }



    @Override
    public int getCount() {
        if(weatherDays == null)
            return 0;
        return weatherDays.size();
    }

    private static class ViewHolder {
        TextView tvDay;
        TextView tvTempLo;
        TextView tvTempHi;
        TextView tvWeatherStatus;
        ImageView imvWeatherThumb;
    }
}