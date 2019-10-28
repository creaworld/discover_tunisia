package tip.tdc.com.tip.model;

import java.util.Date;

/**
 * Created by phamngocthanh on 7/19/17.
 */

public class ForecastDay {

    String day;
    String dayString;
    private String hourOfForecast;
    private int temperature;


    Date date;
    double tempHighF;
    double tempLowF;
    double tempHighC;
    double tempLowC;
    double pressure;
    double seaLevel;
    double grnd;
    int currentHour;
    String condition = "";
    // Icon name for condition
    String icon = "";
    // Probability of Precipitation
    String pop = "";
    String humidity = "";

    public ForecastDay(String day, String dayString, int hourOfForecast, int temperature, String icon) {
        this.day = day;
        this.dayString = dayString;
        this.temperature = temperature;
        this.hourOfForecast = hourOfForecast + ":00";
        this.icon = icon;
    }

    public String getDay() {
        return day;
    }

    public String getDayMonth() {
        return dayString;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getHourOfForecast() {
        return hourOfForecast;
    }

    public String getIcon() {
        return icon;
    }

    public double getTempHighF() {
        return tempHighF;
    }

    public void setTempHighF(double tempHighF) {
        this.tempHighF = tempHighF;
    }

    public double getTempLowF() {
        return tempLowF;
    }

    public void setTempLowF(double tempLowF) {
        this.tempLowF = tempLowF;
    }

    public double getTempHighC() {
        return tempHighC;
    }

    public void setTempHighC(double tempHighC) {
        this.tempHighC = tempHighC;
    }

    public double getTempLowC() {
        return tempLowC;
    }

    public void setTempLowC(double tempLowC) {
        this.tempLowC = tempLowC;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(double seaLevel) {
        this.seaLevel = seaLevel;
    }

    public double getGrnd() {
        return grnd;
    }

    public void setGrnd(double grnd) {
        this.grnd = grnd;
    }

    public int getCurrentHour() {
        return currentHour;
    }

    public void setCurrentHour(int currentHour) {
        this.currentHour = currentHour;
    }

    @Override
    public String toString() {
        return "ForecastDay{" +
                "day='" + day + '\'' +
                ", dayString='" + dayString + '\'' +
                ", hourOfForecast='" + hourOfForecast + '\'' +
                ", temperature=" + temperature +
                ", date=" + date +
                ", tempHighF=" + tempHighF +
                ", tempLowF=" + tempLowF +
                ", tempHighC=" + tempHighC +
                ", tempLowC=" + tempLowC +
                ", pressure=" + pressure +
                ", seaLevel=" + seaLevel +
                ", grnd=" + grnd +
                ", condition='" + condition + '\'' +
                ", icon='" + icon + '\'' +
                ", pop='" + pop + '\'' +
                ", humidity='" + humidity + '\'' +
                '}';
    }
}
