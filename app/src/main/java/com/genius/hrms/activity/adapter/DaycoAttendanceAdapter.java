package com.genius.hrms.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.utility.Pref;
import com.genius.hrms.databinding.DaycoAttendanceReportRawBinding;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DaycoAttendanceAdapter extends RecyclerView.Adapter<DaycoAttendanceAdapter.ViewHolder> {
    Context context;
    JSONArray itemList;

    public DaycoAttendanceAdapter(Context context, JSONArray itemList) {
        this.context=context;
        this.itemList=itemList;

    }


    @NonNull
    @Override
    public DaycoAttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DaycoAttendanceReportRawBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.dayco_attendance_report_raw, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final DaycoAttendanceAdapter.ViewHolder holder, int position) {
        final JSONObject jsonObject = itemList.optJSONObject(position);
        holder.binding.tvDate.setText(jsonObject.optString("PunchIn"));
        holder.binding.tvInTime.setText(jsonObject.optString("PunchInTime"));
        holder.binding.tvOutTime.setText(jsonObject.optString("PunchOutTime"));
        holder.binding.tvType.setText(jsonObject.optString("WorkMode"));
        holder.binding.tvClientName.setText(jsonObject.optString("Client"));
        holder.binding.tvLocation.setText(jsonObject.optString("AddressIN"));






    }

    @Override
    public int getItemCount() {
        return itemList.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        DaycoAttendanceReportRawBinding binding;
        public ViewHolder(@NonNull DaycoAttendanceReportRawBinding binding) {
            super(binding.lnDaycoMin);
            this.binding=binding;
        }
    }
}
