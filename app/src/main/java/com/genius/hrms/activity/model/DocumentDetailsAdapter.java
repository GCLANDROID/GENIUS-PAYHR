package com.genius.hrms.activity.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;

import java.util.ArrayList;

public class DocumentDetailsAdapter extends RecyclerView.Adapter<DocumentDetailsAdapter.MyViewHolder> {

    ArrayList<DocumentDetailsModel> reportList = new ArrayList<>();
    Context mContex;

    @NonNull
    @Override
    public DocumentDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_details_raw, parent, false);
        return new DocumentDetailsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentDetailsAdapter.MyViewHolder holder, int position) {
        holder.tvLeaveAttendance.setText(reportList.get(position).getManualCategory());
        holder.tvLeaveApplication.setText("( " + reportList.get(position).getManualSubCategory() + " )");
        holder.tvDescriprtion.setText(reportList.get(position).getManualDescription());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLeaveAttendance;
        TextView tvLeaveApplication;
        TextView tvDescriprtion;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeaveAttendance = (TextView) itemView.findViewById(R.id.tvLeaveAttendance);
            tvLeaveApplication = (TextView) itemView.findViewById(R.id.tvLeaveApplication);
            tvDescriprtion = (TextView) itemView.findViewById(R.id.tvDescriprtion);
        }
    }
    public DocumentDetailsAdapter(ArrayList<DocumentDetailsModel> reportList)
    {
        this.reportList = reportList;
    }
}
