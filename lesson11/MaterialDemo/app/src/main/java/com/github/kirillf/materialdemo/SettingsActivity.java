package com.github.kirillf.materialdemo;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.github.kirillf.materialdemo.dashboard.DashboardFragment;
import com.github.kirillf.materialdemo.home.HomeFragment;
import com.github.kirillf.materialdemo.notifications.NotificationsFragment;

public class SettingsActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeFragment(new HomeFragment());
                    return true;
                case R.id.navigation_dashboard:
                    changeFragment(new DashboardFragment());
                    return true;
                case R.id.navigation_notifications:
                    changeFragment(new NotificationsFragment());
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_home);
    }

    private void changeFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();
    }

}
