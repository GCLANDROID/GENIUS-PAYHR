package com.genius.hrms.activity.model;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;

public class DocumentDetailsAdapter extends RecyclerView.Adapter<DocumentDetailsAdapter.MyViewHolder> {

    ArrayList<DocumentDetailsModel> reportList = new ArrayList<>();
    Pref pref;
    Context mContex;


    @NonNull
    @Override
    public DocumentDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.document_details_raw, parent, false);
        return new DocumentDetailsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DocumentDetailsAdapter.MyViewHolder holder, final int position) {
        pref=new Pref(mContex);
      final ProgressDialog  pd=new ProgressDialog(mContex);
        pd.setMessage("Loading..");
        pd.setCancelable(true);

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
                            translate.translate(reportList.get(position).getManualCategory(),
                                    Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler1.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            holder.tvLeaveAttendance.setText(hLoc);


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
            holder.tvLeaveAttendance.setText(reportList.get(position).getManualCategory());
        }


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
                            translate.translate(reportList.get(position).getManualSubCategory(),
                                    Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            holder.tvLeaveApplication.setText(hLoc);


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

        } else
            {
                holder.tvLeaveApplication.setText("( " + reportList.get(position).getManualSubCategory() + " )");
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
                            translate.translate(reportList.get(position).getManualDescription(),
                                    Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler3.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            holder.tvDescriprtion.setText(hLoc);


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

        } else {

            holder.tvDescriprtion.setText(reportList.get(position).getManualDescription());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(reportList.get(position).getDocumentPath()));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                mContex.startActivity(browserIntent);
            }
        });


        if (pref.getLanguage().equals("hi")) {
            holder.tvDesp.setText("विवरण");
            holder.tvButton.setText("दस्तावेज़ देखें");
        }
        else
        {
            holder.tvDesp.setText("Description");
            holder.tvButton.setText("VIEW DOCUMENTS");
        }



    }



    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLeaveAttendance;
        TextView tvLeaveApplication;
        TextView tvDescriprtion;
        TextView tvDesp;
        TextView tvButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeaveAttendance = (TextView) itemView.findViewById(R.id.tvLeaveAttendance);
            tvLeaveApplication = (TextView) itemView.findViewById(R.id.tvLeaveApplication);
            tvDescriprtion = (TextView) itemView.findViewById(R.id.tvDescriprtion);
            tvDesp = (TextView) itemView.findViewById(R.id.tvDesp);
            tvButton = (TextView) itemView.findViewById(R.id.tvButton);
        }
    }

    public DocumentDetailsAdapter(ArrayList<DocumentDetailsModel> reportList, Context mContex) {
        this.reportList = reportList;
        this.mContex = mContex;
    }
}
