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

import com.genius.hrms.activity.activity.APiHitActivity;
import com.genius.hrms.activity.dailylog.NumberTourActivity;
import com.genius.hrms.activity.dailylog.OfflineDailyLogReportActivity;
import com.genius.hrms.activity.dailylog.TourViewReportActivity;
import com.genius.hrms.activity.model.NumberTourModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class NumberTourAdapter extends RecyclerView.Adapter<NumberTourAdapter.MyViewHolder> {
    ArrayList<NumberTourModel>itemList=new ArrayList<>();
    Context context;
    Pref pref;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.number_tour_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        pref=new Pref(context);
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Loding...");
        pd.setCancelable(false);
        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler13 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(itemList.get(i).getDate(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler13.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hPinchin = translation.getTranslatedText();
                            myViewHolder.tvDate.setText(hPinchin);


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
            myViewHolder.tvDate.setText(itemList.get(i).getDate());
        }

        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler1 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(itemList.get(i).getNumber(),
                                    Translate.TranslateOption.sourceLanguage("en"),   Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler1.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hPinchin = translation.getTranslatedText();
                            myViewHolder.tvNumber.setText(hPinchin);


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
            myViewHolder.tvNumber.setText(itemList.get(i).getNumber());
        }

        myViewHolder.llNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, OfflineDailyLogReportActivity.class);
                intent.putExtra("attdate",itemList.get(i).getDate());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        myViewHolder.llTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, TourViewReportActivity.class);
                intent.putExtra("attdate",itemList.get(i).getDate());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        myViewHolder.llMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, APiHitActivity.class);
                intent.putExtra("attdate",itemList.get(i).getDate());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

       double d= ((NumberTourActivity) context).distance(i);
        String sDisttance=String.format("%.3f", d);
        myViewHolder.tvDistance.setText(sDisttance+"KM");
        if (pref.getLanguage().equals("hi")){
            myViewHolder.tvLogDate.setText("लॉग डेट");
            myViewHolder.tvTotalDistance.setText("कुल दूरी");
            myViewHolder.tvVisit.setText("देखी गई जगह की संख्या");
            myViewHolder.tvMap.setText("नक्शा दृश्य");
            myViewHolder.tvTour.setText("भ्रमण दृश्य");
            myViewHolder.tvView.setText("सामान्य दृश्य");
        }else {
            myViewHolder.tvLogDate.setText("Log date");
            myViewHolder.tvTotalDistance.setText("Total distance");
            myViewHolder.tvVisit.setText("No. of visited place");
            myViewHolder.tvMap.setText("Map view");
            myViewHolder.tvTour.setText("Tour view");
            myViewHolder.tvView.setText("View");
        }

        if (pref.getSecurityCode().equals("123")){
            myViewHolder.llTour.setVisibility(View.GONE);
        }else {
            myViewHolder.llTour.setVisibility(View.VISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate,tvNumber,tvDistance;
        LinearLayout llMap,llTour,llNormal;
        TextView tvLogDate,tvVisit,tvTotalDistance,tvMap,tvTour,tvView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber=(TextView)itemView.findViewById(R.id.tvNumber);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            llNormal=(LinearLayout)itemView.findViewById(R.id.llNormal);
            llMap=(LinearLayout)itemView.findViewById(R.id.llMap);
            llTour=(LinearLayout)itemView.findViewById(R.id.llTour);
            tvDistance=(TextView)itemView.findViewById(R.id.tvDistance);

            tvLogDate=(TextView)itemView.findViewById(R.id.tvLogDate);
            tvVisit=(TextView)itemView.findViewById(R.id.tvVisit);
            tvTotalDistance=(TextView)itemView.findViewById(R.id.tvTotalDistance);
            tvMap=(TextView)itemView.findViewById(R.id.tvMap);
            tvTour=(TextView)itemView.findViewById(R.id.tvTour);
            tvView=(TextView)itemView.findViewById(R.id.tvView);

        }
    }

    public NumberTourAdapter(ArrayList<NumberTourModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
