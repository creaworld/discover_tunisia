package tip.tdc.com.tip.service;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.services.common.SafeToast;
import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.data.AppConstants;
import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.model.Place;
import tip.tdc.com.tip.utils.LoggerFactory;

/**
 * Created by phamngocthanh on 7/24/17.
 */

public class PlaceService implements ChildEventListener, ValueEventListener {
    private DatabaseReference mPreference;
    private Place mPlace;

    public PlaceService(String cityKey) {
        mPreference = FirebaseDatabase.getInstance().getReference("place" + "/" + cityKey);
        mPreference.keepSynced(true);
        mPreference.addChildEventListener(this);
        mPreference.addValueEventListener(this);
    }

    public Place getmPlace() {
        return mPlace;
    }

    public void setmPlace(Place mPlace) {
        this.mPlace = mPlace;
    }

    public void configureDatabase() {

    }

    private Query mQuery;
    private ChangeEventListener mListener;
    private List<DataSnapshot> mSnapshots = new ArrayList<>();

    public PlaceService(Query ref) {
        mQuery = ref;
        mQuery.addChildEventListener(this);
        mQuery.addValueEventListener(this);
    }

    public void updateQuery(Query ref) {
        if (mQuery != null) {
            cleanup();
        }
        mQuery = ref;
        mQuery.addChildEventListener(this);
        mQuery.addValueEventListener(this);
    }

    public void cleanup() {
        mQuery.removeEventListener((ValueEventListener) this);
        mQuery.removeEventListener((ChildEventListener) this);
    }

    public int getCount() {
        return mSnapshots.size();
    }

    public DataSnapshot getItem(int index) {
        return mSnapshots.get(index);
    }

    public int getIndexOfItem(DataSnapshot snapshot) {
        return mSnapshots.indexOf(snapshot);
    }

    private int getIndexForKey(String key) {
        int index = 0;
        for (DataSnapshot snapshot : mSnapshots) {
            if (snapshot.getKey().equals(key)) {
                return index;
            } else {
                index++;
            }
        }
        throw new IllegalArgumentException("Key not found");
    }

    public boolean existedIndexForKey(String key) {
        for (DataSnapshot snapshot : mSnapshots) {
            if (snapshot.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public DataSnapshot snapshotForKey(String key) {
        for (DataSnapshot snapshot : mSnapshots) {
            if (snapshot.getKey().equals(key)) {
                return snapshot;
            }
        }
        return null;
    }

    @Override
    public void onChildAdded(DataSnapshot snapshot, String previousChildKey) {
        if (existedIndexForKey(snapshot.getKey())) {
            return;
        }
        int index = 0;
        if (previousChildKey != null) {
            index = getIndexForKey(previousChildKey) + 1;
        }
        mSnapshots.add(index, snapshot);
        notifyChangedListeners(ChangeEventListener.EventType.ADDED, index);
    }

    @Override
    public void onChildChanged(DataSnapshot snapshot, String previousChildKey) {
        int index = getIndexForKey(snapshot.getKey());
        mSnapshots.set(index, snapshot);
        notifyChangedListeners(ChangeEventListener.EventType.CHANGED, index);
    }

    @Override
    public void onChildRemoved(DataSnapshot snapshot) {
        int index = getIndexForKey(snapshot.getKey());
        mSnapshots.remove(index);
        notifyChangedListeners(ChangeEventListener.EventType.REMOVED, index);
    }

    @Override
    public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
        int oldIndex = getIndexForKey(snapshot.getKey());
        mSnapshots.remove(oldIndex);
        int newIndex = previousChildKey == null ? 0 : (getIndexForKey(previousChildKey) + 1);
        mSnapshots.add(newIndex, snapshot);
        notifyChangedListeners(ChangeEventListener.EventType.MOVED, newIndex, oldIndex);
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (mListener != null)
            mListener.onDataChanged();
    }

    @Override
    public void onCancelled(DatabaseError error) {
        notifyCancelledListeners(error);
    }

    public void setOnChangedListener(ChangeEventListener listener) {
        mListener = listener;
    }

    Place emergencyPlace;

    public Place getEmergencyPlace() {
        if (emergencyPlace != null)
            return emergencyPlace;

        final String categoryEmergencyKey = TipApplication.getTipApplication().categoryInfoService.getCategoryKeyByType(AppConstants.CATEGORY_EMERGENCY_TYPE);
        for (int i = 0; i < getCount(); i++) {
            try {
                if (getItem(i).getValue(Place.class) != null) {
                    if (categoryEmergencyKey != null && categoryEmergencyKey.length() > 0 && getItem(i).getValue(Place.class).getCategories().toString().contains(categoryEmergencyKey)) {
                        LoggerFactory.e(">>>>>>>>>>>>>>>>>>>>>>> parse Emergency info <<<<<<<<<<<<<<<<<<<<<<<");
                        emergencyPlace = getItem(i).getValue(Place.class);
                        emergencyPlace.setPlaceKey(getItem(i).getKey());

                        return emergencyPlace;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public Place getPlaceByKey(String placeKey) {
        for (int i = 0; i < getCount(); i++) {
            try {
                if (getItem(i).getKey().contains(placeKey)) {
                    Place place = getItem(i).getValue(Place.class);
                    place.setPlaceKey(placeKey);
                    return place;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    Place placeInfo;
    public Place getPlaceInfo() {
        if (placeInfo != null)
            return placeInfo;

        final String categoryEmergencyKey = TipApplication.getTipApplication().categoryInfoService.getCategoryKeyByType(AppConstants.CATEGORY_CITY_INFO_TYPE);
        for (int i = 0; i < getCount(); i++) {
            try {
                if (getItem(i).getValue(Place.class) != null) {
                    if (categoryEmergencyKey != null && categoryEmergencyKey.length() > 0 && getItem(i).getValue(Place.class).getCategories().toString().contains(categoryEmergencyKey)) {
                        LoggerFactory.e(">>>>>>>>>>>>>>>>>>>>>>> parse Place info <<<<<<<<<<<<<<<<<<<<<<<");
                        placeInfo = getItem(i).getValue(Place.class);
                        placeInfo.setPlaceKey(getItem(i).getKey());

                        return placeInfo;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    List<Place> placeTransportInfo;

    public List<Place> getPlaceTransportInfo() {

        if (placeTransportInfo != null)
            return placeTransportInfo;

        placeTransportInfo = new LinkedList<>();

        final String categoryEmergencyKey = TipApplication.getTipApplication().categoryInfoService.getCategoryKeyByType(AppConstants.CATEGORY_TRANSPORT_TYPE);
        for (int i = 0; i < getCount(); i++) {
            try {
                if (getItem(i).getValue(Place.class) != null) {
                    if (categoryEmergencyKey != null && categoryEmergencyKey.length() > 0 && getItem(i).getValue(Place.class).getCategories() != null && getItem(i).getValue(Place.class).getCategories().toString().contains(categoryEmergencyKey)) {
                        LoggerFactory.e(">>>>>>>>>>>>>>>>>>>>>>> parse Place transport info <<<<<<<<<<<<<<<<<<<<<<<");
                        Place emergencyData = getItem(i).getValue(Place.class);
                        emergencyData.setPlaceKey(getItem(i).getKey());

                        placeTransportInfo.add(emergencyData);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return placeTransportInfo;
    }

    public List<Place> getAllPlaces() {
        List<Place> places = new LinkedList<>();
        for (int i = 0; i < getCount(); i++) {
            try {
                if (getItem(i).getValue(Place.class) != null) {
                    Place emergencyData = getItem(i).getValue(Place.class);
                    emergencyData.setPlaceKey(getItem(i).getKey());
                    places.add(emergencyData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return places;
    }

    public List<Place> getAllPlacesByCategoryKey(String categoryKey) {
        List<Place> places = new LinkedList<>();
        if (categoryKey == null || categoryKey.length() == 0)
            return places;

        for (int i = 0; i < getCount(); i++) {
            try {
                Place aPlace = getItem(i).getValue(Place.class);

                if (aPlace != null && aPlace.getCategories() != null && aPlace.getCategories().toString().contains(categoryKey)) {
                    aPlace.setPlaceKey(getItem(i).getKey());
                    places.add(aPlace);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return places;
    }

    public List<Place> searchPlace(String keyword) {
        List<Place> placeList = new LinkedList<>();
        for (int i = 0; i < getCount(); i++) {
            Place place = getItem(i).getValue(Place.class);

            if (place.getName() != null && place.getName().length() > 0 && place.getName().contains(keyword)) {
                place.setPlaceKey(getItem(i).getKey());
                placeList.add(place);
            }
        }

//        if (placeList.size() > 0) {
//            Collections.sort(placeList, new CityService.FcityComparator());
//        }

        return placeList;
    }

    public List<Place> searchPlace(String keyword, int filterCategory) {
        List<Place> placeList = new LinkedList<>();
        for (int i = 0; i < getCount(); i++) {
            Place place = getItem(i).getValue(Place.class);
            if (filterCategory < 0) {
                if (place.getName() != null && place.getName().length() > 0 && place.getName().toLowerCase().contains(keyword)) {
                    place.setPlaceKey(getItem(i).getKey());
                    placeList.add(place);
                }
            } else {
                if (place.getName() != null && place.getName().length() > 0 && place.getName().toLowerCase().contains(keyword)) {
                    if (place.getCategories().size() > 0) {
                        int categoryType = TipApplication.getTipApplication().categoryInfoService.getCategoryTypeByKey(place.getCategories().get(0));
                        if (categoryType == filterCategory) {
                            place.setPlaceKey(getItem(i).getKey());
                            placeList.add(place);
                        }
                    }
                }
            }


        }

//        if (placeList.size() > 0) {
//            Collections.sort(placeList, new CityService.FcityComparator());
//        }

        return placeList;
    }

    protected void notifyChangedListeners(ChangeEventListener.EventType type, int index) {
        notifyChangedListeners(type, index, -1);
    }

    protected void notifyChangedListeners(ChangeEventListener.EventType type, int index, int oldIndex) {
        if (mListener != null) {
            mListener.onChildChanged(type, index, oldIndex);
        }
    }

    protected void notifyCancelledListeners(DatabaseError error) {
        if (mListener != null) {
            mListener.onCancelled(error);
        }
    }

    public void updatePlace(Place place, DatabaseReference.CompletionListener completionListener) {
        place.setUpdatedAt(System.currentTimeMillis() / 1000);

        LoggerFactory.d("Update Place:" + place.toString());

        Map<String, Object> postValues = place.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        // Trong truong hop add thi trong postValue != null
        // Trong truong hop delete thi set postValue = null
        childUpdates.put(place.getPlaceKey(), postValues);

        mPreference.updateChildren(childUpdates, completionListener);
    }

    public void addPlaceTip(final String tips, final Context mContext, FCity fCity) {
        Place placeTip = new Place();
        placeTip.setName(tips);
        placeTip.setCommentCount(0);
        placeTip.setLoved("");
        placeTip.setDescription(tips);
        placeTip.setPlaceKey(mPreference.push().getKey());
        placeTip.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        placeTip.setCreatedAt(System.currentTimeMillis() / 1000);
        placeTip.setUpdatedAt(System.currentTimeMillis() / 1000);

        ArrayList<String> category = new ArrayList<String>();
        category.add(TipApplication.getTipApplication().categoryInfoService.getCategoryKeyByType(AppConstants.CATEGORY_TIP_TYPE));
        placeTip.setCategories(category);

        ArrayList<String> subCategory = new ArrayList<String>();
        subCategory.add(AppConstants.TipUser);
        placeTip.setSubcategories(subCategory);

        if (fCity != null) {
            placeTip.setAddress(fCity.getName());
        }


        Map<String, Object> postValues = placeTip.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        // Trong truong hop add thi trong postValue != null
        // Trong truong hop delete thi set postValue = null
        childUpdates.put(placeTip.getPlaceKey(), postValues);

        mPreference.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Toast.makeText(mContext, "Success", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "Unsuccess", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
