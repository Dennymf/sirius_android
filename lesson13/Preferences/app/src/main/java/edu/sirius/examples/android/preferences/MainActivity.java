package edu.sirius.examples.android.preferences;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private TextView savedSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        savedSettings = findViewById(R.id.saved_settings);
        findViewById(R.id.open_settings).setOnClickListener((v) -> startActivity(new Intent(MainActivity.this, PrefActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Boolean notif = sp.getBoolean("notif", false);
        String address = sp.getString("address", "");
        String text = "Notifications are "
                + ((notif) ? "enabled, address = " + address : "disabled");
        savedSettings.setText(text);
    }
}
