package spb.android.academy.fragments.network;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import spb.android.academy.fragments.auth.AuthApi;
import spb.android.academy.fragments.auth.AuthInterceptor;

public class NetworkManager {
    private static final String API_HOST = "https://api.unsplash.com/";
    private static final String AUTH_API_HOST = "https://unsplash.com/";

    public static final String CLIENT_ID = "123fc0633091c3869ec9ab5fca82ff7d847c269200df57ab475664e11aeba85e"; // Put your own clientId here.
    public static final String CLIENT_SECRET = "7b75ec3578dd6dace4ac4d3a7f86bab03bc2ac39b251c72a814763b7775936d0"; // Put your own clientSecret here.
    public static final String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob"; // Put your own redirect_uri here.

    private static final NetworkManager INSTANCE = new NetworkManager();

    @NonNull
    public static Api getApiInstance() {
        return INSTANCE.api;
    }

    @NonNull
    public static AuthApi getAuthApiInstance() {
        return INSTANCE.authApi;
    }

    private final Api api;
    private final AuthApi authApi;

    private NetworkManager() {
        final Interceptor interceptor = provideRequestInterceptor();
        final OkHttpClient okHttpClient = provideOkHttp(interceptor);

        final Retrofit retrofit = provideRetrofit(okHttpClient, API_HOST);
        api = retrofit.create(Api.class);

        final Retrofit authRetrofit = provideRetrofit(okHttpClient, AUTH_API_HOST);
        authApi = authRetrofit.create(AuthApi.class);
    }

    @NonNull
    private Retrofit provideRetrofit(@NonNull OkHttpClient okHttp, @NonNull String baseUrl) {
        return new Retrofit.Builder() //
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build();
    }

    @NonNull
    private OkHttpClient provideOkHttp(@NonNull Interceptor interceptor) {
        final HttpLoggingInterceptor.Logger logger = new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("OkHttp", message);
            }
        };

        final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(logger) //
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder() //
                .addInterceptor(interceptor)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new AuthInterceptor())
                .build();
    }

    @NonNull
    private Interceptor provideRequestInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final Request originalRequest = chain.request();
                final HttpUrl newUrl = originalRequest.url().newBuilder() //
                        .addQueryParameter("client_id", CLIENT_ID)
                        .build();

                final Request newRequest = originalRequest.newBuilder() //
                        .url(newUrl)
                        .build();

                return chain.proceed(newRequest);
            }
        };
    }
}
