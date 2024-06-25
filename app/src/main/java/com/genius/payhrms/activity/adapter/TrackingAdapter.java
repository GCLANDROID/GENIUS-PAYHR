package com.genius.payhrms.activity.adapter;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.livetracking.TrackMapActivity;
import com.genius.payhrms.activity.model.TrackingModel;


import java.util.ArrayList;


public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.MyViewHolder> {
    ArrayList<TrackingModel>itemList=new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employeesearch_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvEmpName.setText(itemList.get(i).getEmpname());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, TrackMapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("trackId",itemList.get(i).getTrackingId());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmpName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);


        }
    }

    public TrackingAdapter(ArrayList<TrackingModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
