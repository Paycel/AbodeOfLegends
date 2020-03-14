package com.example.adobeoflegends;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder> {
    private int position;
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView tvStats;
        TextView tvDescription;
        ImageView imageView;
        MyViewHolder(View itemView){
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.myCardView);
            tvStats = (TextView) itemView.findViewById(R.id.cardStats);
            tvDescription = (TextView) itemView.findViewById(R.id.cardDescription);
            imageView = (ImageView) itemView.findViewById(R.id.CardImage);
        }
    }

    List<Card> cardList;
    RVAdapter(List<Card> cardList, int position){
        this.cardList = cardList;
        this.position = position;
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvStats.setText(cardList.get(position).damagePoints + " " + cardList.get(position).healthPoints);
        holder.tvDescription.setText(cardList.get(position).name);
        holder.imageView.setImageResource(cardList.get(position).pictureID);
    }
}
