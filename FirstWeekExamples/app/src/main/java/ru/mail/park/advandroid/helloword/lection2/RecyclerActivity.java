package ru.mail.park.advandroid.helloword.lection2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

public class RecyclerActivity extends AppCompatActivity implements
		ClickRecyclerAdapter.OnItemClickListener {
	private RecyclerView mRecyclerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRecyclerView = new RecyclerView(this);

		mRecyclerView.setAdapter(new ClickRecyclerAdapter(getLayoutInflater(), this));
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

		mRecyclerView.setHasFixedSize(true);
		setContentView(mRecyclerView);
	}

	@Override
	public void onItemClick(View view, int position) {
		Toast.makeText(this, SimpleRecyclerAdapter.sItems[position], Toast.LENGTH_SHORT).show();
	}
}
