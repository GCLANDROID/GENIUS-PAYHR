package com.genius.hrms.activity.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.genius.hrms.R;
import com.genius.hrms.databinding.AttendanceMonthlyRawBinding;

import org.json.JSONArray;
import org.json.JSONObject;

public class AttendanceMonthlyAdapter extends RecyclerView.Adapter<AttendanceMonthlyAdapter.ViewHolder> {
    Context context;
    JSONArray itemList;

    public AttendanceMonthlyAdapter(Context context, JSONArray itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AttendanceMonthlyRawBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.attendance_monthly_raw, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final JSONObject jsonObject = itemList.optJSONObject(position);
        holder.binding.tvStart.setText(jsonObject.optString("Intime"));
        String inTime = jsonObject.optString("Intime");
        holder.binding.tvEnd.setText(jsonObject.optString("Outtime"));
        String outTime = jsonObject.optString("Outtime");
        holder.binding.tvCompleteDate.setText(jsonObject.optString("AttDate"));
        holder.binding.tvAttendanceStatus.setText(jsonObject.optString("AttendanceStatus"));
        String attStatus = jsonObject.optString("AttendanceStatus");
        if (inTime.equals("null")) {
            holder.binding.tvStart.setVisibility(View.GONE);
        } else {
            holder.binding.tvStart.setVisibility(View.VISIBLE);
        }

        if (outTime.equals("null")) {
            holder.binding.tvEnd.setVisibility(View.GONE);
        } else {
            holder.binding.tvEnd.setVisibility(View.VISIBLE);
        }
        if (attStatus.equals("null")) {
            //holder.binding.lnMin.setVisibility(View.GONE);
            //holder.itemView.setVisibility(View.GONE);
            //holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));


        } else {
            //holder.binding.lnMin.setVisibility(View.VISIBLE);
            //holder.itemView.setVisibility(View.VISIBLE);
            //holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        }
        if (attStatus.equals("A")) {
            holder.binding.tvAttendanceStatus.setText("Absent");
            holder.binding.lnMin.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#cc0512")));
            ;
        } else if (attStatus.equals("P")) {
            holder.binding.tvAttendanceStatus.setText("Present");
            holder.binding.lnMin.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#006CFF")));
        } else if (attStatus.equals("HDL")) {
            holder.binding.tvAttendanceStatus.setText("Half Day Leave");
            holder.binding.lnMin.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#30B3A3")));
        } else if (attStatus.equals("WO")) {
            holder.binding.tvAttendanceStatus.setText("Weekly Off");
            holder.binding.lnMin.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#a97806")));
        } else {
            holder.binding.tvAttendanceStatus.setText("Holiday");
            holder.binding.lnMin.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#D1D803")));
        }


    }

    @Override
    public int getItemCount() {
        return itemList.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AttendanceMonthlyRawBinding binding;

        public ViewHolder(@NonNull AttendanceMonthlyRawBinding binding) {
            super(binding.lnMin);
            this.binding = binding;
        }
    }
}