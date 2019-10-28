package tip.tdc.com.tip.model;

/**
 * Created by phamngocthanh on 7/17/17.
 */

public class WeatherDay {
    String day;
    String tempLo;
    String tempHi;
    String tempStatus;
    String tempThumb;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTempHi() {
        return tempHi;
    }

    public void setTempHi(String tempHi) {
        this.tempHi = tempHi;
    }

    public String getTempLo() {
        return tempLo;
    }

    public void setTempLo(String tempLo) {
        this.tempLo = tempLo;
    }

    public String getTempStatus() {
        return tempStatus;
    }

    public void setTempStatus(String tempStatus) {
        this.tempStatus = tempStatus;
    }

    public String getTempThumb() {
        return tempThumb;
    }

    public void setTempThumb(String tempThumb) {
        this.tempThumb = tempThumb;
    }
}
