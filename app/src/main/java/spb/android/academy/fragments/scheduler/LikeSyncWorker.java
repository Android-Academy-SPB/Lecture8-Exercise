package spb.android.academy.fragments.scheduler;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import androidx.work.Worker;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import spb.android.academy.fragments.network.Api;
import spb.android.academy.fragments.network.NetworkManager;
import spb.android.academy.fragments.storage.StorageProvider;
import spb.android.academy.fragments.storage.database.LikeDao;
import spb.android.academy.fragments.storage.database.LikeRequest;

public class LikeSyncWorker extends Worker {

    @NonNull
    @Override
    public WorkerResult doWork() {
        final LikeDao likeDao = StorageProvider.getAppDatabase().likeDao();
        List<LikeRequest> requests = likeDao.getAllRequests();

        final Api api = NetworkManager.getApiInstance();
        boolean hasFailedRequests = false;
        for (LikeRequest likeRequest : requests) {
            Call<ResponseBody> call;
            if (likeRequest.isLike()) {
                call = api.likePhoto(likeRequest.getPhotoId());
            } else {
                call = api.unlikePhoto(likeRequest.getPhotoId());
            }

            try {
                Response<ResponseBody> response = call.execute();
                if (response.code() >= 400 || response.errorBody() != null) {
                    hasFailedRequests = true;
                } else {
                    likeDao.deleteRequest(likeRequest);
                }
            } catch (IOException e) {
                hasFailedRequests = true;
            }
        }

        return hasFailedRequests ? WorkerResult.RETRY : WorkerResult.SUCCESS;
    }
}
