package ru.mail.park.advandroid.helloword.lection2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListViewArrayAdapterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);

        String[] names = getResources().getStringArray(R.array.names);
        ListView listView = (ListView) findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.element_text, names);
        listView.setAdapter(adapter);
    }
}
