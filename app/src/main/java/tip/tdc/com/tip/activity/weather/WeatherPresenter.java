package tip.tdc.com.tip.activity.weather;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import az.openweatherapi.listener.OWRequestListener;
import az.openweatherapi.model.OWResponse;
import az.openweatherapi.model.gson.common.Coord;
import az.openweatherapi.model.gson.current_day.CurrentWeather;
import az.openweatherapi.model.gson.five_day.ExtendedWeather;
import az.openweatherapi.model.gson.five_day.WeatherForecastElement;
import az.openweatherapi.utils.OWSupportedUnits;
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.model.ForecastDay;
import tip.tdc.com.tip.utils.LoggerFactory;

/**
 * Presenter of the ColorWeather main screen.
 */
public class WeatherPresenter implements WeatherContract.Presenter {

    private static final String TAG = WeatherPresenter.class.getSimpleName();
    public static final int TEN_MINUTES = 600000;
    public static final String CLOUDY = "cloudy";
    public static final String SCATTERED_CLOUDS = "scattered clouds";
    public static final String MIST = "mist";
    public static final String CLEAR_SKY = "clear sky";
    public static final String SHOWER_RAIN = "shower rain";
    public static final String RAINY = "rainy";
    public static final String SNOW = "snow";
    public static final String FEW_CLOUD = "few clouds";
    public static final String THUNDERSTORM = "thunderstorm";
    public static final String PARTIALLY_CLOUDY = "partially cloudy";
    private WeatherContract.View view;
    private ArrayList<ForecastDay> mFiveDayForecast;
    private ArrayList<ForecastDay> mDayForecast;
    private Date mLastRetrievedDateStamp;
    private CurrentWeather mCurrentWeather;

    public WeatherPresenter(WeatherContract.View view, Locale locale) {
        this.view = view;
    }

    @Override
    public void getFiveDayForecast(final Coord coordinate) {
        if (canUpdateForecast()) {
            TipApplication.getTipApplication().mOWService.getFiveDayForecast(coordinate, new OWRequestListener<ExtendedWeather>() {
                @Override
                public void onResponse(OWResponse<ExtendedWeather> response) {
                    ExtendedWeather extendedWeather = response.body();
                    if (extendedWeather!= null) {
                        LoggerFactory.d("extendedWeather:::" + extendedWeather.toString());
                        LoggerFactory.d("extendedWeather::getCity:" + extendedWeather.getCity().getName());
                        mFiveDayForecast = filterTemps(extendedWeather);
                        mDayForecast = filterUpcomingFiveForecasts(extendedWeather);
                        view.updateFiveDayForecast(mFiveDayForecast);
                        mLastRetrievedDateStamp = new Date(System.currentTimeMillis());
                    }

                    getCurrentForecast(coordinate);
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e(TAG, "Five Day Forecast request failed: " + t.getMessage());
                }
            });
        } else {
            view.updateFiveDayForecast(mFiveDayForecast);
            view.updateTodayForecast(mCurrentWeather);
        }
    }

    private boolean canUpdateForecast() {
        if (mLastRetrievedDateStamp != null && mFiveDayForecast != null) {
            return System.currentTimeMillis() - mLastRetrievedDateStamp.getTime() > TEN_MINUTES;
        } else {
            return true;
        }
    }

    /**
     * Retrieves extended forecast for today.
     */
    @Override
    public void getCurrentDayExtendedForecast() {
        view.updateCurrentDayExtendedForecast(mDayForecast);
    }

    /**
     * Retrieves current moment forecast.
     */
    private void getCurrentForecast(final Coord coordinate) {
        TipApplication.getTipApplication().mOWService.getCurrentDayForecast(coordinate, new OWRequestListener<CurrentWeather>() {
            @Override
            public void onResponse(OWResponse<CurrentWeather> response) {
                mCurrentWeather = response.body();
                view.updateTodayForecast(mCurrentWeather);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "Current Day Forecast request failed: " + t.getMessage());
            }
        });
    }

    @Override
    public Temperature getColorForTemp(int temp) {
        Temperature tempColor = Temperature.OK;
        if (TipApplication.getTipApplication().mOWService.getSelectedMetricSystem() == OWSupportedUnits.METRIC) {
            if (temp > 28) {
                tempColor = Temperature.SUPER_HOT;
            } else if (temp > 26 & temp < 28) {
                tempColor = Temperature.MEDIUM_HOT;
            } else if (temp > 23 & temp < 26) {
                tempColor = Temperature.HOT;
            } else if (temp > 21 & temp < 23) {
                tempColor = Temperature.OK;
            } else if (temp > 15 & temp < 21) {
                tempColor = Temperature.OK_CHILL;
            } else if (temp < 15) {
                tempColor = Temperature.COLD;
            }
        } else if (TipApplication.getTipApplication().mOWService.getSelectedMetricSystem() == OWSupportedUnits.FAHRENHEIT) {
            if (temp > 84) {
                tempColor = Temperature.SUPER_HOT;
            } else if (temp > 80 & temp < 84) {
                tempColor = Temperature.MEDIUM_HOT;
            } else if (temp > 74 & temp < 79) {
                tempColor = Temperature.HOT;
            } else if (temp > 70 & temp < 74) {
                tempColor = Temperature.OK;
            } else if (temp > 60 & temp < 70) {
                tempColor = Temperature.OK_CHILL;
            } else if (temp < 60) {
                tempColor = Temperature.COLD;
            }
        }
        return tempColor;
    }

    private ArrayList<ForecastDay> filterTemps(ExtendedWeather extendedWeather) {
        ArrayList<ForecastDay> curatedFiveDayForecast = new ArrayList<>();
        Map<Integer, ForecastDay> sortedTemperaturesMap = new TreeMap<>();
        if(extendedWeather != null) {

            for (WeatherForecastElement element : extendedWeather.getList()) {

                Date parsedDate = new Date();
                try {
                    parsedDate = TipApplication.getTipApplication().weatherDateStampFormat.parse(element.getDtTxt());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parsedDate);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

                int roundedTemp = (int) Math.round(element.getMain().getTemp());
                String dayName = TipApplication.getTipApplication().dayFormat.format(parsedDate);

                ForecastDay tempForecast = new ForecastDay(dayName,
                        "" + currentDay + "/" + currentMonth,
                        currentHour,
                        roundedTemp,
                        parseWeatherDescription(element.getWeather().get(0).getDescription()));

                tempForecast.setTempHighC(element.getMain().getTempMax());
                tempForecast.setTempLowC(element.getMain().getTempMin());
                tempForecast.setPressure(element.getMain().getPressure());
                tempForecast.setSeaLevel(element.getMain().getSeaLevel());
                tempForecast.setGrnd(element.getMain().getGrndLevel());
                tempForecast.setCurrentHour(currentHour);

                //If it is the first temp of the day, then add it
                if (sortedTemperaturesMap.get(currentDay) == null) {
                    sortedTemperaturesMap.put(currentDay, tempForecast);
                    LoggerFactory.d("Current day null -> add map: "+ tempForecast.toString());
                    //Otherwise check if the current checked temp for this day is greater than
                    //the already stored one, if that's true, then replace the current day greatest temp.
                } else if (sortedTemperaturesMap.get(currentDay) != null ) {
                    ForecastDay savedForecastDay = sortedTemperaturesMap.get(currentDay);
                    if(tempForecast.getTempHighC() > savedForecastDay.getTempHighC()) {
                        savedForecastDay.setTempHighC(tempForecast.getTempHighC());
                    }

                    if(tempForecast.getTempLowC() < savedForecastDay.getTempLowC()) {
                        savedForecastDay.setTempLowC(tempForecast.getTempLowC());
                    }

                    sortedTemperaturesMap.put(currentDay, savedForecastDay);

                    LoggerFactory.d("roundedTemp > sortedTemperaturesMap -> add map: "+ tempForecast.toString());
                } else {
                    LoggerFactory.e("Not add to Map");
                }
            }

            for (Map.Entry<Integer, ForecastDay> entry : sortedTemperaturesMap.entrySet()) {
                curatedFiveDayForecast.add(entry.getValue());
            }
        }

        return curatedFiveDayForecast;
    }

    private ArrayList<ForecastDay> filterUpcomingFiveForecasts(ExtendedWeather extendedWeather) {
        ArrayList<ForecastDay> upcomingFiveForecasts = new ArrayList();

        int currentDayCount = 0;

        for (WeatherForecastElement element : extendedWeather.getList()) {

            Date parsedDate = new Date();
            try {
                parsedDate = TipApplication.getTipApplication().weatherDateStampFormat.parse(element.getDtTxt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
            int currentMonth = calendar.get(Calendar.MONTH) + 1;
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

            int roundedTemp = (int) Math.round(element.getMain().getTemp());
            String dayName = TipApplication.getTipApplication().dayFormat.format(parsedDate);

            ForecastDay tempForecast = new ForecastDay(dayName,
                    "" + currentDay + "/" + currentMonth,
                    currentHour,
                    roundedTemp,
                    parseWeatherDescription(element.getWeather().get(0).getDescription()));
            tempForecast.setTempHighC(element.getMain().getTempMax());
            tempForecast.setTempLowC(element.getMain().getTempMin());
            tempForecast.setPressure(element.getMain().getPressure());
            tempForecast.setSeaLevel(element.getMain().getSeaLevel());
            tempForecast.setGrnd(element.getMain().getGrndLevel());
            tempForecast.setCurrentHour(currentHour);

            upcomingFiveForecasts.add(tempForecast);

            currentDayCount++;

            if (currentDayCount == 7) {
                break;
            }
        }

        return upcomingFiveForecasts;
    }

    private String parseWeatherDescription(String description) {
        switch (description) {
            case "mist":
                return MIST;
            case "scattered clouds":
                return SCATTERED_CLOUDS;
            case "clear sky":
                return CLEAR_SKY;
            case "shower rain":
                return SHOWER_RAIN;
            case "rain":
                return RAINY;
            case "thunderstorm":
                return THUNDERSTORM;
            case "snow":
                return SNOW;
            case "few clouds":
                return FEW_CLOUD;
            case "broken clouds":
                return PARTIALLY_CLOUDY;
        }
        return CLOUDY;
    }
}
