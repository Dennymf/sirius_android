package ru.mail.park.advandroid.helloword.lection2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class UIElements extends Activity {

	Button pushButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_elements);

		String[] data = {"one", "two", "three", "four", "five"};

		pushButton = (Button)findViewById(R.id.push_button);
		Spinner spinner = (Spinner) findViewById(R.id.spinner);

		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setPrompt("Title");
		spinner.setSelection(2);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
											  @Override
											  public void onItemSelected(AdapterView<?> parent, View view,
																		 int position, long id) {
												  Toast.makeText(getBaseContext(), "Position = " + position, Toast.LENGTH_SHORT).show();
											  }

											  @Override
											  public void onNothingSelected(AdapterView<?> parent) {

											  }
										  });

		setListeners();
	}

	private void setListeners() {
		pushButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(UIElements.this, "Button pressed", Toast.LENGTH_LONG).show();
			}
		});
	}
}
