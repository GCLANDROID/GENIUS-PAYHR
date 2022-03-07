package com.genius.hrms.activity.adapter;

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

import com.genius.hrms.R;

import com.genius.hrms.activity.model.ConfigReportModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class ConfigAdapter extends RecyclerView.Adapter<ConfigAdapter.MyViewHolder> {
    ArrayList<ConfigReportModel>configList=new ArrayList<>();
    Context context;
    Pref pref;
    ProgressDialog pd;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.config_report_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        pref=new Pref(context);
        pd=new ProgressDialog(context);
        pd.setMessage("Loading....");
        pd.setCancelable(true);

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
                            translate.translate(configList.get(i).getCreatedOn(),
                                    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler3.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvCreatedOn.setText(hLoc);


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
            myViewHolder.tvCreatedOn.setText(configList.get(i).getCreatedOn());
        }


        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler4 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(configList.get(i).getAddress(),
                                    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler4.post(new Runnable() {
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
                    pd.dismiss();


                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    pd.dismiss();


                }


            }.execute();
        }else {
            myViewHolder.tvAddress.setText(configList.get(i).getAddress());
        }

       // myViewHolder.tvLocation.setText(configList.get(i).getLocationName());
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
                            translate.translate(configList.get(i).getLocationName(),
                                    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler1.post(new Runnable() {
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
                    pd.dismiss();


                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    pd.dismiss();


                }


            }.execute();
        }else {
            myViewHolder.tvLocation.setText(configList.get(i).getLocationName());
        }
        myViewHolder.tvSLat.setText(configList.get(i).getsLat()+"-"+"Lattitude");
        myViewHolder.tvSLong.setText(configList.get(i).getsLong()+"-"+"Longitude");

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
                             translate.translate(configList.get(i).getEndPoint(),
                                     Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                     textViewHandler2.post(new Runnable() {
                         @Override
                         public void run() {

                             Log.d("sssh", translation.getTranslatedText());
                             String hLoc = translation.getTranslatedText();
                             myViewHolder.tvRadius.setText(hLoc);


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
             myViewHolder.tvRadius.setText(configList.get(i).getEndPoint());
         }


    }

    @Override
    public int getItemCount() {
        return configList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCreatedOn,tvAddress,tvLocation,tvSLat,tvSLong,tvRadius;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCreatedOn=(TextView)itemView.findViewById(R.id.tvDate);
            tvAddress=(TextView)itemView.findViewById(R.id.tvAddress);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);

            tvSLat=(TextView)itemView.findViewById(R.id.tvSlat);
            tvSLong=(TextView)itemView.findViewById(R.id.tvSlong);
            tvRadius=(TextView)itemView.findViewById(R.id.tvRadius);

        }
    }

    public ConfigAdapter(ArrayList<ConfigReportModel> configList, Context context) {
        this.configList = configList;
        this.context = context;
    }
}
