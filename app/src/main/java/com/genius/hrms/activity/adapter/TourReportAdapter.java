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

import com.genius.hrms.activity.model.VisitingLocationModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class TourReportAdapter extends RecyclerView.Adapter<TourReportAdapter.MyViewHolder> {
    ArrayList<VisitingLocationModel>itemList=new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.visiting_loaction_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final Pref pref=new Pref(context);
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
                            translate.translate(itemList.get(i).getLocation(),
                                    Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
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

                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);

                }


            }.execute();
        }else {
            myViewHolder.tvLocation.setText(itemList.get(i).getLocation());
        }



        myViewHolder.tvTime.setText(itemList.get(i).getTime()+"-"+"Check-In");


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocation,tvTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            tvTime=(TextView)itemView.findViewById(R.id.tvTime);


        }
    }

    public TourReportAdapter(ArrayList<VisitingLocationModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
