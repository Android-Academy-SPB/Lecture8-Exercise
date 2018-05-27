package spb.android.academy.fragments.storage;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import spb.android.academy.fragments.storage.database.AppDatabase;

/**
 * @author Artur Vasilov
 */
public class StorageProvider {

    private StorageProvider() {
    }

    @Nullable
    private static Preferences sPreferences;

    @Nullable
    private static AppDatabase sAppDatabase;

    @MainThread
    public static void initialize(@NonNull Context context) {
        if (sPreferences != null || sAppDatabase != null) {
            throw new IllegalStateException("You shouldn't call initialize twice");
        }

        sPreferences = new Preferences(context);
        sAppDatabase = Room.databaseBuilder(context, AppDatabase.class, "database-name").build();
    }

    @NonNull
    public static Preferences getPreferences() {
        if (sPreferences == null) {
            throw new IllegalStateException("You should call initialize before accessing Preferences");
        }
        return sPreferences;
    }

    @NonNull
    public static AppDatabase getAppDatabase() {
        if (sAppDatabase == null) {
            throw new IllegalStateException("You should call initialize before accessing AppDatabase");
        }
        return sAppDatabase;
    }
}
