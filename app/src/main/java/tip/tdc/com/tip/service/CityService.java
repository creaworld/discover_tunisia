package tip.tdc.com.tip.service;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import tip.tdc.com.tip.model.FCity;
import tip.tdc.com.tip.utils.LoggerFactory;

/**
 * Created by phamngocthanh on 7/24/17.
 */

public class CityService implements ChildEventListener, ValueEventListener {
    private DatabaseReference mPreference;

    public CityService(String countryKey){
            mPreference = FirebaseDatabase.getInstance().getReference("city"+"/"+countryKey);
            mPreference.keepSynced(true);
            mPreference.addChildEventListener(this);
            mPreference.addValueEventListener(this);
    }

    public void configureDatabase(){

    }

    private Query mQuery;
    private ChangeEventListener mListener;
    private List<DataSnapshot> mSnapshots = new ArrayList<>();

    public CityService(Query ref) {
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


    public List<FCity> getListCities(){
        List<FCity> fCities = new LinkedList<>();
        for(int i = 0; i < getCount(); i++){
            LoggerFactory.d("cityService key:" + getItem(i).getKey());
            LoggerFactory.d("cityService name:" + getItem(i).getValue(FCity.class).toString());
            FCity fCity = getItem(i).getValue(FCity.class);
            if(!fCity.isDeactived()) {
                fCity.setCityKey(getItem(i).getKey());
                fCities.add(fCity);
            }
        }

        Collections.sort(fCities, new FcityComparator());

        return fCities;
    }

    public FCity getCityByKey(String  objectKey){
        for(int i = 0; i < getCount(); i++){
            if(getItem(i).getKey().equalsIgnoreCase(objectKey)) {
                FCity fCity = getItem(i).getValue(FCity.class);
                fCity.setCityKey(getItem(i).getKey());
                return fCity;
            }
        }

        return null;
    }

    public class FcityComparator implements Comparator<FCity> {
        @Override
        public int compare(FCity o1, FCity o2) {
            Integer p1 =0, p2 = 0;
            if(o1.getPriority() != null && o1.getPriority().length() > 0){
                p1 = new Integer(Integer.parseInt(o1.getPriority()));
            }
            if(o2.getPriority() != null && o2.getPriority().length() > 0){
                p2 = new Integer(Integer.parseInt(o2.getPriority()));
            }
            return p2.compareTo(p1);
        }
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
        mListener.onDataChanged();
    }

    @Override
    public void onCancelled(DatabaseError error) {
        notifyCancelledListeners(error);
    }

    public void setOnChangedListener(ChangeEventListener listener) {
        mListener = listener;
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

    public List<FCity> searchCity(String keyword){
        List<FCity> fCities = new LinkedList<>();
        for(int i = 0; i < getCount(); i++){
            FCity fCity = getItem(i).getValue(FCity.class);

            if(fCity.getName() != null && fCity.getName().length() > 0 && fCity.getName().toLowerCase().contains(keyword)) {
                fCity.setCityKey(getItem(i).getKey());
                fCities.add(fCity);
            }
        }

        if (fCities.size() > 0) {
            Collections.sort(fCities, new FcityComparator());
        }

        return fCities;
    }
}
