package com.genius.hrms.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.genius.hrms.R;
import com.genius.hrms.databinding.RawBinding;

import org.json.JSONArray;
import org.json.JSONObject;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {
    Context context;
    JSONArray itemList;

    public AttendanceAdapter(Context context, JSONArray itemList) {
        this.context=context;
        this.itemList=itemList;
    }


    @NonNull
    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RawBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.raw, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendanceAdapter.ViewHolder holder, int position) {
        final JSONObject jsonObject = itemList.optJSONObject(position);
        holder.binding.tvDate.setText(jsonObject.optString("EmpAttendanceDate"));
        holder.binding.tvInTime.setText(jsonObject.optString("EmpInTime"));
        holder.binding.tvOutTime.setText(jsonObject.optString("EmpOutTime"));
        holder.binding.tvType.setText(jsonObject.optString("PunchFrom"));
        holder.binding.tvReason.setText(jsonObject.optString("PunchFromReason"));
        holder.binding.tvStatus.setText(jsonObject.optString("EmpApprovalStatus"));
        holder.binding.tvLocation.setText(jsonObject.optString("EmpInAddress"));
        holder.binding.tvOutLocation.setText(jsonObject.optString("EmpOutAddress"));
        holder.binding.tvInImage.setText(jsonObject.optString("EmpInFname"));
        holder.binding.tvOutImage.setText(jsonObject.optString("EmpOutFname"));
        holder.binding.tvNature.setText(jsonObject.optString("AttendanceNature"));

        if (jsonObject.optString("PunchFrom")==null || jsonObject.optString("PunchFrom").equals("")){
            holder.binding.llType.setVisibility(View.GONE);
        }else {
            holder.binding.llType.setVisibility(View.VISIBLE);
        }

        if (jsonObject.optString("PunchFromReason")==null || jsonObject.optString("PunchFromReason").equals("")){
            holder.binding.llReason.setVisibility(View.GONE);
        }else {
            holder.binding.llReason.setVisibility(View.VISIBLE);
        }

        if (jsonObject.optString("AttendanceNature")==null || jsonObject.optString("AttendanceNature").equals("")){
            holder.binding.tvNature.setVisibility(View.GONE);
        }else {
            holder.binding.tvNature.setVisibility(View.VISIBLE);

            holder.binding.llInImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (jsonObject.optString("EmpInFnameUrl")!=null ||!jsonObject.optString("EmpInFnameUrl").equals("")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(jsonObject.optString("EmpInFnameUrl")));
                        context.startActivity(browserIntent);

                    }
                }
            });

            holder.binding.llInImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (jsonObject.optString("EmpOutFnameUrl")!=null ||!jsonObject.optString("EmpOutFnameUrl").equals("")) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(jsonObject.optString("EmpOutFnameUrl")));
                        context.startActivity(browserIntent);

                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return itemList.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RawBinding binding;
        public ViewHolder(@NonNull RawBinding binding) {
            super(binding.lnMin);
            this.binding=binding;
        }
    }
}
