/**
 * Copyright Mail.ru Games (c) 2015
 * Created by y.bereza.
 */
package ru.mail.park.advandroid.helloword.lection2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import java.util.concurrent.TimeUnit;

public class TestService extends Service {
	private class TestServiceProxy extends ITestService.Stub {
		private ICallback mActivityCallbak;

		@Override
		public String getString() throws RemoteException {
			return sayHello();
		}

		@Override
		public void bindActivity(IBinder callback) throws RemoteException {
			mActivityCallbak = ICallback.Stub.asInterface(callback);
		}

		@Override
		public void getStringAsync() {
			sendString("Hello from Service!");
		}

		public void sendString(final String data) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					if (mActivityCallbak != null) {
						try {
							Thread.sleep(TimeUnit.SECONDS.toMillis(1));
							mActivityCallbak.onNewString(data);
						}
						catch (InterruptedException e) {

						}
						catch (RemoteException e) {

						}
					}
				}
			}).start();
		}
	}

	private TestServiceProxy mServiceProxy = new TestServiceProxy();

	@Override
	public IBinder onBind(Intent intent) {
		return mServiceProxy;
	}

	public String sayHello() {
		return "Hello from Service!";
	}
}
