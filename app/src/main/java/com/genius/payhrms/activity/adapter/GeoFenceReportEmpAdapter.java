package com.genius.payhrms.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.model.GeoFenceReportEmpModel;

import java.util.ArrayList;


public class GeoFenceReportEmpAdapter extends RecyclerView.Adapter<GeoFenceReportEmpAdapter.MyViewHolder> {
    ArrayList<GeoFenceReportEmpModel>itemList=new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.geofence_report_emp_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

     myViewHolder.tvAddress.setText(itemList.get(i).getAddress()+" ( Coordinates : "+itemList.get(i).getLaat()+" , "+itemList.get(i).getLoong()+" )");
     myViewHolder.tvStatus.setText("Approval Status :-"+itemList.get(i).getStatus());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress,tvStatus;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);
            tvStatus=(TextView) itemView.findViewById(R.id.tvStatus);


        }
    }

    public GeoFenceReportEmpAdapter(ArrayList<GeoFenceReportEmpModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
