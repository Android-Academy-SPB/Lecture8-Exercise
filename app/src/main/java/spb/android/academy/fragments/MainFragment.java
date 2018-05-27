package spb.android.academy.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spb.android.academy.fragments.domain.Collection;
import spb.android.academy.fragments.network.NetworkManager;

public class MainFragment extends Fragment {

    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewPager = view.findViewById(R.id.fragment_main_view_pager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NetworkManager.getApiInstance().getFeaturedCollections().enqueue(new Callback<List<Collection>>() {
            @Override
            public void onResponse(@NonNull Call<List<Collection>> call, @NonNull Response<List<Collection>> response) {
                List<Collection> collections = response.body();
                if (collections != null) {
                    for (Collection collection : collections) {
                        CollectionsRepository.getInstance().save(collection);
                    }
                    setupViewPagerAdapter();
                } else {
                    showErrorMessage();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Collection>> call, @NonNull Throwable t) {
                showErrorMessage();
            }
        });
    }

    private void showErrorMessage() {
        Toast.makeText(getContext(), "Something went wrong :(", Toast.LENGTH_SHORT).show();
    }

    private void setupViewPagerAdapter() {
        final List<Collection> collections = CollectionsRepository.getInstance().getAll();
        final List<CollectionFragment> fragments = new ArrayList<>();
        for (Collection collection : collections) {
            fragments.add(CollectionFragment.newInstance(collection.getId()));
        }
        final CollectionsPagerAdapter adapter = new CollectionsPagerAdapter(getChildFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }
}
