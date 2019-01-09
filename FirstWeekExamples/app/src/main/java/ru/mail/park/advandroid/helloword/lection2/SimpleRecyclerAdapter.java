/**
 * Copyright Mail.ru Games (c) 2015
 * Created by y.bereza.
 */
package ru.mail.park.advandroid.helloword.lection2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

public class SimpleRecyclerAdapter extends RecyclerView.Adapter<SimpleViewHolder> {
	public static final String[] sItems = {"lorem", "ipsum", "dolor", "sit", "amet", "consectetuer", "adipiscing", "elit",
			"morbi", "vel", "ligula", "vitae", "arcu", "aliquet", "mollis", "etiam", "vel", "erat", "placerat", "ante",
			"porttitor", "sodales", "pellentesque", "augue", "purus"};

	private final WeakReference<LayoutInflater> mInflater;

	public SimpleRecyclerAdapter(LayoutInflater inflater) {
		mInflater = new WeakReference<LayoutInflater>(inflater);
	}

	@Override
	public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = mInflater.get();
		if (inflater != null) {
			return new SimpleViewHolder(inflater.inflate(R.layout.item_click_layout, parent, false));
		}
		else {
			throw new RuntimeException("Oooops, looks like activity is dead");
		}
	}

	@Override
	public void onBindViewHolder(SimpleViewHolder holder, int position) {
		holder.setText(sItems[position]);
		holder.setTitle(sItems[position].substring(0,1).toUpperCase());
	}

	@Override
	public int getItemCount() {
		return sItems.length;
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}
}
