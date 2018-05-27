package spb.android.academy.fragments.storage.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

/**
 * @author Artur Vasilov
 */
@Dao
public interface LikeDao {

    @Query("SELECT * FROM likerequest")
    List<LikeRequest> getAllRequests();

    @Insert
    void addRequest(@NonNull LikeRequest likeRequest);

    @Nullable
    @Query("SELECT * FROM likerequest WHERE photoId = :photoId LIMIT 1")
    LikeRequest findLikeRequest(@NonNull String photoId);

    @Delete
    void deleteRequest(@NonNull LikeRequest likeRequest);
}
