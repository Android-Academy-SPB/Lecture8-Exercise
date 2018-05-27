package spb.android.academy.fragments.common;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

/**
 * @author Artur Vasilov
 */
public final class UiThreadHandler {

    private static final Handler INSTANCE = new Handler(Looper.getMainLooper());

    private UiThreadHandler() {
    }

    @NonNull
    public static Handler get() {
        return INSTANCE;
    }
}
