package tip.tdc.com.tip.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import io.realm.RealmList;
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.data.AppState;
import tip.tdc.com.tip.utils.LoggerFactory;
import com.bpackingapp.vietnam.travel.R;

/**
 * Created by phamngocthanh on 7/25/17.
 */

public class Place implements Serializable {
    long createdAt;
    long updatedAt;
    String placeKey;
    String name;// = "name"
    String address;// = "address"
    String description;//_ = "description"
    ArrayList<String> photos;// = "photos"
    ArrayList<String> categories;// = "categories"
    String email;// = "email"
    String facebook;// = "facebook"
    ArrayList<String> facilities;// = "facilities"
    String fromprice;// = "fromprice"
    String toprice;// = "toprice"
    String openingday;// = "openingday"
    String openingtime;// = "openingtime"
    String paidFacilities;
    String phonenumber;// = "phonenumber"
    ArrayList<String> subcategories;// = "subcategories"
    String thingstonote;// = "thingstonote"
    String website;// = "website"
    // location
    String longitude;
    String latitude;
    // for hotel
    String starlevel;// = "starlevel"
    String checkin;// = "checkin"
    String checkout;// = "checkout"
    String sleepBookingUrl;// = "sleepBookingUrl"
    String sleepAirbnbUrl;
    String sleepHostelWorldUrl;
    //
//    // status
    boolean deactived;// = "deactived"
    long commentCount;// = "commentCount"
    String loved;// = "loved"
    String priority;// = "priority"
    //
//    // Tour Only
    String tourLocation;// = "tourLocation"
    String tourDuration;// = "tourDuration"
    String tourLanguage;// = "tourLanguage"
    String tourTranspotation;// = "tourTranspotation"
    String tourGroupSize;// = "tourGroupSize"
    String tourBookingUrl;// = "tourBookingUrl"

    String timespend;
    String tags;
    String twitter;
    String rate;

    boolean isBanner;

    @Exclude
    String categoryName;
    @Exclude
    int categoryType;

    @Exclude
    LatLng latLng;

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getTimespend() {
        return timespend;
    }

    public void setTimespend(String timespend) {
        this.timespend = timespend;
    }

    public Place() {
    }

    public Place(SavedItem savedItem) {
        super();
        this.createdAt = savedItem.getCreatedAt();
        this.updatedAt = savedItem.getUpdatedAt();
        this.placeKey = savedItem.getSaveId();
        this.name = savedItem.getName();
        this.address = savedItem.getAddress();
        this.description = savedItem.getDescription();

        if (savedItem.getPhotos() != null && savedItem.getPhotos().length() > 0) {
            this.photos = new ArrayList<>();
            String[] photos = savedItem.getPhotos().split("####");
            for (int i = 0; i < photos.length; i++) {
                if (photos[i].length() > 0) {
                    this.photos.add(photos[i]);
                }
            }
        }
        if (savedItem.getCategories() != null && savedItem.getCategories().length() > 0) {
            this.categories = new ArrayList<>();
            String[] mCategories = savedItem.getCategories().split("####");
            for (int i = 0; i < mCategories.length; i++) {
                if (mCategories[i].length() > 0) {
                    this.categories.add(mCategories[i]);
                }
            }
        }

        this.email = savedItem.getEmail();
        this.facebook = savedItem.getFacebook();

        if (savedItem.getFacilities() != null && savedItem.getFacilities().length() > 0) {
            this.facilities = new ArrayList<>();
            String[] mFacilities = savedItem.getFacilities().split("####");
            for (int i = 0; i < mFacilities.length; i++) {
                if (mFacilities[i].length() > 0) {
                    this.facilities.add(mFacilities[i]);
                }
            }
        }

        this.fromprice = savedItem.getFromprice();
        this.toprice = savedItem.getToprice();
        this.openingday = savedItem.getOpeningday();
        this.openingtime = savedItem.getOpeningtime();
        this.paidFacilities = savedItem.getPaidFacilities();
        this.phonenumber = savedItem.getPhonenumber();

        if (savedItem.getSubcategories() != null && savedItem.getSubcategories().length() > 0) {
            this.subcategories = new ArrayList<>();
            String[] mSubCate = savedItem.getSubcategories().split("####");
            for (int i = 0; i < mSubCate.length; i++) {
                if (mSubCate[i].length() > 0) {
                    this.subcategories.add(mSubCate[i]);
                }
            }
        }

        this.thingstonote = savedItem.getThingstonote();
        this.website = savedItem.getWebsite();
        this.longitude = savedItem.getLongitude();
        this.latitude = savedItem.getLatitude();
        this.starlevel = savedItem.getStarlevel();
        this.checkin = savedItem.getCheckin();
        this.checkout = savedItem.getCheckout();
        this.sleepBookingUrl = savedItem.getSleepBookingUrl();
//        this.sleepAirbnbUrl = savedItem.sleepAirbnbUrl;
//        this.sleepHostelWorldUrl = savedItem.sleepHostelWorldUrl;
        this.deactived = savedItem.isDeactived();
        this.commentCount = savedItem.getCommentCount();
        this.loved = savedItem.getLoved();
        this.priority = savedItem.getPriority();
        this.tourLocation = savedItem.getTourLocation();
        this.tourDuration = savedItem.getTourDuration();
        this.tourLanguage = savedItem.getTourLanguage();
        this.tourTranspotation = savedItem.getTourTranspotation();
        this.tourGroupSize = savedItem.getTourGroupSize();
        this.tourBookingUrl = savedItem.getTourBookingUrl();

        this.rate = savedItem.getRate();
        this.twitter = savedItem.getTwitter();
        this.tags = savedItem.getTags();
        this.timespend = savedItem.getTimespend();
        this.categoryType = savedItem.getCategoryType();
        this.timespend = savedItem.getTimespend();

        this.categoryType = savedItem.getCategoryType();
        this.categoryName = savedItem.getCategoryName();

        this.isBanner = savedItem.isBanner();
    }



    public Place getPlace(SavedItem savedItem) {
        Place newPlace = new Place();
        newPlace.createdAt = savedItem.getCreatedAt();
        newPlace.updatedAt = savedItem.getUpdatedAt();
        newPlace.placeKey = savedItem.getSaveId();
        newPlace.name = savedItem.getName();
        newPlace.address = savedItem.getAddress();
        newPlace.description = savedItem.getDescription();

        if (newPlace.photos != null && savedItem.getPhotos().length() > 0) {
            newPlace.photos = new ArrayList<String>();
            String[] photos = savedItem.getPhotos().split("####");
            for (int i = 0; i < photos.length; i++) {
                if (photos[i].length() > 0) {
                    newPlace.photos.add(photos[i]);
                }
            }
        }
        if (savedItem.getCategories() != null && savedItem.getCategories().length() > 0) {
            newPlace.categories = new ArrayList<>();
            String[] mCategories = savedItem.getCategories().split("####");
            for (int i = 0; i < mCategories.length; i++) {
                if (mCategories[i].length() > 0) {
                    newPlace.categories.add(mCategories[i]);
                }
            }
        }

        newPlace.email = savedItem.getEmail();
        newPlace.facebook = savedItem.getFacebook();

        if (savedItem.getFacilities() != null && savedItem.getFacilities().length() > 0) {
            newPlace.facilities = new ArrayList<>();
            String[] mFacilities = savedItem.getFacilities().split("####");
            for (int i = 0; i < mFacilities.length; i++) {
                if (mFacilities[i].length() > 0) {
                    newPlace.facilities.add(mFacilities[i]);
                }
            }
        }

        newPlace.fromprice = savedItem.getFromprice();
        newPlace.toprice = savedItem.getToprice();
        newPlace.openingday = savedItem.getOpeningday();
        newPlace.openingtime = savedItem.getOpeningtime();
        newPlace.paidFacilities = savedItem.getPaidFacilities();
        newPlace.phonenumber = savedItem.getPhonenumber();

        if (savedItem.getSubcategories() != null && savedItem.getSubcategories().length() > 0) {
            newPlace.subcategories = new ArrayList<String>();
            String[] mSubCate = savedItem.getSubcategories().split("####");
            for (int i = 0; i < mSubCate.length; i++) {
                if (mSubCate[i].length() > 0) {
                    this.subcategories.add(mSubCate[i]);
                }
            }
        }

        newPlace.thingstonote = savedItem.getThingstonote();
        newPlace.website = savedItem.getWebsite();
        newPlace.longitude = savedItem.getLongitude();
        newPlace.latitude = savedItem.getLatitude();
        newPlace.starlevel = savedItem.getStarlevel();
        newPlace.checkin = savedItem.getCheckin();
        newPlace.checkout = savedItem.getCheckout();
        newPlace.sleepBookingUrl = savedItem.getSleepBookingUrl();
        newPlace.sleepAirbnbUrl = savedItem.getSleepAirbnbUrl();
        newPlace.sleepHostelWorldUrl = savedItem.getSleepHostelWorldUrl();
        newPlace.deactived = savedItem.isDeactived();
        newPlace.commentCount = savedItem.getCommentCount();
        newPlace.loved = savedItem.getLoved();
        newPlace.priority = savedItem.getPriority();
        newPlace.tourLocation = savedItem.getTourLocation();
        newPlace.tourDuration = savedItem.getTourDuration();
        newPlace.tourLanguage = savedItem.getTourLanguage();
        newPlace.tourTranspotation = savedItem.getTourTranspotation();
        newPlace.tourGroupSize = savedItem.getTourGroupSize();
        newPlace.tourBookingUrl = savedItem.getTourBookingUrl();
        newPlace.createdAt = savedItem.getCreatedAt();
        newPlace.updatedAt = savedItem.getUpdatedAt();
        newPlace.twitter = savedItem.getTwitter();
        newPlace.rate = savedItem.getRate();
        newPlace.timespend = savedItem.getTimespend();
        newPlace.tags = savedItem.getTags();
        newPlace.categoryName = savedItem.getCategoryName();
        newPlace.categoryType = savedItem.getCategoryType();

        newPlace.isBanner = savedItem.isBanner();

        return newPlace;
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

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
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

    public ArrayList<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayList<String> facilities) {
        this.facilities = facilities;
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

    public String getStarlevel() {
        return starlevel;
    }

    public void setStarlevel(String starlevel) {
        this.starlevel = starlevel;
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

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public ArrayList<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(ArrayList<String> subcategories) {
        this.subcategories = subcategories;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public boolean isBanner() {
        return isBanner;
    }

    public void setBanner(boolean banner) {
        isBanner = banner;
    }

    public boolean getIsBanner() {
        return isBanner;
    }

    public void setIsBanner(boolean banner) {
        isBanner = banner;
    }

    @Override
    public String toString() {
        return "Place{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", placeKey='" + placeKey + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", photos=" + photos +
                ", categories=" + categories +
                ", email='" + email + '\'' +
                ", facebook='" + facebook + '\'' +
                ", facilities=" + facilities +
                ", fromprice='" + fromprice + '\'' +
                ", toprice='" + toprice + '\'' +
                ", openingday='" + openingday + '\'' +
                ", openingtime='" + openingtime + '\'' +
                ", paidFacilities='" + paidFacilities + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", subcategories=" + subcategories +
                ", thingstonote='" + thingstonote + '\'' +
                ", website='" + website + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", starlevel='" + starlevel + '\'' +
                ", checkin='" + checkin + '\'' +
                ", checkout='" + checkout + '\'' +
                ", sleepBookingUrl='" + sleepBookingUrl + '\'' +
                ", sleepAirbnbUrl='" + sleepAirbnbUrl + '\'' +
                ", sleepHostelWorldUrl='" + sleepHostelWorldUrl + '\'' +
                ", deactived=" + deactived +
                ", commentCount=" + commentCount +
                ", loved='" + loved + '\'' +
                ", priority='" + priority + '\'' +
                ", tourLocation='" + tourLocation + '\'' +
                ", tourDuration='" + tourDuration + '\'' +
                ", tourLanguage='" + tourLanguage + '\'' +
                ", tourTranspotation='" + tourTranspotation + '\'' +
                ", tourGroupSize='" + tourGroupSize + '\'' +
                ", tourBookingUrl='" + tourBookingUrl + '\'' +
                ", timespend='" + timespend + '\'' +
                ", tags='" + tags + '\'' +
                ", twitter='" + twitter + '\'' +
                ", rate='" + rate + '\'' +
                ", isBanner=" + isBanner +
                ", categoryName='" + categoryName + '\'' +
                ", categoryType=" + categoryType +
                ", latLng=" + latLng +
                '}';
    }

    public String getPlaceKey() {
        return placeKey;
    }

    public void setPlaceKey(String placeKey) {
        this.placeKey = placeKey;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("address", address);
        result.put("rate", rate);
        result.put("twitter", twitter);
        result.put("tags", tags);
        result.put("timespend", timespend);
        result.put("tourBookingUrl", tourBookingUrl);
        result.put("tourGroupSize", tourGroupSize);
        result.put("tourTranspotation", tourTranspotation);
        result.put("tourLanguage", tourLanguage);
        result.put("tourDuration", tourDuration);
        result.put("tourLocation", tourLocation);
        result.put("priority", priority);
        result.put("loved", loved);
        result.put("commentCount", commentCount);
        result.put("priority", priority);
        result.put("deactived", deactived);
        result.put("sleepHostelWorldUrl", sleepHostelWorldUrl);
        result.put("sleepAirbnbUrl", sleepAirbnbUrl);
        result.put("sleepBookingUrl", sleepBookingUrl);
        result.put("checkout", checkout);
        result.put("checkin", checkin);
        result.put("starlevel", starlevel);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("website", website);
        result.put("thingstonote", thingstonote);
        result.put("subcategories", subcategories);
        result.put("phonenumber", phonenumber);
        result.put("paidFacilities", paidFacilities);
        result.put("openingtime", openingtime);
        result.put("openingday", openingday);
        result.put("toprice", toprice);
        result.put("fromprice", fromprice);
        result.put("facilities", facilities);
        result.put("facebook", facebook);
        result.put("email", email);
        result.put("categories", categories);
        result.put("photos", photos);
        result.put("updatedAt", updatedAt);
        result.put("createdAt", createdAt);
        result.put("timespend", timespend);
        result.put("tags", tags);
        result.put("twitter", twitter);
        result.put("rate", rate);
        result.put("isBanner", isBanner);

        result.put("description", description);
        return result;
    }

    public static int getPlaceMaker(Place place) {
        if (place == null)
            return R.drawable.marker_1;

        int categoryType = place.getCategoryType();

        if (categoryType <= 0) {
            if (place.getCategories() != null && place.getCategories().size() > 0) {
                categoryType = TipApplication.getTipApplication().categoryInfoService.getCategoryTypeByKey(place.getCategories().get(0));
                place.setCategoryType(categoryType);
            }
        }

        LoggerFactory.d("getPlaceMaker categoryType:" + categoryType);

        switch (categoryType) {
            case AppConstants.CATEGORY_SLEEP_TYPE:
                return R.drawable.marker_sleep;
            case AppConstants.CATEGORY_EAT_TYPE:
                return R.drawable.marker_eat;
            case AppConstants.CATEGORY_SHOPPING_TYPE:
                return R.drawable.marker_shopping;
            case AppConstants.CATEGORY_DRINK_TYPE:
                return R.drawable.marker_drink;
            case AppConstants.CATEGORY_EVENT_TYPE:
                return R.drawable.marker_event;
            case AppConstants.CATEGORY_NIGHT_LIGHT_TYPE:
                return R.drawable.marker_nightlight;
            case AppConstants.CATEGORY_SEE_DO_TYPE:
                return R.drawable.marker_see;
            case AppConstants.CATEGORY_TOUR_TYPE:
                return R.drawable.marker_tour;
            case AppConstants.CATEGORY_PLAY_TYPE:
                return R.drawable.marker_play;
        }

        return R.drawable.icon_pin_5;
    }

    public static class PlaceComparatorCreateDate implements Comparator<Place> {
        @Override
        public int compare(Place o1, Place o2) {
            Long p1 = new Long(0), p2 = new Long(0);
            p1 = o1.getCreatedAt();
            p2 = o2.getCreatedAt();
//            LoggerFactory.d("compare by create date:"+p1 + "////" + p2);
            return p2.compareTo(p1);
        }
    }

    public static class PlaceComparatorCreateDateDesc implements Comparator<Place> {
        @Override
        public int compare(Place o1, Place o2) {
            Long p1 = o1.getCreatedAt();
            Long p2 = o2.getCreatedAt();
            return p2.compareTo(p1);
        }
    }

    public static class PlaceComparatorUpdateDate implements Comparator<Place> {
        @Override
        public int compare(Place o1, Place o2) {
            Long p1 = o1.getUpdatedAt();
            Long p2 = o2.getUpdatedAt();
//            LoggerFactory.d("compare by create date:"+p1 + "////" + p2);
            return p1.compareTo(p2);
        }
    }

    public static class PlaceComparatorPriority implements Comparator<Place> {
        @Override
        public int compare(Place o1, Place o2) {
            Long p1 = new Long(0), p2 = new Long(0);
            if (o1.getPriority() != null && o1.getPriority().length() > 0) {
                p1 = Long.valueOf(o1.getPriority());
            }
            if (o2.getPriority() != null && o2.getPriority().length() > 0) {
                p2 = Long.valueOf(o2.getPriority());
            }
            return p2.compareTo(p1);
        }
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(name);
//        parcel.writeString(address);
//        parcel.writeString(description);
//        parcel.writeArray(photos.toArray());
//        parcel.writeArray(categories.toArray());
//
//        parcel.writeString(email);
//        parcel.writeString(facebook);
//        if(facilities != null)
//            parcel.writeArray(facilities.toArray());
//
//        parcel.writeString(fromprice);
//        parcel.writeString(toprice);
//        parcel.writeString(openingday);
//        parcel.writeString(openingtime);
//        parcel.writeString(paidFacilities);
//        parcel.writeString(phonenumber);
//        parcel.writeArray(subcategories.toArray());
//        parcel.writeString(thingstonote);
//        parcel.writeString(website);
//        parcel.writeString(longitude);
//        parcel.writeString(latitude);
//        parcel.writeString(starlevel);
//        parcel.writeString(checkin);
//        parcel.writeString(checkout);
//        parcel.writeString(sleepBookingUrl);
//
//
//    }
}
