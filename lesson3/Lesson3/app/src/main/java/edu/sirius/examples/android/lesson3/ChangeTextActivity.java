package edu.sirius.examples.android.lesson3;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Created by a.halaidzhy on 05.01.2018.
 */

public class ChangeTextActivity extends AppCompatActivity {
    private EditText editText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.set_text);
        editText = findViewById(R.id.editText);
        findViewById(R.id.saveText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("text", editText.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
