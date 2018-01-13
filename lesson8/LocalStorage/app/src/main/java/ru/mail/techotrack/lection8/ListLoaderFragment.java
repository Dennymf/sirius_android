package ru.mail.techotrack.lection8;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.WeakHashMap;


public class ListLoaderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Bitmap> {

	public static int _imageSize;
	private LruCache<String, Bitmap> _memoryCache;
	private WeakHashMap<String, ImageView> _imageViews = new WeakHashMap<>();

	@Override
	public Loader<Bitmap> onCreateLoader(int id, Bundle args) {
		final String name = args.getString("name");
		return new BitmapLoader(getActivity(), name);
	}

	@Override
	public void onLoadFinished(Loader<Bitmap> l, Bitmap bitmap) {
		BitmapLoader loader = (BitmapLoader)l;
		Bitmap bm = getBitmapFromMemCache(loader.getName());
		if (bm == null && bitmap != null) {
			addBitmapToMemoryCache(loader.getName(), bitmap);
			bm = bitmap;
		}
		ImageView iv = _imageViews.get(loader.getName());
		if (iv != null) {
			iv.setImageBitmap(bm);
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
			params.width = _imageSize;
			params.height = _imageSize;
			params.gravity = Gravity.CENTER_HORIZONTAL;
			iv.setLayoutParams(params);
		}
	}

	@Override
	public void onLoaderReset(Loader<Bitmap> loader) {

	}

	private static int updateImageSize(DisplayMetrics dm) {
		int h = dm.heightPixels;
		int w = dm.widthPixels;
		if (w > h) {
			int tmp = w;
			w = h;
			h = tmp;
		}
		return (int)(Math.min(h * 0.7f, w * 0.7f) + 0.5f);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View root = inflater.inflate(R.layout.list_fragment, container, false);
		if (null == root) return null;
		if (_memoryCache == null) {
			final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
			final int cacheSize = maxMemory / 8;
			_memoryCache = new LruCache<String, Bitmap>(cacheSize) {
				@Override
				protected int sizeOf(String key, Bitmap bitmap) {
					return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
				}
			};
		}
		_imageSize = updateImageSize(getResources().getDisplayMetrics());

		RecyclerView mRecyclerView = (RecyclerView) root.findViewById(R.id.dict_list);
		// use this setting to improve performance if you know that changes
		// in content do not change the layout size of the RecyclerView
		mRecyclerView.setHasFixedSize(true);

		// use a linear layout manager
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
		mRecyclerView.setLayoutManager(mLayoutManager);

		MyAdapter wa = new MyAdapter(
				getActivity(),
				R.layout.list_element,
				ImageData.instance().getImages()
		);
		mRecyclerView.setAdapter(wa);
		return root;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		_memoryCache.put(key, bitmap);
	}

	public Bitmap getBitmapFromMemCache(String key) {
		return _memoryCache.get(key);
	}


	private static class ViewHolder extends RecyclerView.ViewHolder {
		TextView _tvw;
		ImageView _iv;
		View _card;
		int _pos;

		public ViewHolder(View itemView) {
			super(itemView);
			_card = itemView;
		}
	}

	private static class DownloadDrawable extends ColorDrawable {
		private final Integer mLoaderId;

		private DownloadDrawable(Integer loaderId) {
			super(Color.WHITE);
			mLoaderId = loaderId;
		}

		public Integer getLoaderId() {
			return mLoaderId;
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void loadBitmap(Context context, String name, ImageView iv) {
		final Bitmap bm = getBitmapFromMemCache(name);
		if (null != bm) {
			iv.setImageBitmap(bm);
		} else {
			Bundle b = new Bundle();
			b.putString("name", name);
			_imageViews.put(name, iv);
			DownloadDrawable dd = new DownloadDrawable(name.hashCode());
			iv.setImageDrawable(dd);

			getLoaderManager().initLoader(name.hashCode(), b, this);
		}
	}

	private static Integer getBitmapLoaderId(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof DownloadDrawable) {
				DownloadDrawable dd = (DownloadDrawable)drawable;
				return dd.getLoaderId();
			}
		}
		return null;
	}

	private class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

		Context _context;
		int _resource;
		List<ImageData.Image> _data;


		public MyAdapter(Context context, int resource, List<ImageData.Image> objects) {
			_context = context;
			_data = objects;
			_resource = resource;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
			final LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			View convertView = inflater.inflate(R.layout.list_element, parent, false);
			ViewHolder holder = new ViewHolder(convertView);
			assert convertView != null;
			holder._tvw = (TextView)convertView.findViewById(R.id.dict_word);
			holder._iv = (ImageView)convertView.findViewById(R.id.dict_image);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(_imageSize, _imageSize);
			holder._iv.setLayoutParams(lp);


			//convertView.setTag(holder);
			//ViewCompat.setElevation(convertView, 3);
			//ViewCompat.setTranslationZ(convertView, 3);


			return holder;
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {
			ImageData.Image word = ImageData.instance().getImage(position);
			if (word == null) return;
			holder._pos = position;
			holder._tvw.setText(word.getText());
			final int i = position;
			holder._card.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//((ScrollingActivity)getActivity()).doSomething(i);
				}
			});
			ListLoaderFragment.this.loadBitmap(getActivity(), word.getImage(), holder._iv);
		}

		@Override
		public int getItemCount() {
			return _data.size();
		}
	}
}

