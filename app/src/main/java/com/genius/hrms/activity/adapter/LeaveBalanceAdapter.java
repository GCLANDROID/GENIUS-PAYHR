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
import com.genius.hrms.activity.model.LeaveBalanceDetailsModel;
import com.genius.hrms.activity.model.LeaveBalanceModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;

public class LeaveBalanceAdapter extends RecyclerView.Adapter<LeaveBalanceAdapter.MyViewHolder> {
    ArrayList<LeaveBalanceModel>itemList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.balance_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final Pref pref=new Pref(context);
//        final ProgressDialog progressDialog=new ProgressDialog(context);
//        progressDialog.setMessage("Loading..");
//        progressDialog.setCancelable(false);
        myViewHolder.tvTaken.setText(itemList.get(i).getTvTaken());
        myViewHolder.tvOpening.setText(itemList.get(i).getTvOpening());
        myViewHolder.tvAvailable.setText(itemList.get(i).getTvAvailable());
        myViewHolder.tvDate.setText(itemList.get(i).getTvDate());
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
                            translate.translate(itemList.get(i).getTvDate(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
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
                    //progressDialog.show();


                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    //progressDialog.dismiss();


                }


            }.execute();
            myViewHolder.tvOpeningTitle.setText("प्रारंभिक:");
            myViewHolder.tvAvailableTitle.setText("उपलब्ध:");
            myViewHolder.tvTakenTitle.setText("लिया:");
        }else {
            myViewHolder.tvDate.setText(itemList.get(i).getTvDate());
        }
//
//

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaken,tvAvailable,tvOpening,tvDate,tvOpeningTitle,tvTakenTitle,tvAvailableTitle ;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTaken=(TextView)itemView.findViewById(R.id.tvTaken);
            tvAvailable=(TextView)itemView.findViewById(R.id.tvAvailable);
            tvDate=(TextView)itemView.findViewById(R.id.tvDate);
            tvOpening=(TextView)itemView.findViewById(R.id.tvOpening);
            tvAvailableTitle=(TextView)itemView.findViewById(R.id.tvAvailableTitle);
            tvTakenTitle=(TextView)itemView.findViewById(R.id.tvTakenTitle);
            tvOpeningTitle=(TextView)itemView.findViewById(R.id.tvOpeningTitle);

        }
    }

    public LeaveBalanceAdapter(ArrayList<LeaveBalanceModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
