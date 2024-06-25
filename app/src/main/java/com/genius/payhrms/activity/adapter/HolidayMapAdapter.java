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

import com.genius.payhrms.activity.attendance.HoliDayMapActivity;
import com.genius.payhrms.activity.model.HolidayMapModel;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class HolidayMapAdapter extends RecyclerView.Adapter<HolidayMapAdapter.MyViewHolder> {
    ArrayList<HolidayMapModel>itemList=new ArrayList<>();
    Context context;
    Pref pref;
    ProgressDialog pd;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.holiday_map_raw,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        pref=new Pref(context);
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage("Loading..");
        myViewHolder.llTick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myViewHolder.imgTick.getVisibility()==View.VISIBLE) {
                    myViewHolder.imgTick.setVisibility(View.GONE);
                }else {
                    myViewHolder.imgTick.setVisibility(View.VISIBLE);
                    ((HoliDayMapActivity) context).updateStatus(i);
                    ((HoliDayMapActivity) context).showStrtDatePicker();
                }
            }
        });
        myViewHolder.tvHolidayDate.setText(itemList.get(i).getHolidayDate());



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
                            translate.translate(itemList.get(i).getHolidayName(),
                                    Translate.TranslateOption.sourceLanguage("en"), Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvHolidayName.setText(hLoc);


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
            myViewHolder.tvHolidayName.setText(itemList.get(i).getHolidayName());
        }




    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvHolidayDate,tvHolidayName;
        ImageView imgTick;
        LinearLayout llTick;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHolidayDate=(TextView)itemView.findViewById(R.id.tvHolidayDate);
            tvHolidayName=(TextView)itemView.findViewById(R.id.tvHolidayName);

            imgTick=(ImageView)itemView.findViewById(R.id.imgTick);
            llTick=(LinearLayout)itemView.findViewById(R.id.llTick);

        }
    }

    public HolidayMapAdapter(ArrayList<HolidayMapModel> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }
}
