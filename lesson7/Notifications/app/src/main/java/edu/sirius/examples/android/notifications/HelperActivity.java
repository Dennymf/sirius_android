package edu.sirius.examples.android.notifications;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by a.halaidzhy on 11.01.2018.
 */

public class HelperActivity extends AppCompatActivity {
    private static final String TAG = HelperActivity.class.getCanonicalName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        if (bundle.getBoolean("notification")) {
            MainActivity.messages.clear();
            Toast.makeText(getApplicationContext(), "Нажали на нотификацию", Toast.LENGTH_SHORT).show();
            Log.d(TAG, TAG + ":onCreate()");
        }
        if (bundle.getBoolean("task")) {
            MainActivity.messages.clear();
            Toast.makeText(getApplicationContext(), "Удалили нотификацию", Toast.LENGTH_SHORT).show();
            Log.d(TAG, TAG + ":onCreate() + action");
        }
        finish();
    }
}
