package edu.sirius.examples.android.lesson4;

import android.content.Context;
import android.content.Loader;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by a.halaidzhy on 08.01.2018.
 */

public class LoaderExample extends Loader<String> {
    private final String LOG_TAG = "myLog";
    private final int PAUSE = 5;
    private GetExampleTask getExampleTask;

    public LoaderExample(Context context) {
        super(context);
        Log.d(LOG_TAG, "create LoaderExample()");
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        Log.d(LOG_TAG, "onStartLoading()");
        if (takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        Log.d(LOG_TAG, "onStopLoading()");
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        Log.d(LOG_TAG, "onForceLoad()");
        if (getExampleTask != null) {
            getExampleTask.cancel(true);
        }
        getExampleTask = new GetExampleTask();
        getExampleTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected void onAbandon() {
        super.onAbandon();
        Log.d(LOG_TAG, "onAbandon()");
    }

    @Override
    protected void onReset() {
        super.onReset();
        Log.d(LOG_TAG, "onReset()");
    }

    void setResultFromTask(String result) {
        deliverResult(result);
    }

    class GetExampleTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            Log.d(LOG_TAG, "doInBackground()");
            for (int i = 1; i <= PAUSE; ++i) {
                publishProgress(String.valueOf(i));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return "Result: " + String.valueOf(42);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            setResultFromTask(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(LOG_TAG, "onPostExecute() -> " + result);
            setResultFromTask(result);
        }
    }
}
