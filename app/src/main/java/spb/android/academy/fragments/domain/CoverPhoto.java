package spb.android.academy.fragments.domain;

import com.google.gson.annotations.SerializedName;

/**
 * @author Artur Vasilov
 */
public class CoverPhoto {

    @SerializedName("id")
    private String id;

    @SerializedName("liked_by_user")
    private boolean isLiked;

    public String getId() {
        return id;
    }

    public boolean isLiked() {
        return isLiked;
    }
}
