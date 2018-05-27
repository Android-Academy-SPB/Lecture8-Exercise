package spb.android.academy.fragments.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class Collection {

    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("preview_photos")
    private List<Photo> previewPhotos;
    @SerializedName("links")
    private Links links;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @NonNull
    public List<Photo> getPreviewPhotos() {
        if (previewPhotos == null) {
            return Collections.emptyList();
        }
        return previewPhotos;
    }

    @Nullable
    public String getBrowserLink() {
        if (links == null) {
            return null;
        }
        return links.html;
    }

    static class Links {
        @SerializedName("html")
        String html;
    }
}
