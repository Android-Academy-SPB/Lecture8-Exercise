package spb.android.academy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import spb.android.academy.fragments.domain.Collection;
import spb.android.academy.fragments.domain.Photo;

public class CollectionFragment extends Fragment {

    public static final String ARG_COLLECTION_ID = "arg:collection_id";

    private CollectionFragmentListener listener;

    private TextView nameTextView;
    private TextView descriptionTextView;
    private ImageView previewImage;

    private Collection collection;

    @NonNull
    public static CollectionFragment newInstance(int collectionId) {
        final CollectionFragment fragment = new CollectionFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(ARG_COLLECTION_ID, collectionId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final int collectionId = getArguments().getInt(ARG_COLLECTION_ID);
        collection = CollectionsRepository.getInstance().getById(collectionId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_collection, container, false);

        nameTextView = view.findViewById(R.id.fragment_collection_name);
        descriptionTextView = view.findViewById(R.id.fragment_collection_description);
        previewImage = view.findViewById(R.id.fragment_collection_preview_image);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nameTextView.setText(collection.getTitle());
        descriptionTextView.setText(collection.getDescription());

        List<Photo> photos = collection.getPreviewPhotos();
        if (photos.isEmpty()) {
            return;
        }
        final String imageUrl = collection.getPreviewPhotos().get(0).getUrlForSmall();
        Picasso.get().load(imageUrl).into(previewImage);

        previewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onPreviewCollection(collection.getId());
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CollectionFragmentListener) {
            listener = (CollectionFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface CollectionFragmentListener {
        void onPreviewCollection(int collectionId);
    }
}
