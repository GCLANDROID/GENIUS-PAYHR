package com.genius.hrms.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.attendance.TeamAttendanceReportActivity;
import com.genius.hrms.activity.dailylog.DailyLogReportActivity;
import com.genius.hrms.activity.dailylog.TabularTeamAttendaceReportActivity;
import com.genius.hrms.activity.model.DailyLogModel;
import com.genius.hrms.activity.model.TeamEmpModel;
import com.genius.hrms.activity.model.TeamReportModel;
import com.genius.hrms.activity.utility.Pref;

import java.util.ArrayList;


public class TeampEmpAdapter extends RecyclerView.Adapter<TeampEmpAdapter.MyViewHolder> {
    ArrayList<TeamEmpModel>itemList=new ArrayList<>();
    Context context;
    Pref pref;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.team_emp_row,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        pref=new Pref(context);
        myViewHolder.tvEmp.setText(itemList.get(i).getEmpName());
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pref.getSecurityCode().equals("1163")) {
                    Intent intent = new Intent(context, TabularTeamAttendaceReportActivity.class);
                    intent.putExtra("empID", itemList.get(i).getEmpID());
                    intent.putExtra("empName", itemList.get(i).getEmpName());
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, TeamAttendanceReportActivity.class);
                    intent.putExtra("empID", itemList.get(i).getEmpID());
                    intent.putExtra("empName", itemList.get(i).getEmpName());
                    context.startActivity(intent);
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmp;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmp=(TextView) itemView.findViewById(R.id.tvEmp);


        }
    }

    public TeampEmpAdapter(ArrayList<TeamEmpModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }


    public void updateList(ArrayList<TeamEmpModel> list){
        itemList = list;
        notifyDataSetChanged();
    }
}
