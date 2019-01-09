package ru.mail.park.advandroid.helloword.lection2;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;


public class SimpleAsyncLoader extends AsyncTaskLoader<String> {
    public static final String TEXT_KEY = "TEXT_KEY";
    private final String mText;

    public SimpleAsyncLoader(Context context, String text) {
        super(context);
        mText = text;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }


    @Override
    public String loadInBackground() {
        try {
            Thread.sleep(3000);
        }
        catch (InterruptedException ex) {

        }
        return mText;
    }
}
