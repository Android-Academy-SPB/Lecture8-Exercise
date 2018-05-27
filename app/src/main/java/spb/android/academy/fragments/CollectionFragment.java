package spb.android.academy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spb.android.academy.fragments.common.Executors;
import spb.android.academy.fragments.common.UiThreadHandler;
import spb.android.academy.fragments.domain.Collection;
import spb.android.academy.fragments.domain.Photo;
import spb.android.academy.fragments.network.NetworkManager;
import spb.android.academy.fragments.scheduler.LikeSyncWorker;
import spb.android.academy.fragments.storage.StorageProvider;
import spb.android.academy.fragments.storage.database.LikeDao;
import spb.android.academy.fragments.storage.database.LikeRequest;

public class CollectionFragment extends Fragment {

    public static final String ARG_COLLECTION_ID = "arg:collection_id";

    private CollectionFragmentListener listener;

    private TextView nameTextView;
    private TextView descriptionTextView;
    private ImageView previewImage;
    private LikeButton likeButton;

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
        likeButton = view.findViewById(R.id.like_button);

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

        likeButton.setLiked(collection.getCoverPhoto().isLiked());
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                performLikeOrUnlike(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                performLikeOrUnlike(false);
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

    private void performLikeOrUnlike(final boolean isLike) {
        // TODO : it's very complicated for us to stand against clicking ddos, so let it be so
        likeButton.setEnabled(false);

        final String photoId = collection.getCoverPhoto().getId();
        Executors.commonExecutor().execute(new Runnable() {
            @Override
            public void run() {
                LikeDao likeDao = StorageProvider.getAppDatabase().likeDao();
                LikeRequest cachedLikeRequest = likeDao.findLikeRequest(photoId);

                if (cachedLikeRequest == null) {
                    final LikeRequest likeRequest = new LikeRequest();
                    likeRequest.setPhotoId(photoId);
                    likeRequest.setLike(isLike);
                    UiThreadHandler.get().post(new Runnable() {
                        @Override
                        public void run() {
                            executeLikeRequest(likeRequest);
                        }
                    });
                } else if (cachedLikeRequest.isLike() != isLike) {
                    // no need to execute request anymore, it's cancelled by user
                    likeDao.deleteRequest(cachedLikeRequest);
                    UiThreadHandler.get().post(new Runnable() {
                        @Override
                        public void run() {
                            likeButton.setEnabled(true);
                        }
                    });
                }
            }
        });
    }

    @UiThread
    private void executeLikeRequest(@NonNull final LikeRequest likeRequest) {
        final Callback<ResponseBody> callback = new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                likeButton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                likeButton.setEnabled(true);

                Executors.commonExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        StorageProvider.getAppDatabase().likeDao().addRequest(likeRequest);
                    }
                });
                scheduleSyncWorkIfNeeded();
            }
        };

        if (likeRequest.isLike()) {
            NetworkManager.getApiInstance().likePhoto(likeRequest.getPhotoId()).enqueue(callback);
        } else {
            NetworkManager.getApiInstance().unlikePhoto(likeRequest.getPhotoId()).enqueue(callback);
        }
    }

    private void scheduleSyncWorkIfNeeded() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        WorkRequest request = new OneTimeWorkRequest.Builder(LikeSyncWorker.class)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance().enqueue(request);
    }

    public interface CollectionFragmentListener {
        void onPreviewCollection(int collectionId);
    }
}
