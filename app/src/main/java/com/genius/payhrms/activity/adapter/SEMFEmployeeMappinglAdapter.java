package com.genius.payhrms.activity.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;

import com.genius.payhrms.activity.geofence.SEMFActivity;
import com.genius.payhrms.activity.model.EmployeeMapiingModule;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class SEMFEmployeeMappinglAdapter extends RecyclerView.Adapter<SEMFEmployeeMappinglAdapter.MyViewHolder> {
    ArrayList<EmployeeMapiingModule>attendanceInfoList=new ArrayList<>();
    Context context;
    Pref pref;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employeelistraw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final EmployeeMapiingModule attandanceModel = attendanceInfoList.get(i);
        pref=new Pref(context);
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Loading..");
        pd.setCancelable(true);



        myViewHolder.tvEmpId.setText(attendanceInfoList.get(i).getEmpId());
       // myViewHolder.tvEmpName.setText(attendanceInfoList.get(i).getEmpName());
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
                            translate.translate(attendanceInfoList.get(i).getEmpName(),
                                    Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
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
            myViewHolder.tvEmpName.setText(attendanceInfoList.get(i).getEmpName());
        }

        if (attendanceInfoList.get(i).getLocation().equals("")) {
            myViewHolder.tvMap.setText("");
        }else {
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
                                translate.translate(attendanceInfoList.get(i).getLocation(),
                                        Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                        textViewHandler2.post(new Runnable() {
                            @Override
                            public void run() {

                                Log.d("sssh", translation.getTranslatedText());
                                String hLoc = translation.getTranslatedText();
                                myViewHolder.tvMap.setText(hLoc);


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
                myViewHolder.tvMap.setText(attendanceInfoList.get(i).getLocation());
            }
        }



        myViewHolder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    myViewHolder.imgLike.setVisibility(View.VISIBLE);

                    ((SEMFActivity) context).updateAttendanceStatus(i, true );




            }
        });




    }

    @Override
    public int getItemCount() {
        return attendanceInfoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmpId,tvEmpName,tvMap;
        ImageView imgLike;
        LinearLayout llMain;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEmpId=(TextView)itemView.findViewById(R.id.tvEmpId);
            tvEmpName=(TextView)itemView.findViewById(R.id.tvEmpName);
            tvMap=(TextView)itemView.findViewById(R.id.tvMap);

            llMain=(LinearLayout) itemView.findViewById(R.id.rlMain);
            imgLike=(ImageView) itemView.findViewById(R.id.imgLike);
        }
    }

    public SEMFEmployeeMappinglAdapter(ArrayList<EmployeeMapiingModule> attendanceInfoList, Context context) {
        this.attendanceInfoList = attendanceInfoList;
        this.context = context;
    }

    public void updateList(ArrayList<EmployeeMapiingModule> list){
        attendanceInfoList = list;
        notifyDataSetChanged();
    }
}
