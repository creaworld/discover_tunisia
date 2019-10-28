/*
 * Copyright (c) 2017.
 *
 * Created by Pham Ngoc Thanh
 * Contact via Email: ngocthanh2207@gmail.com
 */

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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import tip.tdc.com.tip.TipApplication;
import tip.tdc.com.tip.model.CategoryInfo;
import tip.tdc.com.tip.model.CityCategory;
import tip.tdc.com.tip.model.FCityCategory;
import tip.tdc.com.tip.utils.LoggerFactory;

/**
 * Created by phamngocthanh on 8/11/17.
 */

public class CategoryService implements ChildEventListener, ValueEventListener {
    private DatabaseReference mPreference;

    /**
     * load cate of one city
     *
     * @param cityKey
     */
    public CategoryService(String cityKey) {
        mPreference = FirebaseDatabase.getInstance().getReference("category" + "/" + cityKey);
        mPreference.keepSynced(true);
        mPreference.addChildEventListener(this);
        mPreference.addValueEventListener(this);
    }

    /**
     * load cate of all city
     */
    public CategoryService() {
        mPreference = FirebaseDatabase.getInstance().getReference("category");
        mPreference.keepSynced(true);
        mPreference.addChildEventListener(this);
        mPreference.addValueEventListener(this);
    }

    public void configureDatabase() {

    }

    private Query mQuery;
    private ChangeEventListener mListener;
    private List<DataSnapshot> mSnapshots = new ArrayList<>();

    public CategoryService(Query ref) {
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

    /**
     * list all category key of a
     *
     * @param cityKey
     * @return
     */
    private DataSnapshot getCategoryOfCity(String cityKey) {
        if (getCount() > 0) {
            for (int i = 0; i < getCount(); i++) {
                if (getItem(i).getKey().equalsIgnoreCase(cityKey)) {
                    return getItem(i);
                }
            }
        }

        return null;
    }

    public List<FCityCategory> getListCategoryOfCity(String cityKey) {
        List<FCityCategory> cityCategories = new LinkedList<>();
        DataSnapshot dataSnapshot = getCategoryOfCity(cityKey);




        if (dataSnapshot != null && dataSnapshot.getChildrenCount() > 0) {
            LoggerFactory.d("got a getCategoryOfCity:" + dataSnapshot.getKey());
            LoggerFactory.d("got a getChildrenCount:" + dataSnapshot.getChildrenCount());

//            List<DataSnapshot> dataSnapshots = new LinkedList<>();
//            Iterator<DataSnapshot> dataSnapshotIterator = dataSnapshot.getChildren().iterator();
//            if (dataSnapshotIterator.hasNext()) {
//                DataSnapshot dataSnapshot1 = dataSnapshotIterator.next();
//                FCityCategory fCityCategory = dataSnapshot1.getValue(FCityCategory.class);
//                LoggerFactory.d("got FCityCategory:" + fCityCategory.toString());
//                cityCategories.add(fCityCategory);
//            }

            for(DataSnapshot child : dataSnapshot.getChildren()) {
                FCityCategory fCityCategory = child.getValue(FCityCategory.class);
                LoggerFactory.d("got FCityCategory:" + fCityCategory.toString());
                cityCategories.add(fCityCategory);
            }

        }

        Collections.sort(cityCategories, new CustomComparator());

        return cityCategories;
    }

    public List<CategoryInfo> getListCategoryInfoOfCity(String cityKey) {
        List<FCityCategory> cityCategories = getListCategoryOfCity(cityKey);
        List<CategoryInfo> categoryInfos = new LinkedList<>();

        for (int i = 0; i < cityCategories.size(); i++) {
            LoggerFactory.d("FCityCategory:key:" + cityCategories.get(i).getCategory_key());
            CategoryInfo categoryInfo = TipApplication.getTipApplication().categoryInfoService.getCategoryByKey(cityCategories.get(i).getCategory_key());
            if (categoryInfo != null) {
                if (cityCategories.get(i).getOrder() != null && cityCategories.get(i).getOrder().length() > 0) {
                    categoryInfo.setPriority(cityCategories.get(i).getOrder() + "");
                } else {
                    categoryInfo.setPriority("0");
                }

                categoryInfos.add(categoryInfo);
            }
        }

        return categoryInfos;
    }

    public class CustomComparator implements Comparator<FCityCategory> {
        @Override
        public int compare(FCityCategory o1, FCityCategory o2) {
            Integer p1 =0, p2 = 0;
            if(o1.getOrder() != null && o1.getOrder().length() > 0){
                p1 = new Integer(Integer.parseInt(o1.getOrder()));
            }
            if(o2.getOrder() != null && o2.getOrder().length() > 0){
                p2 = new Integer(Integer.parseInt(o2.getOrder()));
            }
            return p2.compareTo(p1);
        }
    }
}
