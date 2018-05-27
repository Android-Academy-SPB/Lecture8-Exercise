package spb.android.academy.fragments.domain;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("id")
    private int id;
    @SerializedName("urls")
    private PhotoUrls urls;

    public int getId() {
        return id;
    }

    @Nullable
    public String getUrlForSmall() {
        if (urls == null) {
            return null;
        }
        return urls.smallUrl;
    }

    static class PhotoUrls {
        @SerializedName("small")
        private String smallUrl;
    }
}
