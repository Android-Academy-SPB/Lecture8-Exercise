package spb.android.academy.fragments.auth;

import android.support.annotation.NonNull;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author Artur Vasilov
 */
class AuthWebViewClient extends WebViewClient {

    private static final String REDIRECT_URL = "https://unsplash.com/oauth/authorize/";

    @NonNull
    private final AuthCallback authCallback;

    AuthWebViewClient(@NonNull AuthCallback authCallback) {
        this.authCallback = authCallback;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(@NonNull WebView webView, @NonNull String url) {
        if (url.startsWith(REDIRECT_URL)) {
            String authCode = url.substring(REDIRECT_URL.length());
            authCallback.onAuthCodeReceived(authCode);
            return true;
        }
        return super.shouldOverrideUrlLoading(webView, url);
    }
}
