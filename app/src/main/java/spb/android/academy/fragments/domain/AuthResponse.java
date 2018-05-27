package spb.android.academy.fragments.domain;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * @author Artur Vasilov
 */
public class AuthResponse {

    @SerializedName("access_token")
    private String token;

    @Nullable
    public String getToken() {
        return token;
    }
}
