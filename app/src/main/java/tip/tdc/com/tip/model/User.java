package tip.tdc.com.tip.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phamngocthanh on 7/19/17.
 */

@IgnoreExtraProperties
public class User extends FObject{
    long createdAt;
    long updatedAt;
//    public String username;
    public String email;
    public String name;
    public String avatar;
    public boolean inactive;
    public boolean isAdmin;
    public String loginProvider;
    public boolean uncommentable;
    public String userId;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("createdAt", createdAt);
        result.put("updatedAt", updatedAt);
        result.put("name", name);
        result.put("avatar", avatar);
        result.put("inactive", inactive);
        result.put("isAdmin", isAdmin);
        result.put("loginProvider", loginProvider);
        result.put("uncommentable", uncommentable);
        result.put("userId", userId);
        return result;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isInactive() {
        return inactive;
    }

    public void setInactive(boolean inactive) {
        this.inactive = inactive;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getLoginProvider() {
        return loginProvider;
    }

    public void setLoginProvider(String loginProvider) {
        this.loginProvider = loginProvider;
    }

    public boolean isUncommentable() {
        return uncommentable;
    }

    public void setUncommentable(boolean uncommentable) {
        this.uncommentable = uncommentable;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public long getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public long getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "User{" +
                "createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                ", inactive=" + inactive +
                ", isAdmin=" + isAdmin +
                ", loginProvider='" + loginProvider + '\'' +
                ", uncommentable=" + uncommentable +
                ", userId='" + userId + '\'' +
                '}';
    }
}
