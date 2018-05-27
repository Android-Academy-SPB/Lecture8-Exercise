package spb.android.academy.fragments;

import android.app.Application;

import spb.android.academy.fragments.storage.PreferencesProvider;

/**
 * @author Artur Vasilov
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesProvider.initialize(this);
    }
}
