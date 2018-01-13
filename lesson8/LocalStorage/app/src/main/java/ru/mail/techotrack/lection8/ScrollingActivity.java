package ru.mail.techotrack.lection8;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class ScrollingActivity extends AppCompatActivity {
    private ListFragment _listFragment;
    private static final String TAG = "ScrollingActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        ImageData.createInstance();

        _listFragment = new ListFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        super.onStart();
        Fragment frag = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        if (frag == null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.main_fragment, _listFragment);
            ft.commit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
