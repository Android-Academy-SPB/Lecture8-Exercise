package spb.android.academy.fragments.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import spb.android.academy.fragments.domain.Collection;

public interface Api {

    @GET("/collections/curated")
    Call<List<Collection>> getFeaturedCollections();
}
