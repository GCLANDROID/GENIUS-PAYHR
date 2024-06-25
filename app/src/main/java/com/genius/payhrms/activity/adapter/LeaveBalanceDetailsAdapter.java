package com.genius.payhrms.activity.adapter;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;

import com.genius.payhrms.activity.model.LeaveBalanceDetailsModel;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class LeaveBalanceDetailsAdapter extends RecyclerView.Adapter<LeaveBalanceDetailsAdapter.MyViewHolder> {
    ArrayList<LeaveBalanceDetailsModel>itemList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.leave_balance_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint("RecyclerView") final int i) {
        final Pref pref=new Pref(context);
        final ProgressDialog progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(false);
        myViewHolder.tvTaken.setText(itemList.get(i).getLeaveTaken());
        myViewHolder.tvBalance.setText(itemList.get(i).getLeaveBalance());
        myViewHolder.tvLeaveDetails.setText(itemList.get(i).getLeave());
        if (pref.getLanguage().equals("hi")) {

        }else {
            myViewHolder.tvLeaveDetails.setText(itemList.get(i).getLeave());
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaken,tvBalance,tvLeaveDetails ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaken=(TextView)itemView.findViewById(R.id.tvTaken);
            tvBalance=(TextView)itemView.findViewById(R.id.tvBalance);
            tvLeaveDetails=(TextView)itemView.findViewById(R.id.tvLeaveDetails);

        }
    }

    public LeaveBalanceDetailsAdapter(ArrayList<LeaveBalanceDetailsModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
