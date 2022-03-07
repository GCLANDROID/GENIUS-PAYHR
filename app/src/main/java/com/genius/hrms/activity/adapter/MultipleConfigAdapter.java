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

import com.genius.hrms.activity.geofence.FencePointActivity;
import com.genius.hrms.activity.geofence.UpdateMulFenceConfigActivity;
import com.genius.hrms.activity.model.MulFenceConfigModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class MultipleConfigAdapter extends RecyclerView.Adapter<MultipleConfigAdapter.MyViewHolder> {
    ArrayList<MulFenceConfigModel>configList=new ArrayList<>();
    Context context;
    Pref pref;
    ProgressDialog pd;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.multiple_fencing_config_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        pref=new Pref(context);
        pd=new ProgressDialog(context);
        pd.setMessage("Loading...");
        pd.setCancelable(true);


        myViewHolder.tvNumber.setText(configList.get(i).getPointNumber());
        if (pref.getLanguage().equals("hi")){
            myViewHolder.tvAdd.setText("बिंदु जोड़ें");
        }else {
            myViewHolder.tvAdd.setText("Add point");
        }

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
                            translate.translate(configList.get(i).getAddress(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
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
            myViewHolder.tvLocation.setText(configList.get(i).getAddress());
        }

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
                             translate.translate(configList.get(i).getCraetdOn(),
                                     Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                     textViewHandler1.post(new Runnable() {
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
                     pd.dismiss();

                 }

                 @Override
                 protected void onPostExecute(Void aVoid) {
                     super.onPostExecute(aVoid);
                     pd.dismiss();

                 }


             }.execute();
         }else {
             myViewHolder.tvCreatedOn.setText(configList.get(i).getCraetdOn());
         }



        myViewHolder.llAddPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UpdateMulFenceConfigActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("fenceid",configList.get(i).getFenceId());
                context.startActivity(intent);
            }
        });
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, FencePointActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("fenceid",configList.get(i).getFenceId());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return configList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvCreatedOn,tvNumber,tvLocation,tvAdd;
        LinearLayout llAddPoint;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCreatedOn=(TextView)itemView.findViewById(R.id.tvDate);
            tvNumber=(TextView)itemView.findViewById(R.id.tvNumber);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);
            llAddPoint=(LinearLayout)itemView.findViewById(R.id.llAddPoint);
            tvAdd=(TextView)itemView.findViewById(R.id.tvAdd);

        }
    }

    public MultipleConfigAdapter(ArrayList<MulFenceConfigModel> configList, Context context) {
        this.configList = configList;
        this.context = context;
    }
}
