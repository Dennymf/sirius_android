/**
 * Copyright Mail.ru Games (c) 2015
 * Created by y.bereza.
 */
package ru.mail.park.advandroid.helloword.lection2;

import android.app.IntentService;
import android.content.Intent;
import android.os.Looper;
import android.os.Handler;
import android.widget.Toast;

public class CommandService extends IntentService {
	public static final String ACTION = "COMMAND_SERVICE_ACTION";
	private Handler mHandler = new Handler(Looper.myLooper());

	public CommandService() {
		this("Command Service");
	}

	public CommandService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			Thread.sleep(2000);
			if (intent.getAction().equals(ACTION)) {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(CommandService.this, "Hello, from CommandService", Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
		catch (InterruptedException e) {

		}
	}
}
