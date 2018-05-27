package spb.android.academy.fragments;

import android.app.Application;

import androidx.work.Configuration;
import androidx.work.WorkManager;
import spb.android.academy.fragments.common.Executors;
import spb.android.academy.fragments.storage.StorageProvider;

/**
 * @author Artur Vasilov
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        StorageProvider.initialize(this);

        Configuration configuration = new Configuration.Builder()
                .setExecutor(Executors.commonExecutor())
                .build();
        WorkManager.initialize(this, configuration);
    }
}
