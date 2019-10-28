package tip.tdc.com.tip.service;

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

import tip.tdc.com.tip.data.AppState;
import tip.tdc.com.tip.model.UserComment;
import tip.tdc.com.tip.utils.LoggerFactory;

/**
 * Created by phamngocthanh on 7/24/17.
 */

public class CommentService implements ChildEventListener, ValueEventListener {
    private DatabaseReference mPreference;

    public CommentService(String placeKey){
        mPreference = FirebaseDatabase.getInstance().getReference("comments"+"/"+placeKey);
//        mPreference = FirebaseDatabase.getInstance().getReference().child("comments").child(placeKey);
        mPreference.keepSynced(true);
        mPreference.addChildEventListener(this);
        mPreference.addValueEventListener(this);
    }

    public void configureDatabase(){

    }

    private Query mQuery;
    private ChangeEventListener mListener;
    private List<DataSnapshot> mSnapshots = new ArrayList<>();

    public CommentService(Query ref) {
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

    public List<UserComment> getListUserComments(){
        List<UserComment> userComments = new LinkedList<>();
        for(int i = 0; i < getCount(); i++){
            LoggerFactory.d("UserComment key:" + getItem(i).getKey());
            LoggerFactory.d("UserComment name:" + getItem(i).getValue(UserComment.class).getSenderId());
            UserComment userComment = getItem(i).getValue(UserComment.class);
            userComment.setCommentKey(getItem(i).getKey());
            userComments.add(userComment);
        }

//        Collections.sort(userComments, new CityService.FcityComparator());

        return userComments;
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

    public void addComment(final String comments, DatabaseReference.CompletionListener completionListener){
        UserComment userComment = new UserComment();
        userComment.setText(comments);
        userComment.setRemoved(false);
        userComment.setCommentKey(mPreference.push().getKey());
        userComment.setCreatedAt(System.currentTimeMillis()/1000);
        userComment.setUpdatedAt(System.currentTimeMillis()/1000);
        userComment.setSenderId(AppState.currentFireUser.getUid());

        LoggerFactory.d("addComment " + userComment.toString());


        Map<String, Object> postValues = userComment.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        // Trong truong hop add thi trong postValue != null
        // Trong truong hop delete thi set postValue = null
        childUpdates.put(userComment.getCommentKey(), postValues);

        mPreference.updateChildren(childUpdates, completionListener);
    }

}
