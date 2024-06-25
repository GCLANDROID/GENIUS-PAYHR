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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.genius.payhrms.R;

import com.genius.payhrms.activity.leaveapplication.ApplicationFragment;
import com.genius.payhrms.activity.model.DayBreakUpModel;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class DayBreakUpAdapter extends RecyclerView.Adapter<DayBreakUpAdapter.MyViewHolder> {
    ArrayList<DayBreakUpModel> itemList = new ArrayList<>();
    Fragment context;
    Context context1;
    String leaveModeId;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.day_breakuo_raw, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final DayBreakUpModel dayModel = itemList.get(i);
        final Pref pref=new Pref(context1);
        final ProgressDialog pd=new ProgressDialog(context1);
        pd.setMessage("Loading..");
        pd.setCancelable(false);

        if (pref.getLanguage().equals("hi")) {
            myViewHolder.tvFullDay.setText("पूरा दिन");
            myViewHolder.tvFirstHalf.setText("पहली पारी");
            myViewHolder.tvScndHalf.setText("दूसरी पारी");
            final Handler textViewHandler2 = new Handler();
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyCEQyxLkrIoD2-k_185t2EUKEc8IlggaMs")
                            .build();
                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(itemList.get(i).getDateName(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvDateName.setText(hLoc);


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
            myViewHolder.tvDateName.setText(itemList.get(i).getDateName());
            myViewHolder.tvFullDay.setText("Full day");
            myViewHolder.tvFirstHalf.setText("First half");
            myViewHolder.tvScndHalf.setText("Second half");
        }

        myViewHolder.tvLeaveDate.setText(itemList.get(i).getBrkupDate());
        if (itemList.get(i).getDayAccess().equals("-1")) {
            myViewHolder.llDayBreakUp.setVisibility(View.GONE);
            myViewHolder.tvStatus.setVisibility(View.VISIBLE);
            myViewHolder.tvStatus.setText(itemList.get(i).getDayAccessDesc());

        } else {
            myViewHolder.llDayBreakUp.setVisibility(View.VISIBLE);
            myViewHolder.tvStatus.setVisibility(View.GONE);
        }

        myViewHolder.llFirstHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayModel.setSelected(!dayModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);
                if (dayModel.isSelected()) {
                    myViewHolder.imgFrstHalf.setVisibility(View.VISIBLE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.GONE);
                    itemList.get(i).setDayModeValue("1");
                    itemList.get(i).setBalance("0.5");
                    itemList.get(i).setSelected(true);
                    notifyDataSetChanged();

                    ((ApplicationFragment) context).updateStatus(i, true );



                } else {
                    /*myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.GONE);*/
                    ((ApplicationFragment) context).updateStatus(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });


        myViewHolder.llScndHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayModel.setSelected(!dayModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (dayModel.isSelected()) {

                    myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.VISIBLE);
                    myViewHolder.imgFull.setVisibility(View.GONE);
                    itemList.get(i).setDayModeValue("2");
                    itemList.get(i).setBalance("0.5");
                    itemList.get(i).setSelected(true);
                    notifyDataSetChanged();

                    ((ApplicationFragment) context).updateStatus(i, true );



                } else {
                    /*myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.GONE);*/
                    ((ApplicationFragment) context).updateStatus(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });

        myViewHolder.llFullDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayModel.setSelected(!dayModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (dayModel.isSelected()) {

                    myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.VISIBLE);
                    itemList.get(i).setDayModeValue("0");
                    itemList.get(i).setBalance("1");
                    itemList.get(i).setSelected(true);
                    notifyDataSetChanged();

                    ((ApplicationFragment) context).updateStatus(i, true );
                } else {
                    /*myViewHolder.imgFrstHalf.setVisibility(View.GONE);
                    myViewHolder.imgScndHalf.setVisibility(View.GONE);
                    myViewHolder.imgFull.setVisibility(View.GONE);*/
                    ((ApplicationFragment) context).updateStatus(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });
        if (leaveModeId.equals("0")){
            myViewHolder.llFullDay.setVisibility(View.GONE);
        }else {
            myViewHolder.llFullDay.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvLeaveDate, tvDateName, tvStatus;
        LinearLayout llDayBreakUp, llFirstHalf, llScndHalf, llFullDay;
        ImageView imgFrstHalf,imgScndHalf,imgFull;
        TextView tvFullDay,tvFirstHalf,tvScndHalf;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLeaveDate = (TextView) itemView.findViewById(R.id.tvLeaveDate);
            tvDateName = (TextView) itemView.findViewById(R.id.tvDateName);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);

            tvFullDay = (TextView) itemView.findViewById(R.id.tvFullDay);
            tvFirstHalf = (TextView) itemView.findViewById(R.id.tvFirtsHalf);
            tvScndHalf = (TextView) itemView.findViewById(R.id.tvScndHalf);

            llDayBreakUp = (LinearLayout) itemView.findViewById(R.id.llDayBreakUp);
            llFullDay = (LinearLayout) itemView.findViewById(R.id.llFullDay);
            llScndHalf = (LinearLayout) itemView.findViewById(R.id.llScndHalf);
            llFirstHalf = (LinearLayout) itemView.findViewById(R.id.llFirstHalf);

            imgFrstHalf=(ImageView)itemView.findViewById(R.id.imgFrstHalf);
            imgScndHalf=(ImageView)itemView.findViewById(R.id.imgScndHalf);
            imgFull=(ImageView)itemView.findViewById(R.id.imgFull);
        }
    }

    public DayBreakUpAdapter(ArrayList<DayBreakUpModel> itemList, Fragment context,Context context1,String leaveModeId) {
        this.itemList = itemList;
        this.context = context;
        this.context1=context1;
        this.leaveModeId=leaveModeId;
    }
}
