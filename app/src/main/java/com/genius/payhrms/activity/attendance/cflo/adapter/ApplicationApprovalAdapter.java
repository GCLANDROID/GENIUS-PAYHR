package com.genius.payhrms.activity.attendance.cflo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.attendance.cflo.model.ApplicationDetailsModel;
import com.genius.payhrms.activity.attendance.cflo.model.ApproverDetailsModel;
import com.genius.payhrms.activity.attendance.cflo.otherapplicationdetails.OtherApplicationApprovalActivity;
import com.genius.payhrms.activity.attendance.cflo.otherapplicationdetails.OtherApplicationDetailsActivity;

import java.util.ArrayList;


public class ApplicationApprovalAdapter extends RecyclerView.Adapter<ApplicationApprovalAdapter.MyViewHolder> {
    ArrayList<ApproverDetailsModel>itemList=new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cflo_application_approval_raw,viewGroup,false);

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
       myViewHolder.tvApplicant.setText(itemList.get(i).getApplicant());
       if (itemList.get(i).getApplicationType().equalsIgnoreCase("TOUR")){
           myViewHolder.llDestination.setVisibility(View.VISIBLE);
           myViewHolder.tvDestination.setText(itemList.get(i).getDestination());
       }else {
              myViewHolder.llDestination.setVisibility(View.GONE);
       }

       myViewHolder.tvApprove.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               ((OtherApplicationApprovalActivity)context).approvalApplication(itemList.get(i).getAID(),1,"Approved");
           }
       });
        myViewHolder.tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((OtherApplicationApprovalActivity)context).approvalApplication(itemList.get(i).getAID(),-1,"Rejected");
            }
        });





        //approver







    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
       TextView tvType,tvApplicationDate,tvStrtDate,tvEndDate,tvValue,tvReason,tvStatus,tvApplicant,tvDestination,tvApprove,tvReject;
       LinearLayout llDestination;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvType=itemView.findViewById(R.id.tvType);
            tvApplicationDate=itemView.findViewById(R.id.tvApplicationDate);
            tvStrtDate=itemView.findViewById(R.id.tvStrtDate);
            tvEndDate=itemView.findViewById(R.id.tvEndDate);
            tvValue=itemView.findViewById(R.id.tvValue);
            tvReason=itemView.findViewById(R.id.tvReason);
            tvStatus=itemView.findViewById(R.id.tvStatus);
            tvApplicant=itemView.findViewById(R.id.tvApplicant);
            llDestination=itemView.findViewById(R.id.llDestination);
            tvDestination=itemView.findViewById(R.id.tvDestination);
            tvReject=itemView.findViewById(R.id.tvReject);
            tvApprove=itemView.findViewById(R.id.tvApprove);
        }
    }

    public ApplicationApprovalAdapter(ArrayList<ApproverDetailsModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;

    }
}
