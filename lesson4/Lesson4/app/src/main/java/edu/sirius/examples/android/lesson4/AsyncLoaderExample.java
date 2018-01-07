package edu.sirius.examples.android.lesson4;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

/**
 * Created by a.halaidzhy on 08.01.2018.
 */

public class AsyncLoaderExample extends AsyncTaskLoader<String> {
    private final String LOG_TAG = "myLog";
    private final int PAUSE = 5;
    private int counter = 1;

    public AsyncLoaderExample(Context context) {
        super(context);
        Log.d(LOG_TAG, "create AsyncLoaderExample()");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(LOG_TAG, "Async onStartLoading()");
        if (takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.d(LOG_TAG, "Async onStopLoading()");
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        Log.d(LOG_TAG, "Async onForceLoad()");
    }

    @Override
    public String loadInBackground() {
        Log.d(LOG_TAG, "Async loadInBackground()");
        for (int i = 1; i <= PAUSE; ++i) {
            Log.d(LOG_TAG, "Async " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String result = "Result: " + String.valueOf(counter++);
        return result;
    }

    @Override
    protected void onAbandon() {
        super.onAbandon();
        Log.d(LOG_TAG, "Async onAbandon()");
    }

    @Override
    protected void onReset() {
        super.onReset();
        Log.d(LOG_TAG, "Async onReset()");
    }
}
