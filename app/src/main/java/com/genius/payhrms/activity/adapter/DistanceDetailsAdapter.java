package com.genius.payhrms.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.model.DistanceModel;
import com.genius.payhrms.activity.utility.Util;

import java.util.ArrayList;

public class DistanceDetailsAdapter extends RecyclerView.Adapter<DistanceDetailsAdapter.MyViewHolder> {
    Context context;
    ArrayList<DistanceModel> distanceList;

    public DistanceDetailsAdapter(Context context, ArrayList<DistanceModel> distanceList) {
        this.context = context;
        this.distanceList = distanceList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_distance, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvAddress.setText(distanceList.get(position).getAddress());
        holder.tvTime.setText(Util.changeAnyDateFormat(distanceList.get(position).getAttendnacedate(),"yyyy-MM-dd'T'HH:mm:ss.SSS", "hh:mm a"));
        holder.tvKm.setText(distanceList.get(position).getDistanceInKM());
    }

    @Override
    public int getItemCount() {
        return distanceList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvAddress,tvKm,tvTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvKm = itemView.findViewById(R.id.tvKm);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}
