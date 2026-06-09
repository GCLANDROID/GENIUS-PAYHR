package com.genius.payhrms.activity.attendance.cflo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.attendance.cflo.model.ApplicationDetailsModel;
import com.genius.payhrms.activity.attendance.cflo.otherapplicationdetails.OtherApplicationDetailsActivity;
import com.genius.payhrms.activity.leaveapplication.DetailsFragment;
import com.genius.payhrms.activity.model.LeaveDetailsModel;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class ApplicationDetailsAdapter extends RecyclerView.Adapter<ApplicationDetailsAdapter.MyViewHolder> {
    ArrayList<ApplicationDetailsModel>itemList=new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cflo_application_details_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {



        myViewHolder.tvType.setText(itemList.get(i).getApplicationType());
        myViewHolder.tvApplicationDate.setText(itemList.get(i).getApplicationDate());
        myViewHolder.tvStrtDate.setText(itemList.get(i).getStartDate());
        myViewHolder.tvEndDate.setText(itemList.get(i).getEndDate());
        myViewHolder.tvReason.setText(itemList.get(i).getReason());
        myViewHolder.tvStatus.setText(itemList.get(i).getApporvalStatus());
        myViewHolder.tvApprover.setText(itemList.get(i).getApproverName());
        myViewHolder.tvAppDate.setText(itemList.get(i).getActionTakenDate());



        if (itemList.get(i).getIsdelete()==0){
            myViewHolder.imgDelete.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.imgDelete.setVisibility(View.GONE);
        }

        //approver



        myViewHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OtherApplicationDetailsActivity)context).deleteApplication(itemList.get(i).getAID());
            }
        });




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       TextView tvType,tvApplicationDate,tvStrtDate,tvEndDate,tvValue,tvReason,tvStatus,tvAppDate,tvApprover;
        ImageView imgDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imgDelete=(ImageView)itemView.findViewById(R.id.imgDelete);
            tvType=itemView.findViewById(R.id.tvType);
            tvApplicationDate=itemView.findViewById(R.id.tvApplicationDate);
            tvStrtDate=itemView.findViewById(R.id.tvStrtDate);
            tvEndDate=itemView.findViewById(R.id.tvEndDate);
            tvValue=itemView.findViewById(R.id.tvValue);
            tvReason=itemView.findViewById(R.id.tvReason);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvAppDate=itemView.findViewById(R.id.tvAppDate);
            tvApprover=itemView.findViewById(R.id.tvApprover);



        }
    }

    public ApplicationDetailsAdapter(ArrayList<ApplicationDetailsModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;

    }
}
