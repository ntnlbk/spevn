package com.LibBib.spevn;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterForRec extends RecyclerView.Adapter<AdapterForRec.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private String nameofcurrentsong;
    private int COLORchecked, COLORunchecked;

    // data is passed into the constructor
    AdapterForRec(Context context, List<String> data, String nameofcurrentsong, int COLORchecked, int COLORunchecked) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.nameofcurrentsong=nameofcurrentsong;
        this.COLORchecked=COLORchecked;
        this.COLORunchecked=COLORunchecked;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fonforrecycler, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String nameofsong = mData.get(position);
        holder.myTextView.setText(nameofsong);
        if(nameofsong.equals(nameofcurrentsong))
            holder.myTextView.setTextColor(COLORchecked);
        else
            holder.myTextView.setTextColor(COLORunchecked);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.myTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
