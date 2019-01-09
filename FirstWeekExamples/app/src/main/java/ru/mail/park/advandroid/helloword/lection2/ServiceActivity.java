/**
 * Copyright Mail.ru Games (c) 2015
 * Created by y.bereza.
 */
package ru.mail.park.advandroid.helloword.lection2;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ServiceActivity extends AppCompatActivity {

	private Button mServiceButton;
	private Button mSendCommandButton;

	private TextView mTextView;

	private ITestService mTestService;

	private ServiceConnection mServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mTestService = ITestService.Stub.asInterface(service);
			try {
				mTestService.bindActivity(mServiceCallback);
				mTestService.getStringAsync();
				mServiceButton.setText("Connected");
			}
			catch (RemoteException e) {
				mTextView.setText("Service connection error");
				Log.e("ServiceActivity", "Can not get string from service", e);
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
	};

	private class ServiceCallback extends ICallback.Stub {
		@Override
		public void onNewString(final String data) throws RemoteException {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mTextView.setText(data);
				}
			});
		}
	}

	private ServiceCallback mServiceCallback = new ServiceCallback();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTextView = (TextView)findViewById(R.id.textView);
 		mServiceButton = (Button)findViewById(R.id.connect);
		mServiceButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mTestService == null) {
					Intent i = new Intent(ServiceActivity.this, TestService.class);
					bindService(i, mServiceConnection, BIND_AUTO_CREATE);
				}
				else {
					disconnect();
				}
			}
		});

		mSendCommandButton = (Button)findViewById(R.id.sendCommand);
		mSendCommandButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ServiceActivity.this, CommandService.class);
				i.setAction(CommandService.ACTION);
				startService(i);
			}
		});
	}

	private void disconnect() {
		if (mTestService != null) {
			unbindService(mServiceConnection);
			mTestService = null;
			mTextView.setText("");
			mServiceButton.setText("Connect");
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mTextView.setText("");
		mServiceButton.setText("Connect");
	}

	@Override
	protected void onStop() {
		disconnect();
		super.onStop();
	}
}
