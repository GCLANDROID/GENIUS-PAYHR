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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.hrms.R;

import com.genius.hrms.activity.model.ELearningModel;
import com.genius.hrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class ELearningAdapter extends RecyclerView.Adapter<ELearningAdapter.MyViewHolder> {
    ArrayList<ELearningModel>itemList=new ArrayList<>();
    Context context;
    Pref pref;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.elearningnew,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        pref=new Pref(context);
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Loading..");
        pd.setCancelable(false);

        myViewHolder.llLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(itemList.get(i).getImageUrl()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
         if (pref.getLanguage().equals("hi")) {
             final Handler textViewHandler3 = new Handler();
             new AsyncTask<Void, Void, Void>() {
                 @Override
                 protected Void doInBackground(Void... params) {
                     TranslateOptions options = TranslateOptions.newBuilder()
                             .setApiKey("AIzaSyDL1itt-7WRkrelJeuvOfiC-_SGc3JZ4vY")
                             .build();
                     Translate translate = options.getService();
                     final Translation translation =
                             translate.translate(itemList.get(i).getFileLabel(),
                                     Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                     textViewHandler3.post(new Runnable() {
                         @Override
                         public void run() {

                             Log.d("sssh", translation.getTranslatedText());
                             String hLoc = translation.getTranslatedText();
                             myViewHolder.tvFileName.setText(hLoc + ":");


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
             myViewHolder.tvFileName.setText(itemList.get(i).getFileLabel() + ":");
         }



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName;
        LinearLayout llLeave;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName=(TextView)itemView.findViewById(R.id.tvFileName);
            llLeave=(LinearLayout)itemView.findViewById(R.id.llLeave);

        }
    }

    public ELearningAdapter(ArrayList<ELearningModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
