package spb.android.academy.fragments.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spb.android.academy.fragments.R;
import spb.android.academy.fragments.domain.AuthResponse;
import spb.android.academy.fragments.network.NetworkManager;

/**
 * @author Artur Vasilov
 */
public class AuthActivity extends AppCompatActivity {

    public static final int AUTH_REQUEST_CODE = 11;

    public static final String TOKEN_KEY = "token";

    private static final String AUTH_URL = "https://unsplash.com/oauth/authorize/?" +
            "client_id=" + NetworkManager.CLIENT_ID +
            "&redirect_uri=" + NetworkManager.REDIRECT_URI +
            "&response_type=code" +
            "&scope=public+write_likes";

    private static final String GRANT_TYPE = "authorization_code";

    private final AuthCallback authCallback = new AuthCallback() {
        @Override
        public void onAuthCodeReceived(@NonNull String authCode) {
            authorizeUser(authCode);
        }
    };

    public static void startForResult(@NonNull Activity activity) {
        Intent intent = new Intent(activity, AuthActivity.class);
        activity.startActivityForResult(intent, AUTH_REQUEST_CODE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        WebView webView = findViewById(R.id.authWebView);
        webView.setWebViewClient(new AuthWebViewClient(authCallback));
        webView.loadUrl(AUTH_URL);
    }

    private void authorizeUser(@NonNull final String authCode) {
        NetworkManager.getAuthApiInstance().authorizeUser(
                NetworkManager.CLIENT_ID,
                NetworkManager.CLIENT_SECRET,
                NetworkManager.REDIRECT_URI,
                authCode,
                GRANT_TYPE
        ).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                final AuthResponse authResponse = response.body();
                if (authResponse != null) {
                    Intent intent = new Intent();
                    intent.putExtra(TOKEN_KEY, authResponse.getToken());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                // TODO : show error
            }
        });
    }
}

