package spb.android.academy.fragments.auth;

import android.support.annotation.NonNull;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import spb.android.academy.fragments.domain.AuthResponse;

/**
 * @author Artur Vasilov
 */
public interface AuthApi {

    @POST("/oauth/token")
    @FormUrlEncoded
    Call<AuthResponse> authorizeUser(@NonNull @Field("client_id") String clientId,
                                     @NonNull @Field("client_secret") String clientSecret,
                                     @NonNull @Field("redirect_uri") String redirect,
                                     @NonNull @Field("code") String authCode,
                                     @NonNull @Field("grant_type") String grantType);
}
