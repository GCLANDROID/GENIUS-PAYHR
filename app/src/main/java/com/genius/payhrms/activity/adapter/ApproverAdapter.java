package com.genius.payhrms.activity.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;
import com.genius.payhrms.activity.MultipleDocumentView.MultipleDocumentViewActivity;
import com.genius.payhrms.activity.leaveapplication.ApproverFragment;
import com.genius.payhrms.activity.model.ApprovalModel;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.genius.payhrms.activity.MultipleDocumentView.LeaveDetailsActivity;

import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.GZIPOutputStream;

public class ApproverAdapter  extends RecyclerView.Adapter<ApproverAdapter.MyViewHolder> {
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(ApproverAdapter.class);
    ArrayList<ApprovalModel>itemList=new ArrayList<>();
    Fragment context;
    Context mContex;
    Pref pref;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.approver_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final Pref pref=new Pref(mContex);
        final ProgressDialog pd=new ProgressDialog(mContex);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        final ApprovalModel approvalModel = itemList.get(i);

        myViewHolder.tvStrtDate.setText(itemList.get(i).getStartDate());
        myViewHolder.tvEndDate.setText(itemList.get(i).getEndDate());
        myViewHolder.tvValue.setText(itemList.get(i).getValue());

        if (itemList.get(i).getApprovalStatus().equals("Pending") ||itemList.get(i).getApprovalStatus().contains("Cancel") ){
            myViewHolder.llTick.setVisibility(View.VISIBLE);
            myViewHolder.llGreen.setVisibility(View.GONE);
            myViewHolder.llYellow.setVisibility(View.GONE);
        }else {
            myViewHolder.llTick.setVisibility(View.GONE);
            myViewHolder.llGreen.setVisibility(View.GONE);
            myViewHolder.llYellow.setVisibility(View.GONE);
        }
        if (approvalModel.isSelected()){
            myViewHolder.imgTick.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.imgTick.setVisibility(View.GONE);
        }

        myViewHolder.llTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                approvalModel.setSelected(!approvalModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (approvalModel.isSelected()) {

                    myViewHolder.imgTick.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();

                    ((ApproverFragment) context).updateAttendanceStatus(i, true );



                } else {
                    /*myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.GONE);*/
                    myViewHolder.imgTick.setVisibility(View.GONE);
                    ((ApproverFragment) context).updateAttendanceStatus(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }


            }
        });

        if (pref.getLanguage().equals("hi")){
            myViewHolder.tvName.setText("कर्मचारी का नाम:");
            myViewHolder.tvType.setText("छुट्टी का प्रकार:");
            myViewHolder.tvleaveStrtDate.setText("आरंभ तिथि:");
            myViewHolder.tvLeaveEndDate.setText("अंतिम तिथि:");
            myViewHolder.tvLeaveValue.setText("मूल्य:");
            myViewHolder.tvLeaveReason.setText("कारण:");
        }else {
            myViewHolder.tvName.setText("Emp. Name:");
            myViewHolder.tvType.setText("Leave:");
            myViewHolder.tvleaveStrtDate.setText("Start date:");
            myViewHolder.tvLeaveEndDate.setText("End date:");
            myViewHolder.tvLeaveValue.setText("Value:");
            myViewHolder.tvLeaveReason.setText("Reason:");

        }

        //empname

        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler2 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(itemList.get(i).getEmpName(),
                                    Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvEmpName.setText(hLoc);


                        }
                    });
                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pd.show();


                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    pd.dismiss();


                }


            }.execute();
        } else {
            myViewHolder.tvEmpName.setText(itemList.get(i).getEmpName());
        }

        //type

        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler2 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(itemList.get(i).getLeave(),
                                    Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvLeaveType.setText(hLoc);


                        }
                    });
                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();



                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);



                }


            }.execute();
        } else {
            myViewHolder.tvLeaveType.setText(itemList.get(i).getLeave());
        }

        //reason

        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler2 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(itemList.get(i).getReason(),
                                    Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvReason.setText(hLoc);


                        }
                    });
                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();



                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);



                }


            }.execute();
        } else {
            myViewHolder.tvReason.setText(itemList.get(i).getReason());
        }

        if (itemList.get(i).getApprovalStatus().contains("Cancel")){
            myViewHolder.tvStatus.setText("Cancel request from applicant");
            myViewHolder.llTick.setVisibility(View.GONE);
            myViewHolder.lnDelete.setVisibility(View.VISIBLE);
        }else if (itemList.get(i).getApprovalStatus().contains("Approved")){
            myViewHolder.tvStatus.setText(itemList.get(i).getApprovalStatus());
            myViewHolder.llTick.setVisibility(View.GONE);
            myViewHolder.lnDelete.setVisibility(View.GONE);
        }else if (itemList.get(i).getApprovalStatus().contains("Rejected")){
            myViewHolder.tvStatus.setText(itemList.get(i).getApprovalStatus());
            myViewHolder.llTick.setVisibility(View.GONE);
            myViewHolder.lnDelete.setVisibility(View.GONE);
        }else {
            myViewHolder.tvStatus.setText(itemList.get(i).getApprovalStatus());
            myViewHolder.llTick.setVisibility(View.VISIBLE);
            myViewHolder.lnDelete.setVisibility(View.VISIBLE);
        }


        if (itemList.get(i).getIsLink()==1){
            myViewHolder.tvDocument.setVisibility(View.VISIBLE);
        }else {
            myViewHolder.tvDocument.setVisibility(View.GONE);
        }

        myViewHolder.tvDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //((ApproverFragment)context).imageAlert(itemList.get(i).getDocumentlink());
                // Compressing a large string
               Log.e("onClick", "onClick: "+itemList.get(i).getDocumentlink());
               pref.saveLargeData(itemList.get(i).getDocumentlink());
               Intent intent = new Intent(mContex, MultipleDocumentViewActivity.class);
               mContex.startActivity(intent);
            }
        });


        myViewHolder.lnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ApproverFragment)context).deleteFunction(itemList.get(i).getmId());
            }
        });



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmpName,tvLeaveType,tvStrtDate,tvEndDate,tvValue,tvReason,tvStatus,tvDocument;
        LinearLayout llTick,llGreen,llYellow,lnDelete;
        ImageView imgTick;
        TextView tvLeaveReason,tvLeaveValue,tvLeaveEndDate,tvleaveStrtDate,tvType,tvName;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvLeaveType=(TextView)itemView.findViewById(R.id.tvLeaveType);
            tvStrtDate=(TextView)itemView.findViewById(R.id.tvStrtDate);
            tvValue=(TextView)itemView.findViewById(R.id.tvValue);
            tvEndDate=(TextView)itemView.findViewById(R.id.tvEndDate);
            tvReason=(TextView)itemView.findViewById(R.id.tvReason);
            tvDocument=(TextView)itemView.findViewById(R.id.tvDocument);
            imgTick=(ImageView)itemView.findViewById(R.id.imgTick);
            llTick=(LinearLayout)itemView.findViewById(R.id.llTick);
            llGreen=(LinearLayout)itemView.findViewById(R.id.llGreen);
            llYellow=(LinearLayout)itemView.findViewById(R.id.llYellow);
            lnDelete=(LinearLayout)itemView.findViewById(R.id.lnDelete);

            tvLeaveReason=(TextView)itemView.findViewById(R.id.tvLeaveReason);
            tvLeaveValue=(TextView)itemView.findViewById(R.id.tvLeaveValue);
            tvLeaveEndDate=(TextView)itemView.findViewById(R.id.tvLeaveEndDate);
            tvleaveStrtDate=(TextView)itemView.findViewById(R.id.tvleaveStrtDate);
            tvType=(TextView)itemView.findViewById(R.id.tvType);
            tvName=(TextView)itemView.findViewById(R.id.tvName);
            tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);


        }
    }

    public ApproverAdapter(ArrayList<ApprovalModel> itemList, Fragment context,Context mContext) {
        this.itemList = itemList;
        this.context = context;
        this.mContex=mContext;
        pref = new Pref(mContex);
    }
}
