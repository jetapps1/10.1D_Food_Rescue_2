package com.example.a101dfoodrescue;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private List<CheckoutCard> cardList;
    private Context context;

    public CartListAdapter(List<CheckoutCard> cardList, Context context) {
        this.cardList = cardList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.checkout_card, parent, false);
        return new CartListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {
        holder.title.setText(cardList.get(position).getTitle());
        holder.price.setText("$" + cardList.get(position).getPrice());
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtName);
            price = itemView.findViewById(R.id.txtPrice);
        }
    }
    @Override
    public int getItemCount() {
        return cardList.size();
    }
}
