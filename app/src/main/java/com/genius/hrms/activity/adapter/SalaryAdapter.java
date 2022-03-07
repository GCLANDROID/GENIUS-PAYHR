package com.genius.hrms.activity.adapter;

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

import com.genius.hrms.activity.model.SalaryModule;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class SalaryAdapter extends RecyclerView.Adapter<SalaryAdapter.MyViewHolder> {
    ArrayList<SalaryModule>salryinfoList=new ArrayList<>();
    Pref pref;
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.salary_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        pref=new Pref(context);

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
                            translate.translate(salryinfoList.get(i).getMonth(),
                                    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvMonth.setText(hLoc);


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
            myViewHolder.tvMonth.setText(salryinfoList.get(i).getMonth());
        }
        myViewHolder.tvSalary.setText(salryinfoList.get(i).getAmount());


    }

    @Override
    public int getItemCount() {
        return salryinfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonth,tvSalary;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMonth=(TextView)itemView.findViewById(R.id.tvMonth);
            tvSalary=(TextView)itemView.findViewById(R.id.tvSalary);
        }
    }

    public SalaryAdapter(ArrayList<SalaryModule> salryinfoList, Context context) {
        this.salryinfoList = salryinfoList;
        this.context = context;
    }
}
