package ru.mipt.dbexample;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private DBHelper mDBHelper;
    private ListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mDBHelper = new DBHelper(this);
        ListView listView = (ListView)findViewById(R.id.list);
        mListAdapter = new ListAdapter(this, mDBHelper);
        listView.setAdapter(mListAdapter);
        listView.setOnItemClickListener(this);

        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.delete).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add:
                addNewValue();
                break;
            case R.id.delete:
                removeLastValue();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        editValue(i);
    }

    private void removeLastValue() {
        mDBHelper.removeLast();
        mListAdapter.notifyDataSetChanged();
    }

    private void addNewValue() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.enter_text));

        final LayoutInflater l = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = l.inflate(R.layout.edit, null);
        final EditText text = (EditText) v.findViewById(R.id.edit_text);
        alertDialog.setView(v);
        text.setText("Hello, World!");

        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = text.getText().toString();
                mDBHelper.addValue(value);
                mListAdapter.notifyDataSetChanged();
            }
        });

        alertDialog.show();
    }

    private void editValue(final int pos) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(getResources().getString(R.string.enter_text));

        final LayoutInflater l = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = l.inflate(R.layout.edit, null);
        final EditText text = (EditText) v.findViewById(R.id.edit_text);
        alertDialog.setView(v);

        String value = mDBHelper.getValue(pos+1);
        if (value != null) {
            text.setText(value);
        }

        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value = text.getText().toString();
                mDBHelper.editValue(pos+1, value);
                mListAdapter.notifyDataSetChanged();
            }
        });

        alertDialog.show();
    }
}
