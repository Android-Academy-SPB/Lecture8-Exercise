package spb.android.academy.fragments.storage.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * @author Artur Vasilov
 */
@Entity
public class LikeRequest {

    @NonNull
    @PrimaryKey
    private String photoId;

    private boolean isLike;

    @NonNull
    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(@NonNull String photoId) {
        this.photoId = photoId;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }
}
