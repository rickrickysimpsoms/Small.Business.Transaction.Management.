package com.example.developer.calvin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.developer.calvin.Models.Home;
import com.example.developer.calvin.R;
import com.example.developer.calvin.TransactionEditActivity;

import java.util.List;

/**
 * Created by Developer on 1/10/2019.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder>{
    private Context mCtx;
    private List<Home> transactionList;
    String Id;

    public HomeAdapter (Context mCtx, List<Home> transactionList){
        this.mCtx = mCtx;
        this.transactionList = transactionList;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.home_cardview, parent, false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        Home home = transactionList.get(position);

        holder.id = home.getTransactionid();
        holder.textViewComment.setText(home.getComment());
        holder.textViewAmount.setText(home.getAmount() + "Ksh");
        holder.textViewCustomer.setText(home.getCustomer());

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private int id;
        TextView textViewAmount, textViewName, textViewComment,textViewCustomer;
        public HomeViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            textViewCustomer = itemView.findViewById(R.id.textViewCustomer);

        }
        @Override
        public void onClick(View view) {

        }
    }
}
