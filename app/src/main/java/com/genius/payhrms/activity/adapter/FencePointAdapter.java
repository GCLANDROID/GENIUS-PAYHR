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

import com.genius.payhrms.activity.model.FencePointModel;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class FencePointAdapter extends RecyclerView.Adapter<FencePointAdapter.MyViewHolder> {
    ArrayList<FencePointModel>itemList=new ArrayList<>();
    Context context;
    Pref pref;
    ProgressDialog pd;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fence_point_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        pref=new Pref(context);
        pd=new ProgressDialog(context);
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
                             translate.translate(itemList.get(i).getAddress(),
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
                     pd.show();

                 }

                 @Override
                 protected void onPostExecute(Void aVoid) {
                     super.onPostExecute(aVoid);
                     pd.dismiss();

                 }


             }.execute();
         }else {
             myViewHolder.tvLocation.setText(itemList.get(i).getAddress());
         }



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLocation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLocation=(TextView)itemView.findViewById(R.id.tvLocation);



        }
    }

    public FencePointAdapter(ArrayList<FencePointModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
