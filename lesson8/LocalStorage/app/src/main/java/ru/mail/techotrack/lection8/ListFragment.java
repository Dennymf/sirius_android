package ru.mail.techotrack.lection8;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;


public class ListFragment extends Fragment {

    private static int _imageSize;
    private LruCache<String, Bitmap> _memoryCache;

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

    private class LoadImageTask extends AsyncTask<Void, Void, Bitmap> {
        private final WeakReference<ImageView> _weakIv;
        private final WeakReference<Context> _context;
        private final String _name;

        public LoadImageTask(Context context, ImageView iv, String name) {
            super();
            _weakIv = new WeakReference<>(iv);
            _context = new WeakReference<>(context);
            _name = name;
        }

        protected Bitmap decodeFile(File file) {
            try {
                InputStream is = new FileInputStream(file);
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, null, opt);
                int sc = calculateInSampleSize(opt, _imageSize, _imageSize);
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

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                Context context = _context.get();
                Bitmap bitmap;
                File file;
                if (context != null) {
                    //InputStream is = context.getAssets().open(_name);
                    file = new File(context.getCacheDir(), _name.replace("/", ""));
                    bitmap = decodeFile(file);
                    if (null == bitmap ) {
                        URL url = new URL(_name);
                        InputStream is = url.openConnection().getInputStream();
                        OutputStream os = new FileOutputStream(file);
                        Utils.CopyStream(is, os);
                        os.close();
                        bitmap = decodeFile(file);
                    }
                    return bitmap;
                }
            } catch (IOException e) {
                Log.e("LoadImageTask", "LoadImageTask.LoadBitmap IOException " + e.getMessage(), e);
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled())
                bitmap = null;

            Bitmap bm = ListFragment.this.getBitmapFromMemCache(_name);
            if (bm == null && bitmap != null) {
                ListFragment.this.addBitmapToMemoryCache(_name, bitmap);
                bm = bitmap;
            }
            ImageView iv = _weakIv.get();
            if (iv != null && this == getBitmapDownloaderTask(iv)) {

                iv.setImageBitmap(bm);
                // Now change ImageView's dimensions to match the scaled image
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) iv.getLayoutParams();
                params.width = _imageSize;
                params.height = _imageSize;
                //params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                //params.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                params.gravity = Gravity.CENTER_HORIZONTAL;
                iv.setLayoutParams(params);

            }
        }
    }

    private static class DownloadDrawable extends ColorDrawable {
        private final WeakReference<LoadImageTask> _loadTaskWeak;

        private DownloadDrawable(LoadImageTask loadTask) {
            super(Color.WHITE);
            _loadTaskWeak = new WeakReference<>(loadTask);
        }

        public LoadImageTask getTask() {
            return _loadTaskWeak.get();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void loadBitmap(Context context, String name, ImageView iv) {
        final Bitmap bm = getBitmapFromMemCache(name);
        if (null != bm) {
            cancelDownload(name, iv);
            iv.setImageBitmap(bm);
        } else {
            LoadImageTask lt = new LoadImageTask(context, iv, name);
            DownloadDrawable dd = new DownloadDrawable(lt);
            iv.setImageDrawable(dd);

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                lt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                lt.execute();
        }
    }

    private static void cancelDownload(String key, ImageView imageView) {
        LoadImageTask task = getBitmapDownloaderTask(imageView);
        if (null != task) {
            String bitKey = task._name;
            if ((bitKey == null) || (!bitKey.equals(key))) {
                task.cancel(true);
            }
        }
    }

    private static LoadImageTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadDrawable) {
                DownloadDrawable dd = (DownloadDrawable)drawable;
                return dd.getTask();
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
            ListFragment.this.loadBitmap(getActivity(), word.getImage(), holder._iv);
        }

        @Override
        public int getItemCount() {
            return _data.size();
        }
    }
}

