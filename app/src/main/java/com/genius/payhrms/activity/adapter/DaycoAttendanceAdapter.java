package com.genius.payhrms.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.databinding.DaycoAttendanceReportRawBinding;

import org.json.JSONArray;
import org.json.JSONObject;

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
        holder.binding.tvDate.setText(jsonObject.optString("EmpAttendanceDate"));
        holder.binding.tvInTime.setText("In Time : "+jsonObject.optString("EmpInTime"));
        holder.binding.tvOutTime.setText("Out Time : "+jsonObject.optString("EmpOutTime"));
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
