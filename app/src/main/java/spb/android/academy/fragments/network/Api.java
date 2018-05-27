package spb.android.academy.fragments.network;

import android.support.annotation.NonNull;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import spb.android.academy.fragments.domain.Collection;

public interface Api {

    @GET("/collections/curated")
    Call<List<Collection>> getFeaturedCollections();

    @POST("/photos/{id}/like")
    Call<ResponseBody> likePhoto(@NonNull @Path("id") String photoId);

    @DELETE("/photos/{id}/like")
    Call<ResponseBody> unlikePhoto(@NonNull @Path("id") String photoId);
}
