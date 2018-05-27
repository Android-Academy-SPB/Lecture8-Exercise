package spb.android.academy.fragments.common;

import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Artur Vasilov
 */
public final class Executors {

    private static final ExecutorService COMMON_EXECUTOR = new ThreadPoolExecutor(
            0, // initial size
            3, // maximum size
            60,
            TimeUnit.SECONDS,
            new SynchronousQueue<Runnable>()
    );

    @NonNull
    public static ExecutorService commonExecutor() {
        return COMMON_EXECUTOR;
    }
}
