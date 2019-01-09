package ru.mail.park.advandroid.helloword.lection2;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ListViewBaseAdapterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout);
        ListView lv = (ListView) findViewById(R.id.listview);
        String[] names = getResources().getStringArray(R.array.names);
        lv.setAdapter(new MyAdapter(this, names));

    }

    static class MyAdapter extends BaseAdapter {

        Context context;
        ArrayList names;

        public static class ViewHolder {
           public TextView textView;
        }

        MyAdapter(Context context, String[] list) {
            this.context = context;
            names = new ArrayList<>();
            Collections.addAll(names, list);

        }

        @Override
        public int getCount() {
            return names.size();
        }

        @Override
        public Object getItem(int position) {
            return names.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String str = (String) getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();
                holder.textView = (TextView)convertView.findViewById(R.id.element_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.textView.setText(str);
            if ((position & 1) == 1) {
                convertView.setBackgroundColor(Color.rgb(170, 170, 170));
            } else {
                convertView.setBackgroundColor(Color.rgb(255, 255, 255));
            }
            return convertView;
        }
    }
}
