package edu.sirius.examples.android.lesson4;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView postTextView;
    private TextView activityTextView;
    private TextView handlerTextView;
    private TextView asyncTextView;
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
}
