/**
 * Copyrigh Mail.ru Games (c) 2015
 * Created by y.bereza.
 */
package ru.mail.techotrack.lection8;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class BitmapLoader extends AsyncTaskLoader<Bitmap> {
	private final String _name;
	private Bitmap _result;

	public BitmapLoader(Context context, String name) {
		super(context);
		_name = name;
	}

	@Override
	public Bitmap loadInBackground() {
		try {
			Context context = getContext();
			File file;
			if (context != null) {
				//InputStream is = context.getAssets().open(_name);
				file = new File(context.getCacheDir(), _name.replace("/", ""));
				_result = decodeFile(file);
				if (null == _result ) {
					URL url = new URL(_name);
					InputStream is = url.openConnection().getInputStream();
					OutputStream os = new FileOutputStream(file);
					Utils.CopyStream(is, os);
					os.close();
					_result = decodeFile(file);
				}
				return _result;
			}
		} catch (IOException e) {
			Log.e("LoadImageTask", "LoadImageTask.LoadBitmap IOException " + e.getMessage(), e);
		}
		return null;

	}

	protected Bitmap decodeFile(File file) {
		try {
			InputStream is = new FileInputStream(file);
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(is, null, opt);
			int sc = ListLoaderFragment.calculateInSampleSize(opt, ListLoaderFragment._imageSize, ListLoaderFragment._imageSize);
			//is.reset();
			opt.inSampleSize = sc;
			opt.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, opt);
			Log.d("LOAD_IMAGE", " name = " + _name + " w = " + bitmap.getWidth() + " h = " + bitmap.getHeight());
			return bitmap;
		} catch (IOException e) {
			//Log.e("LoadImageTask", "LoadImageTask.LoadBitmap IOException " + e.getMessage(), e);
		}
		return null;
	}

	public String getName() {
		return _name;
	}

	@Override
	protected void onStartLoading() {
		if (_result != null) {
			deliverResult(_result);
		}
		else if (!takeContentChanged()) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		cancelLoad();
	}

	@Override
	protected void onReset() {
		_result = null;
		onStopLoading();
	}
}
