package com.example.a101dfoodrescue;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private final List<Card> cardList;
    private final Context context;
    private final OnRowClickListener listener;

    public CardAdapter(List<Card> cardList, Context context, OnRowClickListener clickListener) {
        this.cardList = cardList;
        this.context = context;
        this.listener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.card, parent, false);
        return new ViewHolder(itemView, listener);
    }

    public interface OnRowClickListener {
        void onItemClick (int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ImageView;
        public TextView title;
        public TextView desc;
        public OnRowClickListener onRowClickListener;
        public Button share;

        public ViewHolder(@NonNull View itemView, OnRowClickListener onRowClickListener) {
            super(itemView);
            ImageView = itemView.findViewById(R.id.destinationImageView);
            title = itemView.findViewById(R.id.titleTextView);
            desc = itemView.findViewById(R.id.descTextView);
            share = itemView.findViewById(R.id.button);

            this.onRowClickListener = onRowClickListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            onRowClickListener.onItemClick(getAdapterPosition());
        }
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.ImageView.setImageURI(cardList.get(position).getImage());
        holder.title.setText(cardList.get(position).getTitle());
        holder.desc.setText(cardList.get(position).getDesc());
        holder.share.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, holder.title.getText().toString());
            sendIntent.putExtra(Intent.EXTRA_TEXT, holder.desc.getText().toString());
            sendIntent.setType("text/plain");
        });
    }

    @Override
    public int getItemCount() {
        return this.cardList.size();
    }
}
