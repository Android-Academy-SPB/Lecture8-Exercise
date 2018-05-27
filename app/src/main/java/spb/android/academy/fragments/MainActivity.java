package spb.android.academy.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import spb.android.academy.fragments.auth.AuthActivity;
import spb.android.academy.fragments.storage.PreferencesProvider;

public class MainActivity extends AppCompatActivity implements CollectionFragment.CollectionFragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String token = PreferencesProvider.getPreferences().getSavedToken();
        if (TextUtils.isEmpty(token)) {
            AuthActivity.startForResult(this);
            return;
        }

        if (savedInstanceState == null) {
            openFragment(MainFragment.newInstance(), false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AuthActivity.AUTH_REQUEST_CODE) {
            String token = data.getStringExtra(AuthActivity.TOKEN_KEY);
            PreferencesProvider.getPreferences().saveToken(token);
            openFragment(MainFragment.newInstance(), false);
        }
    }

    @Override
    public void onPreviewCollection(int collectionId) {
        openFragment(PreviewFragment.newInstance(collectionId), true);
    }

    private void openFragment(@NonNull Fragment fragment, boolean addToBackStack) {
        final FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        if (addToBackStack) {
            transaction
                    .addToBackStack(null)
                    .setCustomAnimations(
                            R.anim.slide_in_right, R.anim.slide_out_left,
                            R.anim.slide_in_left, R.anim.slide_out_right
                    );
        }

        transaction
                .replace(R.id.content, fragment)
                .commit();
    }
}
