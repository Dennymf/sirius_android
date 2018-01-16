package com.github.kirillf.materialdemo.cards;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.kirillf.materialdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k.filimonov on 11/04/2017.
 */

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.CardViewHolder> {

    private final List<Card> cards;

    public CardsAdapter() {
        cards = new ArrayList<>();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_view, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.title.setText(card.getTitle());
        holder.message.setText(card.getMessage());
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void addCards(List<Card> items) {
        cards.addAll(items);
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView message;

        CardViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.card_title);
            message = (TextView) itemView.findViewById(R.id.card_message);
        }
    }

}
