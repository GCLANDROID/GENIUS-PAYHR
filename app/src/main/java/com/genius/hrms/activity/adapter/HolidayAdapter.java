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

import com.genius.hrms.activity.model.HoliDayModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.MyViewHolder> {
    ArrayList<HoliDayModel>holidayList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.holiday_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final Pref pref=new Pref(context);
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Loading..");
        pd.setCancelable(true);
        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler3 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(holidayList.get(i).getPurpose(),
                                    Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler3.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvPurpose.setText(hLoc);


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
            myViewHolder.tvPurpose.setText(holidayList.get(i).getPurpose());
        }


        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler3 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(holidayList.get(i).getHolidayDay(),
                                    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler3.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvDay.setText(hLoc);


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
            myViewHolder.tvDay.setText(holidayList.get(i).getHolidayDay());
        }



        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler3 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(holidayList.get(i).getHolidayDate(),
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
            myViewHolder.tvDate.setText(holidayList.get(i).getHolidayDate());
        }






    }

    @Override
    public int getItemCount() {
        return holidayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvPurpose,tvDay;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvPurpose=(TextView)itemView.findViewById(R.id.tvPurpose);
            tvDay=(TextView)itemView.findViewById(R.id.tvDay);


        }
    }

    public HolidayAdapter(ArrayList<HoliDayModel> holidayList, Context context) {
        this.holidayList = holidayList;
        this.context = context;
    }
}
