package spb.android.academy.fragments.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * @author Artur Vasilov
 */
public class Preferences {

    private static final String PREFERENCES_NAME = "unsplash_preferences";

    private static final String ACCESS_TOKEN_KEY = "ACCESS_TOKEN_KEY";

    @NonNull
    private final SharedPreferences preferences;

    public Preferences(@NonNull Context context) {
        this.preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(@NonNull String token) {
        preferences.edit().putString(ACCESS_TOKEN_KEY, token).apply();
    }

    @NonNull
    public String getSavedToken() {
        return preferences.getString(ACCESS_TOKEN_KEY, "");
    }
}
