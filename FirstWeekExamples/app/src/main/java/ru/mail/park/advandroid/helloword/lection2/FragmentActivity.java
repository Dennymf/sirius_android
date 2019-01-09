/**
 * Copyrigh Mail.ru Games (c) 2015
 * Created by y.bereza.
 */
package ru.mail.park.advandroid.helloword.lection2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import ru.mail.park.advandroid.helloword.lection2.fragments.SimpleFragment;

public class FragmentActivity extends AppCompatActivity{


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_activity);
		final FragmentManager fm = getSupportFragmentManager();

		if (savedInstanceState == null) {
			fm.beginTransaction().replace(R.id.fragment, new SimpleFragment(), SimpleFragment.TAG).commit();
		}


	}
}
