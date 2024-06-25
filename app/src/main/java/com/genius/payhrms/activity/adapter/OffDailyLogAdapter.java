package com.genius.payhrms.activity.adapter;

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

import com.genius.payhrms.R;

import com.genius.payhrms.activity.model.OfflineDailyModel;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class OffDailyLogAdapter extends RecyclerView.Adapter<OffDailyLogAdapter.MyViewHolder> {
    ArrayList<OfflineDailyModel> activityList = new ArrayList<>();
    Context context;
    Pref pref;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.offline_daily_raw, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        pref=new Pref(context);
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Loading..");
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
                            translate.translate(activityList.get(i).getDate(),
                                    Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler1.post(new Runnable() {
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
                    pd.show();

                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    pd.show();

                }


            }.execute();
        }else {
            myViewHolder.tvDate.setText(activityList.get(i).getDate());
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
                            translate.translate(activityList.get(i).getInTime(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvInTime.setText(hLoc);


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
            myViewHolder.tvInTime.setText(activityList.get(i).getInTime());
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
                            translate.translate(activityList.get(i).getInLocation(),
                                    Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler3.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvInLocation.setText(hLoc);


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
            myViewHolder.tvInLocation.setText(activityList.get(i).getInLocation());
        }
        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler4 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(activityList.get(i).getOutTime(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler4.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvOutTime.setText(hLoc);


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
            myViewHolder.tvOutTime.setText(activityList.get(i).getOutTime());
        }
        if (pref.getLanguage().equals("hi")) {
            final Handler textViewHandler5 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(activityList.get(i).getOutLocation(),
                                    Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler5.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvOutLocation.setText(hLoc);


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
            myViewHolder.tvOutLocation.setText(activityList.get(i).getOutLocation());
        }
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
                             translate.translate(activityList.get(i).getInRemarks(),
                                     Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                     textViewHandler6.post(new Runnable() {
                         @Override
                         public void run() {

                             Log.d("sssh", translation.getTranslatedText());
                             String hLoc = translation.getTranslatedText();
                             myViewHolder.tvRemarksIn.setText(hLoc);


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
             myViewHolder.tvRemarksIn.setText(activityList.get(i).getInRemarks());
         }
         if (pref.getLanguage().equals("hi")) {
             final Handler textViewHandler7 = new Handler();
             new AsyncTask<Void, Void, Void>() {
                 @Override
                 protected Void doInBackground(Void... params) {
                     TranslateOptions options = TranslateOptions.newBuilder()
                             .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                             .build();
                     Translate translate = options.getService();
                     final Translation translation =
                             translate.translate(activityList.get(i).getOutRemarks(),
                                     Translate.TranslateOption.sourceLanguage("en"),  Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                     textViewHandler7.post(new Runnable() {
                         @Override
                         public void run() {

                             Log.d("sssh", translation.getTranslatedText());
                             String hLoc = translation.getTranslatedText();
                             myViewHolder.tvRemarksOut.setText(hLoc);


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
             myViewHolder.tvRemarksOut.setText(activityList.get(i).getOutRemarks());
         }



            myViewHolder.tvRemarksIn.setText(activityList.get(i).getInRemarks());



            myViewHolder.tvRemarksOut.setText(activityList.get(i).getOutRemarks());


        if (activityList.get(i).getOutTime().equals("")) {
            myViewHolder.llOutTime.setVisibility(View.GONE);
        } else {
            myViewHolder.llOutTime.setVisibility(View.VISIBLE);
        }




        myViewHolder.llInImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(activityList.get(i).getImgUrlIN()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });
        myViewHolder.llOutImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(activityList.get(i).getImgUrlOUT()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        if (activityList.get(i).getCostCenter().equals("null")||activityList.get(i).getCostCenter().equals("")){
            myViewHolder.tvCostCenter.setVisibility(View.GONE);
        }else {
            myViewHolder.tvCostCenter.setVisibility(View.VISIBLE);
        }
        myViewHolder.tvCostCenter.setText(activityList.get(i).getCostCenter());
        if (activityList.get(i).getDocUrl().equals("null")||activityList.get(i).getDocUrl().equals("")){
            myViewHolder.tvViewDoc.setVisibility(View.GONE);
        }else {
            myViewHolder.tvViewDoc.setVisibility(View.VISIBLE);
        }

        myViewHolder.tvViewDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(activityList.get(i).getDocUrl()); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvInTime, tvOutTime, tvInLocation, tvOutLocation, tvRemarksIn, tvRemarksOut,tvCostCenter,tvViewDoc;
        LinearLayout   llOutTime ,llOutImage,llInImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvInTime = (TextView) itemView.findViewById(R.id.tvInTime);
            tvOutTime = (TextView) itemView.findViewById(R.id.tvOutTime);
            tvInLocation = (TextView) itemView.findViewById(R.id.tvInLocation);
            tvOutLocation = (TextView) itemView.findViewById(R.id.tvOutLocation);
            tvRemarksIn = (TextView) itemView.findViewById(R.id.tvRemarksIn);
            tvRemarksOut = (TextView) itemView.findViewById(R.id.tvRemarksOut);
            tvCostCenter = (TextView) itemView.findViewById(R.id.tvCostCenter);
            tvViewDoc=(TextView)itemView.findViewById(R.id.tvViewDoc);

            llOutTime = (LinearLayout) itemView.findViewById(R.id.llOutTime);
            llOutImage = (LinearLayout) itemView.findViewById(R.id.llOutImage);
            llInImage = (LinearLayout) itemView.findViewById(R.id.llInImage);




        }
    }

    public OffDailyLogAdapter(ArrayList<OfflineDailyModel> activityList, Context context) {
        this.activityList = activityList;
        this.context = context;
    }
}
