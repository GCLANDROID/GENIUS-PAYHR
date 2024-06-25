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
import com.genius.payhrms.activity.leaveapplication.SpecialHolidayApprovalManageFragment;
import com.genius.payhrms.activity.model.SpecialHolidayApprovalItemModel;
import com.genius.payhrms.activity.utility.Pref;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;

import java.util.ArrayList;


public class SpecialHolidayAppovalItemAdapter extends RecyclerView.Adapter<SpecialHolidayAppovalItemAdapter.MyViewHolder> {
    ArrayList<SpecialHolidayApprovalItemModel> itemList = new ArrayList<>();
    Fragment context;
    Context context1;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.specialholidayapprovalitem_raw, viewGroup, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        final SpecialHolidayApprovalItemModel dayModel = itemList.get(i);
        final Pref pref=new Pref(context1);
        final ProgressDialog pd=new ProgressDialog(context1);
        pd.setMessage("loading..");
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
                            translate.translate(itemList.get(i).getName(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvName.setText(hLoc);


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
            myViewHolder.tvName.setText(itemList.get(i).getName());


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
                            translate.translate(itemList.get(i).getRemarks(),
                                    Translate.TranslateOption.sourceLanguage("en"),    Translate.TranslateOption.targetLanguage(pref.getLanguage()));
                    textViewHandler2.post(new Runnable() {
                        @Override
                        public void run() {

                            Log.d("sssh", translation.getTranslatedText());
                            String hLoc = translation.getTranslatedText();
                            myViewHolder.tvRemarks.setText(hLoc);


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
        }else {
            myViewHolder.tvRemarks.setText(itemList.get(i).getRemarks());


        }

        myViewHolder.tvDate.setText(itemList.get(i).getDate());


      if (dayModel.isSelected()){
          myViewHolder.imgLike.setVisibility(View.VISIBLE);
      }else {
          myViewHolder.imgLike.setVisibility(View.GONE);
      }

        myViewHolder.llClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dayModel.setSelected(!dayModel.isSelected());
                // holder.view.setBackgroundColor(attandanceModel.isSelected() ? Color.CYAN : Color.WHITE);

                if (dayModel.isSelected()) {

                    myViewHolder.imgLike.setVisibility(View.VISIBLE);

                    itemList.get(i).setSelected(true);
                    notifyDataSetChanged();

                    ((SpecialHolidayApprovalManageFragment) context).updateStatus(i, true );



                } else {
                    myViewHolder.imgLike.setVisibility(View.GONE);

                    ((SpecialHolidayApprovalManageFragment) context).updateStatus(i, false);
                    itemList.get(i).setSelected(false);
                    notifyDataSetChanged();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvName,tvRemarks;
        LinearLayout llClick;
        ImageView imgLike;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvRemarks = (TextView) itemView.findViewById(R.id.tvRemarks);

            llClick = (LinearLayout) itemView.findViewById(R.id.llClick);

            imgLike=(ImageView)itemView.findViewById(R.id.imgLike);

        }
    }

    public SpecialHolidayAppovalItemAdapter(ArrayList<SpecialHolidayApprovalItemModel> itemList, Fragment context, Context context1) {
        this.itemList = itemList;
        this.context = context;
        this.context1=context1;
    }
}
