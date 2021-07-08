package com.genius.hrms.activity.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
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

import com.genius.hrms.R;
import com.genius.hrms.activity.leaveapplication.SpecialHolidayApplicationFragment;
import com.genius.hrms.activity.model.SpecialHolidayApprovalReportModel;
import com.genius.hrms.activity.model.SpecialHolidayItemModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class SpecialHolidayApprovalReportAdapter extends RecyclerView.Adapter<SpecialHolidayApprovalReportAdapter.MyViewHolder> {
    ArrayList<SpecialHolidayApprovalReportModel> itemList = new ArrayList<>();
    Fragment context;
    Context context1;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.specialholidayapprovalreport_raw, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

        final Pref pref=new Pref(context1);
        final ProgressDialog pd=new ProgressDialog(context1);
        pd.setMessage("loading..");
        pd.setCancelable(false);

        if (pref.getLanguage().equals("hi")) {

            final Handler textViewHandler2 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(itemList.get(i).getEmpName(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvName.setText(hLoc);


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
            myViewHolder.tvName.setText(itemList.get(i).getEmpName());


        }

        if (pref.getLanguage().equals("hi")) {

            final Handler textViewHandler2 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(itemList.get(i).getRemarks(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
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



                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);



                }


            }.execute();
        }else {
            myViewHolder.tvRemarks.setText(itemList.get(i).getRemarks());


        }

        if (pref.getLanguage().equals("hi")) {

            final Handler textViewHandler2 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(itemList.get(i).getStatus(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
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



                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);



                }


            }.execute();
        }else {
            myViewHolder.tvStatus.setText(itemList.get(i).getStatus());


        }
        myViewHolder.tvDate.setText(itemList.get(i).getDate());





    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvName,tvRemarks,tvStatus;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvRemarks = (TextView) itemView.findViewById(R.id.tvRemarks);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);





        }
    }

    public SpecialHolidayApprovalReportAdapter(ArrayList<SpecialHolidayApprovalReportModel> itemList,  Context context1) {
        this.itemList = itemList;
        this.context1=context1;
    }
}
