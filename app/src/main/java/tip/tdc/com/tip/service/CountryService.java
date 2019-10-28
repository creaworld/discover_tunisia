package tip.tdc.com.tip.service;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by phamngocthanh on 7/24/17.
 */

public class CountryService implements ChildEventListener, ValueEventListener {
    private DatabaseReference mLanguagePreference;

    public CountryService(String languageKey){
            mLanguagePreference = FirebaseDatabase.getInstance().getReference("country"+"/"+languageKey);
            mLanguagePreference.keepSynced(true);
            mLanguagePreference.addChildEventListener(this);
            mLanguagePreference.addValueEventListener(this);
    }

    public void configureDatabase(){

    }

    private Query mQuery;
    private ChangeEventListener mListener;
    private List<DataSnapshot> mSnapshots = new ArrayList<>();

    public CountryService(Query ref) {
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
}
