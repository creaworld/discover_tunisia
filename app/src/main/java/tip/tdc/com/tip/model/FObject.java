package tip.tdc.com.tip.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by phamngocthanh on 7/29/17.
 */

public class FObject {
    long createdAt;
    long updatedAt;
    String path;
    String objectId;

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

    Map<String, Object> dictionary = new HashMap<String, Object>();

    DatabaseReference getDatabaseReference(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(path);
        if(objectId == null) {
            reference = reference.push();
            objectId = reference.getKey();
        }

        return reference.child(objectId);
    }
    public void saveInBackground(){
        DatabaseReference reference = getDatabaseReference();


        long interval = Calendar.getInstance().getTime().getTime();
        if(createdAt == 0) {
            createdAt = interval;
        }

        updatedAt = interval;


        dictionary.put(objectId, this);
        reference.updateChildren(dictionary);
    }

    @Override
    public String toString() {
        return "FObject{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", path='" + path + '\'' +
                ", objectId='" + objectId + '\'' +
                ", dictionary=" + dictionary +
                '}';
    }
}

