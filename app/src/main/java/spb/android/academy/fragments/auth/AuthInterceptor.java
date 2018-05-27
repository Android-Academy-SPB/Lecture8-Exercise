package spb.android.academy.fragments.auth;

import android.text.TextUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import spb.android.academy.fragments.storage.PreferencesProvider;

/**
 * @author Artur Vasilov
 */
public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        final String token = PreferencesProvider.getPreferences().getSavedToken();
        if (!TextUtils.isEmpty(token)) {
            Request authenticatedRequest = request.newBuilder()
                    .header("Authorization", "Bearer " + token).build();
            return chain.proceed(authenticatedRequest);
        }
        return chain.proceed(request);
    }
}
