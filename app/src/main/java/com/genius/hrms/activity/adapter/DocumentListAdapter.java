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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;
import com.genius.hrms.activity.model.DocumentListModel;
import com.genius.hrms.activity.payroll.PayrollActivity;
import com.genius.hrms.activity.profile.DocumentActivity;
import com.genius.hrms.activity.profile.DocumentDetails;
import com.genius.hrms.activity.utility.Pref;
import com.genius.hrms.activity.utility.RecyclerItemClickListener;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;

public class DocumentListAdapter extends RecyclerView.Adapter<DocumentListAdapter.MyViewHolder> {
    ArrayList<DocumentListModel> reportList = new ArrayList<>();
    Pref pref;
    Context mContex;
    ProgressDialog pd;



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.document_raw, viewGroup, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int pos) {
        pref=new Pref(mContex);
        pd=new ProgressDialog(mContex);
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
                            translate.translate(reportList.get(pos).getHrDescription(),
                                    Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler1.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvDocumentList.setText(hLoc);


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
            myViewHolder.tvDocumentList.setText(reportList.get(pos).getHrDescription());
        }
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Intent i = new Intent(mContex,DocumentDetails.class);
            i.putExtra("documentID",reportList.get(pos).getHRManualID());

            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            mContex.startActivity(i);

            }
        });





    }



    @Override
    public int getItemCount() {
        return reportList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDocumentList;
        LinearLayout llDocumentList;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDocumentList = (TextView) itemView.findViewById(R.id.tvDocumentList);
            llDocumentList = (LinearLayout) itemView.findViewById(R.id.llDocumentList);

        }
    }


    public DocumentListAdapter(ArrayList<DocumentListModel> reportList, Context mContex) {
        this.reportList = reportList;
        this.mContex = mContex;
    }
}
