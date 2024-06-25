package com.genius.payhrms.activity.adapter;

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

import com.genius.payhrms.R;

import com.genius.payhrms.activity.model.PrevieModel;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.MyViewHolder> {
    ArrayList<PrevieModel>itemList=new ArrayList<>();
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.preview_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final Pref pref=new Pref(context);
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Loading..");
        pd.setCancelable(false);


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
                            translate.translate(itemList.get(i).getLeaveType(),
                                    Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvLeaveType.setText(hLoc);


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
            myViewHolder.tvLeaveType.setText(itemList.get(i).getLeaveType());
        }
        myViewHolder.tvStrtDate.setText(itemList.get(i).getStrtDate());
        myViewHolder.tvEndDate.setText(itemList.get(i).getEndDate());
        myViewHolder.tvBalance.setText(itemList.get(i).getBalance());


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
                            translate.translate(itemList.get(i).getReason(),
                                    Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvReason.setText(hLoc);


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
        } else {
            myViewHolder.tvReason.setText(itemList.get(i).getReason());
        }



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLeaveType,tvStrtDate,tvEndDate,tvBalance,tvReason;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeaveType=(TextView)itemView.findViewById(R.id.tvLeaveType);
            tvStrtDate=(TextView)itemView.findViewById(R.id.tvStrtDate);
            tvEndDate=(TextView)itemView.findViewById(R.id.tvEndDate);
            tvBalance=(TextView)itemView.findViewById(R.id.tvBalance);
            tvReason=(TextView)itemView.findViewById(R.id.tvReason);


        }
    }

    public PreviewAdapter(ArrayList<PrevieModel> itemList,Context context) {
        this.itemList = itemList;
        this.context=context;
    }
}
