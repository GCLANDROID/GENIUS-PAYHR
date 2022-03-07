package com.genius.hrms.activity.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;

import com.genius.hrms.activity.geofence.SubordinateReportDetailsActivity;
import com.genius.hrms.activity.model.Notificationmodel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    ArrayList<Notificationmodel>itemList=new ArrayList<>();
    Context context;
    Pref pref;
    ProgressDialog pd;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        pd=new ProgressDialog(context);
        pd.setMessage("loading..");
        pref=new Pref(context);
        //myViewHolder.tvLocation.setText(itemList.get(i).getLocation());
        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler3 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(itemList.get(i).getLocation(),
                                    Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler3.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvLocation.setText(hLoc);


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
                    pd.show();

                }


            }.execute();
        }else {
            myViewHolder.tvLocation.setText(itemList.get(i).getLocation());
        }
        myViewHolder.tvTotal.setText(itemList.get(i).getTotalEmp());
        myViewHolder.tvPresent.setText(itemList.get(i).getIn());
        myViewHolder.tvAbsent.setText(itemList.get(i).getOut());
        myViewHolder.tvNotMark.setText(itemList.get(i).getNotmark());
        myViewHolder.tvPresentPer.setText("("+itemList.get(i).getInPer()+"%"+")");
        myViewHolder.tvAbsentPer.setText("("+itemList.get(i).getOutper()+"%"+")");
        myViewHolder.tvNotMarkPer.setText("("+itemList.get(i).getNotmarkPer()+"%"+")");

        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler1 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(itemList.get(i).getDate(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler1.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvDate.setText(hLoc);


                        }
                    });
                    return null;
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pd.dismiss();

                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    pd.dismiss();

                }


            }.execute();
        }else {
            myViewHolder.tvDate.setText(itemList.get(i).getDate());
        }
        if (pref.getLanguage().equals("hi")) {
            myViewHolder.tvDateTitle.setText("दिनांक");
            myViewHolder.tvLocationTitle.setText("स्थान/शाखाएं");
            myViewHolder.tvTotalTitle.setText("कुल कर्मचारी");
            myViewHolder.tvPresentTitle.setText("में");
            myViewHolder.tvAbsentTitle.setText("बाहर");
            myViewHolder.tvNotMarkTitle.setText("मार्क नहीं");

        }else{
            myViewHolder.tvDateTitle.setText("Date");
            myViewHolder.tvLocationTitle.setText("Location/Branches");
            myViewHolder.tvTotalTitle.setText("Total Employee");
            myViewHolder.tvPresentTitle.setText("IN");
            myViewHolder.tvAbsentTitle.setText("OUT");
            myViewHolder.tvNotMarkTitle.setText("NOT MARK");
        }

        myViewHolder.llIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SubordinateReportDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("actionflag","1");
                intent.putExtra("branchId",itemList.get(i).getBranchID());
                intent.putExtra("date",itemList.get(i).getDate());
                context.startActivity(intent);


            }
        });

        myViewHolder.llOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SubordinateReportDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("actionflag","2");
                intent.putExtra("date",itemList.get(i).getDate());
                intent.putExtra("branchId",itemList.get(i).getBranchID());
                context.startActivity(intent);


            }
        });

        myViewHolder.llNot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SubordinateReportDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("actionflag","3");
                intent.putExtra("date",itemList.get(i).getDate());
                intent.putExtra("branchId",itemList.get(i).getBranchID());
                context.startActivity(intent);


            }
        });



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocation,tvTotal,tvPresent,tvAbsent,tvDate,tvPresentPer,tvAbsentPer,tvNotMarkPer,tvNotMark;
        TextView tvDateTitle,tvLocationTitle,tvPresentTitle,tvAbsentTitle,tvTotalTitle,tvNotMarkTitle;
        LinearLayout llIn,llOut,llNot;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvTotal=(TextView)itemView.findViewById(R.id.tvTotal);
            tvPresent=(TextView)itemView.findViewById(R.id.tvPresent);
            tvAbsent=(TextView)itemView.findViewById(R.id.tvAbsent);
            tvNotMark=(TextView)itemView.findViewById(R.id.tvNotMark);
            tvNotMarkPer=(TextView)itemView.findViewById(R.id.tvNotMarkPer);
            tvAbsentPer=(TextView)itemView.findViewById(R.id.tvAbsentPer);
            tvPresentPer=(TextView)itemView.findViewById(R.id.tvPresentPer);

            tvDateTitle=(TextView)itemView.findViewById(R.id.tvDateTitle);
            tvLocationTitle=(TextView)itemView.findViewById(R.id.tvLocationTitle);
            tvTotalTitle=(TextView)itemView.findViewById(R.id.tvTotalTitle);
            tvPresentTitle=(TextView)itemView.findViewById(R.id.tvPresentTitle);
            tvAbsentTitle=(TextView)itemView.findViewById(R.id.tvAbsentTitle);
            tvNotMarkTitle=(TextView)itemView.findViewById(R.id.tvNotMarkTitle);

            llIn=(LinearLayout)itemView.findViewById(R.id.llIn);
            llOut=(LinearLayout)itemView.findViewById(R.id.llOut);
            llNot=(LinearLayout)itemView.findViewById(R.id.llNot);


        }
    }

    public NotificationAdapter(ArrayList<Notificationmodel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
