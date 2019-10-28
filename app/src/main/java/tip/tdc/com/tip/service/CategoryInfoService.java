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

import tip.tdc.com.tip.model.CategoryInfo;
import tip.tdc.com.tip.utils.LoggerFactory;

/**
 * Created by phamngocthanh on 7/24/17.
 */

public class CategoryInfoService implements ChildEventListener, ValueEventListener {
    private DatabaseReference mLanguagePreference;
    public Boolean isLoaded = false;

    public CategoryInfoService() {
        mLanguagePreference = FirebaseDatabase.getInstance().getReference("category_info");
        mLanguagePreference.keepSynced(true);
        mLanguagePreference.addChildEventListener(this);
        mLanguagePreference.addValueEventListener(this);
    }

    public void configureDatabase() {

    }

    private Query mQuery;
    private ChangeEventListener mListener;
    private List<DataSnapshot> mSnapshots = new ArrayList<>();

    public CategoryInfoService(Query ref) {
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

    public String getCategoryNameByType(int type) {
        if (getCount() > 0) {
            for (int i = 0; i < getCount(); i++) {
                if (getItem(i).getValue(CategoryInfo.class).getType() == type) {
                    return getItem(i).getValue(CategoryInfo.class).getName();
                }
            }
        }

        return "";
    }

    public CategoryInfo getCategoryByKey(String categoryKey) {
        LoggerFactory.d("getCategoryByKey:"+categoryKey);
        if (getCount() > 0) {
            for (int i = 0; i < getCount(); i++) {
                if (getItem(i).getKey().equalsIgnoreCase(categoryKey)) {
                    CategoryInfo categoryInfo = getItem(i).getValue(CategoryInfo.class);
                    categoryInfo.setKey(categoryKey);
                    LoggerFactory.d("getCategoryByKey:value:"+ categoryInfo.toString());
                    return categoryInfo;
                }
            }
        }

        return null;
    }

    public String getCategoryKeyByType(int type) {
        if (getCount() > 0) {
            for (int i = 0; i < getCount(); i++) {
                if (getItem(i).getValue(CategoryInfo.class).getType() == type) {
                    return getItem(i).getKey();
                }
            }
        }

        return "";
    }

    public int getCategoryTypeByKey(String key) {
        if (getCount() > 0) {
            for (int i = 0; i < getCount(); i++) {
                if (getItem(i).getKey().equalsIgnoreCase(key)) {
                    return getItem(i).getValue(CategoryInfo.class).getType();
                }
            }
        }

        return -1;
    }

    public String getCategoryNameByKey(String key) {
        if (getCount() > 0) {
            for (int i = 0; i < getCount(); i++) {
                if (getItem(i).getKey().equalsIgnoreCase(key)) {
                    return getItem(i).getValue(CategoryInfo.class).getName();
                }
            }
        }

        return "";
    }

    List<CategoryInfo> categoryInfoListSorted = new LinkedList<>();

    public List<CategoryInfo> getCategorySorted() {
        if (categoryInfoListSorted.size() == 0) {
            if (getCount() > 0) {
                for (int i = 0; i < getCount(); i++) {
                    categoryInfoListSorted.add(getItem(i).getValue(CategoryInfo.class));
                }
                Collections.sort(categoryInfoListSorted, new CustomComparator());

            }
        }

        return categoryInfoListSorted;

    }

    public class CustomComparator implements Comparator<CategoryInfo> {
        @Override
        public int compare(CategoryInfo o1, CategoryInfo o2) {
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
}
