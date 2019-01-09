package ru.mail.park.advandroid.helloword.lection2;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class LoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private TextView mTextView;
    public static final int SIMPLE_ASYNC_LOADER_ID = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loader_activity);
        mTextView = (TextView)findViewById(R.id.text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle data = new Bundle();
        data.putString(SimpleAsyncLoader.TEXT_KEY, getString(R.string.hello_world));

        if (!getSupportLoaderManager().hasRunningLoaders()) {
            getSupportLoaderManager().initLoader(SIMPLE_ASYNC_LOADER_ID, data, this);
        }
        else {
            getSupportLoaderManager().restartLoader(SIMPLE_ASYNC_LOADER_ID, data, this);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new SimpleAsyncLoader(this, args.getString(SimpleAsyncLoader.TEXT_KEY));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mTextView.setText(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        mTextView.setText("");
    }
}
