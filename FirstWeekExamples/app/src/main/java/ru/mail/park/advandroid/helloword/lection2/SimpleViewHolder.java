/**
 * Copyright Mail.ru Games (c) 2015
 * Created by y.bereza.
 */
package ru.mail.park.advandroid.helloword.lection2;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class SimpleViewHolder extends RecyclerView.ViewHolder {
	public void setTitle(String title) {
		mTitle.setText(title);
	}

	public void setText(String text) {
		mText.setText(text);
	}

	private TextView mTitle;
	private TextView mText;

	public SimpleViewHolder(View itemView) {
		super(itemView);

		mTitle = (TextView)itemView.findViewById(R.id.title);
		mText = (TextView)itemView.findViewById(R.id.text);
	}
}
