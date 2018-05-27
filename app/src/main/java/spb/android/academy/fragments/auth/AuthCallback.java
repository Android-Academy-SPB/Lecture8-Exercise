package spb.android.academy.fragments.auth;

import android.support.annotation.NonNull;

/**
 * @author Artur Vasilov
 */
interface AuthCallback {

    void onAuthCodeReceived(@NonNull String authCode);
}

