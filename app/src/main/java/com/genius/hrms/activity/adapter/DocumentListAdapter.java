package com.genius.hrms.activity.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.model.DocumentListModel;
import com.genius.hrms.activity.payroll.PayrollActivity;
import com.genius.hrms.activity.profile.DocumentActivity;
import com.genius.hrms.activity.profile.DocumentDetails;
import com.genius.hrms.activity.utility.Pref;
import com.genius.hrms.activity.utility.RecyclerItemClickListener;

import java.util.ArrayList;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.MyViewHolder> {
    ArrayList<DocumentListModel> reportList = new ArrayList<>();
    Context mContex;



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.document_raw, viewGroup, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int pos) {


        myViewHolder.tvDocumentList.setText(reportList.get(pos).getHrDescription());

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Intent i = new Intent(mContex,DocumentDetails.class);
            i.putExtra("documentID",reportList.get(pos).getHRManualID());

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            mContex.startActivity(i);

            }
        });



    }



    @Override
    public int getItemCount() {
        return reportList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDocumentList;
        LinearLayout llDocumentList;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDocumentList = (TextView) itemView.findViewById(R.id.tvDocumentList);
            llDocumentList = (LinearLayout) itemView.findViewById(R.id.llDocumentList);

        }
    }


    public DocumentListAdapter(ArrayList<DocumentListModel> reportList, Context mContex) {
        this.reportList = reportList;
        this.mContex = mContex;
    }
}
