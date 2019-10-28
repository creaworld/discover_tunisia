package tip.tdc.com.tip.activity.weather;

import java.util.ArrayList;

import az.openweatherapi.model.gson.common.Coord;
import az.openweatherapi.model.gson.current_day.CurrentWeather;
import tip.tdc.com.tip.model.ForecastDay;

/**
 * Actions that can should be performed by both the View and its Presenter.
 */
public interface WeatherContract {

    interface View {
        void updateFiveDayForecast(ArrayList<ForecastDay> weatherForecastElement);

        void updateCurrentDayExtendedForecast(ArrayList<ForecastDay> currentWeather);

        void updateTodayForecast(CurrentWeather currentWeather);
    }

    interface Presenter {
        void getFiveDayForecast(final Coord coordinate);

        void getCurrentDayExtendedForecast();

        Temperature getColorForTemp(int temp);
    }
}
