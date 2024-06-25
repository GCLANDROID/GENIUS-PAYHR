package com.genius.payhrms.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.model.EmpListModel;

import java.util.ArrayList;


public class EmpDetailsAdapter extends RecyclerView.Adapter<EmpDetailsAdapter.MyViewHolder> {
    ArrayList<EmpListModel>itemList=new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emplist_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        myViewHolder.tvEmployeeName.setText(itemList.get(i).getEmpName());


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmployeeName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmployeeName=(TextView)itemView.findViewById(R.id.tvEmployeeName);


        }
    }

    public EmpDetailsAdapter(ArrayList<EmpListModel> itemList) {
        this.itemList = itemList;
    }
}
