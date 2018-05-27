package spb.android.academy.fragments.storage.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * @author Artur Vasilov
 */
@Database(entities = {LikeRequest.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LikeDao likeDao();
}
