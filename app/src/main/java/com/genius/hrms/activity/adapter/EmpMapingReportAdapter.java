package com.genius.hrms.activity.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
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

import com.genius.hrms.activity.model.EmpMapiingReportModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class EmpMapingReportAdapter extends RecyclerView.Adapter<EmpMapingReportAdapter.MyViewHolder> {
    ArrayList<EmpMapiingReportModel>itemList=new ArrayList<>();
    Context context;
    Pref pref;


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employeemapraw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        pref=new Pref(context);
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("loading...");
        pd.setCancelable(false);
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
                            translate.translate(itemList.get(i).getEmpName(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler1.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvEmpName.setText(hLoc);


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
            myViewHolder.tvEmpName.setText(itemList.get(i).getEmpName());
        }


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmpName;
        LinearLayout rlMain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            rlMain=(LinearLayout)itemView.findViewById(R.id.rlMain);


        }
    }

    public EmpMapingReportAdapter(ArrayList<EmpMapiingReportModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
