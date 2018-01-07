package edu.sirius.examples.android.lesson4;

import android.app.LoaderManager;
import android.content.Loader;
import android.database.ContentObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private final static String LOG_TAG = "myLog";
    private final static int LOADER_ID = 1;
    private final static int ASYNC_LOADER_ID = 2;

    private TextView postTextView;
    private TextView activityTextView;
    private TextView handlerTextView;
    private TextView asyncTextView;
    private TextView loaderTextView;
    private TextView asyncLoaderTextView;
    private int postCounter;
    private int activityCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        postTextView = findViewById(R.id.postTextView);

        PostWorkingClass postWorkingClass = new PostWorkingClass();
        Thread postThread = new Thread(postWorkingClass);
        postThread.start();

        activityTextView = findViewById(R.id.activityTextView);

        ActivityWorkingClass activityWorkingClass = new ActivityWorkingClass();
        Thread activityThread = new Thread(activityWorkingClass);
        activityThread.start();

        handlerTextView = findViewById(R.id.handlerTextView);

        HandlerWorkingClass handlerWorkingClass = new HandlerWorkingClass(false);
        Thread handlerThread = new Thread(handlerWorkingClass);
        handlerThread.start();

        asyncTextView = findViewById(R.id.asyncTextView);
        new AsyncTaskExample(1).execute();

        loaderTextView = findViewById(R.id.loaderTextView);
        Bundle bundle = new Bundle();
        Loader loader = getLoaderManager().initLoader(LOADER_ID, bundle, this);
        final ContentObserver observer = loader.new ForceLoadContentObserver();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "Data changed");
                observer.dispatchChange(true, null);
            }
        }, 1000, 10000);

        asyncLoaderTextView = findViewById(R.id.asyncLoaderTextView);
        bundle = new Bundle();
        Loader asyncLoader = getLoaderManager().initLoader(ASYNC_LOADER_ID, bundle, this);
        final ContentObserver asyncObserver = asyncLoader.new ForceLoadContentObserver();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "Data changed");
                asyncObserver.dispatchChange(true, null);
            }
        }, 1000, 10000);
    }

    class PostWorkingClass implements Runnable {
        @Override
        public void run() {
            postCounter = 0;
            while (true) {
                ++postCounter;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Отправление в UI-поток нового Runnable
                postTextView.post(new Runnable() {
                    @Override
                    public void run() {
                        postTextView.setText(postCounter + " seconds");
                    }
                });
            }
        }
    }

    class ActivityWorkingClass implements Runnable {
        @Override
        public void run() {
            activityCounter = 0;
            while (true) {
                ++activityCounter ;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Отправление в UI-поток нового Runnable
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        activityTextView.setText(activityCounter + " seconds");
                    }
                });
            }
        }
    }

    class HandlerWorkingClass implements Runnable {
        private static final int ON = 1;
        private static final int OFF = 2;
        private boolean state;

        Handler uiHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case HandlerWorkingClass.ON:
                        handlerTextView.setText("ON");
                        return true;
                    case HandlerWorkingClass.OFF:
                        handlerTextView.setText("OFF");
                        return true;
                    default:
                        return false;
                }
            }
        });

        HandlerWorkingClass(boolean initState) {
            this.state = initState;
        }

        @Override
        public void run() {
            while (true) {
                if (!state) {
                    // Можно отправить пустое сообщение со статусом
                    uiHandler.sendEmptyMessage(OFF);
                } else {
                    // Можно передать дополнительные данные
                    Message msg = Message.obtain();
                    msg.what = ON;
                    msg.obj = "Will OFF in 1 second";
                    uiHandler.sendMessage(msg);
                }
                state = !state;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class AsyncTaskExample extends AsyncTask {
        private int counter;

        AsyncTaskExample(int start) {
            this.counter = start;
        }

        @Override
        protected void onPreExecute() {
            asyncTextView.setText("onPreExecute()");
        }

        @Override
        protected void onPostExecute(Object o) {
            asyncTextView.setText("onPostExecute() " + o);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AsyncTaskExample(counter).execute();
                        }
                    });
                    cancel();
                }
            }, 5000);
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            asyncTextView.setText("onProgressUpdate() " + values[0]);

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 10; ++i) {
                publishProgress(counter++);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return 42;
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        Loader<String> loader = null;
        if (id == LOADER_ID) {
            loader = new LoaderExample(this);
            Log.d(LOG_TAG, "onCreateLoader()");
        }
        if (id == ASYNC_LOADER_ID) {
            loader = new AsyncLoaderExample(this);
            Log.d(LOG_TAG, "Async onCreateLoader()");
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String result) {
        Log.d(LOG_TAG, "onLoadFinished() -> " + result);
        if (loader.getId() == LOADER_ID) {
            loaderTextView.setText(result);
        }
        if (loader.getId() == ASYNC_LOADER_ID) {
            asyncLoaderTextView.setText(result);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        Log.d(LOG_TAG, "onLoaderReset()");
    }
}
