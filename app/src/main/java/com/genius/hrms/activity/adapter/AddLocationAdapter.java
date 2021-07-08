package com.genius.hrms.activity.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.model.AddedLocationModel;


import java.util.ArrayList;


public class AddLocationAdapter extends RecyclerView.Adapter<AddLocationAdapter.MyViewHolder> {
    ArrayList<AddedLocationModel>itemList=new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.addedlocation_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.tvLocation.setText(itemList.get(i).getLoaction());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);


        }
    }

    public AddLocationAdapter(ArrayList<AddedLocationModel> itemList) {
        this.itemList = itemList;
    }
}
