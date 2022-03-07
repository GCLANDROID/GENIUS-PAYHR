package com.genius.hrms.activity.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.model.AttendanceModule;
import com.genius.hrms.activity.model.SpecialholidayReportModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class SpecialHolidayReportAdapter extends RecyclerView.Adapter<SpecialHolidayReportAdapter.MyViewHolder> {
  ArrayList<SpecialholidayReportModel>itemList=new ArrayList<>();
  Context context;

  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.specialholiday_report_raw,viewGroup,false);

    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

    final Pref pref=new Pref(context);
    final ProgressDialog pd=new ProgressDialog(context);
    pd.setMessage("Loading..");
    pd.setCancelable(false);
    if (pref.getLanguage().equals("hi")) {
      final Handler textViewHandler6 = new Handler();
      new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
          TranslateOptions options = TranslateOptions.newBuilder()
                  .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                  .build();
          Translate translate = options.getService();
          final Translation translation =
                  translate.translate(itemList.get(i).getRemarks(),
                          Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
          textViewHandler6.post(new Runnable() {
            @Override
            public void run() {

              Log.d("sssh", translation.getTranslatedText());
              String hLoc = translation.getTranslatedText();
              myViewHolder.tvRemarks.setText(hLoc);


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
      myViewHolder.tvRemarks.setText(itemList.get(i).getRemarks());
    }

    myViewHolder.tvDate.setText(itemList.get(i).getDate());





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
                  translate.translate(itemList.get(i).getStatus(),
                          Translate.TranslateOption.targetLanguage(pref.getLanguage()));
          textViewHandler2.post(new Runnable() {
            @Override
            public void run() {

              Log.d("sssh", translation.getTranslatedText());
              String hLoc = translation.getTranslatedText();
              myViewHolder.tvStatus.setText(hLoc);


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
      myViewHolder.tvStatus.setText(itemList.get(i).getStatus());
    }











    //outlocation











  }

  @Override
  public int getItemCount() {
    return itemList.size();
  }

  public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView tvDate,tvRemarks,tvStatus;

    public MyViewHolder(@NonNull View itemView) {
      super(itemView);
      tvDate=(TextView)itemView.findViewById(R.id.tvDate);
      tvRemarks=(TextView)itemView.findViewById(R.id.tvRemarks);
      tvStatus=(TextView)itemView.findViewById(R.id.tvStatus);


    }
  }

  public SpecialHolidayReportAdapter(ArrayList<SpecialholidayReportModel> itemList, Context context) {
    this.itemList = itemList;
    this.context = context;
  }
}
