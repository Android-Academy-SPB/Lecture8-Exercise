package spb.android.academy.fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class CollectionsPagerAdapter extends FragmentStatePagerAdapter {

    @NonNull
    private final List<CollectionFragment> fragments;

    public CollectionsPagerAdapter(@NonNull FragmentManager fm, @NonNull List<CollectionFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
