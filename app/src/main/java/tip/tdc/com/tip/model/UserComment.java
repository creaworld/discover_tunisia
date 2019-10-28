package tip.tdc.com.tip.model;

import com.google.firebase.database.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by phamngocthanh on 7/17/17.
 */

public class UserComment {
    String userName;
    Date cDate;
    String text;
    long createdAt;
    long updatedAt;
    String senderId;
    boolean isRemoved;

    @Exclude
    String commentKey;

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
//        result.put("userName", userName);
//        result.put("cDate", cDate);
        result.put("text", text);
        result.put("createdAt", createdAt);
        result.put("updatedAt", updatedAt);
        result.put("senderId", senderId);
        result.put("isRemoved", isRemoved);
//        result.put("avatar", avatar);
        return result;
    }

    public Date getcDate() {
        return cDate;
    }

    public void setcDate(Date cDate) {
        this.cDate = cDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void setRemoved(boolean removed) {
        isRemoved = removed;
    }

    public String getCommentKey() {
        return commentKey;
    }

    public void setCommentKey(String commentKey) {
        this.commentKey = commentKey;
    }

    @Override
    public String toString() {
        return "UserComment{" +
                "userName='" + userName + '\'' +
                ", cDate=" + cDate +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", senderId='" + senderId + '\'' +
                ", isRemoved=" + isRemoved +
                ", commentKey='" + commentKey + '\'' +
                '}';
    }
}
