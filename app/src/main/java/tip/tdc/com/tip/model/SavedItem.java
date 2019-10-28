/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

package tip.tdc.com.tip.model;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by thanhpn on 06/08/2017.
 */

public class SavedItem extends RealmObject {
    @PrimaryKey
    private String saveId;
    private String cityKey;

    private int categoryType;
    private long createdAt;
    private long updatedAt;

    private String name;
    private String address;
    private String description;
    private String photos;
    private String categories;
    private String email;
    private String facebook;
    private String facilities;
    private String fromprice;
    private String toprice;
    private String openingday;
    private String openingtime;
    private String paidFacilities;
    private String phonenumber;
    private String subcategories;
    private String thingstonote;
    private String website;
    // location
    private String longitude;
    private String latitude;
    // for hotel
    private String starlevel;
    private String checkin;
    private String checkout;
    private String sleepBookingUrl;
    private String sleepAirbnbUrl;
    private String sleepHostelWorldUrl;
    // status
    private boolean deactived;
    private long commentCount;
    private String loved;
    private String priority;
    // Tour Only
    private String tourLocation;
    private String tourDuration;
    private String tourLanguage;
    private String tourTranspotation;
    private String tourGroupSize;
    private String tourBookingUrl;


    private String timespend;
    private String tags;
    private String twitter;
    private String rate;

    private String categoryName;

    private boolean isBanner;

    public SavedItem() {
    }

    public SavedItem(Place mPlace, String cityKey) {
        super();
        this.saveId = mPlace.getPlaceKey();
        this.cityKey = cityKey;
        this.name = mPlace.getName();
        this.address = mPlace.address;
        this.description = mPlace.description;

        if(mPlace.photos != null && mPlace.photos.size() > 0){
            this.photos = "";
            for (int i = 0; i < mPlace.photos.size(); i++){
                this.photos = this.photos +"####" + mPlace.photos.get(i);
            }
        }
        if(mPlace.categories != null && mPlace.categories.size() > 0){
            this.categories = "";
            for (int i = 0; i < mPlace.categories.size(); i++){
                this.categories = this.categories + "####"+mPlace.categories.get(i);
            }
        }

        this.email = mPlace.email;
        this.facebook = mPlace.facebook;

        if(mPlace.facilities != null && mPlace.facilities.size() > 0){
            this.facilities = "";
            for (int i = 0; i < mPlace.facilities.size(); i++){
                this.facilities = this.facilities + "####" + mPlace.facilities.get(i);
            }
        }

        this.fromprice = mPlace.fromprice;
        this.toprice = mPlace.toprice;
        this.openingday = mPlace.openingday;
        this.openingtime = mPlace.openingtime;
        this.paidFacilities = mPlace.paidFacilities;
        this.phonenumber = mPlace.phonenumber;

        if(mPlace.subcategories != null && mPlace.subcategories.size() > 0){
            this.subcategories = "";
            for (int i = 0; i < mPlace.subcategories.size(); i++){
                this.subcategories = this.subcategories + "####" +mPlace.subcategories.get(i);
            }
        }

        this.thingstonote = mPlace.thingstonote;
        this.website = mPlace.website;
        this.longitude = mPlace.longitude;
        this.latitude = mPlace.latitude;
        this.starlevel = mPlace.starlevel;
        this.checkin = mPlace.checkin;
        this.checkout = mPlace.checkout;
        this.sleepBookingUrl = mPlace.sleepBookingUrl;
        this.sleepAirbnbUrl = mPlace.sleepAirbnbUrl;
        this.sleepHostelWorldUrl = mPlace.sleepHostelWorldUrl;
        this.deactived = mPlace.deactived;
        this.commentCount = mPlace.commentCount;
        this.loved = mPlace.loved;
        this.priority = mPlace.priority;
        this.tourLocation = mPlace.tourLocation;
        this.tourDuration = mPlace.tourDuration;
        this.tourLanguage = mPlace.tourLanguage;
        this.tourTranspotation = mPlace.tourTranspotation;
        this.tourGroupSize = mPlace.tourGroupSize;
        this.tourBookingUrl = mPlace.tourBookingUrl;
        this.createdAt = mPlace.createdAt;
        this.updatedAt = mPlace.updatedAt;

        this.timespend = mPlace.timespend;
        this.tags = mPlace.tags;
        this.twitter = mPlace.twitter;
        this.rate = mPlace.rate;
        this.categoryName = mPlace.getCategoryName();
        this.categoryType = mPlace.getCategoryType();

        this.isBanner = mPlace.isBanner();
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getFromprice() {
        return fromprice;
    }

    public void setFromprice(String fromprice) {
        this.fromprice = fromprice;
    }

    public String getToprice() {
        return toprice;
    }

    public void setToprice(String toprice) {
        this.toprice = toprice;
    }

    public String getOpeningday() {
        return openingday;
    }

    public void setOpeningday(String openingday) {
        this.openingday = openingday;
    }

    public String getOpeningtime() {
        return openingtime;
    }

    public void setOpeningtime(String openingtime) {
        this.openingtime = openingtime;
    }

    public String getPaidFacilities() {
        return paidFacilities;
    }

    public void setPaidFacilities(String paidFacilities) {
        this.paidFacilities = paidFacilities;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }


    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public String getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(String subcategories) {
        this.subcategories = subcategories;
    }

    public String getThingstonote() {
        return thingstonote;
    }

    public void setThingstonote(String thingstonote) {
        this.thingstonote = thingstonote;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public String getSleepBookingUrl() {
        return sleepBookingUrl;
    }

    public void setSleepBookingUrl(String sleepBookingUrl) {
        this.sleepBookingUrl = sleepBookingUrl;
    }

    public boolean isDeactived() {
        return deactived;
    }

    public void setDeactived(boolean deactived) {
        this.deactived = deactived;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public String getLoved() {
        return loved;
    }

    public void setLoved(String loved) {
        this.loved = loved;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTourLocation() {
        return tourLocation;
    }

    public void setTourLocation(String tourLocation) {
        this.tourLocation = tourLocation;
    }

    public String getTourDuration() {
        return tourDuration;
    }

    public void setTourDuration(String tourDuration) {
        this.tourDuration = tourDuration;
    }

    public String getTourLanguage() {
        return tourLanguage;
    }

    public void setTourLanguage(String tourLanguage) {
        this.tourLanguage = tourLanguage;
    }

    public String getTourTranspotation() {
        return tourTranspotation;
    }

    public void setTourTranspotation(String tourTranspotation) {
        this.tourTranspotation = tourTranspotation;
    }

    public String getTourGroupSize() {
        return tourGroupSize;
    }

    public void setTourGroupSize(String tourGroupSize) {
        this.tourGroupSize = tourGroupSize;
    }

    public String getTourBookingUrl() {
        return tourBookingUrl;
    }

    public void setTourBookingUrl(String tourBookingUrl) {
        this.tourBookingUrl = tourBookingUrl;
    }


    public String getSaveId() {
        return saveId;
    }

    public void setSaveId(String saveId) {
        this.saveId = saveId;
    }

    public String getStarlevel() {
        return starlevel;
    }

    public void setStarlevel(String starlevel) {
        this.starlevel = starlevel;
    }

    public String getSleepAirbnbUrl() {
        return sleepAirbnbUrl;
    }

    public void setSleepAirbnbUrl(String sleepAirbnbUrl) {
        this.sleepAirbnbUrl = sleepAirbnbUrl;
    }

    public String getSleepHostelWorldUrl() {
        return sleepHostelWorldUrl;
    }

    public void setSleepHostelWorldUrl(String sleepHostelWorldUrl) {
        this.sleepHostelWorldUrl = sleepHostelWorldUrl;
    }

    public int getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(int categoryType) {
        this.categoryType = categoryType;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTimespend() {
        return timespend;
    }

    public void setTimespend(String timespend) {
        this.timespend = timespend;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCityKey() {
        return cityKey;
    }

    public void setCityKey(String cityKey) {
        this.cityKey = cityKey;
    }

    public boolean isBanner() {
        return isBanner;
    }

    public void setBanner(boolean banner) {
        isBanner = banner;
    }
}

