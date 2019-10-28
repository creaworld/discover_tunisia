package tip.tdc.com.tip.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

/**
 * Created by phamngocthanh on 7/19/17.
 */
@IgnoreExtraProperties
public class FCity implements Serializable{
    String name ;
    String intro;
    String photourl;
    String latitude;
    String longitude;
    String zoom;
    String priority;
    String weatherUrl;
    String applePurchaseId;
    String googlePurchaseId;
    String videoIntroUrl;
    String bannerPhotoUrl;
    String bannerUrl;
    boolean deactived;

    @Exclude
    String cityKey;

    public FCity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getZoom() {
        return zoom;
    }

    public void setZoom(String zoom) {
        this.zoom = zoom;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getWeatherUrl() {
        return weatherUrl;
    }

    public void setWeatherUrl(String weatherUrl) {
        this.weatherUrl = weatherUrl;
    }

    public String getVideoIntroUrl() {
        return videoIntroUrl;
    }

    public void setVideoIntroUrl(String videoIntroUrl) {
        this.videoIntroUrl = videoIntroUrl;
    }

    public String getApplePurchaseId() {
        return applePurchaseId;
    }

    public void setApplePurchaseId(String applePurchaseId) {
        this.applePurchaseId = applePurchaseId;
    }

    public boolean isDeactived() {
        return deactived;
    }

    public void setDeactived(boolean deactived) {
        this.deactived = deactived;
    }

    public String getCityKey() {
        return cityKey;
    }

    public void setCityKey(String cityKey) {
        this.cityKey = cityKey;
    }

    public String getGooglePurchaseId() {
        return googlePurchaseId;
    }

    public void setGooglePurchaseId(String googlePurchaseId) {
        this.googlePurchaseId = googlePurchaseId;
    }

    public String getBannerPhotoUrl() {
        return bannerPhotoUrl;
    }

    public void setBannerPhotoUrl(String bannerPhotoUrl) {
        this.bannerPhotoUrl = bannerPhotoUrl;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    @Override
    public String toString() {
        return "FCity{" +
                "name='" + name + '\'' +
                ", intro='" + intro + '\'' +
                ", photourl='" + photourl + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", zoom='" + zoom + '\'' +
                ", priority='" + priority + '\'' +
                ", weatherUrl='" + weatherUrl + '\'' +
                ", applePurchaseId='" + applePurchaseId + '\'' +
                ", googlePurchaseId='" + googlePurchaseId + '\'' +
                ", videoIntroUrl='" + videoIntroUrl + '\'' +
                ", bannerPhotoUrl='" + bannerPhotoUrl + '\'' +
                ", bannerUrl='" + bannerUrl + '\'' +
                ", deactived=" + deactived +
                ", cityKey='" + cityKey + '\'' +
                '}';
    }
}
