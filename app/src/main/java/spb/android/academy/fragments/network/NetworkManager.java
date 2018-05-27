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

public class NetworkManager {
    private static final String API_HOST = "https://api.unsplash.com/";

    private static final String CLIENT_ID = "47fcafd8325da4e303bcea81da22e67ce35b5c41552b68517999fb085db9dcd1"; // Put your own clientId here.

    private static final NetworkManager INSTANCE = new NetworkManager();

    @NonNull
    public static Api getApiInstance() {
        return INSTANCE.api;
    }

    private final Api api;

    private NetworkManager() {
        final Interceptor interceptor = provideRequestInterceptor();
        final OkHttpClient okHttpClient = provideOkHttp(interceptor);
        final Retrofit retrofit = provideRetrofit(okHttpClient);

        api = provideApi(retrofit);
    }

    @NonNull
    private Api provideApi(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }

    @NonNull
    private Retrofit provideRetrofit(OkHttpClient okHttp) {
        return new Retrofit.Builder() //
                .baseUrl(API_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp)
                .build();
    }

    @NonNull
    private OkHttpClient provideOkHttp(Interceptor interceptor) {
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
