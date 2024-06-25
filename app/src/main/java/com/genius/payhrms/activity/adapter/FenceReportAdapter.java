package com.genius.payhrms.activity.adapter;

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

import com.genius.payhrms.activity.model.ReportModel;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;

public class FenceReportAdapter extends RecyclerView.Adapter<FenceReportAdapter.MyViewHolder> {
    ArrayList<ReportModel>itemList=new ArrayList<>();
    Context context;
    Pref pref;
    @NonNull
    @Override
    public FenceReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.report_item_raw, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FenceReportAdapter.MyViewHolder myViewHolder, final int i) {
        pref=new Pref(context);
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.setCancelable(true);
       // myViewHolder.tvAddress.setText(itemList.get(i).getAddress());
        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(itemList.get(i).getAddress(),
                                    Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvAddress.setText(hLoc);


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
        }else {
            myViewHolder.tvAddress.setText(itemList.get(i).getAddress());
        }
     //   myViewHolder.tvType.setText(itemList.get(i).getFenceType());
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
                            translate.translate(itemList.get(i).getFenceType(),
                                    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvType.setText(hLoc);


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
        }else {
            myViewHolder.tvType.setText(itemList.get(i).getFenceType());
        }
        myViewHolder.tvTime.setText(itemList.get(i).getLogTime());
       // myViewHolder.tvDate.setText(itemList.get(i).getLogDate());
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
                            translate.translate(itemList.get(i).getLogDate(),
                                    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler3.post(new Runnable() {
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
                    pd.show();


                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    pd.dismiss();


                }


            }.execute();
        }else {
            myViewHolder.tvDate.setText(itemList.get(i).getLogDate());
        }

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress,tvType,tvTime,tvDate;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);
            tvType=(TextView)itemView.findViewById(R.id.tvType);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
        }
    }

    public FenceReportAdapter(ArrayList<ReportModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
